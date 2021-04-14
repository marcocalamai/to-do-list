package org.marco.calamai.todolist.services;

import java.util.Optional;

import org.marco.calamai.todolist.exceptions.UsernameAlreadyPresent;
import org.marco.calamai.todolist.model.User;
import org.marco.calamai.todolist.repositories.mongo.UserMongoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {
	
	public static final String USERNAME_ALREADY_PRESENT = "A user with that username already exists!";
	

	private UserMongoRepository userMongoRepository;
	
	private PasswordEncoder passwordEncoder;
	
	
	@Autowired
	public UserService(UserMongoRepository userMongoRepository, PasswordEncoder passwordEncoder) {
		this.userMongoRepository = userMongoRepository;
		this.passwordEncoder = passwordEncoder;
	}
	

	public User register(String username, String password) {
		Optional<User> result = userMongoRepository.findByUsername(username);
		if (result.isPresent()){
			throw new UsernameAlreadyPresent(USERNAME_ALREADY_PRESENT);
		}
		User user = createUser(username, password);
		userMongoRepository.save(user);
		return user;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}
	
	private User createUser(String username, String password) {
		return new User(username, passwordEncoder.encode(password));
	}


}
