package org.marco.calamai.todolist.repositories.mongo;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;

import org.marco.calamai.todolist.model.ToDo;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ToDoMongoRepository extends MongoRepository<ToDo, BigInteger> {

	List<ToDo> findByOrderByDoneAscDeadlineAsc();
	List<ToDo> findByUserOrderByDoneAscDeadlineAsc(String user);
	List<ToDo> findByDeadlineOrderByDoneAsc(LocalDate deadline);
	List<ToDo> findByTitleOrderByDoneAscDeadlineAsc(String title);
}
