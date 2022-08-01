package ru.otus.kafka138.producer;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import ru.otus.kafka138.model.Message;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {
	private final static String SERVER = "localhost:9091";

	private ProducerFactory<String, String> producerFactoryString() {
		Map<String, Object> configProps = new HashMap<>();
		configProps.put(
				ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
				SERVER);
		configProps.put(
				ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
				StringSerializer.class);
		configProps.put(
				ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
				StringSerializer.class);
		return new DefaultKafkaProducerFactory<>(configProps);
	}

	@Bean
	public KafkaTemplate<String, String> kafkaTemplateString() {
		return new KafkaTemplate<>(producerFactoryString());
	}

	private ProducerFactory<String, Message> producerFactoryMessage() {
		Map<String, Object> configProps = new HashMap<>();
		configProps.put(
				ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
				SERVER);
		configProps.put(
				ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
				StringSerializer.class);
		configProps.put(
				ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
				JsonSerializer.class);
		return new DefaultKafkaProducerFactory<>(configProps);
	}

	@Bean
	public KafkaTemplate<String, Message> kafkaTemplateMessage() {
		return new KafkaTemplate<>(producerFactoryMessage());
	}

	@Bean
	public KafkaAdmin kafkaAdmin() {
		Map<String, Object> configs = new HashMap<>();
		configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, SERVER);
		return new KafkaAdmin(configs);
	}

	@Bean
	public NewTopic topic1() {
		return new NewTopic("topic1", 2, (short) 1);
	}

	@Bean
	public NewTopic topic2() {
		return new NewTopic("topic2", 1, (short) 1);
	}
}
