package org.marco.calamai.todolist.repositories.mongo;
import java.math.BigInteger;

import org.marco.calamai.todolist.model.ToDo;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ToDoMongoRepository extends MongoRepository<ToDo, BigInteger> {
	
}
