package com.atopion.UGC_repository.rest.ctrl;

import com.atopion.UGC_repository.rest.entities.ApplicationsEntity;
import com.atopion.UGC_repository.rest.repositories.ApplicationsRepository;
import com.atopion.UGC_repository.testutil.AccessTokens;
import com.atopion.UGC_repository.testutil.AssertionHelpers;
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
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("classpath:application-integrationtests.properties")
public class ApplicationsControllerTest {

	private final String[] resultJSON  = new String[]{"{\"application_id\":1,\"application_name\":\"demo_name1\"}", "{\"application_id\":2,\"application_name\":\"demo_name2\"}", "{\"application_id\":3,\"application_name\":\"demo_name3\"}"};
	private final String[] resultXML   = new String[]{"<application_id>1</application_id><application_name>demo_name1</application_name>", "<application_id>2</application_id><application_name>demo_name2</application_name>", "<application_id>3</application_id><application_name>demo_name3</application_name>"};
	private final String[] resultCSV   = new String[]{"application_id,application_name", "1,demo_name1", "2,demo_name2", "3,demo_name3"};
	private final String[] resultHTML  = new String[]{"<tr>\n<th>application_id</th>\n<th>application_name</th>\n</tr>", "<tr>\n<td>1</td>\n<td>demo_name1</td>\n</tr>", "<tr>\n<td>2</td>\n<td>demo_name2</td>\n</tr>", "<tr>\n<td>3</td>\n<td>demo_name3</td>\n</tr>"};
	private final String[] resultXFORM = new String[]{"application_id=1&application_name=demo_name1", "application_id=2&application_name=demo_name2", "application_id=3&application_name=demo_name3"};

	@Autowired
	private MockMvc mvc;

	@Autowired
	private ApplicationsRepository repository;

	@Autowired
	private DataSource restDataSource;

	private ApplicationsEntity demo1, demo3;

	private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@BeforeEach
	void setUp() {
		TimeZone.setDefault(TimeZone.getTimeZone("Europe/Berlin"));

		try {
			demo1 = new ApplicationsEntity("demo_name1");
			ApplicationsEntity demo2 = new ApplicationsEntity("demo_name2");
		
			demo3 = new ApplicationsEntity(3, "demo_name3");

			repository.save(demo1);
			repository.save(demo2);

			repository.flush();
		} catch (Exception e) {
			fail("Unexpected exception occured: " + e.getMessage());
		}
	}

	@AfterEach
	void tearDown() {
		repository.deleteAll();
		repository.flush();

		try (Connection connection = restDataSource.getConnection();
			 Statement statement = connection.createStatement()) {
			statement.executeUpdate("ALTER TABLE applications ALTER COLUMN application_id RESTART WITH 1;");

		} catch (Exception e) {
			fail("Unexpected exception occured: " + e.getMessage());
		}
	}

	@Test
	void test_GET_JSON_format_functionality() {

		try {
			
			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(get("/rest/applications?format=json")).secure(true)).andReturn();

			assertEquals("Wrong status code. ", 200, result.getResponse().getStatus());
			assertThat("Wrong content type. ", result.getResponse().getContentType(), containsString(MediaType.APPLICATION_JSON_VALUE));

			AssertionHelpers.assertResponseContainsText(result.getResponse(), resultJSON[0]);
			AssertionHelpers.assertResponseContainsText(result.getResponse(), resultJSON[1]);

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_GET_XML_format_functionality() {

		try {
			
			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(get("/rest/applications?format=xml")).secure(true)).andReturn();

			assertEquals("Wrong status code. ", 200, result.getResponse().getStatus());
			assertThat("Wrong content type. ", result.getResponse().getContentType(), containsString(MediaType.APPLICATION_XML_VALUE));

			AssertionHelpers.assertResponseContainsText(result.getResponse(), resultXML[0]);
			AssertionHelpers.assertResponseContainsText(result.getResponse(), resultXML[1]);

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_GET_CSV_format_functionality() {

		try {
			
			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(get("/rest/applications?format=csv")).secure(true)).andReturn();

			assertEquals("Wrong status code. ", 200, result.getResponse().getStatus());
			assertThat("Wrong content type. ", result.getResponse().getContentType(), containsString("text/csv"));

			AssertionHelpers.assertResponseContainsText(result.getResponse(), resultCSV[0]);
			AssertionHelpers.assertResponseContainsText(result.getResponse(), resultCSV[1]);
			AssertionHelpers.assertResponseContainsText(result.getResponse(), resultCSV[2]);

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_GET_HTML_format_functionality() {

		try {
			
			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(get("/rest/applications?format=html")).secure(true)).andReturn();

			assertEquals("Wrong status code. ", 200, result.getResponse().getStatus());
			assertThat("Wrong content type. ", result.getResponse().getContentType(), containsString(MediaType.TEXT_HTML_VALUE));

			AssertionHelpers.assertResponseContainsText(result.getResponse(), resultHTML[0]);
			AssertionHelpers.assertResponseContainsText(result.getResponse(), resultHTML[1]);
			AssertionHelpers.assertResponseContainsText(result.getResponse(), resultHTML[2]);

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_GET_TEST_format_robustness() {

		try {
			
			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(get("/rest/applications?format=xlsx")).secure(true)).andReturn();

			assertEquals("Wrong status code. ", 200, result.getResponse().getStatus());


		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_GET_JSON_accept_functionality() {

		try {
			
			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(get("/rest/applications")).accept(MediaType.APPLICATION_JSON).secure(true)).andReturn();

			assertEquals("Wrong status code. ", 200, result.getResponse().getStatus());
			assertThat("Wrong content type. ", result.getResponse().getContentType(), containsString(MediaType.APPLICATION_JSON_VALUE));

			AssertionHelpers.assertResponseContainsText(result.getResponse(), resultJSON[0]);
			AssertionHelpers.assertResponseContainsText(result.getResponse(), resultJSON[1]);

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_GET_XML_accept_functionality() {

		try {
			
			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(get("/rest/applications")).accept(MediaType.APPLICATION_XML).secure(true)).andReturn();

			assertEquals("Wrong status code. ", 200, result.getResponse().getStatus());
			assertThat("Wrong content type. ", result.getResponse().getContentType(), containsString(MediaType.APPLICATION_XML_VALUE));

			AssertionHelpers.assertResponseContainsText(result.getResponse(), resultXML[0]);
			AssertionHelpers.assertResponseContainsText(result.getResponse(), resultXML[1]);

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_GET_CSV_accept_functionality() {

		try {
			
			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(get("/rest/applications")).accept(MediaType.valueOf("text/csv")).secure(true)).andReturn();

			assertEquals("Wrong status code. ", 200, result.getResponse().getStatus());
			assertThat("Wrong content type. ", result.getResponse().getContentType(), containsString("text/csv"));

			AssertionHelpers.assertResponseContainsText(result.getResponse(), resultCSV[0]);
			AssertionHelpers.assertResponseContainsText(result.getResponse(), resultCSV[1]);
			AssertionHelpers.assertResponseContainsText(result.getResponse(), resultCSV[2]);

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_GET_HTML_accept_functionality() {

		try {
			
			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(get("/rest/applications")).accept(MediaType.TEXT_HTML).secure(true)).andReturn();

			assertEquals("Wrong status code. ", 200, result.getResponse().getStatus());
			assertThat("Wrong content type. ", result.getResponse().getContentType(), containsString(MediaType.TEXT_HTML_VALUE));

			AssertionHelpers.assertResponseContainsText(result.getResponse(), resultHTML[0]);
			AssertionHelpers.assertResponseContainsText(result.getResponse(), resultHTML[1]);
			AssertionHelpers.assertResponseContainsText(result.getResponse(), resultHTML[2]);

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_GET_TEST_accept_robustness() {

		try {
			
			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(get("/rest/applications")).accept(MediaType.IMAGE_GIF).secure(true)).andReturn();

			assertEquals("Wrong status code. ", 200, result.getResponse().getStatus());


		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_GET_JSON_id_format_functionality() {

		try {
			
			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(get("/rest/applications/" + demo1.getApplication_id() + "?format=json")).secure(true)).andReturn();

			assertEquals("Wrong status code. ", 200, result.getResponse().getStatus());
			assertThat("Wrong content type. ", result.getResponse().getContentType(), containsString(MediaType.APPLICATION_JSON_VALUE));

			AssertionHelpers.assertResponseContainsText(result.getResponse(), resultJSON[0]);

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_GET_XML_id_format_functionality() {

		try {
			
			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(get("/rest/applications/" + demo1.getApplication_id() + "?format=xml")).secure(true)).andReturn();

			assertEquals("Wrong status code. ", 200, result.getResponse().getStatus());
			assertThat("Wrong content type. ", result.getResponse().getContentType(), containsString(MediaType.APPLICATION_XML_VALUE));

			AssertionHelpers.assertResponseContainsText(result.getResponse(), resultXML[0]);

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_GET_CSV_id_format_functionality() {

		try {
			
			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(get("/rest/applications/" + demo1.getApplication_id() + "?format=csv")).secure(true)).andReturn();

			assertEquals("Wrong status code. ", 200, result.getResponse().getStatus());
			assertThat("Wrong content type. ", result.getResponse().getContentType(), containsString("text/csv"));

			AssertionHelpers.assertResponseContainsText(result.getResponse(), resultCSV[0]);
			AssertionHelpers.assertResponseContainsText(result.getResponse(), resultCSV[1]);

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_GET_HTML_id_format_functionality() {

		try {
			
			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(get("/rest/applications/" + demo1.getApplication_id() + "?format=html")).secure(true)).andReturn();

			assertEquals("Wrong status code. ", 200, result.getResponse().getStatus());
			assertThat("Wrong content type. ", result.getResponse().getContentType(), containsString(MediaType.TEXT_HTML_VALUE));

			AssertionHelpers.assertResponseContainsText(result.getResponse(), resultHTML[0]);
			AssertionHelpers.assertResponseContainsText(result.getResponse(), resultHTML[1]);

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_GET_TEST_id_format_robustness() {

		try {
			
			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(get("/rest/applications/21" + "?format=xlsx")).secure(true)).andReturn();

			assertEquals("Wrong status code. ", 404, result.getResponse().getStatus());


		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_GET_JSON_id_accept_functionality() {

		try {
			
			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(get("/rest/applications/" + demo1.getApplication_id())).accept(MediaType.APPLICATION_JSON).secure(true)).andReturn();

			assertEquals("Wrong status code. ", 200, result.getResponse().getStatus());
			assertThat("Wrong content type. ", result.getResponse().getContentType(), containsString(MediaType.APPLICATION_JSON_VALUE));

			AssertionHelpers.assertResponseContainsText(result.getResponse(), resultJSON[0]);

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_GET_XML_id_accept_functionality() {

		try {
			
			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(get("/rest/applications/" + demo1.getApplication_id())).accept(MediaType.APPLICATION_XML).secure(true)).andReturn();

			assertEquals("Wrong status code. ", 200, result.getResponse().getStatus());
			assertThat("Wrong content type. ", result.getResponse().getContentType(), containsString(MediaType.APPLICATION_XML_VALUE));

			AssertionHelpers.assertResponseContainsText(result.getResponse(), resultXML[0]);

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_GET_CSV_id_accept_functionality() {

		try {
			
			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(get("/rest/applications/" + demo1.getApplication_id())).accept(MediaType.valueOf("text/csv")).secure(true)).andReturn();

			assertEquals("Wrong status code. ", 200, result.getResponse().getStatus());
			assertThat("Wrong content type. ", result.getResponse().getContentType(), containsString("text/csv"));

			AssertionHelpers.assertResponseContainsText(result.getResponse(), resultCSV[0]);
			AssertionHelpers.assertResponseContainsText(result.getResponse(), resultCSV[1]);

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_GET_HTML_id_accept_functionality() {

		try {
			
			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(get("/rest/applications/" + demo1.getApplication_id())).accept(MediaType.TEXT_HTML).secure(true)).andReturn();

			assertEquals("Wrong status code. ", 200, result.getResponse().getStatus());
			assertThat("Wrong content type. ", result.getResponse().getContentType(), containsString(MediaType.TEXT_HTML_VALUE));

			AssertionHelpers.assertResponseContainsText(result.getResponse(), resultHTML[0]);
			AssertionHelpers.assertResponseContainsText(result.getResponse(), resultHTML[1]);

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_GET_TEST_id_accept_robustness() {

		try {
			
			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(get("/rest/applications/21")).accept(MediaType.IMAGE_GIF).secure(true)).andReturn();

			assertEquals("Wrong status code. ", 404, result.getResponse().getStatus());


		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_GET_JSON_param_format_functionality() {

		try {
			
			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(get("/rest/applications?name=" + demo1.getApplication_name() + "&format=json")).secure(true)).andReturn();

			assertEquals("Wrong status code. ", 200, result.getResponse().getStatus());
			assertThat("Wrong content type. ", result.getResponse().getContentType(), containsString(MediaType.APPLICATION_JSON_VALUE));

			AssertionHelpers.assertResponseContainsText(result.getResponse(), resultJSON[0]);

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_GET_XML_param_format_functionality() {

		try {
			
			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(get("/rest/applications?name=" + demo1.getApplication_name() + "&format=xml")).secure(true)).andReturn();

			assertEquals("Wrong status code. ", 200, result.getResponse().getStatus());
			assertThat("Wrong content type. ", result.getResponse().getContentType(), containsString(MediaType.APPLICATION_XML_VALUE));

			AssertionHelpers.assertResponseContainsText(result.getResponse(), resultXML[0]);

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_GET_CSV_param_format_functionality() {

		try {
			
			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(get("/rest/applications?name=" + demo1.getApplication_name() + "&format=csv")).secure(true)).andReturn();

			assertEquals("Wrong status code. ", 200, result.getResponse().getStatus());
			assertThat("Wrong content type. ", result.getResponse().getContentType(), containsString("text/csv"));

			AssertionHelpers.assertResponseContainsText(result.getResponse(), resultCSV[0]);
			AssertionHelpers.assertResponseContainsText(result.getResponse(), resultCSV[1]);

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_GET_HTML_param_format_functionality() {

		try {
			
			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(get("/rest/applications?name=" + demo1.getApplication_name() + "&format=html")).secure(true)).andReturn();

			assertEquals("Wrong status code. ", 200, result.getResponse().getStatus());
			assertThat("Wrong content type. ", result.getResponse().getContentType(), containsString(MediaType.TEXT_HTML_VALUE));

			AssertionHelpers.assertResponseContainsText(result.getResponse(), resultHTML[0]);
			AssertionHelpers.assertResponseContainsText(result.getResponse(), resultHTML[1]);

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_GET_TEST_param_format_robustness() {

		try {
			
			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(get("/rest/applications?name=xtc" + "&format=xlsx")).secure(true)).andReturn();

			assertEquals("Wrong status code. ", 200, result.getResponse().getStatus());


		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_GET_JSON_param_accept_functionality() {

		try {
			
			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(get("/rest/applications?name=" + demo1.getApplication_name())).accept(MediaType.APPLICATION_JSON).secure(true)).andReturn();

			assertEquals("Wrong status code. ", 200, result.getResponse().getStatus());
			assertThat("Wrong content type. ", result.getResponse().getContentType(), containsString(MediaType.APPLICATION_JSON_VALUE));

			AssertionHelpers.assertResponseContainsText(result.getResponse(), resultJSON[0]);

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_GET_XML_param_accept_functionality() {

		try {
			
			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(get("/rest/applications?name=" + demo1.getApplication_name())).accept(MediaType.APPLICATION_XML).secure(true)).andReturn();

			assertEquals("Wrong status code. ", 200, result.getResponse().getStatus());
			assertThat("Wrong content type. ", result.getResponse().getContentType(), containsString(MediaType.APPLICATION_XML_VALUE));

			AssertionHelpers.assertResponseContainsText(result.getResponse(), resultXML[0]);

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_GET_CSV_param_accept_functionality() {

		try {
			
			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(get("/rest/applications?name=" + demo1.getApplication_name())).accept(MediaType.valueOf("text/csv")).secure(true)).andReturn();

			assertEquals("Wrong status code. ", 200, result.getResponse().getStatus());
			assertThat("Wrong content type. ", result.getResponse().getContentType(), containsString("text/csv"));

			AssertionHelpers.assertResponseContainsText(result.getResponse(), resultCSV[0]);
			AssertionHelpers.assertResponseContainsText(result.getResponse(), resultCSV[1]);

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_GET_HTML_param_accept_functionality() {

		try {
			
			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(get("/rest/applications?name=" + demo1.getApplication_name())).accept(MediaType.TEXT_HTML).secure(true)).andReturn();

			assertEquals("Wrong status code. ", 200, result.getResponse().getStatus());
			assertThat("Wrong content type. ", result.getResponse().getContentType(), containsString(MediaType.TEXT_HTML_VALUE));

			AssertionHelpers.assertResponseContainsText(result.getResponse(), resultHTML[0]);
			AssertionHelpers.assertResponseContainsText(result.getResponse(), resultHTML[1]);

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_GET_TEST_param_accept_robustness() {

		try {
			
			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(get("/rest/applications?name=xtc")).accept(MediaType.IMAGE_GIF).secure(true)).andReturn();

			assertEquals("Wrong status code. ", 200, result.getResponse().getStatus());


		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_POST_JSON_format_functionality() {

		try {

			String content = "{" + resultJSON[2].substring(resultJSON[2].indexOf(",")+1);
			
			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(post("/rest/applications")).secure(true).contentType(MediaType.APPLICATION_JSON).content(content)).andReturn();

			assertEquals("Wrong status code. ", 200, result.getResponse().getStatus());
			assertThat("Wrong content type. ", result.getResponse().getContentType(), containsString(MediaType.APPLICATION_JSON_VALUE));
			System.out.println("Response: " + result.getResponse().getContentAsString());

			assertTrue("Could not find expected id 3: ", repository.existsById(3));
			assertEquals("Could not find expected entry: ", demo3, repository.findById(3).orElse(null));

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_POST_JSON_format_robustness_1() {

		try {

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(post("/rest/applications").secure(true)).contentType(MediaType.APPLICATION_JSON).content("{\"application_id\":4}")).andReturn();

			assertEquals("Wrong status code. ", 400, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_POST_JSON_format_robustness_2() {

		try {

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(post("/rest/applications").secure(true)).contentType(MediaType.APPLICATION_JSON).content("")).andReturn();

			assertEquals("Wrong status code. ", 400, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_POST_XML_format_functionality() {

		try {

			String content = "<entity>" + resultXML[2].substring(resultXML[2].indexOf(">", resultXML[2].indexOf(">")+1)+1) + "</entity>";
			
			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(post("/rest/applications?format=xml")).secure(true).contentType(MediaType.APPLICATION_XML).content(content)).andReturn();

			assertEquals("Wrong status code. ", 200, result.getResponse().getStatus());
			assertThat("Wrong content type. ", result.getResponse().getContentType(), containsString(MediaType.APPLICATION_XML_VALUE));


			assertTrue("Could not find expected id 3: ", repository.existsById(3));
			assertEquals("Could not find expected entry: ", demo3, repository.findById(3).orElse(null));

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_POST_XML_format_robustness_1() {

		try {

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(post("/rest/applications").secure(true)).contentType(MediaType.APPLICATION_XML).content("<entity><application_id>4</application_id></entity>")).andReturn();

			assertEquals("Wrong status code. ", 400, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_POST_XML_format_robustness_2() {

		try {

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(post("/rest/applications").secure(true)).contentType(MediaType.APPLICATION_XML).content("")).andReturn();

			assertEquals("Wrong status code. ", 400, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_POST_CSV_format_functionality() {

		try {

			String content = resultCSV[0].substring(resultCSV[0].indexOf(",")+1) + "\n" + resultCSV[3].substring(resultCSV[3].indexOf(",")+1);
			
			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(post("/rest/applications")).secure(true).contentType(MediaType.valueOf("text/csv")).content(content)).andReturn();

			assertEquals("Wrong status code. ", 200, result.getResponse().getStatus());
			assertThat("Wrong content type. ", result.getResponse().getContentType(), containsString(MediaType.APPLICATION_JSON_VALUE));


			assertTrue("Could not find expected id 3: ", repository.existsById(3));
			assertEquals("Could not find expected entry: ", demo3, repository.findById(3).orElse(null));

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_POST_CSV_format_robustness_1() {

		try {

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(post("/rest/applications").secure(true)).contentType(MediaType.valueOf("text/csv")).content("application_id\n4")).andReturn();

			assertEquals("Wrong status code. ", 400, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_POST_CSV_format_robustness_2() {

		try {

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(post("/rest/applications").secure(true)).contentType(MediaType.valueOf("text/csv")).content("")).andReturn();

			assertEquals("Wrong status code. ", 400, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_POST_XFORM_format_functionality() {

		try {

			String content = resultXFORM[2].substring(resultXFORM[2].indexOf("&")+1);
			
			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(post("/rest/applications")).secure(true).contentType(MediaType.APPLICATION_FORM_URLENCODED).content(content)).andReturn();

			assertEquals("Wrong status code. ", 200, result.getResponse().getStatus());
			assertThat("Wrong content type. ", result.getResponse().getContentType(), containsString(MediaType.APPLICATION_JSON_VALUE));


			assertTrue("Could not find expected id 3: ", repository.existsById(3));
			assertEquals("Could not find expected entry: ", demo3, repository.findById(3).orElse(null));

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_POST_XFORM_format_robustness_1() {

		try {

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(post("/rest/applications").secure(true)).contentType(MediaType.APPLICATION_FORM_URLENCODED).content("application_id=4")).andReturn();

			assertEquals("Wrong status code. ", 400, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_POST_XFORM_format_robustness_2() {

		try {

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(post("/rest/applications").secure(true)).contentType(MediaType.APPLICATION_FORM_URLENCODED).content("")).andReturn();

			assertEquals("Wrong status code. ", 400, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PUT_JSON_format_functionality_1() {

		try {

			String content = resultJSON[2];

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(put("/rest/applications/")).secure(true).contentType(MediaType.APPLICATION_JSON)
					.content(content)).andReturn();

			assertEquals("Wrong status code. ", 201, result.getResponse().getStatus());
			assertThat("Wrong content type. ", result.getResponse().getContentType(), containsString(MediaType.APPLICATION_JSON_VALUE));

			assertTrue("Could not find expected id 3: ", repository.existsById(3));
			assertEquals("Could not find expected entry: ", demo3, repository.findById(3).orElse(null));

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PUT_JSON_format_robustness_1() {

		try {

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(put("/rest/applications")).secure(true).contentType(MediaType.APPLICATION_JSON).content("{\"application_id\":4}")).andReturn();

			assertEquals("Wrong status code. ", 400, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PUT_JSON_format_functionality_2() {

		try {

			String content = resultJSON[0];

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(put("/rest/applications/")).secure(true).contentType(MediaType.APPLICATION_JSON)
					.content(content)).andReturn();

			assertEquals("Wrong status code. ", 200, result.getResponse().getStatus());
			assertThat("Wrong content type. ", result.getResponse().getContentType(), containsString(MediaType.APPLICATION_JSON_VALUE));

			assertTrue("Could not find expected id 1: ", repository.existsById(1));
			assertEquals("Could not find expected entry: ", demo1, repository.findById(1).orElse(null));

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PUT_JSON_format_robustness_2() {

		try {

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(put("/rest/applications")).secure(true).contentType(MediaType.APPLICATION_JSON).content("")).andReturn();

			assertEquals("Wrong status code. ", 400, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PUT_XML_format_functionality_1() {

		try {

			String content = "<entity>" + resultXML[2] + "</entity>";

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(put("/rest/applications/?format=xml")).secure(true).contentType(MediaType.APPLICATION_XML)
					.content(content)).andReturn();

			assertEquals("Wrong status code. ", 201, result.getResponse().getStatus());
			assertThat("Wrong content type. ", result.getResponse().getContentType(), containsString(MediaType.APPLICATION_XML_VALUE));

			assertTrue("Could not find expected id 3: ", repository.existsById(3));
			assertEquals("Could not find expected entry: ", demo3, repository.findById(3).orElse(null));

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PUT_XML_format_robustness_1() {

		try {

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(put("/rest/applications")).secure(true).contentType(MediaType.APPLICATION_XML).content("<entity><application_id>4</application_id></entity>")).andReturn();

			assertEquals("Wrong status code. ", 400, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PUT_XML_format_functionality_2() {

		try {

			String content = "<entity>" + resultXML[0] + "</entity>";

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(put("/rest/applications/?format=xml")).secure(true).contentType(MediaType.APPLICATION_XML)
					.content(content)).andReturn();

			assertEquals("Wrong status code. ", 200, result.getResponse().getStatus());
			assertThat("Wrong content type. ", result.getResponse().getContentType(), containsString(MediaType.APPLICATION_XML_VALUE));

			assertTrue("Could not find expected id 1: ", repository.existsById(1));
			assertEquals("Could not find expected entry: ", demo1, repository.findById(1).orElse(null));

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PUT_XML_format_robustness_2() {

		try {

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(put("/rest/applications")).secure(true).contentType(MediaType.APPLICATION_XML).content("")).andReturn();

			assertEquals("Wrong status code. ", 400, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PUT_CSV_format_functionality_1() {

		try {

			String content = resultCSV[0] + "\n" + resultCSV[3];

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(put("/rest/applications/")).secure(true).contentType(MediaType.valueOf("text/csv"))
					.content(content)).andReturn();

			assertEquals("Wrong status code. ", 201, result.getResponse().getStatus());
			assertThat("Wrong content type. ", result.getResponse().getContentType(), containsString(MediaType.APPLICATION_JSON_VALUE));

			assertTrue("Could not find expected id 3: ", repository.existsById(3));
			assertEquals("Could not find expected entry: ", demo3, repository.findById(3).orElse(null));

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PUT_CSV_format_robustness_1() {

		try {

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(put("/rest/applications")).secure(true).contentType(MediaType.valueOf("text/csv")).content("application_id\n4")).andReturn();

			assertEquals("Wrong status code. ", 400, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PUT_CSV_format_functionality_2() {

		try {

			String content = resultCSV[0] + "\n" + resultCSV[1];

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(put("/rest/applications/")).secure(true).contentType(MediaType.valueOf("text/csv"))
					.content(content)).andReturn();

			assertEquals("Wrong status code. ", 200, result.getResponse().getStatus());
			assertThat("Wrong content type. ", result.getResponse().getContentType(), containsString(MediaType.APPLICATION_JSON_VALUE));

			assertTrue("Could not find expected id 1: ", repository.existsById(1));
			assertEquals("Could not find expected entry: ", demo1, repository.findById(1).orElse(null));

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PUT_CSV_format_robustness_2() {

		try {

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(put("/rest/applications")).secure(true).contentType(MediaType.valueOf("text/csv")).content("")).andReturn();

			assertEquals("Wrong status code. ", 400, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PUT_XFORM_format_functionality_1() {

		try {

			String content = resultXFORM[2];

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(put("/rest/applications/")).secure(true).contentType(MediaType.APPLICATION_FORM_URLENCODED)
					.content(content)).andReturn();

			assertEquals("Wrong status code. ", 201, result.getResponse().getStatus());
			assertThat("Wrong content type. ", result.getResponse().getContentType(), containsString(MediaType.APPLICATION_JSON_VALUE));

			assertTrue("Could not find expected id 3: ", repository.existsById(3));
			assertEquals("Could not find expected entry: ", demo3, repository.findById(3).orElse(null));

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PUT_XFORM_format_robustness_1() {

		try {

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(put("/rest/applications")).secure(true).contentType(MediaType.APPLICATION_FORM_URLENCODED).content("application_id=4")).andReturn();

			assertEquals("Wrong status code. ", 400, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PUT_XFORM_format_functionality_2() {

		try {

			String content = resultXFORM[0];

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(put("/rest/applications/")).secure(true).contentType(MediaType.APPLICATION_FORM_URLENCODED)
					.content(content)).andReturn();

			assertEquals("Wrong status code. ", 200, result.getResponse().getStatus());
			assertThat("Wrong content type. ", result.getResponse().getContentType(), containsString(MediaType.APPLICATION_JSON_VALUE));

			assertTrue("Could not find expected id 1: ", repository.existsById(1));
			assertEquals("Could not find expected entry: ", demo1, repository.findById(1).orElse(null));

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PUT_XFORM_format_robustness_2() {

		try {

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(put("/rest/applications")).secure(true).contentType(MediaType.APPLICATION_FORM_URLENCODED).content("")).andReturn();

			assertEquals("Wrong status code. ", 400, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PUT_JSON_id_format_functionality_1() {

		try {

			String content = "{" + resultJSON[2].substring(resultJSON[2].indexOf(",")+1);

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(put("/rest/applications/3")).secure(true).contentType(MediaType.APPLICATION_JSON)
					.content(content)).andReturn();

			assertEquals("Wrong status code. ", 201, result.getResponse().getStatus());
			assertThat("Wrong content type. ", result.getResponse().getContentType(), containsString(MediaType.APPLICATION_JSON_VALUE));

			assertTrue("Could not find expected id 3: ", repository.existsById(3));
			assertEquals("Could not find expected entry: ", demo3, repository.findById(3).orElse(null));

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PUT_JSON_id_format_robustness_1() {

		try {

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(put("/rest/applications")).secure(true).contentType(MediaType.APPLICATION_JSON).content("{\"application_id\":4}")).andReturn();

			assertEquals("Wrong status code. ", 400, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PUT_JSON_id_format_functionality_2() {

		try {

			String content = "{" + resultJSON[0].substring(resultJSON[0].indexOf(",")+1);

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(put("/rest/applications/1")).secure(true).contentType(MediaType.APPLICATION_JSON)
					.content(content)).andReturn();

			assertEquals("Wrong status code. ", 200, result.getResponse().getStatus());
			assertThat("Wrong content type. ", result.getResponse().getContentType(), containsString(MediaType.APPLICATION_JSON_VALUE));

			assertTrue("Could not find expected id 1: ", repository.existsById(1));
			assertEquals("Could not find expected entry: ", demo1, repository.findById(1).orElse(null));

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PUT_JSON_id_format_robustness_2() {

		try {

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(put("/rest/applications")).secure(true).contentType(MediaType.APPLICATION_JSON).content("")).andReturn();

			assertEquals("Wrong status code. ", 400, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PUT_XML_id_format_functionality_1() {

		try {

			String content = "<entity>" + resultXML[2].substring(resultXML[2].indexOf(">", resultXML[2].indexOf(">")+1)+1) + "</entity>";

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(put("/rest/applications/3?format=xml")).secure(true).contentType(MediaType.APPLICATION_XML)
					.content(content)).andReturn();

			assertEquals("Wrong status code. ", 201, result.getResponse().getStatus());
			assertThat("Wrong content type. ", result.getResponse().getContentType(), containsString(MediaType.APPLICATION_XML_VALUE));

			assertTrue("Could not find expected id 3: ", repository.existsById(3));
			assertEquals("Could not find expected entry: ", demo3, repository.findById(3).orElse(null));

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PUT_XML_id_format_robustness_1() {

		try {

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(put("/rest/applications")).secure(true).contentType(MediaType.APPLICATION_XML).content("<entity><application_id>4</application_id></entity>")).andReturn();

			assertEquals("Wrong status code. ", 400, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PUT_XML_id_format_functionality_2() {

		try {

			String content = "<entity>" + resultXML[0].substring(resultXML[0].indexOf(">", resultXML[0].indexOf(">")+1)+1) + "</entity>";

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(put("/rest/applications/1?format=xml")).secure(true).contentType(MediaType.APPLICATION_XML)
					.content(content)).andReturn();

			assertEquals("Wrong status code. ", 200, result.getResponse().getStatus());
			assertThat("Wrong content type. ", result.getResponse().getContentType(), containsString(MediaType.APPLICATION_XML_VALUE));

			assertTrue("Could not find expected id 1: ", repository.existsById(1));
			assertEquals("Could not find expected entry: ", demo1, repository.findById(1).orElse(null));

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PUT_XML_id_format_robustness_2() {

		try {

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(put("/rest/applications")).secure(true).contentType(MediaType.APPLICATION_XML).content("")).andReturn();

			assertEquals("Wrong status code. ", 400, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PUT_CSV_id_format_functionality_1() {

		try {

			String content = resultCSV[0].substring(resultCSV[0].indexOf(",")+1) + "\n" + resultCSV[3].substring(resultCSV[3].indexOf(",")+1);

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(put("/rest/applications/3")).secure(true).contentType(MediaType.valueOf("text/csv"))
					.content(content)).andReturn();

			assertEquals("Wrong status code. ", 201, result.getResponse().getStatus());
			assertThat("Wrong content type. ", result.getResponse().getContentType(), containsString(MediaType.APPLICATION_JSON_VALUE));

			assertTrue("Could not find expected id 3: ", repository.existsById(3));
			assertEquals("Could not find expected entry: ", demo3, repository.findById(3).orElse(null));

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PUT_CSV_id_format_robustness_1() {

		try {

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(put("/rest/applications")).secure(true).contentType(MediaType.valueOf("text/csv")).content("application_id\n4")).andReturn();

			assertEquals("Wrong status code. ", 400, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PUT_CSV_id_format_functionality_2() {

		try {

			String content = resultCSV[0].substring(resultCSV[0].indexOf(",")+1) + "\n" + resultCSV[1].substring(resultCSV[1].indexOf(",")+1);

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(put("/rest/applications/1")).secure(true).contentType(MediaType.valueOf("text/csv"))
					.content(content)).andReturn();

			assertEquals("Wrong status code. ", 200, result.getResponse().getStatus());
			assertThat("Wrong content type. ", result.getResponse().getContentType(), containsString(MediaType.APPLICATION_JSON_VALUE));

			assertTrue("Could not find expected id 1: ", repository.existsById(1));
			assertEquals("Could not find expected entry: ", demo1, repository.findById(1).orElse(null));

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PUT_CSV_id_format_robustness_2() {

		try {

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(put("/rest/applications")).secure(true).contentType(MediaType.valueOf("text/csv")).content("")).andReturn();

			assertEquals("Wrong status code. ", 400, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PUT_XFORM_id_format_functionality_1() {

		try {

			String content = resultXFORM[2].substring(resultXFORM[2].indexOf("&")+1);

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(put("/rest/applications/3")).secure(true).contentType(MediaType.APPLICATION_FORM_URLENCODED)
					.content(content)).andReturn();

			assertEquals("Wrong status code. ", 201, result.getResponse().getStatus());
			assertThat("Wrong content type. ", result.getResponse().getContentType(), containsString(MediaType.APPLICATION_JSON_VALUE));

			assertTrue("Could not find expected id 3: ", repository.existsById(3));
			assertEquals("Could not find expected entry: ", demo3, repository.findById(3).orElse(null));

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PUT_XFORM_id_format_robustness_1() {

		try {

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(put("/rest/applications")).secure(true).contentType(MediaType.APPLICATION_FORM_URLENCODED).content("application_id=4")).andReturn();

			assertEquals("Wrong status code. ", 400, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PUT_XFORM_id_format_functionality_2() {

		try {

			String content = resultXFORM[0].substring(resultXFORM[0].indexOf("&")+1);

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(put("/rest/applications/1")).secure(true).contentType(MediaType.APPLICATION_FORM_URLENCODED)
					.content(content)).andReturn();

			assertEquals("Wrong status code. ", 200, result.getResponse().getStatus());
			assertThat("Wrong content type. ", result.getResponse().getContentType(), containsString(MediaType.APPLICATION_JSON_VALUE));

			assertTrue("Could not find expected id 1: ", repository.existsById(1));
			assertEquals("Could not find expected entry: ", demo1, repository.findById(1).orElse(null));

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PUT_XFORM_id_format_robustness_2() {

		try {

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(put("/rest/applications")).secure(true).contentType(MediaType.APPLICATION_FORM_URLENCODED).content("")).andReturn();

			assertEquals("Wrong status code. ", 400, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PATCH_JSON_format_functionality_1() {

		try {

			String content = resultJSON[0];

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(patch("/rest/applications/")).secure(true).contentType(MediaType.APPLICATION_JSON)
					.content(content)).andReturn();

			assertEquals("Wrong status code. ", 200, result.getResponse().getStatus());
							
			assertThat("Wrong content type. ", result.getResponse().getContentType(), containsString(MediaType.APPLICATION_JSON_VALUE));

			assertTrue("Could not find expected id 1: ", repository.existsById(1));
			assertEquals("Could not find expected entry: ", demo1, repository.findById(1).orElse(null));

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PATCH_JSON_format_robustness_1() {

		try {

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(patch("/rest/applications")).secure(true).contentType(MediaType.APPLICATION_JSON).content("{\"application_id\":4}")).andReturn();

			assertEquals("Wrong status code. ", 404, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PATCH_JSON_format_robustness_2() {

		try {

			String content = resultJSON[2];

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(patch("/rest/applications/")).secure(true).contentType(MediaType.APPLICATION_JSON)
					.content(content)).andReturn();

			assertEquals("Wrong status code. ", 404, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PATCH_JSON_format_robustness_3() {

		try {

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(patch("/rest/applications")).secure(true).contentType(MediaType.APPLICATION_JSON).content("")).andReturn();

			assertEquals("Wrong status code. ", 400, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PATCH_XML_format_functionality_1() {

		try {

			String content = "<entity>" + resultXML[0] + "</entity>";

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(patch("/rest/applications/?format=xml")).secure(true).contentType(MediaType.APPLICATION_XML)
					.content(content)).andReturn();

			assertEquals("Wrong status code. ", 200, result.getResponse().getStatus());
							
			assertThat("Wrong content type. ", result.getResponse().getContentType(), containsString(MediaType.APPLICATION_XML_VALUE));

			assertTrue("Could not find expected id 1: ", repository.existsById(1));
			assertEquals("Could not find expected entry: ", demo1, repository.findById(1).orElse(null));

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PATCH_XML_format_robustness_1() {

		try {

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(patch("/rest/applications")).secure(true).contentType(MediaType.APPLICATION_XML).content("<entity><application_id>4</application_id></entity>")).andReturn();

			assertEquals("Wrong status code. ", 404, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PATCH_XML_format_robustness_2() {

		try {

			String content = "<entity>" + resultXML[2] + "</entity>";

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(patch("/rest/applications/?format=xml")).secure(true).contentType(MediaType.APPLICATION_XML)
					.content(content)).andReturn();

			assertEquals("Wrong status code. ", 404, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PATCH_XML_format_robustness_3() {

		try {

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(patch("/rest/applications")).secure(true).contentType(MediaType.APPLICATION_XML).content("")).andReturn();

			assertEquals("Wrong status code. ", 400, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PATCH_CSV_format_functionality_1() {

		try {

			String content = resultCSV[0] + "\n" + resultCSV[1];

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(patch("/rest/applications/")).secure(true).contentType(MediaType.valueOf("text/csv"))
					.content(content)).andReturn();

			assertEquals("Wrong status code. ", 200, result.getResponse().getStatus());
							
			assertThat("Wrong content type. ", result.getResponse().getContentType(), containsString(MediaType.APPLICATION_JSON_VALUE));

			assertTrue("Could not find expected id 1: ", repository.existsById(1));
			assertEquals("Could not find expected entry: ", demo1, repository.findById(1).orElse(null));

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PATCH_CSV_format_robustness_1() {

		try {

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(patch("/rest/applications")).secure(true).contentType(MediaType.valueOf("text/csv")).content("application_id\n4")).andReturn();

			assertEquals("Wrong status code. ", 400, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PATCH_CSV_format_robustness_2() {

		try {

			String content = resultCSV[0] + "\n" + resultCSV[3];

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(patch("/rest/applications/")).secure(true).contentType(MediaType.valueOf("text/csv"))
					.content(content)).andReturn();

			assertEquals("Wrong status code. ", 404, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PATCH_CSV_format_robustness_3() {

		try {

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(patch("/rest/applications")).secure(true).contentType(MediaType.valueOf("text/csv")).content("")).andReturn();

			assertEquals("Wrong status code. ", 400, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PATCH_XFORM_format_functionality_1() {

		try {

			String content = resultXFORM[0];

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(patch("/rest/applications/")).secure(true).contentType(MediaType.APPLICATION_FORM_URLENCODED)
					.content(content)).andReturn();

			assertEquals("Wrong status code. ", 200, result.getResponse().getStatus());
							
			assertThat("Wrong content type. ", result.getResponse().getContentType(), containsString(MediaType.APPLICATION_JSON_VALUE));

			assertTrue("Could not find expected id 1: ", repository.existsById(1));
			assertEquals("Could not find expected entry: ", demo1, repository.findById(1).orElse(null));

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PATCH_XFORM_format_robustness_1() {

		try {

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(patch("/rest/applications")).secure(true).contentType(MediaType.APPLICATION_FORM_URLENCODED).content("application_id=4")).andReturn();

			assertEquals("Wrong status code. ", 404, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PATCH_XFORM_format_robustness_2() {

		try {

			String content = resultXFORM[2];

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(patch("/rest/applications/")).secure(true).contentType(MediaType.APPLICATION_FORM_URLENCODED)
					.content(content)).andReturn();

			assertEquals("Wrong status code. ", 404, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PATCH_XFORM_format_robustness_3() {

		try {

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(patch("/rest/applications")).secure(true).contentType(MediaType.APPLICATION_FORM_URLENCODED).content("")).andReturn();

			assertEquals("Wrong status code. ", 404, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PATCH_JSON_id_format_functionality_1() {

		try {

			String content = "{" + resultJSON[0].substring(resultJSON[0].indexOf(",")+1);

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(patch("/rest/applications/1")).secure(true).contentType(MediaType.APPLICATION_JSON)
					.content(content)).andReturn();

			assertEquals("Wrong status code. ", 200, result.getResponse().getStatus());
							
			assertThat("Wrong content type. ", result.getResponse().getContentType(), containsString(MediaType.APPLICATION_JSON_VALUE));

			assertTrue("Could not find expected id 1: ", repository.existsById(1));
			assertEquals("Could not find expected entry: ", demo1, repository.findById(1).orElse(null));

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PATCH_JSON_id_format_robustness_1() {

		try {

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(patch("/rest/applications")).secure(true).contentType(MediaType.APPLICATION_JSON).content("{\"application_id\":4}")).andReturn();

			assertEquals("Wrong status code. ", 404, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PATCH_JSON_id_format_robustness_2() {

		try {

			String content = "{" + resultJSON[2].substring(resultJSON[2].indexOf(",")+1);

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(patch("/rest/applications/3")).secure(true).contentType(MediaType.APPLICATION_JSON)
					.content(content)).andReturn();

			assertEquals("Wrong status code. ", 404, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PATCH_JSON_id_format_robustness_3() {

		try {

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(patch("/rest/applications")).secure(true).contentType(MediaType.APPLICATION_JSON).content("")).andReturn();

			assertEquals("Wrong status code. ", 400, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PATCH_XML_id_format_functionality_1() {

		try {

			String content = "<entity>" + resultXML[0].substring(resultXML[0].indexOf(">", resultXML[0].indexOf(">")+1)+1) + "</entity>";

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(patch("/rest/applications/1?format=xml")).secure(true).contentType(MediaType.APPLICATION_XML)
					.content(content)).andReturn();

			assertEquals("Wrong status code. ", 200, result.getResponse().getStatus());
							
			assertThat("Wrong content type. ", result.getResponse().getContentType(), containsString(MediaType.APPLICATION_XML_VALUE));

			assertTrue("Could not find expected id 1: ", repository.existsById(1));
			assertEquals("Could not find expected entry: ", demo1, repository.findById(1).orElse(null));

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PATCH_XML_id_format_robustness_1() {

		try {

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(patch("/rest/applications")).secure(true).contentType(MediaType.APPLICATION_XML).content("<entity><application_id>4</application_id></entity>")).andReturn();

			assertEquals("Wrong status code. ", 404, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PATCH_XML_id_format_robustness_2() {

		try {

			String content = "<entity>" + resultXML[2].substring(resultXML[2].indexOf(">", resultXML[2].indexOf(">")+1)+1) + "</entity>";

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(patch("/rest/applications/3?format=xml")).secure(true).contentType(MediaType.APPLICATION_XML)
					.content(content)).andReturn();

			assertEquals("Wrong status code. ", 404, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PATCH_XML_id_format_robustness_3() {

		try {

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(patch("/rest/applications")).secure(true).contentType(MediaType.APPLICATION_XML).content("")).andReturn();

			assertEquals("Wrong status code. ", 400, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PATCH_CSV_id_format_functionality_1() {

		try {

			String content = resultCSV[0].substring(resultCSV[0].indexOf(",")+1) + "\n" + resultCSV[1].substring(resultCSV[1].indexOf(",")+1);

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(patch("/rest/applications/1")).secure(true).contentType(MediaType.valueOf("text/csv"))
					.content(content)).andReturn();

			assertEquals("Wrong status code. ", 200, result.getResponse().getStatus());
							
			assertThat("Wrong content type. ", result.getResponse().getContentType(), containsString(MediaType.APPLICATION_JSON_VALUE));

			assertTrue("Could not find expected id 1: ", repository.existsById(1));
			assertEquals("Could not find expected entry: ", demo1, repository.findById(1).orElse(null));

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PATCH_CSV_id_format_robustness_1() {

		try {

			MvcResult result = mvc.perform(
					AccessTokens.withValidHeaders(patch("/rest/applications"))
							.secure(true)
							.contentType(MediaType.valueOf("text/csv"))
							.content("application_id\n4"))
					.andReturn();

			assertEquals("Wrong status code. ", 400, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PATCH_CSV_id_format_robustness_2() {

		try {

			String content = resultCSV[0].substring(resultCSV[0].indexOf(",")+1) + "\n" + resultCSV[3].substring(resultCSV[3].indexOf(",")+1);

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(patch("/rest/applications/3")).secure(true).contentType(MediaType.valueOf("text/csv"))
					.content(content)).andReturn();

			assertEquals("Wrong status code. ", 404, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PATCH_CSV_id_format_robustness_3() {

		try {

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(patch("/rest/applications")).secure(true).contentType(MediaType.valueOf("text/csv")).content("")).andReturn();

			assertEquals("Wrong status code. ", 400, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PATCH_XFORM_id_format_functionality_1() {

		try {

			String content = resultXFORM[0].substring(resultXFORM[0].indexOf("&")+1);

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(patch("/rest/applications/1")).secure(true).contentType(MediaType.APPLICATION_FORM_URLENCODED)
					.content(content)).andReturn();

			assertEquals("Wrong status code. ", 200, result.getResponse().getStatus());
							
			assertThat("Wrong content type. ", result.getResponse().getContentType(), containsString(MediaType.APPLICATION_JSON_VALUE));

			assertTrue("Could not find expected id 1: ", repository.existsById(1));
			assertEquals("Could not find expected entry: ", demo1, repository.findById(1).orElse(null));

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PATCH_XFORM_id_format_robustness_1() {

		try {

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(patch("/rest/applications")).secure(true).contentType(MediaType.APPLICATION_FORM_URLENCODED).content("application_id=4")).andReturn();

			assertEquals("Wrong status code. ", 404, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PATCH_XFORM_id_format_robustness_2() {

		try {

			String content = resultXFORM[2].substring(resultXFORM[2].indexOf("&")+1);

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(patch("/rest/applications/3")).secure(true).contentType(MediaType.APPLICATION_FORM_URLENCODED)
					.content(content)).andReturn();

			assertEquals("Wrong status code. ", 404, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PATCH_XFORM_id_format_robustness_3() {

		try {

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(patch("/rest/applications")).secure(true).contentType(MediaType.APPLICATION_FORM_URLENCODED).content("")).andReturn();

			assertEquals("Wrong status code. ", 404, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_DELETE_JSON_format_functionality() {

		try {

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(delete("/rest/applications/1")).secure(true)).andReturn();

			assertEquals("Wrong status code. ", 204, result.getResponse().getStatus());
			assertFalse("Content was not deleted. ", repository.existsById(1));
			assertTrue("Wrong content was deleted. ", repository.existsById(2));

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_DELETE_JSON_format_robustness() {

		try {
			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(delete("/rest/applications/19")).secure(true)).andReturn();

			assertEquals("Wrong status code. ", 500, result.getResponse().getStatus());
			assertTrue("Wrong content was deleted. ", repository.existsById(1));
			assertTrue("Wrong content was deleted. ", repository.existsById(2));

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}
}
