package com.valhallagame.statisticsserviceserver.rabbitmq;

import java.util.List;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;

import com.valhallagame.common.rabbitmq.NotificationMessage;
import com.valhallagame.statisticsserviceserver.model.Statistics;
import com.valhallagame.statisticsserviceserver.service.StatisticsService;

public class NotificationConsumer {
	
	@Autowired
	StatisticsService statisticsService;
	
	@RabbitListener(queues = { "#{statisticsCharacterDeleteQueue.name}" })
	public void receiveCharacterDelete(NotificationMessage message) {
		String characterName = (String) message.getData().get("characterName");
		List<Statistics> statisticss = statisticsService.getStatisticss(characterName);
		for (Statistics statistics : statisticss) {
			statisticsService.deleteStatistics(statistics);
		}
	}
}
