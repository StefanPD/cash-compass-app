CREATE TYPE user_role AS ENUM ('admin', 'user');
ALTER TABLE users ADD COLUMN role user_role not null default 'user';