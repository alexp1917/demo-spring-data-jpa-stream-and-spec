-- https://stackoverflow.com/questions/24841142/how-can-i-generate-big-data-sample-for-postgresql-using-generate-series-and-rand
INSERT INTO customer(name)
    SELECT md5(i::text) FROM generate_series(1, 1000000) s(i)
--    SELECT md5(x) FROM system_range(1, 1000);
ON CONFLICT DO NOTHING;