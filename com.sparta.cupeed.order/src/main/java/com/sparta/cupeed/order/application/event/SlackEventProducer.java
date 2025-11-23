package com.sparta.cupeed.order.application.event;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.sparta.cupeed.order.domain.event.SlackOrderCreatedEvent;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SlackEventProducer {
	private final KafkaTemplate<String, SlackOrderCreatedEvent> kafkaTemplate;

	public void publishOrderCreated(SlackOrderCreatedEvent event) {
		kafkaTemplate.send("slack-order-topic", event);
	}
}
