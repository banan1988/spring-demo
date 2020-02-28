#!/bin/bash

. ../functions.sh

AdminPassword=/var/jenkins_home/secrets/initialAdminPassword

MASTER_container_name="${1:-jenkins-master}"
MASTER_is_running=$(with_backoff docker inspect -f "{{.State.Running}}" "${MASTER_container_name}")

if [[ "${MASTER_is_running}" == "true" ]]; then
  PASSWORD=$(docker exec -it "${MASTER_container_name}" cat "${AdminPassword}" 2>/dev/null || winpty docker exec -it "${MASTER_container_name}" cat \""${AdminPassword}"\")

  echo "Default credentials are: admin / ${PASSWORD}"
  exit 0
fi

echo "[ERROR] Couldn't get initialAdminPassword from ${MASTER_container_name}. Try manually - /var/jenkins_home/secrets/initialAdminPassword"
exit 1
