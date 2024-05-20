FROM maven:3.9.6-eclipse-temurin-21-alpine AS builder
RUN mkdir /app
ADD pom.xml /app
WORKDIR /app
RUN mvn verify --fail-never
ADD . /app
RUN mvn package -DskipTests && \
    java -Djarmode=layertools -jar target/*.jar extract

FROM eclipse-temurin:21-jre-alpine
RUN apk add dumb-init

RUN mkdir /cca-app

RUN addgroup --system pduser && adduser -S -s /bin/false -G pduser pduser
COPY --from=builder app/dependencies/ ./cca-app
COPY --from=builder app/spring-boot-loader/ ./cca-app
COPY --from=builder app/snapshot-dependencies/ ./cca-app
COPY --from=builder app/application/ ./cca-app

WORKDIR cca-app

RUN chown -R pduser:pduser /cca-app
USER pduser


EXPOSE 8083
CMD "dumb-init" "java" "org.springframework.boot.loader.launch.JarLauncher"