package org.marco.calamai.todolist.services;

import java.math.BigInteger;
import java.time.LocalDate;

import org.marco.calamai.todolist.model.ToDo;
import org.marco.calamai.todolist.repositories.mongo.ToDoMongoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("toDoService")
public class ToDoService {
	
	@Autowired
	private ToDoMongoRepository toDoMongoRepository;
	
	public ToDo insertToDo(ToDo toDo) {
		if (toDo.getDeadline().isBefore(LocalDate.now())){
			throw new IllegalArgumentException("Past date");
		}
		toDo.setId(null);
		return toDoMongoRepository.save(toDo);
	}

	public ToDo updateToDo(BigInteger id, ToDo toDo) {
		if (toDo.getDeadline().isBefore(LocalDate.now())){
			throw new IllegalArgumentException("Past date");
		}
		toDo.setId(id);
		return toDoMongoRepository.save(toDo);
	}
	
}
