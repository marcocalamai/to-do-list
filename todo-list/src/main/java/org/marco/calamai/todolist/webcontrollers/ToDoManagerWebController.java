package org.marco.calamai.todolist.webcontrollers;

import java.math.BigInteger;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.List;

import org.marco.calamai.todolist.exceptions.InvalidTimeException;
import org.marco.calamai.todolist.exceptions.ToDoNotFoundException;
import org.marco.calamai.todolist.exceptions.WrongUsernameException;
import org.marco.calamai.todolist.model.ToDo;
import org.marco.calamai.todolist.services.ToDoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ToDoManagerWebController {

	private static final String NEW_TO_DO_PAGE = "newToDoPage";
	private static final String EDIT_TO_DO_PAGE = "editToDoPage";
	private static final String TO_DO_MANAGER_PAGE = "toDoManagerPage";
	
	private static final String ALL_TO_DO_ATTRIBUTE = "allToDo";
	private static final String ALL_MY_TO_DO_ATTRIBUTE = "allMyToDo";
	private static final String MESSAGE_ATTRIBUTE = "message";
	
	private static final String NO_TO_DO_MESSAGE = "There are no to do";
	private static final String NOT_VALID_DATE = "The deadline inserted is not a valid date!";
	private static final String TODO_NOT_FOUND = "ToDo not found!";
	private static final String DEADLINE_HAS_PASSED = "The deadline inserted has passed!";
	private static final String DIFFERENT_USER = "Can't edit or delete other users' ToDo";
	private static final String DELETE_SUCCESSFUL = "ToDo successfully deleted!";

	
	
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
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate deadline,
			ModelAndView mav) {
		List<ToDo> allToDoByDeadline = toDoService.getAllToDoByDeadlineOrderByDoneAsc(deadline);
		mav.setViewName(TO_DO_MANAGER_PAGE);
		mav.addObject(ALL_TO_DO_ATTRIBUTE, allToDoByDeadline);
		addMessageToModel(mav, allToDoByDeadline);
		return mav;
	}
	
	@GetMapping("/toDoManager/editToDo/{id}")
	public ModelAndView editToDo(@PathVariable BigInteger id, ModelAndView mav) {
		ToDo toDoById = toDoService.getToDoById(id);
		mav.setViewName(EDIT_TO_DO_PAGE);
		mav.addObject("toDo", toDoById);
		return mav;
	}
	
	@GetMapping("/toDoManager/newToDo")
	public ModelAndView newToDo(ModelAndView mav) {
		mav.setViewName(NEW_TO_DO_PAGE);
		mav.addObject("toDo", new ToDo());
		return mav;
	}
	
	@PostMapping("/toDoManager/saveNewToDo")
	public String saveTodo(
			@AuthenticationPrincipal UserDetails principal,
			@RequestParam  String title,
			@RequestParam String description,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate deadline) {
		
		toDoService.insertToDo(new ToDo(principal.getUsername(), title, description, deadline));
		return "redirect:/toDoManager";
	}
	
	@PostMapping("/toDoManager/updateToDo")
	public String updateTodo(
			@AuthenticationPrincipal UserDetails principal,
			@RequestParam BigInteger id,
			@RequestParam  String title,
			@RequestParam String description,
			String done,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate deadline) {
		
		String username = toDoService.getToDoById(id).getUser();
		
		ToDo todo = new ToDo(username, title, description, deadline);
		todo.setDone(done!=null);
		toDoService.updateToDo(id, principal.getUsername(), todo);
		return "redirect:/toDoManager";
	}
	
	@PostMapping("/toDoManager/deleteToDo/{id}")
	public ModelAndView deleteToDo(@PathVariable BigInteger id, @AuthenticationPrincipal UserDetails principal, ModelAndView mav) {
		ToDo toDoDeleted = toDoService.deleteToDoById(id, principal.getUsername());
		mav.setViewName(TO_DO_MANAGER_PAGE);
		mav.addObject("toDoDeleted", toDoDeleted);
		mav.addObject(MESSAGE_ATTRIBUTE, DELETE_SUCCESSFUL);
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
	
	@ExceptionHandler(DateTimeException.class)
	private ModelAndView handleDateInputError() {
		return setupModel(NOT_VALID_DATE);
	}

	@ExceptionHandler(ToDoNotFoundException.class)
	private ModelAndView handleToDoNotFound() {
		return setupModel(TODO_NOT_FOUND);
	}
	
	@ExceptionHandler(InvalidTimeException.class)
	private ModelAndView handleInvalidDeadline() {
		return setupModel(DEADLINE_HAS_PASSED);
	}
	
	@ExceptionHandler(WrongUsernameException.class)
	private ModelAndView handleDifferentUsername() {
		return setupModel(DIFFERENT_USER);
		
	}

	private ModelAndView setupModel(String errorMessage) {
		ModelAndView mav = new ModelAndView(TO_DO_MANAGER_PAGE);
		mav.addObject(MESSAGE_ATTRIBUTE, errorMessage);
		mav.setStatus(HttpStatus.BAD_REQUEST);
		return mav;
	}
	
}
			
	

