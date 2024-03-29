ALTER TABLE users ADD COLUMN role_temp varchar DEFAULT 'user';
UPDATE users SET role_temp = role::text;
ALTER TABLE users DROP COLUMN role;
ALTER TABLE users RENAME COLUMN role_temp TO role;
ALTER TABLE users ALTER COLUMN role SET DEFAULT 'user';
DROP TYPE user_role;