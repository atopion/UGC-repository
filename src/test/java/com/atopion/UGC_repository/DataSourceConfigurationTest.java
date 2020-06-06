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

    @Value("${app.datasource.sql.url}")
    private String valueSQLUrl;

    @Value("${app.datasource.sql.username}")
    private String valueSQLUsername;

    @Value("${app.datasource.sql.password}")
    private String valueSQLPassword;

    @Value("${app.datasource.sql.minimum-idle}")
    private int valueSQLMinimumIdle;

    @Value("${app.datasource.sql.maximum-pool-size}")
    private int valueSQLMaximumPoolSize;

    @Value("${app.datasource.sql.idle-timeout}")
    private int valueSQLIdleTimeout;

    @Value("${app.datasource.sql.pool-name}")
    private String valueSQLPoolName;

    @Value("${app.datasource.sql.max-lifetime}")
    private int valueSQLMaxLifetime;

    @Value("${app.datasource.sql.connection-timeout}")
    private int valueSQLConnectionTimeout;

    @Value("${app.datasource.sql.initialization-timeout}")
    private int valueSQLInitializationTimeout;

    @Value("${app.datasource.sql.driver-class-name}")
    private String valueSQLDriverClassName;




    @Autowired
    private DataSource sqlDataSource;

    @Autowired
    @Qualifier("SQL")
    private SQLDataSourceConfig.SQLConfiguration sqlConfiguration;


    @Value("${app.datasource.rest.url}")
    private String valueRESTUrl;

    @Value("${app.datasource.rest.username}")
    private String valueRESTUsername;

    @Value("${app.datasource.rest.password}")
    private String valueRESTPassword;

    @Value("${app.datasource.rest.minimum-idle}")
    private int valueRESTMinimumIdle;

    @Value("${app.datasource.rest.maximum-pool-size}")
    private int valueRESTMaximumPoolSize;

    @Value("${app.datasource.rest.idle-timeout}")
    private int valueRESTIdleTimeout;

    @Value("${app.datasource.rest.pool-name}")
    private String valueRESTPoolName;

    @Value("${app.datasource.rest.max-lifetime}")
    private int valueRESTMaxLifetime;

    @Value("${app.datasource.rest.connection-timeout}")
    private int valueRESTConnectionTimeout;

    @Value("${app.datasource.rest.initialization-timeout}")
    private int valueRESTInitializationTimeout;

    @Value("${app.datasource.rest.driver-class-name}")
    private String valueRESTDriverClassName;


    @Autowired
    private DataSource restDataSource;

    @Autowired
    @Qualifier("REST")
    private RestDataSourceConfig.RESTConfiguration restConfiguration;



    @Value("${app.datasource.user.url}")
    private String valueUSERUrl;

    @Value("${app.datasource.user.username}")
    private String valueUSERUsername;

    @Value("${app.datasource.user.password}")
    private String valueUSERPassword;

    @Value("${app.datasource.user.minimum-idle}")
    private int valueUSERMinimumIdle;

    @Value("${app.datasource.user.maximum-pool-size}")
    private int valueUSERMaximumPoolSize;

    @Value("${app.datasource.user.idle-timeout}")
    private int valueUSERIdleTimeout;

    @Value("${app.datasource.user.pool-name}")
    private String valueUSERPoolName;

    @Value("${app.datasource.user.max-lifetime}")
    private int valueUSERMaxLifetime;

    @Value("${app.datasource.user.connection-timeout}")
    private int valueUSERConnectionTimeout;

    @Value("${app.datasource.user.initialization-timeout}")
    private int valueUSERInitializationTimeout;

    @Value("${app.datasource.user.driver-class-name}")
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
        assertEquals(valueSQLMinimumIdle, sqlConfiguration.getMinimumIdle());
        assertEquals(valueSQLMaximumPoolSize, sqlConfiguration.getMaximumPoolSize());
        assertEquals(valueSQLIdleTimeout, sqlConfiguration.getIdleTimeout());
        assertEquals(valueSQLPoolName, sqlConfiguration.getPoolName());
        assertEquals(valueSQLMaxLifetime, sqlConfiguration.getMaxLifetime());
        assertEquals(valueSQLConnectionTimeout, sqlConfiguration.getConnectionTimeout());
        //assertEquals(valueSQLInitializationTimeout, sqlConfiguration.getInitializationFailTimeout());
        assertEquals(valueSQLDriverClassName, sqlConfiguration.getDriverClassName());

        SQLDataSourceConfig.SQLConfiguration testConfig = new SQLDataSourceConfig.SQLConfiguration();
        testConfig.setUrl(valueSQLUrl);
        testConfig.setUsername(valueSQLUsername);
        testConfig.setPassword(valueSQLPassword);
        testConfig.setMinimumIdle(valueSQLMinimumIdle);
        testConfig.setMaximumPoolSize(valueSQLMaximumPoolSize);
        testConfig.setIdleTimeout(valueSQLIdleTimeout);
        testConfig.setPoolName(valueSQLPoolName);
        testConfig.setMaxLifetime(valueSQLMaxLifetime);
        testConfig.setConnectionTimeout(valueSQLConnectionTimeout);
        testConfig.setInitializationFailTimeout(valueSQLInitializationTimeout);
        testConfig.setDriverClassName(valueSQLDriverClassName);

        assertEquals(testConfig.getUrl(), sqlConfiguration.getUrl());
        assertEquals(testConfig.getUsername(), sqlConfiguration.getUsername());
        assertEquals(testConfig.getPassword(), sqlConfiguration.getPassword());
        assertEquals(testConfig.getMaximumPoolSize(), sqlConfiguration.getMaximumPoolSize());
        assertEquals(testConfig.getIdleTimeout(), sqlConfiguration.getIdleTimeout());
        assertEquals(testConfig.getPoolName(), sqlConfiguration.getPoolName());
        assertEquals(testConfig.getMaxLifetime(), sqlConfiguration.getMaxLifetime());
        assertEquals(testConfig.getConnectionTimeout(), sqlConfiguration.getConnectionTimeout());
        //assertEquals(testConfig.getInitializationFailTimeout(), sqlConfiguration.getInitializationFailTimeout());
        assertEquals(testConfig.getDriverClassName(), sqlConfiguration.getDriverClassName());

        //assertEquals(testConfig.hashCode(), sqlConfiguration.hashCode());
        //assertEquals(testConfig.toString(), sqlConfiguration.toString());
        //assertEquals(testConfig, sqlConfiguration);
    }

    @Test
    void restConfigurationTest() {

        assertEquals(valueRESTUrl, restConfiguration.getUrl());
        assertEquals(valueRESTUsername, restConfiguration.getUsername());
        assertEquals(valueRESTPassword, restConfiguration.getPassword());
        assertEquals(valueRESTMinimumIdle, restConfiguration.getMinimumIdle());
        assertEquals(valueRESTMaximumPoolSize, restConfiguration.getMaximumPoolSize());
        assertEquals(valueRESTIdleTimeout, restConfiguration.getIdleTimeout());
        assertEquals(valueRESTPoolName, restConfiguration.getPoolName());
        assertEquals(valueRESTMaxLifetime, restConfiguration.getMaxLifetime());
        assertEquals(valueRESTConnectionTimeout, restConfiguration.getConnectionTimeout());
        //assertEquals(valueRESTInitializationTimeout, restConfiguration.getInitializationFailTimeout());
        assertEquals(valueRESTDriverClassName, restConfiguration.getDriverClassName());

        RestDataSourceConfig.RESTConfiguration testConfig = new RestDataSourceConfig.RESTConfiguration();
        testConfig.setUrl(valueRESTUrl);
        testConfig.setUsername(valueRESTUsername);
        testConfig.setPassword(valueRESTPassword);
        testConfig.setMinimumIdle(valueRESTMinimumIdle);
        testConfig.setMaximumPoolSize(valueRESTMaximumPoolSize);
        testConfig.setIdleTimeout(valueRESTIdleTimeout);
        testConfig.setPoolName(valueRESTPoolName);
        testConfig.setMaxLifetime(valueRESTMaxLifetime);
        testConfig.setConnectionTimeout(valueRESTConnectionTimeout);
        testConfig.setInitializationFailTimeout(valueRESTInitializationTimeout);
        testConfig.setDriverClassName(valueRESTDriverClassName);

        assertEquals(testConfig.getUrl(), restConfiguration.getUrl());
        assertEquals(testConfig.getUsername(), restConfiguration.getUsername());
        assertEquals(testConfig.getPassword(), restConfiguration.getPassword());
        assertEquals(testConfig.getMaximumPoolSize(), restConfiguration.getMaximumPoolSize());
        assertEquals(testConfig.getIdleTimeout(), restConfiguration.getIdleTimeout());
        assertEquals(testConfig.getPoolName(), restConfiguration.getPoolName());
        assertEquals(testConfig.getMaxLifetime(), restConfiguration.getMaxLifetime());
        assertEquals(testConfig.getConnectionTimeout(), restConfiguration.getConnectionTimeout());
        //assertEquals(testConfig.getInitializationFailTimeout(), restConfiguration.getInitializationFailTimeout());
        assertEquals(testConfig.getDriverClassName(), restConfiguration.getDriverClassName());

        //assertEquals(testConfig.hashCode(), restConfiguration.hashCode());
        //assertEquals(testConfig.toString(), restConfiguration.toString());
        //assertEquals(testConfig, restConfiguration);
    }


    @Test
    void userConfigurationTest() {

        assertEquals(valueUSERUrl, userConfiguration.getUrl());
        assertEquals(valueUSERUsername, userConfiguration.getUsername());
        assertEquals(valueUSERPassword, userConfiguration.getPassword());
        assertEquals(valueUSERMinimumIdle, userConfiguration.getMinimumIdle());
        assertEquals(valueUSERMaximumPoolSize, userConfiguration.getMaximumPoolSize());
        assertEquals(valueUSERIdleTimeout, userConfiguration.getIdleTimeout());
        assertEquals(valueUSERPoolName, userConfiguration.getPoolName());
        assertEquals(valueUSERMaxLifetime, userConfiguration.getMaxLifetime());
        assertEquals(valueUSERConnectionTimeout, userConfiguration.getConnectionTimeout());
        //assertEquals(valueUSERInitializationTimeout, userConfiguration.getInitializationFailTimeout());
        assertEquals(valueUSERDriverClassName, userConfiguration.getDriverClassName());

        UserDataSourceConfig.USERConfiguration testConfig = new UserDataSourceConfig.USERConfiguration();
        testConfig.setUrl(valueUSERUrl);
        testConfig.setUsername(valueUSERUsername);
        testConfig.setPassword(valueUSERPassword);
        testConfig.setMinimumIdle(valueUSERMinimumIdle);
        testConfig.setMaximumPoolSize(valueUSERMaximumPoolSize);
        testConfig.setIdleTimeout(valueUSERIdleTimeout);
        testConfig.setPoolName(valueUSERPoolName);
        testConfig.setMaxLifetime(valueUSERMaxLifetime);
        testConfig.setConnectionTimeout(valueUSERConnectionTimeout);
        testConfig.setInitializationFailTimeout(valueUSERInitializationTimeout);
        testConfig.setDriverClassName(valueUSERDriverClassName);

        assertEquals(testConfig.getUrl(), userConfiguration.getUrl());
        assertEquals(testConfig.getUsername(), userConfiguration.getUsername());
        assertEquals(testConfig.getPassword(), userConfiguration.getPassword());
        assertEquals(testConfig.getMaximumPoolSize(), userConfiguration.getMaximumPoolSize());
        assertEquals(testConfig.getIdleTimeout(), userConfiguration.getIdleTimeout());
        assertEquals(testConfig.getPoolName(), userConfiguration.getPoolName());
        assertEquals(testConfig.getMaxLifetime(), userConfiguration.getMaxLifetime());
        assertEquals(testConfig.getConnectionTimeout(), userConfiguration.getConnectionTimeout());
        //assertEquals(testConfig.getInitializationFailTimeout(), userConfiguration.getInitializationFailTimeout());
        assertEquals(testConfig.getDriverClassName(), userConfiguration.getDriverClassName());

        //assertEquals(testConfig.hashCode(), userConfiguration.hashCode());
        //assertEquals(testConfig.toString(), userConfiguration.toString());
        //assertEquals(testConfig, userConfiguration);
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
    */
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
    }
}