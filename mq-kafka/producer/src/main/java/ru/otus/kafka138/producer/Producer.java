package ru.otus.kafka138.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.otus.kafka138.model.Message;

@Service
@RequiredArgsConstructor
@Slf4j
public class Producer {
	private final KafkaTemplate<String, String> kafkaTemplateString;
	private final KafkaTemplate<String, Message> kafkaTemplateMessage;

	public void sendString(String topic, int keyFrom, int keyTo, String data, int count) {
		log.info("Start send");

		for (int i = keyFrom; i < keyTo; ++i) {
			var no = i;
			for (int j = 0; j < count; ++j) {
				var no2 = j;
				kafkaTemplateString.send(topic, Integer.toString(i), "some data " + i + "-" + j)
						.addCallback(
								result -> log.info("send complete {}-{}", no, no2),
								fail -> log.error("fail send", fail.getCause()));
			}
		}

		log.info("complete send");
	}

	public void sendMessage(String topic, int keyFrom, int keyTo, String data, int count) {
		log.info("Start send");

		for (int i = keyFrom; i < keyTo; ++i) {
			for (int j = 0; j < count; ++j) {
				kafkaTemplateMessage.send(topic, Integer.toString(i),
						Message.builder().text("some data").value(i * 1000 + j).build());
			}
		}

		log.info("complete send");
	}
}
