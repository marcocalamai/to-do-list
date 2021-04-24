package org.marco.calamai.todolist.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

@Configuration
public class MongoConfiguration {

	@Bean
    public MongoClient mongo() {
        return  MongoClients.create("mongodb://localhost:27017");
    }
}