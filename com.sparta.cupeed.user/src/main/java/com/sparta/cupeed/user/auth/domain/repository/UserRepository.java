package com.sparta.cupeed.user.auth.domain.repository;

import java.util.Optional;

import com.sparta.cupeed.user.auth.domain.model.User;
import com.sparta.cupeed.user.auth.domain.model.UserCompany;

public interface UserRepository {

	Optional<User> findByUserId(String userId);

	Optional<User> findBySlackId(String slackId);

	User save(User user, UserCompany newUserCompany);
}
