CREATE SCHEMA IF NOT EXISTS csv_reader;

DROP TABLE IF EXISTS user_visit CASCADE;
CREATE TABLE user_visit
(
    email VARCHAR NOT NULL,
    phone VARCHAR NOT NULL,
    source VARCHAR NOT NULL,
    PRIMARY KEY(email, phone, source)
);
