package com.sparta.cupeed.user.domain.repository;

import java.util.UUID;

import com.sparta.cupeed.user.domain.model.UserCompany;

public interface UserCompanyRepository {
	UserCompany findUserCompanyByUserId(UUID id);
}
