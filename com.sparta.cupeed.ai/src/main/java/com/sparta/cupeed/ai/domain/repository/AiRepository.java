package com.sparta.cupeed.ai.domain.repository;

import com.sparta.cupeed.ai.domain.model.Ai;

public interface AiRepository {
	Ai save(Ai created);
}
