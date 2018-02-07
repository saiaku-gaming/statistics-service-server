package com.valhallagame.statisticsserviceserver.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.valhallagame.common.rabbitmq.NotificationMessage;
import com.valhallagame.common.rabbitmq.RabbitMQRouting;
import com.valhallagame.statisticsserviceclient.message.StatisticsKey;
import com.valhallagame.statisticsserviceserver.model.StatisticsHighTimer;
import com.valhallagame.statisticsserviceserver.repository.StatisticsHighTimerRepository;

@Service
public class StatisticsHighTimerService {

	private final Logger logger = LoggerFactory.getLogger(StatisticsHighTimerService.class);

	@Autowired
	private StatisticsHighTimerRepository highTimerRepository;

	@Autowired
	private RabbitTemplate rabbitTemplate;

	public StatisticsHighTimer upsertLowTimer(String characterName, StatisticsKey key, float timer) {

		StatisticsHighTimer sht = highTimerRepository.upsertHighTimer(characterName, key.name(), timer);

		NotificationMessage notificationMessage = new NotificationMessage(characterName, "statistics item added");
		notificationMessage.addData("characterName", characterName);
		notificationMessage.addData("key", sht.getKey());
		notificationMessage.addData("timer", sht.getTimer());

		logger.info("incremented and now sending");

		rabbitTemplate.convertAndSend(RabbitMQRouting.Exchange.STATISTICS.name(),
				RabbitMQRouting.Statistics.HIGH_TIMER.name(),
				notificationMessage);

		return sht;
	}

	public void deleteStatistics(String characterName) {
		highTimerRepository.deleteByCharacterName(characterName);
	}

}