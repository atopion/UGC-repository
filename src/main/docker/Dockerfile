FROM alpine:3.11.3
MAINTAINER atopi@atopion.com

ENV PATH=$PATH:/usr/lib/jvm/java-1.8-openjdk/bin
RUN apk update && apk add openjdk8-jre

WORKDIR /dist

# TODO: Introduce ARGS for variable build version.
COPY build/libs/UGC-repository-0.2-SNAPSHOT.jar /dist/UGC-rep.jar

EXPOSE 8080

CMD ["java", "-jar", "/dist/UGC-rep.jar"]