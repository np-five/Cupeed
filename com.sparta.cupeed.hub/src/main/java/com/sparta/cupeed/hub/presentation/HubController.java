package com.sparta.cupeed.hub.presentation;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.cupeed.hub.application.HubService;
import com.sparta.cupeed.hub.application.command.CreateHubCommand;
import com.sparta.cupeed.hub.application.command.UpdateHubCommand;
import com.sparta.cupeed.hub.application.dto.HubResponseDto;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

// Presentation Layer: HTTP 요청을 받고 응답을 반환합니다.
@RestController
@RequestMapping("/v1/hubs")
@RequiredArgsConstructor
public class HubController {

	private final HubService hubService;

	// 1. 허브 생성 (POST /v1/hubs)
	@PostMapping
	public ResponseEntity<HubResponseDto> createHub(@Valid @RequestBody CreateHubCommand command) {
		HubResponseDto responseDto = hubService.createHub(command);
		return ResponseEntity.ok(responseDto);
	}

	// 2. 허브 목록 조회 (GET /v1/hubs)
	@GetMapping
	public ResponseEntity<List<HubResponseDto>> getAllHubs() {
		List<HubResponseDto> responseDto = hubService.getAllHubs();
		return ResponseEntity.ok(responseDto);
	}

	// 3. 허브 단건 조회 (GET /v1/hubs/{hubId})
	@GetMapping("/{hubId}")
	public ResponseEntity<HubResponseDto> getHubById(@PathVariable UUID hubId) {
		HubResponseDto responseDto = hubService.getHubById(hubId);
		return ResponseEntity.ok(responseDto);
	}

	// 4. 허브 수정 (PATCH /v1/hubs/{hubId})
	// 요청 예시:
	// PATCH /v1/hubs/aa0e8400-e29b-41d4-a716-446655440000
	// { "name" : "새로운 서울센터", "latitude" : 37.567890 }
	@PutMapping("/{hubId}")
	public ResponseEntity<HubResponseDto> updateHub(@PathVariable UUID hubId,
		@Valid @RequestBody UpdateHubCommand command) {
		HubResponseDto responseDto = hubService.updateHub(hubId, command);
		return ResponseEntity.ok(responseDto);
	}

	// 5. 허브 삭제 (DELETE /v1/hubs/{hubId})
	@DeleteMapping("/{hubId}")
	public ResponseEntity<Void> deleteHub(@PathVariable UUID hubId) {
		hubService.deleteHub(hubId);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/by-name")
	public ResponseEntity<HubResponseDto> getHubByName(@RequestParam String name) {
		HubResponseDto responseDto = hubService.getHubByName(name);
		return ResponseEntity.ok(responseDto);
	}
}
