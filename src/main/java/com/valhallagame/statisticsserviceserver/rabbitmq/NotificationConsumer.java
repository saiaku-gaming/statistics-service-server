package com.valhallagame.statisticsserviceserver.rabbitmq;

import com.valhallagame.common.rabbitmq.NotificationMessage;
import com.valhallagame.statisticsserviceserver.service.StatisticsIntCounterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.UUID;

public class NotificationConsumer {
	private static final Logger logger = LoggerFactory.getLogger(NotificationConsumer.class);

	@Autowired
	private StatisticsIntCounterService statisticsService;

	@Value("${spring.application.name}")
	private String appName;

	@RabbitListener(queues = { "#{statisticsCharacterDeleteQueue.name}" })
	public void receiveCharacterDelete(NotificationMessage message) {
		MDC.put("service_name", appName);
		MDC.put("request_id", message.getData().get("requestId") != null ? (String)message.getData().get("requestId") : UUID.randomUUID().toString());

		logger.info("Received character delete notification with message {}", message);

		try {
			String characterName = (String) message.getData().get("characterName");
			statisticsService.deleteStatistics(characterName);
		} catch (Exception e) {
			logger.error("Error while processing Character Delete notification", e);
		} finally {
			MDC.clear();
		}
	}
}
