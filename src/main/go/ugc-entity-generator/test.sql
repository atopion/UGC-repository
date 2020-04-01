CREATE TABLE IF NOT EXISTS users (
	user_id int NOT NULL,
	user_token varchar(150) NOT NULL,
	user_name varchar(150),
	user_email varchar(150),
	PRIMARY KEY(user_id),
	CONSTRAINT users_unique UNIQUE (user_token, user_name, user_email)
);
