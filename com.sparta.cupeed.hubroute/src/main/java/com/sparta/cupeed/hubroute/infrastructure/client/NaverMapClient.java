package com.sparta.cupeed.hubroute.infrastructure.client;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Component
public class NaverMapClient {

	@Value("${naver.api.client-id}")
	private String clientId;

	@Value("${naver.api.client-secret}")
	private String clientSecret;

	private final RestTemplate restTemplate = new RestTemplate();

	public RouteInfo getRoute(double startLat, double startLng, double endLat, double endLng) {
		String url = String.format(
			"https://maps.apigw.ntruss.com/map-direction-15/v1/driving?start=%f,%f&goal=%f,%f&option=trafast",
			startLng, startLat, endLng, endLat
		);

		HttpHeaders headers = new HttpHeaders();
		headers.set("X-NCP-APIGW-API-KEY-ID", clientId);
		headers.set("X-NCP-APIGW-API-KEY", clientSecret);

		try {
			HttpEntity<String> entity = new HttpEntity<>(headers);
			ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);

			List<?> trafast = (List<?>)((Map<?, ?>)response.getBody().get("route")).get("trafast");
			Map<?, ?> summary = (Map<?, ?>)((Map<?, ?>)trafast.get(0)).get("summary");

			double distance = ((Number)summary.get("distance")).doubleValue() / 1000;  // m → km
			double duration = ((Number)summary.get("duration")).doubleValue() / 3600000; // ms → h

			return new RouteInfo(distance, duration);
		} catch (HttpClientErrorException e) {
			// HTTP 에러 상세 정보 출력
			System.err.println("HTTP Status: " + e.getStatusCode());
			System.err.println("Response Body: " + e.getResponseBodyAsString());
			throw new RuntimeException("Naver API 인증 오류: " + e.getMessage(), e);
		} catch (RestClientException e) {
			throw new RuntimeException("Naver API 호출 중 오류 발생", e);
		}
	}

	@Getter
	@AllArgsConstructor
	public static class RouteInfo {
		private double distance;
		private double duration;
	}
}
