CREATE TABLE statistics_counter (
    statistics_counter_id SERIAL NOT NULL,
    character_name TEXT NOT NULL,
    key TEXT NOT NULL,
    count INT DEFAULT 0 NOT NULL,
    unique (character_name, key)
);