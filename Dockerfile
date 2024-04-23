# Step 1: Build stage using Eclipse Temurin for JDK 21
FROM eclipse-temurin:21 as builder

# Set environment variables for Maven version and installation paths
ENV MAVEN_VERSION 3.9.5
ENV MAVEN_HOME /usr/share/maven
ENV PATH $MAVEN_HOME/bin:$PATH

# Install Maven
RUN apt-get update && apt-get install -y wget \
  && mkdir -p $MAVEN_HOME \
  && wget -q -O maven.tar.gz "https://apache.osuosl.org/maven/maven-3/$MAVEN_VERSION/binaries/apache-maven-$MAVEN_VERSION-bin.tar.gz" \
  && tar -xzf maven.tar.gz -C $MAVEN_HOME --strip-components=1 \
  && rm -f maven.tar.gz \
  && apt-get clean \
  && rm -rf /var/lib/apt/lists/*

WORKDIR application
COPY . .
RUN mvn clean package -DskipTests

# Unpack the layered JAR file to extract dependencies and application layers
RUN java -Djarmode=layertools -jar target/*.jar extract

# Step 2: Run stage, also using Eclipse Temurin
FROM eclipse-temurin:21

WORKDIR application
# Copy the layers from the builder stage
COPY --from=builder application/dependencies/ ./
COPY --from=builder application/spring-boot-loader/ ./
COPY --from=builder application/snapshot-dependencies/ ./
COPY --from=builder application/application/ ./

# Expose the port the app runs on
EXPOSE 8083

# Command to run the application
ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]