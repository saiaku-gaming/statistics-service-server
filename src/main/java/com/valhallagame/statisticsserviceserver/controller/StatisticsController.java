package com.valhallagame.statisticsserviceserver.controller;

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

	@Autowired
	private StatisticsIntCounterService statisticsIntService;

	@Autowired
	private StatisticsLowTimerService lowTimerService;

	@Autowired
	private StatisticsHighTimerService highTimerService;

	@RequestMapping(path = "/increment-int-counter", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<JsonNode> incrementIntCounter(@Valid @RequestBody IncrementIntCounterParameter input) {
		StatisticsIntCounter sc = statisticsIntService.incrementIntCounter(input.getCharacterName().toLowerCase(),
				input.getKey(), input.getValue());
		return JS.message(HttpStatus.OK, String.format("Updated char %s with key %s to count %s", sc.getCharacterName(),
				sc.getKey(), sc.getCount()));
	}

	@RequestMapping(path = "/update-low-timer", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<JsonNode> updateLowTimer(@Valid @RequestBody UpdateLowTimerParameter input) {
		StatisticsLowTimer slt = lowTimerService.upsertLowTimer(input.getCharacterName().toLowerCase(),
				input.getKey(), input.getTimer());
		return JS.message(HttpStatus.OK,
				String.format("Updated char %s with key %s to count %s", slt.getCharacterName(),
						slt.getKey(), slt.getTimer()));
	}

	@RequestMapping(path = "/update-high-timer", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<JsonNode> updateHighTimer(@Valid @RequestBody UpdateHighTimerParameter input) {
		StatisticsHighTimer sht = highTimerService.upsertLowTimer(input.getCharacterName().toLowerCase(),
				input.getKey(), input.getTimer());
		return JS.message(HttpStatus.OK,
				String.format("Updated char %s with key %s to count %s", sht.getCharacterName(),
						sht.getKey(), sht.getTimer()));
	}
}
