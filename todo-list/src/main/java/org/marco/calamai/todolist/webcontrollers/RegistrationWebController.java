package org.marco.calamai.todolist.webcontrollers;

import org.marco.calamai.todolist.exceptions.EmptyRegistrationFieldsException;
import org.marco.calamai.todolist.exceptions.UsernameAlreadyPresent;
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
	public static final String EMPTY_FIEDLD = "The username or password fields are empty!";
	
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
		ModelAndView mav = new ModelAndView();
		mav.setViewName("/registrationPage");
		mav.addObject("error_message", USERNAME_ALREADY_PRESENT);
		mav.setStatus(HttpStatus.BAD_REQUEST);
		return mav;
	}
	
	@ExceptionHandler(EmptyRegistrationFieldsException.class)
	private ModelAndView handleEmptyUsername() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("/registrationPage");
		mav.addObject("error_message", EMPTY_FIEDLD);
		mav.setStatus(HttpStatus.BAD_REQUEST);
		return mav;
	}

}
