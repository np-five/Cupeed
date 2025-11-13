package com.sparta.cupeed.delivery.domain.model;

// 배송 경로 상태 Enum
public enum RouteStatus {
	READY,          // 허브 이동 대기중
	HUB_TRANSIT,    // 허브 이동중
	HUB_ARRIVED     // 목적지 허브 도착
}
