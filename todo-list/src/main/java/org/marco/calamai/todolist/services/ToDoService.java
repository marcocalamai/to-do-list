package org.marco.calamai.todolist.services;

import java.time.LocalDate;

import org.marco.calamai.todolist.model.ToDo;
import org.springframework.stereotype.Service;

@Service("toDoService")
public class ToDoService {
	
	public ToDo createAndSaveToDo(String user, String title, String description, LocalDate deadline) {
		if (deadline.isBefore(LocalDate.now())){
			throw new IllegalArgumentException("Past date");
		}
		return new ToDo(user, title, description, deadline);
	}
	
}
