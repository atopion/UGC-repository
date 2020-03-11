package com.atopion.UGC_repository.rest;

import com.atopion.UGC_repository.testutil.MockResultSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class CSVSerializerTest {

    private ResultSet testSet1;
    private String testResult1 = "Header 1,Header 2\nCell 1.1,Cell 1.2\nCell 2.1,Cell 2.2";

    private ResultSet testSet2;
    private String testResult2 = "Header 1,Header 2\nCell 1,Cell 2";

    private ResultSet testSet3;
    private String testResult3 = "";

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
    void serialize_test_1() {
        try {
            String result = CSVSerializer.serialize(testSet1);
            assertEquals(testResult1, result);
        } catch (SQLException ex) {
            fail("Unexpected Exception: " + ex.getMessage());
        }
    }

    @Test
    void serialize_test_2() {
        try {
            String result = CSVSerializer.serialize(testSet2);
            assertEquals(testResult2, result);
        } catch (SQLException ex) {
            fail("Unexpected Exception: " + ex.getMessage());
        }
    }

    @Test
    void serialize_test_3() {
        try {
            String result = CSVSerializer.serialize(testSet3);
            assertEquals(testResult3, result);
        } catch (SQLException ex) {
            fail("Unexpected Exception: " + ex.getMessage());
        }
    }
}