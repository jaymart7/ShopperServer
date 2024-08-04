FROM gradle:7-jdk11 AS build
COPY --chown=gradle:gradle server /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle buildFatJar --no-daemon

FROM openjdk:11
EXPOSE 8080:8080
RUN mkdir /server
COPY --from=build /home/gradle/src/build/libs/*.jar /server/ShopperServer.jar
ENTRYPOINT ["java","-jar","/server/ShopperServer.jar"]