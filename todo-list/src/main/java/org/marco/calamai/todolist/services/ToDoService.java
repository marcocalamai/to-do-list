package org.marco.calamai.todolist.services;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.marco.calamai.todolist.exceptions.InvalidTimeException;
import org.marco.calamai.todolist.exceptions.ToDoNotFoundException;
import org.marco.calamai.todolist.model.ToDo;
import org.marco.calamai.todolist.repositories.mongo.ToDoMongoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("toDoService")
public class ToDoService {
	
	public static final String DATE_IS_BEFORE_TODAY = "Date not valid. It has passed!";
	public static final String TO_DO_NOT_FOUND = "ToDo not found!";

	
	@Autowired
	private ToDoMongoRepository toDoMongoRepository;
	
	public ToDo insertToDo(ToDo toDo) {
		if (toDo.getDeadline().isBefore(LocalDate.now())){
			throw new InvalidTimeException(DATE_IS_BEFORE_TODAY);
		}
		toDo.setId(null);
		return toDoMongoRepository.save(toDo);
	}

	public ToDo updateToDo(BigInteger id, ToDo toDo) {
		if (toDo.getDeadline().isBefore(LocalDate.now())){
			throw new InvalidTimeException(DATE_IS_BEFORE_TODAY);
		}
		toDo.setId(id);
		return toDoMongoRepository.save(toDo);
	}

	public List<ToDo> findToDoByUser(String username) {
		List<ToDo> allToDo = toDoMongoRepository.findAll();
		List<ToDo> toDoByUser = new ArrayList<>();
		for (ToDo toDo : allToDo) {
			if (toDo.getUser().equals(username)) {
				toDoByUser.add(toDo);
				}
			}
		return toDoByUser;
	}

	public List<ToDo> findAllToDo() {
		return toDoMongoRepository.findAll();
	}

	public ToDo findToDoById(BigInteger id) {
		Optional<ToDo> toDo = toDoMongoRepository.findById(id);
		if (toDo.isPresent()){
			return toDo.get();
			}
		throw new ToDoNotFoundException(TO_DO_NOT_FOUND);
	}
	
}
