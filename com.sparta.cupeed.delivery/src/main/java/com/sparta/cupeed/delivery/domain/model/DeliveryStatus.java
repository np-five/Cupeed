package com.sparta.cupeed.delivery.domain.model;

// 배송 상태 Enum
public enum DeliveryStatus {
	READY,              // 허브 대기중
	HUB_TRANSIT,        // 허브 이동중
	HUB_ARRIVED,        // 목적지 허브 도착
	COMPANY_TRANSIT,    // 업체 이동중
	DELIVERED           // 배송 완료
}

