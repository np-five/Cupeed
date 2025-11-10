package com.sparta.cupeed.slack.presentation.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Builder
public class SlackCreateRequestDtoV1 {

	@NotNull(message = "슬랙 메시지를 생성해주세요.")
	@Valid
	private SlackDto slack;

	@Getter
	@Builder
	public static class SlackDto {

		@NotNull(message = "수령자 슬랙 ID를 입력해주세요.")
		private String recipientSlackId;

		@NotNull(message = "슬랙 메시지를 입력해주세요.")
		private String message;

		@NotNull(message = "주문번호를 입력해주세요.")
		private String orderNumber;

		@NotNull(message = "수령업체명을 입력해주세요.")
		private UUID recieveCompanyId;

		@NotNull(message = "수령업체명을 입력해주세요.")
		private String recieveCompanyName;

		@NotNull(message = "총 금액을 입력해주세요.")
		private BigDecimal totalPrice;
	}
}
