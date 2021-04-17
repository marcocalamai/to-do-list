package org.marco.calamai.todolist.webcontrollers;

import java.time.DateTimeException;
import java.util.List;

import org.marco.calamai.todolist.model.ToDo;
import org.marco.calamai.todolist.services.ToDoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ToDoManagerWebController {

	private static final String TO_DO_MANAGER_PAGE = "toDoManagerPage";
	
	private static final String ALL_TO_DO_ATTRIBUTE = "allToDo";
	private static final String ALL_MY_TO_DO_ATTRIBUTE = "allMyToDo";
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
		mav.setViewName(TO_DO_MANAGER_PAGE);
		mav.addObject(ALL_TO_DO_ATTRIBUTE, allToDo);
		addMessageToModel(mav, allToDo);
		return mav;
	}
	
	
	@GetMapping("/toDoManager/AllMyToDo")
	public ModelAndView getAllMyToDo(@AuthenticationPrincipal UserDetails principal, ModelAndView mav) {
		List<ToDo> allMyToDo = toDoService.getToDoByUserOrderByDoneAscDeadlineAsc(principal.getUsername());
		mav.setViewName(TO_DO_MANAGER_PAGE);
		mav.addObject(ALL_MY_TO_DO_ATTRIBUTE, allMyToDo);
		addMessageToModel(mav, allMyToDo);
		return mav;
	}
	
	@GetMapping("/toDoManager/toDoByTitle")
	public ModelAndView getAllToDoByTitle(@RequestParam String title, ModelAndView mav) {
		List<ToDo> allToDoByTitle = toDoService.getAllToDoByTitleOrderByDoneAscDeadlineAsc(title);
		mav.setViewName(TO_DO_MANAGER_PAGE);
		mav.addObject(ALL_TO_DO_ATTRIBUTE, allToDoByTitle);
		addMessageToModel(mav, allToDoByTitle);
		return mav;
	}
	
	@GetMapping("/toDoManager/toDoByDeadline")
	public ModelAndView getAllToDoByDeadline(
			@RequestParam int year,
			@RequestParam int month,
			@RequestParam int day,
			ModelAndView mav) {
		List<ToDo> allToDoByDeadline = toDoService.getAllToDoByDeadlineOrderByDoneAsc(year, month, day);
		mav.setViewName(TO_DO_MANAGER_PAGE);
		mav.addObject(ALL_TO_DO_ATTRIBUTE, allToDoByDeadline);
		addMessageToModel(mav, allToDoByDeadline);
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
	
	@ExceptionHandler({DateTimeException.class, NumberFormatException.class})
	private ModelAndView handleDateInputError() {
		ModelAndView mav = new ModelAndView(TO_DO_MANAGER_PAGE);
		mav.addObject("info_message", "The date inserted is not a valid date!");
		mav.setStatus(HttpStatus.BAD_REQUEST);
		return mav;
	}
}
			
	

