package com.milcomsolutions.config;


import java.beans.PropertyVetoException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.health.HealthCheckRegistry;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
public class DataSourceConfiguration {

    
    @Value("${spring.datasource.driverClassName}")
    private String jdbcDriver;
    
    @Value("${spring.datasource.url}")
    private String url;
    
    @Value("${spring.datasource.username}")
    private String username;
    
    @Value("${spring.datasource.password}")
    private String password;
    
    @Value("${spring.jpa.database-platform}")
    private String dialect;
    
    @Value("${spring.jpa.hibernate.ddl-auto:update}")
    private String hbm2ddl;
    
    @Value("${spring.jpa.hibernate.naming-strategy:org.hibernate.cfg.EJB3NamingStrategy}")
    private String namingStrategy;
    
    @Value("${spring.jpa.properties.hibernate.default_schema:}")
    private String defaultSchema;
    
    @Value("${datasource.pool.cachePrepStmts:true}")
    private String cachePrepStmts;
    
    @Value("${datasource.pool.prepStmtCacheSize:25}")
    private String stmtCacheSize;
    
    @Value("${datasource.pool.prepStmtCacheSqlLimit:2048}")
    private String prepStmtCacheSqlLimit;
    
    @Value("${datasource.pool.useServerPrepStmts:true}")
    private String userServerPrepStmts;
    
    @Value("${datasource.pool.leakDetectionThreshold:0}")
    private int leakDetectionThreshold;
    
    @Value("${datasource.pool.maxPoolSize:200}")
    private int maxPoolSize;
    
    @Value("${datasource.pool.minIdle:10}")
    private int minIdle;
    
    @Value("${datasource.pool.failFast:true}")
    private boolean failFast;
    
    @Autowired
    private MetricRegistry metricRegistry;



    @Bean(name = "jdbcTemplate")
    @Scope("prototype")
    @Autowired
    public JdbcTemplate jdbcTemplate(@Qualifier("dataSource") DataSource dsMaster) {
        return new JdbcTemplate(dsMaster);
    }


    


    @Bean(name = "dataSource", destroyMethod = "close")
    @Primary
    public DataSource dataSource(Environment env) throws ClassNotFoundException, PropertyVetoException {
        Class.forName(jdbcDriver);
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName(jdbcDriver);
        hikariConfig.setJdbcUrl(url);
        hikariConfig.setUsername(username);
        hikariConfig.setPassword(password);
        
        hikariConfig.setMaximumPoolSize(5);
        hikariConfig.setConnectionTestQuery("SELECT 1");
        hikariConfig.setPoolName("ApplicationDbPool");
        hikariConfig.setMaximumPoolSize(maxPoolSize);
        hikariConfig.setMinimumIdle(minIdle);
        hikariConfig.setInitializationFailFast(failFast);
        hikariConfig.setLeakDetectionThreshold(leakDetectionThreshold);
        
        hikariConfig.setMetricRegistry(metricRegistry);
        hikariConfig.setHealthCheckRegistry(healthRegistry());
        
        hikariConfig.addDataSourceProperty("cachePrepStmts", cachePrepStmts);
        hikariConfig.addDataSourceProperty("prepStmtCacheSize", stmtCacheSize);
        hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", prepStmtCacheSqlLimit);
        hikariConfig.addDataSourceProperty("useServerPrepStmts", userServerPrepStmts);
        
        HikariDataSource dataSource = new HikariDataSource(hikariConfig);
        return dataSource;
    }
    
  
    
    @Bean 
    public HealthCheckRegistry healthRegistry(){
        HealthCheckRegistry registry = new HealthCheckRegistry();
        return registry;
    }

    
    
}
