package com.atopion.UGC_repository.sql;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.server.ResponseStatusException;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class SQLControllerUnitTest {

    private Logger logger = null;
    private ListAppender<ILoggingEvent> listAppender = null;

    private ResultSetMetaData mockMetaData = null;
    private ResultSet mockSet = null;
    private Statement mockStatement = null;
    private Connection mockConnection = null;
    private DataSource mockSource = null;
    private Model mockModel = null;
    private LinkedHashMap<String, Object> mockModelMap = null;
    private int rowCounter = 0;

    private List<String> demoHeaderResult = Arrays.asList("HEADER", "HEADER");
    private List<List<String>> demoCellResult = Arrays.asList( Arrays.asList("CELL", "CELL"), Arrays.asList("CELL", "CELL"));
    private String demoHTMLResult = "sqlResult";
    private String demoJSONResult = "[{\"HEADER\":\"CELL\",\"HEADER\":\"CELL\"},{\"HEADER\":\"CELL\",\"HEADER\":\"CELL\"}]";
    private String demoXMLResult = "<response>\n\t<element>\n\t\t<HEADER>CELL</HEADER>\n\t\t<HEADER>CELL</HEADER>\n\t</element>\n\t<element>\n\t\t<HEADER>CELL</HEADER>\n\t\t<HEADER>CELL</HEADER>\n\t</element>\n</response>";

    @BeforeEach
    void setUp() {
        logger = (Logger) LoggerFactory.getLogger(SQLController.class);
        listAppender = new ListAppender<>();
        listAppender.start();

        logger.addAppender(listAppender);

        mockModelMap = new LinkedHashMap<>();

        mockMetaData = mock(ResultSetMetaData.class);
        mockSet = mock(ResultSet.class);
        mockStatement = mock(Statement.class);
        mockConnection = mock(Connection.class);
        mockSource = mock(DataSource.class);
        mockModel = mock(Model.class);

        // Demo Database Result
        try {
            when(mockSource.getConnection()).thenReturn(mockConnection);
            when(mockConnection.createStatement()).thenReturn(mockStatement);
            when(mockStatement.executeQuery(anyString())).thenReturn(mockSet);
            when(mockSet.getMetaData()).thenReturn(mockMetaData);
            when(mockSet.next()).thenAnswer(ign -> { rowCounter++; return rowCounter <= 2; });

            when(mockMetaData.getColumnCount()).thenReturn(2);
            when(mockMetaData.getColumnName(anyInt())).thenReturn("HEADER");
            when(mockSet.getString(anyInt())).thenReturn("CELL");
            doAnswer(invocation -> {
                String name = invocation.getArgument(0);
                Object element = invocation.getArgument(1);
                mockModelMap.put(name, element);
                return null;
            }).when(mockModel).addAttribute(anyString(), any());
        } catch (SQLException ex) {
            fail("Unexpected mock Init Exception occurred");
        }

    }

    @AfterEach
    void tearDown() {
        logger = null;
        listAppender = null;

        mockMetaData = null;
        mockSet = null;
        mockStatement = null;
        mockConnection = null;
        mockSource = null;
        mockModel = null;
        mockModelMap = null;
        rowCounter = 0;
    }

    @Test
    void sqlRequestHTML_functionality_1() {
        try {
            SQLController ctrl = new SQLController(mockSource);
            assertEquals(demoHTMLResult, ctrl.sqlRequestHTML("SELECT * FROM applications;", mockModel));

            if (!mockModelMap.get("table_headers").equals(demoHeaderResult))
                fail("Did not find expected value for 'table_headers'. Value was: " + mockModelMap.get("table_headers") + ", expected: " + demoHeaderResult);
            if (!mockModelMap.get("table_rows").equals(demoCellResult))
                fail("Did not find expected value for 'table_rows'. Value was: " + mockModelMap.get("table_rows"));
        } catch (ResponseStatusException ex) {
            fail("Unexpected Response status exception with code: " + ex.getStatus());
        } catch (Exception ex) {
            fail("Unexpected Exception: " + ex.getMessage());
        }
    }

    @Test
    void sqlRequestHTML_robustness_1() {
        try {
            SQLController ctrl = new SQLController(mockSource);
            ctrl.sqlRequestHTML("", mockModel);
            fail("Did not throw a ResponseStatusException");
        } catch (ResponseStatusException ex) {
            assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus(), "Did throw wrong HTTP Status code: " + ex.getStatus());
        } catch (Exception ex) {
            ex.printStackTrace();
            fail("Unexpected Exception: " + ex.getMessage());
        }
    }

    @Test
    void sqlRequestHTML_robustness_2() {
        try {
            doThrow(new SQLException("Expected Exception")).when(mockSet).close();

            SQLController ctrl = new SQLController(mockSource);
            try {
                ctrl.sqlRequestHTML("SELECT * FROM applications;", mockModel);
                fail("Did not throw expected ResponseStatusException");
            } catch (ResponseStatusException ex) {
                assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
            }

            boolean test = false;
            for(ILoggingEvent event : listAppender.list)
                if(event.getMessage().equals("Exception occurred: Expected Exception"))
                    test = true;

            assertTrue(test, "Did not find expected exception in logger output");
        } catch (Exception ex) {
            fail("Unexpected Exception: " + ex.getMessage());
        }
    }

    @Test
    void sqlRequestHTML_robustness_3() {
        try {
            when(mockSet.getMetaData()).thenThrow(new SQLException("Expected Exception"));

            SQLController ctrl = new SQLController(mockSource);
            try {
                ctrl.sqlRequestHTML("SELECT * FROM applications;", mockModel);
                fail("Did not throw expected ResponseStatusException");
            } catch (ResponseStatusException ex) {
                assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
            }

            boolean test = false;
            for(ILoggingEvent event : listAppender.list)
                if(event.getMessage().equals("Exception occurred: Expected Exception"))
                    test = true;

            assertTrue(test, "Did not find expected exception in logger output");
        } catch (Exception ex) {
            fail("Unexpected Exception: " + ex.getMessage());
        }
    }

    @Test
    void sqlRequestJSON_functionality_1() {
        try {
            SQLController ctrl = new SQLController(mockSource);
            assertEquals(demoJSONResult, ctrl.sqlRequestJSON("SELECT * FROM applications;"));
        } catch (ResponseStatusException ex) {
            fail("Unexpected Response status exception with code: " + ex.getStatus());
        } catch (Exception ex) {
            fail("Unexpected Exception: " + ex.getMessage());
        }
    }

    @Test
    void sqlRequestJSON_robustness_1() {
        try {
            SQLController ctrl = new SQLController(mockSource);
            ctrl.sqlRequestJSON("");
            fail("Did not throw a ResponseStatusException");
        } catch (ResponseStatusException ex) {
            assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus(), "Did throw wrong HTTP Status code: " + ex.getStatus());
        } catch (Exception ex) {
            fail("Unexpected Exception: " + ex.getMessage());
        }
    }

    @Test
    void sqlRequestJSON_robustness_2() {
        try {
            doThrow(new SQLException("Expected Exception")).when(mockSet).close();

            SQLController ctrl = new SQLController(mockSource);
            try {
                ctrl.sqlRequestJSON("SELECT * FROM applications;");
                fail("Did not throw expected ResponseStatusException");
            } catch (ResponseStatusException ex) {
                assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
            }

            boolean test = false;
            for(ILoggingEvent event : listAppender.list) {
                System.out.println(event.getMessage());
                if (event.getMessage().equals("Exception occurred: Expected Exception"))
                    test = true;
            }

            assertTrue(test, "Did not find expected exception in logger output");
        } catch (Exception ex) {
            fail("Unexpected Exception: " + ex.getMessage());
        }
    }

    @Test
    void sqlRequestJSON_robustness_3() {
        try {
            when(mockSet.getMetaData()).thenThrow(new SQLException("Expected Exception"));

            SQLController ctrl = new SQLController(mockSource);
            try {
                ctrl.sqlRequestJSON("SELECT * FROM applications;");
                fail("Did not throw expected ResponseStatusException");
            } catch (ResponseStatusException ex) {
                assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
            }

            boolean test = false;
            for(ILoggingEvent event : listAppender.list)
                if(event.getMessage().equals("Exception occurred: Expected Exception"))
                    test = true;

            assertTrue(test, "Did not find expected exception in logger output");
        } catch (Exception ex) {
            fail("Unexpected Exception: " + ex.getMessage());
        }
    }

    @Test
    void sqlRequestXML_functionality_1() {
        try {
            SQLController ctrl = new SQLController(mockSource);
            assertEquals(demoXMLResult, ctrl.sqlRequestXML("SELECT * FROM applications;"));
        } catch (ResponseStatusException ex) {
            fail("Unexpected Response status exception with code: " + ex.getStatus());
        } catch (Exception ex) {
            ex.printStackTrace();
            fail("Unexpected Exception: " + ex.getMessage());
        }
    }

    @Test
    void sqlRequestXML_robustness_1() {
        try {
            SQLController ctrl = new SQLController(mockSource);
            ctrl.sqlRequestJSON("");
            fail("Did not throw a ResponseStatusException");
        } catch (ResponseStatusException ex) {
            assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus(), "Did throw wrong HTTP Status code: " + ex.getStatus());
        } catch (Exception ex) {
            fail("Unexpected Exception: " + ex.getMessage());
        }
    }

    @Test
    void sqlRequestXML_robustness_2() {
        try {
            doThrow(new SQLException("Expected Exception")).when(mockSet).close();

            SQLController ctrl = new SQLController(mockSource);
            try {
                ctrl.sqlRequestXML("SELECT * FROM applications;");
                fail("Did not throw expected ResponseStatusException");
            } catch (ResponseStatusException ex) {
                assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
            }

            boolean test = false;
            for(ILoggingEvent event : listAppender.list)
                if(event.getMessage().equals("Exception occurred: Expected Exception"))
                    test = true;

            assertTrue(test, "Did not find expected exception in logger output");
        } catch (Exception ex) {
            fail("Unexpected Exception: " + ex.getMessage());
        }
    }

    @Test
    void sqlRequestXML_robustness_3() {
        try {
            when(mockSet.getMetaData()).thenThrow(new SQLException("Expected Exception"));

            SQLController ctrl = new SQLController(mockSource);
            try {
                ctrl.sqlRequestXML("SELECT * FROM applications;");
                fail("Did not throw expected ResponseStatusException");
            } catch (ResponseStatusException ex) {
                assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
            }

            boolean test = false;
            for(ILoggingEvent event : listAppender.list)
                if(event.getMessage().equals("Exception occurred: Expected Exception"))
                    test = true;

            assertTrue(test, "Did not find expected exception in logger output");
        } catch (Exception ex) {
            fail("Unexpected Exception: " + ex.getMessage());
        }
    }
}