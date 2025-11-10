package com.sparta.cupeed.order.infrastructure.persistence.entity;

import java.math.BigDecimal;
import java.util.UUID;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "p_order_item")
@DynamicInsert
@DynamicUpdate
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id", callSuper = false)
public class OrderItemEntity extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_id")
	private OrderEntity order;

	@Column(nullable = false)
	private UUID productId;

	@Column(nullable = false)
	private String productName;

	@Column(nullable = false)
	private Long quantity;

	@Column(nullable = false, precision = 15, scale = 2)
	private BigDecimal unitPrice;

	@Column(nullable = false, precision = 15, scale = 2)
	private BigDecimal subtotal;

	public void setOrder(OrderEntity entity) {
		this.order = entity;
	}

	public void update(UUID productId, String productName, Long quantity, BigDecimal unitPrice, BigDecimal subtotal) {
		if (productId != null) this.productId = productId;
		if (productName != null) this.productName = productName;
		if (quantity != null) this.quantity = quantity;
		if (unitPrice != null) this.unitPrice = unitPrice;
		if (subtotal != null) this.subtotal = subtotal;
	}

	// 주문 분리
	public void detachOrder() {
		this.order = null;
	}
}
