package com.sparta.cupeed.hub.common;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class) // 엔티티 변경 이벤트를 감지하여 시간/사용자를 자동 업데이트
public abstract class BaseEntity {

	//todo 실제 프로젝트에서는 Spring Security 설정을 통해 Authentication 객체에서 사용자 정보를 가져와야 합니다.

	// 1. 생성 시간/사용자 필드
	@CreatedDate
	@Column(nullable = false, updatable = false)
	private LocalDateTime createdAt;

	@CreatedBy
	@Column(nullable = false, updatable = false, length = 50)
	private String createdBy = "system"; // 임시 기본값

	// 2. 생성/수정 사용자 필드 (String으로 임시 구현)
	@LastModifiedDate
	@Column(nullable = false)
	private LocalDateTime updatedAt;

	@LastModifiedBy
	@Column(nullable = false, length = 50)
	private String updatedBy = "system"; // 임시 기본값

	// 3. 소프트 딜리트 필드
	private LocalDateTime deletedAt;

	@Column(length = 50)
	private String deletedBy;

	// 소프트 딜리트 로직
	public void softDelete(String deleter) {
		this.deletedAt = LocalDateTime.now();
		this.deletedBy = deleter;
	}
}
