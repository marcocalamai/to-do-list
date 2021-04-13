package org.marco.calamai.todolist.repositories.mongo;

import java.math.BigInteger;
import java.util.Optional;

import org.marco.calamai.todolist.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface UserMongoRepository extends MongoRepository<User, BigInteger> {
	
	Optional<User> findByUsername(String username);

}
