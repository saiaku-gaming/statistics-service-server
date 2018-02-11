package com.valhallagame.statisticsserviceserver.service;

import java.io.IOException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.valhallagame.characterserviceclient.CharacterServiceClient;
import com.valhallagame.characterserviceclient.model.CharacterData;
import com.valhallagame.common.RestResponse;
import com.valhallagame.common.rabbitmq.NotificationMessage;
import com.valhallagame.common.rabbitmq.RabbitMQRouting;
import com.valhallagame.statisticsserviceclient.message.StatisticsKey;
import com.valhallagame.statisticsserviceserver.model.StatisticsLowTimer;
import com.valhallagame.statisticsserviceserver.repository.StatisticsLowTimerRepository;

@Service
public class StatisticsLowTimerService {

	private final Logger logger = LoggerFactory.getLogger(StatisticsHighTimerService.class);

	@Autowired
	private StatisticsLowTimerRepository lowTimerRepository;

	@Autowired
	private RabbitTemplate rabbitTemplate;
	
	@Autowired
	private CharacterServiceClient characterServiceClient;

	public Optional<StatisticsLowTimer> upsertLowTimer(String characterName, StatisticsKey key, float timer) throws IOException {

		StatisticsLowTimer slt = lowTimerRepository.upsertLowTimer(characterName, key.name(), timer);

		RestResponse<CharacterData> characterResp = characterServiceClient.getCharacter(characterName);
		Optional<CharacterData> characterOpt = characterResp.get();
		if(!characterOpt.isPresent()) {
			return Optional.empty();
		}
		CharacterData character = characterOpt.get();
		
		
		NotificationMessage notificationMessage = new NotificationMessage(character.getOwnerUsername(), "statistics item added");
		notificationMessage.addData("characterName", characterName);
		notificationMessage.addData("key", slt.getKey());
		notificationMessage.addData("timer", slt.getTimer());

		logger.info("incremented and now sending");

		rabbitTemplate.convertAndSend(RabbitMQRouting.Exchange.STATISTICS.name(),
				RabbitMQRouting.Statistics.LOW_TIMER.name(),
				notificationMessage);

		return Optional.of(slt);
	}

	public void deleteStatistics(String characterName) {
		lowTimerRepository.deleteByCharacterName(characterName);
	}

}
