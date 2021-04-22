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
@WebMvcTest(controllers = IndexWebController.class)
class IndexWebControllerHtmlUnitTest {
	
	@Autowired
	private WebClient webClient;
	
	@Test @DisplayName("Test HomePage title")
	void testHomePageTitle() throws Exception {
		HtmlPage page = webClient.getPage("/");
		assertEquals("ToDo List", page.getTitleText());	
	}
	
	@Test @DisplayName("Test HomePage should provide a link for toDoManagerPage")
	void testHomePageShouldProvideALinkFortoDoManagerPage() throws Exception {
	HtmlPage page = this.webClient.getPage("/");
	assertEquals("/toDoManager", page.getAnchorByText("Manage ToDo").getHrefAttribute());
	}
	
	@Test @DisplayName("Test HomePage should provide a link for user registration")
	void testHomePageShouldProvideALinkForUserRegistrationPage() throws Exception {
	HtmlPage page = this.webClient.getPage("/");
	assertEquals("/registration", page.getAnchorByText("Register").getHrefAttribute());
	}
	
	@Test @DisplayName("Test HomePage should provide a link for user registration")
	void testHomePageShouldProvideALinkForUserLoginPage() throws Exception {
	HtmlPage page = this.webClient.getPage("/");
	assertEquals("/login", page.getAnchorByText("Login").getHrefAttribute());
	}
	

}
