package org.marco.calamai.todolist.webcontrollers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexWebController {
	
	@GetMapping("/")
	public String index() {
		return "index";
	}

}
