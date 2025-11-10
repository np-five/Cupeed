package com.sparta.cupeed.hubroute.infrastructure;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.sparta.cupeed.hubroute.domain.HubRoute;
import com.sparta.cupeed.hubroute.domain.HubRouteRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class HubRouteRepositoryImpl implements HubRouteRepository {

	private final HubRouteJpaRepository jpaRepository;

	@Override
	public HubRoute save(HubRoute hubRoute) {
		return jpaRepository.save(hubRoute);
	}

	@Override
	public Optional<HubRoute> findById(UUID id) {
		return jpaRepository.findById(id);
	}

	@Override
	public List<HubRoute> findAll() {
		return jpaRepository.findAll();
	}

	@Override
	public void delete(HubRoute hubRoute) {
		jpaRepository.delete(hubRoute);
	}
}
