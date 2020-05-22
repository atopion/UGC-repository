package com.atopion.UGC_repository.rest;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Objects;

@Configuration
@EnableJpaRepositories(basePackages = "com.atopion.UGC_repository.rest", entityManagerFactoryRef = "restEntityManagerFactory", transactionManagerRef = "restTransactionManager")
@EnableTransactionManagement
public class RestDataSourceConfig {

    @Bean
    public DataSource restDataSource(RESTConfiguration config) {

        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(config.getUrl());
        hikariConfig.setUsername(config.getUsername());
        hikariConfig.setPassword(config.getPassword());
        hikariConfig.setDriverClassName(config.getDriverClassName());

        hikariConfig.setMinimumIdle(config.getMinimumIdle());
        hikariConfig.setMaximumPoolSize(config.getMaximumPoolSize());
        hikariConfig.setIdleTimeout(config.getIdleTimeout());
        hikariConfig.setPoolName(config.getPoolName());
        hikariConfig.setMaxLifetime(config.getMaxLifetime());
        hikariConfig.setConnectionTimeout(config.getConnectionTimeout());
        hikariConfig.setInitializationFailTimeout(config.getInitializationFailTimeout());

        return new HikariDataSource(hikariConfig);
    }

    @Bean
    @Qualifier("restEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean restEntityManagerFactory(RESTConfiguration configuration) {

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setGenerateDdl(true);

        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setJpaVendorAdapter(vendorAdapter);
        factory.setPackagesToScan("com.atopion.UGC_repository.rest");
        factory.setDataSource(restDataSource(configuration));
        return factory;
    }

    @Bean
    public PlatformTransactionManager restTransactionManager(
            @Qualifier("restEntityManagerFactory") EntityManagerFactory restEntityManagerFactory) {

        JpaTransactionManager txManager = new JpaTransactionManager();
        txManager.setEntityManagerFactory(restEntityManagerFactory);
        return txManager;
    }


    @Configuration
    @ConfigurationProperties(prefix = "app.datasource.rest")
    @Qualifier("REST")
    public static class RESTConfiguration {

        private String url;
        private String username;
        private String password;
        private String driverClassName;

        private int minimumIdle;
        private int maximumPoolSize;
        private int idleTimeout;
        private String poolName;
        private int maxLifetime;
        private int connectionTimeout;
        private int initializationFailTimeout;


        @Override
        public String toString() {
            return "RESTConfiguration{" +
                    "url='" + url + '\'' +
                    ", username='" + username + '\'' +
                    ", password='" + password + '\'' +
                    ", driverClassName='" + driverClassName + '\'' +
                    ", minimumIdle=" + minimumIdle +
                    ", maximumPoolSize=" + maximumPoolSize +
                    ", idleTimeout=" + idleTimeout +
                    ", poolName='" + poolName + '\'' +
                    ", maxLifetime=" + maxLifetime +
                    ", connectionTimeout=" + connectionTimeout +
                    ", initializationFailTimeout=" + initializationFailTimeout +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof RESTConfiguration)) return false;
            RESTConfiguration that = (RESTConfiguration) o;
            return minimumIdle == that.minimumIdle &&
                    maximumPoolSize == that.maximumPoolSize &&
                    idleTimeout == that.idleTimeout &&
                    maxLifetime == that.maxLifetime &&
                    connectionTimeout == that.connectionTimeout &&
                    initializationFailTimeout == that.initializationFailTimeout &&
                    Objects.equals(url, that.url) &&
                    Objects.equals(username, that.username) &&
                    Objects.equals(password, that.password) &&
                    Objects.equals(driverClassName, that.driverClassName) &&
                    Objects.equals(poolName, that.poolName);
        }

        @Override
        public int hashCode() {
            return Objects.hash(url, username, password, driverClassName, minimumIdle, maximumPoolSize, idleTimeout, poolName, maxLifetime, connectionTimeout, initializationFailTimeout);
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

        public int getMinimumIdle() {
            return minimumIdle;
        }

        public void setMinimumIdle(int minimumIdle) {
            this.minimumIdle = minimumIdle;
        }

        public int getIdleTimeout() {
            return idleTimeout;
        }

        public void setIdleTimeout(int idleTimeout) {
            this.idleTimeout = idleTimeout;
        }

        public String getPoolName() {
            return poolName;
        }

        public void setPoolName(String poolName) {
            this.poolName = poolName;
        }

        public int getMaxLifetime() {
            return maxLifetime;
        }

        public void setMaxLifetime(int maxLifetime) {
            this.maxLifetime = maxLifetime;
        }

        public int getConnectionTimeout() {
            return connectionTimeout;
        }

        public void setConnectionTimeout(int connectionTimeout) {
            this.connectionTimeout = connectionTimeout;
        }

        public int getInitializationFailTimeout() {
            return initializationFailTimeout;
        }

        public void setInitializationFailTimeout(int initializationFailTimeout) {
            this.initializationFailTimeout = initializationFailTimeout;
        }
    }
}
