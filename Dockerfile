FROM openjdk:17-jdk-slim as builder
RUN apt update && apt install -y findutils
RUN USER=root mkdir battleshipBack
WORKDIR /battleshipBack
COPY . .
RUN ./gradlew bootjar
# Final stage
FROM openjdk:17-jdk-slim
RUN apt-get update && apt-get install -y fontconfig libfreetype6
COPY --from=builder /battleshipBack/app/build/libs/app.jar .
CMD ["java", "-jar", "app.jar"]
