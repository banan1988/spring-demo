#!/bin/bash

. ../functions.sh

with_backoff curl -vvv http://localhost:8080/credentials/
