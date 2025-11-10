package com.sparta.cupeed.slack.domain.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.sparta.cupeed.slack.domain.model.Slack;

public interface SlackRepository {
	Slack save(Slack slack);

	Optional<Slack> findById(UUID slackMessageId);

	Page<Slack> findAll(Pageable pageable);
}
