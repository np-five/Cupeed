package com.sparta.cupeed.delivery.infrastructure.ai.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HubResponseDtoV1 {

	private UUID id;
	private String name;
	private String address;
	private Double latitude;
	private Double longitude;
}