FROM openjdk:11 as build

EXPOSE 9081

ADD target/authentication_service-0.0.1-SNAPSHOT.jar authentication_service-0.0.1-SNAPSHOT.jar

ENV TZ=Asia/Jakarta

ENTRYPOINT ["java", "-jar", "authentication_service-0.0.1-SNAPSHOT.jar"]