package com.sparta.cupeed.order.application.service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sparta.cupeed.order.domain.model.Order;
import com.sparta.cupeed.order.domain.model.OrderItem;
import com.sparta.cupeed.order.domain.repository.OrderRepository;
import com.sparta.cupeed.order.presentation.advice.OrderError;
import com.sparta.cupeed.order.presentation.advice.OrderException;
import com.sparta.cupeed.order.presentation.dto.request.OrderPostRequestDtoV1;
import com.sparta.cupeed.order.presentation.dto.request.OrderStatusUpdateRequestDtoV1;
import com.sparta.cupeed.order.presentation.dto.response.OrderPostResponseDtoV1;
import com.sparta.cupeed.order.presentation.dto.response.OrderGetResponseDtoV1;
import com.sparta.cupeed.order.presentation.dto.response.OrdersGetResponseDtoV1;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderServiceV1 {

	private final OrderRepository orderRepository;

	@Transactional
	public OrderPostResponseDtoV1 createOrder(OrderPostRequestDtoV1 requestDto) {
		OrderPostRequestDtoV1.OrderDto requestOrder = requestDto.getOrder();

		// 상품 중복 검사
		if (requestOrder.getOrderItemList().stream()
			.map(OrderPostRequestDtoV1.OrderDto.OrderItemDto::getProductId)
			.collect(Collectors.toSet()).size() != requestOrder.getOrderItemList().size()) {
			throw new OrderException(OrderError.ORDER_PRODUCT_DUPLICATED);
		}

		List<OrderItem> orderItemList = new ArrayList<>();
		BigDecimal totalPrice = BigDecimal.ZERO;

		for (OrderPostRequestDtoV1.OrderDto.OrderItemDto itemDto : requestOrder.getOrderItemList()) {
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

		Order created = Order.builder()
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

		Order saved = orderRepository.save(created); // Hibernate가 OrderEntity + OrderItemEntity를 한 번의 INSERT 트랜잭션으로 처리
		return OrderPostResponseDtoV1.of(saved);
	}

	public OrderGetResponseDtoV1 getOrder(UUID orderId) {
		Order order = orderRepository.findById(orderId)
			.orElseThrow(() -> new OrderException(OrderError.ORDER_NOT_FOUND));
		return OrderGetResponseDtoV1.of(order);
	}

	@Transactional
	public OrderPostResponseDtoV1 updateOrder(UUID orderId, @Valid OrderPostRequestDtoV1 requestDto) {
		Order order = orderRepository.findById(orderId)
			.orElseThrow(() -> new OrderException(OrderError.ORDER_NOT_FOUND));
		// 주문 상태가 REQUESTED일 때만 주문을 수정할 수 있다.
		if (order.getStatus() != Order.Status.REQUESTED) {
			throw new OrderException(OrderError.ORDER_FORBIDDEN);
		}
		Order updated = order.withUpdated(requestDto);
		Order saved = orderRepository.save(updated);
		return OrderPostResponseDtoV1.of(saved);
	}

	@Transactional
	public OrderPostResponseDtoV1 updateOrderStatus(UUID orderId, OrderStatusUpdateRequestDtoV1 requestDto) {
		Order order = orderRepository.findById(orderId)
			.orElseThrow(() -> new OrderException(OrderError.ORDER_NOT_FOUND));
		Order updated = order.updateStatus(requestDto.getStatus());
		Order saved = orderRepository.save(updated);
		return OrderPostResponseDtoV1.of(saved);
	}

	public void deleteOrder(UUID orderId) {
		Order order = orderRepository.findById(orderId)
			.orElseThrow(() -> new OrderException(OrderError.ORDER_NOT_FOUND));
		if (order.getDeletedAt() != null) {
			throw new OrderException(OrderError.ORDER_ALREADY_DELETED);
		}
		// 임시 userId
		UUID userId = UUID.randomUUID();
		Order deleted = order.markDeleted(userId);
		orderRepository.save(deleted);
	}

	public OrdersGetResponseDtoV1 getOrders(Pageable pageable) {
		// TODO : 검색할 때 쿼리 DSL 적용
		Page<Order> orders = orderRepository.findAllByDeletedAtIsNull(pageable);
		return OrdersGetResponseDtoV1.of(orders);
	}

}
