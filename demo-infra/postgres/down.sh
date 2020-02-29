#!/bin/bash

STACK="${1:-demo-postgres}"

echo "Let's remove things created by docker-compose up:"
printf "\t- Containers for services defined in the Compose file\n"
printf "\t- Networks defined in the 'networks' section of the Compose file\n"
printf "\t- The default network, if one is used\n"

docker stack rm "${STACK}"
EXIT_CODE=$?

if [[ ${EXIT_CODE} != 0 ]]; then
  exit 1
fi

echo "----------------------------------------"
echo "Left running dockers:"
echo "----------------------------------------"
docker ps

echo "----------------------------------------"
echo "Left stack[${STACK}] volumes:"
echo "----------------------------------------"
docker volume ls | grep "VOLUME\|${STACK}"
