package org.marco.calamai.todolist.webcontrollers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.marco.calamai.todolist.exceptions.PasswordsDontMatchException;
import org.marco.calamai.todolist.exceptions.UsernameAlreadyPresent;
import org.marco.calamai.todolist.exceptions.WhitespaceInRegistrationFieldsException;
import org.marco.calamai.todolist.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = RegistrationWebController.class)

@DisplayName("HtmlUnit test Registration")
class RegistrationWebControllerHtmlUnitTest {
	
	private static final String USERNAME_ALREADY_PRESENT = "A user with that username already exists!";
	private static final String WHITESPACE_IN_FIELD = "The username or password field contains one or more whitespace!";
	private static final String PASSWORDS_DO_NOT_MATCH = "The two passwords do not match!";
	
	@Autowired
	private WebClient webClient;
	
	@MockBean
	private UserService userService;
	
	
	@Test @DisplayName("TestregistrationPage title")
	void testRegistrationTitle() throws Exception {
		HtmlPage page = webClient.getPage("/registration");
		assertEquals("Registration page", page.getTitleText());
	}
	
	
	@Test @DisplayName("Test registrationPage should provide a link for index page")
	void testLoginPageShouldProvideALinkForIndexPage() throws Exception {
	HtmlPage page = this.webClient.getPage("/registration");
	assertEquals("/", page.getAnchorByText("Go Back").getHrefAttribute());
	}
	
	
	@Test @DisplayName("Test register user")
	void testRegisterUser() throws Exception {	
	HtmlPage page = this.webClient.getPage("/registration");
	HtmlForm form = page.getFormByName("registrationForm");
	form.getInputByName("username").setValueAttribute("username_1");
	form.getInputByName("password").setValueAttribute("password_1");
	form.getInputByName("passwordConfirmation").setValueAttribute("password_1");
	page = form.getButtonByName("btn_submit").click();
	
	verify(userService, times(1)).register("username_1", "password_1", "password_1");	
	assertEquals("ToDo List", page.getTitleText());
	}
	
	
	@Test @DisplayName("Test register user when username already exist should show error")
	void testRegisterUserWhenUsernameAlreadyExist() throws Exception {
	webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);	
	
	when(userService.register("username_1", "password_1", "password_1")).thenThrow(UsernameAlreadyPresent.class);
	
	HtmlPage page = this.webClient.getPage("/registration");
	HtmlForm form = page.getFormByName("registrationForm");
	form.getInputByName("username").setValueAttribute("username_1");
	form.getInputByName("password").setValueAttribute("password_1");
	form.getInputByName("passwordConfirmation").setValueAttribute("password_1");
	page = form.getButtonByName("btn_submit").click();
	
	assertThat(page.getBody().getTextContent()).contains(USERNAME_ALREADY_PRESENT);
	verify(userService, times(1)).register("username_1", "password_1", "password_1");	
	}
	
	@Test @DisplayName("Test register user when passwords dont match should show error")
	void testRegisterUserPasswordsDontMatch() throws Exception {
	webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);	
	
	when(userService.register("username_1", "password_1", "passNotMatch")).thenThrow(PasswordsDontMatchException.class);
	
	HtmlPage page = this.webClient.getPage("/registration");
	HtmlForm form = page.getFormByName("registrationForm");
	form.getInputByName("username").setValueAttribute("username_1");
	form.getInputByName("password").setValueAttribute("password_1");
	form.getInputByName("passwordConfirmation").setValueAttribute("passNotMatch");
	page = form.getButtonByName("btn_submit").click();
	
	assertThat(page.getBody().getTextContent()).contains(PASSWORDS_DO_NOT_MATCH);
	verify(userService, times(1)).register("username_1", "password_1", "passNotMatch");	
	}
	
	
	@Test @DisplayName("Test register user when username or password contains one or more whitespace")
	void testRegisterUserWhenUsernameOrPasswordContainsWhitespaces() throws Exception {
	webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);	
	
	when(userService.register("u s e r n a m e", "password_1", "password_1")).thenThrow(WhitespaceInRegistrationFieldsException.class);
	
	HtmlPage page = this.webClient.getPage("/registration");
	HtmlForm form = page.getFormByName("registrationForm");
	form.getInputByName("username").setValueAttribute("u s e r n a m e");
	form.getInputByName("password").setValueAttribute("password_1");
	form.getInputByName("passwordConfirmation").setValueAttribute("password_1");
	page = form.getButtonByName("btn_submit").click();
	
	assertThat(page.getBody().getTextContent()).contains(WHITESPACE_IN_FIELD);
	verify(userService, times(1)).register("u s e r n a m e", "password_1", "password_1");	
	}
}
