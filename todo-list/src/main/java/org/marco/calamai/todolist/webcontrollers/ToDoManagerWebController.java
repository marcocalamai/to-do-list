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
			mav.addObject("message", "There are no to do");
		}
		else {
			mav.addObject("message", "");
		}
		return mav;
	}
	

}
