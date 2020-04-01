CREATE TABLE IF NOT EXISTS users (
	user_token varchar(150) NOT NULL,
	user_name varchar(150),
	user_email varchar(150),
	PRIMARY KEY(user_token),
	CONSTRAINT users_unique UNIQUE (user_name, user_email)
);
