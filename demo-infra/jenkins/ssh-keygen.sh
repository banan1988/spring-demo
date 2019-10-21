#!/bin/bash

EMAIL=${1:-sping-demo@example.com}

ssh-keygen -t rsa -b 4096 -C "${EMAIL}" -f "${PWD}/ssh/id_rsa"
