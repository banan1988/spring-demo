#!/bin/bash

PGNETWORK=demo
PGHOST=postgres
PGUSER=postgres
PGPASSWORD=postgres

docker run -it --rm --network "${PGNETWORK}" --name "psql-${PGHOST}" -e PGPASSWORD="${PGPASSWORD}" postgres:11 \
psql -h "${PGHOST}" -U "${PGUSER}"
