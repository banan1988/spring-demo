#!/bin/bash

echo "Let's remove things created by docker-compose up:"
printf "\t- Containers for services defined in the Compose file\n"
printf "\t- Networks defined in the 'networks' section of the Compose file\n"
printf "\t- The default network, if one is used\n"

docker-compose down
EXIT_CODE=$?

if [[ ${EXIT_CODE} != 0 ]]; then
  exit 1
fi

docker ps
