package com.sparta.cupeed.user.infrastructure.hub.dto.response;

import java.util.UUID;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class HubGetResponseDtoV1 {
	UUID id;
	String name;
	String address;
	double latitude;
	double longitude;
}

