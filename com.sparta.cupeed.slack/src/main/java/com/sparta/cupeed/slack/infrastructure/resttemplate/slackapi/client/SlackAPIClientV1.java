package com.sparta.cupeed.slack.infrastructure.resttemplate.slackapi.client;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SlackAPIClientV1 {

	private final RestTemplate restTemplate;

	@Value("${slack.bot-token}")
	private String botToken;

	public void sendDirectMessage(String userId, String text) {
		String channelId = openDirectMessageChannel(userId); // DM 채널 열기
		String url = "https://slack.com/api/chat.postMessage";
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setBearerAuth(botToken);  // Authorization: Bearer xoxb-...
		Map<String, String> payload = Map.of(
			"channel", channelId,
			"text", text
		);
		HttpEntity<Map<String, String>> entity = new HttpEntity<>(payload, headers);
		restTemplate.postForEntity(url, entity, String.class);
	}

	public String openDirectMessageChannel(String userId) {
		String url = "https://slack.com/api/conversations.open";
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setBearerAuth(botToken);
		Map<String, String> payload = Map.of("users", userId);
		HttpEntity<Map<String, String>> entity = new HttpEntity<>(payload, headers);
		Map response = restTemplate.postForObject(url, entity, Map.class);
		Map channel = (Map) response.get("channel");
		String channelId = (channel != null) ? (String) channel.get("id") : null;
		return channelId;
	}

}
