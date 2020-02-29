#!/bin/bash

NAME=${1:-demo-swarm}

echo "Let's create new network for swarm: ${NAME}"
docker network create \
  --scope=swarm \
  --attachable \
  -o "com.docker.network.enable_ipv6"="false" \
  -d overlay "${NAME}"
EXIT_CODE=$?

if [[ ${EXIT_CODE} != 0 ]]; then
  exit 1
fi

echo "Network has been created:"
docker network inspect "${NAME}"
