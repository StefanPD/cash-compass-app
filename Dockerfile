FROM eclipse-temurin:21 as builder

ENV MAVEN_VERSION 3.9.5
ENV MAVEN_HOME /usr/share/maven
ENV PATH $MAVEN_HOME/bin:$PATH

ADD https://apache.osuosl.org/maven/maven-3/$MAVEN_VERSION/binaries/apache-maven-$MAVEN_VERSION-bin.tar.gz /tmp/maven.tar.gz

RUN mkdir -p $MAVEN_HOME \
    && tar -xzf /tmp/maven.tar.gz -C $MAVEN_HOME --strip-components=1 \
    && rm -f /tmp/maven.tar.gz

WORKDIR application
COPY . .

RUN mvn clean package -DskipTests && \
    java -Djarmode=layertools -jar target/*.jar extract


FROM eclipse-temurin:21

WORKDIR application

COPY --from=builder application/dependencies/ ./
COPY --from=builder application/spring-boot-loader/ ./
COPY --from=builder application/snapshot-dependencies/ ./
COPY --from=builder application/application/ ./

EXPOSE 8083

ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]