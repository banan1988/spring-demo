#!/bin/bash

version=$(date +"%Y-%m-%d.%M%S")
tag="demo/jenkins.lts-jdk11:${version}"

docker build --tag "${tag}" --ulimit nproc=65535 --ulimit nofile=10240:10240 .

echo "${tag}" > image
