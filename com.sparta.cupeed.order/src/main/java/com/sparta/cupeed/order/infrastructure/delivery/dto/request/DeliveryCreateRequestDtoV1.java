package com.sparta.cupeed.order.infrastructure.delivery.dto.request;

import java.util.UUID;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DeliveryCreateRequestDtoV1 {
	private UUID orderId;
	private UUID receiveCompanyId;
}
