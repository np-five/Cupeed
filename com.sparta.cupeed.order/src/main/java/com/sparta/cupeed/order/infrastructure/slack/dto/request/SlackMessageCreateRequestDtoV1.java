package com.sparta.cupeed.order.infrastructure.slack.dto.request;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SlackMessageCreateRequestDtoV1 {
	private String orderNumber;        // 주문 번호
	private UUID recieveCompanyId; // 수령업체 아이디 -> 수령업체 이름으로 변경
	private BigDecimal totalPrice;     // 총 주문 금액
}
