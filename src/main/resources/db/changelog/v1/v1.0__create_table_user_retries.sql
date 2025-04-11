CREATE TABLE if not exists public.user_retries (
	id serial4 NOT NULL,
	chat_id int4 NULL,
	tries int4 DEFAULT 1 NULL,
	create_stamp timestamp DEFAULT CURRENT_TIMESTAMP NULL,
	modify_stamp timestamp DEFAULT CURRENT_TIMESTAMP NULL
);