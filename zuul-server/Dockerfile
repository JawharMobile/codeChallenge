FROM openjdk:alpine
ARG version=0.0.1-SNAPSHOT
COPY target/zuul-server-${version}.jar /zuul-server.jar
ENV JAVA_OPTS ""
ENTRYPOINT java -Djava.security.egd=file:/dev/./urandom $JAVA_OPTS -jar /zuul-server.jar
