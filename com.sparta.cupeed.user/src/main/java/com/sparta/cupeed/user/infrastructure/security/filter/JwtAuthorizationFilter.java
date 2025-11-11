package com.sparta.cupeed.user.infrastructure.security.filter;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.sparta.cupeed.user.infrastructure.security.auth.UserDetailsImpl;
import com.sparta.cupeed.user.infrastructure.security.jwt.JwtProperties;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

	private final JwtProperties jwtProperties;

	private final List<String> whitelist = List.of("/v1/auth/sign-up", "/v1/auth/log-in");

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {
		return whitelist.stream().anyMatch(url -> request.getRequestURI().startsWith(url));
	}

	@Override
	protected void doFilterInternal(
		HttpServletRequest request,
		HttpServletResponse response,
		FilterChain filterChain
	) throws ServletException, IOException {
		String authorizationToken = request.getHeader(jwtProperties.getAccessHeaderName());

		if (!StringUtils.hasText(authorizationToken) || !authorizationToken.startsWith(
			jwtProperties.getHeaderPrefix())) {
			filterChain.doFilter(request, response);
			return;
		}
		String accessJwt = authorizationToken.substring(jwtProperties.getHeaderPrefix().length());
		DecodedJWT decodedAccessJwt = JWT.decode(accessJwt);
		UserDetails userDetails;
		try {
			userDetails = UserDetailsImpl.of(decodedAccessJwt);
		} catch (RuntimeException e) {
			filterChain.doFilter(request, response);
			return;
		}

		Authentication authentication =
			new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

		SecurityContextHolder.getContext().setAuthentication(authentication);

		filterChain.doFilter(request, response);
	}
}
