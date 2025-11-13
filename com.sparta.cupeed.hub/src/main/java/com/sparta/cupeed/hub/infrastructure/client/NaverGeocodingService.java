package com.sparta.cupeed.hub.infrastructure.client;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class NaverGeocodingService {

	private final RestTemplate restTemplate = new RestTemplate();
	private final ObjectMapper objectMapper = new ObjectMapper();

	@Value("${naver.api.client-id}")
	private String CLIENT_ID;

	@Value("${naver.api.client-secret}")
	private String CLIENT_SECRET;

	private final String API_URL = "https://maps.apigw.ntruss.com/map-geocode/v2/geocode";

	public Coordinates getCoordinatesFromAddress(String address) {

		log.info("==================== Geocoding API 호출 시작 ====================");
		log.info("입력 주소: {}", address);

		// ⭐ 1단계: 주소 문자열 정제 ⭐
		// 유니코드 공백 문자(\u00A0: Non-breaking Space) 등을 일반 공백으로 치환
		String cleanAddress = address.replaceAll("\\u00A0|\\uFEFF", " ").trim();
		log.info("입력 주소 (정제 후): {}", cleanAddress);

		// 1. 요청 헤더 설정
		HttpHeaders headers = new HttpHeaders();
		headers.set("X-NCP-APIGW-API-KEY-ID", CLIENT_ID);
		headers.set("X-NCP-APIGW-API-KEY", CLIENT_SECRET);
		headers.set("Accept", "application/json");
		HttpEntity<?> entity = new HttpEntity<>(headers);

		// 2. 요청 URL 구성
		String urlTemplate = UriComponentsBuilder.fromHttpUrl(API_URL)
			.queryParam("query", cleanAddress)
			// .encode(StandardCharsets.UTF_8)
			.build()
			.toUriString();

		log.info("최종 요청 URL: {}", urlTemplate);

		// 3. API 호출
		ResponseEntity<String> response;
		try {
			response = restTemplate.exchange(
				urlTemplate,
				HttpMethod.GET,
				entity,
				String.class
			);
		} catch (Exception e) {
			log.error("API 호출 중 예외 발생", e);
			throw new RuntimeException("네이버 Geocoding API 호출 중 오류 발생: " + e.getMessage(), e);
		}

		log.info("응답 상태 코드: {}", response.getStatusCode());
		log.info("응답 JSON: {}", response.getBody());

		// 4. JSON 파싱
		NaverGeocodingResponse geocodeResponse;
		try {
			geocodeResponse = objectMapper.readValue(response.getBody(), NaverGeocodingResponse.class);
		} catch (Exception e) {
			log.error("JSON 파싱 실패", e);
			throw new RuntimeException("응답 JSON 파싱 실패: " + e.getMessage(), e);
		}

		log.info("파싱 성공 - status: {}, totalCount: {}",
			geocodeResponse.getStatus(),
			geocodeResponse.getMeta() != null ? geocodeResponse.getMeta().getTotalCount() : 0);

		// 5. 응답 검증 및 좌표 추출
		if (!"OK".equals(geocodeResponse.getStatus())) {
			log.error("API 응답 status가 OK가 아님: {}, errorMessage: {}",
				geocodeResponse.getStatus(), geocodeResponse.getErrorMessage());
			throw new RuntimeException("Geocoding API 오류: " + geocodeResponse.getErrorMessage());
		}

		List<NaverGeocodingResponse.AddressInfo> addresses = geocodeResponse.getAddresses();

		if (addresses == null || addresses.isEmpty()) {
			log.error("주소 변환 실패 - addresses가 null이거나 비어있음");
			throw new RuntimeException("주소에 대한 좌표 정보를 찾을 수 없습니다: " + address);
		}

		NaverGeocodingResponse.AddressInfo firstAddress = addresses.get(0);
		log.info("변환된 주소: roadAddress={}, jibunAddress={}, x={}, y={}",
			firstAddress.getRoadAddress(),
			firstAddress.getJibunAddress(),
			firstAddress.getX(),
			firstAddress.getY());

		try {
			double latitude = Double.parseDouble(firstAddress.getY());
			double longitude = Double.parseDouble(firstAddress.getX());

			log.info("✅ 변환 성공! 위도: {}, 경도: {}", latitude, longitude);
			log.info("==================== Geocoding API 호출 종료 ====================");

			return new Coordinates(latitude, longitude);
		} catch (NumberFormatException e) {
			log.error("좌표값 파싱 실패: x={}, y={}", firstAddress.getX(), firstAddress.getY());
			throw new RuntimeException("좌표값 파싱 실패: " + e.getMessage());
		}
	}
}