FROM openjdk:11
EXPOSE 8080
RUN  mkdir -p /mancala
WORKDIR /mancala
COPY target/mancalaBackend-1.0.jar ./mancalaBackend-1.0.jar
ENTRYPOINT ["java","-jar","/mancala/mancalaBackend-1.0.jar"]
