# syntax=docker/dockerfile:1

# ---------- Build stage ----------
FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app

COPY pom.xml .
COPY src ./src

# Cache ~/.m2 between builds so dependencies are not re-downloaded every time
RUN --mount=type=cache,target=/root/.m2 \
    mvn -B -q -DskipTests package

# ---------- Runtime stage ----------
FROM eclipse-temurin:17-jre
WORKDIR /app

ENV TZ=Asia/Kolkata \
    JAVA_OPTS="-Xms256m -Xmx1024m"

# Upload directory used by storage.file.directory.location (mounted as a volume at runtime)
RUN mkdir -p /home/metaverseedu/public_html/workflowfiles

COPY --from=build /app/target/workflow.war /app/workflow.war

EXPOSE 8082

ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -jar /app/workflow.war"]
