package com.valhallagame.statisticsserviceserver.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.valhallagame.statisticsserviceserver.model.StatisticsIntCounter;

public interface StatisticsIntCounterRepository extends JpaRepository<StatisticsIntCounter, Integer> {
	@Query(value = ""
			+ "INSERT INTO statistics_int_counter (character_name, key, count)"
			+ "    VALUES (:characterName, :key, :value)"
			+ "    ON CONFLICT (character_name, key) DO"
			+ "        UPDATE SET count = statistics_int_counter.count + EXCLUDED.count"
			+ "        WHERE statistics_int_counter.key = EXCLUDED.key"
			+ "        AND statistics_int_counter.character_name = EXCLUDED.character_name"
			+ " RETURNING statistics_int_counter.*;", nativeQuery = true)
	public StatisticsIntCounter incrementIntCounter(@Param("characterName") String characterName,
			@Param("key") String key, @Param("value") int value);

	@Transactional
	public long deleteByCharacterName(String characterName);
}
