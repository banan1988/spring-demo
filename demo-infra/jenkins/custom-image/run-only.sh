#!/bin/bash

image=$(cat image)

docker run --rm --name jenkins.jdk11 -p 8080:8080 -e JAVA_OPTS=-Djenkins.install.runSetupWizard=false "${image}"
