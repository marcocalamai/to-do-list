package org.marco.calamai.todolist.services;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.marco.calamai.todolist.model.ToDo;
import org.marco.calamai.todolist.model.User;
class ToDoServiceTest {
	
	private ToDoService toDoService;

	@BeforeEach
	void setup() {
		toDoService = new ToDoService();
	}
	
	@Test
	void testCreateToDoWithDateAfterTodayIsOk() {
		LocalDate deadline = LocalDate.now().plus(1, ChronoUnit.DAYS);
		User user1 = new User("username_1", "password_1");
		ToDo result = toDoService.createToDo(user1, "a_title", "a_description", deadline);
		assertNotNull(result);
		assertEquals(user1, result.getUser());
		assertEquals("a_title", result.getTitle());
		assertEquals("a_description", result.getDescription());
		assertEquals(deadline, result.getDeadline());
	}
}
