package com.sparta.cupeed.order.infrastructure.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.sparta.cupeed.order.infrastructure.security.jwt.JwtProperties;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class OpenApiConfig {

	private final JwtProperties jwtProperties;

	// Swagger에서 Bearer 인가 넣기
	@Bean
	public OpenAPI openApi() {
		return new OpenAPI()
			.info(new Info().title("Cupeed User API").version("v1"))
			.components(new Components()
				.addSecuritySchemes(jwtProperties.getBearerSchema(),
					new SecurityScheme()
						.name(jwtProperties.getBearerSchema())
						.type(SecurityScheme.Type.HTTP)
						.scheme("bearer")
						.bearerFormat("JWT")))
			.addSecurityItem(new SecurityRequirement().addList(jwtProperties.getBearerSchema()));
	}
}
