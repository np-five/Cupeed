package com.sparta.cupeed.order.domain.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import com.sparta.cupeed.order.presentation.advice.OrderError;
import com.sparta.cupeed.order.presentation.advice.OrderException;
import com.sparta.cupeed.order.presentation.dto.request.OrderPostRequestDtoV1;

import jakarta.validation.Valid;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@Builder
@EqualsAndHashCode(of = "id")
public class Order {
	private final UUID id;
	private final String orderNumber;
	private final List<OrderItem> orderItemList;
	private final UUID supplyCompanyId;
	private final UUID recieveCompanyId;
	private final UUID startHubId;
	private final String recieveCompanyName;
	private final BigDecimal totalPrice;
	private final Status status;
	private final Instant deliveryDeadline;
	private final Instant aiDispatchDeadline;
	private final Instant createdAt;
	private final String createdBy;
	private final Instant updatedAt;
	private final String updatedBy;
	private final Instant deletedAt;
	private final String deletedBy;

	public List<OrderItem> getOrderItemList() {
		return orderItemList == null ? List.of() : Collections.unmodifiableList(orderItemList);
	}

	// 불변 도메인 유지하면서 부분 변경 가능(update시)
	OrderBuilder toBuilder() {
		return Order.builder()
			.id(id)
			.orderNumber(orderNumber)
			.orderItemList(orderItemList)
			.supplyCompanyId(supplyCompanyId)
			.recieveCompanyId(recieveCompanyId)
			.startHubId(startHubId)
			.recieveCompanyName(recieveCompanyName)
			.totalPrice(totalPrice)
			.status(status)
			.deliveryDeadline(deliveryDeadline)
			.aiDispatchDeadline(aiDispatchDeadline)
			.createdAt(createdAt)
			.createdBy(createdBy)
			.updatedAt(updatedAt)
			.updatedBy(updatedBy)
			.deletedAt(deletedAt)
			.deletedBy(deletedBy);
	}

	public Order withUpdated(OrderPostRequestDtoV1 requestDto) {
		List<OrderItem> updatedItems = this.orderItemList.stream()
			.map(existing -> {
				var matchedDto = requestDto.getOrder().getOrderItemList().stream()
					.filter(dto -> dto.getProductId().equals(existing.getProductId()))
					.findFirst()
					.orElseThrow(() -> new OrderException(OrderError.ORDER_PRODUCT_NOT_FOUND));

				return existing.toBuilder()
					.quantity(matchedDto.getQuantity())
					.subtotal(existing.getUnitPrice().multiply(BigDecimal.valueOf(matchedDto.getQuantity())))
					.build();
			})
			.toList();

		BigDecimal newTotalPrice = updatedItems.stream()
			.map(OrderItem::getSubtotal)
			.reduce(BigDecimal.ZERO, BigDecimal::add);

		return toBuilder()
			.orderItemList(updatedItems)
			.totalPrice(newTotalPrice)
			.build();
	}

	public Order updateStatus(Status status) {
		return toBuilder()
			.status(status)
			.build();
	}

	public Order markDeleted(UUID userId) {
		List<OrderItem> deletedItems = orderItemList.stream()
			.map(item -> item.toBuilder()
				.deletedAt(Instant.now())
				.deletedBy(userId.toString())
				.build())
			.toList();

		return toBuilder()
			.deletedAt(Instant.now())
			.deletedBy(userId.toString())
			.orderItemList(deletedItems)
			.build();
	}

	public Order markCancelled(UUID userId) {
		return toBuilder()
			.status(Status.CANCELED)
			.build();
	}

	public enum Status {
		REQUESTED,   // 주문 요청됨
		ACCEPTED,    // 주문 수락됨
		SHIPPED,     // 배송 출발
		DELIVERED,   // 배송 완료
		CANCEL_REQUESTED, // 주문 취소 요청됨
		CANCELED;    // 주문 취소
	}
}
