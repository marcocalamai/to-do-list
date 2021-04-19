package org.marco.calamai.todolist.webcontrollers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = LoginWebController.class)
class LoginWebControllerHtmlUnitTest {
	
	@Autowired 
	private WebClient webClient;
	
	@Test @DisplayName("Test loginPage title")
	void testHomePageTitle() throws Exception {
		HtmlPage page = webClient.getPage("/login");
		assertEquals("Login Page", page.getTitleText());	
	}
	
	@Test @DisplayName("Test loginPage should provide a link for index page")
	void testLoginPageShouldProvideAButtonForIndexPage() throws Exception {
	HtmlPage page = this.webClient.getPage("/login");
	assertEquals("/", page.getAnchorByText("Go Back").getHrefAttribute());
	}

}
