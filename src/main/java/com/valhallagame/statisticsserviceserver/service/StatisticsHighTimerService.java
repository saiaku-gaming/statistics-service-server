package com.valhallagame.statisticsserviceserver.service;

import com.valhallagame.characterserviceclient.CharacterServiceClient;
import com.valhallagame.characterserviceclient.model.CharacterData;
import com.valhallagame.common.RestResponse;
import com.valhallagame.common.rabbitmq.NotificationMessage;
import com.valhallagame.common.rabbitmq.RabbitMQRouting;
import com.valhallagame.statisticsserviceclient.message.StatisticsKey;
import com.valhallagame.statisticsserviceserver.model.StatisticsHighTimer;
import com.valhallagame.statisticsserviceserver.repository.StatisticsHighTimerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Service
public class StatisticsHighTimerService {

	private final Logger logger = LoggerFactory.getLogger(StatisticsHighTimerService.class);

	@Autowired
	private StatisticsHighTimerRepository highTimerRepository;
	
	@Autowired
	private CharacterServiceClient characterServiceClient;

	@Autowired
	private RabbitTemplate rabbitTemplate;

	public Optional<StatisticsHighTimer> upsertLowTimer(String characterName, StatisticsKey key, float timer) throws IOException {
		logger.info("Upserting low timer with key {} timer {} character {}", key, timer, characterName);
		StatisticsHighTimer sht = highTimerRepository.upsertHighTimer(characterName, key.name(), timer);

		RestResponse<CharacterData> characterResp = characterServiceClient.getCharacter(characterName);
		Optional<CharacterData> characterOpt = characterResp.get();
		if(!characterOpt.isPresent()) {
			return Optional.empty();
		}
		CharacterData character = characterOpt.get();
		NotificationMessage notificationMessage = new NotificationMessage(character.getOwnerUsername(), "statistics item added");
		notificationMessage.addData("characterName", characterName);
		notificationMessage.addData("key", sht.getKey());
		notificationMessage.addData("timer", sht.getTimer());

		logger.info("incremented and now sending");

		rabbitTemplate.convertAndSend(RabbitMQRouting.Exchange.STATISTICS.name(),
				RabbitMQRouting.Statistics.HIGH_TIMER.name(),
				notificationMessage);

		return Optional.of(sht);
	}

	public void deleteStatistics(String characterName) {
		logger.info("Deleting statistics for {}", characterName);
		highTimerRepository.deleteByCharacterName(characterName);
	}

}