package org.marco.calamai.todolist.repositories.mongo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.marco.calamai.todolist.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestPropertySource(properties = "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration")
@DisplayName("IT for UserMongoRepository")
class UserMongoRepositoryIT {
	
	@Autowired 
	private MongoOperations mongoOps;
	
	@Autowired
	private UserMongoRepository repository;
	
	@BeforeEach
	void setup(){
		mongoOps.dropCollection(User.class);
	}
	
	@AfterEach
	void tearDown(){
		mongoOps.dropCollection(User.class);
	}
	
    @Test @DisplayName("Save new User")
    void testSave(){
    	User toSave = new User("username", "password");
    	User saved = repository.save(toSave);
    	List<User> result = mongoOps.findAll(User.class);
    	assertThat(result).containsExactly(saved);
    	assertNotNull(result.get(0).getId()); 
    }

}
