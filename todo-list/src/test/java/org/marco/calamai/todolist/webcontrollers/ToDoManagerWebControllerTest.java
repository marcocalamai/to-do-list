package org.marco.calamai.todolist.webcontrollers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = ToDoManagerWebController.class)

@DisplayName("Test ToDoManager web controller")
class ToDoManagerWebControllerTest {
	
	@Autowired
	private MockMvc mvc;
	
	@Test @DisplayName("Check get ToDoManager status code and view name")
	@WithMockUser(username = "userTest", password = "passwordTest", roles = "USER")
	void testStatus200AndReturnToDoManagerView() throws Exception{
		mvc.perform(get("/toDoManager")).andExpect(status().isOk()).andExpect(view().name("toDoManagerPage"));
	}
	


}
