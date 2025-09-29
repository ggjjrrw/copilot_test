package com.example.myhome.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.sql.DataSource;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * 数据库配置测试类
 * 验证MySQL连接配置是否正确
 */
@SpringBootTest
@ActiveProfiles("test")
class DatabaseConfigTest {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private MySqlConfig mySqlConfig;

    @Test
    void testDataSourceConfiguration() {
        assertNotNull(dataSource, "DataSource should not be null");
    }

    @Test
    void testMySqlConfigurationProperties() {
        assertNotNull(mySqlConfig, "MySqlConfig should not be null");
        assertNotNull(mySqlConfig.getDriverClassName(), "Driver class name should not be null");
    }
}