package com.valhallagame.statisticsserviceserver.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.valhallagame.statisticsserviceserver.model.StatisticsLowTimer;

public interface StatisticsLowTimerRepository extends JpaRepository<StatisticsLowTimer, Integer> {
	@Query(value = ""
			+ "INSERT INTO statistics_low_timer (character_name, key, timer)"
			+ "    VALUES (:characterName, :key, :timer)"
			+ "    ON CONFLICT (character_name, key) DO"
			+ "        UPDATE SET timer = EXCLUDED.timer"
			+ "        WHERE statistics_low_timer.key = EXCLUDED.key"
			+ "        AND statistics_low_timer.character_name = EXCLUDED.character_name"
			+ "        AND EXCLUDED.timer < statistics_low_timer.timer"
			+ " RETURNING statistics_low_timer.*;", nativeQuery = true)
	public StatisticsLowTimer upsertLowTimer(@Param("characterName") String characterName,
			@Param("key") String key, @Param("timer") float timer);

	@Transactional
	public long deleteByCharacterName(String characterName);
}
