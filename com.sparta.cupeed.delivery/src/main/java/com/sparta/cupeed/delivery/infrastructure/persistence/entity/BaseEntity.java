package com.sparta.cupeed.delivery.infrastructure.persistence.entity;

import java.time.Instant;

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
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

	@CreatedDate
	@Column(name = "created_at", updatable = false, nullable = false)
	private Instant createdAt;

	@CreatedBy
	@Column(name = "created_by", length = 100, updatable = false)
	private String createdBy;

	@LastModifiedDate
	@Column(name = "updated_at")
	private Instant updatedAt;

	@LastModifiedBy
	@Column(name = "updated_by", length = 100)
	private String updatedBy;

	@Column(name = "deleted_at")
	private Instant deletedAt;

	@Column(name = "deleted_by", length = 100)
	private String deletedBy;

	public void markDeleted(String deletedBy) {
		this.deletedAt = Instant.now();
		this.deletedBy = deletedBy;
	}
}