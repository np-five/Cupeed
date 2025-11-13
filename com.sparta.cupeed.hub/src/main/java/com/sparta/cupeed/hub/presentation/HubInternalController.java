package com.sparta.cupeed.hub.presentation;

import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.cupeed.hub.application.HubService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/internal/v1/hubs")
@RequiredArgsConstructor
public class HubInternalController {

	private final HubService hubService;

	@GetMapping("/{hubName}")
	public HubInternalResponseDtoV1 getInternalHubByName(@PathVariable String hubName) {
		return hubService.getInternalHubByName(hubName);
	}
}
