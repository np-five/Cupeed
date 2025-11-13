-- 1. pg_trgm 확장 설치
CREATE EXTENSION IF NOT EXISTS pg_trgm;

-- 2. 유사도 검색 성능 향상을 위한 GIN 인덱스 생성
CREATE INDEX IF NOT EXISTS idx_slack_message_trgm
    ON p_slack_message
    USING gin (message gin_trgm_ops);

CREATE INDEX IF NOT EXISTS idx_slack_recipient_trgm
    ON p_slack_message
    USING gin (recipient_slack_id gin_trgm_ops);
