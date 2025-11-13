package com.sparta.cupeed.delivery.domain.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.sparta.cupeed.delivery.domain.model.DeliveryManager;
import com.sparta.cupeed.delivery.domain.model.DeliveryType;

public interface DeliveryManagerRepository {

	DeliveryManager save(DeliveryManager deliveryManager);

	Optional<DeliveryManager> findById(UUID id);

	Optional<DeliveryManager> findByUserId(String userId);

	List<DeliveryManager> findByHubId(UUID hubId);

	List<DeliveryManager> findByDeliveryType(DeliveryType deliveryType);

	List<DeliveryManager> findByHubIdAndDeliveryType(UUID hubId, DeliveryType deliveryType);

	List<DeliveryManager> findAllActive();

	void delete(DeliveryManager deliveryManager);
}