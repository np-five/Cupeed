package com.sparta.cupeed.ai.infrastructure.persistence.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.sparta.cupeed.ai.domain.model.Ai;
import com.sparta.cupeed.ai.domain.repository.AiRepository;
import com.sparta.cupeed.ai.infrastructure.persistence.entity.AiEntity;
import com.sparta.cupeed.ai.infrastructure.persistence.mapper.AiMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
@RequiredArgsConstructor
public class AiRepositoryImpl implements AiRepository {

	private final AiJpaRepository aiJpaRepository;
	private final AiMapper aiMapper;

	@Override
	public Ai save(Ai created) {
		AiEntity entity = aiMapper.toEntity(created);
		AiEntity saved = aiJpaRepository.save(entity);
		return aiMapper.toDomain(saved);
	}

	@Override
	public Optional<Ai> findById(UUID aiRequestId) {
		return aiJpaRepository.findById(aiRequestId).map(aiMapper::toDomain);
	}

	@Override
	public Page<Ai> findAll(Pageable pageable) {
		return aiJpaRepository.findAll(pageable).map((aiMapper::toDomain));
	}

	// pg_trgm + GIN 인덱스 + similarity() + Native Query
	@Override
	public Page<Ai> searchAiHistories(String keyword, Pageable pageable) {
		int offset = (int) pageable.getOffset();
		int limit = pageable.getPageSize();

		List<AiEntity> entities = aiJpaRepository.findBySimilarityScore(keyword, limit, offset);
		Long total = aiJpaRepository.countBySimilarityScore(keyword);

		List<Ai> domainResults = entities.stream()
			.map(aiMapper::toDomain)
			.toList();

		return new PageImpl<>(domainResults, pageable, total);
	}
}
