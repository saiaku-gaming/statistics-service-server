package com.valhallagame.statisticsserviceserver.config;

import com.valhallagame.common.rabbitmq.RabbitMQRouting;
import com.valhallagame.common.rabbitmq.RabbitSender;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

	@Autowired
	private RabbitTemplate rabbitTemplate;

	// Statistics configs
	@Bean
	public DirectExchange statisticsExchange() {
		return new DirectExchange(RabbitMQRouting.Exchange.STATISTICS.name());
	}

	@Bean
	public Queue statisticsCharacterDelete() {
		return new Queue("statisticsCharacterDeleteQueue");
	}

	@Bean
	public Binding bindingCharacterDeleted(DirectExchange characterExchange, Queue statisticsCharacterDeleteQueue) {
		return BindingBuilder.bind(statisticsCharacterDeleteQueue).to(characterExchange)
				.with(RabbitMQRouting.Character.DELETE);
	}

	@Bean
	public Jackson2JsonMessageConverter jacksonConverter() {
		return new Jackson2JsonMessageConverter();
	}

	@Bean
	public SimpleRabbitListenerContainerFactory containerFactory() {
		SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
		factory.setMessageConverter(jacksonConverter());
		return factory;
	}

	@Bean
	public RabbitSender rabbitSender() {
		return new RabbitSender(rabbitTemplate);
	}
}
