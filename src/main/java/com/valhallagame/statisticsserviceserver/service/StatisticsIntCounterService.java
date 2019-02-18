package com.valhallagame.statisticsserviceserver.service;

import com.valhallagame.characterserviceclient.CharacterServiceClient;
import com.valhallagame.characterserviceclient.model.CharacterData;
import com.valhallagame.common.RestResponse;
import com.valhallagame.common.rabbitmq.NotificationMessage;
import com.valhallagame.common.rabbitmq.RabbitMQRouting;
import com.valhallagame.statisticsserviceclient.message.StatisticsKey;
import com.valhallagame.statisticsserviceserver.model.StatisticsIntCounter;
import com.valhallagame.statisticsserviceserver.repository.StatisticsIntCounterRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Service
public class StatisticsIntCounterService {

	private final Logger logger = LoggerFactory.getLogger(StatisticsIntCounterService.class);
	
	@Autowired
	private RabbitTemplate rabbitTemplate;
	
	@Autowired
	private CharacterServiceClient characterServiceClient;
	
	@Autowired
	private StatisticsIntCounterRepository statisticsCounterRepository;

	public Optional<StatisticsIntCounter> incrementIntCounter(String characterName, StatisticsKey key, int value) throws IOException {
		logger.info("Incrementing int counter key {} value {} character {}", key, value, characterName);
		StatisticsIntCounter sc = statisticsCounterRepository.incrementIntCounter(characterName, key.name(), value);
		
		RestResponse<CharacterData> characterResp = characterServiceClient.getCharacter(characterName);
		Optional<CharacterData> characterOpt = characterResp.get();
		if(!characterOpt.isPresent()) {
			return Optional.empty();
		}
		CharacterData character = characterOpt.get();
		
		NotificationMessage notificationMessage = new NotificationMessage(character.getOwnerUsername(), "statistics item added");
		notificationMessage.addData("characterName", characterName);
		notificationMessage.addData("key", sc.getKey());
		notificationMessage.addData("count", sc.getCount());
		
		logger.info("incremented and now sending");
		
		rabbitTemplate.convertAndSend(RabbitMQRouting.Exchange.STATISTICS.name(), RabbitMQRouting.Statistics.INT_COUNTER.name(),
				notificationMessage);
		
		return Optional.of(sc);
	}

	public void deleteStatistics(String characterName) {
		logger.info("Deleting statistics for {}", characterName);
		statisticsCounterRepository.deleteByCharacterName(characterName);
	}
}
