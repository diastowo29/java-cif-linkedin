package com.example;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@SpringBootApplication
@RequestMapping("/linkedin")
public class Linkedin {
	
	@RequestMapping(method = RequestMethod.GET)
	String index() {
		System.out.println("GET LINKEDIN");
		return "index";
	}

	@RequestMapping(method = RequestMethod.POST)
	String indexGet() {
		System.out.println("POST LINKEDIN");
		return "index";
	}

}
