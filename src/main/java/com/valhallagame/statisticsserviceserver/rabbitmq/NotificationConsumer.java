package com.valhallagame.statisticsserviceserver.rabbitmq;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;

import com.valhallagame.common.rabbitmq.NotificationMessage;
import com.valhallagame.statisticsserviceserver.service.StatisticsIntCounterService;

public class NotificationConsumer {
	
	@Autowired
	StatisticsIntCounterService statisticsService;
	
	@RabbitListener(queues = { "#{statisticsCharacterDeleteQueue.name}" })
	public void receiveCharacterDelete(NotificationMessage message) {
		String characterName = (String) message.getData().get("characterName");
		statisticsService.deleteStatistics(characterName);
	}
}
