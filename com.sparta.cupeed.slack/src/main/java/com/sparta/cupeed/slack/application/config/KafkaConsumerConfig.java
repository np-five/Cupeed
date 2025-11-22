package com.sparta.cupeed.slack.application.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

@Configuration
public class KafkaConsumerConfig {

	@Bean
	public ConsumerFactory<String, Map<String, Object>> consumerFactory() {
		Map<String, Object> props = new HashMap<>();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:29092");
		props.put(ConsumerConfig.GROUP_ID_CONFIG, "slack-service-group");
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, org.apache.kafka.common.serialization.StringDeserializer.class);
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, org.springframework.kafka.support.serializer.ErrorHandlingDeserializer.class);

		// ValueDeserializer 설정
		props.put(org.springframework.kafka.support.serializer.ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS,
			org.springframework.kafka.support.serializer.JsonDeserializer.class);
		props.put(org.springframework.kafka.support.serializer.JsonDeserializer.TRUSTED_PACKAGES, "*");
		props.put(org.springframework.kafka.support.serializer.JsonDeserializer.USE_TYPE_INFO_HEADERS, false);
		props.put(org.springframework.kafka.support.serializer.JsonDeserializer.VALUE_DEFAULT_TYPE, Map.class);

		return new DefaultKafkaConsumerFactory<>(props);
	}

	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, Map<String, Object>> kafkaListenerContainerFactory(
		KafkaTemplate<String, Object> kafkaTemplate) {

		ConcurrentKafkaListenerContainerFactory<String, Map<String, Object>> factory =
			new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(consumerFactory());

		// 재시도 3회, 3초 간격
		FixedBackOff fixedBackOff = new FixedBackOff(3000L, 3);

		// 실패 시 DLQ로 전송
		DeadLetterPublishingRecoverer recoverer = new DeadLetterPublishingRecoverer(kafkaTemplate);

		DefaultErrorHandler errorHandler = new DefaultErrorHandler(recoverer, fixedBackOff);
		factory.setCommonErrorHandler(errorHandler);

		return factory;
	}
}
