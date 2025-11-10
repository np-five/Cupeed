package com.sparta.cupeed.user.user.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

	@GetMapping("/test")
	public String testEndpoint() {
		return "User Controller is working!";
	}
}
