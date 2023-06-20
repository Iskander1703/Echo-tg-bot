CREATE TABLE echo_user_message (
  message_id SERIAL PRIMARY KEY,
  text VARCHAR(255),
  receive_from_user TIMESTAMP,
  send_to_user TIMESTAMP,
  message_count INT,
  status VARCHAR(255),
  echo_user_id BIGINT,
  FOREIGN KEY (echo_user_id) REFERENCES echo_user (user_id)
);