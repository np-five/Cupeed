package com.sparta.cupeed.order.infrastructure.persistence.entity;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "p_order")
@DynamicInsert
@DynamicUpdate
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id", callSuper = false)
public class OrderEntity extends BaseEntity {

	@Version
	private Long version;

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "id", nullable = false, unique = true)
	private UUID id;

	@Column(nullable = false, unique = true)
	private String orderNumber;

	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch =
	FetchType.LAZY)
	@Builder.Default
	private List<OrderItemEntity> orderItemList = new ArrayList<>();

	@Column(nullable = false)
	private UUID supplyCompanyId;

	@Column(nullable = false)
	private UUID recieveCompanyId;

	@Column(nullable = true)
	private UUID startHubId;

	@Column(nullable = false, precision = 15, scale = 2)
	private BigDecimal totalPrice;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	private Status status;

	@Column(nullable = true)
	private Instant deliveryDeadline;

	@Column(nullable = true)
	private Instant aiDispatchDeadline;

	public void addOrderItem(OrderItemEntity item) {
		item.setOrder(this);
		this.orderItemList.add(item);
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
	}

	public void setSupplyCompanyId(UUID id) {
		this.supplyCompanyId = id;
	}

	public void setRecieveCompanyId(UUID id) {
		this.recieveCompanyId = id;
	}

	public void setStartHubId(UUID id) {
		this.startHubId = id;
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
