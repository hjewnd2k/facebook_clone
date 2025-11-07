#!/bin/bash
set -e

# Tạo CSDL mới có tên là 'keycloak_db'
# và cấp quyền cho user 'exam' (đã được tạo bởi biến môi trường)
psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
    CREATE DATABASE keycloak_db;
    GRANT ALL PRIVILEGES ON DATABASE keycloak_db TO $POSTGRES_USER;
EOSQL