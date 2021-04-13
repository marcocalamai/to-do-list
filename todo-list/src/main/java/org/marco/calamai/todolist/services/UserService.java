package org.marco.calamai.todolist.services;

import java.util.Optional;

import org.marco.calamai.todolist.model.User;
import org.marco.calamai.todolist.repositories.mongo.UserMongoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {
	
	@Autowired
	private UserMongoRepository userMongoRepository;
	
	@Autowired
	public UserService(UserMongoRepository userMongoRepository) {
		this.userMongoRepository = userMongoRepository;
	}
	

	public User register(User user) {
		Optional<User> result = userMongoRepository.findByUsername(user.getUsername());
		if (result.isPresent()){
			throw new RuntimeException("Username already present!");
		}
		user.setId(null);
		return userMongoRepository.save(user);
	}


	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}


}
