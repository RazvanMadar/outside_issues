FROM gradle:7.5 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle clean build -x test


FROM openjdk:11
EXPOSE 8080
COPY --from=build /home/gradle/src/build/libs/outside_issues-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]