package com.valhallagame.statisticsserviceserver.controller;

import java.io.IOException;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.JsonNode;
import com.valhallagame.common.JS;
import com.valhallagame.statisticsserviceclient.message.IncrementIntCounterParameter;
import com.valhallagame.statisticsserviceclient.message.UpdateHighTimerParameter;
import com.valhallagame.statisticsserviceclient.message.UpdateLowTimerParameter;
import com.valhallagame.statisticsserviceserver.model.StatisticsHighTimer;
import com.valhallagame.statisticsserviceserver.model.StatisticsIntCounter;
import com.valhallagame.statisticsserviceserver.model.StatisticsLowTimer;
import com.valhallagame.statisticsserviceserver.service.StatisticsHighTimerService;
import com.valhallagame.statisticsserviceserver.service.StatisticsIntCounterService;
import com.valhallagame.statisticsserviceserver.service.StatisticsLowTimerService;

@Controller
@RequestMapping(path = "/v1/statistics")
public class StatisticsController {

	private static final String UPDATED_CHAR_S_WITH_KEY_S_TO_COUNT_S = "Updated char %s with key %s to count %s";

	private static final String CHARACTER_COULD_NOT_BE_FOUND = "Character %s could not be found.";

	@Autowired
	private StatisticsIntCounterService statisticsIntService;

	@Autowired
	private StatisticsLowTimerService lowTimerService;

	@Autowired
	private StatisticsHighTimerService highTimerService;

	@RequestMapping(path = "/increment-int-counter", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<JsonNode> incrementIntCounter(@Valid @RequestBody IncrementIntCounterParameter input)
			throws IOException {
		Optional<StatisticsIntCounter> scOpt = statisticsIntService
				.incrementIntCounter(input.getCharacterName().toLowerCase(), input.getKey(), input.getValue());
		if (scOpt.isPresent()) {
			StatisticsIntCounter sc = scOpt.get();
			return JS.message(HttpStatus.OK, String.format(UPDATED_CHAR_S_WITH_KEY_S_TO_COUNT_S,
					sc.getCharacterName(), sc.getKey(), sc.getCount()));
		} else {
			return JS.message(HttpStatus.BAD_REQUEST,
					String.format(CHARACTER_COULD_NOT_BE_FOUND, input.getCharacterName()));
		}
	}

	@RequestMapping(path = "/update-low-timer", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<JsonNode> updateLowTimer(@Valid @RequestBody UpdateLowTimerParameter input) throws IOException {
		Optional<StatisticsLowTimer> sltOpt = lowTimerService.upsertLowTimer(input.getCharacterName().toLowerCase(),
				input.getKey(), input.getTimer());
		if (sltOpt.isPresent()) {
			StatisticsLowTimer slt = sltOpt.get();
			return JS.message(HttpStatus.OK, String.format(UPDATED_CHAR_S_WITH_KEY_S_TO_COUNT_S,
					slt.getCharacterName(), slt.getKey(), slt.getTimer()));
		} else {
			return JS.message(HttpStatus.BAD_REQUEST,
					String.format(CHARACTER_COULD_NOT_BE_FOUND, input.getCharacterName()));
		}
	}

	@RequestMapping(path = "/update-high-timer", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<JsonNode> updateHighTimer(@Valid @RequestBody UpdateHighTimerParameter input)
			throws IOException {
		Optional<StatisticsHighTimer> shtOpt = highTimerService.upsertLowTimer(input.getCharacterName().toLowerCase(),
				input.getKey(), input.getTimer());
		if (shtOpt.isPresent()) {
			StatisticsHighTimer sht = shtOpt.get();
			return JS.message(HttpStatus.OK, String.format(UPDATED_CHAR_S_WITH_KEY_S_TO_COUNT_S,
					sht.getCharacterName(), sht.getKey(), sht.getTimer()));
		} else {
			return JS.message(HttpStatus.BAD_REQUEST,
					String.format(CHARACTER_COULD_NOT_BE_FOUND, input.getCharacterName()));
		}
	}
}
