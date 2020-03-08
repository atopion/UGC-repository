package com.atopion.UGC_repository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfiguration {

    @Value("${app.sql.datasource.url}")
    private String sqlUrl;
    @Value("${app.sql.datasource.username}")
    private String sqlUsername;
    @Value("${app.sql.datasource.password}")
    private String sqlPassword;
    @Value("${app.sql.datasource.maximum-pool-size}")
    private int sqlMaximumPoolSize;


    @Bean
    public DataSource sqlDataSource() {
        return DataSourceBuilder.create()
                .driverClassName("com.mysql.cj.jdbc.Driver")
                .url(sqlUrl)
                .username(sqlUsername)
                .password(sqlPassword)
                .build();
    }
}
