package com.url.shortener.integration.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;

@TestConfiguration
public class TestMongoConfig {

    @Value("${spring.data.mongodb.host}")
    private int mongoHost;

    @Value("${spring.data.mongodb.port}")
    private int mongoPort;

    @Value("${spring.data.mongodb.testdatabase}")
    private int database;

    @Bean
    public MongoClient mongoClient() {
        return MongoClients.create("mongodb://"+mongoHost+":"+mongoPort+"/"+database); // Specify your test database URL
    }

    @Bean
    public MongoTemplate mongoTemplate(MongoClient mongoClient) {
        return new MongoTemplate(mongoClient, "testdatabase");
    }
}

