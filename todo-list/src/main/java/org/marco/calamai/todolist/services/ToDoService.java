package org.marco.calamai.todolist.services;

import java.time.LocalDate;

import org.marco.calamai.todolist.model.ToDo;
import org.marco.calamai.todolist.model.User;
import org.springframework.stereotype.Service;

@Service("toDoService")
public class ToDoService {
	
	public ToDo createToDo(User user, String title, String description, LocalDate deadline) {
		return new ToDo(user, title, description, deadline);
	}
	
}
