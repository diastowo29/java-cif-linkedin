package com.example;

import java.util.HashMap;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@SpringBootApplication
@RequestMapping("/linkedin/")
public class Linkedin {
	String returnUrl = "";
	String getAccessToken = "https://graph.facebook.com/v2.11/oauth/access_token?client_id=376575612769500&redirect_uri=https://cif-instagram.herokuapp.com/zendesk/instagram/mytoken/&client_secret=c95fc0a354beb66dc9bb490e85762ec3&code=";
	String getAccounts = "";

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
		try {

			ObjectMapper mapper = new ObjectMapper();
			HashMap<String, String> response = new HashMap<>();
			Calling calling = new Calling();
			String output = calling.callingGet(getAccessToken + token);
			response = mapper.readValue(output, new TypeReference<HashMap<String, String>>() {
			});

		} catch (Exception e) {
			e.printStackTrace();
		}

		return "igAccounts";
	}

	@RequestMapping("/testing")
	ModelAndView testingMethod() {
		ModelAndView mav = new ModelAndView("testing");

		return mav;
	}

}
