#!/bin/bash

CONTAINER_NAME=$1

docker inspect "${CONTAINER_NAME}" -f "{{json .NetworkSettings.Networks }}" | jq '.'
