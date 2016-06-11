package com.x7ff.steam.domain.repository;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NamedQuery;
import javax.persistence.Query;

import com.x7ff.steam.domain.Player;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public class PlayerRepositoryImpl extends SimpleJpaRepository<Player, Long> {

	private final EntityManager entityManager;

	@Inject
	public PlayerRepositoryImpl(EntityManager entityManager) {
		super(Player.class, entityManager);
		this.entityManager = entityManager;
	}

	public Player findByIdentifier(String identifier) {
		try {
			return (Player) entityManager
					.createQuery("SELECT p FROM Player p WHERE p.identifier = :identifier")
					.setParameter("identifier", identifier)
					.getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}


}