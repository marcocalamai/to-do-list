package org.marco.calamai.todolist.repositories.mongo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.marco.calamai.todolist.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@DataMongoTest
@DisplayName("Tests for UserMongoRepository")
class UserMongoRepositoryTest {
	
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
	
	@Nested
	@DisplayName("Tests for save User")
	class SaveUser{
	
	    @Test @DisplayName("Save new User")
	    void testSave(){
	    	User toSave = new User("username", "password");
	    	User saved = repository.save(toSave);
	    	List<User> result = mongoOps.findAll(User.class);
	    	assertThat(result).containsExactly(saved);
	    	assertNotNull(result.get(0).getId()); 
	    }
	}
	
	@Nested
	@DisplayName("Tests for find Users")
	class findUsers{
	
	    @Test @DisplayName("Find User by username")
	    void testFindbyUsername(){
	    	User user1 = new User("username", "password");
	    	User saved = mongoOps.save(user1);
	    	Optional<User> result = repository.findByUsername("username");
	    	assertThat(result).isPresent();
	    	assertEquals(saved, result.get());
	    }
	    
	    @Test @DisplayName("Find User by username when there is not")
	    void testFindbyUsernameWhenThereIsNot() {
	    	User toSave = new User("username", "password");
	    	mongoOps.save(toSave);
			Optional<User> UserFound = repository.findByUsername("usernameToFound");
			assertThat(UserFound).isNotPresent();
	    }
	}

}
