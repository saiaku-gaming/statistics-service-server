package com.valhallagame.statisticsserviceserver.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.valhallagame.statisticsserviceserver.model.StatisticsCounter;

public interface StatisticsCounterRepository extends JpaRepository<StatisticsCounter, Integer> {

	@Query(value="" +
			"INSERT INTO statistics_counter (character_name, key, count)" + 
			"    VALUES (:characterName, :key, :value)" + 
			"    ON CONFLICT (character_name, key) DO" + 
			"        UPDATE SET count = statistics_counter.count + EXCLUDED.count" + 
			"        WHERE statistics_counter.key = EXCLUDED.key" + 
			"        AND statistics_counter.character_name = EXCLUDED.character_name" + 
			" RETURNING statistics_counter.*;", nativeQuery = true)
	public StatisticsCounter incrementIntCounter(@Param("characterName") String characterName, @Param("key") String key, @Param("value") int value);
	
	@Transactional
	public long deleteByCharacterName(String characterName); 
}
