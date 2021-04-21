package org.marco.calamai.todolist.webcontrollers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.marco.calamai.todolist.configurations.WebSecurityConfig;
import org.marco.calamai.todolist.exceptions.ToDoNotFoundException;
import org.marco.calamai.todolist.model.ToDo;
import org.marco.calamai.todolist.services.ToDoService;
import org.mockito.InOrder;
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

@DisplayName("HtmlUnit test ToDoManager")
class ToDoManagerWebControllerHtmlUnitTest {
	
	@Autowired 
	private WebClient webClient;

	@MockBean
	private ToDoService toDoService;
	
	@Nested
	@DisplayName("Tests for ToDoManagerPage")
	class InsertToDo{
		
		@Test @DisplayName("Test toDoManagerPage title")
		@WithMockUser(username = "AuthenticatedUser", password = "passwordTest", roles = "USER")
		void testToDoManagerPageTitle() throws Exception {
			HtmlPage page = webClient.getPage("/toDoManager");
			assertEquals("ToDo Manager", page.getTitleText());	
		}
		
		@Test @DisplayName("Test toDoManagerPage should provide a link for creating new ToDo")
		@WithMockUser(username = "AuthenticatedUser", password = "passwordTest", roles = "USER")
		void testToDoManagerPageShouldProvideALinkForCreatingANewToDo() throws Exception {
			HtmlPage page = webClient.getPage("/toDoManager");
			assertThat(page.getAnchorByText("New ToDo").getHrefAttribute()).isEqualTo("/toDoManager/newToDo");
		}
	
		@Test @DisplayName("Test todoManagerPage with no toDo")
		@WithMockUser(username = "AuthenticatedUser", password = "passwordTest", roles = "USER")
		void testToDoManagerWithNoToDo() throws Exception {
			when(toDoService.getAllToDoOrderByDoneAscDeadlineAsc()).thenReturn(Collections.emptyList());
			
			HtmlPage page = webClient.getPage("/toDoManager");
			assertThat(page.getBody().getTextContent()).contains("There are no to do");
		}
		
		@Test @DisplayName("Test todoManagerPage should show all ToDo")
		@WithMockUser(username = "AuthenticatedUser", password = "passwordTest", roles = "USER")
		void testToDoManagerShouldShowAllToDo() throws Exception{
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
					"username_1	title_1	description_1	"+ todo1.isDone()+ "	" + todayAsString+"\n"+
					"username_2	title_2	description_2	"+ todo2.isDone()+ "	" + todayAsString,
					removeWindowsCR(table.asText()));
		}
		
		@Test @DisplayName("Test todoManagerPage AllMyToDo with no toDo")
		@WithMockUser(username = "AuthenticatedUser", password = "passwordTest", roles = "USER")
		void testToDoManagerAllMyToDoWithNoToDo() throws Exception {
			when(toDoService.getToDoByUserOrderByDoneAscDeadlineAsc("AuthenticatedUser")).thenReturn(Collections.emptyList());
			
			HtmlPage page = webClient.getPage("/toDoManager/AllMyToDo");
			assertThat(page.getBody().getTextContent()).contains("There are no to do");
		}
		
		@Test @DisplayName("Test todoManagerPage AllMyToDo should show them")
		@WithMockUser(username = "AuthenticatedUser", password = "passwordTest", roles = "USER")
		void testToDoManagerAllMyToDoShouldShowThem() throws Exception{
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
					"AuthenticatedUser	title_1	description_1	"+ todo1.isDone()+ "	" + todayAsString+"	Edit	 Delete\n"+
					"AuthenticatedUser	title_2	description_2	"+ todo2.isDone()+ "	" + todayAsString+"	Edit	 Delete",
					removeWindowsCR(table.asText()));
			
			page.getAnchorByHref("/toDoManager/editToDo/0");
			page.getAnchorByHref("/toDoManager/editToDo/1");
			assertThat(page.getByXPath("//form[@action='/toDoManager/deleteToDo/0']")).isNotEmpty();
			assertThat(page.getByXPath("//form[@action='/toDoManager/deleteToDo/1']")).isNotEmpty();
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
		
		
		@Test @DisplayName("Test todoManagerPage toDo by deadline should show them")
		@WithMockUser(username = "AuthenticatedUser", password = "passwordTest", roles = "USER")
		void testToDoManagerToDoByDeadlineShouldShowThem() throws Exception {
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
					"username_1	title_1	description_1	"+ todo1.isDone()+ "	" + todayAsString+"\n"+
					"username_2	title_2	description_2	"+ todo2.isDone()+ "	" + todayAsString,
					removeWindowsCR(table.asText()));
		}
		
		@Test @DisplayName("Test todoManagerPage toDo by title whith no todo")
		@WithMockUser(username = "AuthenticatedUser", password = "passwordTest", roles = "USER")
		void testToDoManagerToDoByTitleWithNoToDo() throws Exception {
			when(toDoService.getAllToDoByTitleOrderByDoneAscDeadlineAsc("title_1")).thenReturn(Collections.emptyList());
			
			HtmlPage page = webClient.getPage("/toDoManager");
			HtmlForm form = page.getFormByName("searchByTitleForm");
			
			form.getInputByName("title").setValueAttribute("title_1");
			page = form.getButtonByName("btn_searchByTitle").click();
			
			assertThat(page.getBody().getTextContent()).contains("There are no to do");
		}
		
		
		@Test @DisplayName("Test todoManagerPage toDo by title should show them")
		@WithMockUser(username = "AuthenticatedUser", password = "passwordTest", roles = "USER")
		void testToDoManagerToDoByTitleShouldShowThem() throws Exception {
			ToDo todo1 = new ToDo("username_1", "title_1", "description_1", LocalDate.now());
			todo1.setId(new BigInteger("0"));
			ToDo todo2 = new ToDo("username_2", "title_1", "description_2", LocalDate.now());
			todo2.setId(new BigInteger("1"));
			
			when(toDoService.getAllToDoByTitleOrderByDoneAscDeadlineAsc("title_1")).thenReturn(Arrays.asList(todo1, todo2));
			
			HtmlPage page = webClient.getPage("/toDoManager");
			HtmlForm form = page.getFormByName("searchByTitleForm");
			
			form.getInputByName("title").setValueAttribute("title_1");
			page = form.getButtonByName("btn_searchByTitle").click();
			
			assertThat(page.getBody().getTextContent()).doesNotContain("There are no to do");
			
			HtmlTable table = page.getHtmlElementById("toDo_table");
			assertEquals(
					"All ToDo\n"+
					"Username	Title	Description	Done	Deadline\n"+
					"username_1	title_1	description_1	"+ todo1.isDone()+ "	" + LocalDate.now().toString()+"\n"+
					"username_2	title_1	description_2	"+ todo2.isDone()+ "	" + LocalDate.now().toString(),
					removeWindowsCR(table.asText()));
		}
		
		@Test @DisplayName("test todoManagerPage delete ToDo")
		@WithMockUser(username = "AuthenticatedUser", password = "passwordTest", roles = "USER")
		void testTodoManagerPageDeleteToDo() throws Exception {
			ToDo toDo = new ToDo("AuthenticatedUser", "title_1", "description_1", LocalDate.now());
			toDo.setId(new BigInteger("0"));
			
			when(toDoService.getToDoByUserOrderByDoneAscDeadlineAsc("AuthenticatedUser")).thenReturn(Arrays.asList(toDo));
			//when(toDoService.deleteToDoById(new BigInteger("0"), "AuthenticatedUser")).thenReturn(toDo);
						
			HtmlPage page = webClient.getPage("/toDoManager/AllMyToDo");
			HtmlForm form = page.getFormByName("DeleteToDoForm");
			page = form.getButtonByName("btn_deleteToDo").click();
			
			verify(toDoService, times(1)).deleteToDoById(new BigInteger("0"), "AuthenticatedUser");
		}
	}


	
	@Nested
	@DisplayName("Tests for editToDoPage")
	class EditToDo{
	
		@Test @DisplayName("Test edit non existent toDo")
		@WithMockUser(username = "AuthenticatedUser", password = "passwordTest", roles = "USER")
		void testEditNonExistentToDo() throws Exception {
			webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);	
	
			when(toDoService.getToDoById(new BigInteger("0"))).thenThrow(ToDoNotFoundException.class);
			
			HtmlPage page = webClient.getPage("/toDoManager/editToDo/0");
			assertThat(page.getBody().getTextContent()).contains("ToDo not found!");
		}
		
		@Test @DisplayName("Test edit toDo set done true when it was false")
		@WithMockUser(username = "AuthenticatedUser", password = "passwordTest", roles = "USER")
		void testEditExistentToDo() throws Exception {
			ToDo toDo = new ToDo("AuthenticatedUser", "title_1", "description_1", LocalDate.now());
			toDo.setId(new BigInteger("0"));
			
			when(toDoService.getToDoById(new BigInteger("0"))).thenReturn(toDo);
			
			HtmlPage page = webClient.getPage("/toDoManager/editToDo/0");
			
			assertEquals("Edit ToDo", page.getTitleText());
			
			HtmlForm form = page.getFormByName("EditToDoForm");
			form.getInputByValue("title_1").setValueAttribute("modified_title");	
			form.getInputByValue("description_1").setValueAttribute("modified_description");
			
			assertEquals("", form.getInputByName("done").getCheckedAttribute());
			
			form.getInputByName("done").setChecked(true);
			form.getInputByValue(LocalDate.now().toString()).setValueAttribute(LocalDate.now().plusDays(1).toString());
			
			page = form.getButtonByName("btn_updateToDo").click();
			
			ToDo todoUpdated = new ToDo("AuthenticatedUser", "modified_title", "modified_description", LocalDate.now().plusDays(1));
			todoUpdated.setDone(true);
			
			InOrder inOrder = inOrder(toDoService);
			inOrder.verify(toDoService, times(2)).getToDoById(new BigInteger("0"));
			inOrder.verify(toDoService, times(1)).updateToDo(new BigInteger("0"), "AuthenticatedUser", todoUpdated);
			assertEquals("ToDo Manager", page.getTitleText());
		}
		
		@Test @DisplayName("Test edit toDo set done false when it was true")
		@WithMockUser(username = "AuthenticatedUser", password = "passwordTest", roles = "USER")
		void testEditExistentToDoSetDoneFalse() throws Exception {
			ToDo toDo = new ToDo("AuthenticatedUser", "title_1", "description_1", LocalDate.now());
			toDo.setId(new BigInteger("0"));
			toDo.setDone(true);
			
			when(toDoService.getToDoById(new BigInteger("0"))).thenReturn(toDo);
			
			HtmlPage page = webClient.getPage("/toDoManager/editToDo/0");
			
			assertEquals("Edit ToDo", page.getTitleText());
			
			HtmlForm form = page.getFormByName("EditToDoForm");
			form.getInputByValue("title_1").setValueAttribute("modified_title");
			form.getInputByValue("description_1").setValueAttribute("modified_description");
			
			assertEquals("checked", form.getInputByName("done").getCheckedAttribute());
			
			form.getInputByName("done").setChecked(false);
			form.getInputByValue(LocalDate.now().toString()).setValueAttribute(LocalDate.now().plusDays(1).toString());
			
			page = form.getButtonByName("btn_updateToDo").click();
			
			ToDo todoUpdated = new ToDo("AuthenticatedUser", "modified_title", "modified_description", LocalDate.now().plusDays(1));
			todoUpdated.setDone(false);
			
			InOrder inOrder = inOrder(toDoService);
			inOrder.verify(toDoService, times(2)).getToDoById(new BigInteger("0"));
			inOrder.verify(toDoService, times(1)).updateToDo(new BigInteger("0"), "AuthenticatedUser", todoUpdated);
			assertEquals("ToDo Manager", page.getTitleText());
		}
	}
	
	
	@Nested
	@DisplayName("Tests for newToDoPage")
	class NewToDo{
		
		@Test @DisplayName("Test new toDo")
		@WithMockUser(username = "AuthenticatedUser", password = "passwordTest", roles = "USER")
		void testCreateNewToDo() throws Exception {
						
			HtmlPage page = webClient.getPage("/toDoManager/newToDo");
			assertEquals("New ToDo", page.getTitleText());
			
			HtmlForm form = page.getFormByName("NewToDoForm");
			form.getInputByName("title").setValueAttribute("new_title");	
			form.getInputByName("description").setValueAttribute("new_description");
			form.getInputByName("deadline").setValueAttribute(LocalDate.now().toString());
			
			page = form.getButtonByName("btn_createToDo").click();
	
			
			verify(toDoService, times(1)).insertToDo(new ToDo("AuthenticatedUser", "new_title", "new_description", LocalDate.now()));
			assertEquals("ToDo Manager", page.getTitleText());
		}
	}
	
	
	
	private String removeWindowsCR(String s) {
		return s.replaceAll("\r", "");
		}
	
			
		

}
