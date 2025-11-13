-- pg_trgm 확장 설치
CREATE EXTENSION IF NOT EXISTS pg_trgm;

CREATE INDEX IF NOT EXISTS idx_ai_response_text_trgm
    ON p_ai_request
    USING gin (ai_response_text gin_trgm_ops);

-- 확인 (선택사항)
SELECT * FROM pg_extension WHERE extname = 'pg_trgm';
