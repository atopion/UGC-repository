#!/bin/bash

mysql --user=root --password=1111 --host=localhost --port=3306 --protocol=TCP <<MYSQL_SCRIPT
USE ugc_rep_1;
REPLACE INTO applications (application_name) VALUES ("cuby");
REPLACE INTO applications (application_name) VALUES ("sammlungsportal");
REPLACE INTO applications (application_name) VALUES ("collectionswall");

REPLACE INTO users (user_token, user_name, user_email)
	VALUES ("81F5592AA4409AED", "Peter Franken", "p.franken@gmx.de");
REPLACE INTO users (user_token, user_name, user_email)
	VALUES ("9417DC8622240496", "Alexander Feuer", "feuer.alex@gmail.com");
REPLACE INTO users (user_token, user_name, user_email)
	VALUES ("8156CB8C424774E6", "Philip Cramer", "cramer1966@web.de");

REPLACE INTO records (record_id) VALUES ("record_DE-MUS-069123_238");
REPLACE INTO records (record_id) VALUES ("record_naniweb_365725");
REPLACE INTO records (record_id) VALUES ("record_kuniweb_594086");
REPLACE INTO records (record_id) VALUES ("record_kuniweb_594074");

REPLACE INTO content_lists (list_title, list_description, list_created, application_id, user_token)
  VALUES ("My first list", "This is a list", "2015-04-03 15:33:21", 2, "81F5592AA4409AED");
REPLACE INTO content_lists (list_title, list_description, list_created, application_id)
  VALUES ("My list", "", "2017-09-29 08:12:19", 2);

REPLACE INTO content_lists_records (entry_created, list_id, record_id)
  VALUES ("2015-04-03 15:40:03", 1, "record_DE-MUS-069123_238");
REPLACE INTO content_lists_records (entry_created, list_id, record_id)
  VALUES ("2015-04-03 15:42:10", 1, "record_naniweb_365725");
REPLACE INTO content_lists_records (entry_created, list_id, record_id)
  VALUES ("2015-04-03 15:43:29", 1, "record_kuniweb_594086");
REPLACE INTO content_lists_records (entry_created, list_id, record_id)
  VALUES ("2015-04-03 15:43:59", 1, "record_kuniweb_594074");
REPLACE INTO content_lists_records (entry_created, list_id, record_id)
  VALUES ("2017-09-29 08:13:45", 2, "record_kuniweb_594074");
REPLACE INTO content_lists_records (entry_created, list_id, record_id)
  VALUES ("2018-01-30 19:58:02", 2, "record_naniweb_365725");

REPLACE INTO content_annotations (annotation_url, annotation_content, annotation_canvas, annotation_created, application_id, record_id, user_token)
  VALUES ("https://www.example.com/rest/api/1/2/3", "Content", 3, "2019-03-03 20:01:01", 2, "record_kuniweb_594086", "8156CB8C424774E6");

REPLACE INTO content_comments (comment_text, comment_created, application_id, record_id, user_token)
  VALUES ("This is a comment", "2018-12-21 12:13:59", 3, "record_naniweb_365725", "9417DC8622240496");
REPLACE INTO content_comments (comment_text, comment_created, application_id, record_id, user_token)
  VALUES ("This is another comment", "2018-04-01 17:19:11", 3, "record_kuniweb_594086", "9417DC8622240496");
REPLACE INTO content_comments (comment_text, comment_created, application_id, record_id, user_token)
  VALUES ("This is a comment", "2018-06-06 18:36:36", 3, "record_DE-MUS-069123_238", "9417DC8622240496");

REPLACE INTO content_liked_fields (field_name, field_like_count, record_id)
  VALUES ("Breite", 19, "record_naniweb_365725");
REPLACE INTO content_liked_fields (field_name, field_like_count, record_id)
  VALUES ("Epoche", 51, "record_naniweb_365725");

REPLACE INTO content_likes (record_id, cuby_like_level_1, cuby_like_level_2, cuby_like_level_3)
  VALUES ("record_kuniweb_594086", 33, 29, 25);
REPLACE INTO content_likes (record_id, cuby_like_level_1, cuby_like_level_2, cuby_like_level_3)
  VALUES ("record_naniweb_365725", 65, 59, 58);
REPLACE INTO content_likes (record_id, cuby_like_level_1, cuby_like_level_2, cuby_like_level_3)
  VALUES ("record_kuniweb_594074", 49, 45, 45);
REPLACE INTO content_likes (record_id, cuby_like_level_1, cuby_like_level_2, cuby_like_level_3)
  VALUES ("record_DE-MUS-069123_238", 120, 115, 110);

MYSQL_SCRIPT
