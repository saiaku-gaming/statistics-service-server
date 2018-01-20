ALTER TABLE statistics_counter RENAME TO statistics_int_counter;

ALTER TABLE statistics_int_counter RENAME COLUMN statistics_counter_id TO statistics_int_counter_id;