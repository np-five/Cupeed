package com.sparta.cupeed.slack.infrastructure.persistence.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sparta.cupeed.slack.infrastructure.persistence.entity.SlackEntity;

public interface SlackJpaRepository extends JpaRepository<SlackEntity, UUID> {

	@Query(
		value = """
        SELECT * 
        FROM p_slack_message
        WHERE similarity(message, CAST(:keyword AS text)) > 0.01
           OR similarity(recipient_slack_id, CAST(:keyword AS text)) > 0.01
           OR similarity(error_message, CAST(:keyword AS text)) > 0.01
        ORDER BY similarity(message, CAST(:keyword AS text)) DESC, sent_at DESC
        LIMIT :limit OFFSET :offset
        """,
		nativeQuery = true
	)
	List<SlackEntity> findByKeyword(
		@Param("keyword") String keyword,
		@Param("limit") int limit,
		@Param("offset") int offset
	);

	@Query(
		value = """
            SELECT COUNT(*) 
            FROM p_slack_message
            WHERE similarity(message, :keyword) > 0.01
               OR similarity(recipient_slack_id, :keyword) > 0.01
               OR similarity(error_message, :keyword) > 0.01
            """,
		nativeQuery = true
	)
	Long countByKeyword(@Param("keyword") String keyword);

	@Query(
		value = """
            SELECT * 
            FROM p_slack_message
            ORDER BY sent_at DESC
            LIMIT :limit OFFSET :offset
            """,
		nativeQuery = true
	)
	List<SlackEntity> findAllPaged(
		@Param("limit") int limit,
		@Param("offset") int offset
	);

	@Query(
		value = "SELECT COUNT(*) FROM p_slack_message",
		nativeQuery = true
	)
	Long countAll();

}
