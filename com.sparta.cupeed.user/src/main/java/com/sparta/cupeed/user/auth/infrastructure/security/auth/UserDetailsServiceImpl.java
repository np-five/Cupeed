package com.sparta.cupeed.user.auth.infrastructure.security.auth;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.sparta.cupeed.user.auth.infrastructure.jpa.entity.UserEntity;
import com.sparta.cupeed.user.auth.infrastructure.jpa.repository.UserJpaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

	private final UserJpaRepository userJpaRepository;

	@Override
	public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
		UserEntity userEntity = userJpaRepository.findByUserId(userId).orElseThrow(() ->
			new UsernameNotFoundException("Not Found " + userId));

		return new UserDetailsImpl(userEntity);
	}
}
