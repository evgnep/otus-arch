package ru.otus.kafka138.producer;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class Controller {
	private final Producer producer;

	@PostMapping("/string")
	public void sendString(@RequestParam String topic, @RequestParam int keyFrom, @RequestParam int keyTo, @RequestParam String data, @RequestParam int count) {
		producer.sendString(topic, keyFrom, keyTo, data, count);
	}

	@PostMapping("/message")
	public void sendMessage(@RequestParam String topic, @RequestParam int keyFrom, @RequestParam int keyTo, @RequestParam String data, @RequestParam int count) {
		producer.sendMessage(topic, keyFrom, keyTo, data, count);
	}
}
