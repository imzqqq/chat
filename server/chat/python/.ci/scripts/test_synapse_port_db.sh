#!/usr/bin/env bash
#
# Test script for 'synapse_port_db'.
#   - sets up chat and deps
#   - runs the port script on a prepopulated test sqlite db
#   - also runs it against an new sqlite db


set -xe
cd `dirname $0`/../..

echo "--- Install dependencies"

# Install dependencies for this test.
pip install psycopg2 coverage coverage-enable-subprocess

# Install Chat itself. This won't update any libraries.
pip install -e .

echo "--- Generate the signing key"

# Generate the server's signing key.
python -m chat.app.homeserver --generate-keys -c .ci/sqlite-config.yaml

echo "--- Prepare test database"

# Make sure the SQLite3 database is using the latest schema and has no pending background update.
scripts-dev/update_database --database-config .ci/sqlite-config.yaml

# Create the PostgreSQL database.
.ci/scripts/postgres_exec.py "CREATE DATABASE chat"

echo "+++ Run synapse_port_db against test database"
coverage run scripts/synapse_port_db --sqlite-database .ci/test_db.db --postgres-config .ci/postgres-config.yaml

# We should be able to run twice against the same database.
echo "+++ Run synapse_port_db a second time"
coverage run scripts/synapse_port_db --sqlite-database .ci/test_db.db --postgres-config .ci/postgres-config.yaml

#####

# Now do the same again, on an empty database.

echo "--- Prepare empty SQLite database"

# we do this by deleting the sqlite db, and then doing the same again.
rm .ci/test_db.db

scripts-dev/update_database --database-config .ci/sqlite-config.yaml

# re-create the PostgreSQL database.
.ci/scripts/postgres_exec.py \
  "DROP DATABASE chat" \
  "CREATE DATABASE chat"

echo "+++ Run synapse_port_db against empty database"
coverage run scripts/synapse_port_db --sqlite-database .ci/test_db.db --postgres-config .ci/postgres-config.yaml
