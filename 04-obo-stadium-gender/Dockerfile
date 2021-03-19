#FROM openjdk:15-alpine
#RUN mkdir /app
#WORKDIR /app
#COPY target/obo-stadium-0.0.1-SNAPSHOT.jar .
#CMD ["java","-jar","obo-stadium-0.0.1-SNAPSHOT.jar"]

FROM maven:latest
WORKDIR /app
COPY . .
CMD ["mvn", "spring-boot:run"]