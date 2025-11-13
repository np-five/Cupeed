package com.sparta.cupeed.delivery.domain.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.sparta.cupeed.delivery.domain.model.DeliveryRoute;

public interface DeliveryRouteRepository {

	DeliveryRoute save(DeliveryRoute deliveryRoute);

	Optional<DeliveryRoute> findById(UUID id);

	List<DeliveryRoute> findByDeliveryId(UUID deliveryId);

	List<DeliveryRoute> findAll();
}