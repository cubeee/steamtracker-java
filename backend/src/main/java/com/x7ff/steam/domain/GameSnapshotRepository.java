package com.x7ff.steam.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface GameSnapshotRepository extends JpaRepository<GameSnapshot, Long> {

	default void persist(List<GameSnapshot> snapshots) {
		if (snapshots == null || snapshots.isEmpty()) {
			return;
		}
		save(snapshots);
	}

}