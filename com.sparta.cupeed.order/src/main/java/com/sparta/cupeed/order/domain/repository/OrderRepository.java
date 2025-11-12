package com.sparta.cupeed.order.domain.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.sparta.cupeed.order.domain.model.Order;

public interface OrderRepository {
	Order save(Order order);

	Optional<Order> findById(UUID orderId);

	Page<Order> findAllByDeletedAtIsNull(Pageable pageable);

	Page<Order> searchOrders(String keyword, Pageable pageable);
}
