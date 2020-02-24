#!/bin/bash

function os_type() {
  case "${OSTYPE}" in
  solaris*) echo "SOLARIS" ;;
  darwin*) echo "OSX" ;;
  linux*) echo "LINUX" ;;
  bsd*) echo "BSD" ;;
  msys*) echo "WINDOWS" ;;
  *) echo "unknown: ${OSTYPE}" ;;
  esac
}

function with_backoff() {
  local max_attempts=${ATTEMPTS-5}
  local timeout=${TIMEOUT-1}
  local attempt=0
  local exitCode=0

  while [[ $attempt < $max_attempts ]]; do
    "$@"
    exitCode=$?

    if [[ ${exitCode} == 0 ]]; then
      break
    fi

    echo "Failure! Retrying in ${timeout}.." 1>&2
    sleep "${timeout}"
    attempt=$((attempt + 1))
    timeout=$((timeout * 2))
  done

  if [[ ${exitCode} != 0 ]]; then
    echo "You've failed me for the last time! ($@)" 1>&2
  fi

  return ${exitCode}
}
