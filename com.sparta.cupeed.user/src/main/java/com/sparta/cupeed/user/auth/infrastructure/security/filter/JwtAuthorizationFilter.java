package com.sparta.cupeed.user.auth.infrastructure.security.filter;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.sparta.cupeed.user.auth.infrastructure.security.auth.UserDetailsServiceImpl;
import com.sparta.cupeed.user.auth.infrastructure.security.jwt.JwtUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

	private final JwtUtil jwtUtil;
	private final UserDetailsServiceImpl userDetailsService;

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
		String token = jwtUtil.getJwtTokenFromHeader(request);

		if (!StringUtils.hasText(token)) {
			filterChain.doFilter(request, response);
			return;
		}

		try {
			if (!jwtUtil.validateToken(token)) {
				response.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid JWT Token");
				return;
			}

			String userId = jwtUtil.getUserInfoFromToken(token).getSubject();
			setAuthentication(userId);
		} catch (Exception e) {
			SecurityContextHolder.clearContext();
			response.sendError(HttpServletResponse.SC_FORBIDDEN, "Authentication failed");
			return;
		}

		filterChain.doFilter(request, response);
	}

	public void setAuthentication(String userId) {
		SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
		UserDetails userDetails = userDetailsService.loadUserByUsername(userId);
		Authentication authentication = new UsernamePasswordAuthenticationToken(
			userDetails,
			null,
			userDetails.getAuthorities());

		securityContext.setAuthentication(authentication);
		SecurityContextHolder.setContext(securityContext);
	}
}
