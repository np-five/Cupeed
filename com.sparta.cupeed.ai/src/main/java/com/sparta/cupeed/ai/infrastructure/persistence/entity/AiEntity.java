package com.sparta.cupeed.ai.infrastructure.persistence.entity;

import java.time.Instant;
import java.util.UUID;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "p_ai_request")
@DynamicInsert
@DynamicUpdate
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id", callSuper = false)
public class AiEntity extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "id", nullable = false, updatable = false, unique = true)
	private UUID id;

	@Column(name = "order_id", length = 50, nullable = false)
	private UUID orderId;

	@Column(name = "ai_response_text", columnDefinition = "TEXT")
	private String aiResponseText;

	@Enumerated(EnumType.STRING)
	@Column(length = 20, nullable = false)
	private Status status;

	@Column(name = "error_message", columnDefinition = "TEXT")
	private String errorMessage;

	public enum Status {
		SUCCESS,
		FAILED
	}
}
