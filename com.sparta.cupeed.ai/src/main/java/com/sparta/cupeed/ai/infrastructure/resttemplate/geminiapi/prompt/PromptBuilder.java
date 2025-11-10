package com.sparta.cupeed.ai.infrastructure.resttemplate.geminiapi.prompt;

import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.sparta.cupeed.ai.infrastructure.resttemplate.geminiapi.dto.GeminiSendRequestDtoV1;

@Component
public class PromptBuilder {

	public String generateAiTextPrompt(GeminiSendRequestDtoV1 requestDto) {
		StringBuilder sb = new StringBuilder();

		sb.append("아래는 주문 정보 양식입니다. 이 정보를 토대로 최종 발송 시한을 계산하고, 주문 정보 양식 그대로 출력하되 마지막 줄에 최종 발송 시한을 한 줄 요약으로 추가하세요.\n\n");

		sb.append("주문 번호 : ").append(requestDto.getOrderNumber()).append("\n")
			.append("주문자 정보 : ").append(requestDto.getRecieveCompanyName()).append("\n")
			.append("주문 시간 : ").append(requestDto.getOrderDate()).append("\n");
		if (requestDto.getProducts() != null && !requestDto.getProducts().isEmpty()) {
			String productInfo = requestDto.getProducts().stream()
				.map(p -> p.getProductName() + " " + p.getQuantity() + "박스")
				.collect(Collectors.joining(", "));
			sb.append("상품 정보 : ").append(productInfo).append("\n");
		}
		sb.append("요청 사항 : ").append(requestDto.getCustomerRequest()).append("\n");
		sb.append("발송지 : ").append(requestDto.getStartHubName()).append("\n");
		sb.append("도착지 : ").append(requestDto.getEndHubName()).append("\n")
			.append("배송담당자 : ").append(requestDto.getDeliveryManagerName()).append("\n\n");

		sb.append("위 주문 정보를 기반으로 최종 발송 시한을 계산하세요.\n")
			.append("1. 계산 근거와 단계별 시간을 포함해 내부적으로 고려하세요.\n")
			.append("2. 최종 발송 시한만 마지막 줄에 한 줄 요약으로 작성하세요.\n")
			.append("3. 마지막 줄 형식 예시:\n")
			.append("\"위 내용을 기반으로 도출된 최종 발송 시한은 2025-12-11 14:00 입니다.\"\n")
			.append("4. 다른 요약이나 결론은 작성하지 마세요.");

		return sb.toString();
	}
}
