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

MYSQL_SCRIPT
