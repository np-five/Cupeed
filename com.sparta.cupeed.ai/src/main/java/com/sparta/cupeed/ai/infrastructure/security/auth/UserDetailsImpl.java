package com.sparta.cupeed.ai.infrastructure.security.auth;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.auth0.jwt.interfaces.DecodedJWT;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserDetailsImpl implements UserDetails {

	private UUID id;
	private String userId;
	private String role;
	private String slackId;
	private String token;

	public static UserDetailsImpl of(DecodedJWT decodedJWT) {
		return UserDetailsImpl.builder()
			.id(UUID.fromString(decodedJWT.getClaim("id").asString()))
			.userId(decodedJWT.getSubject())
			.role(decodedJWT.getClaim("role").asString())
			.slackId(decodedJWT.getClaim("slackId").asString())
			.build();
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(role);

		return List.of(simpleGrantedAuthority);
	}

	@Override
	public String getPassword() {
		return null;
	}

	@Override
	public String getUsername() {
		return userId;
	}

}
