CREATE TABLE if not exists public.t_user (
	chat_id bigint,
    username varchar(500),
    authorized bool NULL);