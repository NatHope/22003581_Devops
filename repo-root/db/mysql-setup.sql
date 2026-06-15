CREATE DATABASE IF NOT EXISTS aircraft_db;
CREATE DATABASE IF NOT EXISTS hangar_db;

-- Aircraft service user: full access to aircraft_db only
CREATE USER IF NOT EXISTS 'aircraft_user'@'%' IDENTIFIED BY 'change_this_password';
GRANT ALL PRIVILEGES ON aircraft_db.* TO 'aircraft_user'@'%';

-- Hangar service user: full access to hangar_db only
CREATE USER IF NOT EXISTS 'hangar_user'@'%' IDENTIFIED BY 'change_this_password';
GRANT ALL PRIVILEGES ON hangar_db.* TO 'hangar_user'@'%';

FLUSH PRIVILEGES;
