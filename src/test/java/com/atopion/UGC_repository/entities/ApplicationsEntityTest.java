package com.atopion.UGC_repository.entities;

import org.junit.jupiter.api.Test;

import static org.junit.Assert.*;

class ApplicationsEntityTest {

    private ApplicationsEntity demo0          = new ApplicationsEntity(0, "demo0");
    private ApplicationsEntity demo1          = new ApplicationsEntity(1, "demo1");
    private ApplicationsEntity demoMAX = new ApplicationsEntity(Integer.MAX_VALUE, "demoMAX");
    private ApplicationsEntity demoMIN = new ApplicationsEntity(Integer.MIN_VALUE, "demoMIN");

    @Test
    void getApplication_id() {
        assertEquals("Did not find expected result. ",0, demo0.getApplication_id());
        assertEquals("Did not find expected result. ",1, demo1.getApplication_id());
        assertEquals("Did not find expected result. ", Integer.MAX_VALUE, demoMAX.getApplication_id());
        assertEquals("Did not find expected result. ", Integer.MIN_VALUE, demoMIN.getApplication_id());
    }

    @Test
    void setApplication_id() {
        demoMAX.setApplication_id(9);
        demoMIN.setApplication_id(-1);

        assertEquals("Did not find expected result. ", 9, demoMAX.getApplication_id());
        assertEquals("Did not find expected result. ", -1, demoMIN.getApplication_id());
    }

    @Test
    void getApplication_name() {
        assertEquals("Did not find expected result. ","demo0", demo0.getApplication_name());
        assertEquals("Did not find expected result. ","demo1", demo1.getApplication_name());
        assertEquals("Did not find expected result. ", "demoMAX", demoMAX.getApplication_name());
        assertEquals("Did not find expected result. ", "demoMIN", demoMIN.getApplication_name());
    }

    @Test
    void setApplication_name() {
        demoMAX.setApplication_name("HALLO");
        demoMIN.setApplication_name("");

        assertEquals("Did not find expected result. ", "HALLO", demoMAX.getApplication_name());
        assertEquals("Did not find expected result. ", "", demoMIN.getApplication_name());
    }

    @Test
    void testEquals() {
        ApplicationsEntity test = new ApplicationsEntity(0, "demo0");

        assertEquals("Objects where not equal. ", demo0, test);
    }

    @Test
    void testHashCode() {
        ApplicationsEntity test = new ApplicationsEntity(0, "demo0");

        assertEquals("Objects hash codes where not equal. ", demo0.hashCode(), test.hashCode());
    }

    @Test
    void testToString() {
        ApplicationsEntity test = new ApplicationsEntity(0, "demo0");

        assertEquals("Objects strings where not equal. ", demo0.toString(), test.toString());
        assertEquals("Unexpected toString() result. ", "ApplicationsEntity{application_id=0, application_name='demo0'}", test.toString());
    }
}