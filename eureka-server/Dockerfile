FROM eclipse-temurin:17-jdk-alpine

# Tạo thư mục app
WORKDIR /app

# Copy file jar đã build vào image
COPY target/eureka-server-0.0.1-SNAPSHOT.jar app.jar

# Mở port Eureka Server
EXPOSE 8761

# Chạy ứng dụng
ENTRYPOINT ["java", "-jar", "app.jar"]