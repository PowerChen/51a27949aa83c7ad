package com.powerting.spweb.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class HiveDruidDataSourceCfg {

//	private String url;
//	private String user;
//	private String password;
//	private String driverClassName;
//	private int initialSize;
//	private int minIdle;
//	private int maxActive;
//	private int maxWait;
//	private int timeBetweenEvictionRunsMillis;
//	private int minEvictableIdleTimeMillis;
//	private String validationQuery;
//	private boolean testWhileIdle;
//	private boolean testOnBorrow;
//	private boolean testOnReturn;
//	private boolean poolPreparedStatements;
//	private int maxPoolPreparedStatementPerConnectionSize;

    @Autowired
    private Environment env;

    @Bean(name = "hiveDruidDataSource")
    @Qualifier("hiveDruidDataSource")
    public DataSource dataSource() {
        DruidDataSource datasource = new DruidDataSource();
        datasource.setUrl(env.getProperty("hive.url"));
        datasource.setUsername(env.getProperty("hive.user"));
        datasource.setPassword(env.getProperty("hive.password"));
        datasource.setDriverClassName(env.getProperty("hive.driverClassName"));

        // pool configuration
        datasource.setInitialSize(Integer.parseInt(env.getProperty("hive.initialSize")));
        datasource.setMinIdle(Integer.parseInt(env.getProperty("hive.minIdle")));
        datasource.setMaxActive(Integer.parseInt(env.getProperty("hive.maxActive")));
        datasource.setMaxWait(Integer.parseInt(env.getProperty("hive.maxWait")));
        datasource.setTimeBetweenEvictionRunsMillis(Integer.parseInt(env.getProperty("hive.timeBetweenEvictionRunsMillis")));
        datasource.setMinEvictableIdleTimeMillis(Integer.parseInt(env.getProperty("hive.minEvictableIdleTimeMillis")));
        datasource.setValidationQuery(env.getProperty("hive.validationQuery"));
        datasource.setTestWhileIdle(Boolean.getBoolean(env.getProperty("hive.testWhileIdle")));
        datasource.setTestOnBorrow(Boolean.getBoolean(env.getProperty("hive.testOnBorrow")));
        datasource.setTestOnReturn(Boolean.getBoolean(env.getProperty("hive.testOnReturn")));
        datasource.setPoolPreparedStatements(Boolean.getBoolean(env.getProperty("hive.poolPreparedStatements")));
        datasource.setMaxPoolPreparedStatementPerConnectionSize(Integer.parseInt(env.getProperty("hive.maxPoolPreparedStatementPerConnectionSize")));
        return datasource;
    }

    // 此处省略各个属性的get和set方法

    @Bean(name = "hiveDruidTemplate")
    public JdbcTemplate hiveDruidTemplate(@Qualifier("hiveDruidDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

}