CREATE TABLE if not exists public.t_log (
	id serial4 NOT NULL,
	chat_id int4 NULL,
	create_stamp timestamp DEFAULT CURRENT_TIMESTAMP NULL,
	message text NULL
);