package com.atopion.UGC_repository.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Files;
import java.nio.file.Paths;

@Configuration
public class MasterKeyConfig {

    @Bean
    public String masterKey() {
        try {
            if (Files.exists(Paths.get("/data/masterkey.txt"))) {
                return Files.readAllLines(Paths.get("/data/masterkey.txt")).get(0);
            } else {
                return Files.readAllLines(Paths.get("/home/atopi/IdeaProjects/UGC-repository/db/masterkey.txt")).get(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
