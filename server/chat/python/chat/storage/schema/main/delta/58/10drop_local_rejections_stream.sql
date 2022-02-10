/*
The version of Chat server 1.16.0 on pypi incorrectly contained a migration which
added a table called 'local_rejections_stream'. This table is not used, and
we drop it here for anyone who was affected.
*/

DROP TABLE IF EXISTS local_rejections_stream;
