CREATE TABLE echo_user (
  user_id BIGINT PRIMARY KEY,
  first_use_date TIMESTAMP,
  first_name VARCHAR(255),
  second_name VARCHAR(255),
  user_name VARCHAR(255),
  message_count INT
);