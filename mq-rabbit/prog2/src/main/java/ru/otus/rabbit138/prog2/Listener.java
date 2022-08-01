package ru.otus.rabbit138.prog2;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import ru.otus.rabbit138.model.Query;
import ru.otus.rabbit138.model.Response;

import java.util.Map;

@Service
@Slf4j
public class Listener {
	@RabbitListener(queues = "${appl.queue-map}")
	public void listenQueue1(Map<String, Object> message) {
		log.info("Received: {}", message);
	}

	@RabbitListener(queues = "${appl.queue-any}")
	public void listenQueue2(Message message) {
		log.info("Received: {}\n{}", message, new String(message.getBody()));
	}

	@RabbitListener(queues = "operations")
	public Response makeOperation(Query query) {
		log.info("Received: {}", query);

		double result = switch (query.getOperation()) {
			case PLUS -> query.getA() + query.getB();
			case MINUS -> query.getA() - query.getB();
		};

		return Response.builder()
				.result(result)
				.build();
	}
}
