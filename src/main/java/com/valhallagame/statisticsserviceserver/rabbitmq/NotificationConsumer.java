package com.valhallagame.statisticsserviceserver.rabbitmq;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;

import com.valhallagame.common.rabbitmq.NotificationMessage;
import com.valhallagame.statisticsserviceserver.service.StatisticsCounterService;

public class NotificationConsumer {
	
	@Autowired
	StatisticsCounterService statisticsService;
	
	@RabbitListener(queues = { "#{statisticsCharacterDeleteQueue.name}" })
	public void receiveCharacterDelete(NotificationMessage message) {
		String characterName = (String) message.getData().get("characterName");
		statisticsService.deleteStatistics(characterName);
	}
}
