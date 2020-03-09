package com.atopion.UGC_repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DataSourceConfigurationTest {

    @Value("${app.sql.datasource.url}")
    private String valueSQLUrl;

    @Value("${app.sql.datasource.username}")
    private String valueSQLUsername;

    @Value("${app.sql.datasource.password}")
    private String valueSQLPassword;

    @Value("${app.sql.datasource.driver-class-name}")
    private String valueSQLDriverClassName;

    @Value("${app.sql.datasource.maximum-pool-size}")
    private int valueSQLMaximumPoolSize;


    @Autowired
    private DataSource sqlDataSource;

    @Autowired
    @Qualifier("SQL")
    private DataSourceConfiguration.SQLConfiguration sqlConfiguration;


    @Value("${app.rest.datasource.url}")
    private String valueRESTUrl;

    @Value("${app.rest.datasource.username}")
    private String valueRESTUsername;

    @Value("${app.rest.datasource.password}")
    private String valueRESTPassword;

    @Value("${app.rest.datasource.driver-class-name}")
    private String valueRESTDriverClassName;

    @Value("${app.rest.datasource.maximum-pool-size}")
    private int valueRESTMaximumPoolSize;


    @Autowired
    private DataSource restDataSource;

    @Autowired
    @Qualifier("REST")
    private DataSourceConfiguration.RESTConfiguration restConfiguration;


    @Test
    void sqlConfigurationTest() {

        assertEquals(valueSQLUrl, sqlConfiguration.getUrl());
        assertEquals(valueSQLUsername, sqlConfiguration.getUsername());
        assertEquals(valueSQLPassword, sqlConfiguration.getPassword());
        assertEquals(valueSQLDriverClassName, sqlConfiguration.getDriverClassName());
        assertEquals(valueSQLMaximumPoolSize, sqlConfiguration.getMaximumPoolSize());

        DataSourceConfiguration.SQLConfiguration testConfig = new DataSourceConfiguration.SQLConfiguration();
        testConfig.setUrl(valueSQLUrl);
        testConfig.setUsername(valueSQLUsername);
        testConfig.setPassword(valueSQLPassword);
        testConfig.setDriverClassName(valueSQLDriverClassName);
        testConfig.setMaximumPoolSize(valueSQLMaximumPoolSize);

        assertEquals(testConfig.getUrl(), sqlConfiguration.getUrl());
        assertEquals(testConfig.getUsername(), sqlConfiguration.getUsername());
        assertEquals(testConfig.getPassword(), sqlConfiguration.getPassword());
        assertEquals(testConfig.getDriverClassName(), sqlConfiguration.getDriverClassName());
        assertEquals(testConfig.getMaximumPoolSize(), sqlConfiguration.getMaximumPoolSize());

        assertEquals(testConfig.hashCode(), sqlConfiguration.hashCode());
        assertEquals(testConfig.toString(), sqlConfiguration.toString());
        assertEquals(testConfig, sqlConfiguration);
    }

    @Test
    void restConfigurationTest() {

        assertEquals(valueRESTUrl, restConfiguration.getUrl());
        assertEquals(valueRESTUsername, restConfiguration.getUsername());
        assertEquals(valueRESTPassword, restConfiguration.getPassword());
        assertEquals(valueRESTDriverClassName, restConfiguration.getDriverClassName());
        assertEquals(valueRESTMaximumPoolSize, restConfiguration.getMaximumPoolSize());

        DataSourceConfiguration.RESTConfiguration testConfig = new DataSourceConfiguration.RESTConfiguration();
        testConfig.setUrl(valueRESTUrl);
        testConfig.setUsername(valueRESTUsername);
        testConfig.setPassword(valueRESTPassword);
        testConfig.setDriverClassName(valueRESTDriverClassName);
        testConfig.setMaximumPoolSize(valueRESTMaximumPoolSize);

        assertEquals(testConfig.getUrl(), restConfiguration.getUrl());
        assertEquals(testConfig.getUsername(), restConfiguration.getUsername());
        assertEquals(testConfig.getPassword(), restConfiguration.getPassword());
        assertEquals(testConfig.getDriverClassName(), restConfiguration.getDriverClassName());
        assertEquals(testConfig.getMaximumPoolSize(), restConfiguration.getMaximumPoolSize());

        assertEquals(testConfig.hashCode(), restConfiguration.hashCode());
        assertEquals(testConfig.toString(), restConfiguration.toString());
        assertEquals(testConfig, restConfiguration);
    }


    @Test
    void restDataSource() {

        try {
            Connection con = restDataSource.getConnection();

            // Only URL, Username and Password stay the same in the connection. The rest is up to the implementation.
            // The username is mapped to the IP in this way: user -> user@ip
            assertEquals(valueRESTUrl, con.getMetaData().getURL());
            assertTrue(con.getMetaData().getUserName().startsWith(valueRESTUsername + "@"),
                    "The given username was not found in the connection metadata. Expected to find: <" + valueRESTUsername + "> in <" + con.getMetaData().getUserName());

        } catch (SQLException ex) {
            fail("FAIL: SQL Exception thrown: ", ex);
        }
    }

    @Test
    void sqlDataSource() {

        try {
            Connection con = sqlDataSource.getConnection();

            // Only URL, Username and Password stay the same in the connection. The rest is up to the implementation.
            // The username is mapped to the IP in this way: user -> user@ip
            assertEquals(valueSQLUrl, con.getMetaData().getURL());
            assertTrue(con.getMetaData().getUserName().startsWith(valueSQLUsername + "@"),
                    "The given username was not found in the connection metadata. Expected to find: <" + valueSQLUsername + "> in <" + con.getMetaData().getUserName());

        } catch (SQLException ex) {
            fail("FAIL: SQL Exception thrown: ", ex);
        }
    }
}