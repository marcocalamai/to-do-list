package org.marco.calamai.todolist.webcontrollers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.marco.calamai.todolist.configurations.WebSecurityConfig;
import org.marco.calamai.todolist.services.ToDoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = ToDoManagerWebController.class)
@Import(WebSecurityConfig.class)
class ToDoManagerWebControllerHtmlUnitTest {

	@MockBean
	private ToDoService toDoService;
	
	@Autowired 
	private WebClient webClient;

	

	@Test @DisplayName("Test toDoManagerPage title")
	@WithMockUser(username = "AuthenticatedUser", password = "passwordTest", roles = "USER")
	void testtoDoManagerPageTitle() throws Exception {
		HtmlPage page = webClient.getPage("/toDoManager");
		assertEquals("ToDo Manager", page.getTitleText());	
	}

}
