#!/bin/bash

mysql --user=root --password=1111 --host=localhost --port=3306 --protocol=TCP <<MYSQL_SCRIPT
CREATE DATABASE IF NOT EXISTS ugc_rep_1;
USE ugc_rep_1;
CREATE TABLE IF NOT EXISTS applications (
	application_id int NOT NULL AUTO_INCREMENT,
	application_name varchar(100) NOT NULL,
	PRIMARY KEY(application_id),
	CONSTRAINT applications_unique UNIQUE (application_name)
);
CREATE TABLE IF NOT EXISTS users (
	user_id int NOT NULL AUTO_INCREMENT,
	user_token varchar(150) NOT NULL,
	user_name varchar(150),
	user_email varchar(150),
	PRIMARY KEY(user_id),
	CONSTRAINT users_unique UNIQUE (user_token, user_name, user_email)
);
CREATE TABLE IF NOT EXISTS records (
  record_id varchar(50) NOT NULL,
  PRIMARY KEY(record_id)
);
CREATE TABLE IF NOT EXISTS content_comments (
	comment_id int NOT NULL AUTO_INCREMENT,
	comment_text varchar(3000) NOT NULL,
	comment_created datetime NOT NULL,
	application_id int NOT NULL,
	record_id varchar(50) NOT NULL,
	user_token varchar(150),
	PRIMARY KEY(comment_id),
	FOREIGN KEY(application_id) REFERENCES applications(application_id),
	FOREIGN KEY(record_id) REFERENCES records(record_id),
	FOREIGN KEY(user_token) REFERENCES users(user_token)
);
CREATE TABLE IF NOT EXISTS content_liked_fields (
	field_id int NOT NULL AUTO_INCREMENT,
	field_name varchar(200) NOT NULL,
	field_like_count int NOT NULL,
	record_id varchar(50) NOT NULL,
	PRIMARY KEY(field_id),
	FOREIGN KEY(record_id) REFERENCES records(record_id)
);
CREATE TABLE IF NOT EXISTS content_likes (
	record_id varchar(50) NOT NULL,
	cuby_like_level_1 int NOT NULL,
	cuby_like_level_2 int NOT NULL,
	cuby_like_level_3 int NOT NULL,
	PRIMARY KEY(record_id),
	FOREIGN KEY(record_id) REFERENCES records(record_id)
);
CREATE TABLE IF NOT EXISTS content_lists (
	list_id int NOT NULL AUTO_INCREMENT,
	list_title varchar(200) NOT NULL,
	list_description varchar(3000) NOT NULL,
	list_created datetime NOT NULL,
	application_id int NOT NULL,
	user_token varchar(150),
	PRIMARY KEY(list_id),
	FOREIGN KEY(application_id) REFERENCES applications(application_id),
	FOREIGN KEY(user_token) REFERENCES users(user_token)
);
CREATE TABLE IF NOT EXISTS content_lists_records (
	list_content_id int NOT NULL AUTO_INCREMENT,
	entry_created datetime NOT NULL,
	list_id int NOT NULL,
	record_id varchar(50) NOT NULL,
	PRIMARY KEY(list_content_id),
	FOREIGN KEY(list_id) REFERENCES content_lists(list_id),
	FOREIGN KEY(record_id) REFERENCES records(record_id)
);
CREATE TABLE IF NOT EXISTS content_annotations (
	annotation_id int NOT NULL AUTO_INCREMENT,
	annotation_url varchar(500) NOT NULL,
	annotation_content varchar(5000) NOT NULL,
	annotation_canvas int,
	annotation_created datetime NOT NULL,
	application_id int NOT NULL,
	record_id varchar(50) NOT NULL,
	user_token varchar(150) NOT NULL,
	PRIMARY KEY(annotation_id),
	FOREIGN KEY(application_id) REFERENCES applications(application_id),
	FOREIGN KEY(record_id) REFERENCES records(record_id),
	FOREIGN KEY(user_token) REFERENCES users(user_token)
);


CREATE DATABASE IF NOT EXISTS users;
USE users;

CREATE TABLE IF NOT EXISTS users (
  user_id      int NOT NULL AUTO_INCREMENT,
  user_keyhash varchar(128) NOT NULL,
  user_secret  varchar(128) NOT NULL,

  PRIMARY KEY(user_id),
  CONSTRAINT users_unique UNIQUE (user_keyhash, user_secret)
);

CREATE USER 'spring'@'%' identified by '2222';
CREATE USER 'rest'@'%' identified by '3333';
CREATE USER 'apps'@'%' identified by '4444';

GRANT SELECT, UPDATE, INSERT, DELETE ON ugc_rep_1.* TO 'rest';
GRANT SELECT ON ugc_rep_1.* TO 'spring';
GRANT SELECT ON users.users TO 'apps';

MYSQL_SCRIPT

