package com.valhallagame.wardrobeserviceserver.config;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.valhallagame.common.rabbitmq.RabbitMQRouting;

@Configuration
public class RabbitMQConfig {
	// Wardrobe configs
	@Bean
	public DirectExchange wardrobeExchange() {
		return new DirectExchange(RabbitMQRouting.Exchange.WARDROBE.name());
	}

}
