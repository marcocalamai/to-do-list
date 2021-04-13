package org.marco.calamai.todolist.repositories.mongo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.Optional;

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
	
	@Nested
	@DisplayName("Tests for find Users")
	class findUsers{
	
	    @Test @DisplayName("Find User by username")
	    void testFindbyUsername(){
	    	User toSave = new User("username", "password", "USER");
	    	User saved = mongoOps.save(toSave);
	    	Optional<User> result = repository.findByUsername("username");
	    	assertThat(result).isPresent();
	    	assertEquals(saved, result.get());
	    }
	    
	    @Test @DisplayName("Find User by username when there is not")
	    void testFindbyUsernameWhenThereIsNot() {
	    	User toSave = new User("username", "password", "USER");
	    	mongoOps.save(toSave);
			Optional<User> UserFound = repository.findByUsername("usernameToFound");
			assertThat(UserFound).isNotPresent();
	    }
	}

}
