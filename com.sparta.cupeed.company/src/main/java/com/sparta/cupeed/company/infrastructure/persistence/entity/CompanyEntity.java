package com.sparta.cupeed.company.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "p_company")
public class CompanyEntity extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@Column(nullable = false, length = 50)
	private String name;

	@Column(name = "business_number", unique = true, length = 50)
	private String businessNumber;

	@Column(length = 255)
	private String address;

	@Column(name = "hub_id")
	private UUID hubId;

	@Column(name = "manager_id")
	private UUID managerId;

	// == 편의 메서드 == //
	public void updateInfo(String name, String businessNumber, String address, UUID hubId, UUID managerId) {
		if (name != null && !name.isBlank())
			this.name = name;
		if (businessNumber != null && !businessNumber.isBlank())
			this.businessNumber = businessNumber;
		if (address != null && !address.isBlank())
			this.address = address;
		if (hubId != null)
			this.hubId = hubId;
		if (managerId != null)
			this.managerId = managerId;
	}

}
