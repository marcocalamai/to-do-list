package org.marco.calamai.todolist.webcontrollers;

import static org.mockito.Mockito.*;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.math.BigInteger;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.marco.calamai.todolist.exceptions.EmptyRegistrationFieldsException;
import org.marco.calamai.todolist.exceptions.UsernameAlreadyPresent;
import org.marco.calamai.todolist.exceptions.WhitespaceInRegistrationFieldsException;
import org.marco.calamai.todolist.model.User;
import org.marco.calamai.todolist.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = RegistrationWebController.class)

@DisplayName("Test registration web controller")
class RegistrationWebControllerTest {
	
	@Autowired
	private MockMvc mvc;

	@MockBean
	private UserService userService;
	

	@Test @DisplayName("Check get registration status code and view name")
	void testStatus200AndReturnRegistrationView() throws Exception{
		mvc.perform(get("/registration")).andExpect(status().isOk()).andExpect(view().name("registrationPage"));
	}
	
	@Test @DisplayName("Test successful user registration")
	void testRegistration() throws Exception{
		User user = new User("a_username", "a_password");
		user.setId(new BigInteger("0"));
		when(userService.register("a_username", "a_password")).thenReturn(user);

		mvc.perform(post("/registration")
				.param("username", "a_username")
				.param("password", "a_password")
				.with(csrf()))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/"));	
		verify(userService, times(1)).register("a_username", "a_password");
	}
	
	@Test @DisplayName("Test error registration when user exist")
	void testRegistrationWhenUserExist() throws Exception {
		when(userService.register("a_username", "a_password")).thenThrow(UsernameAlreadyPresent.class);
		mvc.perform(post("/registration")
				.param("username", "a_username")
				.param("password", "a_password")
				.with(csrf()))
				.andExpect(status().is4xxClientError())
				.andExpect(view().name("/registrationPage"))
				.andExpect(model().attribute("error_message", RegistrationWebController.USERNAME_ALREADY_PRESENT));
		verify(userService, times(1)).register("a_username", "a_password");
	}
	
	@Test @DisplayName("Test error registration when username is empty")
	void testeRegistrationWhenUsernameIsEmpty() throws Exception {
		when(userService.register("", "a_password")).thenThrow(EmptyRegistrationFieldsException.class);
		mvc.perform(post("/registration")
				.param("username", "")
				.param("password", "a_password")
				.with(csrf()))
				.andExpect(status().is4xxClientError())
				.andExpect(view().name("/registrationPage"))
				.andExpect(model().attribute("error_message", RegistrationWebController.EMPTY_FIELD));
		verify(userService, times(1)).register("", "a_password");
	}
	
	
	@Test @DisplayName("Test error registration when password is empty")
	void testeRegistrationWhenPasswordIsEmpty() throws Exception {
		when(userService.register("a_username", "")).thenThrow(EmptyRegistrationFieldsException.class);
		mvc.perform(post("/registration")
				.param("username", "a_username")
				.param("password", "")
				.with(csrf()))
				.andExpect(status().is4xxClientError())
				.andExpect(view().name("/registrationPage"))
				.andExpect(model().attribute("error_message", RegistrationWebController.EMPTY_FIELD));
		verify(userService, times(1)).register("a_username", "");
	}
	
	@Test @DisplayName("Test error registration when username contains whitespaces")
	void testeRegistrationWhenUsernameContainsWhitespaces() throws Exception {
		when(userService.register(" a_username", "a_password")).thenThrow(WhitespaceInRegistrationFieldsException.class);
		mvc.perform(post("/registration")
				.param("username", " a_username")
				.param("password", "a_password")
				.with(csrf()))
				.andExpect(status().is4xxClientError())
				.andExpect(view().name("/registrationPage"))
				.andExpect(model().attribute("error_message", RegistrationWebController.WHITESPACE_IN_FIELD));
		verify(userService, times(1)).register(" a_username", "a_password");
	}
	
	@Test @DisplayName("Test error registration when password contains whitespaces")
	void testeRegistrationWhenPasswordContainsWhitespaces() throws Exception {
		when(userService.register("a_username", " a_password")).thenThrow(WhitespaceInRegistrationFieldsException.class);
		mvc.perform(post("/registration")
				.param("username", "a_username")
				.param("password", " a_password")
				.with(csrf()))
				.andExpect(status().is4xxClientError())
				.andExpect(view().name("/registrationPage"))
				.andExpect(model().attribute("error_message", RegistrationWebController.WHITESPACE_IN_FIELD));
		verify(userService, times(1)).register("a_username", " a_password");
	}
}
