#!/bin/bash

cd docker-class-recorder/docker-runnables/crecorder-pc-prod
# Docker login and push of crecorder-pc-prod
echo "$DOCKER_PASSWORD" | docker login -u "$DOCKER_USERNAME" --password-stdin
docker build . -t cruizba/crecorder-pc-prod:$TRAVIS_TAG
docker push cruizba/crecorder-pc-prod