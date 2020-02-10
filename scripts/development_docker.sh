#!/bin/bash

docker rm -f postgres
docker rm -f piper

docker run --name postgres -e POSTGRES_PASSWORD=piper -e POSTGRES_USER=piper -e POSTGRES_DB=piper -d -p 5432:5432 postgres:11

docker run \
       --name piper \
       --link postgres:postgres \
       -e PIPER_MESSAGE_BROKER_PROVIDER=jms \
       -e SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/piper \
       -e PIPER_COORDINATOR_ENABLED=true \
       -e PIPER_WORKER_ENABLED=true \
       -e PIPER_WORKER_SUBSCRIPTIONS_TASKS=1 \
       -e PIPER_PIPELINE_REPOSITORY_GIT_ENABLED=true \
       -e PIPER_PIPELINE_REPOSITORY_GIT_URL=https://github.com/creactiviti/piper-pipelines.git \
       -e PIPER_PIPELINE_REPOSITORY_GIT_SEARCH_PATHS=demo/,video/ \
       -e PIPER_PIPELINE_REPOSITORY_GIT_USERNAME= \
       -e PIPER_PIPELINE_REPOSITORY_GIT_PASSWORD= \
       -d \
       -p 5454:8080 \
       creactiviti/piper:2
