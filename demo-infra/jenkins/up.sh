#!/bin/bash

. ../functions.sh

echo "Let's up docker-compose:"
docker-compose up -d
EXIT_CODE=$?

if [[ ${EXIT_CODE} != 0 ]]; then
  exit 1
fi

echo "Docker-compose has finished:"
MASTER_container_name="jenkins-master"
MATER_is_running=$(with_backoff docker inspect -f "{{.State.Running}}" "${MASTER_container_name}")

if [[ "${MATER_is_running}" == "true" ]]; then
  AdminPassword=/var/jenkins_home/secrets/initialAdminPassword
  PASSWORD=$(docker exec -it "${MASTER_container_name}" cat \""${AdminPassword}"\" 2> /dev/null || winpty docker exec -it "${MASTER_container_name}" cat \""${AdminPassword}"\")

  echo "Default credentials are: admin / ${PASSWORD}"
fi

docker ps
