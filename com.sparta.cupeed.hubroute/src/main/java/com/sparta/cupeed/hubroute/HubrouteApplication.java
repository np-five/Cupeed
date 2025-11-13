package com.sparta.cupeed.hubroute;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class HubrouteApplication {

	public static void main(String[] args) {
		SpringApplication.run(HubrouteApplication.class, args);
	}

}
