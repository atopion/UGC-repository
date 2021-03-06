#!/bin/bash

# shellcheck disable=SC2154
mysql --user=root --password=$dbRootPW --host=localhost --port=3306 --protocol=TCP <<MYSQL_SCRIPT
USE ugc_rep_1;

ALTER TABLE content_comments DROP FOREIGN KEY fk_comment_application;
ALTER TABLE content_comments DROP FOREIGN KEY fk_comment_record;
ALTER TABLE content_comments DROP FOREIGN KEY fk_comment_user;
ALTER TABLE content_liked_fields DROP FOREIGN KEY fk_liked_fields_record;
ALTER TABLE content_likes DROP FOREIGN KEY fk_likes_record;
ALTER TABLE content_lists DROP FOREIGN KEY fk_lists_application;
ALTER TABLE content_lists DROP FOREIGN KEY fk_lists_user;
ALTER TABLE content_lists_records DROP FOREIGN KEY fk_lists_records_list;
ALTER TABLE content_lists_records DROP FOREIGN KEY fk_lists_records_record;
ALTER TABLE content_annotations DROP FOREIGN KEY fk_annotations_application;
ALTER TABLE content_annotations DROP FOREIGN KEY fk_annotations_record;
ALTER TABLE content_annotations DROP FOREIGN KEY fk_annotations_user;

DROP TABLE content_lists;
DROP TABLE content_lists_records;
DROP TABLE content_annotations;
DROP TABLE content_liked_fields;
DROP TABLE content_likes;
DROP TABLE applications;
DROP TABLE users;
DROP TABLE records;

USE webannotations;
ALTER TABLE web_annotation_agent_email DROP FOREIGN KEY fk_agent_email;
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
ALTER TABLE web_annotation_via DROP FOREIGN KEY fk_target_via;
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

DROP USER 'spring'@'%';
DROP USER 'rest'@'%';
DROP USER 'apps'@'%';
MYSQL_SCRIPT
