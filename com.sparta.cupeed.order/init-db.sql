-- pg_trgm 확장 설치
CREATE EXTENSION IF NOT EXISTS pg_trgm;

-- 확인 (선택사항)
SELECT * FROM pg_extension WHERE extname = 'pg_trgm';
