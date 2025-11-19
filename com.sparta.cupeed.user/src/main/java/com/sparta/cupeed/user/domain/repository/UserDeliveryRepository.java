package com.sparta.cupeed.user.domain.repository;

import java.util.List;
import java.util.UUID;

import com.sparta.cupeed.user.domain.model.UserDelivery;

public interface UserDeliveryRepository {

	List<UserDelivery> findUserDeliveryEntitiesByUserHubId(UUID hubId);

	List<UserDelivery> findUserDeliveryEntitiesByUserHubIdIsNull();
}
