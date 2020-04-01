package com.atopion.UGC_repository.rest;

import com.atopion.UGC_repository.testutil.MockResultSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class JSONSerializerTest {

    private ResultSet testSet1;
    private String testResult1 = "{\"table\":\"test1\",\"data\":[{\"Header 1\":\"Cell 1.1\",\"Header 2\":\"Cell 1.2\"},{\"Header 1\":\"Cell 2.1\",\"Header 2\":\"Cell 2.2\"}]}";

    private ResultSet testSet2;
    private String testResult2 = "{\"table\":\"test2\",\"data\":[{\"Header 1\":\"Cell 1\",\"Header 2\":\"Cell 2\"}]}";

    private ResultSet testSet3;
    private String testResult3 = "{\"table\":\"test3\",\"data\":[]}";

    @BeforeEach
    void setUp() {
        try {
            testSet1 = MockResultSet.create(
                    new String[]{"Header 1", "Header 2"},
                    new String[][]{{"Cell 1.1", "Cell 1.2"}, {"Cell 2.1", "Cell 2.2"}}
            );

            testSet2 = MockResultSet.create(
                    new String[]{"Header 1", "Header 2"},
                    new String[][]{{"Cell 1", "Cell 2"}}
            );

            testSet3 = MockResultSet.create(
                    new String[]{},
                    new String[][]{}
            );

        } catch (SQLException ex) {
            fail("Unexpected Exception: " + ex.getMessage());
        }
    }

    @Test
    void serialize_test1() {
        try {
            String result = JSONSerializer.serialize(testSet1, "test1");
            assertEquals(testResult1, result);
        } catch (SQLException ex) {
            fail("Unexpected Exception: " + ex.getMessage());
        }
    }

    @Test
    void serialize_test2() {
        try {
            String result = JSONSerializer.serialize(testSet2, "test2");
            assertEquals(testResult2, result);
        } catch (SQLException ex) {
            fail("Unexpected Exception: " + ex.getMessage());
        }
    }

    @Test
    void serialize_test3() {
        try {
            String result = JSONSerializer.serialize(testSet3, "test3");
            assertEquals(testResult3, result);
        } catch (SQLException ex) {
            fail("Unexpected Exception: " + ex.getMessage());
        }
    }
}