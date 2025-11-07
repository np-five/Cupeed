package com.sparta.cupeed.order.infrastructure.persistence.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sparta.cupeed.order.infrastructure.persistence.entity.OrderEntity;

public interface OrderJpaRepository extends JpaRepository<OrderEntity, UUID> {
}
