package com.sparta.cupeed.hubroute.infrastructure.dto;

import java.util.UUID;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class HubResponseDto {
	private UUID id;
	private String name;
	private String address;
	private double latitude;
	private double longitude;
}
