package com.sparta.cupeed.slack.presentation.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Builder
@Schema(description = "수령업체에게 전송할 주문완료 DM 생성 요청 DTO")
public class SlackReceiveCompanyDMCreateRequestDtoV1 {

	@NotBlank(message = "주문 번호는 필수입니다.")
	@Schema(description = "주문 번호", example = "ORD-20251111001")
	private String orderNumber;

	@NotNull(message = "수령업체 ID는 필수입니다.")
	@Schema(description = "수령업체 ID", example = "d290f1ee-6c54-4b01-90e6-d701748f0851")
	private UUID recieveCompanyId;

	@NotBlank(message = "수령업체 이름은 필수입니다.")
	@Schema(description = "수령업체 이름", example = "서울 물류센터")
	private String recieveCompanyName;

	@NotNull(message = "총 주문 금액은 필수입니다.")
	@Schema(description = "총 주문 금액", example = "150000.50")
	private BigDecimal totalPrice;

	@NotBlank(message = "슬랙 수신자 ID는 필수입니다.")
	@Schema(description = "슬랙 메시지 수신자 ID", example = "U87654321")
	private String recipientSlackId;

	@NotBlank(message = "주문 상태는 필수입니다.")
	@Schema(description = "주문 상태", example = "REQUESTED")
	private String status;
}
