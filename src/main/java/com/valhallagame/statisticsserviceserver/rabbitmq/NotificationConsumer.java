package com.valhallagame.statisticsserviceserver.rabbitmq;

import com.valhallagame.common.rabbitmq.NotificationMessage;
import com.valhallagame.statisticsserviceserver.service.StatisticsIntCounterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;

public class NotificationConsumer {
	private static final Logger logger = LoggerFactory.getLogger(NotificationConsumer.class);

	@Autowired
	private StatisticsIntCounterService statisticsService;
	
	@RabbitListener(queues = { "#{statisticsCharacterDeleteQueue.name}" })
	public void receiveCharacterDelete(NotificationMessage message) {
		logger.info("Received character delete notification with message {}", message);
		String characterName = (String) message.getData().get("characterName");
		statisticsService.deleteStatistics(characterName);
	}
}
