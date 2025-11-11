package com.sparta.cupeed.hubroute.application;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.sparta.cupeed.hubroute.application.command.UpdateHubRouteCommand;
import com.sparta.cupeed.hubroute.application.dto.HubRouteResponse;
import com.sparta.cupeed.hubroute.domain.HubRoute;
import com.sparta.cupeed.hubroute.domain.HubRouteRepository;
import com.sparta.cupeed.hubroute.infrastructure.client.HubClient;
import com.sparta.cupeed.hubroute.infrastructure.client.NaverMapClient;
import com.sparta.cupeed.hubroute.infrastructure.dto.HubResponseDto;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class HubRouteService {

	private final HubRouteRepository hubRouteRepository;
	private final HubClient hubClient;
	private final NaverMapClient naverMapClient;

	@Transactional
	public HubRouteResponse createDynamicHubRoute(UUID startHubId, UUID endHubId) {
		// 출발지/도착지 Hub 검증 (Feign Client 사용)
		validateHubs(startHubId, endHubId);

		// 기존 경로가 있다면 그대로 리턴
		Optional<HubRoute> existing = hubRouteRepository.findByStartHubIdAndEndHubId(startHubId, endHubId);
		if (existing.isPresent()) {
			return HubRouteResponse.mapToResponse(existing.get());
		}

		// 허브 좌표 조회 (Feign Client 사용)
		HubResponseDto startHub = hubClient.getHubById(startHubId);
		HubResponseDto endHub = hubClient.getHubById(endHubId);

		// 네이버 API로 거리/시간 계산
		NaverMapClient.RouteInfo routeInfo = naverMapClient.getRoute(
			startHub.getLatitude(), startHub.getLongitude(),
			endHub.getLatitude(), endHub.getLongitude()
		);

		// 저장(캐시)
		HubRoute newRoute = new HubRoute(
			startHubId,
			endHubId,
			routeInfo.getDuration(),
			routeInfo.getDistance()
		);
		hubRouteRepository.save(newRoute);

		return HubRouteResponse.mapToResponse(newRoute);
	}

	// 1. 허브 라우트 생성 (POST /v1/hub-routes)
	// public HubRouteResponse createHubRoute(CreateHubRouteCommand request) {
	//
	// 	// 출발지/도착지 Hub 검증 (Feign Client 사용)
	// 	validateHubs(request.getStartHubId(), request.getEndHubId());
	//
	// 	HubRoute newRoute = new HubRoute(
	// 		request.getStartHubId(),
	// 		request.getEndHubId(),
	// 		request.getDuration(),
	// 		request.getDistance()
	// 	);
	//
	// 	HubRoute savedRoute = hubRouteRepository.save(newRoute);
	// 	return HubRouteResponse.mapToResponse(savedRoute);
	// }

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

		// 출발지/도착지 Hub 검증 (Feign Client 사용)
		validateHubs(request.getStartHubId(), request.getEndHubId());

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

	private void validateHubs(UUID startHubId, UUID endHubId) {
		// 출발지 Hub 검증 (Feign Client 사용)
		if (!hubClient.checkHubExists(startHubId)) {
			throw new IllegalArgumentException("출발지 Hub ID를 찾을 수 없습니다.");
		}

		// 도착지 Hub 검증 (Feign Client 사용)
		if (!hubClient.checkHubExists(endHubId)) {
			throw new IllegalArgumentException("도착지 Hub ID를 찾을 수 없습니다.");
		}
	}
}
