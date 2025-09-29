# MySQL数据库连接配置文档

## 概述

本项目实现了完整的MySQL数据库连接框架配置，支持多环境部署和灵活的配置管理。

## 配置特性

### 1. 多环境支持
- **开发环境 (dev)**: 本地开发使用的轻量级配置
- **测试环境 (test)**: 测试阶段使用的中等配置
- **生产环境 (prod)**: 生产部署使用的高性能配置

### 2. 灵活的配置方式
- 支持环境变量配置
- 支持Spring Profile配置
- 支持Kubernetes Secret管理敏感信息

### 3. 连接池优化
- 使用HikariCP高性能连接池
- 针对不同环境优化连接池参数
- 支持连接健康检查和自动重连

## 配置文件说明

### Spring Boot配置文件
- `src/main/resources/application.yml`: 主配置文件，包含所有环境的配置

### Helm Chart配置
- `manifests/myhome/values.yaml`: 默认Helm配置
- `manifests/myhome/values-dev.yaml`: 开发环境配置
- `manifests/myhome/values-test.yaml`: 测试环境配置
- `manifests/myhome/values-prod.yaml`: 生产环境配置

## 使用方法

### 1. 本地开发
```bash
# 设置环境变量
export MYSQL_HOST=localhost
export MYSQL_PORT=3306
export MYSQL_DATABASE=myhome_dev
export MYSQL_USERNAME=root
export MYSQL_PASSWORD=your_password

# 启动应用
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

### 2. Docker部署
```bash
# 构建镜像
docker build -f build/Dockerfile -t myhome:latest .

# 运行容器
docker run -p 8080:8080 \
  -e MYSQL_HOST=your_mysql_host \
  -e MYSQL_PORT=3306 \
  -e MYSQL_DATABASE=myhome \
  -e MYSQL_USERNAME=your_username \
  -e MYSQL_PASSWORD=your_password \
  -e SPRING_PROFILES_ACTIVE=prod \
  myhome:latest
```

### 3. Kubernetes部署
```bash
# 开发环境部署
helm install myhome-dev ./manifests/myhome -f ./manifests/myhome/values-dev.yaml

# 测试环境部署
helm install myhome-test ./manifests/myhome -f ./manifests/myhome/values-test.yaml

# 生产环境部署（需要先创建Secret）
kubectl create secret generic mysql-prod-secret --from-literal=password=your_prod_password
helm install myhome-prod ./manifests/myhome -f ./manifests/myhome/values-prod.yaml
```

## 配置参数说明

### MySQL连接配置
| 参数 | 环境变量 | 默认值 | 说明 |
|------|----------|--------|------|
| mysql.host | MYSQL_HOST | localhost | MySQL服务器地址 |
| mysql.port | MYSQL_PORT | 3306 | MySQL服务器端口 |
| mysql.database | MYSQL_DATABASE | myhome | 数据库名称 |
| mysql.username | MYSQL_USERNAME | root | 数据库用户名 |
| mysql.password | MYSQL_PASSWORD | - | 数据库密码 |

### 连接池配置
| 参数 | 说明 | 开发环境 | 测试环境 | 生产环境 |
|------|------|----------|----------|----------|
| maxActive | 最大活跃连接数 | 10 | 15 | 50 |
| maxIdle | 最大空闲连接数 | 5 | 8 | 20 |
| minIdle | 最小空闲连接数 | 2 | 3 | 10 |
| maxWait | 最大等待时间(ms) | 60000 | 60000 | 60000 |

## 健康检查

应用提供了多个健康检查端点：

1. **基础健康检查**: `GET /healthz`
   - 用于Kubernetes liveness和readiness探针

2. **数据库健康检查**: `GET /health/database`
   - 检查数据库连接状态
   - 测试Repository查询功能

3. **系统信息**: `GET /health/info`
   - 提供详细的系统和JVM信息

## 数据库初始化

应用使用JPA自动创建和更新数据库表结构：
- 开发环境：`hibernate.hbm2ddl.auto=update`
- 生产环境：建议设置为`validate`并使用数据库迁移工具

## 示例实体和Repository

项目包含了示例的User实体和UserRepository，演示了：
- JPA实体映射
- 基础CRUD操作
- 自定义查询方法
- 审计字段自动填充

## 安全考虑

1. **生产环境密码管理**：使用Kubernetes Secret存储敏感信息
2. **SSL连接**：生产环境启用SSL连接
3. **连接池监控**：配置连接池泄漏检测
4. **最小权限原则**：为应用创建专用数据库用户

## 监控和日志

- 使用Spring Boot Actuator提供监控端点
- 配置HikariCP连接池监控
- 根据环境设置不同的日志级别

## 故障排查

常见问题和解决方案：

1. **连接超时**：检查网络连通性和防火墙设置
2. **连接池耗尽**：调整连接池参数或检查连接泄漏
3. **认证失败**：验证用户名密码和权限设置
4. **SSL连接问题**：检查证书配置和SSL参数