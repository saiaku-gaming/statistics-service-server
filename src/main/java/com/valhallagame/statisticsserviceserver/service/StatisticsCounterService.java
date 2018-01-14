package com.valhallagame.statisticsserviceserver.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.valhallagame.common.rabbitmq.NotificationMessage;
import com.valhallagame.common.rabbitmq.RabbitMQRouting;
import com.valhallagame.statisticsserviceserver.model.StatisticsCounter;
import com.valhallagame.statisticsserviceserver.repository.StatisticsCounterRepository;

@Service
public class StatisticsCounterService {

	private final Logger logger = LoggerFactory.getLogger(StatisticsCounterService.class);
	
	@Autowired
	private RabbitTemplate rabbitTemplate;
	
	@Autowired
	private StatisticsCounterRepository statisticsCounterRepository;

	public StatisticsCounter incrementIntCounter(String characterName, String key, int value) {
		
		StatisticsCounter sc = statisticsCounterRepository.incrementIntCounter(characterName, key, value);
		
		NotificationMessage notificationMessage = new NotificationMessage(characterName, "statistics item added");
		notificationMessage.addData("characterName", characterName);
		notificationMessage.addData("key", sc.getKey());
		notificationMessage.addData("count", sc.getCount());
		
		logger.info("incremented and now sending");
		
		rabbitTemplate.convertAndSend(RabbitMQRouting.Exchange.STATISTICS.name(), RabbitMQRouting.Statistics.INT_COUNTER.name(),
				notificationMessage);
		
		return sc;
	}

	public void deleteStatistics(String characterName) {
		statisticsCounterRepository.deleteByCharacterName(characterName);
	}
}
