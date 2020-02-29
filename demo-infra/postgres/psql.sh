#!/bin/bash

PGNETWORK=demo-swarm
PGHOST=postgres
PGUSER=postgres
PGPASSWORD=postgres

docker run -it --rm --network "${PGNETWORK}" --name "psql-${PGHOST}" -e PGPASSWORD="${PGPASSWORD}" postgres:12 \
psql -h "${PGHOST}" -U "${PGUSER}"
