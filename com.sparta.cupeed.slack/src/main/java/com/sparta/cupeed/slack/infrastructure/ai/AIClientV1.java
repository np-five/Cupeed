package com.sparta.cupeed.slack.infrastructure.ai;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name="ai")
public interface AIClientV1 {
}
