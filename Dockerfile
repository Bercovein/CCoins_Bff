# syntax=docker/dockerfile:experimental

########Maven build stage########
FROM maven:3.6-jdk-11 as maven_build

WORKDIR /app

#copy pom
COPY pom.xml .

#copy source
COPY src ./src

# Copiar la biblioteca libXext.so.6 en el directorio de la aplicación
# COPY /usr/lib64/libXext.so.6 ./lib

# ARG MAVEN_BUILD_COMMAND
# RUN ${MAVEN_BUILD_COMMAND}


# build the app and download dependencies only when these are new (thanks to the cache)
RUN --mount=type=cache,target=/root/.m2  mvn clean package -Dmaven.test.skip

# split the built app into multiple layers to improve layer rebuild
RUN mkdir -p target/docker-packaging && cd target/docker-packaging && jar -xf ../bff-app*.jar

RUN apt-get update \
    && apt-get install -y libxrender1 libxtst6 libxi6 libxext6

RUN apt-get install -y libxext6

# Instalar dependencias para AWT
# RUN apt-get update && \
#     apt-get install -y libxext-dev libxrender-dev

# COPY lib/libXext.so.6 /usr/lib/
# COPY lib/libXrender.so.1 /usr/lib/

########JRE run stage########
FROM openjdk:11.0-jre
WORKDIR /app

#copy built app layer by layer
ARG DOCKER_PACKAGING_DIR=/app/target/docker-packaging
COPY --from=maven_build ${DOCKER_PACKAGING_DIR}/BOOT-INF/lib /app/lib
COPY --from=maven_build ${DOCKER_PACKAGING_DIR}/BOOT-INF/classes /app/classes
COPY --from=maven_build ${DOCKER_PACKAGING_DIR}/META-INF /app/META-INF

EXPOSE 8082

ENV LD_LIBRARY_PATH="/usr/lib64:${LD_LIBRARY_PATH}"

#run the app
# CMD java -cp .:classes:lib/* \
#          -Djava.security.egd=file:/dev/./urandom \
#          com.ccoins.bff.BffApplication
CMD java -Djava.awt.headless=true -cp .:classes:lib/* \
         -Djava.security.egd=file:/dev/./urandom \
         com.ccoins.bff.BffApplication

# WORKDIR /build
# COPY pom.xml .
# RUN mvn dependency:go-offline
# COPY src ./src
# RUN mvn package -DskipTests

# FROM adoptopenjdk:11-jre-hotspot

# ARG JAR_FILE=/build/target/*.jar
# COPY --from=maven_build ${JAR_FILE} app.jar

# ENTRYPOINT ["java", "-jar", "/app.jar"]