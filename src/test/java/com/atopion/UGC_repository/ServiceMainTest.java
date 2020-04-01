package com.atopion.UGC_repository;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource("classpath:application-integrationtests.properties")
class ServiceMainTest {

    @Test
    void main() {
        ServiceMain.main(new String[] {});
    }
}