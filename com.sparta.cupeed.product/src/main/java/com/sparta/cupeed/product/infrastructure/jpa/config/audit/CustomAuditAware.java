package com.sparta.cupeed.product.infrastructure.jpa.config.audit;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.sparta.cupeed.product.infrastructure.security.auth.UserDetailsImpl;

@Component
public class CustomAuditAware implements AuditorAware<String> {
	@Override
	public Optional<String> getCurrentAuditor() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		var principal = authentication.getPrincipal();
		if (principal instanceof UserDetailsImpl) {
			return Optional.of(((UserDetailsImpl)principal).getId().toString());
		}

		return Optional.of("system");
	}
}

