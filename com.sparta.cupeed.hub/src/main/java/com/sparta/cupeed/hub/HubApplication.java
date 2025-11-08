package com.sparta.cupeed.hub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.sparta.cupeed.hub.infrastructure.jpa")
public class HubApplication {

	public static void main(String[] args) {
		SpringApplication.run(HubApplication.class, args);
	}

}
