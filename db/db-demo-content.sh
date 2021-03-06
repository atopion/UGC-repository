#!/bin/bash

# shellcheck disable=SC2154
mysql --user=root --password=$dbRootPW --host=localhost --port=3306 --protocol=TCP <<MYSQL_SCRIPT
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

REPLACE INTO records (record_identifier) VALUES ("record_DE-MUS-069123_238");
REPLACE INTO records (record_identifier) VALUES ("record_naniweb_365725");
REPLACE INTO records (record_identifier) VALUES ("record_kuniweb_594086");
REPLACE INTO records (record_identifier) VALUES ("record_kuniweb_594074");

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

REPLACE INTO web_annotation(web_annotation_id, id, created, modified, generated, bodyValue, canonical)
  VALUES (1, "https://data.forum-wissen.de/rest/annotations/1", "2020-04-24 12:00:00", "2020-04-24 12:01:00", "2020-04-24 12:00:10", NULL, "https://hdl.handle.net/21.11107/record_DE-MUS-069123_69");

REPLACE INTO web_annotation_body(body_id, id, processing_language, text_direction, value, modified, created, canonical, source, annotation_id, choice_id)
  VALUES (1, "https://data.forum-wissen.de/annotations/body/1", "de", "ltr", "Dort ist etwas abgesplittert.", "2020-04-24 12:01:00", "2020-04-24 12:00:00", "https://hdl.handle.net/21.11107/record_DE-MUS-069123_69", NULL, 1, NULL);

REPLACE INTO web_annotation_target(target_id, id, value, processing_language, text_direction, canonical, source, annotation_id)
  VALUES (1, "https://data.forum-wissen.de/annotations/target/1", NULL, "de", "ltr", "https://hdl.handle.net/21.11107/record_DE-MUS-069123_69", "https://sammlungen.uni-goettingen.de/rest/image/record_DE-MUS-069123_69/i69_0.jpg", 1);

REPLACE INTO web_annotation_agent(agent_id, id, nickname, annotation_id, body_id)
  VALUES (1, "https://sammlungsportal.uni-goettingen.de/user/kheck", "kheck", 1, NULL);

REPLACE INTO web_annotation_state(state_id, id, type, sourceDateStart, sourceDateEnd, value, body_id, target_id, refiningState, refiningSelector)
  VALUES (1, NULL, "TimeState", NULL, NULL, NULL, NULL, 1, NULL, NULL);

REPLACE INTO web_annotation_accessibility(accessibility_id, accessibility, target_id, body_id)
  VALUES (1, "resizeText/CSSEnabled", 1, NULL);

REPLACE INTO web_annotation_agent_email(agent_email_id, email, agent_id)
  VALUES (1, "heck@kustodie.uni-goettingen.de", 1);

REPLACE INTO web_annotation_agent_homepage(agent_homepage_id, homepage, agent_id)
  VALUES (1, "https://www.uni-goettingen.de/de/521338.html", 1);

REPLACE INTO web_annotation_agent_name(agent_name_id, name, agent_id)
  VALUES (1, "Karsten Heck", 1);

REPLACE INTO web_annotation_audience(audience_id, id, annotation_id)
  VALUES (1, "https://container.uni-goettingen.de/roles/Kustodie", 1);

REPLACE INTO web_annotation_cached(cached_id, cached, state_id)
  VALUES (1, "https://sammlungen.uni-goettingen.de/objekt/record_DE-MUS-069123_69/1/-/", 1);

REPLACE INTO web_annotation_context(context_id, context, annotation_id)
  VALUES (1, "http://www.w3.org/ns/anno.jsonld", 1);

REPLACE INTO web_annotation_email_sha1(agent_email_sha1_id, email_sha1, agent_id)
  VALUES (1, "171bc3343d089b46d57f111b2939ad8a6b565a0f", 1);

REPLACE INTO web_annotation_format(format_id, format, body_id, target_id)
  VALUES (1, "text/html", 1, NULL);

REPLACE INTO web_annotation_format(format_id, format, body_id, target_id)
  VALUES (2, "text/html", NULL, 1);

REPLACE INTO web_annotation_language(language_id, language, body_id, target_id)
  VALUES (1, "de", 1, NULL);

REPLACE INTO web_annotation_language(language_id, language, body_id, target_id)
  VALUES (1, "de", NULL, 1);

REPLACE INTO web_annotation_motivation(motivation_id, motivation, annotation_id)
  VALUES (1, "commenting", 1);

REPLACE INTO web_annotation_purpose(purpose_id, purpose, body_id, target_id)
  VALUES (1, "commenting", 1, NULL);

REPLACE INTO web_annotation_rendered_via(rendered_via_id, id, type, body_id, target_id)
  VALUES (1, "https://www.google.com/intl/de_de/chrome/", "Software", NULL, 1);

REPLACE INTO web_annotation_rights(rights_id, rights, annotation_id, body_id, target_id)
  VALUES (1, "http://creativecommons.org/licenses/by-nc/4.0/", 1, NULL, NULL);

REPLACE INTO web_annotation_rights(rights_id, rights, annotation_id, body_id, target_id)
  VALUES (1, "http://creativecommons.org/licenses/by-nc/4.0/", NULL, 1, NULL);

REPLACE INTO web_annotation_rights(rights_id, rights, annotation_id, body_id, target_id)
  VALUES (1, "https://www.deutsche-digitale-bibliothek.de/content/lizenzen/rv-fz", NULL, NULL, 1);

REPLACE INTO web_annotation_scope(scope_id, id, body_id, target_id)
  VALUES (1, "https://sammlungen.uni-goettingen.de/mirador/", 1, NULL);

REPLACE INTO web_annotation_selector(selector_id, value, conformsTo, exact, prefix, suffix, startPos, endPos, startSelector, endSelector, refiningSelector, body_id, target_id)
  VALUES (1, "/770,500,150,150/!1000,1000/0/default.jpg", "https://iiif.io/api/image/2.1/#region", NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);

REPLACE INTO web_annotation_source_date(source_date_id, source_date, state_id)
  VALUES (1, "2020-04-24 12:01:00", 1);

REPLACE INTO web_annotation_style_class(style_class_id, style_class, body_id, target_id)
  VALUES (1, "red", 1, NULL);

REPLACE INTO web_annotation_stylesheet(stylesheet_id, value, annotation_id)
  VALUES (1, ".red { color: red }", 1);

REPLACE INTO web_annotation_type(type_id, type, annotation_id, body_id, target_id, agent_id, audience_id, stylesheet_id, selector_id)
  VALUES (1, "Annotation", 1, NULL, NULL, NULL, NULL, NULL, NULL);
REPLACE INTO web_annotation_type(type_id, type, annotation_id, body_id, target_id, agent_id, audience_id, stylesheet_id, selector_id)
  VALUES (2, "TextualBody", NULL, 1, NULL, NULL, NULL, NULL, NULL);
REPLACE INTO web_annotation_type(type_id, type, annotation_id, body_id, target_id, agent_id, audience_id, stylesheet_id, selector_id)
  VALUES (3, "SpecificResource", NULL, NULL, 1, NULL, NULL, NULL, NULL);
REPLACE INTO web_annotation_type(type_id, type, annotation_id, body_id, target_id, agent_id, audience_id, stylesheet_id, selector_id)
  VALUES (4, "Person", NULL, NULL, NULL, 1, NULL, NULL, NULL);
REPLACE INTO web_annotation_type(type_id, type, annotation_id, body_id, target_id, agent_id, audience_id, stylesheet_id, selector_id)
  VALUES (5, "KustodieMitarbeiter", NULL, NULL, NULL, NULL, 1, NULL, NULL);
REPLACE INTO web_annotation_type(type_id, type, annotation_id, body_id, target_id, agent_id, audience_id, stylesheet_id, selector_id)
  VALUES (6, "CssStylesheet", NULL, NULL, NULL, NULL, NULL, 1, NULL);
REPLACE INTO web_annotation_type(type_id, type, annotation_id, body_id, target_id, agent_id, audience_id, stylesheet_id, selector_id)
  VALUES (7, "FragmentSelector", NULL, NULL, NULL, NULL, NULL, NULL, 1);

REPLACE INTO web_annotation_via(via_id, via, annotation_id, target_id, body_id)
  VALUES (1, "https://sammlungen.uni-goettingen.de/objekt/record_DE-MUS-069123_69/1/", 1, NULL, NULL);




MYSQL_SCRIPT
