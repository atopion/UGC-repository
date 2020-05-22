package com.atopion.UGC_repository;

import com.atopion.UGC_repository.rest.RestDataSourceConfig;
import com.atopion.UGC_repository.sql.SQLDataSourceConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource("classpath:application-integrationtests.properties")
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
    private SQLDataSourceConfig.SQLConfiguration sqlConfiguration;


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
    private RestDataSourceConfig.RESTConfiguration restConfiguration;



    @Value("${app.user.datasource.url}")
    private String valueUSERUrl;

    @Value("${app.user.datasource.username}")
    private String valueUSERUsername;

    @Value("${app.user.datasource.password}")
    private String valueUSERPassword;

    @Value("${app.user.datasource.driver-class-name}")
    private String valueUSERDriverClassName;

    @Autowired
    @Qualifier("USERDB")
    private DataSource userDataSource;

    @Autowired
    @Qualifier("USER")
    private UserDataSourceConfig.USERConfiguration userConfiguration;


    @Test
    void sqlConfigurationTest() {

        assertEquals(valueSQLUrl, sqlConfiguration.getUrl());
        assertEquals(valueSQLUsername, sqlConfiguration.getUsername());
        assertEquals(valueSQLPassword, sqlConfiguration.getPassword());
        assertEquals(valueSQLDriverClassName, sqlConfiguration.getDriverClassName());
        assertEquals(valueSQLMaximumPoolSize, sqlConfiguration.getMaximumPoolSize());

        SQLDataSourceConfig.SQLConfiguration testConfig = new SQLDataSourceConfig.SQLConfiguration();
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

        RestDataSourceConfig.RESTConfiguration testConfig = new RestDataSourceConfig.RESTConfiguration();
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
    void userConfigurationTest() {

        assertEquals(valueUSERUrl, userConfiguration.getUrl());
        assertEquals(valueUSERUsername, userConfiguration.getUsername());
        assertEquals(valueUSERPassword, userConfiguration.getPassword());
        assertEquals(valueUSERDriverClassName, userConfiguration.getDriverClassName());

        UserDataSourceConfig.USERConfiguration testConfig = new UserDataSourceConfig.USERConfiguration();
        testConfig.setUrl(valueUSERUrl);
        testConfig.setUsername(valueUSERUsername);
        testConfig.setPassword(valueUSERPassword);
        testConfig.setDriverClassName(valueUSERDriverClassName);

        assertEquals(testConfig.getUrl(), userConfiguration.getUrl());
        assertEquals(testConfig.getUsername(), userConfiguration.getUsername());
        assertEquals(testConfig.getPassword(), userConfiguration.getPassword());
        assertEquals(testConfig.getDriverClassName(), userConfiguration.getDriverClassName());

        assertEquals(testConfig.hashCode(), userConfiguration.hashCode());
        assertEquals(testConfig.toString(), userConfiguration.toString());
        assertEquals(testConfig, userConfiguration);
    }


    @Test
    void restDataSource() {

        try {
            Connection con = restDataSource.getConnection();

            // Only URL, Username and Password stay the same in the connection. The rest is up to the implementation.
            // The username is mapped to the IP in this way: user -> user@ip
            assertEquals(valueRESTUrl, con.getMetaData().getURL());
            assertTrue(con.getMetaData().getUserName().toLowerCase().startsWith(valueRESTUsername.toLowerCase()),
                    "The given username was not found in the connection metadata. Expected to find: <" + valueRESTUsername.toLowerCase() + "> in <" + con.getMetaData().getUserName().toLowerCase() + ">");

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
            assertTrue(con.getMetaData().getUserName().toLowerCase().startsWith(valueSQLUsername.toLowerCase()),
                    "The given username was not found in the connection metadata. Expected to find: <" + valueSQLUsername.toLowerCase() + "> in <" + con.getMetaData().getUserName().toLowerCase() + ">");

        } catch (SQLException ex) {
            fail("FAIL: SQL Exception thrown: ", ex);
        }
    }


    /*
    NOT USED - needs an active database at the time of building images.

    @Test
    void userDataSource() {

        try {
            Connection con = userDataSource.getConnection();

            System.out.println("USER: " + con.getMetaData().getUserName());
            System.out.println("URL: " + con.getMetaData().getURL());

            // Only URL, Username and Password stay the same in the connection. The rest is up to the implementation.
            // The username is mapped to the IP in this way: user -> user@ip
            assertEquals(valueUSERUrl, con.getMetaData().getURL());
            assertTrue(con.getMetaData().getUserName().toLowerCase().startsWith(valueUSERUsername.toLowerCase()),
                    "The given username was not found in the connection metadata. Expected to find: <" + valueUSERUsername.toLowerCase()  + "> in <" + con.getMetaData().getUserName().toLowerCase() + ">");

        } catch (SQLException ex) {
            fail("FAIL: SQL Exception thrown: ", ex);
        }
    }*/
}