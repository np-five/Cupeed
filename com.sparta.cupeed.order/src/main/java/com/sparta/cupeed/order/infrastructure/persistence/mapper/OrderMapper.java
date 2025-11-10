package com.sparta.cupeed.order.infrastructure.persistence.mapper;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.sparta.cupeed.order.domain.model.Order;
import com.sparta.cupeed.order.domain.model.OrderItem;
import com.sparta.cupeed.order.infrastructure.persistence.entity.OrderEntity;
import com.sparta.cupeed.order.infrastructure.persistence.entity.OrderItemEntity;

@Component
public class OrderMapper {

	public Order toDomain(OrderEntity entity) {
		if (entity == null) return null;

		return Order.builder()
			.id(entity.getId())
			.orderNumber(entity.getOrderNumber())
			.supplyCompanyId(entity.getSupplyCompanyId())
			.recieveCompanyId(entity.getRecieveCompanyId())
			.startHubId(entity.getStartHubId())
			.recieveCompanyName(entity.getRecieveCompanyName())
			.totalPrice(entity.getTotalPrice())
			.status(Order.Status.valueOf(entity.getStatus().name()))
			.deliveryDeadline(entity.getDeliveryDeadline())
			.aiDispatchDeadline(entity.getAiDispatchDeadline())
			.createdAt(entity.getCreatedAt())
			.createdBy(entity.getCreatedBy())
			.updatedAt(entity.getUpdatedAt())
			.updatedBy(entity.getUpdatedBy())
			.deletedAt(entity.getDeletedAt())
			.deletedBy(entity.getDeletedBy())
			.orderItemList(
				entity.getOrderItemList() == null ? List.of()
					: entity.getOrderItemList().stream()
					.map(this::toDomain)
					.collect(Collectors.toList())
			)
			.build();
	}

	private OrderItem toDomain(OrderItemEntity entity) {
		if (entity == null) return null;

		return OrderItem.builder()
			.id(entity.getId())
			.productId(entity.getProductId())
			.productName(entity.getProductName())
			.quantity(entity.getQuantity())
			.unitPrice(entity.getUnitPrice())
			.subtotal(entity.getSubtotal())
			.createdAt(entity.getCreatedAt())
			.createdBy(entity.getCreatedBy())
			.updatedAt(entity.getUpdatedAt())
			.updatedBy(entity.getUpdatedBy())
			.deletedAt(entity.getDeletedAt())
			.deletedBy(entity.getDeletedBy())
			.build();
	}

	public OrderEntity toEntity(Order domain) {
		if (domain == null) return null;

		OrderEntity entity = OrderEntity.builder()
			.id(domain.getId()) // 새 주문이면 null일 것
			.orderNumber(domain.getOrderNumber())
			.supplyCompanyId(domain.getSupplyCompanyId())
			.recieveCompanyId(domain.getRecieveCompanyId())
			.startHubId(domain.getStartHubId())
			.recieveCompanyName(domain.getRecieveCompanyName())
			.totalPrice(domain.getTotalPrice())
			.status(OrderEntity.Status.valueOf(domain.getStatus().name()))
			.deliveryDeadline(domain.getDeliveryDeadline())
			.aiDispatchDeadline(domain.getAiDispatchDeadline())
			.build();

		// ✅ OrderItemEntity 리스트 매핑 수정
		if (domain.getOrderItemList() != null) {
			domain.getOrderItemList().forEach(item -> {
				OrderItemEntity itemEntity = OrderItemEntity.builder()
					.id(null) // ← 중요: 신규 저장 시 id를 절대 복사하지 말 것
					.productId(item.getProductId())
					.productName(item.getProductName())
					.quantity(item.getQuantity())
					.unitPrice(item.getUnitPrice())
					.subtotal(item.getSubtotal())
					.build();

				entity.addOrderItem(itemEntity); // ← 핵심: 부모-자식 연관관계 자동 연결
			});
		}

		return entity;
	}

	// 도메인 모델의 변경내용을 영속성 엔티티에 반영하는 메서드
	public void applyDomain(Order order, OrderEntity entity) {
		if (order == null || entity == null) {
			return;
		}

		// 기본 필드 업데이트
		entity.setStatus(toEntity(order.getStatus()));
		entity.setTotalPrice(order.getTotalPrice());
		entity.setSupplyCompanyId(order.getSupplyCompanyId());
		entity.setRecieveCompanyId(order.getRecieveCompanyId());
		entity.setStartHubId(order.getStartHubId());
		entity.setRecieveCompanyName(order.getRecieveCompanyName());

		// 삭제 정보 반영
		if (order.getDeletedAt() != null && order.getDeletedBy() != null) {
			entity.markDeleted(order.getDeletedAt(), order.getDeletedBy());
		}

		// 아이템 동기화
		syncOrderItems(order, entity);
	}

	// 주문(도메인)객체에 담긴 주문 항목 목록(OrderItemList)을 주문엔티티(영속성 엔티티)의 주문항목목록과 동기화시킨다.
	// 존재하는 항목은 업데이트하고, 새 항목은 추가하고, 존재하지 않는 항목은 제거한다.
	private void syncOrderItems(Order order, OrderEntity entity) {
		// entity.getOrderItemList()에 있는 OrderItemEntity들 중 id가 null이 아닌 것들만 골라 Map으로 만든다.
		Map<UUID, OrderItemEntity> existingItems = entity.getOrderItemList().stream()
			.filter(item -> item.getId() != null)
			.collect(Collectors.toMap(OrderItemEntity::getId, Function.identity(),
					(a, b) -> {
					System.err.println("Dupplicate OrderItemEntity id foundd: " + a.getId());
					return a;
					}
				)); // 키는 OrderItemEntity, 값은 엔티티 그 자체

		// 최종 id 집합 준비 - 최종적으로 엔티티에 남아있어야 할 항목들의 id 집합 (중복 없고, 존재하는 OrderItem들)
		Set<UUID> desiredIds = new HashSet<>();
		for (OrderItem orderItem : order.getOrderItemList()) {
			UUID itemId = orderItem.getId();

			if (itemId != null && existingItems.containsKey(itemId)) {
				OrderItemEntity existing = existingItems.get(itemId);

				// 수량/가격 업데이트
				existing.update(orderItem.getProductId(),
					orderItem.getProductName(),
					orderItem.getQuantity(),
					orderItem.getUnitPrice(),
					orderItem.getSubtotal());

				// 삭제 정보 반영
				if (orderItem.getDeletedAt() != null && orderItem.getDeletedBy() != null) {
					existing.markDeleted(orderItem.getDeletedAt(), orderItem.getDeletedBy());
				}

				desiredIds.add(itemId);
			} else {
				// 새 아이템(엔티티) 생성
				OrderItemEntity created = toEntity(orderItem); // 새 OrderItemEntity 만들고
				entity.addOrderItem(created); // 부모 엔티티(OrderEntity) 리스트에 추가
				if (created.getId() != null) {
					desiredIds.add(created.getId());
				}
			}
		}

		// 엔티티의 현재 항목들 중에서 id는 존재하는데 desiredIds에 없는 것들 제거 -> Order에 더 이상 존재하지 않으므로 삭제해야 함.
		List<OrderItemEntity> toRemove = entity.getOrderItemList().stream()
			.filter(item -> item.getId() != null && !desiredIds.contains(item.getId()))
			.toList();
		toRemove.forEach(OrderItemEntity::detachOrder);
		entity.getOrderItemList().removeAll(toRemove);
	}

	private OrderItemEntity toEntity(OrderItem domain) {
		if (domain == null) return null;

		return OrderItemEntity.builder()
			.id(domain.getId() != null ? domain.getId() : UUID.randomUUID())
			.productId(domain.getProductId())
			.productName(domain.getProductName())
			.quantity(domain.getQuantity())
			.unitPrice(domain.getUnitPrice())
			.subtotal(domain.getSubtotal())
			.build();
	}

	private OrderEntity.Status toEntity(Order.Status status) {
		return status == null ? null : OrderEntity.Status.valueOf(status.name());
	}
}
