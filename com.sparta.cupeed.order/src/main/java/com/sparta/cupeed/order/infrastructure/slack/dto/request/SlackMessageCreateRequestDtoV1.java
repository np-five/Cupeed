package com.sparta.cupeed.order.infrastructure.slack.dto.request;

import java.math.BigDecimal;
import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SlackMessageCreateRequestDtoV1 {

	@NotBlank(message = "주문 번호는 필수입니다.")
	private String orderNumber;

	@NotNull(message = "수령업체 ID는 필수입니다.")
	private UUID recieveCompanyId;

	@NotBlank(message = "수령업체 이름은 필수입니다.")
	private String recieveCompanyName;

	@NotNull(message = "총 주문 금액은 필수입니다.")
	private BigDecimal totalPrice;

	@NotBlank(message = "슬랙 수신자 ID는 필수입니다.")
	private String recipientSlackId;

	@NotBlank(message = "주문 상태는 필수입니다.")
	private String status;

}
