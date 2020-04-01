package com.atopion.UGC_repository.sql;

import com.atopion.UGC_repository.entities.ApplicationsEntity;
import com.atopion.UGC_repository.entities.ApplicationsRepository;
import com.atopion.UGC_repository.entities.UsersEntity;
import com.atopion.UGC_repository.entities.UsersRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.SingletonBeanRegistry;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;

import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("classpath:application-integrationtests.properties")
class SQLControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private ApplicationsRepository appRepository;

    @Autowired
    private DataSource restDataSource;

    @BeforeEach
    void setUp() {
        usersRepository.save(new UsersEntity("token_1", "name_1", "email_1"));
        usersRepository.save(new UsersEntity("token_2", "name_2", "email_2"));

        appRepository.save(new ApplicationsEntity("name_1"));
        appRepository.save(new ApplicationsEntity("name_2"));
    }

    @AfterEach
    void tearDown() {
        usersRepository.deleteAll();
        usersRepository.flush();
        appRepository.deleteAll();
        appRepository.flush();

        try (Connection connection = restDataSource.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate("ALTER TABLE users ALTER COLUMN user_id RESTART WITH 1;");

        } catch (Exception e) {
            fail("Unexpected exception occured: " + e.getMessage());
        }


        try (Connection connection = restDataSource.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate("ALTER TABLE APPLICATIONS ALTER COLUMN APPLICATION_ID RESTART WITH 1;");

        } catch (Exception e) {
            fail("Unexpected exception occured: " + e.getMessage());
        }
    }

    @Test
    void sqlRequestHTML_functionality_1() {
        try {
            MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/sql?query=SELECT * FROM applications;&format=html"))
                    .andReturn();

            assertEquals(200, result.getResponse().getStatus());

            String content = result.getResponse().getContentAsString().toLowerCase();
            assertTrue(content.startsWith("<!doctype html>"), "Received content does not start with <!DOCTYPE html>");
            assertTrue(content.endsWith("</html>"), "Received content does not end with </html>");
            assertTrue(content.contains("<title>result for query</title>"), "Received content does not contain the correct title.");
            assertTrue(content.contains("<table"), "Received content does not contain a table.");
            assertTrue(content.contains("</table>"), "Received content does not contain a table.");
            assertTrue(content.contains("<th>application_id</th>"), "Received content does not contain the header 'application_id'.");
            assertTrue(content.contains("<th>application_name</th>"), "Received content does not contain the header 'application_name'.");

            assertTrue(content.contains("<td>"), "Received content does not contain a single row.");
            assertTrue(content.contains("</td>"), "Received content does not contain a single row.");
        } catch (Exception ex) {
            fail("Unexpected Exception occurred: " + ex.getMessage());
        }
    }

    @Test
    void sqlRequestHTML_functionality_2() {
        try {
            MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/sql?query=SELECT * FROM users;&format=html"))
                    .andReturn();

            assertEquals(200, result.getResponse().getStatus());

            String content = result.getResponse().getContentAsString().toLowerCase();
            assertTrue(content.startsWith("<!doctype html>"), "Received content does not start with <!DOCTYPE html>");
            assertTrue(content.endsWith("</html>"), "Received content does not end with </html>");
            assertTrue(content.contains("<title>result for query</title>"), "Received content does not contain the correct title.");
            assertTrue(content.contains("<table"), "Received content does not contain a table.");
            assertTrue(content.contains("</table>"), "Received content does not contain a table.");
            assertTrue(content.contains("<th>user_token</th>"), "Received content does not contain the header 'user_token'.");
            assertTrue(content.contains("<th>user_name</th>"), "Received content does not contain the header 'user_name'.");
            assertTrue(content.contains("<th>user_email</th>"), "Received content does not contain the header 'user_email'.");

            assertTrue(content.contains("<td>"), "Received content does not contain a single row.");
            assertTrue(content.contains("</td>"), "Received content does not contain a single row.");
        } catch (Exception ex) {
            fail("Unexpected Exception occurred: " + ex.getMessage());
        }
    }

    @Test
    void sqlRequestHTML_robustness_1() {
        try {
            mvc.perform(MockMvcRequestBuilders.get("/sql?format=html"))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest());
        } catch (Exception ex) {
            fail("Unexpected Exception occurred: " + ex.getMessage());
        }
    }

    @Test
    void sqlRequestHTML_robustness_2() {
        try {
            mvc.perform(MockMvcRequestBuilders.get("/sql?query=DELETE FROM applications&format=html"))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest());
        } catch (Exception ex) {
            fail("Unexpected Exception occurred: " + ex.getMessage());
        }
    }

    @Test
    void sqlRequestHTML_robustness_3() {
        try {
            mvc.perform(MockMvcRequestBuilders.get("/sql?query=SELECT * INTO demo FROM applications;&format=html"))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest());
        } catch (Exception ex) {
            fail("Unexpected Exception occurred: " + ex.getMessage());
        }
    }

    @Test
    void sqlRequestHTML_robustness_4() {
        try {
            mvc.perform(MockMvcRequestBuilders.get("/sql?query=SELECT * FRAM applications;&format=html"))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest());
        } catch (Exception ex) {
            fail("Unexpected Exception occurred: " + ex.getMessage());
        }
    }

    @Test
    void sqlRequestJSON_functionality_1() {
        try {
            MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/sql?query=SELECT * FROM applications;&format=json"))
                    .andReturn();

            assertEquals(200, result.getResponse().getStatus());

            String content = result.getResponse().getContentAsString().toLowerCase();

            assertTrue(content.startsWith("["), "Received content did not start with '['");
            assertTrue(content.endsWith("]"), "Received content did not end with ']'");
            assertNotEquals("[]", content, "Received content is empty");
            assertTrue(content.contains("application_id"), "Received content did not contain 'application_id'");
            assertTrue(content.contains("application_name"), "Received content did not contain 'application_name'");

        } catch (Exception ex) {
            fail("Unexpected Exception occurred: " + ex.getMessage());
        }
    }

    @Test
    void sqlRequestJSON_functionality_2() {
        try {
            MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/sql?query=SELECT * FROM users;&format=json"))
                    .andReturn();

            assertEquals(200, result.getResponse().getStatus());

            String content = result.getResponse().getContentAsString().toLowerCase();

            assertTrue(content.startsWith("["), "Received content did not start with '['");
            assertTrue(content.endsWith("]"), "Received content did not end with ']'");
            assertNotEquals("[]", content, "Received content is empty");
            assertTrue(content.contains("user_token"), "Received content did not contain 'user_token'");
            assertTrue(content.contains("user_name"), "Received content did not contain 'user_name'");
            assertTrue(content.contains("user_email"), "Received content did not contain 'user_email'");

        } catch (Exception ex) {
            fail("Unexpected Exception occurred: " + ex.getMessage());
        }
    }

    @Test
    void sqlRequestJSON_robustness_1() {
        try {
            mvc.perform(MockMvcRequestBuilders.get("/sql?format=json"))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest());
        } catch (Exception ex) {
            fail("Unexpected Exception occurred: " + ex.getMessage());
        }
    }

    @Test
    void sqlRequestJSON_robustness_2() {
        try {
            mvc.perform(MockMvcRequestBuilders.get("/sql?query=DELETE FROM applications&format=json"))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest());
        } catch (Exception ex) {
            fail("Unexpected Exception occurred: " + ex.getMessage());
        }
    }

    @Test
    void sqlRequestJSON_robustness_3() {
        try {
            mvc.perform(MockMvcRequestBuilders.get("/sql?query=SELECT * INTO demo FROM applications;&format=json"))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest());
        } catch (Exception ex) {
            fail("Unexpected Exception occurred: " + ex.getMessage());
        }
    }

    @Test
    void sqlRequestXML_functionality_1() {
        try {
            MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/sql?query=SELECT * FROM applications;&format=xml"))
                    .andReturn();

            assertEquals(200, result.getResponse().getStatus());

            String content = result.getResponse().getContentAsString().toLowerCase();
            content = content.replace("\t", "").replace("\n", "").trim();

            assertTrue(content.startsWith("<response>"), "Received content did not start with '<response>'");
            assertTrue(content.endsWith("</response>"), "Received content did not end with '</response>'");
            assertNotEquals("<response></response>", content, "Received content is empty");
            assertTrue(content.contains("<application_id>"), "Received content did not contain 'application_id'");
            assertTrue(content.contains("<application_name>"), "Received content did not contain 'application_name'");

        } catch (Exception ex) {
            fail("Unexpected Exception occurred: " + ex.getMessage());
        }
    }

    @Test
    void sqlRequestXML_functionality_2() {
        try {
            MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/sql?query=SELECT * FROM users;&format=xml"))
                    .andReturn();

            assertEquals(200, result.getResponse().getStatus());

            String content = result.getResponse().getContentAsString().toLowerCase();
            content = content.replace("\t", "").replace("\n", "").trim();

            assertTrue(content.startsWith("<response>"), "Received content did not start with '<response>'");
            assertTrue(content.endsWith("</response>"), "Received content did not end with '</response>'");
            assertNotEquals("<response></response>", content, "Received content is empty");
            assertTrue(content.contains("<user_token>"), "Received content did not contain 'user_token'");
            assertTrue(content.contains("<user_name>"), "Received content did not contain 'user_name'");
            assertTrue(content.contains("<user_email>"), "Received content did not contain 'user_email'");

        } catch (Exception ex) {
            fail("Unexpected Exception occurred: " + ex.getMessage());
        }
    }

    @Test
    void sqlRequestXML_robustness_1() {
        try {
            mvc.perform(MockMvcRequestBuilders.get("/sql?format=xml"))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest());
        } catch (Exception ex) {
            fail("Unexpected Exception occurred: " + ex.getMessage());
        }
    }

    @Test
    void sqlRequestXML_robustness_2() {
        try {
            mvc.perform(MockMvcRequestBuilders.get("/sql?query=DELETE FROM applications&format=xml"))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest());
        } catch (Exception ex) {
            fail("Unexpected Exception occurred: " + ex.getMessage());
        }
    }

    @Test
    void sqlRequestXML_robustness_3() {
        try {
            mvc.perform(MockMvcRequestBuilders.get("/sql?query=SELECT * INTO demo FROM applications;&format=xml"))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest());
        } catch (Exception ex) {
            fail("Unexpected Exception occurred: " + ex.getMessage());
        }
    }
}