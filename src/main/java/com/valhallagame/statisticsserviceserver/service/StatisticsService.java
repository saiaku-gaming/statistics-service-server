package com.valhallagame.statisticsserviceserver.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.valhallagame.statisticsserviceserver.model.Statistics;
import com.valhallagame.statisticsserviceserver.repository.StatisticsRepository;

@Service
public class StatisticsService {

	@Autowired
	private StatisticsRepository statisticsItemRepository;

	public Statistics saveStatistics(Statistics statistics) {
		return statisticsItemRepository.save(statistics);
	}

	public void deleteStatistics(Statistics statistics) {
		statisticsItemRepository.delete(statistics);
	}

	public List<Statistics> getStatisticss(String characterName) {
		return statisticsItemRepository.findByCharacterOwner(characterName);
	}
}
