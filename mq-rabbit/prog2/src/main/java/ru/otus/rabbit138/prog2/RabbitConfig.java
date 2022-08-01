package ru.otus.rabbit138.prog2;

import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.PooledChannelConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
	//@Bean
	/*public PooledChannelConnectionFactory connectionFactory() {
		ConnectionFactory rabbitConnectionFactory = new ConnectionFactory();
		rabbitConnectionFactory.setHost("localhost");
		return new PooledChannelConnectionFactory(rabbitConnectionFactory);
	}*/

	@Bean
	public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
		SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
		factory.setMessageConverter(new Jackson2JsonMessageConverter());
		factory.setConnectionFactory(connectionFactory);
		return factory;
	}

//	@Autowired
//	public void setupRabbitTemplate(RabbitTemplate template) {
//		template.setMessageConverter(new Jackson2JsonMessageConverter());
//	}
}
