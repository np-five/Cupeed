package com.sparta.cupeed.slack.infrastructure.jpa.config.audit;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.sparta.cupeed.slack.infrastructure.security.auth.UserDetailsImpl;

@Component
public class CustomAuditAware implements AuditorAware<String> {
	@Override
	public Optional<String> getCurrentAuditor() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication == null || !authentication.isAuthenticated()) {
			// Kafka나 비인증 상황에서는 system user 사용
			return Optional.of("system");
		}

		var principal = authentication.getPrincipal();
		if (principal instanceof UserDetailsImpl) {
			return Optional.of(((UserDetailsImpl) principal).getId().toString());
		}

		return Optional.of("system");
	}
}
