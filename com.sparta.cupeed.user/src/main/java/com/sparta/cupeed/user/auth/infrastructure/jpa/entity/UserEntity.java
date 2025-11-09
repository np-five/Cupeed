package com.sparta.cupeed.user.auth.infrastructure.jpa.entity;

import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "p_user")
public class UserEntity extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "id", nullable = false, updatable = false)
	private UUID id;

	@Column(name = "user_id", nullable = false, updatable = false, unique = true)
	private String userId;

	@Column(name = "password", nullable = false)
	private String password;

	@Column(name = "slack_id", nullable = false, unique = true)
	private String slackId;

	@Column(name = "role", nullable = false, updatable = false)
	private RoleEnum role;

	@Column(name = "status", nullable = false)
	private StatusEnum status;

	@OneToOne(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private UserCompanyEntity userCompany;

	public void attachUserCompany(UserCompanyEntity company) {
		this.userCompany = company;
		if (company != null) {
			company.setUser(this);
		}
	}
}
