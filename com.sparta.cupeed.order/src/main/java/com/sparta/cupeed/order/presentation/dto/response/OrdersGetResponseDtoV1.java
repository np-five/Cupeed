package com.sparta.cupeed.order.presentation.dto.response;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.web.PagedModel;

import com.sparta.cupeed.order.domain.model.Order;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
public class OrdersGetResponseDtoV1 {
	private final OrderPageDto orderPage;

	public static OrdersGetResponseDtoV1 of(Page<Order> orderPage) {
		return OrdersGetResponseDtoV1.builder()
			.orderPage(new OrderPageDto(orderPage))
			.build();
	}

	@Getter
	@ToString
	public static class OrderPageDto extends PagedModel<OrderPageDto.OrderDto> {

		public OrderPageDto(Page<Order> orderPage) {
			super(
				new PageImpl<>(
					OrderDto.from(orderPage.getContent()),
					orderPage.getPageable(),
					orderPage.getTotalElements()
				)
			);
		}

		public OrderPageDto(OrderDto... orderDtoArray) {
			super(new PageImpl<>(List.of(orderDtoArray)));
		}

		@Getter
		@Builder
		public static class OrderDto {

			private final String id;
			private final Order.Status status;
			private final BigDecimal totalPrice;
			private final Instant createdAt;
			private final Instant updatedAt;

			private static List<OrderDto> from(List<Order> orderList) {
				return orderList.stream()
					.map(OrderDto::from)
					.toList();
			}

			public static OrderDto from(Order order) {
				return OrderDto.builder()
					.id(String.valueOf(order.getId()))
					.status(order.getStatus())
					.totalPrice(order.getTotalPrice())
					.createdAt(order.getCreatedAt())
					.updatedAt(order.getUpdatedAt())
					.build();
			}
		}
	}
}
