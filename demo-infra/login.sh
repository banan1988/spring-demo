#!/bin/bash

CONTAINER_NAME=$1

docker exec -it "${CONTAINER_NAME}" bash 2> /dev/null || winpty docker exec -it "${CONTAINER_NAME}" bash
