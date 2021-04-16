package org.marco.calamai.todolist.webcontrollers;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.marco.calamai.todolist.model.ToDo;
import org.marco.calamai.todolist.services.ToDoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = ToDoManagerWebController.class)

@DisplayName("Test ToDoManager web controller")
class ToDoManagerWebControllerTest {
	
	private static final String MESSAGE_ATTRIBUTE = "message";
	
	private static final String NO_TO_DO_MESSAGE = "There are no to do";
	
	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private ToDoService toDoService;
	

	
	@Test @DisplayName("Test ToDoManager status code is 200 and view name")
	@WithMockUser(username = "userTest", password = "passwordTest", roles = "USER")
	void testStatus200AndReturnToDoManagerView() throws Exception{
		mvc.perform(get("/toDoManager")).andExpect(status().isOk()).andExpect(view().name("toDoManagerPage"));
	}
	
	@Nested @DisplayName("Test for show toDo")
	class showToDo {
		
		@Test @DisplayName("Test ToDoManager show all todo and empty message")
		@WithMockUser(username = "userTest", password = "passwordTest", roles = "USER")
		void testToDoManagerViewShowAllToDo() throws Exception {
			ToDo toDo1 = new ToDo("username_1", "title_1", "description_1", LocalDate.now());
			toDo1.setId(new BigInteger("0"));
			List<ToDo> allToDo = new ArrayList<>();
			allToDo.add(toDo1);
			
			when(toDoService.getAllToDoOrderByDoneAscDeadlineAsc()).thenReturn(allToDo);
			mvc.perform(get("/toDoManager"))
					.andExpect(view().name("toDoManagerPage"))
					.andExpect(model().attribute("allToDo", allToDo))
					.andExpect(model().attribute("message", ""));
			
			verify(toDoService, times(1)).getAllToDoOrderByDoneAscDeadlineAsc();
		}
		
		@Test @DisplayName("Test ToDoManager show message when there are not to do")
		@WithMockUser(username = "userTest", password = "passwordTest", roles = "USER")
		void testToDoManagerViewWhenThereAreNotToDo() throws Exception {
			when(toDoService.getAllToDoOrderByDoneAscDeadlineAsc()).thenReturn(Collections.emptyList());
			
			mvc.perform(get("/toDoManager"))
					.andExpect(view().name("toDoManagerPage"))
					.andExpect(model().attribute("allToDo", Collections.emptyList()))
					.andExpect(model().attribute(MESSAGE_ATTRIBUTE, NO_TO_DO_MESSAGE));
			
			verify(toDoService, times(1)).getAllToDoOrderByDoneAscDeadlineAsc();
		}
		
		@Test @DisplayName("Test /toDoManager/AllMyToDo show all my todo")
		@WithMockUser(username = "AuthenticatedUser", password = "passwordTest", roles = "USER")
		void testShowAllMyToDo() throws Exception {
			ToDo toDo1 = new ToDo("AuthenticatedUser", "title_1", "description_1", LocalDate.now());
			toDo1.setId(new BigInteger("0"));
			List<ToDo> allMyToDo = new ArrayList<>();
			allMyToDo.add(toDo1);
			
			when(toDoService.getToDoByUserOrderByDoneAscDeadlineAsc("AuthenticatedUser")).thenReturn(allMyToDo);
			mvc.perform(get("/toDoManager/AllMyToDo"))
					.andExpect(status().isOk())
					.andExpect(view().name("toDoManagerPage"))
					.andExpect(model().attribute("allToDo", allMyToDo))
					.andExpect(model().attribute(MESSAGE_ATTRIBUTE, ""));
			
			verify(toDoService, times(1)).getToDoByUserOrderByDoneAscDeadlineAsc("AuthenticatedUser");
			
		}
	}
	


}
