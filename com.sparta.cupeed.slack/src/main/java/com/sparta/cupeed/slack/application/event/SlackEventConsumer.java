package com.sparta.cupeed.slack.application.event;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.sparta.cupeed.slack.application.service.SlackServiceV1;
import com.sparta.cupeed.slack.presentation.dto.request.SlackReceiveCompanyDMCreateRequestDtoV1;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class SlackEventConsumer {

	private final SlackServiceV1 slackService;

	/**
	 * 정상 메시지 처리
	 */
	@KafkaListener(topics = "slack-order-topic", groupId = "slack-service-group")
	public void listen(Map<String, Object> event) {
		processEvent(event, false);
	}

	/**
	 * DLQ 메시지 처리
	 */
	@KafkaListener(topics = "slack-order-topic.DLT", groupId = "slack-service-group-dlt")
	public void listenDLQ(Map<String, Object> event) {
		log.warn("DLQ로 이동한 메시지 처리: {}", event);
		processEvent(event, true);
	}

	private void processEvent(Map<String, Object> event, boolean fromDLQ) {
		try {
			// UUID 변환
			UUID recieveCompanyId = event.get("recieveCompanyId") != null
				? UUID.fromString(event.get("recieveCompanyId").toString())
				: null;

			// BigDecimal 변환 (Integer, Double 등 모두 대응)
			Object totalPriceObj = event.get("totalPrice");
			BigDecimal totalPrice = totalPriceObj != null
				? new BigDecimal(totalPriceObj.toString())
				: BigDecimal.ZERO;

			SlackReceiveCompanyDMCreateRequestDtoV1 requestDto = SlackReceiveCompanyDMCreateRequestDtoV1.builder()
				.orderNumber((String) event.get("orderNumber"))
				.recieveCompanyId(recieveCompanyId)
				.recieveCompanyName((String) event.get("recieveCompanyName"))
				.totalPrice(totalPrice)
				.recipientSlackId((String) event.get("recipientSlackId"))
				.status((String) event.get("status"))
				.build();

			// Slack 전송. fromDLQ 플래그로 재시도 여부를 로깅할 수도 있음
			slackService.createDMToReciveCompany(null, requestDto);

		} catch (Exception e) {
			log.error("[{}] Slack DM 전송 실패: {}", fromDLQ ? "DLQ" : "NORMAL", e.getMessage(), e);
			// DLQ에 다시 보내거나 수동 조치 필요 시 여기서 처리
		}
	}
}
