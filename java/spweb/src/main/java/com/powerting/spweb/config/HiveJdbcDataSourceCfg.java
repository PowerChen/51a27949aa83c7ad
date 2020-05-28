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
public class HiveJdbcDataSourceCfg {

    @Autowired
    private Environment env;

    @Bean(name = "hiveDataSource")
    @Qualifier("hiveDataSourceCfg")
    public DataSource dataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl(env.getProperty("hive.url"));
        dataSource.setDriverClassName(env.getProperty("hive.driver-class-name"));
        dataSource.setUsername(env.getProperty("hive.user"));
        dataSource.setPassword(env.getProperty("hive.password"));
        return dataSource;
    }

    @Bean(name = "hiveTemplate")
    public JdbcTemplate hiveJdbcTemplate(@Qualifier("hiveDataSourceCfg") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

}
	