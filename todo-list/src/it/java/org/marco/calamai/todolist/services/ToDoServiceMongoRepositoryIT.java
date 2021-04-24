package org.marco.calamai.todolist.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.marco.calamai.todolist.model.ToDo;
import org.marco.calamai.todolist.repositories.mongo.ToDoMongoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestPropertySource(properties = "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration")

public class ToDoServiceMongoRepositoryIT {
	
	private ToDoService toDoService;
	
	@Autowired
	private ToDoMongoRepository toDoMongoRepository;
	
	@BeforeEach
	void setup() {
		toDoMongoRepository.deleteAll();
		toDoService = new ToDoService(toDoMongoRepository);
	}
	
	
	@Nested
	@DisplayName("Tests for insert ToDo")
	class InsertToDo{
		
		@Test @DisplayName("Insert happy case")
		void testInsertToDo() {
			ToDo toSave = new ToDo("username_1", "title", "description", LocalDate.now());
			toDoService.insertToDo(toSave);
			
			assertEquals(1, toDoMongoRepository.count());
			
			ToDo saved = toDoMongoRepository.findAll().get(0);
			assertThat(saved.getId()).isNotNull();
			assertEquals(toSave.getUser(), saved.getUser());
			assertEquals(toSave.getTitle(), saved.getTitle());
			assertEquals(toSave.getDescription(), saved.getDescription());
			assertEquals(toSave.getDeadline(), saved.getDeadline());
		}
	
	
	}	
	
	
	
}
