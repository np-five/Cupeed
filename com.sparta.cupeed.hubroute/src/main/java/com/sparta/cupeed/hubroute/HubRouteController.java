package com.sparta.cupeed.hubroute;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HubRouteController {

	@Value("${server.port}")
	private String serverPort;

	@GetMapping("/v1/hub-route")
	public String getHubRoute() {
		return "Route INFO [ FROM PORT : " + serverPort + "]";
	}
}
