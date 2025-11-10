package com.sparta.cupeed.slack.infrastructure.persistence.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.sparta.cupeed.slack.domain.model.Slack;
import com.sparta.cupeed.slack.domain.repository.SlackRepository;
import com.sparta.cupeed.slack.infrastructure.persistence.entity.SlackEntity;
import com.sparta.cupeed.slack.infrastructure.persistence.mapper.SlackMapper;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class SlackRepositoryImpl implements SlackRepository {
	private final SlackJpaRepository slackJpaRepository;
	private final SlackMapper slackMapper;

	@Override
	public Slack save(Slack slack) {
		SlackEntity slackEntity;
		slackEntity = slackMapper.toEntity(slack);
		SlackEntity saved = slackJpaRepository.save(slackEntity);
		return slackMapper.toDomain(saved);
	}

	@Override
	public Optional<Slack> findById(UUID slackMessageId) {
		return slackJpaRepository.findById(slackMessageId).map(slackMapper::toDomain);
	}

	@Override
	public Page<Slack> findAll(Pageable pageable) {
		return slackJpaRepository.findAll(pageable).map(slackMapper::toDomain);
	}
}
