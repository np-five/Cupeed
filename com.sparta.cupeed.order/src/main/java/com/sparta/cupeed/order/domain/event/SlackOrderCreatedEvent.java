package com.sparta.cupeed.order.domain.event;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SlackOrderCreatedEvent {
	private String orderNumber;
	private UUID recieveCompanyId;
	private String recieveCompanyName;
	private BigDecimal totalPrice;
	private String recipientSlackId;
	private String status;
}
