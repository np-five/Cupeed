package com.sparta.cupeed.ai.infrastructure.persistence.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sparta.cupeed.ai.infrastructure.persistence.entity.AiEntity;

public interface AiJpaRepository extends JpaRepository<AiEntity, UUID> {

	@Query(
		value = """
            SELECT * 
            FROM p_ai_request 
            WHERE similarity(ai_response_text, :keyword) > 0.01
            ORDER BY similarity(ai_response_text, :keyword) DESC, created_at DESC 
            LIMIT :limit OFFSET :offset
            """,
		nativeQuery = true
	)
	List<AiEntity> findBySimilarityScore(
		@Param("keyword") String keyword,
		@Param("limit") int limit,
		@Param("offset") int offset
	);

	@Query(
		value = """
            SELECT COUNT(*) 
            FROM p_ai_request 
            WHERE similarity(ai_response_text, :keyword) > 0.01
            """,
		nativeQuery = true
	)
	Long countBySimilarityScore(@Param("keyword") String keyword);

}
