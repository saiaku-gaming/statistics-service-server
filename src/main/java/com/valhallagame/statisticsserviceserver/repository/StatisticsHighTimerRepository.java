package com.valhallagame.statisticsserviceserver.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.valhallagame.statisticsserviceserver.model.StatisticsHighTimer;

public interface StatisticsHighTimerRepository extends JpaRepository<StatisticsHighTimer, Integer> {
	@Query(value = ""
			+ "INSERT INTO statistics_high_timer (character_name, key, timer)"
			+ "    VALUES (:characterName, :key, :timer)"
			+ "    ON CONFLICT (character_name, key) DO"
			+ "        UPDATE SET timer = EXCLUDED.timer"
			+ "        WHERE statistics_high_timer.key = EXCLUDED.key"
			+ "        AND statistics_high_timer.character_name = EXCLUDED.character_name"
			+ "        AND EXCLUDED.timer > statistics_high_timer.timer"
			+ " RETURNING statistics_high_timer.*;", nativeQuery = true)
	public StatisticsHighTimer upsertHighTimer(@Param("characterName") String characterName,
			@Param("key") String key, @Param("timer") float timer);

	@Transactional
	public long deleteByCharacterName(String characterName);
}