package org.marco.calamai.todolist.repositories.mongo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.marco.calamai.todolist.model.ToDo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.test.context.junit.jupiter.SpringExtension;


@ExtendWith(SpringExtension.class)
@DataMongoTest
@DisplayName("Tests for ToDoMongoRepository")
class ToDoMongoRepositoryTest {


	@Autowired 
	private MongoOperations mongoOps;
   
    
	@Autowired
	private ToDoMongoRepository repository;
	
	@BeforeEach
	void setup(){
		mongoOps.dropCollection(ToDo.class);
	}
    
	@Nested
	@DisplayName("Tests for save ToDo")
	class saveToDo{
	    @Test @DisplayName("Save new ToDo")
	    void testSave(){
	    	ToDo toSave = new ToDo("username_1", "title_1", "description_1", LocalDate.now());
	    	ToDo saved = repository.save(toSave);
	    	List<ToDo> toDoList = mongoOps.findAll(ToDo.class);
	    	assertThat(toDoList).containsExactly(saved);
	    	assertNotNull(toDoList.get(0).getId()); 
	    }
	    
	    @Test @DisplayName("Save inserted ToDo")
	   void testSaveWithSameIdShouldUpdate() {
	   	ToDo toDo1 = new ToDo("username_1", "title_1", "description_1", LocalDate.now());
	   	ToDo saved = repository.save(toDo1);
	   	ToDo toDo2 = new ToDo("username_2", "title_2", "description_2", LocalDate.now());
	   	BigInteger id = saved.getId();
	   	toDo2.setId(id);
	   	ToDo updated = repository.save(toDo2);
	   	List<ToDo> toDoList = mongoOps.findAll(ToDo.class);
	   	assertThat(toDoList).containsExactly(updated);
	   	assertEquals(id, updated.getId());
	   }
	}
	@Nested
	@DisplayName("Tests for find ToDo")
	class findToDo{
		
		@Test @DisplayName("Find ToDo by id")
		void testFindById() {
    	ToDo toDo1 = new ToDo("username_1", "title_1", "description_1", LocalDate.now());
    	BigInteger id = new BigInteger("0");
    	toDo1.setId(id);
    	ToDo toDoSaved = mongoOps.save(toDo1);
    	Optional<ToDo> toDoFound = repository.findById(id);
    	assertThat(toDoFound).isPresent();
    	assertEquals(toDoSaved, toDoFound.get());
		}
		
		@Test @DisplayName("Find ToDo by id when there is not")
		void testFindByIdWhenThereIsNot() {
	    	ToDo toSave = new ToDo("username_1", "title_1", "description_1", LocalDate.now());
	    	BigInteger id = new BigInteger("0");
	    	toSave.setId(id);
	    	mongoOps.save(toSave);
			Optional<ToDo> toDoFound = repository.findById(new BigInteger("1"));
			assertThat(toDoFound).isNotPresent();
		}
		
		@Test @DisplayName("Find all ToDo")
		void testFindAll() {
			ToDo toDo1 = new ToDo("username_1", "title_1", "description_1", LocalDate.now());
			ToDo toDo2 = new ToDo("username_2", "title_2", "description_2", LocalDate.now().plusDays(1));
			ToDo toDoSaved1 = mongoOps.save(toDo1);
			ToDo toDoSaved2 = mongoOps.save(toDo2);
			List<ToDo> result = repository.findAll();
			assertThat(result).containsExactly(toDoSaved1, toDoSaved2);
		}
		
		@Test @DisplayName("Find all ToDo when repository is empty")
		void testFindAllWhenRepositoryIsEmpty() {
			List<ToDo> result = repository.findAll();
			assertEquals(0, result.size());
		}
		
		@Test @DisplayName("Find all ToDo ordered by Done and deadline ")
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
	}
		
	@Nested
	@DisplayName("Tests for delete ToDo")
	class deleteToDo{
		
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
		
		@Test @DisplayName("Delete ToDo by id when there is not")
		void testDeleteByIdWhenThereIsNot() {
			ToDo toDo1 = new ToDo("username_1", "title_1", "description_1", LocalDate.now());
			ToDo toDo2 = new ToDo("username_2", "title_2", "description_2", LocalDate.now().plusDays(1));
			toDo1.setId(new BigInteger("0"));
			toDo1.setId(new BigInteger("1"));
			ToDo toDoSaved1 = mongoOps.save(toDo1);
			ToDo toDoSaved2 = mongoOps.save(toDo2);
			List<ToDo> result = mongoOps.findAll(ToDo.class);
			assertThat(result).containsExactly(toDoSaved1, toDoSaved2);
			repository.deleteById(new BigInteger("2"));
			assertThat(result).containsExactly(toDoSaved1, toDoSaved2);
		}
	}
}
