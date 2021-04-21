package org.marco.calamai.todolist.webcontrollers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigInteger;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.marco.calamai.todolist.model.User;
import org.marco.calamai.todolist.services.ToDoService;
import org.marco.calamai.todolist.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = {LoginWebController.class, ToDoManagerWebController.class})

@DisplayName("HtmlUnit test Login")
class LoginWebControllerHtmlUnitTest {
	
	@Autowired 
	private WebClient webClient;
	
	@MockBean
	private UserService userService;
	
	@MockBean
	private ToDoService toDoService;
	
	private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

	
	@Test @DisplayName("Test loginPage title")
	void testHomePageTitle() throws Exception {
		HtmlPage page = webClient.getPage("/login");
		assertEquals("Login Page", page.getTitleText());	
	}
	
	
	@Test @DisplayName("Test loginPage should provide a link for index page")
	void testLoginPageShouldProvideALinkForIndexPage() throws Exception {
	HtmlPage page = this.webClient.getPage("/login");
	
	assertEquals("/", page.getAnchorByText("Go Back").getHrefAttribute());
	assertThat(page.getBody().getTextContent()).doesNotContain("Wrong username or password!");
	}
	
	
	@Test @DisplayName("Test loginPage when user not found should show error")
	void testLoginPageWhenItFailShouldShowError() throws Exception {
	webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);	
	
	when(userService.loadUserByUsername("WrongUsername")).thenThrow(UsernameNotFoundException.class);
	
	HtmlPage page = this.webClient.getPage("/login");
	HtmlForm form = page.getFormByName("loginForm");
	form.getInputByName("username").setValueAttribute("WrongUsername");
	form.getInputByName("password").setValueAttribute("password_1");
	page = form.getButtonByName("btn_submit").click();
	
	assertThat(page.getBody().getTextContent()).contains("Wrong username or password!");
	verify(userService, times(1)).loadUserByUsername("WrongUsername");	
	}
	
	
	@Test @DisplayName("Test login successful")
	void testDoLogin() throws Exception {
	User user = new User("username_1", encoder.encode("password_1"));
	user.setId(new BigInteger("0"));
	
	when(userService.loadUserByUsername("username_1")).thenReturn(user);

	HtmlPage page = this.webClient.getPage("/login");
	HtmlForm form = page.getFormByName("loginForm");
	form.getInputByName("username").setValueAttribute("username_1");
	form.getInputByName("password").setValueAttribute("password_1");
	form.getButtonByName("btn_submit").click();

	verify(userService, times(1)).loadUserByUsername("username_1");
	}
}
