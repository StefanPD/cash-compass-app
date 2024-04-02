UPDATE users
SET password_hash = (
    SELECT password_hash
    FROM users
    WHERE username = 'test123'
    LIMIT 1
)
WHERE username != 'test123';