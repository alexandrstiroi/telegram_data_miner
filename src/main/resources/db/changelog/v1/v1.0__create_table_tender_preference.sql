CREATE TABLE if not exists public.tender_preference (
    id          SERIAL,
    keyword     varchar(500),
    category_id varchar(500),
    chat_id     bigint);