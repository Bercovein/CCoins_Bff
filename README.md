# CCoins-ms

* To run all microservices with cached m2 execute:
$ COMPOSE_DOCKER_CLI_BUILD=1 DOCKER_BUILDKIT=1 docker-compose up --build

* Or only execute:
$ ./run.sh

spring.datasource.url=jdbc:mysql://${MYSQL_HOST:localhost}:${MYSQL_PORT:3306}/chopp_coins
