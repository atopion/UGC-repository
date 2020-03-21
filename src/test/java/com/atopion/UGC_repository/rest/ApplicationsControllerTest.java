package com.atopion.UGC_repository.rest;

import com.atopion.UGC_repository.entities.ApplicationsEntity;
import com.atopion.UGC_repository.entities.ApplicationsRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.Statement;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("classpath:application-integrationtests.properties")
class ApplicationsControllerTest {

    private final String[] resultJSON = new String[] {"{\"application_id\":1,\"application_name\":\"demoApp1\"}", "{\"application_id\":2,\"application_name\":\"demoApp2\"}", "{\"application_id\":3,\"application_name\":\"demoApp3\"}"};

    private final String[] resultXML  = new String[] {"<application_id>1</application_id><application_name>demoApp1</application_name>", "<application_id>2</application_id><application_name>demoApp2</application_name>", "<application_id>3</application_id><application_name>demoApp3</application_name>"};

    private final String[] resultCSV  = new String[] {"application_id,application_name", "1,demoApp1", "2,demoApp2", "3,demoApp3"};

    private final String[] resultHTML = new String[] {"<tr>\n<th>application_id</th>\n<th>application_name</th>\n</tr>", "<tr>\n<td>1</td>\n<td>demoApp1</td>\n</tr>", "<tr>\n<td>2</td>\n<td>demoApp2</td>\n</tr>", "<tr>\n<td>3</td>\n<td>demoApp3</td>\n</tr>" };


    private final String demoApp4JSON_1 = "{\"application_name\": \"demoApp4\"}";
    private final String demoApp5JSON_2 = "{\"application_id\": 1,\"application_name\": \"demoApp5\"}";

    private final String demoApp4XML_1  = "<application><application_name>demoApp4</application_name></application>";
    private final String demoApp5XML_2 = "<application><application_id>2</application_id><application_name>demoApp5</application_name></application>";

    private final String demoApp4XFORM_1 = "application_name=demoApp4";
    private final String demoApp5XFORM_2 = "application_name=demoApp5&application_id=1";

    private final String demoApp4CSV_1 = "application_name\ndemoApp4";
    private final String demoApp5CSV_2 = "application_id,application_name\n1,demoApp5";

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ApplicationsRepository repository;

    @Autowired
    private DataSource dataSource;


    private ApplicationsEntity demoApp4, demoApp5;

    @BeforeEach
    void setUp() {
        ApplicationsEntity demoApp1 = new ApplicationsEntity("demoApp1");
        ApplicationsEntity demoApp2 = new ApplicationsEntity("demoApp2");
        ApplicationsEntity demoApp3 = new ApplicationsEntity("demoApp3");

        repository.save(demoApp1);
        repository.save(demoApp2);
        repository.save(demoApp3);
        repository.flush();

        demoApp4 = new ApplicationsEntity(4, "demoApp4");
        demoApp5 = new ApplicationsEntity(1, "demoApp5");

        System.out.println(repository.findAll());
    }

    @AfterEach
    void tearDown() {
        repository.deleteAll();
        repository.flush();

        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate("ALTER TABLE applications ALTER COLUMN application_id RESTART WITH 1;");

        } catch (Exception e) {
            fail("Unexpected exception occured: " + e.getMessage());
        }
    }

    @Test
    void getAll_functionality() {
        try {
            // Test general functionality
            MvcResult result1 = mvc.perform(get("/rest/applications").accept(MediaType.APPLICATION_JSON)).andReturn();

            assertEquals("Wrong status code. ", 200, result1.getResponse().getStatus());
            assertEquals("Wrong content type. ", MediaType.APPLICATION_JSON_VALUE, result1.getResponse().getContentType());

            for(String res : resultJSON)
                assertTrue("Could not find expected content.", result1.getResponse().getContentAsString().contains(res));


            MvcResult result2 = mvc.perform(get("/rest/applications").accept(MediaType.APPLICATION_XML)).andReturn();

            assertEquals("Wrong status code. ", 200, result2.getResponse().getStatus());
            assertEquals("Wrong content type. ", MediaType.APPLICATION_XML_VALUE, result2.getResponse().getContentType());

            for(String res : resultXML)
                assertTrue("Could not find expected content.", result2.getResponse().getContentAsString().replace(" ", "").contains(res));


            MvcResult result3 = mvc.perform(get("/rest/applications?format=json")).andReturn();

            assertEquals("Wrong status code. ", 200, result3.getResponse().getStatus());
            assertEquals("Wrong content type. ", MediaType.APPLICATION_JSON_VALUE, result3.getResponse().getContentType());

            for(String res : resultJSON)
                assertTrue("Could not find expected content.", result3.getResponse().getContentAsString().contains(res));


            MvcResult result4 = mvc.perform(get("/rest/applications?format=xml")).andReturn();

            assertEquals("Wrong status code. ", 200, result4.getResponse().getStatus());
            assertEquals("Wrong content type. ", MediaType.APPLICATION_XML_VALUE, result4.getResponse().getContentType());

            for(String res : resultXML)
                assertTrue("Could not find expected content.", result4.getResponse().getContentAsString().replace(" ", "").contains(res));


            // Test name parameter (JSON)
            MvcResult result5 = mvc.perform(get("/rest/applications?name=demoApp3").accept(MediaType.APPLICATION_JSON)).andReturn();

            assertEquals("Wrong status code. ", 200, result5.getResponse().getStatus());
            assertEquals("Wrong content type. ", MediaType.APPLICATION_JSON_VALUE, result5.getResponse().getContentType());

            assertTrue("Could not find expected content.", result5.getResponse().getContentAsString().contains(resultJSON[2]));

            assertFalse("Could find unexpected content.", result5.getResponse().getContentAsString().contains(resultJSON[0]));
            assertFalse("Could find unexpected content.", result5.getResponse().getContentAsString().contains(resultJSON[1]));


            MvcResult result6 = mvc.perform(get("/rest/applications?name=App").accept(MediaType.APPLICATION_JSON)).andReturn();

            assertEquals("Wrong status code. ", 200, result6.getResponse().getStatus());
            assertEquals("Wrong content type. ", MediaType.APPLICATION_JSON_VALUE, result6.getResponse().getContentType());

            for(String res : resultJSON)
                assertTrue("Could not find expected content.", result6.getResponse().getContentAsString().contains(res));


            // Test name parameter (XML)
            MvcResult result7 = mvc.perform(get("/rest/applications?name=demoApp3").accept(MediaType.APPLICATION_XML)).andReturn();

            assertEquals("Wrong status code. ", 200, result7.getResponse().getStatus());
            assertEquals("Wrong content type. ", MediaType.APPLICATION_XML_VALUE, result7.getResponse().getContentType());

            assertTrue("Could not find expected content.", result7.getResponse().getContentAsString().replace(" ", "").contains(resultXML[2]));

            assertFalse("Could find unexpected content.", result7.getResponse().getContentAsString().replace(" ", "").contains(resultXML[0]));
            assertFalse("Could find unexpected content.", result7.getResponse().getContentAsString().replace(" ", "").contains(resultXML[1]));


            MvcResult result8 = mvc.perform(get("/rest/applications?name=App").accept(MediaType.APPLICATION_XML)).andReturn();

            assertEquals("Wrong status code. ", 200, result8.getResponse().getStatus());
            assertEquals("Wrong content type. ", MediaType.APPLICATION_XML_VALUE, result8.getResponse().getContentType());

            for(String res : resultXML)
                assertTrue("Could not find expected content.", result8.getResponse().getContentAsString().replace(" ", "").contains(res));

        } catch (Exception e) {
            fail("Unexpected exception occurred: " + e.getMessage());
        }
    }

    @Test
    void getAll_robustness() {
        try {
            MvcResult result8 = mvc.perform(get("/rest/applications?name=UNKNOWN").accept(MediaType.APPLICATION_XML)).andReturn();

            assertEquals("Wrong status code. ", 200, result8.getResponse().getStatus());
            assertEquals("Wrong content type. ", MediaType.APPLICATION_XML_VALUE, result8.getResponse().getContentType());

            assertFalse("Could find unexpected content.", result8.getResponse().getContentAsString().replace(" ", "").contains(resultXML[2]));
            assertFalse("Could find unexpected content.", result8.getResponse().getContentAsString().replace(" ", "").contains(resultXML[0]));
            assertFalse("Could find unexpected content.", result8.getResponse().getContentAsString().replace(" ", "").contains(resultXML[1]));


            MvcResult result9 = mvc.perform(get("/rest/applications?name=&format=xml")).andReturn();

            assertEquals("Wrong status code. ", 200, result9.getResponse().getStatus());
            assertEquals("Wrong content type. ", MediaType.APPLICATION_XML_VALUE, result9.getResponse().getContentType());

            for(String res : resultXML)
                assertTrue("Could not find expected content.", result9.getResponse().getContentAsString().replace(" ", "").contains(res));


            MvcResult result = mvc.perform(get("/rest/applications?format=demo")).andReturn();
            assertEquals("Wrong status code. ", 200, result.getResponse().getStatus());
            assertEquals("Wrong content type. ", MediaType.APPLICATION_JSON_VALUE, result.getResponse().getContentType());


            MvcResult result1 = mvc.perform(get("/rest/applications").accept(MediaType.IMAGE_GIF)).andReturn();
            assertEquals("Wrong status code. ", 200, result1.getResponse().getStatus());
            assertEquals("Wrong content type. ", MediaType.APPLICATION_JSON_VALUE, result1.getResponse().getContentType());

        } catch (Exception e) {
            fail("Unexpected exception occurred: " + e.getMessage());
        }
    }

    @Test
    void getAllCSV_functionality() {
        try {
            // Test general functionality
            MvcResult result1 = mvc.perform(get("/rest/applications").accept(MediaType.valueOf("text/csv"))).andReturn();

            assertEquals("Wrong status code. ", 200, result1.getResponse().getStatus());
            assertEquals("Wrong content type. ", "text/csv", result1.getResponse().getContentType().substring(0,8));

            for(String res : resultCSV)
                assertTrue("Could not find expected content.", result1.getResponse().getContentAsString().contains(res));


            MvcResult result2 = mvc.perform(get("/rest/applications?format=csv")).andReturn();

            assertEquals("Wrong status code. ", 200, result2.getResponse().getStatus());
            assertEquals("Wrong content type. ", "text/csv", result2.getResponse().getContentType().substring(0,8));

            for(String res : resultCSV)
                assertTrue("Could not find expected content.", result2.getResponse().getContentAsString().contains(res));


            // Test name parameter
            MvcResult result3 = mvc.perform(get("/rest/applications?name=demoApp3").accept(MediaType.valueOf("text/csv"))).andReturn();

            assertEquals("Wrong status code. ", 200, result3.getResponse().getStatus());
            assertEquals("Wrong content type. ", "text/csv", result3.getResponse().getContentType().substring(0,8));

            assertTrue("Could not find expected content.", result3.getResponse().getContentAsString().contains(resultCSV[0]));
            assertTrue("Could not find expected content.", result3.getResponse().getContentAsString().contains(resultCSV[3]));

            assertFalse("Could find unexpected content.", result3.getResponse().getContentAsString().contains(resultCSV[1]));
            assertFalse("Could find unexpected content.", result3.getResponse().getContentAsString().contains(resultCSV[2]));


            MvcResult result4 = mvc.perform(get("/rest/applications?name=App").accept(MediaType.valueOf("text/csv"))).andReturn();

            assertEquals("Wrong status code. ", 200, result4.getResponse().getStatus());
            assertEquals("Wrong content type. ", "text/csv", result4.getResponse().getContentType().substring(0,8));

            for(String res : resultCSV)
                assertTrue("Could not find expected content.", result4.getResponse().getContentAsString().contains(res));

        } catch (Exception e) {
            fail("Unexpected exception occurred: " + e.getMessage());
        }
    }

    @Test
    void getAllCSV_robustness() {
        try {
            MvcResult result9 = mvc.perform(get("/rest/applications?name=&format=csv")).andReturn();

            assertEquals("Wrong status code. ", 200, result9.getResponse().getStatus());
            assertEquals("Wrong content type. ", "text/csv", result9.getResponse().getContentType().substring(0,8));

            for(String res : resultCSV)
                assertTrue("Could not find expected content.", result9.getResponse().getContentAsString().replace(" ", "").contains(res));


            repository.save(new ApplicationsEntity(4, "App\"Demo\",the first"));
            repository.flush();

            MvcResult result10 = mvc.perform(get("/rest/applications?name=&format=csv")).andReturn();

            assertEquals("Wrong status code. ", 200, result10.getResponse().getStatus());
            assertEquals("Wrong content type. ", "text/csv", result10.getResponse().getContentType().substring(0,8));

            for(String res : resultCSV)
                assertTrue("Could not find expected content.", result10.getResponse().getContentAsString().replace(" ", "").contains(res));

            assertTrue("Could not find expected content." , result10.getResponse().getContentAsString().replace(" ", "").contains("4,\"App\\\"Demo\\\",thefirst\""));

        } catch (Exception e) {
            fail("Unexpected exception occurred: " + e.getMessage());
        }
    }

    @Test
    void getAllHTML_functionality() {
        try {
            // Test general functionality
            MvcResult result1 = mvc.perform(get("/rest/applications").accept(MediaType.TEXT_HTML)).andReturn();

            assertEquals("Wrong status code. ", 200, result1.getResponse().getStatus());
            assertEquals("Wrong content type. ", MediaType.TEXT_HTML_VALUE, result1.getResponse().getContentType().substring(0,9));

            for(String res : resultHTML)
                assertTrue("Could not find expected content. ", result1.getResponse().getContentAsString().replace(" ", "").contains(res));


            MvcResult result2 = mvc.perform(get("/rest/applications?format=html")).andReturn();

            assertEquals("Wrong status code. ", 200, result2.getResponse().getStatus());
            assertEquals("Wrong content type. ", MediaType.TEXT_HTML_VALUE, result2.getResponse().getContentType().substring(0,9));

            for(String res : resultHTML)
                assertTrue("Could not find expected content.", result2.getResponse().getContentAsString().replace(" ", "").contains(res));


            // Test name parameter
            MvcResult result3 = mvc.perform(get("/rest/applications?name=demoApp3").accept(MediaType.TEXT_HTML)).andReturn();

            assertEquals("Wrong status code. ", 200, result3.getResponse().getStatus());
            assertEquals("Wrong content type. ", MediaType.TEXT_HTML_VALUE, result3.getResponse().getContentType().substring(0,9));

            assertTrue("Could not find expected content.", result3.getResponse().getContentAsString().replace(" ", "").contains(resultHTML[0]));
            assertTrue("Could not find expected content.", result3.getResponse().getContentAsString().replace(" ", "").contains(resultHTML[3]));

            assertFalse("Could find unexpected content.", result3.getResponse().getContentAsString().replace(" ", "").contains(resultHTML[1]));
            assertFalse("Could find unexpected content.", result3.getResponse().getContentAsString().replace(" ", "").contains(resultHTML[2]));


            MvcResult result4 = mvc.perform(get("/rest/applications?name=App").accept(MediaType.TEXT_HTML)).andReturn();

            assertEquals("Wrong status code. ", 200, result4.getResponse().getStatus());
            assertEquals("Wrong content type. ", MediaType.TEXT_HTML_VALUE, result4.getResponse().getContentType().substring(0,9));

            for(String res : resultHTML)
                assertTrue("Could not find expected content.", result4.getResponse().getContentAsString().replace(" ", "").contains(res));

        } catch (Exception e) {
            fail("Unexpected exception occurred: " + e.getMessage());
        }
    }

    @Test
    void getAllHTML_robustness() {
        try {
            MvcResult result1 = mvc.perform(get("/rest/applications?name=UNKNOWN").accept(MediaType.TEXT_HTML)).andReturn();

            assertEquals("Wrong status code. ", 200, result1.getResponse().getStatus());
            assertEquals("Wrong content type. ", MediaType.TEXT_HTML_VALUE, result1.getResponse().getContentType().substring(0,9));

            assertTrue("Could not find expected content.", result1.getResponse().getContentAsString().replace(" ", "").contains(resultHTML[0]));

            assertFalse("Could find unexpected content.", result1.getResponse().getContentAsString().replace(" ", "").contains(resultHTML[1]));
            assertFalse("Could find unexpected content.", result1.getResponse().getContentAsString().replace(" ", "").contains(resultHTML[2]));
            assertFalse("Could find unexpected content.", result1.getResponse().getContentAsString().replace(" ", "").contains(resultHTML[3]));

        } catch (Exception e) {
            fail("Unexpected exception occurred: " + e.getMessage());
        }
    }

    @Test
    void getById_functionality() {
        try {
            MvcResult result1 = mvc.perform(get("/rest/applications/1").accept(MediaType.APPLICATION_JSON)).andReturn();

            assertEquals("Wrong status code. ", 200, result1.getResponse().getStatus());
            assertEquals("Wrong content type. ", MediaType.APPLICATION_JSON_VALUE, result1.getResponse().getContentType());

            assertTrue("Could not find expected content.", result1.getResponse().getContentAsString().contains(resultJSON[0]));

            assertFalse("Could find unexpected content.", result1.getResponse().getContentAsString().contains(resultJSON[1]));
            assertFalse("Could find unexpected content.", result1.getResponse().getContentAsString().contains(resultJSON[2]));


            MvcResult result2 = mvc.perform(get("/rest/applications/2").accept(MediaType.APPLICATION_XML)).andReturn();

            assertEquals("Wrong status code. ", 200, result2.getResponse().getStatus());
            assertEquals("Wrong content type. ", MediaType.APPLICATION_XML_VALUE, result2.getResponse().getContentType());

            System.out.println(result2.getResponse().getContentAsString());
            assertTrue("Could not find expected content.", result2.getResponse().getContentAsString().replace(" ", "").contains(resultXML[1]));

            assertFalse("Could find unexpected content.", result2.getResponse().getContentAsString().replace(" ", "").contains(resultXML[0]));
            assertFalse("Could find unexpected content.", result2.getResponse().getContentAsString().replace(" ", "").contains(resultXML[2]));

        } catch (Exception e) {
            fail("Unexpected exception occurred: " + e.getMessage());
        }
    }

    @Test
    void getById_robustness() {
        try {
            MvcResult result1 = mvc.perform(get("/rest/applications/8").accept(MediaType.APPLICATION_JSON)).andReturn();
            assertEquals("Wrong status code. ", 404, result1.getResponse().getStatus());

            MvcResult result2 = mvc.perform(get("/rest/applications/0").accept(MediaType.APPLICATION_XML)).andReturn();
            assertEquals("Wrong status code. ", 404, result2.getResponse().getStatus());
        } catch (Exception e) {
            fail("Unexpected exception occurred: " + e.getMessage());
        }
    }

    @Test
    void getByIdCSV_functionality() {
        try {
            MvcResult result4 = mvc.perform(get("/rest/applications/3").accept(MediaType.valueOf("text/csv"))).andReturn();

            assertEquals("Wrong status code. ", 200, result4.getResponse().getStatus());
            assertEquals("Wrong content type. ", "text/csv", result4.getResponse().getContentType().substring(0,8));

            assertTrue("Could not find expected content.", result4.getResponse().getContentAsString().contains(resultCSV[0]));
            assertTrue("Could not find expected content. ", result4.getResponse().getContentAsString().contains(resultCSV[3]));

            assertFalse("Could find unexpected content.", result4.getResponse().getContentAsString().contains(resultCSV[1]));
            assertFalse("Could find unexpected content.", result4.getResponse().getContentAsString().contains(resultCSV[2]));
        } catch (Exception e) {
            fail("Unexpected exception occurred: " + e.getMessage());
        }
    }

    @Test
    void getByIdCSV_robustness() {
        try {
            MvcResult result1 = mvc.perform(get("/rest/applications/8").accept(MediaType.valueOf("text/csv"))).andReturn();
            assertEquals("Wrong status code. ", 404, result1.getResponse().getStatus());

            MvcResult result2 = mvc.perform(get("/rest/applications/0").accept(MediaType.valueOf("text/csv"))).andReturn();
            assertEquals("Wrong status code. ", 404, result2.getResponse().getStatus());
        } catch (Exception e) {
            fail("Unexpected exception occurred: " + e.getMessage());
        }
    }

    @Test
    void getByIdHTML_functionality() {
        try {
            MvcResult result3 = mvc.perform(get("/rest/applications/3").accept(MediaType.TEXT_HTML)).andReturn();

            assertEquals("Wrong status code. ", 200, result3.getResponse().getStatus());
            assertEquals("Wrong content type. ", MediaType.TEXT_HTML_VALUE, result3.getResponse().getContentType().substring(0,9));

            assertTrue("Could not find expected content.", result3.getResponse().getContentAsString().replace(" ", "").contains(resultHTML[0]));
            assertTrue("Could not find expected content.", result3.getResponse().getContentAsString().replace(" ", "").contains(resultHTML[3]));

            assertFalse("Could find unexpected content.", result3.getResponse().getContentAsString().replace(" ", "").contains(resultHTML[1]));
            assertFalse("Could find unexpected content.", result3.getResponse().getContentAsString().replace(" ", "").contains(resultHTML[2]));
        } catch (Exception e) {
            fail("Unexpected exception occurred: " + e.getMessage());
        }
    }

    @Test
    void getByIdHTML_robustness() {
        try {
            MvcResult result1 = mvc.perform(get("/rest/applications/5").accept(MediaType.TEXT_HTML)).andReturn();
            assertEquals("Wrong status code. ", 404, result1.getResponse().getStatus());

            MvcResult result2 = mvc.perform(get("/rest/applications/0").accept(MediaType.TEXT_HTML)).andReturn();
            assertEquals("Wrong status code. ", 404, result2.getResponse().getStatus());
        } catch (Exception e) {
            fail("Unexpected exception occurred: " + e.getMessage());
        }
    }

    @Test
    void postNewApplication_functionality_1() {
        try {
            MvcResult result = mvc.perform(post("/rest/applications").contentType(MediaType.APPLICATION_JSON)
                        .content(demoApp4JSON_1)).andReturn();

            assertEquals("Wrong status code. ", 200, result.getResponse().getStatus());
            assertEquals("Wrong content type. ", MediaType.APPLICATION_JSON_VALUE, result.getResponse().getContentType());

            assertTrue("Could not find expected id 4: ", repository.existsById(4));
            assertEquals("Could not find expected entry: ", demoApp4, repository.findById(4).orElse(null));

        } catch (Exception e) {
            fail("Unexpected exception occurred: " + e.getMessage());
        }
    }

    @Test
    void postNewApplication_robustness_1() {
        try {
            MvcResult result = mvc.perform(post("/rest/applications").contentType(MediaType.APPLICATION_JSON)
                    .content("{\"application_id\":5}")).andReturn();

            assertEquals("Wrong status code. ", 400, result.getResponse().getStatus());


            MvcResult result2 = mvc.perform(post("/rest/applications").contentType(MediaType.APPLICATION_JSON)
                    .content("{}")).andReturn();

            assertEquals("Wrong status code. ", 400, result2.getResponse().getStatus());

        } catch (Exception e) {
            fail("Unexpected exception occurred: " + e.getMessage());
        }
    }

    @Test
    void postNewApplication_functionality_2() {
        try {
            MvcResult result2 = mvc.perform(post("/rest/applications").contentType(MediaType.APPLICATION_XML).accept(MediaType.APPLICATION_XML)
                    .content(demoApp4XML_1)).andReturn();

            assertEquals("Wrong status code. ", 200, result2.getResponse().getStatus());
            assertEquals("Wrong content type. ", MediaType.APPLICATION_XML_VALUE, result2.getResponse().getContentType());

            assertTrue("Could not find expected id 4: ", repository.existsById(4));
            assertEquals("Could not find expected entry: ", demoApp4, repository.findById(4).orElse(null));

        } catch (Exception e) {
            fail("Unexpected exception occurred: " + e.getMessage());
        }
    }

    @Test
    void postNewApplication_robustness_2() {
        try {
            MvcResult result = mvc.perform(post("/rest/applications").contentType(MediaType.APPLICATION_XML)
                    .content("<application_id>5</application_id>")).andReturn();

            assertEquals("Wrong status code. ", 400, result.getResponse().getStatus());


            MvcResult result2 = mvc.perform(post("/rest/applications").contentType(MediaType.APPLICATION_XML)
                    .content("")).andReturn();

            assertEquals("Wrong status code. ", 400, result2.getResponse().getStatus());

        } catch (Exception e) {
            fail("Unexpected exception occurred: " + e.getMessage());
        }
    }

    @Test
    void postNewApplicationXFORM_functionality() {
        try {
            MvcResult result = mvc.perform(post("/rest/applications?format=xml").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .content(demoApp4XFORM_1)).andReturn();

            assertEquals("Wrong status code. ", 200, result.getResponse().getStatus());
            assertEquals("Wrong content type. ", MediaType.APPLICATION_XML_VALUE, result.getResponse().getContentType());

            assertTrue("Could not find expected id 4: ", repository.existsById(4));
            assertEquals("Could not find expected entry: ", demoApp4, repository.findById(4).orElse(null));

        } catch (Exception e) {
            fail("Unexpected exception occurred: " + e.getMessage());
        }
    }

    @Test
    void postNewApplicationXFORM_robustness() {
        try {
            MvcResult result = mvc.perform(post("/rest/applications").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .content("application_id=5")).andReturn();

            assertEquals("Wrong status code. ", 400, result.getResponse().getStatus());


            MvcResult result2 = mvc.perform(post("/rest/applications").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .content("nanana=3")).andReturn();

            assertEquals("Wrong status code. ", 400, result2.getResponse().getStatus());

        } catch (Exception e) {
            fail("Unexpected exception occurred: " + e.getMessage());
        }
    }

    @Test
    void postNewApplicationCSV_functionality() {
        try {
            MvcResult result = mvc.perform(post("/rest/applications").contentType(MediaType.valueOf("text/csv"))
                    .content(demoApp4CSV_1)).andReturn();

            assertEquals("Wrong status code. ", 200, result.getResponse().getStatus());
            assertEquals("Wrong content type. ", MediaType.APPLICATION_JSON_VALUE, result.getResponse().getContentType());

            assertTrue("Could not find expected id 4: ", repository.existsById(4));
            assertEquals("Could not find expected entry: ", demoApp4, repository.findById(4).orElse(null));

        } catch (Exception e) {
            fail("Unexpected exception occurred: " + e.getMessage());
        }
    }

    @Test
    void postNewApplicationCSV_robustness() {
        try {
            MvcResult result = mvc.perform(post("/rest/applications").contentType(MediaType.valueOf("text/csv"))
                    .content("application_id\n5")).andReturn();

            assertEquals("Wrong status code. ", 400, result.getResponse().getStatus());


            MvcResult result2 = mvc.perform(post("/rest/applications").contentType(MediaType.valueOf("text/csv"))
                    .content("nanana\n3")).andReturn();

            assertEquals("Wrong status code. ", 400, result2.getResponse().getStatus());

        } catch (Exception e) {
            fail("Unexpected exception occurred: " + e.getMessage());
        }
    }

    @Test
    void putApplicationById_functionality_1() {
        try {
            MvcResult result = mvc.perform(put("/rest/applications/4").contentType(MediaType.APPLICATION_JSON)
                    .content(demoApp4JSON_1)).andReturn();

            assertEquals("Wrong status code. ", 201, result.getResponse().getStatus());
            assertEquals("Wrong content type. ", MediaType.APPLICATION_JSON_VALUE, result.getResponse().getContentType());

            assertTrue("Could not find expected id 4: ", repository.existsById(4));
            assertEquals("Could not find expected entry: ", demoApp4, repository.findById(4).orElse(null));

        } catch (Exception e) {
            fail("Unexpected exception occurred: " + e.getMessage());
        }
    }

    @Test
    void putApplicationById_robustness_1() {
        try {
            MvcResult result = mvc.perform(put("/rest/applications/4").contentType(MediaType.APPLICATION_JSON)
                    .content("{\"application_id\":4}")).andReturn();

            assertEquals("Wrong status code. ", 400, result.getResponse().getStatus());


            MvcResult result2 = mvc.perform(put("/rest/applications/0").contentType(MediaType.APPLICATION_JSON)
                    .content("{\"application_id\":4}")).andReturn();

            assertEquals("Wrong status code. ", 400, result2.getResponse().getStatus());


            MvcResult result3 = mvc.perform(put("/rest/applications/4").contentType(MediaType.APPLICATION_JSON)
                    .content("{}")).andReturn();

            assertEquals("Wrong status code. ", 400, result3.getResponse().getStatus());

        } catch (Exception e) {
            fail("Unexpected exception occurred: " + e.getMessage());
        }
    }

    @Test
    void putApplicationById_functionality_2() {
        try {

            MvcResult result2 = mvc.perform(put("/rest/applications/4").contentType(MediaType.APPLICATION_XML)
                    .content(demoApp4XML_1)).andReturn();

            assertEquals("Wrong status code. ", 201, result2.getResponse().getStatus());
            assertEquals("Wrong content type. ", MediaType.APPLICATION_JSON_VALUE, result2.getResponse().getContentType());

            assertTrue("Could not find expected id 4: ", repository.existsById(4));
            assertEquals("Could not find expected entry: ", demoApp4, repository.findById(4).orElse(null));

        } catch (Exception e) {
            fail("Unexpected exception occurred: " + e.getMessage());
        }
    }

    @Test
    void putApplicationById_robustness_2() {
        try {
            MvcResult result = mvc.perform(put("/rest/applications/4").contentType(MediaType.APPLICATION_XML)
                    .content("<application_id>4</application_id>")).andReturn();

            assertEquals("Wrong status code. ", 400, result.getResponse().getStatus());


            MvcResult result2 = mvc.perform(put("/rest/applications/0").contentType(MediaType.APPLICATION_XML)
                    .content("<application_id>4</application_id>")).andReturn();

            assertEquals("Wrong status code. ", 400, result2.getResponse().getStatus());


            MvcResult result3 = mvc.perform(put("/rest/applications/4").contentType(MediaType.APPLICATION_XML)
                    .content("")).andReturn();

            assertEquals("Wrong status code. ", 400, result3.getResponse().getStatus());

        } catch (Exception e) {
            fail("Unexpected exception occurred: " + e.getMessage());
        }
    }

    @Test
    void putApplicationById_functionality_3() {
        try {
            MvcResult result3 = mvc.perform(put("/rest/applications/1").contentType(MediaType.APPLICATION_JSON)
                    .content(demoApp5JSON_2)).andReturn();

            assertEquals("Wrong status code. ", 200, result3.getResponse().getStatus());
            assertEquals("Wrong content type. ", MediaType.APPLICATION_JSON_VALUE, result3.getResponse().getContentType());

            assertTrue("Could not find expected id 1: ", repository.existsById(1));
            assertEquals("Could not find expected entry: ", demoApp5, repository.findById(1).orElse(null));

        } catch (Exception e) {
            fail("Unexpected exception occurred: " + e.getMessage());
        }
    }

    @Test
    void putApplicationById_functionality_4() {
        try {
            MvcResult result4 = mvc.perform(put("/rest/applications/1").contentType(MediaType.APPLICATION_XML)
                    .content(demoApp5XML_2)).andReturn();

            assertEquals("Wrong status code. ", 200, result4.getResponse().getStatus());
            assertEquals("Wrong content type. ", MediaType.APPLICATION_JSON_VALUE, result4.getResponse().getContentType());

            assertTrue("Could not find expected id 1: ", repository.existsById(1));
            assertEquals("Could not find expected entry: ", demoApp5, repository.findById(1).orElse(null));

        } catch (Exception e) {
            fail("Unexpected exception occurred: " + e.getMessage());
        }
    }

    @Test
    void putApplicationByIdXFORM_functionality_1() {
        try {
            MvcResult result1 = mvc.perform(put("/rest/applications/4").contentType(MediaType.APPLICATION_FORM_URLENCODED).accept(MediaType.APPLICATION_XML)
                    .content(demoApp4XFORM_1)).andReturn();

            assertEquals("Wrong status code. ", 201, result1.getResponse().getStatus());
            assertEquals("Wrong content type. ", MediaType.APPLICATION_XML_VALUE, result1.getResponse().getContentType());

            assertTrue("Could not find expected id 4: ", repository.existsById(4));
            assertEquals("Could not find expected entry: ", demoApp4, repository.findById(4).orElse(null));


            MvcResult result2 = mvc.perform(put("/rest/applications/1").contentType(MediaType.APPLICATION_FORM_URLENCODED).accept(MediaType.APPLICATION_XML)
                    .content(demoApp5XFORM_2)).andReturn();

            assertEquals("Wrong status code. ", 200, result2.getResponse().getStatus());
            assertEquals("Wrong content type. ", MediaType.APPLICATION_XML_VALUE, result2.getResponse().getContentType());

            assertTrue("Could not find expected id 1: ", repository.existsById(1));
            assertEquals("Could not find expected entry: ", demoApp5, repository.findById(1).orElse(null));
        } catch (Exception e) {
            fail("Unexpected exception occurred: " + e.getMessage());
        }
    }

    @Test
    void putApplicationByIdXFORM_robustness_1() {
        try {
            MvcResult result = mvc.perform(put("/rest/applications/4").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .content("application_id=4")).andReturn();

            assertEquals("Wrong status code. ", 400, result.getResponse().getStatus());


            MvcResult result2 = mvc.perform(put("/rest/applications/0").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .content("application_id=4")).andReturn();

            assertEquals("Wrong status code. ", 400, result2.getResponse().getStatus());


            MvcResult result3 = mvc.perform(put("/rest/applications/4").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .content("")).andReturn();

            assertEquals("Wrong status code. ", 400, result3.getResponse().getStatus());

        } catch (Exception e) {
            fail("Unexpected exception occurred: " + e.getMessage());
        }
    }

    @Test
    void putApplicationByIdCSV_functionality_1() {
        try {
            MvcResult result = mvc.perform(put("/rest/applications/4").contentType(MediaType.valueOf("text/csv"))
                    .content(demoApp4CSV_1)).andReturn();

            assertEquals("Wrong status code. ", 201, result.getResponse().getStatus());
            assertEquals("Wrong content type. ", MediaType.APPLICATION_JSON_VALUE, result.getResponse().getContentType());

            assertTrue("Could not find expected id 4: ", repository.existsById(4));
            assertEquals("Could not find expected entry: ", demoApp4, repository.findById(4).orElse(null));

            MvcResult result1 = mvc.perform(put("/rest/applications/1").contentType(MediaType.valueOf("text/csv"))
                    .content(demoApp5CSV_2)).andReturn();

            assertEquals("Wrong status code. ", 200, result1.getResponse().getStatus());
            assertEquals("Wrong content type. ", MediaType.APPLICATION_JSON_VALUE, result1.getResponse().getContentType());

            assertTrue("Could not find expected id 1: ", repository.existsById(1));
            assertEquals("Could not find expected entry: ", demoApp5, repository.findById(1).orElse(null));
        } catch (Exception e) {
            fail("Unexpected exception occurred: " + e.getMessage());
        }
    }

    @Test
    void putApplicationByIdCSV_robustness_1() {
        try {
            MvcResult result = mvc.perform(put("/rest/applications/4").contentType(MediaType.valueOf("text/csv"))
                    .content("application_id\n4")).andReturn();

            assertEquals("Wrong status code. ", 400, result.getResponse().getStatus());


            MvcResult result2 = mvc.perform(put("/rest/applications/0").contentType(MediaType.valueOf("text/csv"))
                    .content("application_id\n4")).andReturn();

            assertEquals("Wrong status code. ", 400, result2.getResponse().getStatus());


            MvcResult result3 = mvc.perform(put("/rest/applications/4").contentType(MediaType.valueOf("text/csv"))
                    .content("")).andReturn();

            assertEquals("Wrong status code. ", 400, result3.getResponse().getStatus());

        } catch (Exception e) {
            fail("Unexpected exception occurred: " + e.getMessage());
        }
    }

    @Test
    void putApplication_functionality_1() {
        try {
            MvcResult result = mvc.perform(put("/rest/applications").contentType(MediaType.APPLICATION_JSON)
                    .content(demoApp4JSON_1)).andReturn();

            assertEquals("Wrong status code. ", 201, result.getResponse().getStatus());
            assertEquals("Wrong content type. ", MediaType.APPLICATION_JSON_VALUE, result.getResponse().getContentType());

            assertTrue("Could not find expected id 4: ", repository.existsById(4));
            assertEquals("Could not find expected entry: ", demoApp4, repository.findById(4).orElse(null));

        } catch (Exception e) {
            fail("Unexpected exception occurred: " + e.getMessage());
        }
    }

    @Test
    void putApplication_robustness_1() {
        try {
            MvcResult result = mvc.perform(put("/rest/applications").contentType(MediaType.APPLICATION_JSON)
                    .content("{\"application_id\":4}")).andReturn();

            assertEquals("Wrong status code. ", 400, result.getResponse().getStatus());


            MvcResult result2 = mvc.perform(put("/rest/applications").contentType(MediaType.APPLICATION_JSON)
                    .content("{\"application_id\":0}")).andReturn();

            assertEquals("Wrong status code. ", 400, result2.getResponse().getStatus());


            MvcResult result3 = mvc.perform(put("/rest/applications").contentType(MediaType.APPLICATION_JSON)
                    .content("{}")).andReturn();

            assertEquals("Wrong status code. ", 400, result3.getResponse().getStatus());

        } catch (Exception e) {
            fail("Unexpected exception occurred: " + e.getMessage());
        }
    }

    @Test
    void putApplication_functionality_2() {
        try {

            MvcResult result2 = mvc.perform(put("/rest/applications").contentType(MediaType.APPLICATION_XML)
                    .content(demoApp4XML_1)).andReturn();

            assertEquals("Wrong status code. ", 201, result2.getResponse().getStatus());
            assertEquals("Wrong content type. ", MediaType.APPLICATION_JSON_VALUE, result2.getResponse().getContentType());

            assertTrue("Could not find expected id 4: ", repository.existsById(4));
            assertEquals("Could not find expected entry: ", demoApp4, repository.findById(4).orElse(null));

        } catch (Exception e) {
            fail("Unexpected exception occurred: " + e.getMessage());
        }
    }

    @Test
    void putApplication_robustness_2() {
        try {
            MvcResult result = mvc.perform(put("/rest/applications").contentType(MediaType.APPLICATION_XML)
                    .content("<application_id>4</application_id>")).andReturn();

            assertEquals("Wrong status code. ", 400, result.getResponse().getStatus());


            MvcResult result2 = mvc.perform(put("/rest/applications").contentType(MediaType.APPLICATION_XML)
                    .content("<application_id>0</application_id>")).andReturn();

            assertEquals("Wrong status code. ", 400, result2.getResponse().getStatus());


            MvcResult result3 = mvc.perform(put("/rest/applications").contentType(MediaType.APPLICATION_XML)
                    .content("")).andReturn();

            assertEquals("Wrong status code. ", 400, result3.getResponse().getStatus());

        } catch (Exception e) {
            fail("Unexpected exception occurred: " + e.getMessage());
        }
    }

    @Test
    void putApplication_functionality_3() {
        try {

            MvcResult result3 = mvc.perform(put("/rest/applications").contentType(MediaType.APPLICATION_JSON)
                    .content(demoApp5JSON_2)).andReturn();

            assertEquals("Wrong status code. ", 200, result3.getResponse().getStatus());
            assertEquals("Wrong content type. ", MediaType.APPLICATION_JSON_VALUE, result3.getResponse().getContentType());

            assertTrue("Could not find expected id 1: ", repository.existsById(1));
            assertEquals("Could not find expected entry: ", demoApp5, repository.findById(1).orElse(null));

        } catch (Exception e) {
            fail("Unexpected exception occurred: " + e.getMessage());
        }
    }

    @Test
    void putApplication_functionality_4() {
        try {

            MvcResult result4 = mvc.perform(put("/rest/applications").contentType(MediaType.APPLICATION_XML).accept(MediaType.APPLICATION_XML)
                    .content(demoApp5XML_2)).andReturn();

            assertEquals("Wrong status code. ", 200, result4.getResponse().getStatus());
            assertEquals("Wrong content type. ", MediaType.APPLICATION_XML_VALUE, result4.getResponse().getContentType());

            assertTrue("Could not find expected id 2: ", repository.existsById(2));
            assertEquals("Could not find expected entry: ", new ApplicationsEntity(2, "demoApp5"), repository.findById(2).orElse(null));
        } catch (Exception e) {
            fail("Unexpected exception occurred: " + e.getMessage());
        }
    }

    @Test
    void putApplicationXFORM_functionality_1() {
        try {
            MvcResult result1 = mvc.perform(put("/rest/applications").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .content(demoApp4XFORM_1)).andReturn();

            assertEquals("Wrong status code. ", 201, result1.getResponse().getStatus());
            assertEquals("Wrong content type. ", MediaType.APPLICATION_JSON_VALUE, result1.getResponse().getContentType());

            assertTrue("Could not find expected id 4: ", repository.existsById(4));
            assertEquals("Could not find expected entry: ", demoApp4, repository.findById(4).orElse(null));

            MvcResult result = mvc.perform(put("/rest/applications").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .content(demoApp5XFORM_2)).andReturn();

            assertEquals("Wrong status code. ", 200, result.getResponse().getStatus());
            assertEquals("Wrong content type. ", MediaType.APPLICATION_JSON_VALUE, result.getResponse().getContentType());

            assertTrue("Could not find expected id 1: ", repository.existsById(1));
            assertEquals("Could not find expected entry: ", demoApp5, repository.findById(1).orElse(null));
        } catch (Exception e) {
            fail("Unexpected exception occurred: " + e.getMessage());
        }
    }

    @Test
    void putApplicationXFORM_robustness_1() {
        try {
            MvcResult result = mvc.perform(put("/rest/applications").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .content("application_id=4")).andReturn();

            assertEquals("Wrong status code. ", 400, result.getResponse().getStatus());


            MvcResult result2 = mvc.perform(put("/rest/applications").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .content("application_id=0")).andReturn();

            assertEquals("Wrong status code. ", 400, result2.getResponse().getStatus());


            MvcResult result3 = mvc.perform(put("/rest/applications").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .content("")).andReturn();

            assertEquals("Wrong status code. ", 400, result3.getResponse().getStatus());

        } catch (Exception e) {
            fail("Unexpected exception occurred: " + e.getMessage());
        }
    }


    @Test
    void putApplicationCSV_functionality_1() {
        try {
            MvcResult result = mvc.perform(put("/rest/applications?format=xml").contentType(MediaType.valueOf("text/csv"))
                    .content(demoApp4CSV_1)).andReturn();

            assertEquals("Wrong status code. ", 201, result.getResponse().getStatus());
            assertEquals("Wrong content type. ", MediaType.APPLICATION_XML_VALUE, result.getResponse().getContentType());

            assertTrue("Could not find expected id 4: ", repository.existsById(4));
            assertEquals("Could not find expected entry: ", demoApp4, repository.findById(4).orElse(null));

            MvcResult result1 = mvc.perform(put("/rest/applications?format=xml").contentType(MediaType.valueOf("text/csv"))
                    .content(demoApp5CSV_2)).andReturn();

            assertEquals("Wrong status code. ", 200, result1.getResponse().getStatus());
            assertEquals("Wrong content type. ", MediaType.APPLICATION_XML_VALUE, result1.getResponse().getContentType());

            assertTrue("Could not find expected id 1: ", repository.existsById(1));
            assertEquals("Could not find expected entry: ", demoApp5, repository.findById(1).orElse(null));



        } catch (Exception e) {
            fail("Unexpected exception occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Test
    void putApplicationCSV_robustness_1() {
        try {
            MvcResult result = mvc.perform(put("/rest/applications").contentType(MediaType.valueOf("text/csv"))
                    .content("application_id\n4")).andReturn();

            assertEquals("Wrong status code. ", 400, result.getResponse().getStatus());


            MvcResult result2 = mvc.perform(put("/rest/applications").contentType(MediaType.valueOf("text/csv"))
                    .content("application_id\n0")).andReturn();

            assertEquals("Wrong status code. ", 400, result2.getResponse().getStatus());


            MvcResult result3 = mvc.perform(put("/rest/applications").contentType(MediaType.valueOf("text/csv"))
                    .content("")).andReturn();

            assertEquals("Wrong status code. ", 400, result3.getResponse().getStatus());


            MvcResult result4 = mvc.perform(put("/rest/applications").contentType(MediaType.valueOf("text/csv"))
                    .content("application_id,application_name\nnanana")).andReturn();

            assertEquals("Wrong status code. ", 400, result4.getResponse().getStatus());

        } catch (Exception e) {
            fail("Unexpected exception occurred: " + e.getMessage());
        }
    }

    @Test
    void patchApplicationById_functionality_1() {
        try {
            MvcResult result = mvc.perform(patch("/rest/applications/4").contentType(MediaType.APPLICATION_JSON)
                    .content(demoApp4JSON_1)).andReturn();

            assertEquals("Wrong status code. ", 400, result.getResponse().getStatus());
        } catch (Exception e) {
            fail("Unexpected exception occurred: " + e.getMessage());
        }
    }

    @Test
    void patchApplicationById_robustness_1() {
        try {
            MvcResult result = mvc.perform(patch("/rest/applications/4").contentType(MediaType.APPLICATION_JSON)
                    .content("{\"application_id\":4}")).andReturn();

            assertEquals("Wrong status code. ", 400, result.getResponse().getStatus());


            MvcResult result2 = mvc.perform(patch("/rest/applications/0").contentType(MediaType.APPLICATION_JSON)
                    .content("{\"application_id\":4}")).andReturn();

            assertEquals("Wrong status code. ", 400, result2.getResponse().getStatus());


            MvcResult result3 = mvc.perform(patch("/rest/applications/4").contentType(MediaType.APPLICATION_JSON)
                    .content("{}")).andReturn();

            assertEquals("Wrong status code. ", 400, result3.getResponse().getStatus());

        } catch (Exception e) {
            fail("Unexpected exception occurred: " + e.getMessage());
        }
    }

    @Test
    void patchApplicationById_functionality_2() {
        try {

            MvcResult result2 = mvc.perform(patch("/rest/applications/4").contentType(MediaType.APPLICATION_XML)
                    .content(demoApp4XML_1)).andReturn();

            assertEquals("Wrong status code. ", 400, result2.getResponse().getStatus());

        } catch (Exception e) {
            fail("Unexpected exception occurred: " + e.getMessage());
        }
    }

    @Test
    void patchApplicationById_robustness_2() {
        try {
            MvcResult result = mvc.perform(patch("/rest/applications/4").contentType(MediaType.APPLICATION_XML)
                    .content("<application_id>4</application_id>")).andReturn();

            assertEquals("Wrong status code. ", 400, result.getResponse().getStatus());


            MvcResult result2 = mvc.perform(patch("/rest/applications/0").contentType(MediaType.APPLICATION_XML)
                    .content("<application_id>4</application_id>")).andReturn();

            assertEquals("Wrong status code. ", 400, result2.getResponse().getStatus());


            MvcResult result3 = mvc.perform(patch("/rest/applications/4").contentType(MediaType.APPLICATION_XML)
                    .content("")).andReturn();

            assertEquals("Wrong status code. ", 400, result3.getResponse().getStatus());

        } catch (Exception e) {
            fail("Unexpected exception occurred: " + e.getMessage());
        }
    }

    @Test
    void patchApplicationById_functionality_3() {
        try {

            MvcResult result3 = mvc.perform(patch("/rest/applications/1").contentType(MediaType.APPLICATION_JSON)
                    .content(demoApp4JSON_1)).andReturn();

            assertEquals("Wrong status code. ", 200, result3.getResponse().getStatus());
            assertEquals("Wrong content type. ", MediaType.APPLICATION_JSON_VALUE, result3.getResponse().getContentType());

            assertTrue("Could not find expected id 1: ", repository.existsById(1));
            assertEquals("Could not find expected entry: ", new ApplicationsEntity(1, "demoApp4"), repository.findById(1).orElse(null));

        } catch (Exception e) {
            fail("Unexpected exception occurred: " + e.getMessage());
        }
    }

    @Test
    void patchApplicationById_functionality_4() {
        try {

            MvcResult result4 = mvc.perform(patch("/rest/applications/1").contentType(MediaType.APPLICATION_XML).accept(MediaType.APPLICATION_XML)
                    .content(demoApp4XML_1)).andReturn();

            assertEquals("Wrong status code. ", 200, result4.getResponse().getStatus());
            assertEquals("Wrong content type. ", MediaType.APPLICATION_XML_VALUE, result4.getResponse().getContentType());

            assertTrue("Could not find expected id 1: ", repository.existsById(1));
            assertEquals("Could not find expected entry: ", new ApplicationsEntity(1, "demoApp4"), repository.findById(1).orElse(null));
        } catch (Exception e) {
            fail("Unexpected exception occurred: " + e.getMessage());
        }
    }

    @Test
    void patchApplicationByIdXFORM_functionality_1() {
        try {
            MvcResult result1 = mvc.perform(patch("/rest/applications/4").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .content(demoApp4XFORM_1)).andReturn();

            assertEquals("Wrong status code. ", 400, result1.getResponse().getStatus());


            MvcResult result2 = mvc.perform(patch("/rest/applications/1").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .content(demoApp5XFORM_2)).andReturn();

            assertEquals("Wrong status code. ", 200, result2.getResponse().getStatus());
            assertEquals("Wrong content type. ", MediaType.APPLICATION_JSON_VALUE, result2.getResponse().getContentType());

            assertTrue("Could not find expected id 1: ", repository.existsById(1));
            assertEquals("Could not find expected entry: ", demoApp5, repository.findById(1).orElse(null));
        } catch (Exception e) {
            fail("Unexpected exception occurred: " + e.getMessage());
        }

    }

    @Test
    void patchApplicationByIdXFORM_robustness_1() {
        try {
            MvcResult result = mvc.perform(patch("/rest/applications/4").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .content("application_id=4")).andReturn();

            assertEquals("Wrong status code. ", 400, result.getResponse().getStatus());


            MvcResult result2 = mvc.perform(patch("/rest/applications/0").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .content("application_id=4")).andReturn();

            assertEquals("Wrong status code. ", 400, result2.getResponse().getStatus());


            MvcResult result3 = mvc.perform(patch("/rest/applications/4").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .content("")).andReturn();

            assertEquals("Wrong status code. ", 400, result3.getResponse().getStatus());

        } catch (Exception e) {
            fail("Unexpected exception occurred: " + e.getMessage());
        }
    }

    @Test
    void patchApplicationByIdCSV_functionality_1() {
        try {
            MvcResult result = mvc.perform(patch("/rest/applications/4").contentType(MediaType.valueOf("text/csv"))
                    .content(demoApp4CSV_1)).andReturn();

            assertEquals("Wrong status code. ", 404, result.getResponse().getStatus());


            MvcResult result1 = mvc.perform(patch("/rest/applications/1").contentType(MediaType.valueOf("text/csv"))
                    .content(demoApp5CSV_2)).andReturn();

            assertEquals("Wrong status code. ", 200, result1.getResponse().getStatus());
            assertEquals("Wrong content type. ", MediaType.APPLICATION_JSON_VALUE, result1.getResponse().getContentType());

            assertTrue("Could not find expected id 1: ", repository.existsById(1));
            assertEquals("Could not find expected entry: ", demoApp5, repository.findById(1).orElse(null));
        } catch (Exception e) {
            fail("Unexpected exception occurred: " + e.getMessage());
        }
    }

    @Test
    void patchApplicationByIdCSV_robustness_1() {
        try {
            MvcResult result = mvc.perform(patch("/rest/applications/4").contentType(MediaType.valueOf("text/csv"))
                    .content("application_id\n4")).andReturn();

            assertEquals("Wrong status code. ", 400, result.getResponse().getStatus());


            MvcResult result2 = mvc.perform(patch("/rest/applications/0").contentType(MediaType.valueOf("text/csv"))
                    .content("application_id\n4")).andReturn();

            assertEquals("Wrong status code. ", 400, result2.getResponse().getStatus());


            MvcResult result3 = mvc.perform(patch("/rest/applications/4").contentType(MediaType.valueOf("text/csv"))
                    .content("")).andReturn();

            assertEquals("Wrong status code. ", 400, result3.getResponse().getStatus());

        } catch (Exception e) {
            fail("Unexpected exception occurred: " + e.getMessage());
        }
    }

    @Test
    void patchApplication_functionality_1() {
        try {
            MvcResult result = mvc.perform(patch("/rest/applications").contentType(MediaType.APPLICATION_JSON)
                    .content(demoApp4JSON_1)).andReturn();

            assertEquals("Wrong status code. ", 400, result.getResponse().getStatus());

        } catch (Exception e) {
            fail("Unexpected exception occurred: " + e.getMessage());
        }
    }

    @Test
    void patchApplication_robustness_1() {
        try {
            MvcResult result = mvc.perform(patch("/rest/applications").contentType(MediaType.APPLICATION_JSON)
                    .content("{\"application_id\":4}")).andReturn();

            assertEquals("Wrong status code. ", 400, result.getResponse().getStatus());


            MvcResult result2 = mvc.perform(patch("/rest/applications").contentType(MediaType.APPLICATION_JSON)
                    .content("{\"application_id\":0}")).andReturn();

            assertEquals("Wrong status code. ", 400, result2.getResponse().getStatus());


            MvcResult result3 = mvc.perform(patch("/rest/applications").contentType(MediaType.APPLICATION_JSON)
                    .content("{}")).andReturn();

            assertEquals("Wrong status code. ", 400, result3.getResponse().getStatus());

        } catch (Exception e) {
            fail("Unexpected exception occurred: " + e.getMessage());
        }
    }

    @Test
    void patchApplication_functionality_2() {
        try {
            MvcResult result2 = mvc.perform(patch("/rest/applications").contentType(MediaType.APPLICATION_XML)
                    .content(demoApp4XML_1)).andReturn();

            assertEquals("Wrong status code. ", 400, result2.getResponse().getStatus());

        } catch (Exception e) {
            fail("Unexpected exception occurred: " + e.getMessage());
        }
    }

    @Test
    void patchApplication_robustness_2() {
        try {
            MvcResult result = mvc.perform(patch("/rest/applications").contentType(MediaType.APPLICATION_XML)
                    .content("<application_id>4</application_id>")).andReturn();

            assertEquals("Wrong status code. ", 400, result.getResponse().getStatus());


            MvcResult result2 = mvc.perform(patch("/rest/applications").contentType(MediaType.APPLICATION_XML)
                    .content("<application_id>0</application_id>")).andReturn();

            assertEquals("Wrong status code. ", 400, result2.getResponse().getStatus());


            MvcResult result3 = mvc.perform(patch("/rest/applications").contentType(MediaType.APPLICATION_XML)
                    .content("")).andReturn();

            assertEquals("Wrong status code. ", 400, result3.getResponse().getStatus());

        } catch (Exception e) {
            fail("Unexpected exception occurred: " + e.getMessage());
        }
    }

    @Test
    void patchApplication_3() {
        try {

            MvcResult result3 = mvc.perform(patch("/rest/applications").contentType(MediaType.APPLICATION_JSON)
                    .content(demoApp5JSON_2)).andReturn();

            assertEquals("Wrong status code. ", 200, result3.getResponse().getStatus());
            assertEquals("Wrong content type. ", MediaType.APPLICATION_JSON_VALUE, result3.getResponse().getContentType());

            assertTrue("Could not find expected id 1: ", repository.existsById(1));
            assertEquals("Could not find expected entry: ", demoApp5, repository.findById(1).orElse(null));

        } catch (Exception e) {
            fail("Unexpected exception occurred: " + e.getMessage());
        }
    }

    @Test
    void patchApplication_4() {
        try {

            MvcResult result4 = mvc.perform(patch("/rest/applications").contentType(MediaType.APPLICATION_XML).accept(MediaType.APPLICATION_XML)
                    .content(demoApp5XML_2)).andReturn();

            assertEquals("Wrong status code. ", 200, result4.getResponse().getStatus());
            assertEquals("Wrong content type. ", MediaType.APPLICATION_XML_VALUE, result4.getResponse().getContentType());

            assertTrue("Could not find expected id 1: ", repository.existsById(2));
            assertEquals("Could not find expected entry: ", new ApplicationsEntity(2, "demoApp5"), repository.findById(2).orElse(null));
        } catch (Exception e) {
            fail("Unexpected exception occurred: " + e.getMessage());
        }
    }

    @Test
    void patchApplicationXFORM_functionality_1() {
        try {
            MvcResult result1 = mvc.perform(patch("/rest/applications").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .content(demoApp4XFORM_1)).andReturn();

            assertEquals("Wrong status code. ", 400, result1.getResponse().getStatus());


            MvcResult result = mvc.perform(patch("/rest/applications").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .content(demoApp5XFORM_2)).andReturn();

            assertEquals("Wrong status code. ", 200, result.getResponse().getStatus());
            assertEquals("Wrong content type. ", MediaType.APPLICATION_JSON_VALUE, result.getResponse().getContentType());

            assertTrue("Could not find expected id 1: ", repository.existsById(1));
            assertEquals("Could not find expected entry: ", demoApp5, repository.findById(1).orElse(null));
        } catch (Exception e) {
            fail("Unexpected exception occurred: " + e.getMessage());
        }
    }

    @Test
    void patchApplicationXFORM_robustness_1() {
        try {
            MvcResult result = mvc.perform(patch("/rest/applications").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .content("application_id=4")).andReturn();

            assertEquals("Wrong status code. ", 400, result.getResponse().getStatus());


            MvcResult result2 = mvc.perform(patch("/rest/applications").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .content("application_id=0")).andReturn();

            assertEquals("Wrong status code. ", 400, result2.getResponse().getStatus());


            MvcResult result3 = mvc.perform(put("/rest/applications").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .content("")).andReturn();

            assertEquals("Wrong status code. ", 400, result3.getResponse().getStatus());

        } catch (Exception e) {
            fail("Unexpected exception occurred: " + e.getMessage());
        }
    }

    @Test
    void patchApplicationCSV_functionality_1() {
        try {
            MvcResult result = mvc.perform(patch("/rest/applications").contentType(MediaType.valueOf("text/csv"))
                    .content(demoApp4CSV_1)).andReturn();

            assertEquals("Wrong status code. ", 404, result.getResponse().getStatus());


            MvcResult result1 = mvc.perform(patch("/rest/applications").contentType(MediaType.valueOf("text/csv"))
                    .content(demoApp5CSV_2)).andReturn();

            assertEquals("Wrong status code. ", 200, result1.getResponse().getStatus());
            assertEquals("Wrong content type. ", MediaType.APPLICATION_JSON_VALUE, result1.getResponse().getContentType());

            assertTrue("Could not find expected id 4: ", repository.existsById(1));
            assertEquals("Could not find expected entry: ", demoApp5, repository.findById(1).orElse(null));
        } catch (Exception e) {
            fail("Unexpected exception occurred: " + e.getMessage());
        }
    }

    @Test
    void patchApplicationCSV_robustness_1() {
        try {
            MvcResult result = mvc.perform(patch("/rest/applications").contentType(MediaType.valueOf("text/csv"))
                    .content("application_id\n4")).andReturn();

            assertEquals("Wrong status code. ", 400, result.getResponse().getStatus());


            MvcResult result2 = mvc.perform(patch("/rest/applications").contentType(MediaType.valueOf("text/csv"))
                    .content("application_id\n0")).andReturn();

            assertEquals("Wrong status code. ", 400, result2.getResponse().getStatus());


            MvcResult result3 = mvc.perform(patch("/rest/applications").contentType(MediaType.valueOf("text/csv"))
                    .content("")).andReturn();

            assertEquals("Wrong status code. ", 400, result3.getResponse().getStatus());

        } catch (Exception e) {
            fail("Unexpected exception occurred: " + e.getMessage());
        }
    }

    @Test
    void deleteById_functionality_1() {
        try {
            MvcResult result = mvc.perform(delete("/rest/applications/1")).andReturn();

            assertEquals("Wrong status code. ", 204, result.getResponse().getStatus());
            assertFalse("Content was not deleted. ", repository.existsById(1));
        } catch (Exception e) {
            fail("Unexpected exception occurred: " + e.getMessage());
        }
    }

    @Test
    void deleteById_robustness_1() {
        try {
            MvcResult result = mvc.perform(delete("/rest/applications/19")).andReturn();

            assertEquals("Wrong status code. ", 500, result.getResponse().getStatus());
            assertTrue("Content was deleted. ", repository.existsById(1));
            assertTrue("Content was deleted. ", repository.existsById(2));
            assertTrue("Content was deleted. ", repository.existsById(3));
        } catch (Exception e) {
            fail("Unexpected exception occurred: " + e.getMessage());
        }
    }

    @Test
    void deleteApplication_functionality_1() {
        try {
            MvcResult result = mvc.perform(delete("/rest/applications").contentType(MediaType.APPLICATION_JSON)
                    .content(demoApp5JSON_2)).andReturn();

            assertEquals("Wrong status code. ", 204, result.getResponse().getStatus());
            assertFalse("Content was not deleted. ", repository.existsById(1));

            MvcResult result1 = mvc.perform(delete("/rest/applications").contentType(MediaType.APPLICATION_XML)
                    .content(demoApp5XML_2)).andReturn();

            assertEquals("Wrong status code. ", 204, result1.getResponse().getStatus());
            assertFalse("Content was not deleted. ", repository.existsById(2));
        } catch (Exception e) {
            fail("Unexpected exception occurred: " + e.getMessage());
        }
    }

    @Test
    void deleteApplication_robustness_1() {
        try {
            MvcResult result = mvc.perform(delete("/rest/applications").contentType(MediaType.APPLICATION_JSON)
                    .content("{\"application_id\":8}")).andReturn();

            assertEquals("Wrong status code. ", 500, result.getResponse().getStatus());
            assertTrue("Content was deleted. ", repository.existsById(1));
            assertTrue("Content was deleted. ", repository.existsById(2));
            assertTrue("Content was deleted. ", repository.existsById(3));


            MvcResult result1 = mvc.perform(delete("/rest/applications").contentType(MediaType.APPLICATION_XML)
                    .content("<application_id>7</application_id><application_name>demoApp2</application_name>")).andReturn();

            assertEquals("Wrong status code. ", 500, result1.getResponse().getStatus());
            assertTrue("Content was deleted. ", repository.existsById(1));
            assertTrue("Content was deleted. ", repository.existsById(2));
            assertTrue("Content was deleted. ", repository.existsById(3));
        } catch (Exception e) {
            fail("Unexpected exception occurred: " + e.getMessage());
        }
    }

    @Test
    void deleteApplicationXFORM_functionality_1() {
        try {
            MvcResult result = mvc.perform(delete("/rest/applications").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .content(demoApp5XFORM_2)).andReturn();

            assertEquals("Wrong status code. ", 204, result.getResponse().getStatus());
            assertFalse("Content was not deleted. ", repository.existsById(1));
        } catch (Exception e) {
            fail("Unexpected exception occurred: " + e.getMessage());
        }
    }

    @Test
    void deleteApplicationXFORM_robustness_1() {
        try {
            MvcResult result = mvc.perform(delete("/rest/applications").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .content("application_id=0")).andReturn();

            assertEquals("Wrong status code. ", 500, result.getResponse().getStatus());
            assertTrue("Content was deleted. ", repository.existsById(1));
            assertTrue("Content was deleted. ", repository.existsById(2));
            assertTrue("Content was deleted. ", repository.existsById(3));
        } catch (Exception e) {
            fail("Unexpected exception occurred: " + e.getMessage());
        }
    }

    @Test
    void deleteApplicationCSV() {
        try {
            MvcResult result = mvc.perform(delete("/rest/applications").contentType(MediaType.valueOf("text/csv"))
                    .content(demoApp5CSV_2)).andReturn();

            assertEquals("Wrong status code. ", 204, result.getResponse().getStatus());
            assertFalse("Content was not deleted. ", repository.existsById(1));
        } catch (Exception e) {
            fail("Unexpected exception occurred: " + e.getMessage());
        }
    }

    @Test
    void deleteApplicationCSV_robustness_1() {
        try {
            MvcResult result = mvc.perform(delete("/rest/applications").contentType(MediaType.valueOf("text/csv"))
                    .content("application_id,application_name\n8,demoApp1")).andReturn();

            assertEquals("Wrong status code. ", 500, result.getResponse().getStatus());
            assertTrue("Content was deleted. ", repository.existsById(1));
            assertTrue("Content was deleted. ", repository.existsById(2));
            assertTrue("Content was deleted. ", repository.existsById(3));


            MvcResult result1 = mvc.perform(delete("/rest/applications").contentType(MediaType.valueOf("text/csv"))
                    .content("application_id\n8")).andReturn();

            assertEquals("Wrong status code. ", 400, result1.getResponse().getStatus());
            assertTrue("Content was deleted. ", repository.existsById(1));
            assertTrue("Content was deleted. ", repository.existsById(2));
            assertTrue("Content was deleted. ", repository.existsById(3));
        } catch (Exception e) {
            fail("Unexpected exception occurred: " + e.getMessage());
        }
    }
}