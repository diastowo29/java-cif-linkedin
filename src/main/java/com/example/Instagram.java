package com.example;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@SpringBootApplication
@RequestMapping("/instagram/")
public class Instagram {
	String herokuDomain = "https://java-cif-linkedin.herokuapp.com/";
	String returnUrl = "";
	String fbApiDomain = "https://graph.facebook.com/v3.0";
	String getAccessToken = fbApiDomain
			+ "/oauth/access_token?client_id=376575612769500&redirect_uri=https://cif-instagram.herokuapp.com/zendesk/instagram/mytoken/&client_secret=c95fc0a354beb66dc9bb490e85762ec3&code=";
	String getIgAccountsId = fbApiDomain + "/me/accounts?fields=connected_instagram_account,name&access_token=";

	@RequestMapping(method = RequestMethod.GET)
	String indexGet() {
		System.out.println("GET instagram");
		returnUrl = "testing";
		return "admin";
	}

	@RequestMapping(method = RequestMethod.POST, consumes = { MediaType.APPLICATION_FORM_URLENCODED_VALUE,
			MediaType.APPLICATION_JSON_UTF8_VALUE }, produces = { MediaType.APPLICATION_ATOM_XML_VALUE,
					MediaType.APPLICATION_JSON_UTF8_VALUE })
	String indexPost(@RequestParam Map<String, String> paramMap) {
		returnUrl = paramMap.get("return_url");
		System.out.println(returnUrl);
		return "admin";
	}

	@RequestMapping("/getToken/")
	String getToken() {
		System.out.println("GET GETTOKEN");
		return "get_token";
	}

	@RequestMapping("/submittoken")
	String finalSubmit(@RequestParam(name = "getId") String igId, @RequestParam(name = "name") String igName,
			@RequestParam(name = "token") String igToken, Model model) {
		HashMap<String, String> hashMap = new HashMap<>();
		hashMap.put("returnUrl", returnUrl);
		System.out.println("RETURN URL: " + returnUrl);
		hashMap.put("igId", igId);
		try {
			hashMap.put("name", "" + URLDecoder.decode(igName, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		hashMap.put("metadata", "{\"igId\": \"" + igId + "\", \"token\": \"" + igToken + "\"}");
		hashMap.put("state", "{\"state\":\"testing\"}");

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

	@RequestMapping("/manifest")
	ResponseEntity<Object> manifest() {
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("name", "Instagram Integration");
		hashMap.put("id", "zendesk-internal-instagram-integration-java");
		hashMap.put("author", "Diastowo Faryduana");
		hashMap.put("version", "v1.0");
		HashMap<String, String> urlMap = new HashMap<>();
		urlMap.put("admin_ui", herokuDomain + "instagram/");
		urlMap.put("pull_url", herokuDomain + "instagram/manifest");
		urlMap.put("channelback_url", herokuDomain + "instagram/manifest");
		urlMap.put("clickthrough_url", herokuDomain + "instagram/manifest");

		hashMap.put("urls", urlMap);
		return new ResponseEntity<Object>(hashMap, HttpStatus.OK);
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
