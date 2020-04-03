package com.atopion.UGC_repository.rest;

import com.atopion.UGC_repository.entities.ContentCommentsEntity;
import com.atopion.UGC_repository.repositories.ContentCommentsRepository;
import com.atopion.UGC_repository.testutil.AccessTokens;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.Statement;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("classpath:application-integrationtests.properties")
public class ContentCommentsControllerTest {

	private final String[] resultJSON  = new String[]{"{\"comment_id\":1,\"comment_text\":\"demo_text1\",\"comment_created\":\"2001-01-01 00:00:00\",\"application_id\":1,\"record_id\":1,\"user_token\":\"demo_token1\"}", "{\"comment_id\":2,\"comment_text\":\"demo_text2\",\"comment_created\":\"2002-01-01 00:00:00\",\"application_id\":2,\"record_id\":2,\"user_token\":\"demo_token2\"}", "{\"comment_id\":3,\"comment_text\":\"demo_text3\",\"comment_created\":\"2003-01-01 00:00:00\",\"application_id\":3,\"record_id\":3,\"user_token\":\"demo_token3\"}"};
	private final String[] resultXML   = new String[]{"<comment_id>1</comment_id><comment_text>demo_text1</comment_text><comment_created>2001-01-01 00:00:00</comment_created><application_id>1</application_id><record_id>1</record_id><user_token>demo_token1</user_token>", "<comment_id>2</comment_id><comment_text>demo_text2</comment_text><comment_created>2002-01-01 00:00:00</comment_created><application_id>2</application_id><record_id>2</record_id><user_token>demo_token2</user_token>", "<comment_id>3</comment_id><comment_text>demo_text3</comment_text><comment_created>2003-01-01 00:00:00</comment_created><application_id>3</application_id><record_id>3</record_id><user_token>demo_token3</user_token>"};
	private final String[] resultCSV   = new String[]{"comment_id,comment_text,comment_created,application_id,record_id,user_token", "1,demo_text1,2001-01-01 00:00:00,1,1,demo_token1", "2,demo_text2,2002-01-01 00:00:00,2,2,demo_token2", "3,demo_text3,2003-01-01 00:00:00,3,3,demo_token3"};
	private final String[] resultHTML  = new String[]{"<tr>\n<th>comment_id</th>\n<th>comment_text</th>\n<th>comment_created</th>\n<th>application_id</th>\n<th>record_id</th>\n<th>user_token</th>\n</tr>", "<tr>\n<td>1</td>\n<td>demo_text1</td>\n<td>2001-01-01 00:00:00</td>\n<td>1</td>\n<td>1</td>\n<td>demo_token1</td>\n</tr>", "<tr>\n<td>2</td>\n<td>demo_text2</td>\n<td>2002-01-01 00:00:00</td>\n<td>2</td>\n<td>2</td>\n<td>demo_token2</td>\n</tr>", "<tr>\n<td>3</td>\n<td>demo_text3</td>\n<td>2003-01-01 00:00:00</td>\n<td>3</td>\n<td>3</td>\n<td>demo_token3</td>\n</tr>"};
	private final String[] resultXFORM = new String[]{"comment_id=1&comment_text=demo_text1&comment_created=2001-01-01 00:00:00&application_id=1&record_id=1&user_token=demo_token1", "comment_id=2&comment_text=demo_text2&comment_created=2002-01-01 00:00:00&application_id=2&record_id=2&user_token=demo_token2", "comment_id=3&comment_text=demo_text3&comment_created=2003-01-01 00:00:00&application_id=3&record_id=3&user_token=demo_token3"};

	@Autowired
	private MockMvc mvc;

	@Autowired
	private ContentCommentsRepository repository;

	@Autowired
	private DataSource dataSource;

	private ContentCommentsEntity demo1, demo3;

	private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@BeforeEach
	void setUp() {
		TimeZone.setDefault(TimeZone.getTimeZone("Europe/Berlin"));

		try {
			demo1 = new ContentCommentsEntity("demo_text1", format.parse("2001-01-01 00:00:00"), 1, 1, "demo_token1");
			ContentCommentsEntity demo2 = new ContentCommentsEntity("demo_text2", format.parse("2002-01-01 00:00:00"), 2, 2, "demo_token2");
		
			demo3 = new ContentCommentsEntity(3, "demo_text3", format.parse("2003-01-01 00:00:00"), 3, 3, "demo_token3");

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

		try (Connection connection = dataSource.getConnection();
			Statement statement = connection.createStatement()) {
			statement.executeUpdate("ALTER TABLE content_comments ALTER COLUMN comment_id RESTART WITH 1;");

		} catch (Exception e) {
			fail("Unexpected exception occured: " + e.getMessage());
		}
	}

	@Test
	void test_GET_JSON_format_functionality() {

		try {
			
			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(get("/rest/contentComments?format=json")).secure(true)).andReturn();

			assertEquals("Wrong status code. ", 200, result.getResponse().getStatus());
			assertThat("Wrong content type. ", result.getResponse().getContentType(), containsString(MediaType.APPLICATION_JSON_VALUE));

			assertThat("Could not find expected content. ", result.getResponse().getContentAsString().replace(" ", "").replace("\t", ""), containsString(resultJSON[0].replace(" ", "").replace("	", "")));
			assertThat("Could not find expected content. ", result.getResponse().getContentAsString().replace(" ", "").replace("\t", ""), containsString(resultJSON[1].replace(" ", "").replace("	", "")));

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_GET_XML_format_functionality() {

		try {
			
			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(get("/rest/contentComments?format=xml")).secure(true)).andReturn();

			assertEquals("Wrong status code. ", 200, result.getResponse().getStatus());
			assertThat("Wrong content type. ", result.getResponse().getContentType(), containsString(MediaType.APPLICATION_XML_VALUE));

			assertThat("Could not find expected content. ", result.getResponse().getContentAsString().replace(" ", "").replace("\t", ""), containsString(resultXML[0].replace(" ", "").replace("	", "")));
			assertThat("Could not find expected content. ", result.getResponse().getContentAsString().replace(" ", "").replace("\t", ""), containsString(resultXML[1].replace(" ", "").replace("	", "")));

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_GET_CSV_format_functionality() {

		try {
			
			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(get("/rest/contentComments?format=csv")).secure(true)).andReturn();

			assertEquals("Wrong status code. ", 200, result.getResponse().getStatus());
			assertThat("Wrong content type. ", result.getResponse().getContentType(), containsString("text/csv"));

			assertThat("Could not find expected content. ", result.getResponse().getContentAsString().replace(" ", "").replace("\t", ""), containsString(resultCSV[0].replace(" ", "").replace("	", "")));
			assertThat("Could not find expected content. ", result.getResponse().getContentAsString().replace(" ", "").replace("\t", ""), containsString(resultCSV[1].replace(" ", "").replace("	", "")));
			assertThat("Could not find expected content. ", result.getResponse().getContentAsString().replace(" ", "").replace("\t", ""), containsString(resultCSV[2].replace(" ", "").replace("	", "")));

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_GET_HTML_format_functionality() {

		try {
			
			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(get("/rest/contentComments?format=html")).secure(true)).andReturn();

			assertEquals("Wrong status code. ", 200, result.getResponse().getStatus());
			assertThat("Wrong content type. ", result.getResponse().getContentType(), containsString(MediaType.TEXT_HTML_VALUE));

			assertThat("Could not find expected content. ", result.getResponse().getContentAsString().replace(" ", "").replace("\t", ""), containsString(resultHTML[0].replace(" ", "").replace("	", "")));
			assertThat("Could not find expected content. ", result.getResponse().getContentAsString().replace(" ", "").replace("\t", ""), containsString(resultHTML[1].replace(" ", "").replace("	", "")));
			assertThat("Could not find expected content. ", result.getResponse().getContentAsString().replace(" ", "").replace("\t", ""), containsString(resultHTML[2].replace(" ", "").replace("	", "")));

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_GET_TEST_format_robustness() {

		try {
			
			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(get("/rest/contentComments?format=xlsx")).secure(true)).andReturn();

			assertEquals("Wrong status code. ", 200, result.getResponse().getStatus());


		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_GET_JSON_accept_functionality() {

		try {
			
			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(get("/rest/contentComments")).accept(MediaType.APPLICATION_JSON).secure(true)).andReturn();

			assertEquals("Wrong status code. ", 200, result.getResponse().getStatus());
			assertThat("Wrong content type. ", result.getResponse().getContentType(), containsString(MediaType.APPLICATION_JSON_VALUE));

			assertThat("Could not find expected content. ", result.getResponse().getContentAsString().replace(" ", "").replace("\t", ""), containsString(resultJSON[0].replace(" ", "").replace("	", "")));
			assertThat("Could not find expected content. ", result.getResponse().getContentAsString().replace(" ", "").replace("\t", ""), containsString(resultJSON[1].replace(" ", "").replace("	", "")));

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_GET_XML_accept_functionality() {

		try {
			
			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(get("/rest/contentComments")).accept(MediaType.APPLICATION_XML).secure(true)).andReturn();

			assertEquals("Wrong status code. ", 200, result.getResponse().getStatus());
			assertThat("Wrong content type. ", result.getResponse().getContentType(), containsString(MediaType.APPLICATION_XML_VALUE));

			assertThat("Could not find expected content. ", result.getResponse().getContentAsString().replace(" ", "").replace("\t", ""), containsString(resultXML[0].replace(" ", "").replace("	", "")));
			assertThat("Could not find expected content. ", result.getResponse().getContentAsString().replace(" ", "").replace("\t", ""), containsString(resultXML[1].replace(" ", "").replace("	", "")));

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_GET_CSV_accept_functionality() {

		try {
			
			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(get("/rest/contentComments")).accept(MediaType.valueOf("text/csv")).secure(true)).andReturn();

			assertEquals("Wrong status code. ", 200, result.getResponse().getStatus());
			assertThat("Wrong content type. ", result.getResponse().getContentType(), containsString("text/csv"));

			assertThat("Could not find expected content. ", result.getResponse().getContentAsString().replace(" ", "").replace("\t", ""), containsString(resultCSV[0].replace(" ", "").replace("	", "")));
			assertThat("Could not find expected content. ", result.getResponse().getContentAsString().replace(" ", "").replace("\t", ""), containsString(resultCSV[1].replace(" ", "").replace("	", "")));
			assertThat("Could not find expected content. ", result.getResponse().getContentAsString().replace(" ", "").replace("\t", ""), containsString(resultCSV[2].replace(" ", "").replace("	", "")));

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_GET_HTML_accept_functionality() {

		try {
			
			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(get("/rest/contentComments")).accept(MediaType.TEXT_HTML).secure(true)).andReturn();

			assertEquals("Wrong status code. ", 200, result.getResponse().getStatus());
			assertThat("Wrong content type. ", result.getResponse().getContentType(), containsString(MediaType.TEXT_HTML_VALUE));

			assertThat("Could not find expected content. ", result.getResponse().getContentAsString().replace(" ", "").replace("\t", ""), containsString(resultHTML[0].replace(" ", "").replace("	", "")));
			assertThat("Could not find expected content. ", result.getResponse().getContentAsString().replace(" ", "").replace("\t", ""), containsString(resultHTML[1].replace(" ", "").replace("	", "")));
			assertThat("Could not find expected content. ", result.getResponse().getContentAsString().replace(" ", "").replace("\t", ""), containsString(resultHTML[2].replace(" ", "").replace("	", "")));

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_GET_TEST_accept_robustness() {

		try {
			
			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(get("/rest/contentComments")).accept(MediaType.IMAGE_GIF).secure(true)).andReturn();

			assertEquals("Wrong status code. ", 200, result.getResponse().getStatus());


		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_GET_JSON_id_format_functionality() {

		try {
			
			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(get("/rest/contentComments/" + demo1.getComment_id() + "?format=json")).secure(true)).andReturn();

			assertEquals("Wrong status code. ", 200, result.getResponse().getStatus());
			assertThat("Wrong content type. ", result.getResponse().getContentType(), containsString(MediaType.APPLICATION_JSON_VALUE));

			assertThat("Could not find expected content. ", result.getResponse().getContentAsString().replace(" ", "").replace("\t", ""), containsString(resultJSON[0].replace(" ", "").replace("	", "")));

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_GET_XML_id_format_functionality() {

		try {
			
			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(get("/rest/contentComments/" + demo1.getComment_id() + "?format=xml")).secure(true)).andReturn();

			assertEquals("Wrong status code. ", 200, result.getResponse().getStatus());
			assertThat("Wrong content type. ", result.getResponse().getContentType(), containsString(MediaType.APPLICATION_XML_VALUE));

			assertThat("Could not find expected content. ", result.getResponse().getContentAsString().replace(" ", "").replace("\t", ""), containsString(resultXML[0].replace(" ", "").replace("	", "")));

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_GET_CSV_id_format_functionality() {

		try {
			
			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(get("/rest/contentComments/" + demo1.getComment_id() + "?format=csv")).secure(true)).andReturn();

			assertEquals("Wrong status code. ", 200, result.getResponse().getStatus());
			assertThat("Wrong content type. ", result.getResponse().getContentType(), containsString("text/csv"));

			assertThat("Could not find expected content. ", result.getResponse().getContentAsString().replace(" ", "").replace("\t", ""), containsString(resultCSV[0].replace(" ", "").replace("	", "")));
			assertThat("Could not find expected content. ", result.getResponse().getContentAsString().replace(" ", "").replace("\t", ""), containsString(resultCSV[1].replace(" ", "").replace("	", "")));

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_GET_HTML_id_format_functionality() {

		try {
			
			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(get("/rest/contentComments/" + demo1.getComment_id() + "?format=html")).secure(true)).andReturn();

			assertEquals("Wrong status code. ", 200, result.getResponse().getStatus());
			assertThat("Wrong content type. ", result.getResponse().getContentType(), containsString(MediaType.TEXT_HTML_VALUE));

			assertThat("Could not find expected content. ", result.getResponse().getContentAsString().replace(" ", "").replace("\t", ""), containsString(resultHTML[0].replace(" ", "").replace("	", "")));
			assertThat("Could not find expected content. ", result.getResponse().getContentAsString().replace(" ", "").replace("\t", ""), containsString(resultHTML[1].replace(" ", "").replace("	", "")));

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_GET_TEST_id_format_robustness() {

		try {
			
			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(get("/rest/contentComments/21" + "?format=xlsx")).secure(true)).andReturn();

			assertEquals("Wrong status code. ", 404, result.getResponse().getStatus());


		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_GET_JSON_id_accept_functionality() {

		try {
			
			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(get("/rest/contentComments/" + demo1.getComment_id())).accept(MediaType.APPLICATION_JSON).secure(true)).andReturn();

			assertEquals("Wrong status code. ", 200, result.getResponse().getStatus());
			assertThat("Wrong content type. ", result.getResponse().getContentType(), containsString(MediaType.APPLICATION_JSON_VALUE));

			assertThat("Could not find expected content. ", result.getResponse().getContentAsString().replace(" ", "").replace("\t", ""), containsString(resultJSON[0].replace(" ", "").replace("	", "")));

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_GET_XML_id_accept_functionality() {

		try {
			
			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(get("/rest/contentComments/" + demo1.getComment_id())).accept(MediaType.APPLICATION_XML).secure(true)).andReturn();

			assertEquals("Wrong status code. ", 200, result.getResponse().getStatus());
			assertThat("Wrong content type. ", result.getResponse().getContentType(), containsString(MediaType.APPLICATION_XML_VALUE));

			assertThat("Could not find expected content. ", result.getResponse().getContentAsString().replace(" ", "").replace("\t", ""), containsString(resultXML[0].replace(" ", "").replace("	", "")));

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_GET_CSV_id_accept_functionality() {

		try {
			
			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(get("/rest/contentComments/" + demo1.getComment_id())).accept(MediaType.valueOf("text/csv")).secure(true)).andReturn();

			assertEquals("Wrong status code. ", 200, result.getResponse().getStatus());
			assertThat("Wrong content type. ", result.getResponse().getContentType(), containsString("text/csv"));

			assertThat("Could not find expected content. ", result.getResponse().getContentAsString().replace(" ", "").replace("\t", ""), containsString(resultCSV[0].replace(" ", "").replace("	", "")));
			assertThat("Could not find expected content. ", result.getResponse().getContentAsString().replace(" ", "").replace("\t", ""), containsString(resultCSV[1].replace(" ", "").replace("	", "")));

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_GET_HTML_id_accept_functionality() {

		try {
			
			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(get("/rest/contentComments/" + demo1.getComment_id())).accept(MediaType.TEXT_HTML).secure(true)).andReturn();

			assertEquals("Wrong status code. ", 200, result.getResponse().getStatus());
			assertThat("Wrong content type. ", result.getResponse().getContentType(), containsString(MediaType.TEXT_HTML_VALUE));

			assertThat("Could not find expected content. ", result.getResponse().getContentAsString().replace(" ", "").replace("\t", ""), containsString(resultHTML[0].replace(" ", "").replace("	", "")));
			assertThat("Could not find expected content. ", result.getResponse().getContentAsString().replace(" ", "").replace("\t", ""), containsString(resultHTML[1].replace(" ", "").replace("	", "")));

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_GET_TEST_id_accept_robustness() {

		try {
			
			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(get("/rest/contentComments/21")).accept(MediaType.IMAGE_GIF).secure(true)).andReturn();

			assertEquals("Wrong status code. ", 404, result.getResponse().getStatus());


		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_GET_JSON_param_format_functionality() {

		try {
			
			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(get("/rest/contentComments?text=" + demo1.getComment_text() + "&format=json")).secure(true)).andReturn();

			assertEquals("Wrong status code. ", 200, result.getResponse().getStatus());
			assertThat("Wrong content type. ", result.getResponse().getContentType(), containsString(MediaType.APPLICATION_JSON_VALUE));

			assertThat("Could not find expected content. ", result.getResponse().getContentAsString().replace(" ", "").replace("\t", ""), containsString(resultJSON[0].replace(" ", "").replace("	", "")));

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_GET_XML_param_format_functionality() {

		try {
			
			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(get("/rest/contentComments?text=" + demo1.getComment_text() + "&format=xml")).secure(true)).andReturn();

			assertEquals("Wrong status code. ", 200, result.getResponse().getStatus());
			assertThat("Wrong content type. ", result.getResponse().getContentType(), containsString(MediaType.APPLICATION_XML_VALUE));

			assertThat("Could not find expected content. ", result.getResponse().getContentAsString().replace(" ", "").replace("\t", ""), containsString(resultXML[0].replace(" ", "").replace("	", "")));

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_GET_CSV_param_format_functionality() {

		try {
			
			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(get("/rest/contentComments?text=" + demo1.getComment_text() + "&format=csv")).secure(true)).andReturn();

			assertEquals("Wrong status code. ", 200, result.getResponse().getStatus());
			assertThat("Wrong content type. ", result.getResponse().getContentType(), containsString("text/csv"));

			assertThat("Could not find expected content. ", result.getResponse().getContentAsString().replace(" ", "").replace("\t", ""), containsString(resultCSV[0].replace(" ", "").replace("	", "")));
			assertThat("Could not find expected content. ", result.getResponse().getContentAsString().replace(" ", "").replace("\t", ""), containsString(resultCSV[1].replace(" ", "").replace("	", "")));

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_GET_HTML_param_format_functionality() {

		try {
			
			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(get("/rest/contentComments?text=" + demo1.getComment_text() + "&format=html")).secure(true)).andReturn();

			assertEquals("Wrong status code. ", 200, result.getResponse().getStatus());
			assertThat("Wrong content type. ", result.getResponse().getContentType(), containsString(MediaType.TEXT_HTML_VALUE));

			assertThat("Could not find expected content. ", result.getResponse().getContentAsString().replace(" ", "").replace("\t", ""), containsString(resultHTML[0].replace(" ", "").replace("	", "")));
			assertThat("Could not find expected content. ", result.getResponse().getContentAsString().replace(" ", "").replace("\t", ""), containsString(resultHTML[1].replace(" ", "").replace("	", "")));

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_GET_TEST_param_format_robustness() {

		try {
			
			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(get("/rest/contentComments?text=xtc" + "&format=xlsx")).secure(true)).andReturn();

			assertEquals("Wrong status code. ", 200, result.getResponse().getStatus());


		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_GET_JSON_param_accept_functionality() {

		try {
			
			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(get("/rest/contentComments?text=" + demo1.getComment_text())).accept(MediaType.APPLICATION_JSON).secure(true)).andReturn();

			assertEquals("Wrong status code. ", 200, result.getResponse().getStatus());
			assertThat("Wrong content type. ", result.getResponse().getContentType(), containsString(MediaType.APPLICATION_JSON_VALUE));

			assertThat("Could not find expected content. ", result.getResponse().getContentAsString().replace(" ", "").replace("\t", ""), containsString(resultJSON[0].replace(" ", "").replace("	", "")));

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_GET_XML_param_accept_functionality() {

		try {
			
			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(get("/rest/contentComments?text=" + demo1.getComment_text())).accept(MediaType.APPLICATION_XML).secure(true)).andReturn();

			assertEquals("Wrong status code. ", 200, result.getResponse().getStatus());
			assertThat("Wrong content type. ", result.getResponse().getContentType(), containsString(MediaType.APPLICATION_XML_VALUE));

			assertThat("Could not find expected content. ", result.getResponse().getContentAsString().replace(" ", "").replace("\t", ""), containsString(resultXML[0].replace(" ", "").replace("	", "")));

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_GET_CSV_param_accept_functionality() {

		try {
			
			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(get("/rest/contentComments?text=" + demo1.getComment_text())).accept(MediaType.valueOf("text/csv")).secure(true)).andReturn();

			assertEquals("Wrong status code. ", 200, result.getResponse().getStatus());
			assertThat("Wrong content type. ", result.getResponse().getContentType(), containsString("text/csv"));

			assertThat("Could not find expected content. ", result.getResponse().getContentAsString().replace(" ", "").replace("\t", ""), containsString(resultCSV[0].replace(" ", "").replace("	", "")));
			assertThat("Could not find expected content. ", result.getResponse().getContentAsString().replace(" ", "").replace("\t", ""), containsString(resultCSV[1].replace(" ", "").replace("	", "")));

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_GET_HTML_param_accept_functionality() {

		try {
			
			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(get("/rest/contentComments?text=" + demo1.getComment_text())).accept(MediaType.TEXT_HTML).secure(true)).andReturn();

			assertEquals("Wrong status code. ", 200, result.getResponse().getStatus());
			assertThat("Wrong content type. ", result.getResponse().getContentType(), containsString(MediaType.TEXT_HTML_VALUE));

			assertThat("Could not find expected content. ", result.getResponse().getContentAsString().replace(" ", "").replace("\t", ""), containsString(resultHTML[0].replace(" ", "").replace("	", "")));
			assertThat("Could not find expected content. ", result.getResponse().getContentAsString().replace(" ", "").replace("\t", ""), containsString(resultHTML[1].replace(" ", "").replace("	", "")));

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_GET_TEST_param_accept_robustness() {

		try {
			
			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(get("/rest/contentComments?text=xtc")).accept(MediaType.IMAGE_GIF).secure(true)).andReturn();

			assertEquals("Wrong status code. ", 200, result.getResponse().getStatus());


		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_POST_JSON_format_functionality() {

		try {

			String content = "{" + resultJSON[2].substring(resultJSON[2].indexOf(",")+1);
			
			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(post("/rest/contentComments")).secure(true).contentType(MediaType.APPLICATION_JSON).content(content)).andReturn();

			assertEquals("Wrong status code. ", 200, result.getResponse().getStatus());
			assertThat("Wrong content type. ", result.getResponse().getContentType(), containsString(MediaType.APPLICATION_JSON_VALUE));


			assertTrue("Could not find expected id 3: ", repository.existsById(3));
			assertEquals("Could not find expected entry: ", demo3, repository.findById(3).orElse(null));

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_POST_JSON_format_robustness_1() {

		try {

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(post("/rest/contentComments").secure(true)).contentType(MediaType.APPLICATION_JSON).content("{\"comment_id\":4}")).andReturn();

			assertEquals("Wrong status code. ", 400, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_POST_JSON_format_robustness_2() {

		try {

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(post("/rest/contentComments").secure(true)).contentType(MediaType.APPLICATION_JSON).content("")).andReturn();

			assertEquals("Wrong status code. ", 400, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_POST_XML_format_functionality() {

		try {

			String content = "<entity>" + resultXML[2].substring(resultXML[2].indexOf(">", resultXML[2].indexOf(">")+1)+1) + "</entity>";
			
			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(post("/rest/contentComments?format=xml")).secure(true).contentType(MediaType.APPLICATION_XML).content(content)).andReturn();

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

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(post("/rest/contentComments").secure(true)).contentType(MediaType.APPLICATION_XML).content("<entity><comment_id>4</comment_id></entity>")).andReturn();

			assertEquals("Wrong status code. ", 400, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_POST_XML_format_robustness_2() {

		try {

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(post("/rest/contentComments").secure(true)).contentType(MediaType.APPLICATION_XML).content("")).andReturn();

			assertEquals("Wrong status code. ", 400, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_POST_CSV_format_functionality() {

		try {

			String content = resultCSV[0].substring(resultCSV[0].indexOf(",")+1) + "\n" + resultCSV[3].substring(resultCSV[3].indexOf(",")+1);
			
			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(post("/rest/contentComments")).secure(true).contentType(MediaType.valueOf("text/csv")).content(content)).andReturn();

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

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(post("/rest/contentComments").secure(true)).contentType(MediaType.valueOf("text/csv")).content("comment_id\n4")).andReturn();

			assertEquals("Wrong status code. ", 400, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_POST_CSV_format_robustness_2() {

		try {

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(post("/rest/contentComments").secure(true)).contentType(MediaType.valueOf("text/csv")).content("")).andReturn();

			assertEquals("Wrong status code. ", 400, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_POST_XFORM_format_functionality() {

		try {

			String content = resultXFORM[2].substring(resultXFORM[2].indexOf("&")+1);
			
			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(post("/rest/contentComments")).secure(true).contentType(MediaType.APPLICATION_FORM_URLENCODED).content(content)).andReturn();

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

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(post("/rest/contentComments").secure(true)).contentType(MediaType.APPLICATION_FORM_URLENCODED).content("comment_id=4")).andReturn();

			assertEquals("Wrong status code. ", 400, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_POST_XFORM_format_robustness_2() {

		try {

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(post("/rest/contentComments").secure(true)).contentType(MediaType.APPLICATION_FORM_URLENCODED).content("")).andReturn();

			assertEquals("Wrong status code. ", 400, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PUT_JSON_format_functionality_1() {

		try {

			String content = resultJSON[2];

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(put("/rest/contentComments/")).secure(true).contentType(MediaType.APPLICATION_JSON)
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

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(put("/rest/contentComments")).secure(true).contentType(MediaType.APPLICATION_JSON).content("{\"comment_id\":4}")).andReturn();

			assertEquals("Wrong status code. ", 400, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PUT_JSON_format_functionality_2() {

		try {

			String content = resultJSON[0];

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(put("/rest/contentComments/")).secure(true).contentType(MediaType.APPLICATION_JSON)
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

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(put("/rest/contentComments")).secure(true).contentType(MediaType.APPLICATION_JSON).content("")).andReturn();

			assertEquals("Wrong status code. ", 400, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PUT_XML_format_functionality_1() {

		try {

			String content = "<entity>" + resultXML[2] + "</entity>";

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(put("/rest/contentComments/?format=xml")).secure(true).contentType(MediaType.APPLICATION_XML)
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

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(put("/rest/contentComments")).secure(true).contentType(MediaType.APPLICATION_XML).content("<entity><comment_id>4</comment_id></entity>")).andReturn();

			assertEquals("Wrong status code. ", 400, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PUT_XML_format_functionality_2() {

		try {

			String content = "<entity>" + resultXML[0] + "</entity>";

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(put("/rest/contentComments/?format=xml")).secure(true).contentType(MediaType.APPLICATION_XML)
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

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(put("/rest/contentComments")).secure(true).contentType(MediaType.APPLICATION_XML).content("")).andReturn();

			assertEquals("Wrong status code. ", 400, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PUT_CSV_format_functionality_1() {

		try {

			String content = resultCSV[0] + "\n" + resultCSV[3];

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(put("/rest/contentComments/")).secure(true).contentType(MediaType.valueOf("text/csv"))
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

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(put("/rest/contentComments")).secure(true).contentType(MediaType.valueOf("text/csv")).content("comment_id\n4")).andReturn();

			assertEquals("Wrong status code. ", 400, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PUT_CSV_format_functionality_2() {

		try {

			String content = resultCSV[0] + "\n" + resultCSV[1];

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(put("/rest/contentComments/")).secure(true).contentType(MediaType.valueOf("text/csv"))
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

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(put("/rest/contentComments")).secure(true).contentType(MediaType.valueOf("text/csv")).content("")).andReturn();

			assertEquals("Wrong status code. ", 400, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PUT_XFORM_format_functionality_1() {

		try {

			String content = resultXFORM[2];

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(put("/rest/contentComments/")).secure(true).contentType(MediaType.APPLICATION_FORM_URLENCODED)
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

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(put("/rest/contentComments")).secure(true).contentType(MediaType.APPLICATION_FORM_URLENCODED).content("comment_id=4")).andReturn();

			assertEquals("Wrong status code. ", 400, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PUT_XFORM_format_functionality_2() {

		try {

			String content = resultXFORM[0];

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(put("/rest/contentComments/")).secure(true).contentType(MediaType.APPLICATION_FORM_URLENCODED)
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

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(put("/rest/contentComments")).secure(true).contentType(MediaType.APPLICATION_FORM_URLENCODED).content("")).andReturn();

			assertEquals("Wrong status code. ", 400, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PUT_JSON_id_format_functionality_1() {

		try {

			String content = "{" + resultJSON[2].substring(resultJSON[2].indexOf(",")+1);

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(put("/rest/contentComments/3")).secure(true).contentType(MediaType.APPLICATION_JSON)
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

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(put("/rest/contentComments")).secure(true).contentType(MediaType.APPLICATION_JSON).content("{\"comment_id\":4}")).andReturn();

			assertEquals("Wrong status code. ", 400, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PUT_JSON_id_format_functionality_2() {

		try {

			String content = "{" + resultJSON[0].substring(resultJSON[0].indexOf(",")+1);

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(put("/rest/contentComments/1")).secure(true).contentType(MediaType.APPLICATION_JSON)
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

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(put("/rest/contentComments")).secure(true).contentType(MediaType.APPLICATION_JSON).content("")).andReturn();

			assertEquals("Wrong status code. ", 400, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PUT_XML_id_format_functionality_1() {

		try {

			String content = "<entity>" + resultXML[2].substring(resultXML[2].indexOf(">", resultXML[2].indexOf(">")+1)+1) + "</entity>";

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(put("/rest/contentComments/3?format=xml")).secure(true).contentType(MediaType.APPLICATION_XML)
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

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(put("/rest/contentComments")).secure(true).contentType(MediaType.APPLICATION_XML).content("<entity><comment_id>4</comment_id></entity>")).andReturn();

			assertEquals("Wrong status code. ", 400, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PUT_XML_id_format_functionality_2() {

		try {

			String content = "<entity>" + resultXML[0].substring(resultXML[0].indexOf(">", resultXML[0].indexOf(">")+1)+1) + "</entity>";

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(put("/rest/contentComments/1?format=xml")).secure(true).contentType(MediaType.APPLICATION_XML)
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

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(put("/rest/contentComments")).secure(true).contentType(MediaType.APPLICATION_XML).content("")).andReturn();

			assertEquals("Wrong status code. ", 400, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PUT_CSV_id_format_functionality_1() {

		try {

			String content = resultCSV[0].substring(resultCSV[0].indexOf(",")+1) + "\n" + resultCSV[3].substring(resultCSV[3].indexOf(",")+1);

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(put("/rest/contentComments/3")).secure(true).contentType(MediaType.valueOf("text/csv"))
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

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(put("/rest/contentComments")).secure(true).contentType(MediaType.valueOf("text/csv")).content("comment_id\n4")).andReturn();

			assertEquals("Wrong status code. ", 400, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PUT_CSV_id_format_functionality_2() {

		try {

			String content = resultCSV[0].substring(resultCSV[0].indexOf(",")+1) + "\n" + resultCSV[1].substring(resultCSV[1].indexOf(",")+1);

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(put("/rest/contentComments/1")).secure(true).contentType(MediaType.valueOf("text/csv"))
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

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(put("/rest/contentComments")).secure(true).contentType(MediaType.valueOf("text/csv")).content("")).andReturn();

			assertEquals("Wrong status code. ", 400, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PUT_XFORM_id_format_functionality_1() {

		try {

			String content = resultXFORM[2].substring(resultXFORM[2].indexOf("&")+1);

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(put("/rest/contentComments/3")).secure(true).contentType(MediaType.APPLICATION_FORM_URLENCODED)
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

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(put("/rest/contentComments")).secure(true).contentType(MediaType.APPLICATION_FORM_URLENCODED).content("comment_id=4")).andReturn();

			assertEquals("Wrong status code. ", 400, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PUT_XFORM_id_format_functionality_2() {

		try {

			String content = resultXFORM[0].substring(resultXFORM[0].indexOf("&")+1);

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(put("/rest/contentComments/1")).secure(true).contentType(MediaType.APPLICATION_FORM_URLENCODED)
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

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(put("/rest/contentComments")).secure(true).contentType(MediaType.APPLICATION_FORM_URLENCODED).content("")).andReturn();

			assertEquals("Wrong status code. ", 400, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PATCH_JSON_format_functionality_1() {

		try {

			String content = resultJSON[0];

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(patch("/rest/contentComments/")).secure(true).contentType(MediaType.APPLICATION_JSON)
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

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(patch("/rest/contentComments")).secure(true).contentType(MediaType.APPLICATION_JSON).content("{\"comment_id\":4}")).andReturn();

			assertEquals("Wrong status code. ", 404, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PATCH_JSON_format_robustness_2() {

		try {

			String content = resultJSON[2];

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(patch("/rest/contentComments/")).secure(true).contentType(MediaType.APPLICATION_JSON)
					.content(content)).andReturn();

			assertEquals("Wrong status code. ", 404, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PATCH_JSON_format_robustness_3() {

		try {

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(patch("/rest/contentComments")).secure(true).contentType(MediaType.APPLICATION_JSON).content("")).andReturn();

			assertEquals("Wrong status code. ", 400, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PATCH_XML_format_functionality_1() {

		try {

			String content = "<entity>" + resultXML[0] + "</entity>";

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(patch("/rest/contentComments/?format=xml")).secure(true).contentType(MediaType.APPLICATION_XML)
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

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(patch("/rest/contentComments")).secure(true).contentType(MediaType.APPLICATION_XML).content("<entity><comment_id>4</comment_id></entity>")).andReturn();

			assertEquals("Wrong status code. ", 404, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PATCH_XML_format_robustness_2() {

		try {

			String content = "<entity>" + resultXML[2] + "</entity>";

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(patch("/rest/contentComments/?format=xml")).secure(true).contentType(MediaType.APPLICATION_XML)
					.content(content)).andReturn();

			assertEquals("Wrong status code. ", 404, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PATCH_XML_format_robustness_3() {

		try {

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(patch("/rest/contentComments")).secure(true).contentType(MediaType.APPLICATION_XML).content("")).andReturn();

			assertEquals("Wrong status code. ", 400, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PATCH_CSV_format_functionality_1() {

		try {

			String content = resultCSV[0] + "\n" + resultCSV[1];

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(patch("/rest/contentComments/")).secure(true).contentType(MediaType.valueOf("text/csv"))
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

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(patch("/rest/contentComments")).secure(true).contentType(MediaType.valueOf("text/csv")).content("comment_id\n4")).andReturn();

			assertEquals("Wrong status code. ", 400, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PATCH_CSV_format_robustness_2() {

		try {

			String content = resultCSV[0] + "\n" + resultCSV[3];

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(patch("/rest/contentComments/")).secure(true).contentType(MediaType.valueOf("text/csv"))
					.content(content)).andReturn();

			assertEquals("Wrong status code. ", 404, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PATCH_CSV_format_robustness_3() {

		try {

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(patch("/rest/contentComments")).secure(true).contentType(MediaType.valueOf("text/csv")).content("")).andReturn();

			assertEquals("Wrong status code. ", 400, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PATCH_XFORM_format_functionality_1() {

		try {

			String content = resultXFORM[0];

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(patch("/rest/contentComments/")).secure(true).contentType(MediaType.APPLICATION_FORM_URLENCODED)
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

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(patch("/rest/contentComments")).secure(true).contentType(MediaType.APPLICATION_FORM_URLENCODED).content("comment_id=4")).andReturn();

			assertEquals("Wrong status code. ", 404, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PATCH_XFORM_format_robustness_2() {

		try {

			String content = resultXFORM[2];

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(patch("/rest/contentComments/")).secure(true).contentType(MediaType.APPLICATION_FORM_URLENCODED)
					.content(content)).andReturn();

			assertEquals("Wrong status code. ", 404, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PATCH_XFORM_format_robustness_3() {

		try {

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(patch("/rest/contentComments")).secure(true).contentType(MediaType.APPLICATION_FORM_URLENCODED).content("")).andReturn();

			assertEquals("Wrong status code. ", 404, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PATCH_JSON_id_format_functionality_1() {

		try {

			String content = "{" + resultJSON[0].substring(resultJSON[0].indexOf(",")+1);

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(patch("/rest/contentComments/1")).secure(true).contentType(MediaType.APPLICATION_JSON)
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

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(patch("/rest/contentComments")).secure(true).contentType(MediaType.APPLICATION_JSON).content("{\"comment_id\":4}")).andReturn();

			assertEquals("Wrong status code. ", 404, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PATCH_JSON_id_format_robustness_2() {

		try {

			String content = "{" + resultJSON[2].substring(resultJSON[2].indexOf(",")+1);

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(patch("/rest/contentComments/3")).secure(true).contentType(MediaType.APPLICATION_JSON)
					.content(content)).andReturn();

			assertEquals("Wrong status code. ", 404, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PATCH_JSON_id_format_robustness_3() {

		try {

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(patch("/rest/contentComments")).secure(true).contentType(MediaType.APPLICATION_JSON).content("")).andReturn();

			assertEquals("Wrong status code. ", 400, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PATCH_XML_id_format_functionality_1() {

		try {

			String content = "<entity>" + resultXML[0].substring(resultXML[0].indexOf(">", resultXML[0].indexOf(">")+1)+1) + "</entity>";

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(patch("/rest/contentComments/1?format=xml")).secure(true).contentType(MediaType.APPLICATION_XML)
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

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(patch("/rest/contentComments")).secure(true).contentType(MediaType.APPLICATION_XML).content("<entity><comment_id>4</comment_id></entity>")).andReturn();

			assertEquals("Wrong status code. ", 404, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PATCH_XML_id_format_robustness_2() {

		try {

			String content = "<entity>" + resultXML[2].substring(resultXML[2].indexOf(">", resultXML[2].indexOf(">")+1)+1) + "</entity>";

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(patch("/rest/contentComments/3?format=xml")).secure(true).contentType(MediaType.APPLICATION_XML)
					.content(content)).andReturn();

			assertEquals("Wrong status code. ", 404, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PATCH_XML_id_format_robustness_3() {

		try {

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(patch("/rest/contentComments")).secure(true).contentType(MediaType.APPLICATION_XML).content("")).andReturn();

			assertEquals("Wrong status code. ", 400, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PATCH_CSV_id_format_functionality_1() {

		try {

			String content = resultCSV[0].substring(resultCSV[0].indexOf(",")+1) + "\n" + resultCSV[1].substring(resultCSV[1].indexOf(",")+1);

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(patch("/rest/contentComments/1")).secure(true).contentType(MediaType.valueOf("text/csv"))
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

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(patch("/rest/contentComments")).secure(true).contentType(MediaType.valueOf("text/csv")).content("comment_id\n4")).andReturn();

			assertEquals("Wrong status code. ", 400, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PATCH_CSV_id_format_robustness_2() {

		try {

			String content = resultCSV[0].substring(resultCSV[0].indexOf(",")+1) + "\n" + resultCSV[3].substring(resultCSV[3].indexOf(",")+1);

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(patch("/rest/contentComments/3")).secure(true).contentType(MediaType.valueOf("text/csv"))
					.content(content)).andReturn();

			assertEquals("Wrong status code. ", 404, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PATCH_CSV_id_format_robustness_3() {

		try {

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(patch("/rest/contentComments")).secure(true).contentType(MediaType.valueOf("text/csv")).content("")).andReturn();

			assertEquals("Wrong status code. ", 400, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PATCH_XFORM_id_format_functionality_1() {

		try {

			String content = resultXFORM[0].substring(resultXFORM[0].indexOf("&")+1);

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(patch("/rest/contentComments/1")).secure(true).contentType(MediaType.APPLICATION_FORM_URLENCODED)
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

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(patch("/rest/contentComments")).secure(true).contentType(MediaType.APPLICATION_FORM_URLENCODED).content("comment_id=4")).andReturn();

			assertEquals("Wrong status code. ", 404, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PATCH_XFORM_id_format_robustness_2() {

		try {

			String content = resultXFORM[2].substring(resultXFORM[2].indexOf("&")+1);

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(patch("/rest/contentComments/3")).secure(true).contentType(MediaType.APPLICATION_FORM_URLENCODED)
					.content(content)).andReturn();

			assertEquals("Wrong status code. ", 404, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PATCH_XFORM_id_format_robustness_3() {

		try {

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(patch("/rest/contentComments")).secure(true).contentType(MediaType.APPLICATION_FORM_URLENCODED).content("")).andReturn();

			assertEquals("Wrong status code. ", 404, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_DELETE_JSON_format_functionality() {

		try {

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(delete("/rest/contentComments/1")).secure(true)).andReturn();

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
			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(delete("/rest/contentComments/19")).secure(true)).andReturn();

			assertEquals("Wrong status code. ", 500, result.getResponse().getStatus());
			assertTrue("Wrong content was deleted. ", repository.existsById(1));
			assertTrue("Wrong content was deleted. ", repository.existsById(2));

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}
}
