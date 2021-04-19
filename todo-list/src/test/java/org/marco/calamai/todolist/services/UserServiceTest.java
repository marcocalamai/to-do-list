package org.marco.calamai.todolist.services;


import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.marco.calamai.todolist.exceptions.EmptyRegistrationFieldsException;
import org.marco.calamai.todolist.exceptions.UsernameAlreadyPresent;
import org.marco.calamai.todolist.exceptions.WhitespaceInRegistrationFieldsException;
import org.marco.calamai.todolist.model.User;
import org.marco.calamai.todolist.repositories.mongo.UserMongoRepository;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests for UserService")
class UserServiceTest {
	
	private static final String USERNAME_ALREADY_PRESENT = "A user with that username already exists!";
	private static final String EMPTY_FIELD = "The username or password fields are empty!";
	private static final String WHITESPACE_IN_FIELD = "The username or password field contains one or more whitespace!";
	private static final String USER_NOT_FOUND = "User not found!";
	
	@Mock
	private PasswordEncoder passwordEncoder;
	
	@Mock
	private UserMongoRepository userMongoRepository;
	
	@InjectMocks
	private UserService userService;
	
	@Nested
	@DisplayName("Tests for User registration")
	class UserRegistration{
		
		
		@Test @DisplayName("Success registration")
		void testRegister() {
			when(userMongoRepository.findByUsername(any(String.class))).thenReturn(Optional.empty());
			when(passwordEncoder.encode(any(String.class))).thenReturn("password_encoded");
			User result = userService.register("username_1", "password_1");
			assertEquals("username_1", result.getUsername());
			assertEquals("password_encoded", result.getPassword());
			InOrder inOrder = inOrder(userMongoRepository, passwordEncoder);
			inOrder.verify(userMongoRepository).findByUsername("username_1");
			inOrder.verify(passwordEncoder).encode("password_1");
			inOrder.verify(userMongoRepository).save(result);
		}
		
		@Test @DisplayName("Fail registration")
		void testFaildRegister() {
			User userRegistered = new User("username_1","password_2");
			when(userMongoRepository.findByUsername(any(String.class))).thenReturn(Optional.of(userRegistered));
			assertThatThrownBy(() -> userService.register("username_1", "password_1"))
			.isInstanceOf(UsernameAlreadyPresent.class)
			.hasMessage(USERNAME_ALREADY_PRESENT);
		}
		
		@Test @DisplayName("Empty username")
		void testUserEmpty() {
			assertThatThrownBy(() -> userService.register("", "password"))
			.isInstanceOf(EmptyRegistrationFieldsException.class)
			.hasMessage(EMPTY_FIELD);
			verifyNoInteractions(userMongoRepository);
		}
		
		@Test @DisplayName("Empty password")
		void testPasswordEmpty() {
			assertThatThrownBy(() -> userService.register("username", ""))
			.isInstanceOf(EmptyRegistrationFieldsException.class)
			.hasMessage(EMPTY_FIELD);
			verifyNoInteractions(userMongoRepository);
		}
		
		@Test @DisplayName("Whitespace character in username field")
		void testUserContainsWhitespace() {
			assertThatThrownBy(() -> userService.register(" username", "password"))
			.isInstanceOf(WhitespaceInRegistrationFieldsException.class)
			.hasMessage(WHITESPACE_IN_FIELD);
			verifyNoInteractions(userMongoRepository);
		}
		
		@Test @DisplayName("Whitespace character in password field")
		void testPasswordContainsWhitespace() {
			assertThatThrownBy(() -> userService.register("username", " password"))
			.isInstanceOf(WhitespaceInRegistrationFieldsException.class)
			.hasMessage(WHITESPACE_IN_FIELD);
			verifyNoInteractions(userMongoRepository);
		}
		
	}
	
	
	@Nested
	@DisplayName("Tests for load User")
	class UserLoad{
		
		
		@Test @DisplayName("Load User by username when it exist")
		void testLoadUserByUsernameWhenItExist() {
			User userToFind = new User("username", "password");
			when(userMongoRepository.findByUsername("username")).thenReturn(Optional.of(userToFind));
			User result = userService.loadUserByUsername("username");
			assertEquals("username", result.getUsername());
			verify(userMongoRepository, times(1)).findByUsername("username");			
		}
		
		@Test @DisplayName("Load User by username when it does not exist")
		void testLoadUserByUsernameWhenItDoesNotExist() {
			when(userMongoRepository.findByUsername(any(String.class))).thenReturn(Optional.empty());
			assertThatThrownBy(() -> userService.loadUserByUsername("username"))
			.isInstanceOf(UsernameNotFoundException.class)
			.hasMessage(USER_NOT_FOUND);
			verify(userMongoRepository, times(1)).findByUsername("username");
		}
	}

}
