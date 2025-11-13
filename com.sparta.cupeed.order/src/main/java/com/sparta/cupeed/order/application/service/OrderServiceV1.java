package com.sparta.cupeed.order.application.service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sparta.cupeed.order.domain.model.Order;
import com.sparta.cupeed.order.domain.model.OrderItem;
import com.sparta.cupeed.order.domain.repository.OrderRepository;
import com.sparta.cupeed.order.infrastructure.delivery.client.DeliveryClientV1;
import com.sparta.cupeed.order.infrastructure.delivery.dto.request.DeliveryCreateRequestDtoV1;
import com.sparta.cupeed.order.infrastructure.product.client.ProductClientV1;
import com.sparta.cupeed.order.infrastructure.product.dto.request.ProductStockRequestDtoV1;
import com.sparta.cupeed.order.infrastructure.product.dto.response.ProductGetResponseDtoV1;
import com.sparta.cupeed.order.infrastructure.security.RoleEnum;
import com.sparta.cupeed.order.infrastructure.security.auth.UserDetailsImpl;
import com.sparta.cupeed.order.infrastructure.slack.client.SlackClientV1;
import com.sparta.cupeed.order.infrastructure.slack.dto.request.SlackMessageCreateRequestDtoV1;
import com.sparta.cupeed.order.presentation.advice.OrderError;
import com.sparta.cupeed.order.presentation.advice.OrderException;
import com.sparta.cupeed.order.presentation.dto.request.OrderPostRequestDtoV1;
import com.sparta.cupeed.order.presentation.dto.request.OrderStatusUpdateRequestDtoV1;
import com.sparta.cupeed.order.presentation.dto.response.OrderPostResponseDtoV1;
import com.sparta.cupeed.order.presentation.dto.response.OrderGetResponseDtoV1;
import com.sparta.cupeed.order.presentation.dto.response.OrdersGetResponseDtoV1;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderServiceV1 {
	// TODO : Client한테 userDetails 전달

	private final OrderRepository orderRepository;
	private final ProductClientV1 productClient;
	private final DeliveryClientV1 deliveryClient;
	private final SlackClientV1 slackClient;

	@PreAuthorize("hasAnyAuthority('ROLE_MASTER', 'ROLE_COMPANY')")
	@Transactional
	public OrderPostResponseDtoV1 createOrder(UserDetailsImpl userDetails, OrderPostRequestDtoV1 requestDto) {
		OrderPostRequestDtoV1.OrderDto requestOrder = requestDto.getOrder();

		// 상품 중복 검사
		if (requestOrder.getOrderItemList().stream()
			.map(OrderPostRequestDtoV1.OrderDto.OrderItemDto::getProductId)
			.collect(Collectors.toSet()).size() != requestOrder.getOrderItemList().size()) {
			throw new OrderException(OrderError.ORDER_PRODUCT_DUPLICATED);
		}

		List<OrderItem> orderItemList = new ArrayList<>();
		BigDecimal totalPrice = BigDecimal.ZERO;
		UUID supplyCompanyId = UUID.randomUUID(); // 아래 로직에서 값 세팅됩니다. (임시 값 아님)

		for (OrderPostRequestDtoV1.OrderDto.OrderItemDto itemDto : requestOrder.getOrderItemList()) {
			UUID productId = itemDto.getProductId();
			Long quantity = itemDto.getQuantity();

			if (productId == null || quantity == null || quantity <= 0) {
				throw new OrderException(OrderError.ORDER_INVALID_QUANTITY);
			}

			// 상품 정보 조회
			ProductGetResponseDtoV1 response = productClient.getProduct(productId);
			ProductGetResponseDtoV1.ProductDto productInfo = response.getProduct();

			if (productInfo == null) {
				throw new OrderException(OrderError.ORDER_PRODUCT_NOT_FOUND);
			}
			Long availableStock = productInfo.getQuantity();
			if (availableStock == null || availableStock < quantity) {
				throw new OrderException(OrderError.ORDER_PRODUCT_OUT_OF_STOCK);
			}

			supplyCompanyId = productInfo.getCompanyId();

			UUID companyId =  productInfo.getCompanyId();
			BigDecimal unitPrice = productInfo.getUnitPrice();
			String productName = productInfo.getName();
			BigDecimal subtotal = unitPrice.multiply(BigDecimal.valueOf(quantity));

			totalPrice = totalPrice.add(subtotal);

			OrderItem orderItem = OrderItem.builder()
				.id(null)
				.productId(productId)
				.productName(productName)
				.quantity(quantity)
				.unitPrice(unitPrice)
				.subtotal(subtotal)
				.companyId(companyId)
				.build();

			orderItemList.add(orderItem);
		}

		UUID receiveCompanyId;
		String receiveCompanyName;

		if (userDetails.getRole().equals("ROLE_MASTER")) {
			receiveCompanyId = UUID.fromString("00000000-0000-0000-0000-000000000000");
			receiveCompanyName = "MASTER_DEFAULT_COMPANY";
		} else {
			receiveCompanyId = userDetails.getCompanyId();
			receiveCompanyName = userDetails.getCompanyName(); // TODO : 수령업체명 필요 (인증 토큰에서 가져와야 함)
		}

		String orderNumber = "ORD-" + Instant.now().toEpochMilli();

		Order created = Order.builder()
			.orderNumber(orderNumber)
			.supplyCompanyId(supplyCompanyId)
			.recieveCompanyId(receiveCompanyId)
			.recieveCompanyName(receiveCompanyName)
			.orderItemList(orderItemList)
			.totalPrice(totalPrice)
			.status(Order.Status.REQUESTED)
			.build();

		Order saved = orderRepository.save(created);

		ProductStockRequestDtoV1 decreaseRequestDto = ProductStockRequestDtoV1.builder()
			.order(ProductStockRequestDtoV1.OrderDto.builder()
				.orderId(saved.getId())
				.build())
			.productStocks(
				saved.getOrderItemList().stream()
					.map(item -> ProductStockRequestDtoV1.ProductStockDto.builder()
						.productId(item.getProductId())
						.quantity(item.getQuantity())
						.build())
					.toList()
			)
			.build();
		productClient.decreaseStock(decreaseRequestDto);

		DeliveryCreateRequestDtoV1 deliveryRequestDto = DeliveryCreateRequestDtoV1.builder()
			.orderId(saved.getId())
			.receiveCompanyId(saved.getRecieveCompanyId()).build();
		deliveryClient.createDelivery(deliveryRequestDto, "X-User-Chohee");

		slackClient.dmToReceiveCompany(
			SlackMessageCreateRequestDtoV1.builder()
				.orderNumber(saved.getOrderNumber())
				.recieveCompanyId(saved.getRecieveCompanyId())
				.recieveCompanyName(saved.getRecieveCompanyName())
				.totalPrice(saved.getTotalPrice())
				.recipientSlackId(userDetails.getSlackId()) // TODO : 수령자 슬랙 ID 필요 (인증 토큰에서 가져와야 함)
				.status("REQUESTED")
				.build()
		);

		return OrderPostResponseDtoV1.of(saved);
	}

	@PreAuthorize("hasAnyAuthority('ROLE_MASTER', 'ROLE_COMPANY')")
	@Transactional(readOnly = true)
	public OrderGetResponseDtoV1 getOrder(UserDetailsImpl userDetails, UUID orderId) {
		Order order = orderRepository.findById(orderId)
			.orElseThrow(() -> new OrderException(OrderError.ORDER_NOT_FOUND));
		RoleEnum role = RoleEnum.fromAuthority(userDetails.getRole());
		if (role == RoleEnum.COMPANY && !order.getRecieveCompanyId().equals(userDetails.getCompanyId())) {
			throw new OrderException(OrderError.ORDER_FORBIDDEN);
		}
		return OrderGetResponseDtoV1.of(order);
	}

	@PreAuthorize("hasAnyAuthority('ROLE_MASTER', 'ROLE_COMPANY')")
	@Transactional
	public OrderPostResponseDtoV1 updateOrder(UserDetailsImpl userDetails, UUID orderId, OrderPostRequestDtoV1 requestDto) {
		Order order = orderRepository.findById(orderId)
			.orElseThrow(() -> new OrderException(OrderError.ORDER_NOT_FOUND));

		RoleEnum role = RoleEnum.fromAuthority(userDetails.getRole());
		if (role == RoleEnum.COMPANY && !order.getRecieveCompanyId().equals(userDetails.getCompanyId())) {
			throw new OrderException(OrderError.ORDER_FORBIDDEN);
		}

		// 주문 상태가 REQUESTED일 때만 주문 수정 가능
		if (order.getStatus() != Order.Status.REQUESTED) {
			throw new OrderException(OrderError.ORDER_FORBIDDEN);
		}

		// 기존 주문 아이템 맵핑: productId -> quantity
		var oldQuantityMap = order.getOrderItemList().stream()
			.collect(Collectors.toMap(OrderItem::getProductId, OrderItem::getQuantity));

		// 새로운 주문 아이템 맵핑: productId -> quantity
		var newQuantityMap = requestDto.getOrder().getOrderItemList().stream()
			.collect(Collectors.toMap(
				OrderPostRequestDtoV1.OrderDto.OrderItemDto::getProductId,
				OrderPostRequestDtoV1.OrderDto.OrderItemDto::getQuantity
			));

		// 재고 차이 계산
		List<ProductStockRequestDtoV1.ProductStockDto> stockAdjustments = new ArrayList<>();

		// 기존 상품과 비교
		oldQuantityMap.forEach((productId, oldQty) -> {
			Long newQty = newQuantityMap.getOrDefault(productId, 0L);
			long diff = newQty - oldQty; // diff > 0 => 추가 주문, diff < 0 => 수량 감소
			if (diff != 0) {
				stockAdjustments.add(ProductStockRequestDtoV1.ProductStockDto.builder()
					.productId(productId)
					.quantity(diff)
					.build());
			}
		});

		// 신규 상품 추가 (기존에 없는 상품)
		newQuantityMap.forEach((productId, newQty) -> {
			if (!oldQuantityMap.containsKey(productId)) {
				stockAdjustments.add(ProductStockRequestDtoV1.ProductStockDto.builder()
					.productId(productId)
					.quantity(newQty)
					.build());
			}
		});

		// 차액에 따라 재고 반영
		for (ProductStockRequestDtoV1.ProductStockDto adjustment : stockAdjustments) {
			if (adjustment.getQuantity() > 0) {
				// 수량 증가: 재고 차감
				productClient.decreaseStock(
					ProductStockRequestDtoV1.builder()
						.order(ProductStockRequestDtoV1.OrderDto.builder().orderId(order.getId()).build())
						.productStocks(List.of(adjustment))
						.build()
				);
			} else if (adjustment.getQuantity() < 0) {
				// 수량 감소: 재고 복원
				ProductStockRequestDtoV1.ProductStockDto restore = ProductStockRequestDtoV1.ProductStockDto.builder()
					.productId(adjustment.getProductId())
					.quantity(-adjustment.getQuantity())
					.build();
				productClient.restoreStock(
					ProductStockRequestDtoV1.builder()
						.order(ProductStockRequestDtoV1.OrderDto.builder().orderId(order.getId()).build())
						.productStocks(List.of(restore))
						.build()
				);
			}
		}

		// 주문 정보 업데이트
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

	@Transactional
	public void cancelOrder(UserDetailsImpl userDetails, UUID orderId) {
		Order order = orderRepository.findById(orderId)
			.orElseThrow(() -> new OrderException(OrderError.ORDER_NOT_FOUND));
		if (order.getStatus() == Order.Status.CANCELED) {
			throw new OrderException(OrderError.ORDER_ALREADY_CANCELED);
		}
		if (order.getStatus() != Order.Status.CANCEL_REQUESTED) {
			throw new OrderException(OrderError.ORDER_CANCEL_NOT_REQUESTED);
		}

		ProductStockRequestDtoV1 restoreRequestDto = ProductStockRequestDtoV1.builder()
			.order(ProductStockRequestDtoV1.OrderDto.builder()
				.orderId(order.getId())
				.build())
			.productStocks(
				order.getOrderItemList().stream()
					.map(item -> ProductStockRequestDtoV1.ProductStockDto.builder()
						.productId(item.getProductId())
						.quantity(item.getQuantity())
						.build())
					.toList()
			)
			.build();
		for (OrderItem item : order.getOrderItemList()) {
			productClient.restoreStock(restoreRequestDto);
		}

		UUID userId = userDetails.getId();
		Order canceled = order.markCancelled(userId);
		orderRepository.save(canceled);
	}

	@PreAuthorize("hasAnyAuthority('ROLE_MASTER')")
	public void deleteOrder(UserDetailsImpl userDetails, UUID orderId) {
		Order order = orderRepository.findById(orderId)
			.orElseThrow(() -> new OrderException(OrderError.ORDER_NOT_FOUND));
		// 주문 상태가 DELIVERED(주문완료)일 때만 주문 내역 삭제 가능
		if (order.getStatus() != Order.Status.DELIVERED) {
			throw new OrderException(OrderError.ORDER_FORBIDDEN);
		}
		if (order.getDeletedAt() != null) {
			throw new OrderException(OrderError.ORDER_ALREADY_DELETED);
		}
		UUID userId = userDetails.getId();
		Order deleted = order.markDeleted(userId);
		orderRepository.save(deleted);
	}

	@PreAuthorize("hasAnyAuthority('ROLE_MASTER', 'ROLE_COMPANY')")
	@Transactional(readOnly = true)
	public OrdersGetResponseDtoV1 getOrders(UserDetailsImpl userDetails, String keyword, Pageable pageable) {
		Page<Order> orders;
		RoleEnum role = RoleEnum.fromAuthority(userDetails.getRole());
		if (role == RoleEnum.MASTER) {
			orders = orderRepository.searchOrders(null, keyword, pageable);
		} else {
			UUID receiveCompanyId = userDetails.getCompanyId();
			orders = orderRepository.searchOrders(receiveCompanyId, keyword, pageable);
		}
		return OrdersGetResponseDtoV1.of(orders);
	}
}
