package org.marco.calamai.todolist.webcontrollers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.marco.calamai.todolist.configurations.WebSecurityConfig;
import org.marco.calamai.todolist.model.ToDo;
import org.marco.calamai.todolist.services.ToDoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTable;

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
	
	@Test @DisplayName("Test todoManagerPage with ToDo should show them")
	@WithMockUser(username = "AuthenticatedUser", password = "passwordTest", roles = "USER")
	void testToDoManagerWithToDoShouldShowThem() throws Exception{
		LocalDate today = LocalDate.now();
		ToDo todo1 = new ToDo("username_1", "title_1", "description_1", today);
		todo1.setId(new BigInteger("0"));
		ToDo todo2 = new ToDo("username_2", "title_2", "description_2", today);
		todo2.setId(new BigInteger("1"));
		
		when(toDoService.getAllToDoOrderByDoneAscDeadlineAsc()).thenReturn(Arrays.asList(todo1, todo2));
		
		HtmlPage page = webClient.getPage("/toDoManager");
		assertThat(page.getBody().getTextContent()).doesNotContain("There are no to do");
		HtmlTable table = page.getHtmlElementById("toDo_table");
		String todayAsString = today.toString();
		assertEquals(
				"All ToDo\n"+
				"Username	Title	Description	Done	Deadline\n"+
				"username_1	title_1	description_1	"+ todo1.isDone()+ "	" +  	todayAsString+"\n"+
				"username_2	title_2	description_2	"+ todo2.isDone()+ "	" + 	todayAsString,
				removeWindowsCR(table.asText()));
	}
	
	@Test @DisplayName("Test todoManagerPage AllMyToDo with no toDo")
	@WithMockUser(username = "AuthenticatedUser", password = "passwordTest", roles = "USER")
	void testToDoManagerAllMyToDoWithNoToDo() throws Exception {
		when(toDoService.getToDoByUserOrderByDoneAscDeadlineAsc("AuthenticatedUser")).thenReturn(Collections.emptyList());
		
		HtmlPage page = webClient.getPage("/toDoManager/AllMyToDo");
		assertThat(page.getBody().getTextContent()).contains("There are no to do");
	}
	
	@Test @DisplayName("Test todoManagerPage AllMyToDo with ToDo should show them")
	@WithMockUser(username = "AuthenticatedUser", password = "passwordTest", roles = "USER")
	void testToDoManagerAllMyToDoWithToDoShouldShowThem() throws Exception{
		LocalDate today = LocalDate.now();
		ToDo todo1 = new ToDo("AuthenticatedUser", "title_1", "description_1", today);
		todo1.setId(new BigInteger("0"));
		ToDo todo2 = new ToDo("AuthenticatedUser", "title_2", "description_2", today);
		todo2.setId(new BigInteger("1"));
		
		when(toDoService.getToDoByUserOrderByDoneAscDeadlineAsc("AuthenticatedUser")).thenReturn(Arrays.asList(todo1, todo2));
		
		HtmlPage page = webClient.getPage("/toDoManager/AllMyToDo");
		assertThat(page.getBody().getTextContent()).doesNotContain("There are no to do");
		HtmlTable table = page.getHtmlElementById("myToDo_table");
		String todayAsString = today.toString();
		assertEquals(
				"All my ToDo\n"+
				"Username	Title	Description	Done	Deadline\n"+
				"AuthenticatedUser	title_1	description_1	"+ todo1.isDone()+ "	" +  	todayAsString+"	Edit	Delete\n"+
				"AuthenticatedUser	title_2	description_2	"+ todo2.isDone()+ "	" + 	todayAsString+"	Edit	Delete",
				removeWindowsCR(table.asText()));
		
		page.getAnchorByHref("/editToDo/0");
		page.getAnchorByHref("/editToDo/1");
		page.getAnchorByHref("/deleteToDo/0");
		page.getAnchorByHref("/deleteToDo/1");
	}
	
	@Test @DisplayName("Test todoManagerPage toDo by deadline whith no todo")
	@WithMockUser(username = "AuthenticatedUser", password = "passwordTest", roles = "USER")
	void testToDoManagerToDoByDeadlineWithNoToDo() throws Exception {
		LocalDate today = LocalDate.now();
	
		when(toDoService.getAllToDoByDeadlineOrderByDoneAsc(today)).thenReturn(Collections.emptyList());
		
		HtmlPage page = webClient.getPage("/toDoManager");
		HtmlForm form = page.getFormByName("searchByDeadlineForm");
		String todayAsString = today.toString();
		
		form.getInputByName("deadline").setValueAttribute(todayAsString);
		page = form.getButtonByName("btn_searchByDeadline").click();
		
		assertThat(page.getBody().getTextContent()).contains("There are no to do");
	}
	
	
	@Test @DisplayName("Test todoManagerPage toDo by deadline")
	@WithMockUser(username = "AuthenticatedUser", password = "passwordTest", roles = "USER")
	void testToDoManagerToDoByDeadline() throws Exception {
		LocalDate today = LocalDate.now();
		ToDo todo1 = new ToDo("username_1", "title_1", "description_1", today);
		todo1.setId(new BigInteger("0"));
		ToDo todo2 = new ToDo("username_2", "title_2", "description_2", today);
		todo2.setId(new BigInteger("1"));
		
		when(toDoService.getAllToDoByDeadlineOrderByDoneAsc(today)).thenReturn(Arrays.asList(todo1, todo2));
		
		HtmlPage page = webClient.getPage("/toDoManager");
		HtmlForm form = page.getFormByName("searchByDeadlineForm");
		String todayAsString = today.toString();
		
		form.getInputByName("deadline").setValueAttribute(todayAsString);
		page = form.getButtonByName("btn_searchByDeadline").click();
		
		assertThat(page.getBody().getTextContent()).doesNotContain("There are no to do");
		
		HtmlTable table = page.getHtmlElementById("toDo_table");
		assertEquals(
				"All ToDo\n"+
				"Username	Title	Description	Done	Deadline\n"+
				"username_1	title_1	description_1	"+ todo1.isDone()+ "	" +  	todayAsString+"\n"+
				"username_2	title_2	description_2	"+ todo2.isDone()+ "	" + 	todayAsString,
				removeWindowsCR(table.asText()));
	}
	
	
	
	private String removeWindowsCR(String s) {
		return s.replaceAll("\r", "");
		}
	
}
