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
public class Instagram {
	String returnUrl = "";
	String fbApiDomain = "https://graph.facebook.com/v3.0";
	String getAccessToken = fbApiDomain
			+ "/oauth/access_token?client_id=376575612769500&redirect_uri=https://cif-instagram.herokuapp.com/zendesk/instagram/mytoken/&client_secret=c95fc0a354beb66dc9bb490e85762ec3&code=";
	String getIgAccount = fbApiDomain
			+ "/oauth/access_token?client_id=376575612769500&redirect_uri=https://cif-instagram.herokuapp.com/zendesk/instagram/mytoken/&client_secret=c95fc0a354beb66dc9bb490e85762ec3&code=";
	String getAccounts = fbApiDomain + "/me/accounts?fields=connected_instagram_account,name";

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
		String accToken = "";
		try {

			ObjectMapper mapper = new ObjectMapper();
			HashMap<String, String> response = new HashMap<>();
			Calling calling = new Calling();
			String output = calling.callingGet(getAccessToken + token);
			System.out.println(output);
			response = mapper.readValue(output, new TypeReference<HashMap<String, String>>() {
			});
			accToken = response.get("access_token");

			try {

				ObjectMapper mapperAcc = new ObjectMapper();
				HashMap<String, String> responseAcc = new HashMap<>();
				String outputAcc = calling.callingGet(getAccounts + accToken);
				System.out.println(outputAcc);
				responseAcc = mapperAcc.readValue(outputAcc, new TypeReference<HashMap<String, String>>() {
				});

			} catch (Exception e) {
				e.printStackTrace();
			}

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
