package com.sparta.cupeed.user.auth.infrastructure.security.auth;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.sparta.cupeed.user.auth.infrastructure.jpa.entity.RoleEnum;
import com.sparta.cupeed.user.auth.infrastructure.jpa.entity.UserEntity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserDetailsImpl implements UserDetails {

	private final UserEntity userEntity;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		RoleEnum role = userEntity.getRole();
		String authority = role.getAuthority();
		SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(authority);

		return List.of(simpleGrantedAuthority);
	}

	@Override
	public String getPassword() {
		return userEntity.getPassword();
	}

	@Override
	public String getUsername() {
		return userEntity.getUserId();
	}
}
