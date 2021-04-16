package org.marco.calamai.todolist.webcontrollers;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.math.BigInteger;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.marco.calamai.todolist.model.User;
import org.marco.calamai.todolist.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = LoginWebController.class)
@DisplayName("Test login web controller")
class LoginWebControllerTest {
	
	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private UserService userService;
	
	private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
	
	@Test @DisplayName("Check get login status code and view name")
	void testStatus200AndReturnLoginView() throws Exception {
		mvc.perform(get("/login"))
				.andExpect(status().isOk())
				.andExpect(view().name("loginPage"))
				.andExpect(model().attribute("error", nullValue()));
	}
	
	@Test @DisplayName("Test successful login")
	void testLogin() throws Exception {
		User user = new User("username1", encoder.encode("password1"));
		user.setId(new BigInteger("0"));
		when(userService.loadUserByUsername("username1")).thenReturn(user);
		mvc.perform(formLogin().user("username1").password("password1"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/toDoManager"));
		verify(userService, times(1)).loadUserByUsername("username1");
	}
	
	@Test @DisplayName("Test login wrong username")
	void testLoginWrongUsername() throws Exception {
		when(userService.loadUserByUsername("WrongUsername1")).thenThrow(UsernameNotFoundException.class);
		mvc.perform(formLogin().user("WrongUsername1").password("password1"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/login?error=true"));
		verify(userService, times(1)).loadUserByUsername("WrongUsername1");
	}
	
	@Test @DisplayName("Test login wrong password")
	void testLoginWrongPassword() throws Exception {
		User user = new User("username1", encoder.encode("WrongPassword1"));
		user.setId(new BigInteger("0"));
		when(userService.loadUserByUsername("username1")).thenReturn(user);
		mvc.perform(formLogin().user("username1").password("password1"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/login?error=true"));
		verify(userService, times(1)).loadUserByUsername("username1");
	}
	
	@Test @DisplayName("Test modelAndView on wrong username or password")
	void testLoginModelAndViewOnWrongUsernameOrPassword() throws Exception {
		mvc.perform(get("/login?error=true"))
				.andExpect(view().name("loginPage"))
				.andExpect(status().is4xxClientError())
				.andExpect(model().attribute("error", "true"))
				.andExpect(model().attribute("error_message", LoginWebController.WRONG_USERNAME_OR_PASSWORD));
	}
	
}
