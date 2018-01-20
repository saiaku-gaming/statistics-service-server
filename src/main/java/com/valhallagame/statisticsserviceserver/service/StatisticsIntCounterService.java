package com.valhallagame.statisticsserviceserver.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.valhallagame.common.rabbitmq.NotificationMessage;
import com.valhallagame.common.rabbitmq.RabbitMQRouting;
import com.valhallagame.statisticsserviceclient.message.StatisticsKey;
import com.valhallagame.statisticsserviceserver.model.StatisticsIntCounter;
import com.valhallagame.statisticsserviceserver.repository.StatisticsIntCounterRepository;

@Service
public class StatisticsIntCounterService {

	private final Logger logger = LoggerFactory.getLogger(StatisticsIntCounterService.class);
	
	@Autowired
	private RabbitTemplate rabbitTemplate;
	
	@Autowired
	private StatisticsIntCounterRepository statisticsCounterRepository;

	public StatisticsIntCounter incrementIntCounter(String characterName, StatisticsKey key, int value) {
		
		StatisticsIntCounter sc = statisticsCounterRepository.incrementIntCounter(characterName, key.name(), value);
		
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
