create database peertube_dev;
create user tube password 'tube';
grant all privileges on database peertube_dev to tube;
\c peertube_dev
CREATE EXTENSION pg_trgm;
CREATE EXTENSION unaccent;
