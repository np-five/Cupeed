package com.sparta.cupeed.company.domain.model;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@Builder
@EqualsAndHashCode(of = "id")
public class Company {

	private final UUID id;
	private final String name;
	private final String businessNumber;
	private final String address;
	private final UUID hubId;
	private final UUID managerId;

	private final Instant createdAt;
	private final String createdBy;
	private final Instant updatedAt;
	private final String updatedBy;
	private final Instant deletedAt;
	private final String deletedBy;

	// === 불변 객체 유지용 toBuilder() ===
	CompanyBuilder toBuilder() {
		return Company.builder()
			.id(id)
			.name(name)
			.businessNumber(businessNumber)
			.address(address)
			.hubId(hubId)
			.managerId(managerId)
			.createdAt(createdAt)
			.createdBy(createdBy)
			.updatedAt(updatedAt)
			.updatedBy(updatedBy)
			.deletedAt(deletedAt)
			.deletedBy(deletedBy);
	}

	// === 정보 수정 ===
	public Company withUpdatedInfo(String name, String businessNumber, String address, UUID hubId, UUID managerId) {
		return toBuilder()
			.name(name != null ? name : this.name)
			.businessNumber(businessNumber != null ? businessNumber : this.businessNumber)
			.address(address != null ? address : this.address)
			.hubId(hubId != null ? hubId : this.hubId)
			.managerId(managerId != null ? managerId : this.managerId)
			.updatedAt(Instant.now())
			.build();
	}

	// === 논리 삭제 ===
	// public Company markDeleted(String deletedBy) {
	// 	return toBuilder()
	// 		.deletedAt(Instant.now())
	// 		.deletedBy(deletedBy != null ? deletedBy : "system")
	// 		.build();
	// }

	public Company markDeleted() {
		return toBuilder()
			.deletedAt(Instant.now())
			.deletedBy("system")
			.build();
	}

	// === 생성 팩토리 ===
	public static Company create(String name, String businessNumber, String address, UUID hubId, UUID managerId) {
		Objects.requireNonNull(name, "업체명은 필수입니다.");
		Objects.requireNonNull(hubId, "허브 ID는 필수입니다.");

		return Company.builder()
			.id(UUID.randomUUID())
			.name(name)
			.businessNumber(businessNumber)
			.address(address)
			.hubId(hubId)
			.managerId(managerId)
			.createdAt(Instant.now())
			.build();
	}
}
