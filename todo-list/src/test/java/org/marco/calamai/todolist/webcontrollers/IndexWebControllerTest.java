package org.marco.calamai.todolist.webcontrollers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = IndexWebController.class)
class IndexWebControllerTest {
	
	@Autowired
	private MockMvc mvc;
	
	@Test @DisplayName("Index status 200")
	void testStatus200() throws Exception {
		mvc.perform(get("/")).andExpect(status().isOk()).andExpect(view().name("index"));
	}

}
