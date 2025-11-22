package com.sparta.cupeed.order.application.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.sparta.cupeed.order.infrastructure.slack.client.SlackClientV1;
import com.sparta.cupeed.order.infrastructure.slack.dto.request.SlackMessageCreateRequestDtoV1;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderAsyncServiceV1 {
	private final SlackClientV1 slackClient;

	@Async("slackTaskExecutor")
	public void sendSlackMessageAsync(SlackMessageCreateRequestDtoV1 request) {
		slackClient.dmToReceiveCompany(request);
	}
}
