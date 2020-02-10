#!/bin/sh

docker rm -f postgres

docker run --name postgres -e POSTGRES_PASSWORD=piper -e POSTGRES_USER=piper -e POSTGRES_DB=piper -d -p 5432:5432 postgres:11
