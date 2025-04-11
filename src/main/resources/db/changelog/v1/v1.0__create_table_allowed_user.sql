CREATE TABLE if not exists public.allowed_user (
	id serial4 NOT NULL,
	chat_id int4 NULL,
	username varchar(100) NULL,
	firstname varchar(100) NULL,
	is_active bool NULL);