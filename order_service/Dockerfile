FROM openjdk:11 as build

EXPOSE 9080

ADD target/order_service-0.0.1-SNAPSHOT.jar order_service-0.0.1-SNAPSHOT.jar

ENV TZ=Asia/Jakarta

ENTRYPOINT ["java", "-jar", "order_service-0.0.1-SNAPSHOT.jar"]