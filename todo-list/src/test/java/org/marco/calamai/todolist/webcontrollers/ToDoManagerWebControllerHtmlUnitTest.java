package org.marco.calamai.todolist.webcontrollers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Collections;

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
	
	@Autowired 
	private WebClient webClient;

	@MockBean
	private ToDoService toDoService;
	

	@Test @DisplayName("Test toDoManagerPage title")
	@WithMockUser(username = "AuthenticatedUser", password = "passwordTest", roles = "USER")
	void testToDoManagerPageTitle() throws Exception {
		HtmlPage page = webClient.getPage("/toDoManager");
		assertEquals("ToDo Manager", page.getTitleText());	
	}

	@Test @DisplayName("Test todoManagerPage with no toDo")
	@WithMockUser(username = "AuthenticatedUser", password = "passwordTest", roles = "USER")
	void testToDoManagerWithNoToDo() throws Exception {
		when(toDoService.getAllToDoOrderByDoneAscDeadlineAsc()).thenReturn(Collections.emptyList());
		HtmlPage page = webClient.getPage("/toDoManager");
		assertThat(page.getBody().getTextContent()).contains("There are no to do");
	}
	
}
