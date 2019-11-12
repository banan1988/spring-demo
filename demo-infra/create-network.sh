#!/bin/bash

NAME=${1:-demo}

echo "Let's create new network: ${NAME}"
docker network create \
  -o "com.docker.network.enable_ipv6"="false" \
  -d bridge "${NAME}"
EXIT_CODE=$?

if [[ ${EXIT_CODE} != 0 ]]; then
  exit 1
fi

echo "Network has been created:"
docker network inspect "${NAME}"
