package org.marco.calamai.todolist.services;

import java.util.Optional;

import org.marco.calamai.todolist.exceptions.EmptyRegistrationFieldsException;
import org.marco.calamai.todolist.exceptions.PasswordsDontMatchException;
import org.marco.calamai.todolist.exceptions.UsernameAlreadyPresent;
import org.marco.calamai.todolist.exceptions.WhitespaceInRegistrationFieldsException;
import org.marco.calamai.todolist.model.User;
import org.marco.calamai.todolist.repositories.mongo.UserMongoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {
	
	private static final String USERNAME_ALREADY_PRESENT = "A user with that username already exists!";
	private static final String EMPTY_FIELD = "The username or password fields are empty!";
	private static final String WHITESPACE_IN_FIELD = "The username or password field contains one or more whitespace!";
	private static final String USER_NOT_FOUND = "User not found!";
	private static final String PASSWORDS_DO_NOT_MATCH = "The two passwords do not match!";
	
	private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
	

	private UserMongoRepository userMongoRepository;
	
	private PasswordEncoder passwordEncoder;
	
	
	@Autowired
	public UserService(UserMongoRepository userMongoRepository, PasswordEncoder passwordEncoder) {
		this.userMongoRepository = userMongoRepository;
		this.passwordEncoder = passwordEncoder;
	}
	

	public User register(String username, String password, String passwordConfirmation) {
		checkOnFields(username, password);
		if (!passwordConfirmation.equals(password)) {
			LOGGER.warn("Password: {} and Confirmation: {} do not match!", password, passwordConfirmation);
			throw new PasswordsDontMatchException(PASSWORDS_DO_NOT_MATCH);
		}
		Optional<User> result = userMongoRepository.findByUsername(username);
		if (result.isPresent()){
			LOGGER.warn("Username: {} already exist!", username);
			throw new UsernameAlreadyPresent(USERNAME_ALREADY_PRESENT);
		}
		User user = createUser(username, password);
		userMongoRepository.save(user);
		LOGGER.info("User: {} joined!", username);
		return user;
	}

	@Override
	public User loadUserByUsername(String username) {
		Optional<User> user = userMongoRepository.findByUsername(username);
		if (!user.isPresent()) {
			LOGGER.warn("Username: {} not found!", username);
			throw new UsernameNotFoundException(USER_NOT_FOUND);
		}
		return user.get();
	}
	
	private void checkOnFields(String username, String password) {
		if (username.isEmpty() || password.isEmpty()) {
			LOGGER.warn("Username: {} or Password: {} are empty!", username, password);
			throw new EmptyRegistrationFieldsException(EMPTY_FIELD);
		}
		if (username.contains(" ") || password.contains(" ")) {
			LOGGER.warn("Username: {} or Password: {} contains whitespace!", username, password);
			throw new WhitespaceInRegistrationFieldsException(WHITESPACE_IN_FIELD);
		}
	}
	
	private User createUser(String username, String password) {
		return new User(username, passwordEncoder.encode(password));
	}


}
