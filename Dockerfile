#Build - Stage#

FROM gradle:jdk11 AS BUILD

COPY --chown=gradle:gradle . /home/gradle/src

WORKDIR /home/gradle/src

RUN gradle bootJar --no-daemon

#----------------------------------------------------------------------------#

#Produktions - Stage#

FROM openjdk:11-jre-slim as PRODUCTION

EXPOSE 8080

RUN mkdir /gruppenbildung
WORKDIR ./gruppenbildung
COPY --from=BUILD /home/gradle/src/build/libs/*.jar /gruppenbildung/gruppenbildung.jar
COPY wait-for-it.sh wait-for-it.sh
RUN ["chmod", "+x", "wait-for-it.sh"]


ENTRYPOINT ["./wait-for-it.sh", "database:3306", "--timeout=0", "--", "java", "-jar", "gruppenbildung.jar"]
