package com.sparta.cupeed.order.application.service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sparta.cupeed.order.domain.model.Order;
import com.sparta.cupeed.order.domain.model.OrderItem;
import com.sparta.cupeed.order.domain.repository.OrderRepository;
import com.sparta.cupeed.order.presentation.advice.OrderError;
import com.sparta.cupeed.order.presentation.advice.OrderException;
import com.sparta.cupeed.order.presentation.dto.request.OrderCreateRequestDtoV1;
import com.sparta.cupeed.order.presentation.dto.response.OrderCreateResponseDtoV1;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderServiceV1 {

	private final OrderRepository orderRepository;

	@Transactional
	public OrderCreateResponseDtoV1 createOrder(OrderCreateRequestDtoV1 requestDto) {
		OrderCreateRequestDtoV1.OrderDto requestOrder = requestDto.getOrder();

		// 상품 중복 검사
		if (requestOrder.getOrderItemList().stream()
			.map(OrderCreateRequestDtoV1.OrderDto.OrderItemDto::getProductId)
			.collect(Collectors.toSet()).size() != requestOrder.getOrderItemList().size()) {
			throw new OrderException(OrderError.ORDER_PRODUCT_DUPLICATED);
		}

		List<OrderItem> orderItemList = new ArrayList<>();
		BigDecimal totalPrice = BigDecimal.ZERO;

		for (OrderCreateRequestDtoV1.OrderDto.OrderItemDto itemDto : requestOrder.getOrderItemList()) {
			UUID productId = itemDto.getProductId();
			Long quantity = itemDto.getQuantity();

			if (productId == null || quantity == null || quantity <= 0) {
				throw new OrderException(OrderError.ORDER_INVALID_QUANTITY);
			}

			// 더미 상품 데이터
			String dummyProductName = "TestProduct-" + productId.toString().substring(0, 8);
			BigDecimal unitPrice = BigDecimal.valueOf(1000);
			BigDecimal subtotal = unitPrice.multiply(BigDecimal.valueOf(quantity)); // subtotal = unitPrice × quantity

			totalPrice = totalPrice.add(subtotal); // 각 주문 아이템들의 subtotal 합계

			OrderItem orderItem = OrderItem.builder()
				.id(null)
				.productId(productId)
				.productName(dummyProductName)
				.quantity(quantity)
				.unitPrice(unitPrice)
				.subtotal(subtotal)
				.createdAt(Instant.now())
				.createdBy("system")
				.build();

			orderItemList.add(orderItem);
		}

		// 임시 값 (인증 토큰에서 가져와야 함)
		UUID authRecieveCompanyId = UUID.randomUUID();
		UUID dummySupplyCompanyId = UUID.randomUUID();
		UUID dummyStartHubId = UUID.randomUUID();

		// 임시 주문 번호 생성
		String orderNumber = "ORD-" + Instant.now().toEpochMilli();

		Order order = Order.builder()
			.orderNumber(orderNumber)
			.supplyCompanyId(dummySupplyCompanyId)
			.recieveCompanyId(authRecieveCompanyId)
			.startHubId(dummyStartHubId)
			.orderItemList(orderItemList)
			.totalPrice(totalPrice)
			.status(Order.Status.REQUESTED)
			.createdAt(Instant.now())
			.createdBy("system")
			.build();

		Order savedOrder = orderRepository.save(order); // Hibernate가 OrderEntity + OrderItemEntity를 한 번의 INSERT 트랜잭션으로 처리

		return OrderCreateResponseDtoV1.of(savedOrder);
	}
}
