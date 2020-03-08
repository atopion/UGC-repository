#!/bin/bash

mysql --user=root --password=1111 --host=172.31.208.1 --port=3306 --protocol=TCP <<MYSQL_SCRIPT
USE ugc_rep_1;
REPLACE INTO applications (application_name, application_token_hash)
	VALUES ("cuby", "ABCDEFGHIJKLMNOPQRSTUVWXYZ");
REPLACE INTO applications (application_name, application_token_hash)
	VALUES ("sammlungsportal", "ABCDEFGHIJKLMNOPQRSTUVWXYZ");
REPLACE INTO applications (application_name, application_token_hash)
	VALUES ("collectionswall", "ABCDEFGHIJKLMNOPQRSTUVWXYZ");

REPLACE INTO users (user_token, user_name, user_email)
	VALUES ("btrwijmoemviuewf", "Peter Franken", "p.franken@gmx.de");
REPLACE INTO users (user_token, user_name, user_email)
	VALUES ("vneoifiwqjhfowfopi", "Alexander Feuer", "feuer.alex@gmail.com");
REPLACE INTO users (user_token, user_name, user_email)
	VALUES ("wvwoijjertggiowwf", "Philip Cramer", "cramer1966@web.de");
MYSQL_SCRIPT
