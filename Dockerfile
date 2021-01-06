FROM adoptopenjdk/openjdk11:latest
VOLUME /tmp
COPY target/*.jar rlstop-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/rlstop-0.0.1-SNAPSHOT.jar", "--spring.config.name=docker"]