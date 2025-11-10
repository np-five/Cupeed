package com.sparta.cupeed.order.domain.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@Builder
@EqualsAndHashCode(of = "id") // 두 객체의 id 값이 같으면 같은 객체로 본다.
public class OrderItem {
	private final UUID id;
	private final UUID orderId;
	private final UUID productId;
	private final String productName;
	private final Long quantity;
	private final BigDecimal unitPrice;
	private final BigDecimal subtotal;
	private final Instant createdAt;
	private final String createdBy;
	private final Instant updatedAt;
	private final String updatedBy;
	private final Instant deletedAt;
	private final String deletedBy;

	OrderItemBuilder toBuilder() {
		return OrderItem.builder()
			.id(id)
			.orderId(orderId)
			.productId(productId)
			.productName(productName)
			.unitPrice(unitPrice)
			.quantity(quantity)
			.subtotal(subtotal)
			.createdAt(createdAt)
			.createdBy(createdBy)
			.updatedAt(updatedAt)
			.updatedBy(updatedBy)
			.deletedAt(deletedAt)
			.deletedBy(deletedBy);
	}
}
