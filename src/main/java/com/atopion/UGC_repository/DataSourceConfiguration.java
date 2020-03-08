package com.atopion.UGC_repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfiguration {

    @Bean
    @Autowired
    public DataSource restDataSource(@Qualifier("REST") RESTConfiguration config) {

        System.out.println(config.toString());

        return DataSourceBuilder.create()
                .url(config.getUrl())
                .driverClassName(config.getDriverClassName())
                .username(config.getUsername())
                .password(config.getPassword())
                .build();
    }


    @Bean
    @Autowired
    public DataSource sqlDataSource(@Qualifier("SQL") SQLConfiguration config) {

        System.out.println(config.toString());

        return DataSourceBuilder.create()
                .url(config.getUrl())
                .driverClassName(config.getDriverClassName())
                .username(config.getUsername())
                .password(config.getPassword())
                .build();
    }

    @Configuration
    @ConfigurationProperties(prefix = "app.sql.datasource")
    @Qualifier("SQL")
    public static class SQLConfiguration {

        private String url;
        private String username;
        private String password;
        private String driverClassName;

        @Override
        public String toString() {
            return "SQLConfiguration{ url='" + url +
                    "', username='" + username +
                    "', password='" + password +
                    ", driverClassName=" + driverClassName +
                    "'}";
        }

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
    }


    @Configuration
    @ConfigurationProperties(prefix = "app.rest.datasource")
    @Qualifier("REST")
    public static class RESTConfiguration {

        private String url;
        private String username;
        private String password;
        private String driverClassName;

        @Override
        public String toString() {
            return "SQLConfiguration{ url='" + url +
                    "', username='" + username +
                    "', password='" + password +
                    ", driverClassName=" + driverClassName +
                    "'}";
        }

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
    }
}
