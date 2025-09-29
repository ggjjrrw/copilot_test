package com.example.myhome.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * MySQL数据库连接配置类
 * 支持不同环境下的数据库配置
 */
@Configuration
@ConfigurationProperties(prefix = "spring.datasource.mysql")
public class MySqlConfig {

    private String url;
    private String username;
    private String password;
    private String driverClassName = "com.mysql.cj.jdbc.Driver";
    private Pool pool = new Pool();
    private Connection connection = new Connection();

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    public Pool getPool() {
        return pool;
    }

    public void setPool(Pool pool) {
        this.pool = pool;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    /**
     * 数据库连接池配置
     */
    public static class Pool {
        private int maxActive = 20;
        private int maxIdle = 10;
        private int minIdle = 5;
        private long maxWait = 60000;

        public int getMaxActive() {
            return maxActive;
        }

        public void setMaxActive(int maxActive) {
            this.maxActive = maxActive;
        }

        public int getMaxIdle() {
            return maxIdle;
        }

        public void setMaxIdle(int maxIdle) {
            this.maxIdle = maxIdle;
        }

        public int getMinIdle() {
            return minIdle;
        }

        public void setMinIdle(int minIdle) {
            this.minIdle = minIdle;
        }

        public long getMaxWait() {
            return maxWait;
        }

        public void setMaxWait(long maxWait) {
            this.maxWait = maxWait;
        }
    }

    /**
     * 数据库连接配置
     */
    public static class Connection {
        private long connectionTimeout = 30000;
        private long validationTimeout = 5000;
        private String validationQuery = "SELECT 1";

        public long getConnectionTimeout() {
            return connectionTimeout;
        }

        public void setConnectionTimeout(long connectionTimeout) {
            this.connectionTimeout = connectionTimeout;
        }

        public long getValidationTimeout() {
            return validationTimeout;
        }

        public void setValidationTimeout(long validationTimeout) {
            this.validationTimeout = validationTimeout;
        }

        public String getValidationQuery() {
            return validationQuery;
        }

        public void setValidationQuery(String validationQuery) {
            this.validationQuery = validationQuery;
        }
    }
}