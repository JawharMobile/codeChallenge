FROM openjdk:alpine
ARG version=0.0.1-SNAPSHOT
COPY target/config-server-${version}.jar /config-server.jar
ENV JAVA_OPTS ""
ENTRYPOINT java -Djava.security.egd=file:/dev/./urandom $JAVA_OPTS -jar /config-server.jar
