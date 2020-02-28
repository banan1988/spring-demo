#!/bin/bash

COMMENT=${1:-jenkinsAgent}

ssh-keygen -t rsa -b 4096 -C "${COMMENT}" -f "${PWD}/agent-ssh-key/id_rsa"
