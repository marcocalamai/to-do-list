package org.marco.calamai.todolist.services;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.marco.calamai.todolist.exceptions.InvalidTimeException;
import org.marco.calamai.todolist.exceptions.ToDoNotFoundException;
import org.marco.calamai.todolist.exceptions.WrongUsernameException;
import org.marco.calamai.todolist.model.ToDo;
import org.marco.calamai.todolist.repositories.mongo.ToDoMongoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("toDoService")
public class ToDoService {
	
	private static final String DATE_IS_BEFORE_TODAY = "Date not valid. It has passed!";
	private static final String TO_DO_NOT_FOUND = "ToDo not found!";
	private static final String WRONG_USERNAME = "Logged username do not match!";

	
	private ToDoMongoRepository toDoMongoRepository;
	
	
	@Autowired
	public ToDoService(ToDoMongoRepository toDoMongoRepository) {
		this.toDoMongoRepository = toDoMongoRepository;
	}

	public ToDo insertToDo(ToDo toDo) {
		deadlineCheck(toDo);
		toDo.setId(null);
		return toDoMongoRepository.save(toDo);
	}

	public ToDo updateToDo(BigInteger id, String username, ToDo toDo) {
		deadlineCheck(toDo);
		usernameCheck(username, toDo);
		toDo.setId(id);
		return toDoMongoRepository.save(toDo);
	}
	
	public ToDo deleteToDoById(BigInteger id, String username) {
		Optional<ToDo> toDo = toDoMongoRepository.findById(id);
		if (toDo.isPresent()){
			usernameCheck(username, toDo.get());
			toDoMongoRepository.deleteById(id);
			return toDo.get();
		}
		throw new ToDoNotFoundException(TO_DO_NOT_FOUND);	
	}

	public List<ToDo> getToDoByUserOrderByDoneAscDeadlineAsc(String username) {
		return  toDoMongoRepository.findByUserOrderByDoneAscDeadlineAsc(username);
	}

	public List<ToDo> getAllToDoOrderByDoneAscDeadlineAsc() {
		return toDoMongoRepository.findByOrderByDoneAscDeadlineAsc();
	}
	
	public List<ToDo> getAllToDoByDeadlineOrderByDoneAsc(LocalDate deadline) {
		return toDoMongoRepository.findByDeadlineOrderByDoneAsc(deadline);
	}
	
	public List<ToDo> getAllToDoByTitleOrderByDoneAscDeadlineAsc(String title) {
		return toDoMongoRepository.findByTitleOrderByDoneAscDeadlineAsc(title);
	}

	public ToDo getToDoById(BigInteger id) {
		Optional<ToDo> toDo = toDoMongoRepository.findById(id);
		if (toDo.isPresent()){
			return toDo.get();
			}
		throw new ToDoNotFoundException(TO_DO_NOT_FOUND);
	}
	
	
	private void deadlineCheck(ToDo toDo) {
		if (toDo.getDeadline().isBefore(LocalDate.now())){
			throw new InvalidTimeException(DATE_IS_BEFORE_TODAY);
		}
	}
	
	private void usernameCheck(String username, ToDo toDo) {
		if (!toDo.getUser().equals(username) ) {
			throw new WrongUsernameException(WRONG_USERNAME);
		}
	}


	
}
