#!/usr/bin/env python

import sys
import psycopg2

# a very simple replacment for `psql`, to make up for the lack of the postgres client
# libraries in the chat docker image.

# We use "postgres" as a database because it's bound to exist and the "chat" one
# doesn't exist yet.
db_conn = psycopg2.connect(
    user="postgres", host="postgres", password="postgres", dbname="postgres"
)
db_conn.autocommit = True
cur = db_conn.cursor()
for c in sys.argv[1:]:
    cur.execute(c)
