#!/bin/bash
sudo rm -r data
COMPOSE_DOCKER_CLI_BUILD=1 DOCKER_BUILDKIT=1 docker-compose --env-file ./.env -f docker-compose.yml up --build --remove-orphans
# COMPOSE_DOCKER_CLI_BUILD=1 DOCKER_BUILDKIT=1 docker-compose up  -d