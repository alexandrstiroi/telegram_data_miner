CREATE TABLE IF NOT EXISTS user_favorite (
    id SERIAL PRIMARY KEY,
    chat_id BIGINT NOT NULL,
    tender_id VARCHAR(255) NOT NULL,
    last_snapshot text,
    created_at TIMESTAMP DEFAULT now(),
    active boolean DEFAULT true
);