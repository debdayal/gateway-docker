FROM java:8
VOLUME /tmp
EXPOSE 8082
ADD gateway-1.0.jar app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
