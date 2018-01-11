package com.valhallagame.statisticsserviceserver.controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.JsonNode;
import com.valhallagame.characterserviceclient.CharacterServiceClient;
import com.valhallagame.characterserviceclient.model.CharacterData;
import com.valhallagame.common.JS;
import com.valhallagame.common.RestResponse;
import com.valhallagame.common.rabbitmq.NotificationMessage;
import com.valhallagame.common.rabbitmq.RabbitMQRouting;
import com.valhallagame.statisticsserviceclient.message.AddStatisticsParameter;
import com.valhallagame.statisticsserviceclient.message.DebugAddStatisticsParameter;
import com.valhallagame.statisticsserviceclient.message.GetStatisticssParameter;
import com.valhallagame.statisticsserviceserver.model.Statistics;
import com.valhallagame.statisticsserviceserver.service.StatisticsService;

@Controller
@RequestMapping(path = "/v1/statistics")
public class StatisticsController {

	@Autowired
	private RabbitTemplate rabbitTemplate;

	@Autowired
	private StatisticsService statisticsService;

	@Autowired
	private CharacterServiceClient characterServiceClient;

	@RequestMapping(path = "/get-statisticss", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<JsonNode> getStatisticss(@Valid @RequestBody GetStatisticssParameter input) {
		List<Statistics> Statisticss = statisticsService.getStatisticss(input.getCharacterName());
		List<String> items = Statisticss.stream().map(Statistics::getName).collect(Collectors.toList());
		return JS.message(HttpStatus.OK, items);
	}

	@RequestMapping(path = "/debug-add-statistics", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<JsonNode> addStatistics(@Valid @RequestBody DebugAddStatisticsParameter input) throws IOException {
		RestResponse<CharacterData> characterResp = characterServiceClient
				.getCharacterWithoutOwnerValidation(input.getUsername().toLowerCase());
		Optional<CharacterData> characterOpt = characterResp.get();
		if (characterOpt.isPresent()) {
			AddStatisticsParameter newItemParam = new AddStatisticsParameter(characterOpt.get().getCharacterName(),
					input.getItemName());
			return addStatistics(newItemParam);
		} else {
			return JS.message(characterResp);
		}
	}

	@RequestMapping(path = "/add-statistics", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<JsonNode> addStatistics(@Valid @RequestBody AddStatisticsParameter input) {

		// Duplicate protection
		List<Statistics> Statisticss = statisticsService.getStatisticss(input.getCharacterName());
		List<String> items = Statisticss.stream().map(Statistics::getName).collect(Collectors.toList());
		if (items.contains(input.getStatisticsName())) {
			return JS.message(HttpStatus.ALREADY_REPORTED, "Already in store");
		}

		statisticsService.saveStatistics(new Statistics(input.getStatisticsName(), input.getCharacterName()));
		rabbitTemplate.convertAndSend(RabbitMQRouting.Exchange.STATISTICS.name(), RabbitMQRouting.Statistics.ADD_STATISTICS.name(),
				new NotificationMessage(input.getCharacterName(), "statistics item added"));

		return JS.message(HttpStatus.OK, "Statistics item added");
	}
}
