FROM openjdk:alpine
ARG version=0.0.1-SNAPSHOT
COPY target/flight-data-${version}.jar /flight-data.jar
ENV JAVA_OPTS ""
ENTRYPOINT java -Djava.security.egd=file:/dev/./urandom $JAVA_OPTS -jar /flight-data.jar
