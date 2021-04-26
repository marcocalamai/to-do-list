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
@DisplayName("IT for ToDoMongoRepository")
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
	
	@Test @DisplayName("Find all ToDo sort by done and deadline")
	void testFindByOrderByDoneAscDeadlineAsc() {
		ToDo toDo1 = new ToDo("username_1", "title_1", "description_1", LocalDate.now().plusMonths(1));
		ToDo toDo2 = new ToDo("username_2", "title_2", "description_2", LocalDate.now().plusDays(1));
		ToDo toDo3 = new ToDo("username_3", "title_3", "description_3", LocalDate.now());
		toDo2.setDone(true);
		ToDo toDoSaved1 = mongoOps.save(toDo1);
		ToDo toDoSaved2 = mongoOps.save(toDo2);
		ToDo toDoSaved3 = mongoOps.save(toDo3);
		List<ToDo> result = repository.findByOrderByDoneAscDeadlineAsc();
		assertThat(result).containsExactly(toDoSaved3, toDoSaved1, toDoSaved2);
	}
	
	@Test @DisplayName("Find ToDo by user name sort by done and deadline")
	void testFindByUserOrderByDoneAscDeadlineAsc() {
		String user1 = "a_username"; 
		String userToFind = "usernameToFind";
		ToDo toDo1 = new ToDo(user1, "title_1", "description_1", LocalDate.now());
		ToDo toDo2 = new ToDo(userToFind, "title_2", "description_2", LocalDate.now().plusMonths(1));
		ToDo toDo3 = new ToDo(userToFind, "title_3", "description_3", LocalDate.now().plusDays(1));
		ToDo toDo4 = new ToDo(userToFind, "title_4", "description_4", LocalDate.now());
		toDo3.setDone(true);
		mongoOps.save(toDo1);
		ToDo toDoSaved2 = mongoOps.save(toDo2);
		ToDo toDoSaved3 = mongoOps.save(toDo3);
		ToDo toDoSaved4 = mongoOps.save(toDo4);
		List<ToDo> result = repository.findByUserOrderByDoneAscDeadlineAsc(userToFind);
		assertThat(result).containsExactly(toDoSaved4, toDoSaved2, toDoSaved3);
	}
	
	@Test @DisplayName("Find ToDo by deadline sort by done")
	void testFindByDeadlineOrderByDoneAsc() {
		ToDo toDo1 = new ToDo("username_1", "title_1", "description_1", LocalDate.now());
		ToDo toDo2 = new ToDo("a_username_2", "title_2", "description_2", LocalDate.now());
		ToDo toDo3 = new ToDo("a_username_3", "title_3", "description_3", LocalDate.now());
		ToDo toDo4 = new ToDo("a_username_4", "title_4", "description_4", LocalDate.now().plusDays(1));
		toDo2.setDone(true);
		ToDo toDoSaved1 = mongoOps.save(toDo1);
		ToDo toDoSaved2 = mongoOps.save(toDo2);
		ToDo toDoSaved3 = mongoOps.save(toDo3);
		mongoOps.save(toDo4);
		List<ToDo> result = repository.findByDeadlineOrderByDoneAsc(LocalDate.now());
		assertThat(result).containsExactly(toDoSaved1, toDoSaved3, toDoSaved2);
	}
	
	@Test @DisplayName("Find ToDo by title sort by done and deadline")
	void testFindByTitleOrderByDoneAscDeadlineAsc() {
		String titleToFind = "title_to_find";
		ToDo toDo1 = new ToDo("username_1", "title_1", "description_1", LocalDate.now());
		ToDo toDo2 = new ToDo("username_2", titleToFind, "description_2", LocalDate.now().plusMonths(1));
		ToDo toDo3 = new ToDo("username_3", titleToFind, "description_3", LocalDate.now().plusDays(1));
		ToDo toDo4 = new ToDo("username_4", titleToFind, "description_4", LocalDate.now());
		toDo3.setDone(true);
		mongoOps.save(toDo1);
		ToDo toDoSaved2 = mongoOps.save(toDo2);
		ToDo toDoSaved3 = mongoOps.save(toDo3);
		ToDo toDoSaved4 = mongoOps.save(toDo4);
		List<ToDo> result = repository.findByTitleOrderByDoneAscDeadlineAsc(titleToFind);
		assertThat(result).containsExactly(toDoSaved4, toDoSaved2, toDoSaved3);
	}
	
	@Test @DisplayName("Delete ToDo by id")
	void testDeleteById() {
		ToDo toDo1 = new ToDo("username_1", "title_1", "description_1", LocalDate.now());
		ToDo toDo2 = new ToDo("username_2", "title_2", "description_2", LocalDate.now().plusDays(1));
		ToDo toDo3 = new ToDo("username_3", "title_3", "description_3", LocalDate.now().plusMonths(1));
		ToDo toDoSaved1 = mongoOps.save(toDo1);
		ToDo toDoSaved2 = mongoOps.save(toDo2);
		ToDo toDoSaved3 = mongoOps.save(toDo3);
		repository.deleteById(toDoSaved1.getId());
		List<ToDo> result = mongoOps.findAll(ToDo.class);
		assertEquals(2, result.size());
		assertThat(result).containsExactly(toDoSaved2, toDoSaved3);	
	}
	
}
