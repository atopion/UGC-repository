package com.atopion.UGC_repository;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariConfigMXBean;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.util.Objects;

@Configuration
public class DataSourceConfiguration {

    @Primary
    @Bean
    @Autowired
    public DataSource restDataSource(@Qualifier("REST") RESTConfiguration config) {

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

        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(config.url);
        hikariConfig.setUsername(config.getUsername());
        hikariConfig.setPassword(config.getPassword());
        hikariConfig.setDriverClassName(config.driverClassName);

        return new HikariDataSource(hikariConfig);
    }

    @Configuration
    @ConfigurationProperties(prefix = "app.sql.datasource")
    @Qualifier("SQL")
    public static class SQLConfiguration {

        private String url;
        private String username;
        private String password;
        private String driverClassName;
        private int maximumPoolSize;

        @Override
        public String toString() {
            return "SQLConfiguration{ url='" + url +
                    "', username='" + username +
                    "', password='" + password +
                    ", driverClassName=" + driverClassName +
                    "'}";
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null) return false;
            if (!(o instanceof SQLConfiguration)) return false;
            SQLConfiguration that = (SQLConfiguration) o;
            return maximumPoolSize == that.maximumPoolSize &&
                    Objects.equals(url, that.url) &&
                    Objects.equals(username, that.username) &&
                    Objects.equals(password, that.password) &&
                    Objects.equals(driverClassName, that.driverClassName);
        }

        @Override
        public int hashCode() {
            return Objects.hash(url, username, password, driverClassName, maximumPoolSize);
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

        public int getMaximumPoolSize() {
            return maximumPoolSize;
        }

        public void setMaximumPoolSize(int maximumPoolSize) {
            this.maximumPoolSize = maximumPoolSize;
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
        private int maximumPoolSize;

        @Override
        public String toString() {
            return "SQLConfiguration{ url='" + url +
                    "', username='" + username +
                    "', password='" + password +
                    ", driverClassName=" + driverClassName +
                    "'}";
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null) return false;
            if (!(o instanceof RESTConfiguration)) return false;
            RESTConfiguration that = (RESTConfiguration) o;
            return maximumPoolSize == that.maximumPoolSize &&
                    Objects.equals(url, that.url) &&
                    Objects.equals(username, that.username) &&
                    Objects.equals(password, that.password) &&
                    Objects.equals(driverClassName, that.driverClassName);
        }

        @Override
        public int hashCode() {
            return Objects.hash(url, username, password, driverClassName, maximumPoolSize);
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

        public int getMaximumPoolSize() {
            return maximumPoolSize;
        }

        public void setMaximumPoolSize(int maximumPoolSize) {
            this.maximumPoolSize = maximumPoolSize;
        }
    }
}
