package com.sparta.cupeed.order.domain.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

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

	public enum Status {
		REQUESTED,   // 주문 요청됨
		ACCEPTED,    // 주문 수락됨
		SHIPPED,     // 배송 출발
		DELIVERED,   // 배송 완료
		CANCEL_REQUESTED, // 주문 취소 요청됨
		CANCELED;    // 주문 취소
	}
}
