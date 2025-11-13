package com.sparta.cupeed.user.domain.repository;

import java.util.Optional;
import java.util.UUID;

import com.sparta.cupeed.user.domain.model.User;
import com.sparta.cupeed.user.domain.model.UserCompany;
import com.sparta.cupeed.user.domain.model.UserDelivery;

public interface UserRepository {

	User findByIdOrElseThrow(UUID id);

	User findByUserIdOrElseThrow(String userId);

	Optional<User> findByUserId(String userId);

	Optional<User> findBySlackId(String slackId);

	void save(User user);

	void save(User user, UserCompany newUserCompany);

	void save(User user, UserDelivery newUserDelivery);

	void saveStatus(User user, String statusEnum);
}
