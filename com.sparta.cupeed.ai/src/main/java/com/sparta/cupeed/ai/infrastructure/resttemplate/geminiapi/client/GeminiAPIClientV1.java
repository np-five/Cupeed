package com.sparta.cupeed.ai.infrastructure.resttemplate.geminiapi.client;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sparta.cupeed.ai.infrastructure.resttemplate.config.GoogleAiProperties;
import com.sparta.cupeed.ai.presentation.advice.AiError;
import com.sparta.cupeed.ai.presentation.advice.AiException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class GeminiAPIClientV1 {

	private final RestTemplate restTemplate;
	private final GoogleAiProperties googleAiProperties;

	public String createAiText(String prompt) {
		String apiKey = googleAiProperties.getApiKey();
		String apiUrl = googleAiProperties.getUrl();

		if (apiKey == null || apiKey.isBlank()) {
			throw new AiException(AiError.AI_KEY_MISSING);
		}

		try {
			// 요청 헤더 구성
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.set("x-goog-api-key", apiKey); // API 키 인증 방식

			// 요청 바디 구성
			Map<String, Object> content = new HashMap<>();
			content.put("parts", List.of(Map.of("text", prompt)));

			Map<String, Object> requestBody = new HashMap<>();
			requestBody.put("contents", List.of(content));

			HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

			// API 호출
			ResponseEntity<String> response =
				restTemplate.exchange(apiUrl, HttpMethod.POST, entity, String.class);

			// 응답 파싱
			JsonObject json = JsonParser.parseString(response.getBody()).getAsJsonObject();

			return json.getAsJsonArray("candidates")
				.get(0).getAsJsonObject()
				.getAsJsonObject("content")
				.getAsJsonArray("parts")
				.get(0).getAsJsonObject()
				.get("text").getAsString();

		} catch (com.google.gson.JsonSyntaxException e) {
			throw new AiException(AiError.GEMINI_AI_JSON_PARSE_FAILED, e);
		} catch (Exception e) {
			throw new AiException(AiError.GEMINI_AI_API_CALL_FAILED);
		}
	}

	// 비동기 메서드 추가
	@Async("aiTaskExecutor")
	public CompletableFuture<String> createAiTextAsync(String prompt) {
		try {
			String result = createAiText(prompt);
			return CompletableFuture.completedFuture(result);
		} catch (Exception e) {
			log.error("[GeminiAPIClientV1] 비동기 AI 생성 실패: {}", e.getMessage());
			return CompletableFuture.failedFuture(e);
		}
	}
}
