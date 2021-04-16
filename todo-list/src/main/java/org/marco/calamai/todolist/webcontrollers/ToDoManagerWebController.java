package org.marco.calamai.todolist.webcontrollers;

import java.util.List;

import org.marco.calamai.todolist.model.ToDo;
import org.marco.calamai.todolist.services.ToDoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ToDoManagerWebController {
	
	static final String MESSAGE_ATTRIBUTE = "message";
	
	static final String NO_TO_DO_MESSAGE = "There are no to do";
	
	
	private ToDoService toDoService;
	
	@Autowired
	public ToDoManagerWebController(ToDoService toDoService) {
		this.toDoService = toDoService;
	}
	
	@GetMapping("/toDoManager")
	public ModelAndView getToDoModelIndex(ModelAndView mav) {
		List<ToDo> allToDo = toDoService.getAllToDoOrderByDoneAscDeadlineAsc();
		mav.setViewName("toDoManagerPage");
		mav.addObject("allToDo", allToDo);
		if (allToDo.isEmpty()){
			mav.addObject(MESSAGE_ATTRIBUTE, NO_TO_DO_MESSAGE);
		}
		else {
			mav.addObject(MESSAGE_ATTRIBUTE, "");
		}
		return mav;
	}
	

}
