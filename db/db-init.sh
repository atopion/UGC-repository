#!/bin/bash

# shellcheck disable=SC2154
mysql --user=root --password=$dbRootPW --host=localhost --port=3306 --protocol=TCP <<MYSQL_SCRIPT
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
  record_id int NOT NULL AUTO_INCREMENT,
  record_identifier varchar(50) NOT NULL,
  PRIMARY KEY(record_id)
);
CREATE TABLE IF NOT EXISTS content_comments (
	comment_id int NOT NULL AUTO_INCREMENT,
	comment_text varchar(3000) NOT NULL,
	comment_created datetime NOT NULL,
	application_id int NOT NULL,
	record_id int NOT NULL,
	user_token varchar(150),
	PRIMARY KEY(comment_id),
	CONSTRAINT fk_comment_application FOREIGN KEY(application_id) REFERENCES applications(application_id),
	CONSTRAINT fk_comment_record FOREIGN KEY(record_id) REFERENCES records(record_id),
	CONSTRAINT fk_comment_user FOREIGN KEY(user_token) REFERENCES users(user_token)
);
CREATE TABLE IF NOT EXISTS content_liked_fields (
	field_id int NOT NULL AUTO_INCREMENT,
	field_name varchar(200) NOT NULL,
	field_like_count int NOT NULL,
	record_id int NOT NULL,
	PRIMARY KEY(field_id),
	CONSTRAINT fk_liked_fields_record FOREIGN KEY(record_id) REFERENCES records(record_id)
);
CREATE TABLE IF NOT EXISTS content_likes (
	record_id int NOT NULL,
	cuby_like_level_1 int NOT NULL,
	cuby_like_level_2 int NOT NULL,
	cuby_like_level_3 int NOT NULL,
	PRIMARY KEY(record_id),
	CONSTRAINT fk_likes_record FOREIGN KEY(record_id) REFERENCES records(record_id)
);
CREATE TABLE IF NOT EXISTS content_lists (
	list_id int NOT NULL AUTO_INCREMENT,
	list_title varchar(200) NOT NULL,
	list_description varchar(3000) NOT NULL,
	list_created datetime NOT NULL,
	application_id int NOT NULL,
	user_token varchar(150),
	PRIMARY KEY(list_id),
	CONSTRAINT fk_lists_application FOREIGN KEY(application_id) REFERENCES applications(application_id),
	CONSTRAINT fk_lists_user FOREIGN KEY(user_token) REFERENCES users(user_token)
);
CREATE TABLE IF NOT EXISTS content_lists_records (
	list_content_id int NOT NULL AUTO_INCREMENT,
	entry_created datetime NOT NULL,
	list_id int NOT NULL,
	record_id int NOT NULL,
	PRIMARY KEY(list_content_id),
	CONSTRAINT fk_lists_records_list FOREIGN KEY(list_id) REFERENCES content_lists(list_id),
	CONSTRAINT fk_lists_records_record FOREIGN KEY(record_id) REFERENCES records(record_id)
);
CREATE TABLE IF NOT EXISTS content_annotations (
	annotation_id int NOT NULL AUTO_INCREMENT,
	annotation_url varchar(500) NOT NULL,
	annotation_content varchar(5000) NOT NULL,
	annotation_canvas int,
	annotation_created datetime NOT NULL,
	application_id int NOT NULL,
	record_id int NOT NULL,
	user_token varchar(150) NOT NULL,
	PRIMARY KEY(annotation_id),
	CONSTRAINT fk_annotations_application FOREIGN KEY(application_id) REFERENCES applications(application_id),
	CONSTRAINT fk_annotations_record FOREIGN KEY(record_id) REFERENCES records(record_id),
	CONSTRAINT fk_annotations_user FOREIGN KEY(user_token) REFERENCES users(user_token)
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


CREATE DATABASE IF NOT EXISTS webannotations;
USE webannotations;

CREATE TABLE web_annotation (
  web_annotation_id int(10) NOT NULL AUTO_INCREMENT,
  id                varchar(255) NOT NULL UNIQUE,
  created           date,
  modified          date,
  generated         date,
  bodyValue         varchar(5000),
  canonical         varchar(500),
  PRIMARY KEY (web_annotation_id));
CREATE TABLE web_annotation_accessibility (
  accessibility_id int(10) NOT NULL AUTO_INCREMENT,
  accessibility    varchar(255) NOT NULL,
  target_id        int(10),
  body_id          int(10),
  PRIMARY KEY (accessibility_id));
CREATE TABLE web_annotation_agent (
  agent_id      int(10) NOT NULL AUTO_INCREMENT,
  id            varchar(255) UNIQUE,
  nickname      varchar(255),
  annotation_id int(10),
  body_id       int(10),
  PRIMARY KEY (agent_id));
CREATE TABLE web_annotation_agent_email (
  agent_email_id int(10) NOT NULL AUTO_INCREMENT,
  email          varchar(255) NOT NULL,
  agent_id       int(10) NOT NULL,
  PRIMARY KEY (agent_email_id));
CREATE TABLE web_annotation_agent_homepage (
  agent_homepage_id int(10) NOT NULL AUTO_INCREMENT,
  homepage          varchar(255) NOT NULL,
  agent_id          int(10) NOT NULL,
  PRIMARY KEY (agent_homepage_id));
CREATE TABLE web_annotation_agent_name (
  agent_name_id int(10) NOT NULL AUTO_INCREMENT,
  name          varchar(255) NOT NULL,
  agent_id      int(10) NOT NULL,
  PRIMARY KEY (agent_name_id));
CREATE TABLE web_annotation_audience (
  audience_id   int(10) NOT NULL AUTO_INCREMENT,
  id            varchar(255),
  annotation_id int(10) NOT NULL,
  PRIMARY KEY (audience_id));
CREATE TABLE web_annotation_body (
  body_id             int(10) NOT NULL AUTO_INCREMENT,
  id                  varchar(255) UNIQUE,
  processing_language varchar(5),
  text_direction      varchar(5),
  value               varchar(5000),
  modified            date,
  created             date,
  canonical           varchar(255),
  source              varchar(255),
  annotation_id       int(10) NOT NULL,
  choice_id           int(10),
  PRIMARY KEY (body_id));
CREATE TABLE web_annotation_cached (
  cached_id int(10) NOT NULL AUTO_INCREMENT,
  cached    varchar(255),
  state_id  int(10) NOT NULL,
  PRIMARY KEY (cached_id));
CREATE TABLE web_annotation_context (
  context_id    int(11) NOT NULL AUTO_INCREMENT,
  context       varchar(255) NOT NULL,
  annotation_id int(10) NOT NULL,
  PRIMARY KEY (context_id));
CREATE TABLE web_annotation_email_sha1 (
  agent_email_sha1_id int(10) NOT NULL AUTO_INCREMENT,
  email_sha1          varchar(255) NOT NULL,
  agent_id            int(10) NOT NULL,
  PRIMARY KEY (agent_email_sha1_id));
CREATE TABLE web_annotation_format (
  format_id int(11) NOT NULL AUTO_INCREMENT,
  format    varchar(255) NOT NULL,
  body_id   int(10),
  target_id int(10),
  PRIMARY KEY (format_id));
CREATE TABLE web_annotation_language (
  language_id int(11) NOT NULL AUTO_INCREMENT,
  language    varchar(5) NOT NULL,
  body_id     int(10),
  target_id   int(10),
  PRIMARY KEY (language_id));
CREATE TABLE web_annotation_motivation (
  motivation_id int(10) NOT NULL AUTO_INCREMENT,
  motivation    varchar(255) NOT NULL,
  annotation_id int(10) NOT NULL,
  PRIMARY KEY (motivation_id));
CREATE TABLE web_annotation_purpose (
  purpose_id int(10) NOT NULL AUTO_INCREMENT,
  purpose    varchar(255) NOT NULL,
  body_id    int(10),
  target_id  int(10),
  PRIMARY KEY (purpose_id));
CREATE TABLE web_annotation_rendered_via (
  rendered_via_id int(10) NOT NULL AUTO_INCREMENT,
  id              varchar(255) NOT NULL,
  type            varchar(255),
  body_id         int(10),
  target_id       int(10),
  PRIMARY KEY (rendered_via_id));
CREATE TABLE web_annotation_rights (
  rights_id     int(10) NOT NULL AUTO_INCREMENT,
  rights        varchar(255) NOT NULL,
  annotation_id int(10),
  body_id       int(10),
  target_id     int(10),
  PRIMARY KEY (rights_id));
CREATE TABLE web_annotation_scope (
  scope_id  int(10) NOT NULL AUTO_INCREMENT,
  id        varchar(255) NOT NULL,
  body_id   int(10),
  target_id int(10),
  PRIMARY KEY (scope_id));
CREATE TABLE web_annotation_selector (
  selector_id      int(10) NOT NULL AUTO_INCREMENT,
  value            varchar(5000) NOT NULL,
  conformsTo       varchar(255),
  exact            varchar(5000),
  prefix           varchar(5000),
  suffix           varchar(5000),
  startPos         int(10),
  endPos           int(10),
  startSelector    int(10),
  endSelector      int(10),
  refiningSelector int(10),
  body_id          int(10),
  target_id        int(10),
  PRIMARY KEY (selector_id));
CREATE TABLE web_annotation_source_date (
  source_date_id int(10) NOT NULL AUTO_INCREMENT,
  source_date    date NOT NULL,
  state_id       int(10) NOT NULL,
  PRIMARY KEY (source_date_id));
CREATE TABLE web_annotation_state (
  state_id         int(10) NOT NULL AUTO_INCREMENT,
  id               varchar(255),
  type             varchar(255) NOT NULL,
  sourceDateStart  date,
  sourceDateEnd    date,
  value            varchar(5000),
  body_id          int(10),
  target_id        int(10),
  refiningState    int(10),
  refiningSelector int(10),
  PRIMARY KEY (state_id));
CREATE TABLE web_annotation_style_class (
  style_class_id int(10) NOT NULL AUTO_INCREMENT,
  style_class    varchar(255) NOT NULL,
  body_id        int(10),
  target_id      int(10),
  PRIMARY KEY (style_class_id));
CREATE TABLE web_annotation_stylesheet (
  stylesheet_id int(10) NOT NULL AUTO_INCREMENT,
  value         varchar(5000),
  annotation_id int(10) NOT NULL,
  PRIMARY KEY (stylesheet_id));
CREATE TABLE web_annotation_target (
  target_id           int(10) NOT NULL AUTO_INCREMENT,
  id                  varchar(255) NOT NULL UNIQUE,
  value               varchar(5000),
  processing_language varchar(5),
  text_direction      varchar(5),
  canonical           varchar(255),
  source              varchar(255),
  annotation_id       int(10) NOT NULL,
  PRIMARY KEY (target_id));
CREATE TABLE web_annotation_type (
  type_id       int(11) NOT NULL AUTO_INCREMENT,
  type          varchar(255) NOT NULL,
  annotation_id int(10),
  body_id       int(10),
  target_id     int(10),
  agent_id      int(10),
  audience_id   int(10),
  stylesheet_id int(10),
  selector_id   int(10),
  PRIMARY KEY (type_id));
CREATE TABLE web_annotation_via (
  via_id        int(10) NOT NULL AUTO_INCREMENT,
  via           varchar(255) NOT NULL,
  annotation_id int(10),
  target_id     int(10),
  body_id       int(10),
  PRIMARY KEY (via_id));

ALTER TABLE web_annotation_agent_email ADD INDEX fk_agent_email (agent_id), ADD CONSTRAINT fk_agent_email FOREIGN KEY (agent_id) REFERENCES web_annotation_agent (agent_id);
ALTER TABLE web_annotation_email_sha1 ADD INDEX fk_agent_email_sha1 (agent_id), ADD CONSTRAINT fk_agent_email_sha1 FOREIGN KEY (agent_id) REFERENCES web_annotation_agent (agent_id);
ALTER TABLE web_annotation_agent_homepage ADD INDEX fk_agent_homepage (agent_id), ADD CONSTRAINT fk_agent_homepage FOREIGN KEY (agent_id) REFERENCES web_annotation_agent (agent_id);
ALTER TABLE web_annotation_agent_name ADD INDEX fk_agent_name (agent_id), ADD CONSTRAINT fk_agent_name FOREIGN KEY (agent_id) REFERENCES web_annotation_agent (agent_id);
ALTER TABLE web_annotation_type ADD INDEX fk_agent_type (agent_id), ADD CONSTRAINT fk_agent_type FOREIGN KEY (agent_id) REFERENCES web_annotation_agent (agent_id);
ALTER TABLE web_annotation_audience ADD INDEX fk_annotation_audience (annotation_id), ADD CONSTRAINT fk_annotation_audience FOREIGN KEY (annotation_id) REFERENCES web_annotation (web_annotation_id);
ALTER TABLE web_annotation_body ADD INDEX fk_annotation_body (annotation_id), ADD CONSTRAINT fk_annotation_body FOREIGN KEY (annotation_id) REFERENCES web_annotation (web_annotation_id);
ALTER TABLE web_annotation_context ADD INDEX fk_annotation_context (annotation_id), ADD CONSTRAINT fk_annotation_context FOREIGN KEY (annotation_id) REFERENCES web_annotation (web_annotation_id);
ALTER TABLE web_annotation_agent ADD INDEX fk_annotation_creator (annotation_id), ADD CONSTRAINT fk_annotation_creator FOREIGN KEY (annotation_id) REFERENCES web_annotation (web_annotation_id);
ALTER TABLE web_annotation_agent ADD INDEX fk_annotation_generator (annotation_id), ADD CONSTRAINT fk_annotation_generator FOREIGN KEY (annotation_id) REFERENCES web_annotation (web_annotation_id);
ALTER TABLE web_annotation_motivation ADD INDEX fk_annotation_motivation (annotation_id), ADD CONSTRAINT fk_annotation_motivation FOREIGN KEY (annotation_id) REFERENCES web_annotation (web_annotation_id);
ALTER TABLE web_annotation_rights ADD INDEX fk_annotation_rights (annotation_id), ADD CONSTRAINT fk_annotation_rights FOREIGN KEY (annotation_id) REFERENCES web_annotation (web_annotation_id);
ALTER TABLE web_annotation_stylesheet ADD INDEX fk_annotation_stylesheet (annotation_id), ADD CONSTRAINT fk_annotation_stylesheet FOREIGN KEY (annotation_id) REFERENCES web_annotation (web_annotation_id);
ALTER TABLE web_annotation_target ADD INDEX fk_annotation_target (annotation_id), ADD CONSTRAINT fk_annotation_target FOREIGN KEY (annotation_id) REFERENCES web_annotation (web_annotation_id);
ALTER TABLE web_annotation_type ADD INDEX fk_annotation_type (annotation_id), ADD CONSTRAINT fk_annotation_type FOREIGN KEY (annotation_id) REFERENCES web_annotation (web_annotation_id);
ALTER TABLE web_annotation_via ADD INDEX fk_annotation_via (annotation_id), ADD CONSTRAINT fk_annotation_via FOREIGN KEY (annotation_id) REFERENCES web_annotation (web_annotation_id);
ALTER TABLE web_annotation_type ADD INDEX fk_audience_type (audience_id), ADD CONSTRAINT fk_audience_type FOREIGN KEY (audience_id) REFERENCES web_annotation_audience (audience_id);
ALTER TABLE web_annotation_accessibility ADD INDEX fk_body_accessibility (body_id), ADD CONSTRAINT fk_body_accessibility FOREIGN KEY (body_id) REFERENCES web_annotation_body (body_id);
ALTER TABLE web_annotation_agent ADD INDEX fk_body_creator (body_id), ADD CONSTRAINT fk_body_creator FOREIGN KEY (body_id) REFERENCES web_annotation_body (body_id);
ALTER TABLE web_annotation_format ADD INDEX fk_body_format (body_id), ADD CONSTRAINT fk_body_format FOREIGN KEY (body_id) REFERENCES web_annotation_body (body_id);
ALTER TABLE web_annotation_body ADD INDEX fk_body_items (choice_id), ADD CONSTRAINT fk_body_items FOREIGN KEY (choice_id) REFERENCES web_annotation_body (body_id);
ALTER TABLE web_annotation_language ADD INDEX fk_body_language (body_id), ADD CONSTRAINT fk_body_language FOREIGN KEY (body_id) REFERENCES web_annotation_body (body_id);
ALTER TABLE web_annotation_purpose ADD INDEX fk_body_purpose (body_id), ADD CONSTRAINT fk_body_purpose FOREIGN KEY (body_id) REFERENCES web_annotation_body (body_id);
ALTER TABLE web_annotation_rendered_via ADD INDEX fk_body_renderedVia (body_id), ADD CONSTRAINT fk_body_renderedVia FOREIGN KEY (body_id) REFERENCES web_annotation_body (body_id);
ALTER TABLE web_annotation_rights ADD INDEX fk_body_rights (body_id), ADD CONSTRAINT fk_body_rights FOREIGN KEY (body_id) REFERENCES web_annotation_body (body_id);
ALTER TABLE web_annotation_scope ADD INDEX fk_body_scope (body_id), ADD CONSTRAINT fk_body_scope FOREIGN KEY (body_id) REFERENCES web_annotation_body (body_id);
ALTER TABLE web_annotation_selector ADD INDEX fk_body_selector (body_id), ADD CONSTRAINT fk_body_selector FOREIGN KEY (body_id) REFERENCES web_annotation_body (body_id);
ALTER TABLE web_annotation_state ADD INDEX fk_body_state (body_id), ADD CONSTRAINT fk_body_state FOREIGN KEY (body_id) REFERENCES web_annotation_body (body_id);
ALTER TABLE web_annotation_style_class ADD INDEX fk_body_styleClass (body_id), ADD CONSTRAINT fk_body_styleClass FOREIGN KEY (body_id) REFERENCES web_annotation_body (body_id);
ALTER TABLE web_annotation_type ADD INDEX fk_body_type (body_id), ADD CONSTRAINT fk_body_type FOREIGN KEY (body_id) REFERENCES web_annotation_body (body_id);
ALTER TABLE web_annotation_via ADD INDEX fk_body_via (body_id), ADD CONSTRAINT fk_body_via FOREIGN KEY (body_id) REFERENCES web_annotation_body (body_id);
ALTER TABLE web_annotation_selector ADD INDEX fk_selector_endSelector (endSelector), ADD CONSTRAINT fk_selector_endSelector FOREIGN KEY (endSelector) REFERENCES web_annotation_selector (selector_id);
ALTER TABLE web_annotation_selector ADD INDEX fk_selector_refinedBy (refiningSelector), ADD CONSTRAINT fk_selector_refinedBy FOREIGN KEY (refiningSelector) REFERENCES web_annotation_selector (selector_id);
ALTER TABLE web_annotation_state ADD INDEX fk_selector_refiningSelector (refiningSelector), ADD CONSTRAINT fk_selector_refiningSelector FOREIGN KEY (refiningSelector) REFERENCES web_annotation_selector (selector_id);
ALTER TABLE web_annotation_selector ADD INDEX fk_selector_startSelector (startSelector), ADD CONSTRAINT fk_selector_startSelector FOREIGN KEY (startSelector) REFERENCES web_annotation_selector (selector_id);
ALTER TABLE web_annotation_type ADD INDEX fk_selector_type (selector_id), ADD CONSTRAINT fk_selector_type FOREIGN KEY (selector_id) REFERENCES web_annotation_selector (selector_id);
ALTER TABLE web_annotation_cached ADD INDEX fk_state_cached (state_id), ADD CONSTRAINT fk_state_cached FOREIGN KEY (state_id) REFERENCES web_annotation_state (state_id);
ALTER TABLE web_annotation_state ADD INDEX fk_state_refiningState (refiningState), ADD CONSTRAINT fk_state_refiningState FOREIGN KEY (refiningState) REFERENCES web_annotation_state (state_id);
ALTER TABLE web_annotation_source_date ADD INDEX fk_state_sourceDate (state_id), ADD CONSTRAINT fk_state_sourceDate FOREIGN KEY (state_id) REFERENCES web_annotation_state (state_id);
ALTER TABLE web_annotation_type ADD INDEX fk_stylesheet_type (stylesheet_id), ADD CONSTRAINT fk_stylesheet_type FOREIGN KEY (stylesheet_id) REFERENCES web_annotation_stylesheet (stylesheet_id);
ALTER TABLE web_annotation_accessibility ADD INDEX fk_target_accessibility (target_id), ADD CONSTRAINT fk_target_accessibility FOREIGN KEY (target_id) REFERENCES web_annotation_target (target_id);
ALTER TABLE web_annotation_format ADD INDEX fk_target_format (target_id), ADD CONSTRAINT fk_target_format FOREIGN KEY (target_id) REFERENCES web_annotation_target (target_id);
ALTER TABLE web_annotation_language ADD INDEX fk_target_language (target_id), ADD CONSTRAINT fk_target_language FOREIGN KEY (target_id) REFERENCES web_annotation_target (target_id);
ALTER TABLE web_annotation_purpose ADD INDEX fk_target_purpose (target_id), ADD CONSTRAINT fk_target_purpose FOREIGN KEY (target_id) REFERENCES web_annotation_target (target_id);
ALTER TABLE web_annotation_rendered_via ADD INDEX fk_target_renderedVia (target_id), ADD CONSTRAINT fk_target_renderedVia FOREIGN KEY (target_id) REFERENCES web_annotation_target (target_id);
ALTER TABLE web_annotation_rights ADD INDEX fk_target_rights (target_id), ADD CONSTRAINT fk_target_rights FOREIGN KEY (target_id) REFERENCES web_annotation_target (target_id);
ALTER TABLE web_annotation_scope ADD INDEX fk_target_scope (target_id), ADD CONSTRAINT fk_target_scope FOREIGN KEY (target_id) REFERENCES web_annotation_target (target_id);
ALTER TABLE web_annotation_selector ADD INDEX fk_target_selector (target_id), ADD CONSTRAINT fk_target_selector FOREIGN KEY (target_id) REFERENCES web_annotation_target (target_id);
ALTER TABLE web_annotation_state ADD INDEX fk_target_state (target_id), ADD CONSTRAINT fk_target_state FOREIGN KEY (target_id) REFERENCES web_annotation_target (target_id);
ALTER TABLE web_annotation_style_class ADD INDEX fk_target_styleClass (target_id), ADD CONSTRAINT fk_target_styleClass FOREIGN KEY (target_id) REFERENCES web_annotation_target (target_id);
ALTER TABLE web_annotation_type ADD INDEX fk_target_type (target_id), ADD CONSTRAINT fk_target_type FOREIGN KEY (target_id) REFERENCES web_annotation_target (target_id);
ALTER TABLE web_annotation_via ADD INDEX fk_target_via (target_id), ADD CONSTRAINT fk_target_via FOREIGN KEY (target_id) REFERENCES web_annotation_target (target_id);


CREATE USER 'spring'@'%' identified by '$SQLPassword';
CREATE USER 'rest'@'%' identified by '$RESTPassword';
CREATE USER 'apps'@'%' identified by '$USERPassword';

GRANT SELECT, UPDATE, INSERT, DELETE ON ugc_rep_1.* TO 'rest';
GRANT SELECT, UPDATE, INSERT, DELETE ON webannotations.* TO 'rest';
GRANT SELECT ON ugc_rep_1.* TO 'spring';
GRANT SELECT ON users.users TO 'apps';

MYSQL_SCRIPT

