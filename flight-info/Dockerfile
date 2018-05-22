FROM openjdk:alpine
ARG version=0.0.1-SNAPSHOT
COPY target/flight-info-${version}.jar /flight-info.jar
ENV JAVA_OPTS ""
ENTRYPOINT java -Djava.security.egd=file:/dev/./urandom $JAVA_OPTS -jar /flight-info.jar
