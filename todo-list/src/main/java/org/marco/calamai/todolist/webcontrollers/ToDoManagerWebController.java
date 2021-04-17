package org.marco.calamai.todolist.webcontrollers;

import java.util.List;

import org.marco.calamai.todolist.model.ToDo;
import org.marco.calamai.todolist.services.ToDoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ToDoManagerWebController {
	
	private static final String MESSAGE_ATTRIBUTE = "message";
	
	private static final String NO_TO_DO_MESSAGE = "There are no to do";
	
	
	private ToDoService toDoService;
	
	@Autowired
	public ToDoManagerWebController(ToDoService toDoService) {
		this.toDoService = toDoService;
	}
	
	@GetMapping("/toDoManager")
	public ModelAndView getAllToDo(ModelAndView mav) {
		List<ToDo> allToDo = toDoService.getAllToDoOrderByDoneAscDeadlineAsc();
		mav.setViewName("toDoManagerPage");
		mav.addObject("allToDo", allToDo);
		addMessageToModel(mav, allToDo);
		return mav;
	}
	
	
	@GetMapping("/toDoManager/AllMyToDo")
	public ModelAndView getAllMyToDo(@AuthenticationPrincipal UserDetails principal, ModelAndView mav) {
		List<ToDo> allMyToDo = toDoService.getToDoByUserOrderByDoneAscDeadlineAsc(principal.getUsername());
		mav.setViewName("toDoManagerPage");
		mav.addObject("allMyToDo", allMyToDo);
		addMessageToModel(mav, allMyToDo);
		return mav;
	}
	
	private void addMessageToModel(ModelAndView mav, List<ToDo> allToDo) {
		if (allToDo.isEmpty()){
			mav.addObject(MESSAGE_ATTRIBUTE, NO_TO_DO_MESSAGE);
		}
		else {
			mav.addObject(MESSAGE_ATTRIBUTE, "");
		}
	}
}
			
	

