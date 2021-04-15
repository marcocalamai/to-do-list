package org.marco.calamai.todolist.webcontrollers;

import static org.mockito.Mockito.*;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = RegistrationWebController.class)

class RegistrationWebControllerTest {
	
	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private UserService userService;
	
	
	@Test @DisplayName("Check registration status code and view name")
	void testStatus200AndReturnRegistrationView() throws Exception{
		mvc.perform(get("/registration")).andExpect(status().isOk()).andExpect(view().name("registrationPage"));
	}
	
	@Test @DisplayName("")
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


	
	
	

}
