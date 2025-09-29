# 多阶段构建Dockerfile
# Stage 1: 构建阶段
FROM openjdk:17-jdk-slim as builder

WORKDIR /app

# 复制Maven配置文件
COPY pom.xml .
COPY src ./src

# 安装Maven
RUN apt-get update && apt-get install -y maven

# 构建应用
RUN mvn clean package -DskipTests

# Stage 2: 运行阶段
FROM openjdk:17-jre-slim

WORKDIR /app

# 创建应用用户
RUN useradd --create-home --shell /bin/bash app

# 复制构建好的jar文件
COPY --from=builder /app/target/*.jar app.jar

# 设置文件所有者
RUN chown app:app app.jar

# 切换到应用用户
USER app

# 设置JVM参数和环境变量
ENV JAVA_OPTS="-Xmx512m -Xms256m -Dfile.encoding=UTF-8 -Duser.timezone=Asia/Shanghai"

# MySQL连接参数可以通过环境变量传入
ENV MYSQL_HOST=localhost
ENV MYSQL_PORT=3306
ENV MYSQL_DATABASE=myhome
ENV MYSQL_USERNAME=root
ENV MYSQL_PASSWORD=password

# 暴露端口
EXPOSE 8080

# 健康检查
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
    CMD curl -f http://localhost:8080/healthz || exit 1

# 启动应用
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]