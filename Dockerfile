FROM openjdk:latest
COPY ./target/setMethods.jar /tmp
WORKDIR /tmp
ENTRYPOINT ["java", "-jar", "setMethods.jar", "db:3306", "30000"]