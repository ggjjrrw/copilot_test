package com.example.myhome.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * 数据库配置类
 * 配置MySQL数据源、JPA和事务管理
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "com.example.myhome.dao")
@EnableConfigurationProperties(MySqlConfig.class)
public class DatabaseConfig {

    @Autowired
    private MySqlConfig mySqlConfig;

    /**
     * 配置MySQL数据源
     * 使用HikariCP连接池
     */
    @Bean
    @Primary
    public DataSource dataSource() {
        HikariConfig config = new HikariConfig();
        
        config.setJdbcUrl(mySqlConfig.getUrl());
        config.setUsername(mySqlConfig.getUsername());
        config.setPassword(mySqlConfig.getPassword());
        config.setDriverClassName(mySqlConfig.getDriverClassName());
        
        // 连接池配置
        config.setMaximumPoolSize(mySqlConfig.getPool().getMaxActive());
        config.setMinimumIdle(mySqlConfig.getPool().getMinIdle());
        config.setConnectionTimeout(mySqlConfig.getConnection().getConnectionTimeout());
        config.setValidationTimeout(mySqlConfig.getConnection().getValidationTimeout());
        config.setConnectionTestQuery(mySqlConfig.getConnection().getValidationQuery());
        
        // 连接池名称
        config.setPoolName("MySqlHikariCP");
        
        // 连接池优化配置
        config.setIdleTimeout(600000);
        config.setMaxLifetime(1800000);
        config.setLeakDetectionThreshold(60000);
        
        return new HikariDataSource(config);
    }

    /**
     * 配置JPA EntityManagerFactory
     */
    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource());
        em.setPackagesToScan("com.example.myhome.entity");
        
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        
        Properties properties = new Properties();
        properties.setProperty("hibernate.hbm2ddl.auto", "update");
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
        properties.setProperty("hibernate.show_sql", "false");
        properties.setProperty("hibernate.format_sql", "true");
        properties.setProperty("hibernate.use_sql_comments", "true");
        properties.setProperty("hibernate.jdbc.batch_size", "20");
        properties.setProperty("hibernate.order_inserts", "true");
        properties.setProperty("hibernate.order_updates", "true");
        properties.setProperty("hibernate.jdbc.batch_versioned_data", "true");
        
        em.setJpaProperties(properties);
        
        return em;
    }

    /**
     * 配置事务管理器
     */
    @Bean
    @Primary
    public PlatformTransactionManager transactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
        return transactionManager;
    }
}