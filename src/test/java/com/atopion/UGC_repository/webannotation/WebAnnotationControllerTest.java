package com.atopion.UGC_repository.webannotation;

import com.atopion.UGC_repository.util.CSVSerializer;
import com.atopion.UGC_repository.webannotation.entities.WebAnnotationEntity;
import com.atopion.UGC_repository.webannotation.repositories.WebAnnotationRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static com.atopion.UGC_repository.testutil.AccessTokens.withValidHeaders;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("classpath:application-integrationtests.properties")
@Sql(scripts = { "classpath:sql-init.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, config = @SqlConfig(dataSource = "webAnnotationDataSource"))
public class WebAnnotationControllerTest {

	private final String annotationJSON = "{\n" +
			"  \"id\" : \"https://data.forum-wissen.de/rest/annotations/15\",\n" +
			"  \"created\" : \"2020-04-25 02:00:00\",\n" +
			"  \"modified\" : \"2020-04-25 02:00:00\",\n" +
			"  \"generated\" : \"2020-04-25 02:00:00\",\n" +
			"  \"canonical\" : \"https://hdl.handle.net/21.11107/record_DE-MUS-069123_118\",\n" +
			"  \"generator\" : {\n" +
			"    \"id\" : \"https://sammlungsportal.uni-goettingen.de/user/tvogt\",\n" +
			"    \"nickname\" : \"tvogt\",\n" +
			"    \"name\" : \"Timon Vogt\",\n" +
			"    \"email\" : \"vogt@kustodie.uni-goettingen.de\",\n" +
			"    \"emailsSha1\" : \"001\",\n" +
			"    \"homepage\" : \"https://www.uni-goettingen.de/de/521338.html\"\n" +
			"  },\n" +
			"  \"motivation\" : \"describing\",\n" +
			"  \"creator\" : {\n" +
			"    \"id\" : \"https://sammlungsportal.uni-goettingen.de/user/kheck\",\n" +
			"    \"nickname\" : \"kheck\",\n" +
			"    \"name\" : \"Karsten Heck\",\n" +
			"    \"email\" : \"heck@kustodie.uni-goettingen.de\",\n" +
			"    \"emailsSha1\" : \"171bc3343d089b46d57f111b2939ad8a6b565a0f\",\n" +
			"    \"homepage\" : \"https://www.uni-goettingen.de/de/521338.html\"\n" +
			"  },\n" +
			"  \"via\" : \"https://sammlungen.uni-goettingen.de/objekt/record_DE-MUS-069123_118/1/-/\",\n" +
			"  \"body\" : {\n" +
			"    \"id\" : \"https://data.forum-wissen.de/annotations/body/2\",\n" +
			"    \"processing_language\" : \"de\",\n" +
			"    \"text_direction\" : \"ltr\",\n" +
			"    \"value\" : \"Eine interessante Kugel\",\n" +
			"    \"modified\" : \"2020-04-25 02:00:00\",\n" +
			"    \"created\" : \"2020-04-25 02:00:00\",\n" +
			"    \"canonical\" : \"https://hdl.handle.net/21.11107/record_DE-MUS-069123_118\",\n" +
			"    \"type\" : \"TextualBody\",\n" +
			"    \"format\" : \"text/html\",\n" +
			"    \"purpose\" : \"describing\",\n" +
			"    \"scope\" : \"https://sammlungen.uni-goettingen.de/mirador/\",\n" +
			"    \"styleClass\" : \"blue\"\n" +
			"  },\n" +
			"  \"target\" : {\n" +
			"    \"id\" : \"https://data.forum-wissen.de/annotations/target/2\",\n" +
			"    \"processing_language\" : \"de\",\n" +
			"    \"text_direction\" : \"ltr\",\n" +
			"    \"canonical\" : \"https://hdl.handle.net/21.11107/record_DE-MUS-069123_118\",\n" +
			"    \"source\" : \"https://sammlungen.uni-goettingen.de/rest/image/record_DE-MUS-069123_118/i118_0.jpg\",\n" +
			"    \"state\" : {\n" +
			"      \"type\" : \"TimeState\",\n" +
			"      \"sourceDate\" : \"2020-04-25 02:00:00\",\n" +
			"      \"cached\" : \"https://sammlungen.uni-goettingen.de/objekt/record_DE-MUS-069123_118/1/-/\"\n" +
			"    },\n" +
			"    \"type\" : \"SpecificResource\",\n" +
			"    \"language\" : \"de\",\n" +
			"    \"accessibility\" : \"resizeText/CSSEnabled\",\n" +
			"    \"renderedVia\" : {\n" +
			"      \"id\" : \"https://www.google.com/intl/de_de/chrome/\",\n" +
			"      \"type\" : \"Software\"\n" +
			"    },\n" +
			"    \"selector\" : {\n" +
			"      \"value\" : \"/full/!400,400/0/default.jpg\",\n" +
			"      \"conformsTo\" : \"https://iiif.io/api/image/2.1/#region\",\n" +
			"      \"type\" : \"FragmentSelector\"\n" +
			"    },\n" +
			"    \"rights\" : \"https://www.deutsche-digitale-bibliothek.de/content/lizenzen/rv-fz\"\n" +
			"  },\n" +
			"  \"audience\" : {\n" +
			"    \"id\" : \"https://container.uni-goettingen.de/roles/Studenten\",\n" +
			"    \"type\" : \"Studenten\"\n" +
			"  },\n" +
			"  \"type\" : \"Annotation\",\n" +
			"  \"stylesheet\" : {\n" +
			"    \"value\" : \".blue { color: blue }\",\n" +
			"    \"type\" : \"CssStylesheet3\"\n" +
			"  },\n" +
			"  \"@context\" : \"http://www.w3.org/ns/anno.jsonld\"\n" +
			"}";


	private final String[] resultJSON  = new String[]{"{\"web_annotation_id\":1,\"id\":\"demo_id1\",\"created\":\"2001-01-01 00:00:00\",\"modified\":\"2001-01-01 00:00:00\",\"generated\":\"2001-01-01 00:00:00\",\"bodyValue\":\"demo_bodyValue1\",\"canonical\":\"demo_canonical1\",\"stylesheet_id\":1}", "{\"web_annotation_id\":2,\"id\":\"demo_id2\",\"created\":\"2002-01-01 00:00:00\",\"modified\":\"2002-01-01 00:00:00\",\"generated\":\"2002-01-01 00:00:00\",\"bodyValue\":\"demo_bodyValue2\",\"canonical\":\"demo_canonical2\",\"stylesheet_id\":2}", "{\"web_annotation_id\":3,\"id\":\"demo_id3\",\"created\":\"2003-01-01 00:00:00\",\"modified\":\"2003-01-01 00:00:00\",\"generated\":\"2003-01-01 00:00:00\",\"bodyValue\":\"demo_bodyValue3\",\"canonical\":\"demo_canonical3\",\"stylesheet_id\":3}"};
	private final String[] resultXML   = new String[]{"<web_annotation_id>1</web_annotation_id><id>demo_id1</id><created>2001-01-01 00:00:00</created><modified>2001-01-01 00:00:00</modified><generated>2001-01-01 00:00:00</generated><bodyValue>demo_bodyValue1</bodyValue><canonical>demo_canonical1</canonical><stylesheet_id>1</stylesheet_id>", "<web_annotation_id>2</web_annotation_id><id>demo_id2</id><created>2002-01-01 00:00:00</created><modified>2002-01-01 00:00:00</modified><generated>2002-01-01 00:00:00</generated><bodyValue>demo_bodyValue2</bodyValue><canonical>demo_canonical2</canonical><stylesheet_id>2</stylesheet_id>", "<web_annotation_id>3</web_annotation_id><id>demo_id3</id><created>2003-01-01 00:00:00</created><modified>2003-01-01 00:00:00</modified><generated>2003-01-01 00:00:00</generated><bodyValue>demo_bodyValue3</bodyValue><canonical>demo_canonical3</canonical><stylesheet_id>3</stylesheet_id>"};
	/*private final String[] resultCSV   = new String[]{"web_annotation_id,id,created,modified,generated,bodyValue,canonical,stylesheet_id", "1,demo_id1,2001-01-01 00:00:00,2001-01-01 00:00:00,2001-01-01 00:00:00,demo_bodyValue1,demo_canonical1,1", "2,demo_id2,2002-01-01 00:00:00,2002-01-01 00:00:00,2002-01-01 00:00:00,demo_bodyValue2,demo_canonical2,2", "3,demo_id3,2003-01-01 00:00:00,2003-01-01 00:00:00,2003-01-01 00:00:00,demo_bodyValue3,demo_canonical3,3"};
	private final String[] resultHTML  = new String[]{"<tr>\n<th>web_annotation_id</th>\n<th>id</th>\n<th>created</th>\n<th>modified</th>\n<th>generated</th>\n<th>bodyValue</th>\n<th>canonical</th>\n<th>stylesheet_id</th>\n</tr>", "<tr>\n<td>1</td>\n<td>demo_id1</td>\n<td>2001-01-01 00:00:00</td>\n<td>2001-01-01 00:00:00</td>\n<td>2001-01-01 00:00:00</td>\n<td>demo_bodyValue1</td>\n<td>demo_canonical1</td>\n<td>1</td>\n</tr>", "<tr>\n<td>2</td>\n<td>demo_id2</td>\n<td>2002-01-01 00:00:00</td>\n<td>2002-01-01 00:00:00</td>\n<td>2002-01-01 00:00:00</td>\n<td>demo_bodyValue2</td>\n<td>demo_canonical2</td>\n<td>2</td>\n</tr>", "<tr>\n<td>3</td>\n<td>demo_id3</td>\n<td>2003-01-01 00:00:00</td>\n<td>2003-01-01 00:00:00</td>\n<td>2003-01-01 00:00:00</td>\n<td>demo_bodyValue3</td>\n<td>demo_canonical3</td>\n<td>3</td>\n</tr>"};
	private final String[] resultXFORM = new String[]{"web_annotation_id=1&id=demo_id1&created=2001-01-01 00:00:00&modified=2001-01-01 00:00:00&generated=2001-01-01 00:00:00&bodyValue=demo_bodyValue1&canonical=demo_canonical1&stylesheet_id=1", "web_annotation_id=2&id=demo_id2&created=2002-01-01 00:00:00&modified=2002-01-01 00:00:00&generated=2002-01-01 00:00:00&bodyValue=demo_bodyValue2&canonical=demo_canonical2&stylesheet_id=2", "web_annotation_id=3&id=demo_id3&created=2003-01-01 00:00:00&modified=2003-01-01 00:00:00&generated=2003-01-01 00:00:00&bodyValue=demo_bodyValue3&canonical=demo_canonical3&stylesheet_id=3"};*/

	@Autowired
	private MockMvc mvc;

	@Autowired
	private WebAnnotationRepository repository;

	@Autowired
	private DataSource webAnnotationDataSource;

	private WebAnnotationEntity demo1; //, demo3;

	private final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@BeforeEach
	void setUp() {
		TimeZone.setDefault(TimeZone.getTimeZone("Europe/Berlin"));

		try {
			repository.flush();
		} catch (Exception e) {
			fail("Unexpected exception occured: " + e.getMessage());
		}
	}

	@AfterEach
	void tearDown() {
		repository.deleteAll();
		repository.flush();

		try (Connection connection = webAnnotationDataSource.getConnection();
			 Statement statement = connection.createStatement()) {

			statement.executeUpdate("ALTER TABLE web_annotation ALTER COLUMN web_annotation_id RESTART WITH 1;");
			statement.executeUpdate("ALTER TABLE web_annotation_accessibility ALTER COLUMN accessibility_id RESTART WITH 1;");
			statement.executeUpdate("ALTER TABLE web_annotation_agent ALTER COLUMN agent_id RESTART WITH 1;");
			statement.executeUpdate("ALTER TABLE web_annotation_agent_email ALTER COLUMN agent_email_id RESTART WITH 1;");
			statement.executeUpdate("ALTER TABLE web_annotation_agent_homepage ALTER COLUMN agent_homepage_id RESTART WITH 1;");
			statement.executeUpdate("ALTER TABLE web_annotation_agent_name ALTER COLUMN agent_name_id RESTART WITH 1;");
			statement.executeUpdate("ALTER TABLE web_annotation_audience ALTER COLUMN audience_id RESTART WITH 1;");
			statement.executeUpdate("ALTER TABLE web_annotation_body ALTER COLUMN body_id RESTART WITH 1;");
			statement.executeUpdate("ALTER TABLE web_annotation_cached ALTER COLUMN cached_id RESTART WITH 1;");
			statement.executeUpdate("ALTER TABLE web_annotation_context ALTER COLUMN context_id RESTART WITH 1;");
			statement.executeUpdate("ALTER TABLE web_annotation_email_sha1 ALTER COLUMN agent_email_sha1_id RESTART WITH 1;");
			statement.executeUpdate("ALTER TABLE web_annotation_format ALTER COLUMN format_id RESTART WITH 1;");
			statement.executeUpdate("ALTER TABLE web_annotation_language ALTER COLUMN language_id RESTART WITH 1;");
			statement.executeUpdate("ALTER TABLE web_annotation_motivation ALTER COLUMN motivation_id RESTART WITH 1;");
			statement.executeUpdate("ALTER TABLE web_annotation_purpose ALTER COLUMN purpose_id RESTART WITH 1;");
			statement.executeUpdate("ALTER TABLE web_annotation_rendered_via ALTER COLUMN rendered_via_id RESTART WITH 1;");
			statement.executeUpdate("ALTER TABLE web_annotation_rights ALTER COLUMN rights_id RESTART WITH 1;");
			statement.executeUpdate("ALTER TABLE web_annotation_scope ALTER COLUMN scope_id RESTART WITH 1;");
			statement.executeUpdate("ALTER TABLE web_annotation_selector ALTER COLUMN selector_id RESTART WITH 1;");
			statement.executeUpdate("ALTER TABLE web_annotation_source_date ALTER COLUMN source_date_id RESTART WITH 1;");
			statement.executeUpdate("ALTER TABLE web_annotation_state ALTER COLUMN state_id RESTART WITH 1;");
			statement.executeUpdate("ALTER TABLE web_annotation_style_class ALTER COLUMN style_class_id RESTART WITH 1;");
			statement.executeUpdate("ALTER TABLE web_annotation_stylesheet ALTER COLUMN stylesheet_id RESTART WITH 1;");
			statement.executeUpdate("ALTER TABLE web_annotation_target ALTER COLUMN target_id RESTART WITH 1;");
			statement.executeUpdate("ALTER TABLE web_annotation_type ALTER COLUMN type_id RESTART WITH 1;");
			statement.executeUpdate("ALTER TABLE web_annotation_via ALTER COLUMN via_id RESTART WITH 1;");

		} catch (Exception e) {
			fail("Unexpected exception occured: " + e.getMessage());
		}
	}

	@Test
	void test_GET_functionality() {

		try {
			mvc.perform(withValidHeaders(get("/rest/webannotation/1")).secure(true))
					.andDo(print())
					.andExpect(status().isOk())
					.andExpect(content().contentType("application/ld+json"))
					.andExpect(jsonPath("$.id", is("https://data.forum-wissen.de/rest/annotations/1")))
					.andExpect(jsonPath("$.@context", is("http://www.w3.org/ns/anno.jsonld")));

		} catch (Exception e) {
			fail("Unexpected exception occured: " + e.getMessage());
			e.printStackTrace();
		}
	}

	@Test
	void test_POST_functionality() {

		try {
			mvc.perform(withValidHeaders(post("/rest/webannotation")
					.contentType("application/ld+json").content(annotationJSON)).secure(true))
					.andDo(print())
					.andExpect(status().isCreated())
					.andExpect(content().contentType("application/ld+json"))
					.andExpect(jsonPath("$.id", is("https://data.forum-wissen.de/rest/webannotation/2")))
					.andExpect(jsonPath("$.@context", is("http://www.w3.org/ns/anno.jsonld")));

			mvc.perform(withValidHeaders(get("/rest/webannotation/1")).secure(true))
					.andDo(print())
					.andExpect(status().isOk())
					.andExpect(content().contentType("application/ld+json"))
					.andExpect(jsonPath("$.id", is("https://data.forum-wissen.de/rest/annotations/1")))
					.andExpect(jsonPath("$.@context", is("http://www.w3.org/ns/anno.jsonld")));

			mvc.perform(withValidHeaders(get("/rest/webannotation/2")).secure(true))
					.andDo(print())
					.andExpect(status().isOk())
					.andExpect(content().contentType("application/ld+json"))
					.andExpect(jsonPath("$.id", is("https://data.forum-wissen.de/rest/webannotation/2")))
					.andExpect(jsonPath("$.body.value", is("Eine interessante Kugel")))
					.andExpect(jsonPath("$.@context", is("http://www.w3.org/ns/anno.jsonld")));

		} catch (Exception e) {
			fail("Unexpected exception occured: " + e.getMessage());
			e.printStackTrace();
		}
	}


	@Test
	void test_PUT_functionality() {
		try {
			mvc.perform(withValidHeaders(put("/rest/webannotation/1")
					.contentType("application/ld+json").content(annotationJSON)).secure(true))
					.andDo(print())
					.andExpect(status().isOk())
					.andExpect(content().contentType("application/ld+json"))
					.andExpect(jsonPath("$.id", is("https://data.forum-wissen.de/rest/annotations/1")))
					.andExpect(jsonPath("$.@context", is("http://www.w3.org/ns/anno.jsonld")))
					.andExpect(jsonPath("$.body.id", is("https://data.forum-wissen.de/annotations/body/2")));

			mvc.perform(withValidHeaders(get("/rest/webannotation/1")).secure(true))
					.andDo(print())
					.andExpect(status().isOk())
					.andExpect(content().contentType("application/ld+json"))
					.andExpect(jsonPath("$.id", is("https://data.forum-wissen.de/rest/annotations/1")))
					.andExpect(jsonPath("$.@context", is("http://www.w3.org/ns/anno.jsonld")))
					.andExpect(jsonPath("$.body.id", is("https://data.forum-wissen.de/annotations/body/2")));

		} catch (Exception e) {
			e.printStackTrace();
			fail("Unexpected exception occured: " + e.getMessage());
		}
	}


	private final String patchAnnotationJSON = "{\n" +
			"  \"modified\" : \"2020-04-25 05:00:00\",\n" +
			"  \"motivation\" : \"testing\"\n" +
			"}";

	@Test
	void test_PATCH_functionality() {
		try {
			mvc.perform(withValidHeaders(patch("/rest/webannotation/1")
					.contentType("application/ld+json").content(patchAnnotationJSON)).secure(true))
					.andDo(print())
					.andExpect(status().isOk())
					.andExpect(content().contentType("application/ld+json"))
					.andExpect(jsonPath("$.id", is("https://data.forum-wissen.de/rest/annotations/1")))
					.andExpect(jsonPath("$.modified", is("2020-04-25 05:00:00")))
					.andExpect(jsonPath("$.motivation", is("testing")))
					.andExpect(jsonPath("$.body.id", is("https://data.forum-wissen.de/annotations/body/1")));

			mvc.perform(withValidHeaders(get("/rest/webannotation/1")).secure(true))
					.andDo(print())
					.andExpect(status().isOk())
					.andExpect(content().contentType("application/ld+json"))
					.andExpect(jsonPath("$.id", is("https://data.forum-wissen.de/rest/annotations/1")))
					.andExpect(jsonPath("$.modified", is("2020-04-25 05:00:00")))
					.andExpect(jsonPath("$.motivation", is("testing")))
					.andExpect(jsonPath("$.body.id", is("https://data.forum-wissen.de/annotations/body/1")));

		} catch (Exception e) {
			e.printStackTrace();
			fail("Unexpected exception occured: " + e.getMessage());
		}
	}

	/*
	@Test
	void test_GET_JSON_format_functionality() {

		try {
			
			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(get("/rest/webAnnotation?format=json")).secure(true)).andReturn();

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
			
			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(get("/rest/webAnnotation?format=xml")).secure(true)).andReturn();

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
			
			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(get("/rest/webAnnotation?format=csv")).secure(true)).andReturn();

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
			
			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(get("/rest/webAnnotation?format=html")).secure(true)).andReturn();

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
			
			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(get("/rest/webAnnotation?format=xlsx")).secure(true)).andReturn();

			assertEquals("Wrong status code. ", 200, result.getResponse().getStatus());


		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_GET_JSON_accept_functionality() {

		try {
			
			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(get("/rest/webAnnotation")).accept(MediaType.APPLICATION_JSON).secure(true)).andReturn();

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
			
			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(get("/rest/webAnnotation")).accept(MediaType.APPLICATION_XML).secure(true)).andReturn();

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
			
			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(get("/rest/webAnnotation")).accept(MediaType.valueOf("text/csv")).secure(true)).andReturn();

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
			
			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(get("/rest/webAnnotation")).accept(MediaType.TEXT_HTML).secure(true)).andReturn();

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
			
			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(get("/rest/webAnnotation")).accept(MediaType.IMAGE_GIF).secure(true)).andReturn();

			assertEquals("Wrong status code. ", 200, result.getResponse().getStatus());


		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_GET_JSON_id_format_functionality() {

		try {
			
			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(get("/rest/webAnnotation/" + demo1.getWeb_annotation_id() + "?format=json")).secure(true)).andReturn();

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
			
			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(get("/rest/webAnnotation/" + demo1.getWeb_annotation_id() + "?format=xml")).secure(true)).andReturn();

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
			
			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(get("/rest/webAnnotation/" + demo1.getWeb_annotation_id() + "?format=csv")).secure(true)).andReturn();

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
			
			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(get("/rest/webAnnotation/" + demo1.getWeb_annotation_id() + "?format=html")).secure(true)).andReturn();

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
			
			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(get("/rest/webAnnotation/21" + "?format=xlsx")).secure(true)).andReturn();

			assertEquals("Wrong status code. ", 404, result.getResponse().getStatus());


		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_GET_JSON_id_accept_functionality() {

		try {
			
			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(get("/rest/webAnnotation/" + demo1.getWeb_annotation_id())).accept(MediaType.APPLICATION_JSON).secure(true)).andReturn();

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
			
			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(get("/rest/webAnnotation/" + demo1.getWeb_annotation_id())).accept(MediaType.APPLICATION_XML).secure(true)).andReturn();

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
			
			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(get("/rest/webAnnotation/" + demo1.getWeb_annotation_id())).accept(MediaType.valueOf("text/csv")).secure(true)).andReturn();

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
			
			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(get("/rest/webAnnotation/" + demo1.getWeb_annotation_id())).accept(MediaType.TEXT_HTML).secure(true)).andReturn();

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
			
			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(get("/rest/webAnnotation/21")).accept(MediaType.IMAGE_GIF).secure(true)).andReturn();

			assertEquals("Wrong status code. ", 404, result.getResponse().getStatus());


		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_GET_JSON_param_format_functionality() {

		try {
			
			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(get("/rest/webAnnotation?id=" + demo1.getId() + "&format=json")).secure(true)).andReturn();

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
			
			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(get("/rest/webAnnotation?id=" + demo1.getId() + "&format=xml")).secure(true)).andReturn();

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
			
			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(get("/rest/webAnnotation?id=" + demo1.getId() + "&format=csv")).secure(true)).andReturn();

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
			
			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(get("/rest/webAnnotation?id=" + demo1.getId() + "&format=html")).secure(true)).andReturn();

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
			
			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(get("/rest/webAnnotation?id=xtc" + "&format=xlsx")).secure(true)).andReturn();

			assertEquals("Wrong status code. ", 200, result.getResponse().getStatus());


		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_GET_JSON_param_accept_functionality() {

		try {
			
			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(get("/rest/webAnnotation?id=" + demo1.getId())).accept(MediaType.APPLICATION_JSON).secure(true)).andReturn();

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
			
			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(get("/rest/webAnnotation?id=" + demo1.getId())).accept(MediaType.APPLICATION_XML).secure(true)).andReturn();

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
			
			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(get("/rest/webAnnotation?id=" + demo1.getId())).accept(MediaType.valueOf("text/csv")).secure(true)).andReturn();

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
			
			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(get("/rest/webAnnotation?id=" + demo1.getId())).accept(MediaType.TEXT_HTML).secure(true)).andReturn();

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
			
			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(get("/rest/webAnnotation?id=xtc")).accept(MediaType.IMAGE_GIF).secure(true)).andReturn();

			assertEquals("Wrong status code. ", 200, result.getResponse().getStatus());


		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_POST_JSON_format_functionality() {

		try {

			String content = "{" + resultJSON[2].substring(resultJSON[2].indexOf(",")+1);
			
			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(post("/rest/webAnnotation")).secure(true).contentType(MediaType.APPLICATION_JSON).content(content)).andReturn();

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

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(post("/rest/webAnnotation").secure(true)).contentType(MediaType.APPLICATION_JSON).content("{\"web_annotation_id\":4}")).andReturn();

			assertEquals("Wrong status code. ", 400, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_POST_JSON_format_robustness_2() {

		try {

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(post("/rest/webAnnotation").secure(true)).contentType(MediaType.APPLICATION_JSON).content("")).andReturn();

			assertEquals("Wrong status code. ", 400, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_POST_XML_format_functionality() {

		try {

			String content = "<entity>" + resultXML[2].substring(resultXML[2].indexOf(">", resultXML[2].indexOf(">")+1)+1) + "</entity>";
			
			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(post("/rest/webAnnotation?format=xml")).secure(true).contentType(MediaType.APPLICATION_XML).content(content)).andReturn();

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

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(post("/rest/webAnnotation").secure(true)).contentType(MediaType.APPLICATION_XML).content("<entity><web_annotation_id>4</web_annotation_id></entity>")).andReturn();

			assertEquals("Wrong status code. ", 400, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_POST_XML_format_robustness_2() {

		try {

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(post("/rest/webAnnotation").secure(true)).contentType(MediaType.APPLICATION_XML).content("")).andReturn();

			assertEquals("Wrong status code. ", 400, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_POST_CSV_format_functionality() {

		try {

			String content = resultCSV[0].substring(resultCSV[0].indexOf(",")+1) + "\n" + resultCSV[3].substring(resultCSV[3].indexOf(",")+1);
			
			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(post("/rest/webAnnotation")).secure(true).contentType(MediaType.valueOf("text/csv")).content(content)).andReturn();

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

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(post("/rest/webAnnotation").secure(true)).contentType(MediaType.valueOf("text/csv")).content("web_annotation_id\n4")).andReturn();

			assertEquals("Wrong status code. ", 400, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_POST_CSV_format_robustness_2() {

		try {

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(post("/rest/webAnnotation").secure(true)).contentType(MediaType.valueOf("text/csv")).content("")).andReturn();

			assertEquals("Wrong status code. ", 400, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_POST_XFORM_format_functionality() {

		try {

			String content = resultXFORM[2].substring(resultXFORM[2].indexOf("&")+1);
			
			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(post("/rest/webAnnotation")).secure(true).contentType(MediaType.APPLICATION_FORM_URLENCODED).content(content)).andReturn();

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

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(post("/rest/webAnnotation").secure(true)).contentType(MediaType.APPLICATION_FORM_URLENCODED).content("web_annotation_id=4")).andReturn();

			assertEquals("Wrong status code. ", 400, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_POST_XFORM_format_robustness_2() {

		try {

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(post("/rest/webAnnotation").secure(true)).contentType(MediaType.APPLICATION_FORM_URLENCODED).content("")).andReturn();

			assertEquals("Wrong status code. ", 400, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PUT_JSON_format_functionality_1() {

		try {

			String content = resultJSON[2];

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(put("/rest/webAnnotation/")).secure(true).contentType(MediaType.APPLICATION_JSON)
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

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(put("/rest/webAnnotation")).secure(true).contentType(MediaType.APPLICATION_JSON).content("{\"web_annotation_id\":4}")).andReturn();

			assertEquals("Wrong status code. ", 400, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PUT_JSON_format_functionality_2() {

		try {

			String content = resultJSON[0];

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(put("/rest/webAnnotation/")).secure(true).contentType(MediaType.APPLICATION_JSON)
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

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(put("/rest/webAnnotation")).secure(true).contentType(MediaType.APPLICATION_JSON).content("")).andReturn();

			assertEquals("Wrong status code. ", 400, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PUT_XML_format_functionality_1() {

		try {

			String content = "<entity>" + resultXML[2] + "</entity>";

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(put("/rest/webAnnotation/?format=xml")).secure(true).contentType(MediaType.APPLICATION_XML)
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

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(put("/rest/webAnnotation")).secure(true).contentType(MediaType.APPLICATION_XML).content("<entity><web_annotation_id>4</web_annotation_id></entity>")).andReturn();

			assertEquals("Wrong status code. ", 400, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PUT_XML_format_functionality_2() {

		try {

			String content = "<entity>" + resultXML[0] + "</entity>";

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(put("/rest/webAnnotation/?format=xml")).secure(true).contentType(MediaType.APPLICATION_XML)
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

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(put("/rest/webAnnotation")).secure(true).contentType(MediaType.APPLICATION_XML).content("")).andReturn();

			assertEquals("Wrong status code. ", 400, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PUT_CSV_format_functionality_1() {

		try {

			String content = resultCSV[0] + "\n" + resultCSV[3];

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(put("/rest/webAnnotation/")).secure(true).contentType(MediaType.valueOf("text/csv"))
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

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(put("/rest/webAnnotation")).secure(true).contentType(MediaType.valueOf("text/csv")).content("web_annotation_id\n4")).andReturn();

			assertEquals("Wrong status code. ", 400, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PUT_CSV_format_functionality_2() {

		try {

			String content = resultCSV[0] + "\n" + resultCSV[1];

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(put("/rest/webAnnotation/")).secure(true).contentType(MediaType.valueOf("text/csv"))
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

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(put("/rest/webAnnotation")).secure(true).contentType(MediaType.valueOf("text/csv")).content("")).andReturn();

			assertEquals("Wrong status code. ", 400, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PUT_XFORM_format_functionality_1() {

		try {

			String content = resultXFORM[2];

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(put("/rest/webAnnotation/")).secure(true).contentType(MediaType.APPLICATION_FORM_URLENCODED)
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

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(put("/rest/webAnnotation")).secure(true).contentType(MediaType.APPLICATION_FORM_URLENCODED).content("web_annotation_id=4")).andReturn();

			assertEquals("Wrong status code. ", 400, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PUT_XFORM_format_functionality_2() {

		try {

			String content = resultXFORM[0];

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(put("/rest/webAnnotation/")).secure(true).contentType(MediaType.APPLICATION_FORM_URLENCODED)
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

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(put("/rest/webAnnotation")).secure(true).contentType(MediaType.APPLICATION_FORM_URLENCODED).content("")).andReturn();

			assertEquals("Wrong status code. ", 400, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PUT_JSON_id_format_functionality_1() {

		try {

			String content = "{" + resultJSON[2].substring(resultJSON[2].indexOf(",")+1);

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(put("/rest/webAnnotation/3")).secure(true).contentType(MediaType.APPLICATION_JSON)
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

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(put("/rest/webAnnotation")).secure(true).contentType(MediaType.APPLICATION_JSON).content("{\"web_annotation_id\":4}")).andReturn();

			assertEquals("Wrong status code. ", 400, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PUT_JSON_id_format_functionality_2() {

		try {

			String content = "{" + resultJSON[0].substring(resultJSON[0].indexOf(",")+1);

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(put("/rest/webAnnotation/1")).secure(true).contentType(MediaType.APPLICATION_JSON)
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

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(put("/rest/webAnnotation")).secure(true).contentType(MediaType.APPLICATION_JSON).content("")).andReturn();

			assertEquals("Wrong status code. ", 400, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PUT_XML_id_format_functionality_1() {

		try {

			String content = "<entity>" + resultXML[2].substring(resultXML[2].indexOf(">", resultXML[2].indexOf(">")+1)+1) + "</entity>";

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(put("/rest/webAnnotation/3?format=xml")).secure(true).contentType(MediaType.APPLICATION_XML)
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

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(put("/rest/webAnnotation")).secure(true).contentType(MediaType.APPLICATION_XML).content("<entity><web_annotation_id>4</web_annotation_id></entity>")).andReturn();

			assertEquals("Wrong status code. ", 400, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PUT_XML_id_format_functionality_2() {

		try {

			String content = "<entity>" + resultXML[0].substring(resultXML[0].indexOf(">", resultXML[0].indexOf(">")+1)+1) + "</entity>";

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(put("/rest/webAnnotation/1?format=xml")).secure(true).contentType(MediaType.APPLICATION_XML)
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

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(put("/rest/webAnnotation")).secure(true).contentType(MediaType.APPLICATION_XML).content("")).andReturn();

			assertEquals("Wrong status code. ", 400, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PUT_CSV_id_format_functionality_1() {

		try {

			String content = resultCSV[0].substring(resultCSV[0].indexOf(",")+1) + "\n" + resultCSV[3].substring(resultCSV[3].indexOf(",")+1);

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(put("/rest/webAnnotation/3")).secure(true).contentType(MediaType.valueOf("text/csv"))
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

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(put("/rest/webAnnotation")).secure(true).contentType(MediaType.valueOf("text/csv")).content("web_annotation_id\n4")).andReturn();

			assertEquals("Wrong status code. ", 400, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PUT_CSV_id_format_functionality_2() {

		try {

			String content = resultCSV[0].substring(resultCSV[0].indexOf(",")+1) + "\n" + resultCSV[1].substring(resultCSV[1].indexOf(",")+1);

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(put("/rest/webAnnotation/1")).secure(true).contentType(MediaType.valueOf("text/csv"))
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

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(put("/rest/webAnnotation")).secure(true).contentType(MediaType.valueOf("text/csv")).content("")).andReturn();

			assertEquals("Wrong status code. ", 400, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PUT_XFORM_id_format_functionality_1() {

		try {

			String content = resultXFORM[2].substring(resultXFORM[2].indexOf("&")+1);

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(put("/rest/webAnnotation/3")).secure(true).contentType(MediaType.APPLICATION_FORM_URLENCODED)
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

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(put("/rest/webAnnotation")).secure(true).contentType(MediaType.APPLICATION_FORM_URLENCODED).content("web_annotation_id=4")).andReturn();

			assertEquals("Wrong status code. ", 400, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PUT_XFORM_id_format_functionality_2() {

		try {

			String content = resultXFORM[0].substring(resultXFORM[0].indexOf("&")+1);

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(put("/rest/webAnnotation/1")).secure(true).contentType(MediaType.APPLICATION_FORM_URLENCODED)
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

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(put("/rest/webAnnotation")).secure(true).contentType(MediaType.APPLICATION_FORM_URLENCODED).content("")).andReturn();

			assertEquals("Wrong status code. ", 400, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PATCH_JSON_format_functionality_1() {

		try {

			String content = resultJSON[0];

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(patch("/rest/webAnnotation/")).secure(true).contentType(MediaType.APPLICATION_JSON)
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

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(patch("/rest/webAnnotation")).secure(true).contentType(MediaType.APPLICATION_JSON).content("{\"web_annotation_id\":4}")).andReturn();

			assertEquals("Wrong status code. ", 404, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PATCH_JSON_format_robustness_2() {

		try {

			String content = resultJSON[2];

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(patch("/rest/webAnnotation/")).secure(true).contentType(MediaType.APPLICATION_JSON)
					.content(content)).andReturn();

			assertEquals("Wrong status code. ", 404, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PATCH_JSON_format_robustness_3() {

		try {

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(patch("/rest/webAnnotation")).secure(true).contentType(MediaType.APPLICATION_JSON).content("")).andReturn();

			assertEquals("Wrong status code. ", 400, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PATCH_XML_format_functionality_1() {

		try {

			String content = "<entity>" + resultXML[0] + "</entity>";

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(patch("/rest/webAnnotation/?format=xml")).secure(true).contentType(MediaType.APPLICATION_XML)
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

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(patch("/rest/webAnnotation")).secure(true).contentType(MediaType.APPLICATION_XML).content("<entity><web_annotation_id>4</web_annotation_id></entity>")).andReturn();

			assertEquals("Wrong status code. ", 404, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PATCH_XML_format_robustness_2() {

		try {

			String content = "<entity>" + resultXML[2] + "</entity>";

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(patch("/rest/webAnnotation/?format=xml")).secure(true).contentType(MediaType.APPLICATION_XML)
					.content(content)).andReturn();

			assertEquals("Wrong status code. ", 404, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PATCH_XML_format_robustness_3() {

		try {

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(patch("/rest/webAnnotation")).secure(true).contentType(MediaType.APPLICATION_XML).content("")).andReturn();

			assertEquals("Wrong status code. ", 400, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PATCH_CSV_format_functionality_1() {

		try {

			String content = resultCSV[0] + "\n" + resultCSV[1];

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(patch("/rest/webAnnotation/")).secure(true).contentType(MediaType.valueOf("text/csv"))
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

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(patch("/rest/webAnnotation")).secure(true).contentType(MediaType.valueOf("text/csv")).content("web_annotation_id\n4")).andReturn();

			assertEquals("Wrong status code. ", 400, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PATCH_CSV_format_robustness_2() {

		try {

			String content = resultCSV[0] + "\n" + resultCSV[3];

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(patch("/rest/webAnnotation/")).secure(true).contentType(MediaType.valueOf("text/csv"))
					.content(content)).andReturn();

			assertEquals("Wrong status code. ", 404, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PATCH_CSV_format_robustness_3() {

		try {

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(patch("/rest/webAnnotation")).secure(true).contentType(MediaType.valueOf("text/csv")).content("")).andReturn();

			assertEquals("Wrong status code. ", 400, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PATCH_XFORM_format_functionality_1() {

		try {

			String content = resultXFORM[0];

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(patch("/rest/webAnnotation/")).secure(true).contentType(MediaType.APPLICATION_FORM_URLENCODED)
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

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(patch("/rest/webAnnotation")).secure(true).contentType(MediaType.APPLICATION_FORM_URLENCODED).content("web_annotation_id=4")).andReturn();

			assertEquals("Wrong status code. ", 404, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PATCH_XFORM_format_robustness_2() {

		try {

			String content = resultXFORM[2];

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(patch("/rest/webAnnotation/")).secure(true).contentType(MediaType.APPLICATION_FORM_URLENCODED)
					.content(content)).andReturn();

			assertEquals("Wrong status code. ", 404, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PATCH_XFORM_format_robustness_3() {

		try {

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(patch("/rest/webAnnotation")).secure(true).contentType(MediaType.APPLICATION_FORM_URLENCODED).content("")).andReturn();

			assertEquals("Wrong status code. ", 404, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PATCH_JSON_id_format_functionality_1() {

		try {

			String content = "{" + resultJSON[0].substring(resultJSON[0].indexOf(",")+1);

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(patch("/rest/webAnnotation/1")).secure(true).contentType(MediaType.APPLICATION_JSON)
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

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(patch("/rest/webAnnotation")).secure(true).contentType(MediaType.APPLICATION_JSON).content("{\"web_annotation_id\":4}")).andReturn();

			assertEquals("Wrong status code. ", 404, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PATCH_JSON_id_format_robustness_2() {

		try {

			String content = "{" + resultJSON[2].substring(resultJSON[2].indexOf(",")+1);

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(patch("/rest/webAnnotation/3")).secure(true).contentType(MediaType.APPLICATION_JSON)
					.content(content)).andReturn();

			assertEquals("Wrong status code. ", 404, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PATCH_JSON_id_format_robustness_3() {

		try {

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(patch("/rest/webAnnotation")).secure(true).contentType(MediaType.APPLICATION_JSON).content("")).andReturn();

			assertEquals("Wrong status code. ", 400, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PATCH_XML_id_format_functionality_1() {

		try {

			String content = "<entity>" + resultXML[0].substring(resultXML[0].indexOf(">", resultXML[0].indexOf(">")+1)+1) + "</entity>";

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(patch("/rest/webAnnotation/1?format=xml")).secure(true).contentType(MediaType.APPLICATION_XML)
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

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(patch("/rest/webAnnotation")).secure(true).contentType(MediaType.APPLICATION_XML).content("<entity><web_annotation_id>4</web_annotation_id></entity>")).andReturn();

			assertEquals("Wrong status code. ", 404, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PATCH_XML_id_format_robustness_2() {

		try {

			String content = "<entity>" + resultXML[2].substring(resultXML[2].indexOf(">", resultXML[2].indexOf(">")+1)+1) + "</entity>";

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(patch("/rest/webAnnotation/3?format=xml")).secure(true).contentType(MediaType.APPLICATION_XML)
					.content(content)).andReturn();

			assertEquals("Wrong status code. ", 404, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PATCH_XML_id_format_robustness_3() {

		try {

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(patch("/rest/webAnnotation")).secure(true).contentType(MediaType.APPLICATION_XML).content("")).andReturn();

			assertEquals("Wrong status code. ", 400, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PATCH_CSV_id_format_functionality_1() {

		try {

			String content = resultCSV[0].substring(resultCSV[0].indexOf(",")+1) + "\n" + resultCSV[1].substring(resultCSV[1].indexOf(",")+1);

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(patch("/rest/webAnnotation/1")).secure(true).contentType(MediaType.valueOf("text/csv"))
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

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(patch("/rest/webAnnotation")).secure(true).contentType(MediaType.valueOf("text/csv")).content("web_annotation_id\n4")).andReturn();

			assertEquals("Wrong status code. ", 400, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PATCH_CSV_id_format_robustness_2() {

		try {

			String content = resultCSV[0].substring(resultCSV[0].indexOf(",")+1) + "\n" + resultCSV[3].substring(resultCSV[3].indexOf(",")+1);

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(patch("/rest/webAnnotation/3")).secure(true).contentType(MediaType.valueOf("text/csv"))
					.content(content)).andReturn();

			assertEquals("Wrong status code. ", 404, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PATCH_CSV_id_format_robustness_3() {

		try {

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(patch("/rest/webAnnotation")).secure(true).contentType(MediaType.valueOf("text/csv")).content("")).andReturn();

			assertEquals("Wrong status code. ", 400, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PATCH_XFORM_id_format_functionality_1() {

		try {

			String content = resultXFORM[0].substring(resultXFORM[0].indexOf("&")+1);

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(patch("/rest/webAnnotation/1")).secure(true).contentType(MediaType.APPLICATION_FORM_URLENCODED)
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

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(patch("/rest/webAnnotation")).secure(true).contentType(MediaType.APPLICATION_FORM_URLENCODED).content("web_annotation_id=4")).andReturn();

			assertEquals("Wrong status code. ", 404, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PATCH_XFORM_id_format_robustness_2() {

		try {

			String content = resultXFORM[2].substring(resultXFORM[2].indexOf("&")+1);

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(patch("/rest/webAnnotation/3")).secure(true).contentType(MediaType.APPLICATION_FORM_URLENCODED)
					.content(content)).andReturn();

			assertEquals("Wrong status code. ", 404, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_PATCH_XFORM_id_format_robustness_3() {

		try {

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(patch("/rest/webAnnotation")).secure(true).contentType(MediaType.APPLICATION_FORM_URLENCODED).content("")).andReturn();

			assertEquals("Wrong status code. ", 404, result.getResponse().getStatus());

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	@Test
	void test_DELETE_JSON_format_functionality() {

		try {

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(delete("/rest/webAnnotation/1")).secure(true)).andReturn();

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
			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(delete("/rest/webAnnotation/19")).secure(true)).andReturn();

			assertEquals("Wrong status code. ", 500, result.getResponse().getStatus());
			assertTrue("Wrong content was deleted. ", repository.existsById(1));
			assertTrue("Wrong content was deleted. ", repository.existsById(2));

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}

	 */
}
