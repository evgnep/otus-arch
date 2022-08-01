package ru.otus.rabbit138.prog1;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestAttribute;
import ru.otus.rabbit138.model.Operation;
import ru.otus.rabbit138.model.Query;
import ru.otus.rabbit138.model.Response;

@Service
@RequiredArgsConstructor
public class MessageAdapter {
	private final AmqpTemplate amqp;

	public void sendMessage(String exchange, String routingKey, String text) {
		var message = MessageBuilder.withBody(text.getBytes()).build();
		amqp.send(exchange, routingKey, message);
	}

	public void sendMessage(String exchange, String routingKey, Object object) {
		amqp.convertAndSend(exchange, routingKey, object);
	}

	public double makeOperation(double a, double b, Operation operation) {
		var request = Query.builder()
				.a(a)
				.b(b)
				.operation(operation)
				.build();
		var result = amqp.convertSendAndReceiveAsType("operations", request, new ParameterizedTypeReference<Response>() {});
		return result.getResult();
	}
}
