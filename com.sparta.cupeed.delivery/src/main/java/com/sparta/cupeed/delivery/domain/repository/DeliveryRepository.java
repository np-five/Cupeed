package com.sparta.cupeed.delivery.domain.repository;

import com.sparta.cupeed.delivery.domain.model.Delivery;
import com.sparta.cupeed.delivery.domain.model.DeliveryStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DeliveryRepository {

	Delivery save(Delivery delivery);

	Optional<Delivery> findById(UUID id);

	Optional<Delivery> findByOrderId(UUID orderId);

	List<Delivery> findAll(int page, int size);

	List<Delivery> findByStatus(DeliveryStatus status, int page, int size);

	long countAll();

	long countByStatus(DeliveryStatus status);

	void delete(Delivery delivery);
}