package com.sparta.cupeed.hubroute.presentation;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.cupeed.hubroute.application.HubRouteService;
import com.sparta.cupeed.hubroute.application.command.CreateHubRouteCommand;
import com.sparta.cupeed.hubroute.application.command.UpdateHubRouteCommand;
import com.sparta.cupeed.hubroute.application.dto.HubRouteResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/hub-routes")
public class HubRouteController {

	private final HubRouteService hubRouteService;

	// POST /v1/hub_routes : 새로운 HubRoute 생성
	@PostMapping
	public ResponseEntity<HubRouteResponse> createHubRoute(@Valid @RequestBody CreateHubRouteCommand request) {
		// HubRouteResponse response = hubRouteService.createHubRoute(request);
		HubRouteResponse response = hubRouteService.createDynamicHubRoute(request.getStartHubId(),
			request.getEndHubId());
		return ResponseEntity.ok(response);
	}

	// GET /v1/hub_routes : 모든 HubRoute 조회
	@GetMapping
	public ResponseEntity<List<HubRouteResponse>> getAllHubRoutes() {
		List<HubRouteResponse> responses = hubRouteService.findAllHubRoutes();
		return ResponseEntity.ok(responses);
	}

	// GET /v1/hub_routes/{hubRouteId} : 특정 ID의 HubRoute 조회
	@GetMapping("/{hubRouteId}")
	public ResponseEntity<HubRouteResponse> getHubRouteById(@PathVariable UUID hubRouteId) {
		HubRouteResponse response = hubRouteService.findHubRouteById(hubRouteId);
		return ResponseEntity.ok(response);
	}

	// PUT /v1/hub_routes/{id} : HubRoute 수정
	@PutMapping("/{id}")
	public ResponseEntity<HubRouteResponse> updateHubRoute(@PathVariable UUID id,
		@Valid @RequestBody UpdateHubRouteCommand request) {
		HubRouteResponse response = hubRouteService.updateHubRoute(id, request);
		return ResponseEntity.ok(response);
	}

	// DELETE /api/v1/hub_routes/{id} : HubRoute 삭제
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteHubRoute(@PathVariable UUID id) {
		hubRouteService.deleteHubRoute(id);
		return ResponseEntity.ok().build(); // 204 No Content 반환
	}
}