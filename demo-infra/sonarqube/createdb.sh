#!/bin/bash

USER=sonar
PASSWORD=sonar
DBNAME=sonarqube

function psql_exec() {
  local command=$1

  if [ -z "$command" ]; then
    echo "[psql] Command is required !"
    exit 1
  fi

  local pgnetwork=demo-swarm
  local pghost=postgres
  local pguser=postgres
  local pgpassword=postgres

  docker run -it --rm --network "${pgnetwork}" --name psql -e PGPASSWORD="${pgpassword}" postgres:12 \
    psql -h "${pghost}" -U "${pguser}" -c "${command}"
}

echo "Create user/role: ${USER}"
psql_exec "CREATE ROLE ${USER} LOGIN PASSWORD '${PASSWORD}';"

echo "Create database: ${DBNAME}"
psql_exec "CREATE DATABASE ${DBNAME} WITH ENCODING 'UTF8' OWNER ${USER} TEMPLATE=template0;"
