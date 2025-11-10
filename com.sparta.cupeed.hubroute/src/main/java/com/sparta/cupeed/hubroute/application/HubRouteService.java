package com.sparta.cupeed.hubroute.application;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.sparta.cupeed.hubroute.application.command.CreateHubRouteCommand;
import com.sparta.cupeed.hubroute.application.command.UpdateHubRouteCommand;
import com.sparta.cupeed.hubroute.application.dto.HubRouteResponse;
import com.sparta.cupeed.hubroute.domain.HubRoute;
import com.sparta.cupeed.hubroute.domain.HubRouteRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class HubRouteService {

	private final HubRouteRepository hubRouteRepository;

	public HubRouteService(HubRouteRepository hubRouteRepository) {
		this.hubRouteRepository = hubRouteRepository;
	}

	// 1. 허브 라우트 생성 (POST /v1/hub-routes)
	public HubRouteResponse createHubRoute(CreateHubRouteCommand request) {
		HubRoute newRoute = new HubRoute(
			request.getStartHubId(),
			request.getEndHubId(),
			request.getDuration(),
			request.getDistance()
		);

		HubRoute savedRoute = hubRouteRepository.save(newRoute);
		return HubRouteResponse.mapToResponse(savedRoute);
	}

	// 2. 허브 라우트 목록 조회 (GET /v1/hub-routes)
	public List<HubRouteResponse> findAllHubRoutes() {
		return hubRouteRepository.findAll().stream()
			.map(HubRouteResponse::mapToResponse)
			.collect(Collectors.toList());
	}

	// 3. 허브 라우트 단건 조회 (GET /v1/hub-routes/{hubRouteId})
	public HubRouteResponse findHubRouteById(UUID id) {
		HubRoute route = hubRouteRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("Hub Route ID를 찾을 수 없습니다"));

		return HubRouteResponse.mapToResponse(route);
	}

	// 4. 허브 라우트 수정 (PUT /v1/hub-routes/{hubRouteId})
	@Transactional
	public HubRouteResponse updateHubRoute(UUID id, UpdateHubRouteCommand request) {
		HubRoute route = hubRouteRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("Hub Route ID를 찾을 수 없습니다"));

		route.updateInfo(
			request.getStartHubId(),
			request.getEndHubId(),
			request.getDuration(),
			request.getDistance()
		);

		if (route.getStartHubId().equals(route.getEndHubId())) {
			throw new IllegalArgumentException("출발지 Hub와 도착지 Hub는 동일할 수 없습니다.");
		}

		return HubRouteResponse.mapToResponse(route);
	}

	// 5. 허브 라우트 삭제 (DELETE /v1/hub-routes/{hubRouteId})
	@Transactional
	public void deleteHubRoute(UUID id) {
		HubRoute route = hubRouteRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("Hub Route ID를 찾을 수 없습니다"));

		hubRouteRepository.delete(route);
	}
}
