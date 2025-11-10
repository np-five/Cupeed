package com.sparta.cupeed.hubroute.infrastructure;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sparta.cupeed.hubroute.domain.HubRoute;

public interface HubRouteJpaRepository extends JpaRepository<HubRoute, UUID> {
}
