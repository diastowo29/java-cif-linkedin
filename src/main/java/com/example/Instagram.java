package com.example;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@SpringBootApplication
@RequestMapping("/linkedin/")
public class Instagram {
	String returnUrl = "";
	String fbApiDomain = "https://graph.facebook.com/v3.0";
	String getAccessToken = fbApiDomain
			+ "/oauth/access_token?client_id=376575612769500&redirect_uri=https://cif-instagram.herokuapp.com/zendesk/instagram/mytoken/&client_secret=c95fc0a354beb66dc9bb490e85762ec3&code=";
	String getIgAccountsId = fbApiDomain + "/me/accounts?fields=connected_instagram_account,name&access_token=";

	@RequestMapping(method = RequestMethod.GET)
	String indexGet() {
		System.out.println("GET LINKEDIN");
		returnUrl = "testing";
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
		return "get_token";
	}

	@RequestMapping("/submittoken")
	String finalSubmit(@RequestParam(name = "getId") String igId, @RequestParam(name = "name") String igName, @RequestParam(name = "token") String igToken, Model model) {
		HashMap<String, String> hashMap = new HashMap<>();
		hashMap.put("returnUrl", returnUrl);
		System.out.println("RETURN URL: " + returnUrl);
		hashMap.put("igId", igId);
		try {
			hashMap.put("name", "Instagram - " + URLDecoder.decode(igName, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		hashMap.put("metadata", "{\"igId\": \"" + igId + "\", \"token\": \"" + igToken + "\"}");

		model.addAttribute("metadata", hashMap);
		return "final_submit";
	}

	@RequestMapping("/submit")
	String submitToken(@RequestParam("token") String token, Model model) {
		System.out.println("GET SUBMIT TOKEN: " + token);
		String accToken = "";
		Calling calling = new Calling();

		HashMap<String, String> hashMap = new HashMap<>();
		ArrayList<HashMap<String, String>> hashList = new ArrayList<>();
		try {

			JSONObject output = calling.callingGet(getAccessToken + token);
			accToken = output.getString("access_token");

			try {

				JSONObject outputAcc = calling.callingGet(getIgAccountsId + accToken);
				JSONArray igData = outputAcc.getJSONArray("data");
				if (outputAcc != null) {
					for (int i = 0; i < igData.length(); i++) {
						hashMap = new HashMap<>();
						hashMap.put("name", igData.getJSONObject(i).getString("name"));
						hashMap.put("id",
								igData.getJSONObject(i).getJSONObject("connected_instagram_account").getString("id"));
						hashMap.put("token", accToken);
						hashList.add(hashMap);
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		model.addAttribute("igList", hashList);
		return "ig_account";
	}

	@RequestMapping("/testing")
	String testingMethod(@RequestParam(name = "name", defaultValue = "dias") String name, Model model) {
		ArrayList<HashMap<String, String>> list = new ArrayList<>();

		HashMap<String, String> hashMap = new HashMap<>();
		hashMap.put("name", "amizah");
		hashMap.put("id", "1");
		list.add(hashMap);

		hashMap = new HashMap<>();
		hashMap.put("name", "diastowo");
		hashMap.put("id", "2");
		list.add(hashMap);

		model.addAttribute("namelist", list);

		return "testing";
	}

}
