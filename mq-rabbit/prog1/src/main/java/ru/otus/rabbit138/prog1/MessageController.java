package ru.otus.rabbit138.prog1;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.rabbit138.model.Operation;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class MessageController {
	private final MessageAdapter messageAdapter;

	@PostMapping("message/send")
	public void sendMessage(@RequestParam String exchange, @RequestParam String routingKey, @RequestBody String message) {
		if (exchange.equals("-")) exchange = "";
		messageAdapter.sendMessage(exchange, routingKey, message);
	}

	@PostMapping("message/send-ext")
	public void sendMessageExt(@RequestParam String exchange, @RequestParam String routingKey, @RequestBody Map<String, Object> message) {
		if (exchange.equals("-")) exchange = "";
		messageAdapter.sendMessage(exchange, routingKey, message);
	}

	@PostMapping("message/operation")
	public double makeOperation(@RequestParam double a, @RequestParam double b, @RequestParam Operation operation) {
		return messageAdapter.makeOperation(a, b, operation);
	}
}
