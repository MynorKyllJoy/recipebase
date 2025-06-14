FROM eclipse-temurin:24-jdk AS builder
WORKDIR /recipebase
COPY . .
RUN ./gradlew build

FROM eclipse-temurin:24-jre
COPY --from=builder /recipebase/build/libs/recipebase-0.0.1-SNAPSHOT.jar /app/recipebase.jar
ENTRYPOINT ["java", "-jar", "/app/recipebase.jar"]
EXPOSE 8080