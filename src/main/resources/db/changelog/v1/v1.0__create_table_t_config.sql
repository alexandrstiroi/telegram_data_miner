CREATE TABLE if not exists public.t_config (
	id serial,
    parament varchar(500),
    value varchar(500),
    modify_stamp timestamp default current_timestamp);