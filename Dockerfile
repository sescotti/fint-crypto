FROM openjdk:11.0.8-slim
EXPOSE 8080
VOLUME /tmp

ADD /build/libs/crypto-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT [ "java", \
             "-server", \
             "-Djava.security.egd=file:/dev/./urandom", \
             "-Duser.timezone=UTC", \
             "-jar", "/app.jar" \
]
