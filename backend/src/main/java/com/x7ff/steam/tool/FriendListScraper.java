package com.x7ff.steam.tool;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.persistence.EntityManagerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.x7ff.steam.batch.PlayerBatchUtils;
import com.x7ff.steam.batch.profile.PlayerListWriter;
import com.x7ff.steam.domain.Player;
import com.x7ff.steam.domain.repository.PlayerRepository;
import com.x7ff.steam.domain.steam.friends.SteamFriend;
import com.x7ff.steam.domain.steam.friends.SteamFriends;
import com.x7ff.steam.domain.steam.friends.SteamFriendsList;
import com.x7ff.steam.service.steam.FetchOption;
import com.x7ff.steam.service.steam.SteamFriendListService;
import com.x7ff.steam.service.steam.SteamPlayerService;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableBatchProcessing
@EntityScan(basePackages = "com.x7ff.steam")
@ComponentScan(basePackages = "com.x7ff.steam")
public class FriendListScraper {
	private static final int PROFILE_SAVE_BATCH_SIZE = 100;

	private static final Logger logger = Logger.getLogger(FriendListScraper.class.getName());

	@Value("#{T(java.lang.Integer).parseInt('${fetchLimit:100}')}")
	private int fetchLimit;

	private int addedPlayers;

	@Inject
	private JobBuilderFactory jobBuilderFactory;

	@Inject
	private StepBuilderFactory stepBuilderFactory;

	@Inject
	private JobRepository jobRepository;

	@Inject
	private EntityManagerFactory entityManagerFactory;

	@Inject
	private PlayerListWriter playerListWriter;

	@Inject
	private SteamFriendListService friendListService;

	@Inject
	private SteamPlayerService steamPlayerService;

	@Inject
	private PlayerRepository playerRepository;

	public static void main(String[] args) {
		SpringApplication.run(FriendListScraper.class, args);
	}

	@Bean
	public Job playerProcessJob() {
		return jobBuilderFactory.get("friendlist_scraper_job")
				.repository(jobRepository)
				.incrementer(new RunIdIncrementer())
				.flow(playerProcessStep())
				.next(shutdownStep())
				.end()
				.build();
	}

	public Step playerProcessStep() {
		return stepBuilderFactory.get("process_friends")
				.<Player, List<Player>>chunk(1)
				.reader(playerReader())
				.processor(new PlayerProcessor())
				.writer(playerListWriter())
				.build();
	}

	private Step shutdownStep() {
		return stepBuilderFactory.get("shutdown")
				.tasklet((contribution, chunkContext) -> {
					System.exit(0);
					return RepeatStatus.FINISHED;
				})
				.build();
	}

	private JpaPagingItemReader<Player> playerReader() {
		return PlayerBatchUtils.getPlayerReader(1, entityManagerFactory);
	}

	private PlayerListWriter playerListWriter() {
		playerListWriter.setSaveGames(true);
		return playerListWriter;
	}

	private class PlayerProcessor implements ItemProcessor<Player, List<Player>> {

		@Override
		public List<Player> process(Player player) throws Exception {
			Preconditions.checkArgument(addedPlayers < fetchLimit, "Limit reached");

			List<Player> friends = Lists.newArrayList();

			SteamFriends steamFriendsRequest = friendListService.fetch(player.getIdentifier());
			if (steamFriendsRequest == null) {
				return friends;
			}
			SteamFriendsList friendsList = steamFriendsRequest.getFriendsList();
			if (friendsList == null) {
				return friends;
			}

			List<SteamFriend> steamFriends = friendsList.getFriends();
			int friendCount = steamFriends.size();
			int count = 1;
			logger.info("Getting " + friendCount + " friends of " + player.getDisplayName());

			for (SteamFriend friend : friendsList.getFriends()) {
				if (addedPlayers >= fetchLimit) {
					playerRepository.save(friends);
					break;
				}

				String id = friend.getSteamId();
				Player existingFriend = playerRepository.findByIdentifier(id);
				if (existingFriend == null) {
					try {
						existingFriend = steamPlayerService.fetchPlayer(null, id, FetchOption.SAVE_PLAYER);
						if (existingFriend == null) {
							continue;
						}
						logger.info("New player added: " + existingFriend.getDisplayName() + " (" + count + "/" + friendCount + ")");

						friends.add(existingFriend);
						if (friends.size() == PROFILE_SAVE_BATCH_SIZE || friends.size() == friendCount) {
							steamPlayerService.resolveProfiles(friends);
							playerRepository.save(friends);
							friends.clear();
						}

						addedPlayers++;
						count++;
					} catch (Exception e) {
						logger.log(Level.WARNING, "Failed to fetch information for player '" + id + "'", e);
					}
				}
			}
			return friends;
		}

	}

}