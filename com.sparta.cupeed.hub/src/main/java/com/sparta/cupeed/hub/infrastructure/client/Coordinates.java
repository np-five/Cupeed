package com.sparta.cupeed.hub.infrastructure.client;

import lombok.AllArgsConstructor;
import lombok.Getter;

// Coordinates.java (좌표 정보를 담을 최종 DTO)
@Getter
@AllArgsConstructor
public class Coordinates {
	private double latitude; // 위도 (y)
	private double longitude; // 경도 (x)
}
