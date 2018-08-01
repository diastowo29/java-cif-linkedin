package com.example;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.repo.ClientRepository;
import com.example.urls.Entity;

@Controller
@SpringBootApplication
@CrossOrigin
@RequestMapping("/instagram/")
public class Instagram {

	@Autowired
	ClientRepository clientRepo;

	Entity entity = new Entity();
	String RETURNURL = "";

	@RequestMapping(method = RequestMethod.GET)
	String indexGet() {
		System.out.println("/get");
		RETURNURL = "testing";
		return "admin";
	}

	@RequestMapping(method = RequestMethod.POST, consumes = { MediaType.APPLICATION_FORM_URLENCODED_VALUE,
			MediaType.APPLICATION_JSON_UTF8_VALUE }, produces = { MediaType.APPLICATION_ATOM_XML_VALUE,
					MediaType.APPLICATION_JSON_UTF8_VALUE })
	String indexPost(@RequestParam Map<String, String> paramMap) {
		System.out.println("/post");
		RETURNURL = paramMap.get("return_url");
		System.out.println(RETURNURL);
		return "admin";
	}

	@RequestMapping("/getToken/")
	String getToken() {
		System.out.println("/getToken/");
		return "get_token";
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

	@RequestMapping("/submittoken")
	String finalSubmit(@RequestParam(name = "getId") String igId, @RequestParam(name = "name") String igName,
			@RequestParam(name = "token") String igToken, @RequestParam(name = "option") String option, Model model) {
		System.out.println("/submittoken");
		HashMap<String, String> hashMap = new HashMap<>();
		hashMap.put("returnUrl", RETURNURL);
		System.out.println("RETURN URL: " + RETURNURL);
		hashMap.put("igId", igId);
		try {
			hashMap.put("name", "Instagram - " + URLDecoder.decode(igName, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		hashMap.put("metadata",
				"{\"igId\": \"" + igId + "\", \"token\": \"" + igToken + "\", \"option\": \"" + option + "\"}");
		hashMap.put("state", "{\"state\":\"testing\"}");

		model.addAttribute("metadata", hashMap);
		return "final_submit";
	}

	@RequestMapping("/callback")
	String callBack(@RequestParam("code") String code, Model model) {
		System.out.println("/callback");
		model.addAttribute("code", code);
		return "callback";
	}

	@RequestMapping("/manifest")
	ResponseEntity<Object> manifest() {
		System.out.println("/manifest");
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("name", "Instagram Integration Java");
		hashMap.put("id", "zendesk-internal-instagram-integration-java");
		hashMap.put("author", "Diastowo Faryduana");
		hashMap.put("version", "v1.0");
		HashMap<String, String> urlMap = new HashMap<>();
		urlMap.put("admin_ui", entity.HEROKUDOMAIN + "instagram/");
		urlMap.put("pull_url", entity.HEROKUDOMAIN + "instagram/pull");
		urlMap.put("channelback_url", entity.HEROKUDOMAIN + "instagram/channelback");
		urlMap.put("clickthrough_url", entity.HEROKUDOMAIN + "instagram/manifest");

		hashMap.put("urls", urlMap);
		return new ResponseEntity<Object>(hashMap, HttpStatus.OK);
	}

	/* FIXME PULL */
	@RequestMapping("/pull")
	ResponseEntity<Object> pulling(@RequestParam Map<String, String> paramMap) throws JSONException {

		System.out.println("/pull");
		Calling calling = new Calling();
		Entity entity = new Entity();
		HashMap<String, Object> extObj = new HashMap<>();
		ArrayList<Object> extResource = new ArrayList<>();

		JSONObject jobject = new JSONObject();
		String igId = "";
		String igToken = "";
		String option = "";
		try {
			jobject = new JSONObject(paramMap.get("metadata").toString());
			igId = jobject.getString("igId");
			igToken = jobject.getString("token");
			option = jobject.getString("option");

			JSONObject allMedia = calling.callingGet(entity.getMediaUrl(igId, igToken));
			// System.out.println(allMedia);
			if (allMedia.has("data")) {
				for (int i = 0; i < allMedia.getJSONArray("data").length(); i++) {
					HashMap<String, String> author = new HashMap<>();
					author.put("external_id", "cif-user-" + allMedia.getJSONArray("data").getJSONObject(i)
							.getJSONObject("owner").getString("username") + "-" + igId);
					author.put("name", allMedia.getJSONArray("data").getJSONObject(i).getJSONObject("owner")
							.getString("username"));
					extObj = new HashMap<>();
					extObj.put("external_id",
							"cif-media-" + allMedia.getJSONArray("data").getJSONObject(i).getString("id") + "-" + igId);
					extObj.put("message", allMedia.getJSONArray("data").getJSONObject(i).getString("caption"));
					extObj.put("created_at", allMedia.getJSONArray("data").getJSONObject(i).getString("timestamp")
							.replace("+0000", "Z"));
					extObj.put("author", author);
					extObj.put("allow_channelback", true);
					extResource.add(extObj);
					if (allMedia.getJSONArray("data").getJSONObject(i).has("comments")) {
						for (int j = 0; j < allMedia.getJSONArray("data").getJSONObject(i).getJSONObject("comments")
								.getJSONArray("data").length(); j++) {
							author = new HashMap<>();
							author.put("external_id",
									"cif-user-"
											+ allMedia.getJSONArray("data").getJSONObject(i).getJSONObject("comments")
													.getJSONArray("data").getJSONObject(j).getString("username")
											+ "-" + igId);
							author.put("name", allMedia.getJSONArray("data").getJSONObject(i).getJSONObject("comments")
									.getJSONArray("data").getJSONObject(j).getString("username"));
							extObj = new HashMap<>();
							if (option.equals("1")) {
								extObj.put("parent_id", "cif-media-"
										+ allMedia.getJSONArray("data").getJSONObject(i).getString("id") + "-" + igId);
							}
							extObj.put("external_id",
									"cif-comment-"
											+ allMedia.getJSONArray("data").getJSONObject(i).getJSONObject("comments")
													.getJSONArray("data").getJSONObject(j).getString("id")
											+ "-" + igId);
							extObj.put("message", allMedia.getJSONArray("data").getJSONObject(i)
									.getJSONObject("comments").getJSONArray("data").getJSONObject(j).getString("text"));
							extObj.put("created_at",
									allMedia.getJSONArray("data").getJSONObject(i).getJSONObject("comments")
											.getJSONArray("data").getJSONObject(j).getString("timestamp")
											.replace("+0000", "Z"));
							extObj.put("author", author);
							extObj.put("allow_channelback", true);
							extResource.add(extObj);
							if (allMedia.getJSONArray("data").getJSONObject(i).getJSONObject("comments")
									.getJSONArray("data").getJSONObject(j).has("replies")) {
								for (int k = 0; k < allMedia.getJSONArray("data").getJSONObject(i)
										.getJSONObject("comments").getJSONArray("data").getJSONObject(j)
										.getJSONObject("replies").getJSONArray("data").length(); k++) {
									author = new HashMap<>();
									author.put("external_id", "cif-user-"
											+ allMedia.getJSONArray("data").getJSONObject(i).getJSONObject("comments")
													.getJSONArray("data").getJSONObject(j).getJSONObject("replies")
													.getJSONArray("data").getJSONObject(k).getString("username")
											+ "-" + igId);
									author.put("name",
											allMedia.getJSONArray("data").getJSONObject(i).getJSONObject("comments")
													.getJSONArray("data").getJSONObject(j).getJSONObject("replies")
													.getJSONArray("data").getJSONObject(k).getString("username"));
									extObj = new HashMap<>();
									extObj.put("parent_id", "cif-comment-"
											+ allMedia.getJSONArray("data").getJSONObject(i).getJSONObject("comments")
													.getJSONArray("data").getJSONObject(j).getString("id")
											+ "-" + igId);
									extObj.put("external_id", "cif-comment-"
											+ allMedia.getJSONArray("data").getJSONObject(i).getJSONObject("comments")
													.getJSONArray("data").getJSONObject(j).getJSONObject("replies")
													.getJSONArray("data").getJSONObject(k).getString("id")
											+ "-" + igId);
									extObj.put("message",
											allMedia.getJSONArray("data").getJSONObject(i).getJSONObject("comments")
													.getJSONArray("data").getJSONObject(j).getJSONObject("replies")
													.getJSONArray("data").getJSONObject(k).getString("text"));
									extObj.put("created_at",
											allMedia.getJSONArray("data").getJSONObject(i).getJSONObject("comments")
													.getJSONArray("data").getJSONObject(j).getJSONObject("replies")
													.getJSONArray("data").getJSONObject(k).getString("timestamp")
													.replace("+0000", "Z"));
									extObj.put("author", author);
									extObj.put("allow_channelback", true);
									extResource.add(extObj);
								}
							}
						}
					}
				}
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		HashMap<String, Object> response = new HashMap<>();
		response.put("external_resources", extResource);
		return new ResponseEntity<Object>(response, HttpStatus.OK);
	}

	/* FIXME CHANNELBACK */
	@RequestMapping("/channelback")
	public ResponseEntity<String> channelback(@RequestParam Map<String, String> paramMap) {
		System.out.println("/channelback");
		// System.out.println(paramMap.get("message"));
		// System.out.println(paramMap.get("parent_id"));
		// System.out.println(paramMap.get("recipient_id"));
		System.out.println(paramMap.get("thread_id"));

		/* GET COMMENT ID */
		String commentId = paramMap.get("thread_id").split("-")[2];
		String message = paramMap.get("message").toString();

		System.out.println(commentId);

		Calling call = new Calling();
		Entity ent = new Entity();

		return new ResponseEntity<String>("", HttpStatus.OK);
	}

	@RequestMapping("/saveclient")
	public ResponseEntity<String> saveClient(@RequestBody Map<String, String> parameter) {
		System.out.println("/saveclient");
		System.out.println(parameter);
		return new ResponseEntity<String>(HttpStatus.OK);
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
