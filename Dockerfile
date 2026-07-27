# 1. Build Stage
FROM eclipse-temurin:21-jdk-alpine AS builder
WORKDIR /app

# Gradle Wrapper 및 빌드 파일 복사 (캐싱 활용)
COPY gradlew .
COPY gradle gradle
COPY build.gradle settings.gradle ./
RUN chmod +x ./gradlew
RUN ./gradlew dependencies --no-daemon

# 소스코드 복사 및 빌드
COPY src src
RUN ./gradlew bootJar -x test --no-daemon

# 2. Run Stage
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# 빌드 결과물 복사
COPY --from=builder /app/build/libs/*.jar app.jar

# Render PORT 주입 대응 및 JVM 메모리 제한 옵션 설정
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"
EXPOSE ${PORT:-10000}

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -Dserver.port=${PORT:-10000} -jar app.jar"]