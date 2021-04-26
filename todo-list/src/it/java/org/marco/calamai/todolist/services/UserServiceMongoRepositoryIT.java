package org.marco.calamai.todolist.services;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.marco.calamai.todolist.model.User;
import org.marco.calamai.todolist.repositories.mongo.UserMongoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestPropertySource(properties = "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration")
class UserServiceMongoRepositoryIT {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserMongoRepository userMongoRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@BeforeEach
	void setup() {
		userMongoRepository.deleteAll();
		userService = new UserService(userMongoRepository, passwordEncoder);
	}
	
	@Test @DisplayName("Test registration")
	void testRegister() {
		User user = userService.register("username_1", "password_1", "password_1");
		assertEquals(1, userMongoRepository.count());
		User result = userMongoRepository.findAll().get(0);
		assertEquals(user.getUsername(), result.getUsername());
		assertEquals(user.getPassword(), result.getPassword());
	}

}
