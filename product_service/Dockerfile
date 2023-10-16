FROM openjdk:11 as build

EXPOSE 9082

ADD target/product_service-0.0.1-SNAPSHOT.jar product_service-0.0.1-SNAPSHOT.jar

ENV TZ=Asia/Jakarta

ENTRYPOINT ["java", "-jar", "product_service-0.0.1-SNAPSHOT.jar"]