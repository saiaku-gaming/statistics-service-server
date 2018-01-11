package com.valhallagame.statisticsserviceserver.controller;

import java.io.IOException;

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
import com.valhallagame.statisticsserviceclient.message.IncrementParameter;
import com.valhallagame.statisticsserviceserver.model.StatisticsCounter;
import com.valhallagame.statisticsserviceserver.service.StatisticsCounterService;

@Controller
@RequestMapping(path = "/v1/statistics")
public class StatisticsController {

	@Autowired
	private StatisticsCounterService statisticsService;

	@RequestMapping(path = "/increment", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<JsonNode> increment(@Valid @RequestBody IncrementParameter input) throws IOException {
		StatisticsCounter sc = statisticsService.increment(input.getCharacterName(), input.getKey(), input.getValue());
		return JS.message(HttpStatus.OK, String.format("Updated char %s with key %s to count %s", sc.getCharacterName(), sc.getKey(), sc.getCount()));
	}
}
