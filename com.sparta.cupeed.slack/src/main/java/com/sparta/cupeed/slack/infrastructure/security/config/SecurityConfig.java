package com.sparta.cupeed.slack.infrastructure.security.config;

import java.util.Collections;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import com.sparta.cupeed.slack.infrastructure.security.filter.JwtAuthorizationFilter;

import lombok.RequiredArgsConstructor;

@Configuration            // Spring 설정 파일임을 나타냄
@EnableWebSecurity        // Spring Security를 활성화함
@EnableMethodSecurity    // @PreAuthorize 같은 메서드 단위 인가를 켤 수 있게 함
@RequiredArgsConstructor
public class SecurityConfig {

	private final JwtAuthorizationFilter jwtAuthorizationFilter;

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
		return configuration.getAuthenticationManager();
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			.csrf(AbstractHttpConfigurer::disable)
			.formLogin(AbstractHttpConfigurer::disable)
			.httpBasic(AbstractHttpConfigurer::disable)
			.logout(AbstractHttpConfigurer::disable)
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.cors(cors -> cors.configurationSource(corsConfigurationSource()))
			.headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
			.addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class)
			.authorizeHttpRequests(auth -> auth
				.requestMatchers(
					"/swagger-ui/**",
					"/swagger",
					"/v3/api-docs/**",
					"/v3/api-docs.yaml",
					"/actuator/**"
				).permitAll()
				.anyRequest().authenticated()
			);

		return http.build();
	}

	private CorsConfigurationSource corsConfigurationSource() {
		return request -> {
			CorsConfiguration configuration = new CorsConfiguration();

			configuration.setAllowedHeaders(Collections.singletonList("*"));
			configuration.setAllowedMethods(Collections.singletonList("*"));
			configuration.setAllowedOriginPatterns(List.of(
				"http://localhost:[*]"
			));
			configuration.setAllowCredentials(true);

			return configuration;
		};
	}
}
