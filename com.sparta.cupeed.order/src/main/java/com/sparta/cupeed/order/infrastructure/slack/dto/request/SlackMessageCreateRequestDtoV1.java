package com.sparta.cupeed.order.infrastructure.slack.dto.request;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SlackMessageCreateRequestDtoV1 {
	private SlackDto slack;

	@Getter
	@Builder
	public static class SlackDto {
		private String orderNumber;
		private UUID recieveCompanyId;
		private String recieveCompanyName;
		private BigDecimal totalPrice;
		private String recipientSlackId;
		private String status;
	}
}
