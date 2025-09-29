package com.example.myhome.controller;

import com.example.myhome.dao.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * 健康检查控制器
 * 提供服务健康状态检查和数据库连接测试
 */
@RestController
public class HealthController {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private UserRepository userRepository;

    /**
     * 基础健康检查接口
     * 用于Kubernetes健康检查
     *
     * @return 健康状态信息
     */
    @GetMapping("/healthz")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        response.put("service", "MyHome Service");
        response.put("version", "1.0.0");

        return ResponseEntity.ok(response);
    }

    /**
     * 数据库连接健康检查
     * 测试MySQL数据库连接状态
     *
     * @return 数据库连接状态
     */
    @GetMapping("/health/database")
    public ResponseEntity<Map<String, Object>> databaseHealthCheck() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // 测试数据源连接
            try (Connection connection = dataSource.getConnection()) {
                boolean isValid = connection.isValid(5);
                response.put("database_connection", isValid ? "UP" : "DOWN");
                response.put("database_url", connection.getMetaData().getURL());
                response.put("database_driver", connection.getMetaData().getDriverName());
                response.put("database_version", connection.getMetaData().getDatabaseProductVersion());
            }

            // 测试Repository查询
            try {
                Long activeUserCount = userRepository.countActiveUsers();
                response.put("repository_test", "PASSED");
                response.put("active_users", activeUserCount != null ? activeUserCount : 0);
            } catch (Exception e) {
                response.put("repository_test", "FAILED");
                response.put("repository_error", e.getMessage());
            }

            response.put("status", "UP");
            response.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

            return ResponseEntity.ok(response);

        } catch (SQLException e) {
            response.put("status", "DOWN");
            response.put("error", e.getMessage());
            response.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            
            return ResponseEntity.status(503).body(response);
        }
    }

    /**
     * 详细的系统信息接口
     * 提供更详细的系统和数据库信息
     *
     * @return 系统信息
     */
    @GetMapping("/health/info")
    public ResponseEntity<Map<String, Object>> systemInfo() {
        Map<String, Object> response = new HashMap<>();
        
        // 系统信息
        response.put("service_name", "MyHome Service");
        response.put("version", "1.0.0");
        response.put("java_version", System.getProperty("java.version"));
        response.put("spring_profile", System.getProperty("spring.profiles.active", "dev"));
        response.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        
        // JVM信息
        Runtime runtime = Runtime.getRuntime();
        Map<String, Object> jvmInfo = new HashMap<>();
        jvmInfo.put("max_memory", runtime.maxMemory());
        jvmInfo.put("total_memory", runtime.totalMemory());
        jvmInfo.put("free_memory", runtime.freeMemory());
        jvmInfo.put("used_memory", runtime.totalMemory() - runtime.freeMemory());
        response.put("jvm", jvmInfo);

        return ResponseEntity.ok(response);
    }
}