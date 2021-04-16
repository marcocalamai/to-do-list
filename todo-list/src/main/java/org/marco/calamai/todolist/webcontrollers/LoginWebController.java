package org.marco.calamai.todolist.webcontrollers;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LoginWebController {
	
	public static final String WRONG_USERNAME_OR_PASSWORD = "Wrong username or password!";
	
	@GetMapping("/login")
	public ModelAndView getLoginIndex(String error) {
		
		ModelAndView mav = new ModelAndView("loginPage");
		
		if (error != null) {
			mav.addObject("error", error);
			mav.addObject("error_message", WRONG_USERNAME_OR_PASSWORD);
			mav.setStatus(HttpStatus.BAD_REQUEST);
		}
		return mav;
	}


}
