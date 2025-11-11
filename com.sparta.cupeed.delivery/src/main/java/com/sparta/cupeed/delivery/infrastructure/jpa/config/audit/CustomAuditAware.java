package com.sparta.cupeed.delivery.infrastructure.jpa.config.audit;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

@Component
public class CustomAuditAware implements AuditorAware<String> {
	@Override
	public Optional<String> getCurrentAuditor() {
		// Spring Data JPA Auditing 은 현재 SecurityContextHolder 또는 AuditorAware 구현체가 반환하는
		// 사용자 정보를 기준으로 created_by, updated_by 를 채운다.
		// 지금은 매번 새로운 UUID로 임시 사용자 식별자 생성 -> 같은 주문이라도 주문아이템 DB에 created_by updated_by 값이 다르게 들어감
		return Optional.of(UUID.randomUUID().toString());
	}
}