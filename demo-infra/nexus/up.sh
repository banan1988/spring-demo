#!/bin/bash

STACK="${1:-demo}"

docker stack deploy --compose-file docker-compose.yaml "${STACK}"
EXIT_CODE=$?

if [[ ${EXIT_CODE} != 0 ]]; then
    exit 1
fi

echo "----------------------------------------"
echo "Running stacks:"
echo "----------------------------------------"
docker stack ls

echo "----------------------------------------"
echo "Running dockers in stack[${STACK}]:"
echo "----------------------------------------"
docker stack ps "${STACK}"

echo "----------------------------------------"
echo "Running dockers:"
echo "----------------------------------------"
docker ps
