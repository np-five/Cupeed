package com.sparta.cupeed.user.domain.repository;

import java.util.Optional;

import com.sparta.cupeed.user.domain.model.User;
import com.sparta.cupeed.user.domain.model.UserCompany;
import com.sparta.cupeed.user.domain.model.UserDelivery;

public interface UserRepository {

	Optional<User> findByUserId(String userId);

	Optional<User> findBySlackId(String slackId);

	void save(User user);

	void save(User user, UserCompany newUserCompany);

	void save(User user, UserDelivery newUserDelivery);
}
