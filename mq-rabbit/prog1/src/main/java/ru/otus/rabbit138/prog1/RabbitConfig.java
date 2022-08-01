package ru.otus.rabbit138.prog1;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
	@Autowired
	public void setupRabbitTemplate(RabbitTemplate template) {
		template.setMessageConverter(new Jackson2JsonMessageConverter());
	}
}
