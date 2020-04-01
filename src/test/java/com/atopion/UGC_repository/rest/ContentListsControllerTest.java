package com.atopion.UGC_repository.rest;

import com.atopion.UGC_repository.entities.ContentListsEntity;
import com.atopion.UGC_repository.repositories.ContentListsRepository;
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

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("classpath:application-integrationtests.properties")
public class ContentListsControllerTest {

	private final String[] resultJSON  = new String[]{"{\"list_id\":1,\"list_title\":\"demo_title1\",\"list_description\":\"demo_description1\",\"list_created\":\"2001-01-01 00:00:00\",\"application_id\":1,\"user_token\":\"demo_token1\"}", "{\"list_id\":2,\"list_title\":\"demo_title2\",\"list_description\":\"demo_description2\",\"list_created\":\"2002-01-01 00:00:00\",\"application_id\":2,\"user_token\":\"demo_token2\"}", "{\"list_id\":3,\"list_title\":\"demo_title3\",\"list_description\":\"demo_description3\",\"list_created\":\"2003-01-01 00:00:00\",\"application_id\":3,\"user_token\":\"demo_token3\"}"};
	private final String[] resultXML   = new String[]{"<list_id>1</list_id><list_title>demo_title1</list_title><list_description>demo_description1</list_description><list_created>2001-01-01 00:00:00</list_created><application_id>1</application_id><user_token>demo_token1</user_token>", "<list_id>2</list_id><list_title>demo_title2</list_title><list_description>demo_description2</list_description><list_created>2002-01-01 00:00:00</list_created><application_id>2</application_id><user_token>demo_token2</user_token>", "<list_id>3</list_id><list_title>demo_title3</list_title><list_description>demo_description3</list_description><list_created>2003-01-01 00:00:00</list_created><application_id>3</application_id><user_token>demo_token3</user_token>"};
	private final String[] resultCSV   = new String[]{"list_id,list_title,list_description,list_created,application_id,user_token", "1,demo_title1,demo_description1,2001-01-01 00:00:00,1,demo_token1", "2,demo_title2,demo_description2,2002-01-01 00:00:00,2,demo_token2", "3,demo_title3,demo_description3,2003-01-01 00:00:00,3,demo_token3"};
	private final String[] resultHTML  = new String[]{"<tr>\n<th>list_id</th>\n<th>list_title</th>\n<th>list_description</th>\n<th>list_created</th>\n<th>application_id</th>\n<th>user_token</th>\n</tr>", "<tr>\n<td>1</td>\n<td>demo_title1</td>\n<td>demo_description1</td>\n<td>2001-01-01 00:00:00</td>\n<td>1</td>\n<td>demo_token1</td>\n</tr>", "<tr>\n<td>2</td>\n<td>demo_title2</td>\n<td>demo_description2</td>\n<td>2002-01-01 00:00:00</td>\n<td>2</td>\n<td>demo_token2</td>\n</tr>", "<tr>\n<td>3</td>\n<td>demo_title3</td>\n<td>demo_description3</td>\n<td>2003-01-01 00:00:00</td>\n<td>3</td>\n<td>demo_token3</td>\n</tr>"};
	private final String[] resultXFORM = new String[]{"list_id=1&list_title=demo_title1&list_description=demo_description1&list_created=2001-01-01 00:00:00&application_id=1&user_token=demo_token1", "list_id=2&list_title=demo_title2&list_description=demo_description2&list_created=2002-01-01 00:00:00&application_id=2&user_token=demo_token2", "list_id=3&list_title=demo_title3&list_description=demo_description3&list_created=2003-01-01 00:00:00&application_id=3&user_token=demo_token3"};

	@Autowired
	private MockMvc mvc;

	@Autowired
	private ContentListsRepository repository;

	@Autowired
	private DataSource dataSource;

	private ContentListsEntity demo1, demo3;

	private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@BeforeEach
	void setUp() {
		try {
			demo1 = new ContentListsEntity("demo_title1", "demo_description1", format.parse("2001-01-01 00:00:00"), 1, "demo_token1");
			ContentListsEntity demo2 = new ContentListsEntity("demo_title2", "demo_description2", format.parse("2002-01-01 00:00:00"), 2, "demo_token2");
		
			demo3 = new ContentListsEntity(3, "demo_title3", "demo_description3", format.parse("2003-01-01 00:00:00"), 3, "demo_token3");

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
			statement.executeUpdate("ALTER TABLE content_lists ALTER COLUMN list_id RESTART WITH 1;");

		} catch (Exception e) {
			fail("Unexpected exception occured: " + e.getMessage());
		}
	}

	@Test
	void test_GET_JSON_format_functionality() {

		try {
			
			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(get("/rest/contentLists?format=json"))).andReturn();

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
			
			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(get("/rest/contentLists?format=xml"))).andReturn();

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
			
			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(get("/rest/contentLists?format=csv"))).andReturn();

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
			
			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(get("/rest/contentLists?format=html"))).andReturn();

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
			
			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(get("/rest/contentLists?format=xlsx"))).andReturn();

			assertEquals("Wrong status code. ", 200, result.getResponse().getStatus());


		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_GET_JSON_accept_functionality() {

		try {
			
			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(get("/rest/contentLists")).accept(MediaType.APPLICATION_JSON)).andReturn();

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
			
			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(get("/rest/contentLists")).accept(MediaType.APPLICATION_XML)).andReturn();

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
			
			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(get("/rest/contentLists")).accept(MediaType.valueOf("text/csv"))).andReturn();

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
			
			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(get("/rest/contentLists")).accept(MediaType.TEXT_HTML)).andReturn();

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
			
			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(get("/rest/contentLists")).accept(MediaType.IMAGE_GIF)).andReturn();

			assertEquals("Wrong status code. ", 200, result.getResponse().getStatus());


		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_GET_JSON_id_format_functionality() {

		try {
			
			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(get("/rest/contentLists/" + demo1.getList_id() + "?format=json"))).andReturn();

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
			
			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(get("/rest/contentLists/" + demo1.getList_id() + "?format=xml"))).andReturn();

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
			
			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(get("/rest/contentLists/" + demo1.getList_id() + "?format=csv"))).andReturn();

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
			
			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(get("/rest/contentLists/" + demo1.getList_id() + "?format=html"))).andReturn();

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
			
			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(get("/rest/contentLists/21" + "?format=xlsx"))).andReturn();

			assertEquals("Wrong status code. ", 404, result.getResponse().getStatus());


		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_GET_JSON_id_accept_functionality() {

		try {
			
			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(get("/rest/contentLists/" + demo1.getList_id())).accept(MediaType.APPLICATION_JSON)).andReturn();

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
			
			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(get("/rest/contentLists/" + demo1.getList_id())).accept(MediaType.APPLICATION_XML)).andReturn();

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
			
			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(get("/rest/contentLists/" + demo1.getList_id())).accept(MediaType.valueOf("text/csv"))).andReturn();

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
			
			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(get("/rest/contentLists/" + demo1.getList_id())).accept(MediaType.TEXT_HTML)).andReturn();

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
			
			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(get("/rest/contentLists/21")).accept(MediaType.IMAGE_GIF)).andReturn();

			assertEquals("Wrong status code. ", 404, result.getResponse().getStatus());


		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_GET_JSON_param_format_functionality() {

		try {
			
			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(get("/rest/contentLists?title=" + demo1.getList_title() + "&format=json"))).andReturn();

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
			
			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(get("/rest/contentLists?title=" + demo1.getList_title() + "&format=xml"))).andReturn();

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
			
			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(get("/rest/contentLists?title=" + demo1.getList_title() + "&format=csv"))).andReturn();

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
			
			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(get("/rest/contentLists?title=" + demo1.getList_title() + "&format=html"))).andReturn();

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
			
			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(get("/rest/contentLists?title=xtc" + "&format=xlsx"))).andReturn();

			assertEquals("Wrong status code. ", 200, result.getResponse().getStatus());


		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_GET_JSON_param_accept_functionality() {

		try {
			
			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(get("/rest/contentLists?title=" + demo1.getList_title())).accept(MediaType.APPLICATION_JSON)).andReturn();

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
			
			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(get("/rest/contentLists?title=" + demo1.getList_title())).accept(MediaType.APPLICATION_XML)).andReturn();

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
			
			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(get("/rest/contentLists?title=" + demo1.getList_title())).accept(MediaType.valueOf("text/csv"))).andReturn();

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
			
			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(get("/rest/contentLists?title=" + demo1.getList_title())).accept(MediaType.TEXT_HTML)).andReturn();

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
			
			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(get("/rest/contentLists?title=xtc")).accept(MediaType.IMAGE_GIF)).andReturn();

			assertEquals("Wrong status code. ", 200, result.getResponse().getStatus());


		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_POST_JSON_format_functionality() {

		try {

			String content = "{" + resultJSON[2].substring(resultJSON[2].indexOf(",")+1);
			
			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(post("/rest/contentLists")).contentType(MediaType.APPLICATION_JSON).content(content)).andReturn();

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

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(post("/rest/contentLists")).contentType(MediaType.APPLICATION_JSON).content("{\"list_id\":4}")).andReturn();

			assertEquals("Wrong status code. ", 400, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_POST_JSON_format_robustness_2() {

		try {

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(post("/rest/contentLists")).contentType(MediaType.APPLICATION_JSON).content("")).andReturn();

			assertEquals("Wrong status code. ", 400, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_POST_XML_format_functionality() {

		try {

			String content = "<entity>" + resultXML[2].substring(resultXML[2].indexOf(">", resultXML[2].indexOf(">")+1)+1) + "</entity>";
			
			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(post("/rest/contentLists?format=xml")).contentType(MediaType.APPLICATION_XML).content(content)).andReturn();

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

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(post("/rest/contentLists")).contentType(MediaType.APPLICATION_XML).content("<entity><list_id>4</list_id></entity>")).andReturn();

			assertEquals("Wrong status code. ", 400, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_POST_XML_format_robustness_2() {

		try {

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(post("/rest/contentLists")).contentType(MediaType.APPLICATION_XML).content("")).andReturn();

			assertEquals("Wrong status code. ", 400, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_POST_CSV_format_functionality() {

		try {

			String content = resultCSV[0].substring(resultCSV[0].indexOf(",")+1) + "\n" + resultCSV[3].substring(resultCSV[3].indexOf(",")+1);
			
			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(post("/rest/contentLists")).contentType(MediaType.valueOf("text/csv")).content(content)).andReturn();

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

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(post("/rest/contentLists")).contentType(MediaType.valueOf("text/csv")).content("list_id\n4")).andReturn();

			assertEquals("Wrong status code. ", 400, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_POST_CSV_format_robustness_2() {

		try {

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(post("/rest/contentLists")).contentType(MediaType.valueOf("text/csv")).content("")).andReturn();

			assertEquals("Wrong status code. ", 400, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_POST_XFORM_format_functionality() {

		try {

			String content = resultXFORM[2].substring(resultXFORM[2].indexOf("&")+1);
			
			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(post("/rest/contentLists")).contentType(MediaType.APPLICATION_FORM_URLENCODED).content(content)).andReturn();

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

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(post("/rest/contentLists")).contentType(MediaType.APPLICATION_FORM_URLENCODED).content("list_id=4")).andReturn();

			assertEquals("Wrong status code. ", 400, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_POST_XFORM_format_robustness_2() {

		try {

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(post("/rest/contentLists")).contentType(MediaType.APPLICATION_FORM_URLENCODED).content("")).andReturn();

			assertEquals("Wrong status code. ", 400, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PUT_JSON_format_functionality_1() {

		try {

			String content = resultJSON[2];

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(put("/rest/contentLists/")).contentType(MediaType.APPLICATION_JSON)
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

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(put("/rest/contentLists")).contentType(MediaType.APPLICATION_JSON).content("{\"list_id\":4}")).andReturn();

			assertEquals("Wrong status code. ", 400, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PUT_JSON_format_functionality_2() {

		try {

			String content = resultJSON[0];

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(put("/rest/contentLists/")).contentType(MediaType.APPLICATION_JSON)
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

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(put("/rest/contentLists")).contentType(MediaType.APPLICATION_JSON).content("")).andReturn();

			assertEquals("Wrong status code. ", 400, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PUT_XML_format_functionality_1() {

		try {

			String content = "<entity>" + resultXML[2] + "</entity>";

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(put("/rest/contentLists/?format=xml")).contentType(MediaType.APPLICATION_XML)
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

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(put("/rest/contentLists")).contentType(MediaType.APPLICATION_XML).content("<entity><list_id>4</list_id></entity>")).andReturn();

			assertEquals("Wrong status code. ", 400, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PUT_XML_format_functionality_2() {

		try {

			String content = "<entity>" + resultXML[0] + "</entity>";

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(put("/rest/contentLists/?format=xml")).contentType(MediaType.APPLICATION_XML)
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

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(put("/rest/contentLists")).contentType(MediaType.APPLICATION_XML).content("")).andReturn();

			assertEquals("Wrong status code. ", 400, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PUT_CSV_format_functionality_1() {

		try {

			String content = resultCSV[0] + "\n" + resultCSV[3];

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(put("/rest/contentLists/")).contentType(MediaType.valueOf("text/csv"))
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

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(put("/rest/contentLists")).contentType(MediaType.valueOf("text/csv")).content("list_id\n4")).andReturn();

			assertEquals("Wrong status code. ", 400, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PUT_CSV_format_functionality_2() {

		try {

			String content = resultCSV[0] + "\n" + resultCSV[1];

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(put("/rest/contentLists/")).contentType(MediaType.valueOf("text/csv"))
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

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(put("/rest/contentLists")).contentType(MediaType.valueOf("text/csv")).content("")).andReturn();

			assertEquals("Wrong status code. ", 400, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PUT_XFORM_format_functionality_1() {

		try {

			String content = resultXFORM[2];

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(put("/rest/contentLists/")).contentType(MediaType.APPLICATION_FORM_URLENCODED)
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

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(put("/rest/contentLists")).contentType(MediaType.APPLICATION_FORM_URLENCODED).content("list_id=4")).andReturn();

			assertEquals("Wrong status code. ", 400, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PUT_XFORM_format_functionality_2() {

		try {

			String content = resultXFORM[0];

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(put("/rest/contentLists/")).contentType(MediaType.APPLICATION_FORM_URLENCODED)
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

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(put("/rest/contentLists")).contentType(MediaType.APPLICATION_FORM_URLENCODED).content("")).andReturn();

			assertEquals("Wrong status code. ", 400, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PUT_JSON_id_format_functionality_1() {

		try {

			String content = "{" + resultJSON[2].substring(resultJSON[2].indexOf(",")+1);

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(put("/rest/contentLists/3")).contentType(MediaType.APPLICATION_JSON)
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

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(put("/rest/contentLists")).contentType(MediaType.APPLICATION_JSON).content("{\"list_id\":4}")).andReturn();

			assertEquals("Wrong status code. ", 400, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PUT_JSON_id_format_functionality_2() {

		try {

			String content = "{" + resultJSON[0].substring(resultJSON[0].indexOf(",")+1);

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(put("/rest/contentLists/1")).contentType(MediaType.APPLICATION_JSON)
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

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(put("/rest/contentLists")).contentType(MediaType.APPLICATION_JSON).content("")).andReturn();

			assertEquals("Wrong status code. ", 400, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PUT_XML_id_format_functionality_1() {

		try {

			String content = "<entity>" + resultXML[2].substring(resultXML[2].indexOf(">", resultXML[2].indexOf(">")+1)+1) + "</entity>";

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(put("/rest/contentLists/3?format=xml")).contentType(MediaType.APPLICATION_XML)
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

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(put("/rest/contentLists")).contentType(MediaType.APPLICATION_XML).content("<entity><list_id>4</list_id></entity>")).andReturn();

			assertEquals("Wrong status code. ", 400, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PUT_XML_id_format_functionality_2() {

		try {

			String content = "<entity>" + resultXML[0].substring(resultXML[0].indexOf(">", resultXML[0].indexOf(">")+1)+1) + "</entity>";

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(put("/rest/contentLists/1?format=xml")).contentType(MediaType.APPLICATION_XML)
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

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(put("/rest/contentLists")).contentType(MediaType.APPLICATION_XML).content("")).andReturn();

			assertEquals("Wrong status code. ", 400, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PUT_CSV_id_format_functionality_1() {

		try {

			String content = resultCSV[0].substring(resultCSV[0].indexOf(",")+1) + "\n" + resultCSV[3].substring(resultCSV[3].indexOf(",")+1);

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(put("/rest/contentLists/3")).contentType(MediaType.valueOf("text/csv"))
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

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(put("/rest/contentLists")).contentType(MediaType.valueOf("text/csv")).content("list_id\n4")).andReturn();

			assertEquals("Wrong status code. ", 400, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PUT_CSV_id_format_functionality_2() {

		try {

			String content = resultCSV[0].substring(resultCSV[0].indexOf(",")+1) + "\n" + resultCSV[1].substring(resultCSV[1].indexOf(",")+1);

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(put("/rest/contentLists/1")).contentType(MediaType.valueOf("text/csv"))
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

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(put("/rest/contentLists")).contentType(MediaType.valueOf("text/csv")).content("")).andReturn();

			assertEquals("Wrong status code. ", 400, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PUT_XFORM_id_format_functionality_1() {

		try {

			String content = resultXFORM[2].substring(resultXFORM[2].indexOf("&")+1);

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(put("/rest/contentLists/3")).contentType(MediaType.APPLICATION_FORM_URLENCODED)
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

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(put("/rest/contentLists")).contentType(MediaType.APPLICATION_FORM_URLENCODED).content("list_id=4")).andReturn();

			assertEquals("Wrong status code. ", 400, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PUT_XFORM_id_format_functionality_2() {

		try {

			String content = resultXFORM[0].substring(resultXFORM[0].indexOf("&")+1);

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(put("/rest/contentLists/1")).contentType(MediaType.APPLICATION_FORM_URLENCODED)
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

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(put("/rest/contentLists")).contentType(MediaType.APPLICATION_FORM_URLENCODED).content("")).andReturn();

			assertEquals("Wrong status code. ", 400, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PATCH_JSON_format_functionality_1() {

		try {

			String content = resultJSON[0];

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(patch("/rest/contentLists/")).contentType(MediaType.APPLICATION_JSON)
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

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(patch("/rest/contentLists")).contentType(MediaType.APPLICATION_JSON).content("{\"list_id\":4}")).andReturn();

			assertEquals("Wrong status code. ", 404, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PATCH_JSON_format_robustness_2() {

		try {

			String content = resultJSON[2];

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(patch("/rest/contentLists/")).contentType(MediaType.APPLICATION_JSON)
					.content(content)).andReturn();

			assertEquals("Wrong status code. ", 404, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PATCH_JSON_format_robustness_3() {

		try {

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(patch("/rest/contentLists")).contentType(MediaType.APPLICATION_JSON).content("")).andReturn();

			assertEquals("Wrong status code. ", 400, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PATCH_XML_format_functionality_1() {

		try {

			String content = "<entity>" + resultXML[0] + "</entity>";

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(patch("/rest/contentLists/?format=xml")).contentType(MediaType.APPLICATION_XML)
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

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(patch("/rest/contentLists")).contentType(MediaType.APPLICATION_XML).content("<entity><list_id>4</list_id></entity>")).andReturn();

			assertEquals("Wrong status code. ", 404, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PATCH_XML_format_robustness_2() {

		try {

			String content = "<entity>" + resultXML[2] + "</entity>";

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(patch("/rest/contentLists/?format=xml")).contentType(MediaType.APPLICATION_XML)
					.content(content)).andReturn();

			assertEquals("Wrong status code. ", 404, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PATCH_XML_format_robustness_3() {

		try {

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(patch("/rest/contentLists")).contentType(MediaType.APPLICATION_XML).content("")).andReturn();

			assertEquals("Wrong status code. ", 400, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PATCH_CSV_format_functionality_1() {

		try {

			String content = resultCSV[0] + "\n" + resultCSV[1];

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(patch("/rest/contentLists/")).contentType(MediaType.valueOf("text/csv"))
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

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(patch("/rest/contentLists")).contentType(MediaType.valueOf("text/csv")).content("list_id\n4")).andReturn();

			assertEquals("Wrong status code. ", 400, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PATCH_CSV_format_robustness_2() {

		try {

			String content = resultCSV[0] + "\n" + resultCSV[3];

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(patch("/rest/contentLists/")).contentType(MediaType.valueOf("text/csv"))
					.content(content)).andReturn();

			assertEquals("Wrong status code. ", 404, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PATCH_CSV_format_robustness_3() {

		try {

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(patch("/rest/contentLists")).contentType(MediaType.valueOf("text/csv")).content("")).andReturn();

			assertEquals("Wrong status code. ", 400, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PATCH_XFORM_format_functionality_1() {

		try {

			String content = resultXFORM[0];

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(patch("/rest/contentLists/")).contentType(MediaType.APPLICATION_FORM_URLENCODED)
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

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(patch("/rest/contentLists")).contentType(MediaType.APPLICATION_FORM_URLENCODED).content("list_id=4")).andReturn();

			assertEquals("Wrong status code. ", 404, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PATCH_XFORM_format_robustness_2() {

		try {

			String content = resultXFORM[2];

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(patch("/rest/contentLists/")).contentType(MediaType.APPLICATION_FORM_URLENCODED)
					.content(content)).andReturn();

			assertEquals("Wrong status code. ", 404, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PATCH_XFORM_format_robustness_3() {

		try {

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(patch("/rest/contentLists")).contentType(MediaType.APPLICATION_FORM_URLENCODED).content("")).andReturn();

			assertEquals("Wrong status code. ", 404, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PATCH_JSON_id_format_functionality_1() {

		try {

			String content = "{" + resultJSON[0].substring(resultJSON[0].indexOf(",")+1);

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(patch("/rest/contentLists/1")).contentType(MediaType.APPLICATION_JSON)
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

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(patch("/rest/contentLists")).contentType(MediaType.APPLICATION_JSON).content("{\"list_id\":4}")).andReturn();

			assertEquals("Wrong status code. ", 404, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PATCH_JSON_id_format_robustness_2() {

		try {

			String content = "{" + resultJSON[2].substring(resultJSON[2].indexOf(",")+1);

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(patch("/rest/contentLists/3")).contentType(MediaType.APPLICATION_JSON)
					.content(content)).andReturn();

			assertEquals("Wrong status code. ", 404, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PATCH_JSON_id_format_robustness_3() {

		try {

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(patch("/rest/contentLists")).contentType(MediaType.APPLICATION_JSON).content("")).andReturn();

			assertEquals("Wrong status code. ", 400, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PATCH_XML_id_format_functionality_1() {

		try {

			String content = "<entity>" + resultXML[0].substring(resultXML[0].indexOf(">", resultXML[0].indexOf(">")+1)+1) + "</entity>";

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(patch("/rest/contentLists/1?format=xml")).contentType(MediaType.APPLICATION_XML)
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

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(patch("/rest/contentLists")).contentType(MediaType.APPLICATION_XML).content("<entity><list_id>4</list_id></entity>")).andReturn();

			assertEquals("Wrong status code. ", 404, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PATCH_XML_id_format_robustness_2() {

		try {

			String content = "<entity>" + resultXML[2].substring(resultXML[2].indexOf(">", resultXML[2].indexOf(">")+1)+1) + "</entity>";

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(patch("/rest/contentLists/3?format=xml")).contentType(MediaType.APPLICATION_XML)
					.content(content)).andReturn();

			assertEquals("Wrong status code. ", 404, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PATCH_XML_id_format_robustness_3() {

		try {

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(patch("/rest/contentLists")).contentType(MediaType.APPLICATION_XML).content("")).andReturn();

			assertEquals("Wrong status code. ", 400, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PATCH_CSV_id_format_functionality_1() {

		try {

			String content = resultCSV[0].substring(resultCSV[0].indexOf(",")+1) + "\n" + resultCSV[1].substring(resultCSV[1].indexOf(",")+1);

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(patch("/rest/contentLists/1")).contentType(MediaType.valueOf("text/csv"))
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

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(patch("/rest/contentLists")).contentType(MediaType.valueOf("text/csv")).content("list_id\n4")).andReturn();

			assertEquals("Wrong status code. ", 400, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PATCH_CSV_id_format_robustness_2() {

		try {

			String content = resultCSV[0].substring(resultCSV[0].indexOf(",")+1) + "\n" + resultCSV[3].substring(resultCSV[3].indexOf(",")+1);

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(patch("/rest/contentLists/3")).contentType(MediaType.valueOf("text/csv"))
					.content(content)).andReturn();

			assertEquals("Wrong status code. ", 404, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PATCH_CSV_id_format_robustness_3() {

		try {

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(patch("/rest/contentLists")).contentType(MediaType.valueOf("text/csv")).content("")).andReturn();

			assertEquals("Wrong status code. ", 400, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PATCH_XFORM_id_format_functionality_1() {

		try {

			String content = resultXFORM[0].substring(resultXFORM[0].indexOf("&")+1);

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(patch("/rest/contentLists/1")).contentType(MediaType.APPLICATION_FORM_URLENCODED)
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

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(patch("/rest/contentLists")).contentType(MediaType.APPLICATION_FORM_URLENCODED).content("list_id=4")).andReturn();

			assertEquals("Wrong status code. ", 404, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PATCH_XFORM_id_format_robustness_2() {

		try {

			String content = resultXFORM[2].substring(resultXFORM[2].indexOf("&")+1);

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(patch("/rest/contentLists/3")).contentType(MediaType.APPLICATION_FORM_URLENCODED)
					.content(content)).andReturn();

			assertEquals("Wrong status code. ", 404, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PATCH_XFORM_id_format_robustness_3() {

		try {

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(patch("/rest/contentLists")).contentType(MediaType.APPLICATION_FORM_URLENCODED).content("")).andReturn();

			assertEquals("Wrong status code. ", 404, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_DELETE_JSON_format_functionality() {

		try {

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(delete("/rest/contentLists/1"))).andReturn();

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
			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(delete("/rest/contentLists/19"))).andReturn();

			assertEquals("Wrong status code. ", 500, result.getResponse().getStatus());
			assertTrue("Wrong content was deleted. ", repository.existsById(1));
			assertTrue("Wrong content was deleted. ", repository.existsById(2));

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}
}
