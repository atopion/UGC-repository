FROM alpine:3.11.3
MAINTAINER atopi@atopion.com

ENV PATH=$PATH:/usr/lib/jvm/java-1.8-openjdk/bin
RUN apk update && apk add openjdk11-jre

WORKDIR /dist

COPY build/libs/$jarFile /dist/UGC-rep.jar

EXPOSE 8080
EXPOSE 8443

CMD ["java", "-jar", "/dist/UGC-rep.jar"]