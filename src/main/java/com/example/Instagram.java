package com.example;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.Urls.Entity;

@Controller
@SpringBootApplication
@CrossOrigin
@RequestMapping("/instagram/")
public class Instagram {
	Entity entity = new Entity();
	String RETURNURL = "";

	@RequestMapping(method = RequestMethod.GET)
	String indexGet() {
		System.out.println("GET instagram");
		RETURNURL = "testing";
		return "admin";
	}

	@RequestMapping(method = RequestMethod.POST, consumes = { MediaType.APPLICATION_FORM_URLENCODED_VALUE,
			MediaType.APPLICATION_JSON_UTF8_VALUE }, produces = { MediaType.APPLICATION_ATOM_XML_VALUE,
					MediaType.APPLICATION_JSON_UTF8_VALUE })
	String indexPost(@RequestParam Map<String, String> paramMap) {
		RETURNURL = paramMap.get("return_url");
		System.out.println(RETURNURL);
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
		hashMap.put("RETURNURL", RETURNURL);
		System.out.println("RETURN URL: " + RETURNURL);
		hashMap.put("igId", igId);
		try {
			hashMap.put("name", "Instagram - " + URLDecoder.decode(igName, "UTF-8"));
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

			JSONObject output = calling.callingGet(entity.GET_ACC_TOKEN_API + token);
			accToken = output.getString("access_token");
			// accToken =
			// "EAAcIvztqj7IBAIsFZAqZC5VieI82WSGIm1XWV8fqEJ6KMgDv0hQin3gCjFt857BkoiBZBSMSQoO5KghXsQZApmTrnMJafPkYCjhfQHNN9Sspq2b9FKOqPuQHbj0cR4X9jUv5u5g9nPitU8rPIxpXOwUzkW6DvMTyAz92qiAQs4ZBN5vnlelNumkCDvMCMiQvfykB4Wv4xHQZDZD";

			try {

				JSONObject outputAcc = calling.callingGet(entity.GET_ACC_ID_API + accToken);
				JSONArray igData = outputAcc.getJSONArray("data");
				if (outputAcc != null) {
					for (int i = 0; i < igData.length(); i++) {
						hashMap = new HashMap<>();
						if (igData.getJSONObject(i).has("connected_instagram_account")) {
							hashMap.put("name", igData.getJSONObject(i).getString("name"));
							hashMap.put("id", igData.getJSONObject(i).getJSONObject("connected_instagram_account")
									.getString("id"));
							hashMap.put("token", accToken);
							hashList.add(hashMap);
						}
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

	@RequestMapping("/callback")
	String callBack(@RequestParam("code") String code, Model model) {
		model.addAttribute("code", code);
		return "callback";
	}

	@RequestMapping("/manifest")
	ResponseEntity<Object> manifest() {
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("name", "Instagram Integration Java");
		hashMap.put("id", "zendesk-internal-instagram-integration-java");
		hashMap.put("author", "Diastowo Faryduana");
		hashMap.put("version", "v1.0");
		HashMap<String, String> urlMap = new HashMap<>();
		urlMap.put("admin_ui", entity.HEROKUDOMAIN + "instagram/");
		urlMap.put("pull_url", entity.HEROKUDOMAIN + "instagram/pull");
		urlMap.put("channelback_url", entity.HEROKUDOMAIN + "instagram/manifest");
		urlMap.put("clickthrough_url", entity.HEROKUDOMAIN + "instagram/manifest");

		hashMap.put("urls", urlMap);
		return new ResponseEntity<Object>(hashMap, HttpStatus.OK);
	}

	@RequestMapping("/pull")
	ResponseEntity<Object> pulling(@RequestParam Map<String, String> paramMap) {
		System.out.println(paramMap.get("metadata"));
		JSONObject jobject = new JSONObject();
		String igId = "";
		String igToken = "";
		try {
			jobject = new JSONObject(paramMap.get("metadata").toString());
			System.out.println(jobject.get("igId"));
			igId = jobject.getString("igId");
			igToken = jobject.getString("token");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		HashMap<String, String> hashMap = new HashMap<>();
		return new ResponseEntity<Object>(hashMap, HttpStatus.OK);
	}
	
	@RequestMapping("/webhook")
	public ResponseEntity<String> webhook () {
		System.out.println("WEbHOOK Triggered");
		return new ResponseEntity<String>("1", HttpStatus.OK);
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
