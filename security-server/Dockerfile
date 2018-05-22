FROM openjdk:alpine
ARG version=0.0.1-SNAPSHOT
COPY target/security-server-${version}.jar /security-server.jar
ENV JAVA_OPTS ""
ENTRYPOINT java -Djava.security.egd=file:/dev/./urandom $JAVA_OPTS -jar /security-server.jar
