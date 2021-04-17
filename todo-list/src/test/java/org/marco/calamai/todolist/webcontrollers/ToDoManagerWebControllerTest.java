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
import org.marco.calamai.todolist.exceptions.ToDoNotFoundException;
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
	
	
	private static final String TO_DO_MANAGER_PAGE = "toDoManagerPage";

	private static final String ALL_TO_DO_ATTRIBUTE = "allToDo";
	private static final String ALL_MY_TO_DO_ATTRIBUTE = "allMyToDo";
	private static final String MESSAGE_ATTRIBUTE = "message";
	
	private static final String NO_TO_DO_MESSAGE = "There are no to do";
	

	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private ToDoService toDoService;
	

	
	@Test @DisplayName("Test ToDoManager status code is 200 and view name")
	@WithMockUser(username = "AuthenticatedUser", password = "passwordTest", roles = "USER")
	void testStatus200AndReturnToDoManagerView() throws Exception{
		mvc.perform(get("/toDoManager")).andExpect(status().isOk()).andExpect(view().name(TO_DO_MANAGER_PAGE));
	}
	
	@Nested @DisplayName("Test for search and show toDo")
	class showToDo {


		@Test @DisplayName("Test ToDoManager show all todo and empty message")
		@WithMockUser(username = "AuthenticatedUser", password = "passwordTest", roles = "USER")
		void testToDoManagerViewShowAllToDo() throws Exception {
			ToDo toDo1 = new ToDo("username_1", "title_1", "description_1", LocalDate.now());
			toDo1.setId(new BigInteger("0"));
			List<ToDo> allToDo = new ArrayList<>();
			allToDo.add(toDo1);
			
			when(toDoService.getAllToDoOrderByDoneAscDeadlineAsc()).thenReturn(allToDo);
			mvc.perform(get("/toDoManager"))
					.andExpect(view().name(TO_DO_MANAGER_PAGE))
					.andExpect(model().attribute(ALL_TO_DO_ATTRIBUTE, allToDo))
					.andExpect(model().attribute(MESSAGE_ATTRIBUTE, ""));
			
			verify(toDoService, times(1)).getAllToDoOrderByDoneAscDeadlineAsc();
		}
		
		@Test @DisplayName("Test ToDoManager show message when there are not to do")
		@WithMockUser(username = "AuthenticatedUser", password = "passwordTest", roles = "USER")
		void testToDoManagerViewWhenThereAreNotToDo() throws Exception {
			when(toDoService.getAllToDoOrderByDoneAscDeadlineAsc()).thenReturn(Collections.emptyList());
			
			mvc.perform(get("/toDoManager"))
					.andExpect(view().name(TO_DO_MANAGER_PAGE))
					.andExpect(model().attribute(ALL_TO_DO_ATTRIBUTE, Collections.emptyList()))
					.andExpect(model().attribute(MESSAGE_ATTRIBUTE, NO_TO_DO_MESSAGE));
			
			verify(toDoService, times(1)).getAllToDoOrderByDoneAscDeadlineAsc();
		}
		
		@Test @DisplayName("Test show all my todo")
		@WithMockUser(username = "AuthenticatedUser", password = "passwordTest", roles = "USER")
		void testShowAllMyToDo() throws Exception {
			ToDo toDo1 = new ToDo("AuthenticatedUser", "title_1", "description_1", LocalDate.now());
			toDo1.setId(new BigInteger("0"));
			List<ToDo> allMyToDo = new ArrayList<>();
			allMyToDo.add(toDo1);
			
			when(toDoService.getToDoByUserOrderByDoneAscDeadlineAsc("AuthenticatedUser")).thenReturn(allMyToDo);
			mvc.perform(get("/toDoManager/AllMyToDo"))
					.andExpect(status().isOk())
					.andExpect(view().name(TO_DO_MANAGER_PAGE))
					.andExpect(model().attribute(ALL_MY_TO_DO_ATTRIBUTE, allMyToDo))
					.andExpect(model().attribute(MESSAGE_ATTRIBUTE, ""));
			
			verify(toDoService, times(1)).getToDoByUserOrderByDoneAscDeadlineAsc("AuthenticatedUser");
		}
		
		@Test @DisplayName("Test show all my todo when there are not, show message")
		@WithMockUser(username = "AuthenticatedUser", password = "passwordTest", roles = "USER")
		void testShowAllMyToDoWhenThereAreNot() throws Exception {
			when(toDoService.getToDoByUserOrderByDoneAscDeadlineAsc("AuthenticatedUser")).thenReturn(Collections.emptyList());
			
			mvc.perform(get("/toDoManager/AllMyToDo"))
					.andExpect(status().isOk())
					.andExpect(view().name(TO_DO_MANAGER_PAGE))
					.andExpect(model().attribute(ALL_MY_TO_DO_ATTRIBUTE, Collections.emptyList()))
					.andExpect(model().attribute(MESSAGE_ATTRIBUTE, NO_TO_DO_MESSAGE));
			
			verify(toDoService, times(1)).getToDoByUserOrderByDoneAscDeadlineAsc("AuthenticatedUser");
		}
		
		

		@Test @DisplayName("Test search toDo by title")
		@WithMockUser(username = "AuthenticatedUser", password = "passwordTest", roles = "USER")
		void testSerachToDoByTitle() throws Exception {
			ToDo toDo1 = new ToDo("user_1", "title_1", "description_1", LocalDate.now());
			toDo1.setId(new BigInteger("0"));
			List<ToDo> allToDoByTitle = new ArrayList<>();
			allToDoByTitle.add(toDo1);
			
			when(toDoService.getAllToDoByTitleOrderByDoneAscDeadlineAsc("title_1")).thenReturn(allToDoByTitle);
			
			mvc.perform(get("/toDoManager/toDoByTitle")
					.param("title", "title_1"))
					.andExpect(status().isOk())
					.andExpect(view().name(TO_DO_MANAGER_PAGE))
					.andExpect(model().attribute(ALL_TO_DO_ATTRIBUTE, allToDoByTitle))
					.andExpect(model().attribute(MESSAGE_ATTRIBUTE, ""));
			
			verify(toDoService, times(1)).getAllToDoByTitleOrderByDoneAscDeadlineAsc("title_1");
		}
		
		@Test @DisplayName("Test search ToDo by title when there are not, show message")
		@WithMockUser(username = "AuthenticatedUser", password = "passwordTest", roles = "USER")
		void testSerachToDoByTitleWhenThereAreNot() throws Exception {
			when(toDoService.getAllToDoByTitleOrderByDoneAscDeadlineAsc("title_1")).thenReturn(Collections.emptyList());
			
			mvc.perform(get("/toDoManager/toDoByTitle")
					.param("title", "title_1"))
					.andExpect(status().isOk())
					.andExpect(view().name(TO_DO_MANAGER_PAGE))
					.andExpect(model().attribute(ALL_TO_DO_ATTRIBUTE, Collections.emptyList()))
					.andExpect(model().attribute(MESSAGE_ATTRIBUTE, NO_TO_DO_MESSAGE));
			
			verify(toDoService, times(1)).getAllToDoByTitleOrderByDoneAscDeadlineAsc("title_1");
		}
		
		@Test @DisplayName("Test search ToDo by title when title attribute is empty")
		@WithMockUser(username = "AuthenticatedUser", password = "passwordTest", roles = "USER")
		void testSerachToDoByTitleWhenItIsEmpty() throws Exception {		
			mvc.perform(get("/toDoManager/toDoByTitle"))
					.andExpect(status().is4xxClientError());
				}

		
		@Test @DisplayName("Test search toDo by deadline")
		@WithMockUser(username = "AuthenticatedUser", password = "passwordTest", roles = "USER")
		void testSerachToDoByDeadline() throws Exception {
			LocalDate deadline = LocalDate.of(2040, 12, 31);
			ToDo toDo1 = new ToDo("user_1", "title_1", "description_1", deadline);
			toDo1.setId(new BigInteger("0"));
			List<ToDo> allToDoByDeadline = new ArrayList<>();
			allToDoByDeadline.add(toDo1);
			
			when(toDoService.getAllToDoByDeadlineOrderByDoneAsc(deadline)).thenReturn(allToDoByDeadline);
			
			mvc.perform(get("/toDoManager/toDoByDeadline")
					.param("deadline", "2040-12-31"))
					.andExpect(status().isOk())
					.andExpect(view().name(TO_DO_MANAGER_PAGE))
					.andExpect(model().attribute(ALL_TO_DO_ATTRIBUTE, allToDoByDeadline))
					.andExpect(model().attribute(MESSAGE_ATTRIBUTE, ""));
			
			verify(toDoService, times(1)).getAllToDoByDeadlineOrderByDoneAsc(deadline);
		}

		
		@Test @DisplayName("Test search toDo by deadline when there are not, show message")
		@WithMockUser(username = "AuthenticatedUser", password = "passwordTest", roles = "USER")
		void testSerachToDoByDeadlineWhenThereAreNot() throws Exception {
			LocalDate deadline = LocalDate.of(2040, 12, 31);
			when(toDoService.getAllToDoByDeadlineOrderByDoneAsc(deadline)).thenReturn(Collections.emptyList());
			
			mvc.perform(get("/toDoManager/toDoByDeadline")
					.param("deadline", "2040-12-31"))
					.andExpect(status().isOk())
					.andExpect(view().name(TO_DO_MANAGER_PAGE))
					.andExpect(model().attribute(ALL_TO_DO_ATTRIBUTE, Collections.emptyList()))
					.andExpect(model().attribute(MESSAGE_ATTRIBUTE, NO_TO_DO_MESSAGE));
			
			verify(toDoService, times(1)).getAllToDoByDeadlineOrderByDoneAsc(deadline);
		}
		
		
		@Test @DisplayName("Test search toDo by deadline when it is not a valid date")
		@WithMockUser(username = "AuthenticatedUser", password = "passwordTest", roles = "USER")
		void testSerachToDoByDeadlineWhenItIsNotValid() throws Exception {
			mvc.perform(get("/toDoManager/toDoByDeadline")
					.param("deadline", "2040-122-311"))
					.andExpect(status().is4xxClientError())
					.andExpect(view().name(TO_DO_MANAGER_PAGE))
					.andExpect(model().attribute(MESSAGE_ATTRIBUTE, "The date inserted is not a valid date!"));
		}
		
		@Test @DisplayName("Test search ToDo by deadline when deadline attribute is empty")
		@WithMockUser(username = "AuthenticatedUser", password = "passwordTest", roles = "USER")
		void testSerachToDoByDeadlineWhenItIsEmpty() throws Exception {		
			mvc.perform(get("/toDoManager/toDoByDeadline"))
					.andExpect(status().is4xxClientError());
				}
	}
	
	@Nested @DisplayName("Test for edit and insert toDo")
	class insertEditToDo {
		
		@Test @DisplayName("Test edit toDo")
		@WithMockUser(username = "AuthenticatedUser", password = "passwordTest", roles = "USER")
		void testEditToDo() throws Exception {
			ToDo toDo1 = new ToDo("username_1", "title_1", "description_1", LocalDate.now());
			toDo1.setId(new BigInteger("0"));
				
			when(toDoService.getToDoById(new BigInteger("0"))).thenReturn(toDo1);
			
			mvc.perform(get("/toDoManager/editToDo/0"))
					.andExpect(status().isOk())
					.andExpect(view().name("editToDoPage"))
					.andExpect(model().attribute("toDo", toDo1));
			
			verify(toDoService, times(1)).getToDoById(new BigInteger("0"));
		}
		
		@Test @DisplayName("Test edit toDo when it is not found")
		@WithMockUser(username = "AuthenticatedUser", password = "passwordTest", roles = "USER")
		void testEditToDoWhenNotFound() throws Exception {
			when(toDoService.getToDoById(new BigInteger("0"))).thenThrow(ToDoNotFoundException.class);
			
			mvc.perform(get("/toDoManager/editToDo/0"))
					.andExpect(status().is4xxClientError())
					.andExpect(view().name(TO_DO_MANAGER_PAGE))
					.andExpect(model().attribute(MESSAGE_ATTRIBUTE, "User not found!"));
			
			verify(toDoService, times(1)).getToDoById(new BigInteger("0"));			
		}
		
		@Test @DisplayName("Test insert new toDo")
		@WithMockUser(username = "AuthenticatedUser", password = "passwordTest", roles = "USER")
		void testInsertNewToDo() throws Exception {
			mvc.perform(get("/toDoManager/newToDo"))
					.andExpect(status().isOk())
					.andExpect(view().name("editToDoPage"))
					.andExpect(model().attribute("toDo", new ToDo()));
		}
	}

	
}
