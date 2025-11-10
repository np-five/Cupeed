package com.sparta.cupeed.order.presentation.dto.response;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import com.sparta.cupeed.order.domain.model.Order;
import com.sparta.cupeed.order.domain.model.OrderItem;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrderGetResponseDtoV1 {
	private final OrderDto order;

	public static OrderGetResponseDtoV1 of(Order order) {
		return OrderGetResponseDtoV1.builder()
			.order(OrderDto.from(order))
			.build();
	}

	@Getter
	@Builder
	public static class OrderDto {

		private final String id;
		private final Order.Status status;
		private final BigDecimal totalPrice;
		private final Instant createdAt;
		private final Instant updatedAt;
		private final List<OrderItemDto> orderItemList;

		public static OrderDto from(Order order) {
			return OrderDto.builder()
				.id(String.valueOf(order.getId()))
				.status(order.getStatus())
				.totalPrice(order.getTotalPrice())
				.createdAt(order.getCreatedAt())
				.updatedAt(order.getUpdatedAt())
				.orderItemList(OrderItemDto.from(order.getOrderItemList()))
				.build();
		}
	}

	@Getter
	@Builder
	public static class OrderItemDto {

		private final String id;
		private final String productId;
		private final String productName;
		private final BigDecimal unitPrice;
		private final Long quantity;
		private final BigDecimal subtotal;

		private static List<OrderItemDto> from(List<OrderItem> orderItemList) {
			return orderItemList.stream()
				.map(OrderItemDto::from)
				.toList();
		}

		public static OrderItemDto from(OrderItem orderItem) {
			if (orderItem == null) {
				return null;
			}
			return OrderItemDto.builder()
				.id(String.valueOf(orderItem.getId()))
				.productId(String.valueOf(orderItem.getProductId()))
				.productName(orderItem.getProductName())
				.unitPrice(orderItem.getUnitPrice())
				.quantity(orderItem.getQuantity())
				.subtotal(orderItem.getSubtotal())
				.build();
		}
	}
}
