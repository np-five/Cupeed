package com.sparta.cupeed.hub.application;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sparta.cupeed.hub.application.command.CreateHubCommand;
import com.sparta.cupeed.hub.application.command.UpdateHubCommand;
import com.sparta.cupeed.hub.application.dto.HubResponseDto;
import com.sparta.cupeed.hub.domain.Hub;
import com.sparta.cupeed.hub.domain.HubRepository;
import com.sparta.cupeed.hub.infrastructure.client.Coordinates;
import com.sparta.cupeed.hub.infrastructure.client.NaverGeocodingService;

import lombok.RequiredArgsConstructor;

// @Transactional: 애플리케이션 서비스에서 트랜잭션을 관리합니다.
// 이 서비스는 도메인 객체(Hub)를 로드하고, 도메인의 비즈니스 메서드를 호출하며, 결과를 저장소에 저장하는 '조율자' 역할을 합니다.
@Service
@RequiredArgsConstructor
public class HubService {

	private final HubRepository hubRepository;
	private final NaverGeocodingService geocodingService;

	// 1. 허브 생성 (POST /v1/hubs)
	@Transactional
	public HubResponseDto createHub(CreateHubCommand command) {
		// ⭐ 주소를 이용해 위도/경도 조회 ⭐
		Coordinates coordinates = geocodingService.getCoordinatesFromAddress(command.getAddress());

		// 1. Command를 사용하여 Domain Entity 생성
		Hub newHub = new Hub(
			command.getName(),
			command.getAddress(),
			coordinates.getLatitude(),
			coordinates.getLongitude()
		);

		// 2. Repository를 사용하여 DB에 저장
		Hub savedHub = hubRepository.save(newHub);

		// 3. Response DTO로 변환하여 반환
		return HubResponseDto.mapToResponse(savedHub);
	}

	// 2. 허브 단건 조회 (GET /v1/hubs/{hubId})
	@Transactional(readOnly = true)
	public HubResponseDto getHubById(UUID hubId) {
		Hub hub = hubRepository.findById(hubId)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않은 ID입니다." + hubId));
		return HubResponseDto.mapToResponse(hub);
	}

	// 3. 허브 목록 조회 (GET /v1/hubs)
	@Transactional(readOnly = true)
	public List<HubResponseDto> getAllHubs() {
		return hubRepository.findAll().stream()
			.map(HubResponseDto::mapToResponse)
			.collect(Collectors.toList());
	}

	// 4. 허브 수정 (PUT /v1/hubs/{hubId})
	@Transactional
	public HubResponseDto updateHub(UUID hubId, UpdateHubCommand command) {
		// 1. Repository를 통해 Hub 애그리거트 루트를 로드
		Hub hub = hubRepository.findById(hubId)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않은 ID입니다." + hubId));

		// 2. Domain 객체의 비즈니스 메서드 호출
		hub.updateInfo(
			command.getName() != null ? command.getName() : hub.getName(),
			command.getAddress() != null ? command.getAddress() : hub.getAddress(),
			command.getLatitude() != null ? command.getLatitude() : hub.getLatitude(),
			command.getLongitude() != null ? command.getLongitude() : hub.getLongitude()
		);

		// 3. Response DTO로 변환하여 반환
		return HubResponseDto.mapToResponse(hub);
	}

	// 5. 허브 삭제 (DELETE /v1/hubs/{hubId})
	@Transactional
	public void deleteHub(UUID hubId) {
		Hub hub = hubRepository.findById(hubId)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않은 ID입니다." + hubId));

		// 소프트 딜리트
		hub.softDelete("system");
	}

	public HubResponseDto getHubByName(String name) {
		Hub hub = hubRepository.findByName(name)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않은 허브 이름입니다."));
		return HubResponseDto.mapToResponse(hub);
	}
}
