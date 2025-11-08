package com.sparta.cupeed.ai.infrastructure.persistence.repository;

import org.springframework.stereotype.Repository;

import com.sparta.cupeed.ai.domain.model.Ai;
import com.sparta.cupeed.ai.domain.repository.AiRepository;
import com.sparta.cupeed.ai.infrastructure.persistence.entity.AiEntity;
import com.sparta.cupeed.ai.infrastructure.persistence.mapper.AiMapper;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class AiRepositoryImpl implements AiRepository {

	private final AiJpaRepository aiJpaRepository;
	private final AiMapper aiMapper;

	@Override
	public Ai save(Ai created) {
		AiEntity aiEntity = aiMapper.toEntity(created);
		AiEntity saved = aiJpaRepository.save(aiEntity);
		return aiMapper.toDomain(saved);
	}
}
