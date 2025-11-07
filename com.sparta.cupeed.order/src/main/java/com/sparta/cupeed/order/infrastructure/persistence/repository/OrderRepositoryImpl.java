package com.sparta.cupeed.order.infrastructure.persistence.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.sparta.cupeed.order.domain.model.Order;
import com.sparta.cupeed.order.domain.repository.OrderRepository;
import com.sparta.cupeed.order.infrastructure.persistence.entity.OrderEntity;
import com.sparta.cupeed.order.infrastructure.persistence.mapper.OrderMapper;
import com.sparta.cupeed.order.presentation.advice.OrderError;
import com.sparta.cupeed.order.presentation.advice.OrderException;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepository {

	private final OrderJpaRepository orderJpaRepository;
	private final OrderMapper orderMapper;

	@Override
	@Transactional
	public Order save(Order order) {
		OrderEntity orderEntity;

		if (order.getId() == null) {
			// 신규 주문 생성
			orderEntity = orderMapper.toEntity(order);
		} else {
			// 기존 주문 업데이트
			orderEntity = orderJpaRepository.findById(order.getId())
				.orElseThrow(() -> new OrderException(OrderError.ORDER_NOT_FOUND));
			orderMapper.applyDomain(order, orderEntity);
		}

		// 항상 save 호출 (신규 or 수정 둘 다 JPA merge/dirty check 트리거)
		OrderEntity saved = orderJpaRepository.save(orderEntity);

		return orderMapper.toDomain(saved);
	}
}
