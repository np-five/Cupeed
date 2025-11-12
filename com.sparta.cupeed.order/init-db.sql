-- pg_trgm 확장 설치
CREATE EXTENSION IF NOT EXISTS pg_trgm;

-- gin 인덱스 생성
CREATE INDEX IF NOT EXISTS idx_order_number_trgm
    ON p_order USING gin (order_number gin_trgm_ops);

CREATE INDEX IF NOT EXISTS idx_recieve_company_name_trgm
    ON p_order USING gin (recieve_company_name gin_trgm_ops);



CREATE EXTENSION IF NOT EXISTS unaccent;


-- 확인 (선택사항)
SELECT * FROM pg_extension WHERE extname = 'pg_trgm';
