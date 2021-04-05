package org.marco.calamai.todolist.services;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.marco.calamai.todolist.model.ToDo;
class ToDoServiceTest {
	
	private ToDoService toDoService;

	@BeforeEach
	void setup() {
		toDoService = new ToDoService();
	}
	
	@Test
	void testCreateAndSaveToDo() {
		LocalDate deadline = LocalDate.now();
		String user1 = "username_1";
		ToDo result = toDoService.createAndSaveToDo(user1, "a_title", "a_description", deadline);
		assertNotNull(result);
		assertEquals(user1, result.getUser());
		assertEquals("a_title", result.getTitle());
		assertEquals("a_description", result.getDescription());
		assertThat(result.isDone()).isFalse();
		assertEquals(deadline, result.getDeadline());
	}
	
	@Test
	void testCreateAndSaveToDoWithDateBeforeTodayShouldThrow() {
		LocalDate deadline = LocalDate.now().minusDays(1);
		String user1 = "username_1";
		assertThatThrownBy(() -> toDoService.createAndSaveToDo(user1, "a_title", "a_description", deadline))
		.isInstanceOf(IllegalArgumentException.class)
		.hasMessage("Past date");
	}	
	
}
