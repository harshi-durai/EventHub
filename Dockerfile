FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY lib/mysql-connector-j-26.7.0.jar /app/lib/mysql-connector-j-26.7.0.jar
COPY src /app/src

RUN mkdir -p /app/bin

RUN javac -cp "/app/lib/mysql-connector-j-26.7.0.jar" -d /app/bin $(find /app/src -name "*.java")

EXPOSE 8080

CMD ["sh", "-c", "java -cp '/app/bin:/app/lib/mysql-connector-j-26.7.0.jar' api.ApiServer"]
