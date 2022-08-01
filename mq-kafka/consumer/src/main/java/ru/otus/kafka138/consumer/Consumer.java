package ru.otus.kafka138.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.boot.CommandLineRunner;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.otus.kafka138.model.Message;

@Service
@RequiredArgsConstructor
@Slf4j
public class Consumer {

	@KafkaListener(topics = "topic1", containerFactory = "kafkaListenerContainerFactoryString")
	public void listenGroupTopic1(String message) {
		log.info("Receive message {}", message);
	}

	@KafkaListener(topics = "topic2", containerFactory = "kafkaListenerContainerFactoryMessage")
	public void listenGroupTopic2(ConsumerRecord<String, Message> record) {
		log.info("Receive message {}", record);
	}
}
