#!/bin/bash
set -x
docker exec mysql1 bash -c "mysql mysql < /import/create_tpch_sf1.sql -u mysqldb -p123456"
docker exec mysql1 bash -c "mysql mysql < /import/import_tpch_sf1.sql -u mysqldb -p123456"
docker exec mysql1 bash -c "mysql mysql < /import/indexes_tpch_sf1.sql -u mysqldb -p123456"