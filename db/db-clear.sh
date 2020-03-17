#!/bin/bash

mysql --user=root --password=1111 --host=172.31.208.1 --port=3306 --protocol=TCP <<MYSQL_SCRIPT
USE ugc_rep_1;
DELETE FROM applications;
DELETE FROM users;
DELETE FROM records;
DELETE FROM content_lists;
DELETE FROM content_lists_records;
DELETE FROM content_annotations;
DELETE FROM content_liked_fields;
DELETE FROM content_likes;
MYSQL_SCRIPT
