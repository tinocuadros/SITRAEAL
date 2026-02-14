package uts.edu.java.sitraeal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AppController {

	//Método para mostrar el home
		@GetMapping("/") 
		public String index() {
			return "views/home"; 
		}
		
		 @GetMapping("/login")
		    public String login() {
		        return "login";
		    }
}
