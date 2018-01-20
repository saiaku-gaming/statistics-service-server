CREATE TABLE statistics_low_timer(
	statistics_low_timer_id SERIAL NOT NULL,
	timer DECIMAL NOT NULL,
	key TEXT NOT NULL,
	character_name TEXT NOT NULL,
	unique (character_name, key)
);

CREATE TABLE statistics_high_timer(
	statistics_high_timer_id SERIAL NOT NULL,
	timer DECIMAL NOT NULL,
	key TEXT NOT NULL,
	character_name TEXT NOT NULL,
	unique (character_name, key)
);