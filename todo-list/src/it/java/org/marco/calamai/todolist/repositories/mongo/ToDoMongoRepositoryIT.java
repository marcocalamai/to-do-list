package org.marco.calamai.todolist.repositories.mongo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.marco.calamai.todolist.model.ToDo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestPropertySource(properties = "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration")
class ToDoMongoRepositoryIT {
	
	@Autowired 
	private MongoOperations mongoOps;
	
	@Autowired
	private ToDoMongoRepository repository;
	
	@BeforeEach
	void setup(){
		mongoOps.dropCollection(ToDo.class);
	}
	
	@AfterEach
	void tearDown(){
		mongoOps.dropCollection(ToDo.class);
	}
	
	

    @Test @DisplayName("Save new ToDo")
    void testSave(){
    	ToDo toSave = new ToDo("username_1", "title_1", "description_1", LocalDate.now());
    	ToDo saved = repository.save(toSave);
    	List<ToDo> toDoList = mongoOps.findAll(ToDo.class);
    	assertThat(toDoList).containsExactly(saved);
    	assertNotNull(toDoList.get(0).getId()); 
    }
    
	@Test @DisplayName("Find ToDo by id")
	void testFindById() {
	ToDo toDo1 = new ToDo("username_1", "title_1", "description_1", LocalDate.now());
	ToDo toDoSaved = mongoOps.save(toDo1);
	Optional<ToDo> toDoFound = repository.findById(toDoSaved.getId());
	assertThat(toDoFound).isPresent();
	assertEquals(toDoSaved, toDoFound.get());
	}
	
}
