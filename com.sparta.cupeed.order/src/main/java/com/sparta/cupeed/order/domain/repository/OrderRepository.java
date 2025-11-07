package com.sparta.cupeed.order.domain.repository;

import com.sparta.cupeed.order.domain.model.Order;

public interface OrderRepository {
	Order save(Order order);
}
