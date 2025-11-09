package com.sparta.cupeed.ai.domain.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.sparta.cupeed.ai.domain.model.Ai;

public interface AiRepository {
	Ai save(Ai created);

	Optional<Ai> findById(UUID aiRequestId);

	Page<Ai> findAll(Pageable pageable);
}
