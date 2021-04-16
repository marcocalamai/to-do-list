package org.marco.calamai.todolist.webcontrollers;

import org.marco.calamai.todolist.exceptions.EmptyRegistrationFieldsException;
import org.marco.calamai.todolist.exceptions.UsernameAlreadyPresent;
import org.marco.calamai.todolist.exceptions.WhitespaceInRegistrationFieldsException;
import org.marco.calamai.todolist.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class RegistrationWebController {
	
	public static final String USERNAME_ALREADY_PRESENT = "A user with that username already exists!";
	public static final String EMPTY_FIELD = "The username or password fields are empty!";
	public static final String WHITESPACE_IN_FIELD = "The username or password field contains one or more whitespace!";
	
	@Autowired
	private UserService userService;
	
	@GetMapping("/registration")
	public String getRegistrationIndex() {
		return "registrationPage";
	}
	
	@PostMapping("/registration")
	public String registerUser(String username, String password) {
		userService.register(username, password);
		return "redirect:/";
	}
	
	@ExceptionHandler(UsernameAlreadyPresent.class)
	private ModelAndView handleUsernameAlreadyPresent() {
		return setupModel(USERNAME_ALREADY_PRESENT);
	}
	
	@ExceptionHandler(EmptyRegistrationFieldsException.class)
	private ModelAndView handleEmptyUsernameOrPassword() {
		return setupModel(EMPTY_FIELD);
	}
	
	@ExceptionHandler(WhitespaceInRegistrationFieldsException.class)
	private ModelAndView handleWhitespaceInUsernameOrPassword() {
		return setupModel(WHITESPACE_IN_FIELD);
	}

	private ModelAndView setupModel(String errorMessage) {
		ModelAndView mav = new ModelAndView("/registrationPage");
		mav.addObject("error_message", errorMessage);
		mav.setStatus(HttpStatus.BAD_REQUEST);
		return mav;
	}

}
