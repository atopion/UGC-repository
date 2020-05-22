/*CREATE DATABASE IF NOT EXISTS ugc_rep_1;
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

*/
/* CREATE DATABASE webannotations;*/
/* USE webannotations; */

/*ALTER TABLE web_annotation_agent_email DROP FOREIGN KEY fk_agent_email;
ALTER TABLE web_annotation_email_sha1 DROP FOREIGN KEY fk_agent_email_sha1;
ALTER TABLE web_annotation_agent_homepage DROP FOREIGN KEY fk_agent_homepage;
ALTER TABLE web_annotation_agent_name DROP FOREIGN KEY fk_agent_name;
ALTER TABLE web_annotation_type DROP FOREIGN KEY fk_agent_type;
ALTER TABLE web_annotation_audience DROP FOREIGN KEY fk_annotation_audience;
ALTER TABLE web_annotation_body DROP FOREIGN KEY fk_annotation_body;
ALTER TABLE web_annotation_context DROP FOREIGN KEY fk_annotation_context;
ALTER TABLE web_annotation_agent DROP FOREIGN KEY fk_annotation_creator;
ALTER TABLE web_annotation_agent DROP FOREIGN KEY fk_annotation_generator;
ALTER TABLE web_annotation_motivation DROP FOREIGN KEY fk_annotation_motivation;
ALTER TABLE web_annotation_rights DROP FOREIGN KEY fk_annotation_rights;
ALTER TABLE web_annotation_stylesheet DROP FOREIGN KEY fk_annotation_stylesheet;
ALTER TABLE web_annotation_target DROP FOREIGN KEY fk_annotation_target;
ALTER TABLE web_annotation_type DROP FOREIGN KEY fk_annotation_type;
ALTER TABLE web_annotation_via DROP FOREIGN KEY fk_annotation_via;
ALTER TABLE web_annotation_type DROP FOREIGN KEY fk_audience_type;
ALTER TABLE web_annotation_accessibility DROP FOREIGN KEY fk_body_accessibility;
ALTER TABLE web_annotation_agent DROP FOREIGN KEY fk_body_creator;
ALTER TABLE web_annotation_format DROP FOREIGN KEY fk_body_format;
ALTER TABLE web_annotation_body DROP FOREIGN KEY fk_body_items;
ALTER TABLE web_annotation_language DROP FOREIGN KEY fk_body_language;
ALTER TABLE web_annotation_purpose DROP FOREIGN KEY fk_body_purpose;
ALTER TABLE web_annotation_rendered_via DROP FOREIGN KEY fk_body_renderedVia;
ALTER TABLE web_annotation_rights DROP FOREIGN KEY fk_body_rights;
ALTER TABLE web_annotation_scope DROP FOREIGN KEY fk_body_scope;
ALTER TABLE web_annotation_selector DROP FOREIGN KEY fk_body_selector;
ALTER TABLE web_annotation_state DROP FOREIGN KEY fk_body_state;
ALTER TABLE web_annotation_style_class DROP FOREIGN KEY fk_body_styleClass;
ALTER TABLE web_annotation_type DROP FOREIGN KEY fk_body_type;
ALTER TABLE web_annotation_via DROP FOREIGN KEY fk_body_via;
ALTER TABLE web_annotation_selector DROP FOREIGN KEY fk_selector_endSelector;
ALTER TABLE web_annotation_selector DROP FOREIGN KEY fk_selector_refinedBy;
ALTER TABLE web_annotation_state DROP FOREIGN KEY fk_selector_refiningSelector;
ALTER TABLE web_annotation_selector DROP FOREIGN KEY fk_selector_startSelector;
ALTER TABLE web_annotation_type DROP FOREIGN KEY fk_selector_type;
ALTER TABLE web_annotation_cached DROP FOREIGN KEY fk_state_cached;
ALTER TABLE web_annotation_state DROP FOREIGN KEY fk_state_refiningState;
ALTER TABLE web_annotation_source_date DROP FOREIGN KEY fk_state_sourceDate;
ALTER TABLE web_annotation_type DROP FOREIGN KEY fk_stylesheet_type;
ALTER TABLE web_annotation_accessibility DROP FOREIGN KEY fk_target_accessibility;
ALTER TABLE web_annotation_format DROP FOREIGN KEY fk_target_format;
ALTER TABLE web_annotation_language DROP FOREIGN KEY fk_target_language;
ALTER TABLE web_annotation_purpose DROP FOREIGN KEY fk_target_purpose;
ALTER TABLE web_annotation_rendered_via DROP FOREIGN KEY fk_target_renderedVia;
ALTER TABLE web_annotation_rights DROP FOREIGN KEY fk_target_rights;
ALTER TABLE web_annotation_scope DROP FOREIGN KEY fk_target_scope;
ALTER TABLE web_annotation_selector DROP FOREIGN KEY fk_target_selector;
ALTER TABLE web_annotation_state DROP FOREIGN KEY fk_target_state;
ALTER TABLE web_annotation_style_class DROP FOREIGN KEY fk_target_styleClass;
ALTER TABLE web_annotation_type DROP FOREIGN KEY fk_target_type;
ALTER TABLE web_annotation_via DROP FOREIGN KEY fk_target_via;*/
DROP TABLE IF EXISTS web_annotation;
DROP TABLE IF EXISTS web_annotation_accessibility;
DROP TABLE IF EXISTS web_annotation_agent;
DROP TABLE IF EXISTS web_annotation_agent_email;
DROP TABLE IF EXISTS web_annotation_agent_homepage;
DROP TABLE IF EXISTS web_annotation_agent_name;
DROP TABLE IF EXISTS web_annotation_audience;
DROP TABLE IF EXISTS web_annotation_body;
DROP TABLE IF EXISTS web_annotation_cached;
DROP TABLE IF EXISTS web_annotation_context;
DROP TABLE IF EXISTS web_annotation_email_sha1;
DROP TABLE IF EXISTS web_annotation_format;
DROP TABLE IF EXISTS web_annotation_language;
DROP TABLE IF EXISTS web_annotation_motivation;
DROP TABLE IF EXISTS web_annotation_purpose;
DROP TABLE IF EXISTS web_annotation_rendered_via;
DROP TABLE IF EXISTS web_annotation_rights;
DROP TABLE IF EXISTS web_annotation_scope;
DROP TABLE IF EXISTS web_annotation_selector;
DROP TABLE IF EXISTS web_annotation_source_date;
DROP TABLE IF EXISTS web_annotation_state;
DROP TABLE IF EXISTS web_annotation_style_class;
DROP TABLE IF EXISTS web_annotation_stylesheet;
DROP TABLE IF EXISTS web_annotation_target;
DROP TABLE IF EXISTS web_annotation_type;
DROP TABLE IF EXISTS web_annotation_via;

CREATE TABLE web_annotation (
  web_annotation_id int(10) NOT NULL AUTO_INCREMENT,
  id                varchar(255) NOT NULL UNIQUE,
  created           datetime,
  modified          datetime,
  generated         datetime,
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
  id            varchar(255),
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
  modified            datetime,
  created             datetime,
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
  source_date    datetime NOT NULL,
  state_id       int(10) NOT NULL,
  PRIMARY KEY (source_date_id));
CREATE TABLE web_annotation_state (
  state_id         int(10) NOT NULL AUTO_INCREMENT,
  id               varchar(255),
  type             varchar(255) NOT NULL,
  sourceDateStart  datetime,
  sourceDateEnd    datetime,
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

/*ALTER TABLE web_annotation_agent_email AUTO_INCREMENT = 1;
ALTER TABLE web_annotation_email_sha1 AUTO_INCREMENT = 1;
ALTER TABLE web_annotation_agent_homepage AUTO_INCREMENT = 1;
ALTER TABLE web_annotation_agent_name AUTO_INCREMENT = 1;
ALTER TABLE web_annotation_type AUTO_INCREMENT = 1;
ALTER TABLE web_annotation_audience AUTO_INCREMENT = 1;
ALTER TABLE web_annotation_body AUTO_INCREMENT = 1;
ALTER TABLE web_annotation_context AUTO_INCREMENT = 1;
ALTER TABLE web_annotation_agent AUTO_INCREMENT = 1;
ALTER TABLE web_annotation_agent AUTO_INCREMENT = 1;
ALTER TABLE web_annotation_motivation AUTO_INCREMENT = 1;
ALTER TABLE web_annotation_rights AUTO_INCREMENT = 1;
ALTER TABLE web_annotation_stylesheet AUTO_INCREMENT = 1;
ALTER TABLE web_annotation_target AUTO_INCREMENT = 1;
ALTER TABLE web_annotation_type AUTO_INCREMENT = 1;
ALTER TABLE web_annotation_via AUTO_INCREMENT = 1;
ALTER TABLE web_annotation_type AUTO_INCREMENT = 1;
ALTER TABLE web_annotation_accessibility AUTO_INCREMENT = 1;
ALTER TABLE web_annotation_agent AUTO_INCREMENT = 1;
ALTER TABLE web_annotation_format AUTO_INCREMENT = 1;
ALTER TABLE web_annotation_body AUTO_INCREMENT = 1;
ALTER TABLE web_annotation_language AUTO_INCREMENT = 1;
ALTER TABLE web_annotation_purpose AUTO_INCREMENT = 1;
ALTER TABLE web_annotation_rendered_via AUTO_INCREMENT = 1;
ALTER TABLE web_annotation_rights AUTO_INCREMENT = 1;
ALTER TABLE web_annotation_scope AUTO_INCREMENT = 1;
ALTER TABLE web_annotation_selector AUTO_INCREMENT = 1;
ALTER TABLE web_annotation_state AUTO_INCREMENT = 1;
ALTER TABLE web_annotation_style_class AUTO_INCREMENT = 1;
ALTER TABLE web_annotation_type AUTO_INCREMENT = 1;
ALTER TABLE web_annotation_via AUTO_INCREMENT = 1;
ALTER TABLE web_annotation_selector AUTO_INCREMENT = 1;
ALTER TABLE web_annotation_selector AUTO_INCREMENT = 1;
ALTER TABLE web_annotation_state AUTO_INCREMENT = 1;
ALTER TABLE web_annotation_selector AUTO_INCREMENT = 1;
ALTER TABLE web_annotation_type AUTO_INCREMENT = 1;
ALTER TABLE web_annotation_cached AUTO_INCREMENT = 1;
ALTER TABLE web_annotation_state AUTO_INCREMENT = 1;
ALTER TABLE web_annotation_source_date AUTO_INCREMENT = 1;
ALTER TABLE web_annotation_type AUTO_INCREMENT = 1;
ALTER TABLE web_annotation_accessibility AUTO_INCREMENT = 1;
ALTER TABLE web_annotation_format AUTO_INCREMENT = 1;
ALTER TABLE web_annotation_language AUTO_INCREMENT = 1;
ALTER TABLE web_annotation_purpose AUTO_INCREMENT = 1;
ALTER TABLE web_annotation_rendered_via AUTO_INCREMENT = 1;
ALTER TABLE web_annotation_rights AUTO_INCREMENT = 1;
ALTER TABLE web_annotation_scope AUTO_INCREMENT = 1;
ALTER TABLE web_annotation_selector AUTO_INCREMENT = 1;
ALTER TABLE web_annotation_state AUTO_INCREMENT = 1;
ALTER TABLE web_annotation_style_class AUTO_INCREMENT = 1;
ALTER TABLE web_annotation_type AUTO_INCREMENT = 1;
ALTER TABLE web_annotation_via AUTO_INCREMENT = 1;*/

ALTER TABLE web_annotation_agent_email ADD CONSTRAINT fk_agent_email FOREIGN KEY (agent_id) REFERENCES web_annotation_agent (agent_id) ON DELETE CASCADE;
ALTER TABLE web_annotation_email_sha1 ADD CONSTRAINT fk_agent_email_sha1 FOREIGN KEY (agent_id) REFERENCES web_annotation_agent (agent_id) ON DELETE CASCADE;
ALTER TABLE web_annotation_agent_homepage ADD CONSTRAINT fk_agent_homepage FOREIGN KEY (agent_id) REFERENCES web_annotation_agent (agent_id) ON DELETE CASCADE;
ALTER TABLE web_annotation_agent_name ADD CONSTRAINT fk_agent_name FOREIGN KEY (agent_id) REFERENCES web_annotation_agent (agent_id) ON DELETE CASCADE;
ALTER TABLE web_annotation_type ADD CONSTRAINT fk_agent_type FOREIGN KEY (agent_id) REFERENCES web_annotation_agent (agent_id) ON DELETE CASCADE;
ALTER TABLE web_annotation_audience ADD CONSTRAINT fk_annotation_audience FOREIGN KEY (annotation_id) REFERENCES web_annotation (web_annotation_id) ON DELETE CASCADE;
ALTER TABLE web_annotation_body ADD CONSTRAINT fk_annotation_body FOREIGN KEY (annotation_id) REFERENCES web_annotation (web_annotation_id) ON DELETE CASCADE;
ALTER TABLE web_annotation_context ADD CONSTRAINT fk_annotation_context FOREIGN KEY (annotation_id) REFERENCES web_annotation (web_annotation_id) ON DELETE CASCADE;
ALTER TABLE web_annotation_agent ADD CONSTRAINT fk_annotation_creator FOREIGN KEY (annotation_id) REFERENCES web_annotation (web_annotation_id) ON DELETE CASCADE;
ALTER TABLE web_annotation_agent ADD CONSTRAINT fk_annotation_generator FOREIGN KEY (annotation_id) REFERENCES web_annotation (web_annotation_id) ON DELETE CASCADE;
ALTER TABLE web_annotation_motivation ADD CONSTRAINT fk_annotation_motivation FOREIGN KEY (annotation_id) REFERENCES web_annotation (web_annotation_id) ON DELETE CASCADE;
ALTER TABLE web_annotation_rights ADD CONSTRAINT fk_annotation_rights FOREIGN KEY (annotation_id) REFERENCES web_annotation (web_annotation_id) ON DELETE CASCADE;
ALTER TABLE web_annotation_stylesheet ADD CONSTRAINT fk_annotation_stylesheet FOREIGN KEY (annotation_id) REFERENCES web_annotation (web_annotation_id) ON DELETE CASCADE;
ALTER TABLE web_annotation_target ADD CONSTRAINT fk_annotation_target FOREIGN KEY (annotation_id) REFERENCES web_annotation (web_annotation_id) ON DELETE CASCADE;
ALTER TABLE web_annotation_type ADD CONSTRAINT fk_annotation_type FOREIGN KEY (annotation_id) REFERENCES web_annotation (web_annotation_id) ON DELETE CASCADE;
ALTER TABLE web_annotation_via ADD CONSTRAINT fk_annotation_via FOREIGN KEY (annotation_id) REFERENCES web_annotation (web_annotation_id) ON DELETE CASCADE;
ALTER TABLE web_annotation_type ADD CONSTRAINT fk_audience_type FOREIGN KEY (audience_id) REFERENCES web_annotation_audience (audience_id) ON DELETE CASCADE;
ALTER TABLE web_annotation_accessibility ADD CONSTRAINT fk_body_accessibility FOREIGN KEY (body_id) REFERENCES web_annotation_body (body_id) ON DELETE CASCADE;
ALTER TABLE web_annotation_agent ADD CONSTRAINT fk_body_creator FOREIGN KEY (body_id) REFERENCES web_annotation_body (body_id) ON DELETE CASCADE;
ALTER TABLE web_annotation_format ADD CONSTRAINT fk_body_format FOREIGN KEY (body_id) REFERENCES web_annotation_body (body_id) ON DELETE CASCADE;
ALTER TABLE web_annotation_body ADD CONSTRAINT fk_body_items FOREIGN KEY (choice_id) REFERENCES web_annotation_body (body_id) ON DELETE CASCADE;
ALTER TABLE web_annotation_language ADD CONSTRAINT fk_body_language FOREIGN KEY (body_id) REFERENCES web_annotation_body (body_id) ON DELETE CASCADE;
ALTER TABLE web_annotation_purpose ADD CONSTRAINT fk_body_purpose FOREIGN KEY (body_id) REFERENCES web_annotation_body (body_id) ON DELETE CASCADE;
ALTER TABLE web_annotation_rendered_via ADD CONSTRAINT fk_body_renderedVia FOREIGN KEY (body_id) REFERENCES web_annotation_body (body_id) ON DELETE CASCADE;
ALTER TABLE web_annotation_rights ADD CONSTRAINT fk_body_rights FOREIGN KEY (body_id) REFERENCES web_annotation_body (body_id) ON DELETE CASCADE;
ALTER TABLE web_annotation_scope ADD CONSTRAINT fk_body_scope FOREIGN KEY (body_id) REFERENCES web_annotation_body (body_id) ON DELETE CASCADE;
ALTER TABLE web_annotation_selector ADD CONSTRAINT fk_body_selector FOREIGN KEY (body_id) REFERENCES web_annotation_body (body_id) ON DELETE CASCADE;
ALTER TABLE web_annotation_state ADD CONSTRAINT fk_body_state FOREIGN KEY (body_id) REFERENCES web_annotation_body (body_id) ON DELETE CASCADE;
ALTER TABLE web_annotation_style_class ADD CONSTRAINT fk_body_styleClass FOREIGN KEY (body_id) REFERENCES web_annotation_body (body_id) ON DELETE CASCADE;
ALTER TABLE web_annotation_type ADD CONSTRAINT fk_body_type FOREIGN KEY (body_id) REFERENCES web_annotation_body (body_id) ON DELETE CASCADE;
ALTER TABLE web_annotation_via ADD CONSTRAINT fk_body_via FOREIGN KEY (body_id) REFERENCES web_annotation_body (body_id) ON DELETE CASCADE;
ALTER TABLE web_annotation_selector ADD CONSTRAINT fk_selector_endSelector FOREIGN KEY (endSelector) REFERENCES web_annotation_selector (selector_id) ON DELETE CASCADE;
ALTER TABLE web_annotation_selector ADD CONSTRAINT fk_selector_refinedBy FOREIGN KEY (refiningSelector) REFERENCES web_annotation_selector (selector_id) ON DELETE CASCADE;
ALTER TABLE web_annotation_state ADD CONSTRAINT fk_selector_refiningSelector FOREIGN KEY (refiningSelector) REFERENCES web_annotation_selector (selector_id) ON DELETE CASCADE;
ALTER TABLE web_annotation_selector ADD CONSTRAINT fk_selector_startSelector FOREIGN KEY (startSelector) REFERENCES web_annotation_selector (selector_id) ON DELETE CASCADE;
ALTER TABLE web_annotation_type ADD CONSTRAINT fk_selector_type FOREIGN KEY (selector_id) REFERENCES web_annotation_selector (selector_id) ON DELETE CASCADE;
ALTER TABLE web_annotation_cached ADD CONSTRAINT fk_state_cached FOREIGN KEY (state_id) REFERENCES web_annotation_state (state_id) ON DELETE CASCADE;
ALTER TABLE web_annotation_state ADD CONSTRAINT fk_state_refiningState FOREIGN KEY (refiningState) REFERENCES web_annotation_state (state_id) ON DELETE CASCADE;
ALTER TABLE web_annotation_source_date ADD CONSTRAINT fk_state_sourceDate FOREIGN KEY (state_id) REFERENCES web_annotation_state (state_id) ON DELETE CASCADE;
ALTER TABLE web_annotation_type ADD CONSTRAINT fk_stylesheet_type FOREIGN KEY (stylesheet_id) REFERENCES web_annotation_stylesheet (stylesheet_id) ON DELETE CASCADE;
ALTER TABLE web_annotation_accessibility ADD CONSTRAINT fk_target_accessibility FOREIGN KEY (target_id) REFERENCES web_annotation_target (target_id) ON DELETE CASCADE;
ALTER TABLE web_annotation_format ADD CONSTRAINT fk_target_format FOREIGN KEY (target_id) REFERENCES web_annotation_target (target_id) ON DELETE CASCADE;
ALTER TABLE web_annotation_language ADD CONSTRAINT fk_target_language FOREIGN KEY (target_id) REFERENCES web_annotation_target (target_id) ON DELETE CASCADE;
ALTER TABLE web_annotation_purpose ADD CONSTRAINT fk_target_purpose FOREIGN KEY (target_id) REFERENCES web_annotation_target (target_id) ON DELETE CASCADE;
ALTER TABLE web_annotation_rendered_via ADD CONSTRAINT fk_target_renderedVia FOREIGN KEY (target_id) REFERENCES web_annotation_target (target_id) ON DELETE CASCADE;
ALTER TABLE web_annotation_rights ADD CONSTRAINT fk_target_rights FOREIGN KEY (target_id) REFERENCES web_annotation_target (target_id) ON DELETE CASCADE;
ALTER TABLE web_annotation_scope ADD CONSTRAINT fk_target_scope FOREIGN KEY (target_id) REFERENCES web_annotation_target (target_id) ON DELETE CASCADE;
ALTER TABLE web_annotation_selector ADD CONSTRAINT fk_target_selector FOREIGN KEY (target_id) REFERENCES web_annotation_target (target_id) ON DELETE CASCADE;
ALTER TABLE web_annotation_state ADD CONSTRAINT fk_target_state FOREIGN KEY (target_id) REFERENCES web_annotation_target (target_id) ON DELETE CASCADE;
ALTER TABLE web_annotation_style_class ADD CONSTRAINT fk_target_styleClass FOREIGN KEY (target_id) REFERENCES web_annotation_target (target_id) ON DELETE CASCADE;
ALTER TABLE web_annotation_type ADD CONSTRAINT fk_target_type FOREIGN KEY (target_id) REFERENCES web_annotation_target (target_id) ON DELETE CASCADE;
ALTER TABLE web_annotation_via ADD CONSTRAINT fk_target_via FOREIGN KEY (target_id) REFERENCES web_annotation_target (target_id) ON DELETE CASCADE;


/*CREATE USER 'spring'@'%' identified by '$SQLPassword';
CREATE USER 'rest'@'%' identified by '$RESTPassword';
CREATE USER 'apps'@'%' identified by '$USERPassword';

GRANT SELECT, UPDATE, INSERT, DELETE ON ugc_rep_1.* TO 'rest';
GRANT SELECT, UPDATE, INSERT, DELETE ON webannotations.* TO 'rest';
GRANT SELECT ON ugc_rep_1.* TO 'spring';
GRANT SELECT ON users.users TO 'apps';*/

/*USE ugc_rep_1;
REPLACE INTO applications (application_name) VALUES ("cuby");
REPLACE INTO applications (application_name) VALUES ("sammlungsportal");
REPLACE INTO applications (application_name) VALUES ("collectionswall");

REPLACE INTO users (user_token, user_name, user_email)
  VALUES ("81F5592AA4409AED", "Peter Franken", "p.franken@gmx.de");
REPLACE INTO users (user_token, user_name, user_email)
  VALUES ("9417DC8622240496", "Alexander Feuer", "feuer.alex@gmail.com");
REPLACE INTO users (user_token, user_name, user_email)
  VALUES ("8156CB8C424774E6", "Philip Cramer", "cramer1966@web.de");

REPLACE INTO records (record_identifier)   VALUES ("record_DE-MUS-069123_238");
REPLACE INTO records (record_identifier)   VALUES ("record_naniweb_365725");
REPLACE INTO records (record_identifier)   VALUES ("record_kuniweb_594086");
REPLACE INTO records (record_identifier)   VALUES ("record_kuniweb_594074");

REPLACE INTO content_lists (list_title, list_description, list_created, application_id, user_token)
  VALUES ("My first list", "This is a list", "2015-04-03 15:33:21", 2, "81F5592AA4409AED");
REPLACE INTO content_lists (list_title, list_description, list_created, application_id)
  VALUES ("My list", "", "2017-09-29 08:12:19", 2);

REPLACE INTO content_lists_records (entry_created, list_id, record_id)
  VALUES ("2015-04-03 15:40:03", 1, 1);
REPLACE INTO content_lists_records (entry_created, list_id, record_id)
  VALUES ("2015-04-03 15:42:10", 1, 2);
REPLACE INTO content_lists_records (entry_created, list_id, record_id)
  VALUES ("2015-04-03 15:43:29", 1, 3);
REPLACE INTO content_lists_records (entry_created, list_id, record_id)
  VALUES ("2015-04-03 15:43:59", 1, 4);
REPLACE INTO content_lists_records (entry_created, list_id, record_id)
  VALUES ("2017-09-29 08:13:45", 2, 4);
REPLACE INTO content_lists_records (entry_created, list_id, record_id)
  VALUES ("2018-01-30 19:58:02", 2, 2);

REPLACE INTO content_annotations (annotation_url, annotation_content, annotation_canvas, annotation_created, application_id, record_id, user_token)
  VALUES ("https://www.example.com/rest/api/1/2/3", "Content", 3, "2019-03-03 20:01:01", 2, 3, "8156CB8C424774E6");

REPLACE INTO content_comments (comment_text, comment_created, application_id, record_id, user_token)
  VALUES ("This is a comment", "2018-12-21 12:13:59", 3, 2, "9417DC8622240496");
REPLACE INTO content_comments (comment_text, comment_created, application_id, record_id, user_token)
  VALUES ("This is another comment", "2018-04-01 17:19:11", 3, 3, "9417DC8622240496");
REPLACE INTO content_comments (comment_text, comment_created, application_id, record_id, user_token)
  VALUES ("This is a comment", "2018-06-06 18:36:36", 3, 1, "9417DC8622240496");

REPLACE INTO content_liked_fields (field_name, field_like_count, record_id)
  VALUES ("Breite", 19, 2);
REPLACE INTO content_liked_fields (field_name, field_like_count, record_id)
  VALUES ("Epoche", 51, 2);

REPLACE INTO content_likes (record_id, cuby_like_level_1, cuby_like_level_2, cuby_like_level_3)
  VALUES (3, 33, 29, 25);
REPLACE INTO content_likes (record_id, cuby_like_level_1, cuby_like_level_2, cuby_like_level_3)
  VALUES (2, 65, 59, 58);
REPLACE INTO content_likes (record_id, cuby_like_level_1, cuby_like_level_2, cuby_like_level_3)
  VALUES (4, 49, 45, 45);
REPLACE INTO content_likes (record_id, cuby_like_level_1, cuby_like_level_2, cuby_like_level_3)
  VALUES (1, 120, 115, 110);

USE users;

-- Key:    "7E0A55872199849864FBA3D029B2A4FC519818AA21DBB23D01FD57A1F917C12C2E1A0F026072C5B7BF3660A73A319F7D7CAD2725B57BFAA7A3919FD739018049"
-- Secret: "0649A13A3CBFA10E9A42F60A212962DC8F43E481B26C78F2CE6686E4059A5ED8D5D7CD3B6F40FDF917C95550FABC75CCA3F35FC24DAEDF26DB1C5C4EA3D0B784"
REPLACE INTO users (user_keyhash, user_secret)
  VALUES ("\$2a\$08\$M.f5QhQTRcfm7XPYXXbX8ufeP.xCH3GUl6fuDi1EaL7Oc2gHOR/9a", "F1C85B4DA80978942462C44B6FF4659DFC49646E797027E65B976AB8DC6D4D0AF542E7C47CA5FBD7E9A21FD5EC68C873448B8BD20609CE98A745A63F4421271A");

-- Key:    "7E9EBD55DA6AD230CD322AD65E3B0EF8CB544B926DC9944D3EF5AF6F4F494C99890FDB4D06055635A080A12502B7A53C5E014A84A1939336D4922B6378CE5AC9"
-- Secret: "528AEC8753566DC59EC72F9509C3CD0536E5B36194EEBD47B523A6DAA01C43089C75D9DAD0D8841543EC08B9AF2B9EB2EDDBEB8DD34C2B3391E85AD666BE2D28"
REPLACE INTO users (user_keyhash, user_secret)
  VALUES ("\$2a\$08\$HQbax.EhHFTD5j8s9aUobeB04sbs6gCXKvaN9KqpoATenl23jOAuC", "A59FFE223C13E2F7892E94D230976040DF2360B613E0C4231FDAB248CFB5DD6F1BF5276AA54A11B9A23183BE8179194C280F523C8C03531C9AB21413C0806736");

-- Key:    "4A3952DECE7284EEC87EF38E74B98B6ED3A34157F5A855D73360062AC01EE4B28859581F2AF738943ED47D7E51AF9E7ED326A4191977CFB128B664DF8A27FAA5"
-- Secret: "42AC8707C3BC5F140D8365AC43FE91DADBBE9597255C0F587267CFE5DD042B3F97EB1A3E2997DEBF16890F3C8E9D09BBFA2787972AA25EAB6829B0AED88F75CF"
REPLACE INTO users (user_keyhash, user_secret)
  VALUES ("\$2a\$08\$CZPeu/SwfgaekcsgeBo.rusyrJhWSlhd2peiiA3PVxuNCukg5KNp2", "811E7A29B8E186F81F2607B35028B9092A8F4C853A33B7A6D50B72323DFA1D73113D67DC70F725B269005860F3D7B507B2D4D0BBCD097A039F57B1D78C589FBD");

USE webannotations;
*/

INSERT INTO web_annotation(web_annotation_id, id, created, modified, generated, bodyValue, canonical) VALUES (1, 'https://data.forum-wissen.de/rest/annotations/1', '2020-04-24 12:00:00', '2020-04-24 12:01:00', '2020-04-24 12:00:10', NULL, 'https://hdl.handle.net/21.11107/record_DE-MUS-069123_69');

INSERT INTO web_annotation_body(body_id, id, processing_language, text_direction, value, modified, created, canonical, source, annotation_id, choice_id) VALUES (1, 'https://data.forum-wissen.de/annotations/body/1', 'de', 'ltr', 'Dort ist etwas abgesplittert.', '2020-04-24 12:01:00', '2020-04-24 12:00:00', 'https://hdl.handle.net/21.11107/record_DE-MUS-069123_69', NULL, 1, NULL);

INSERT INTO web_annotation_target(target_id, id, value, processing_language, text_direction, canonical, source, annotation_id) VALUES (1, 'https://data.forum-wissen.de/annotations/target/1', NULL, 'de', 'ltr', 'https://hdl.handle.net/21.11107/record_DE-MUS-069123_69', 'https://sammlungen.uni-goettingen.de/rest/image/record_DE-MUS-069123_69/i69_0.jpg', 1);

INSERT INTO web_annotation_agent(agent_id, id, nickname, annotation_id, body_id) VALUES (1, 'https://sammlungsportal.uni-goettingen.de/user/kheck', 'kheck', 1, NULL);

INSERT INTO web_annotation_state(state_id, id, type, sourceDateStart, sourceDateEnd, value, body_id, target_id, refiningState, refiningSelector) VALUES (1, NULL, 'TimeState', NULL, NULL, NULL, NULL, 1, NULL, NULL);

INSERT INTO web_annotation_accessibility(accessibility_id, accessibility, target_id, body_id) VALUES (1, 'resizeText/CSSEnabled', 1, NULL);

INSERT INTO web_annotation_agent_email(agent_email_id, email, agent_id) VALUES (1, 'heck@kustodie.uni-goettingen.de', 1);

INSERT INTO web_annotation_agent_homepage(agent_homepage_id, homepage, agent_id) VALUES (1, 'https://www.uni-goettingen.de/de/521338.html', 1);

INSERT INTO web_annotation_agent_name(agent_name_id, name, agent_id) VALUES (1, 'Karsten Heck', 1);

INSERT INTO web_annotation_audience(audience_id, id, annotation_id) VALUES (1, 'https://container.uni-goettingen.de/roles/Kustodie', 1);

INSERT INTO web_annotation_cached(cached_id, cached, state_id) VALUES (1, 'https://sammlungen.uni-goettingen.de/objekt/record_DE-MUS-069123_69/1/-/', 1);

INSERT INTO web_annotation_context(context_id, context, annotation_id) VALUES (1, 'http://www.w3.org/ns/anno.jsonld', 1);

INSERT INTO web_annotation_email_sha1(agent_email_sha1_id, email_sha1, agent_id) VALUES (1, '171bc3343d089b46d57f111b2939ad8a6b565a0f', 1);

INSERT INTO web_annotation_format(format_id, format, body_id, target_id) VALUES (1, 'text/html', 1, NULL);

INSERT INTO web_annotation_format(format_id, format, body_id, target_id) VALUES (2, 'text/html', NULL, 1);

INSERT INTO web_annotation_language(language_id, language, body_id, target_id) VALUES (1, 'de', 1, NULL);

INSERT INTO web_annotation_language(language_id, language, body_id, target_id) VALUES (2, 'de', NULL, 1);

INSERT INTO web_annotation_motivation(motivation_id, motivation, annotation_id) VALUES (1, 'commenting', 1);

INSERT INTO web_annotation_purpose(purpose_id, purpose, body_id, target_id) VALUES (1, 'commenting', 1, NULL);

INSERT INTO web_annotation_rendered_via(rendered_via_id, id, type, body_id, target_id) VALUES (1, 'https://www.google.com/intl/de_de/chrome/', 'Software', NULL, 1);

INSERT INTO web_annotation_rights(rights_id, rights, annotation_id, body_id, target_id) VALUES (1, 'http://creativecommons.org/licenses/by-nc/4.0/', 1, NULL, NULL);

INSERT INTO web_annotation_rights(rights_id, rights, annotation_id, body_id, target_id) VALUES (2, 'http://creativecommons.org/licenses/by-nc/4.0/', NULL, 1, NULL);

INSERT INTO web_annotation_rights(rights_id, rights, annotation_id, body_id, target_id) VALUES (3, 'https://www.deutsche-digitale-bibliothek.de/content/lizenzen/rv-fz', NULL, NULL, 1);

INSERT INTO web_annotation_scope(scope_id, id, body_id, target_id) VALUES (1, 'https://sammlungen.uni-goettingen.de/mirador/', 1, NULL);

INSERT INTO web_annotation_selector(selector_id, value, conformsTo, exact, prefix, suffix, startPos, endPos, startSelector, endSelector, refiningSelector, body_id, target_id) VALUES (1, '/770,500,150,150/!1000,1000/0/default.jpg', 'https://iiif.io/api/image/2.1/#region', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);

INSERT INTO web_annotation_source_date(source_date_id, source_date, state_id) VALUES (1, '2020-04-24 12:01:00', 1);

INSERT INTO web_annotation_style_class(style_class_id, style_class, body_id, target_id) VALUES (1, 'red', 1, NULL);

INSERT INTO web_annotation_stylesheet(stylesheet_id, value, annotation_id) VALUES (1, '.red { color: red }', 1);

INSERT INTO web_annotation_type(type_id, type, annotation_id, body_id, target_id, agent_id, audience_id, stylesheet_id, selector_id) VALUES (1, 'Annotation', 1, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO web_annotation_type(type_id, type, annotation_id, body_id, target_id, agent_id, audience_id, stylesheet_id, selector_id) VALUES (2, 'TextualBody', NULL, 1, NULL, NULL, NULL, NULL, NULL);
INSERT INTO web_annotation_type(type_id, type, annotation_id, body_id, target_id, agent_id, audience_id, stylesheet_id, selector_id) VALUES (3, 'SpecificResource', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO web_annotation_type(type_id, type, annotation_id, body_id, target_id, agent_id, audience_id, stylesheet_id, selector_id) VALUES (4, 'Person', NULL, NULL, NULL, 1, NULL, NULL, NULL);
INSERT INTO web_annotation_type(type_id, type, annotation_id, body_id, target_id, agent_id, audience_id, stylesheet_id, selector_id) VALUES (5, 'KustodieMitarbeiter', NULL, NULL, NULL, NULL, 1, NULL, NULL);
INSERT INTO web_annotation_type(type_id, type, annotation_id, body_id, target_id, agent_id, audience_id, stylesheet_id, selector_id) VALUES (6, 'CssStylesheet', NULL, NULL, NULL, NULL, NULL, 1, NULL);
INSERT INTO web_annotation_type(type_id, type, annotation_id, body_id, target_id, agent_id, audience_id, stylesheet_id, selector_id) VALUES (7, 'FragmentSelector', NULL, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO web_annotation_via(via_id, via, annotation_id, target_id, body_id) VALUES (1, 'https://sammlungen.uni-goettingen.de/objekt/record_DE-MUS-069123_69/1/', 1, NULL, NULL);
