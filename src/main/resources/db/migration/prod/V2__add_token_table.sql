CREATE TABLE IF NOT EXISTS token (
                                     token_id SERIAL PRIMARY KEY,
                                     token VARCHAR(255) UNIQUE NOT NULL,
                                     token_type VARCHAR(50) NOT NULL DEFAULT 'BEARER',
                                     revoked BOOLEAN NOT NULL DEFAULT FALSE,
                                     expired BOOLEAN NOT NULL DEFAULT FALSE,
                                     user_id INTEGER,
                                     CONSTRAINT fk_token_user FOREIGN KEY (user_id)
                                         REFERENCES "users"(user_id)
                                         ON DELETE CASCADE
);