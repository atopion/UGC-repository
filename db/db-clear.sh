#!/bin/bash

mysql --user=root --password=1111 --host=localhost --port=3306 --protocol=TCP <<MYSQL_SCRIPT
USE ugc_rep_1;
DROP TABLE content_lists;
DROP TABLE content_lists_records;
DROP TABLE content_annotations;
DROP TABLE content_liked_fields;
DROP TABLE content_likes;
DROP TABLE applications;
DROP TABLE users;
DROP TABLE records;
MYSQL_SCRIPT
