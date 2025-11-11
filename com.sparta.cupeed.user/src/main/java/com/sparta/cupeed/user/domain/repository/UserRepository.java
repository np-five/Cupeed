package com.sparta.cupeed.user.domain.repository;

import java.util.Optional;

import com.sparta.cupeed.user.domain.model.User;
import com.sparta.cupeed.user.domain.model.UserCompany;

public interface UserRepository {

	Optional<User> findByUserId(String userId);

	Optional<User> findBySlackId(String slackId);

	void save(User user, UserCompany newUserCompany);
}
