package org.marco.calamai.todolist.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.awaitility.Awaitility;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.marco.calamai.todolist.exceptions.ToDoNotFoundException;
import org.marco.calamai.todolist.model.ToDo;
import org.marco.calamai.todolist.repositories.mongo.ToDoMongoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.UncategorizedMongoDbException;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestPropertySource(properties = "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration")
@DisplayName("IT for ToDoService")
class ToDoServiceIT {

	@Autowired
	private ToDoService toDoService;

	@Autowired
	private ToDoMongoRepository toDoMongoRepository;

	@BeforeEach
	void setup() {
		toDoMongoRepository.deleteAll();
	}

	@AfterEach
	void tearDown() {
		toDoMongoRepository.deleteAll();
	}

	@Test
	@DisplayName("Tests for insert ToDo")
	void testInsertToDo() {
		ToDo toSave = new ToDo("username_1", "title", "description", LocalDate.now());
		ToDo toDoSaved = toDoService.insertToDo(toSave);

		assertEquals(1, toDoMongoRepository.count());

		ToDo result = toDoMongoRepository.findAll().get(0);
		assertEquals(toSave.getUser(), result.getUser());
		assertEquals(toSave.getTitle(), result.getTitle());
		assertEquals(toSave.getDescription(), result.getDescription());
		assertEquals(toSave.getDeadline(), result.getDeadline());

		assertEquals(result, toDoSaved);
	}

	@Test
	@DisplayName("Test update ToDo")
	void testUpdateToDo() {
		ToDo ToDoSaved = toDoMongoRepository.save(new ToDo("username_1", "title", "description", LocalDate.now()));

		ToDo ToDoModified = toDoService.updateToDo(ToDoSaved.getId(), "username_1",
				new ToDo("username_1", "title_modified", "description_modified", LocalDate.now().plusDays(1)));
		assertEquals(ToDoModified, toDoMongoRepository.findById(ToDoSaved.getId()).get());
	}

	@Test
	@DisplayName("Get all ToDo sorted by done and deadline")
	void testGetAllToDoOrderByDoneAscDeadlineAsc() {
		ToDo toDo1 = new ToDo("username_1", "title_1", "description_1", LocalDate.now().plusMonths(1));
		ToDo toDo2 = new ToDo("username_2", "title_2", "description_2", LocalDate.now().plusDays(1));
		ToDo toDo3 = new ToDo("username_3", "title_3", "description_3", LocalDate.now());
		toDo2.setDone(true);
		toDoMongoRepository.save(toDo1);
		toDoMongoRepository.save(toDo2);
		toDoMongoRepository.save(toDo3);
		List<ToDo> result = toDoService.getAllToDoOrderByDoneAscDeadlineAsc();
		assertThat(result).containsExactly(toDo3, toDo1, toDo2);
	}

	@Test
	@DisplayName("Get all ToDo by user name sorted by done and deadline")
	void testGetToDoByUserOrderByDoneAscDeadlineAsc() {
		ToDo toDo1 = new ToDo("username_1", "title_1", "description_1", LocalDate.now().plusMonths(1));
		ToDo toDo2 = new ToDo("username_1", "title_2", "description_2", LocalDate.now().plusDays(1));
		ToDo toDo3 = new ToDo("username_3", "title_3", "description_3", LocalDate.now());
		toDoMongoRepository.save(toDo1);
		toDoMongoRepository.save(toDo2);
		toDoMongoRepository.save(toDo3);
		List<ToDo> result = toDoService.getToDoByUserOrderByDoneAscDeadlineAsc("username_1");
		assertThat(result).containsExactly(toDo2, toDo1);
	}

	@Test
	@DisplayName("Get all ToDo by deadline sorted by done")
	void testGetAllToDoByDeadlineOrderByDoneAsc() {
		ToDo toDo1 = new ToDo("username_1", "title_1", "description_1", LocalDate.now().plusDays(1));
		ToDo toDo2 = new ToDo("username_2", "title_2", "description_2", LocalDate.now());
		ToDo toDo3 = new ToDo("username_3", "title_3", "description_3", LocalDate.now());
		toDo2.setDone(true);
		toDoMongoRepository.save(toDo1);
		toDoMongoRepository.save(toDo2);
		toDoMongoRepository.save(toDo3);
		List<ToDo> result = toDoService.getAllToDoByDeadlineOrderByDoneAsc(LocalDate.now());
		assertThat(result).containsExactly(toDo3, toDo2);
	}

	@Test
	@DisplayName("Get all ToDo by title sorted by done and deadline")
	void testGetAllToDoByTitleOrderByDoneAscDeadlineAsc() {
		ToDo toDo1 = new ToDo("username_1", "title_1", "description_1", LocalDate.now().plusDays(1));
		ToDo toDo2 = new ToDo("username_2", "title_1", "description_2", LocalDate.now());
		ToDo toDo3 = new ToDo("username_3", "title_3", "description_3", LocalDate.now());
		toDoMongoRepository.save(toDo1);
		toDoMongoRepository.save(toDo2);
		toDoMongoRepository.save(toDo3);
		List<ToDo> result = toDoService.getAllToDoByTitleOrderByDoneAscDeadlineAsc("title_1");
		assertThat(result).containsExactly(toDo2, toDo1);
	}

	@Test
	@DisplayName("Get ToDo by id")
	void testGetToDoById() {
		ToDo toDo1 = new ToDo("username1", "title_1", "description_1", LocalDate.now());
		ToDo toDoSaved = toDoMongoRepository.save(toDo1);

		ToDo result = toDoService.getToDoById(toDoSaved.getId());
		assertEquals(toDoSaved, result);
	}

	@Test
	@DisplayName("Delete ToDo by id")
	void testDeleteToDoById() {
		ToDo toDo = new ToDo("username_1", "title_1", "description_1", LocalDate.now());
		ToDo toDoSaved = toDoMongoRepository.save(toDo);

		assertEquals(1, toDoMongoRepository.count());

		ToDo result = toDoService.deleteToDoById(toDoSaved.getId(), "username_1");

		assertEquals(0, toDoMongoRepository.count());
		assertEquals(toDoSaved, result);
	}

	@Test
	@DisplayName("Delete ToDo by id in transaction")
	void testDeleteToDoByIdInTransaction() {
		final int NUM_THREAD = 10;
		ToDo toDo = new ToDo("username_1", "title_1", "description_1", LocalDate.now());
		ToDo toDoSaved = toDoMongoRepository.save(toDo);

		assertEquals(1, toDoMongoRepository.count());
		
		Collection<Exception> exc = new ConcurrentLinkedQueue<>();
		List<Thread> threads = IntStream.range(0, NUM_THREAD).mapToObj(i -> new Thread(() -> {
			try {
				toDoService.deleteToDoById(toDoSaved.getId(), "username_1");
			}
			catch(ToDoNotFoundException  | UncategorizedMongoDbException e) {
				exc.add(e);
			}
		})).peek(t -> t.start()).collect(Collectors.toList());
		Awaitility.await().atMost(10, TimeUnit.SECONDS).until(() -> threads.stream().noneMatch(t -> t.isAlive()));
		
		assertEquals(0, toDoMongoRepository.count());
		assertEquals(NUM_THREAD-1, exc.size());
	}

}