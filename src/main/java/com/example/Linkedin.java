package com.example;

import java.util.HashMap;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@SpringBootApplication
@RequestMapping("/linkedin/")
public class Linkedin {
	String returnUrl = "";

	@RequestMapping(method = RequestMethod.GET)
	String indexGet() {
		System.out.println("GET LINKEDIN");
		return "admin";
	}

	@RequestMapping(method = RequestMethod.POST)
	String indexPost(@RequestBody HashMap<String, String> jsonParam) {
		System.out.println("POST LINKEDIN");
		returnUrl = jsonParam.get("return_url");
		return "admin";
	}

	@RequestMapping("/getToken/")
	String getToken() {
		System.out.println("GET GETTOKEN");
		return "getToken";
	}

	@RequestMapping("/submit")
	String submitToken(@RequestParam("token") String token) {
		System.out.println("GET SUBMIT TOKEN: " + token);
		return "getToken";
	}

}
