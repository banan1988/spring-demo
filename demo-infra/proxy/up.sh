#!/bin/bash

docker-compose up -d
EXIT_CODE=$?

if [[ ${EXIT_CODE} != 0 ]]; then
  exit 1
fi

docker ps
