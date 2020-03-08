#!/bin/bash

mysql --user=root --password=1111 --host=172.31.208.1 --port=3306 --protocol=TCP <<MYSQL_SCRIPT
USE ugc_rep_1;
DELETE FROM applications;
DELETE FROM users;
MYSQL_SCRIPT
