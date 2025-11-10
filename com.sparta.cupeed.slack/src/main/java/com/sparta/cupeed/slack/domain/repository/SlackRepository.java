package com.sparta.cupeed.slack.domain.repository;

import com.sparta.cupeed.slack.domain.model.Slack;

public interface SlackRepository {
	Slack save(Slack slack);
}
