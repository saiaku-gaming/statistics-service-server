package com.valhallagame.featserviceserver.rabbitmq;

import java.util.List;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;

import com.valhallagame.common.rabbitmq.NotificationMessage;
import com.valhallagame.featserviceserver.model.Feat;
import com.valhallagame.featserviceserver.service.FeatService;

public class NotificationConsumer {
	
	@Autowired
	FeatService featService;
	
	@RabbitListener(queues = { "#{featCharacterDeleteQueue.name}" })
	public void receiveCharacterDelete(NotificationMessage message) {
		String characterName = (String) message.getData().get("characterName");
		List<Feat> feats = featService.getFeats(characterName);
		for (Feat feat : feats) {
			featService.deleteFeat(feat);
		}
	}
}
