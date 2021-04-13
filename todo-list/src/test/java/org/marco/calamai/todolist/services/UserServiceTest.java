package org.marco.calamai.todolist.services;


import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigInteger;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.marco.calamai.todolist.model.User;
import org.marco.calamai.todolist.repositories.mongo.UserMongoRepository;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests for UserService")
class UserServiceTest {
	
	@Mock
	private UserMongoRepository userMongoRepository;
	
	@InjectMocks
	private UserService userService;
	
	@Nested
	@DisplayName("Tests for User registration")
	class UserRegistration{
		
		@Test @DisplayName("Success registration")
		void testRegister() {
			User userToRegister = spy(new User("username_1","password_1"));
			User userRegistered = new User("username_1","password_1");
			userRegistered.setId(new BigInteger("0"));
			when(userMongoRepository.findByUsername(any(String.class))).thenReturn(Optional.empty());
			when(userMongoRepository.save(any(User.class))).thenReturn(userRegistered);
			User result = userService.register(userToRegister);
			assertThat(result).isSameAs(userRegistered);
			InOrder inOrder = inOrder(userMongoRepository, userToRegister);
			inOrder.verify(userToRegister).setId(null);
			inOrder.verify(userMongoRepository).save(userToRegister);
		}
		
		@Test @DisplayName("Fail registration")
		void testFaildRegister() {
			User userToRegister = new User("username_1","password_1");
			User userRegistered = new User("username_1","password_2");
			when(userMongoRepository.findByUsername(any(String.class))).thenReturn(Optional.of(userRegistered));
			assertThatThrownBy(() -> userService.register(userToRegister))
			.isInstanceOf(RuntimeException.class)
			.hasMessage("Username already present!");
		}
		
	}

}
