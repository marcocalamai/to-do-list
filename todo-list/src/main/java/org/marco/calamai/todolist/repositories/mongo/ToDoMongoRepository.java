package org.marco.calamai.todolist.repositories.mongo;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

import org.marco.calamai.todolist.model.ToDo;

public class ToDoMongoRepository {
	
	private static final String TEMPORARY_IMPLEMENTATION = "Temporary implementation";
	
	public ToDo save(ToDo toDo) {
		throw new UnsupportedOperationException(TEMPORARY_IMPLEMENTATION);
		}

	public List<ToDo> findAll() {
		throw new UnsupportedOperationException(TEMPORARY_IMPLEMENTATION);
		}

	public Optional<ToDo> findById(BigInteger id) {
		throw new UnsupportedOperationException(TEMPORARY_IMPLEMENTATION);
		}

}
