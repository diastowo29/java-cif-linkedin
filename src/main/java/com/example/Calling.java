package com.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

import org.json.JSONException;
import org.json.JSONObject;

public class Calling {
	public JSONObject callingGet(String newUrl) {
		JSONObject response = new JSONObject();
		try {
			System.out.println("CALLING GET: " + newUrl);
			URL url = new URL(newUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");

			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
			}

			System.out.println("Output from Server .... \n");
			BufferedReader rd = new BufferedReader(
					new InputStreamReader(conn.getInputStream(), Charset.forName("UTF-8")));
			String jsonText = readUser(rd);
			if (conn.getResponseCode() != 200) {
				response = null;
			} else {
				response = new JSONObject(jsonText);	
			}

			conn.disconnect();

		} catch (MalformedURLException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();

		} catch (JSONException e) {

			e.printStackTrace();

		}

		return response;
	}

	private static String readUser(Reader rd) throws IOException {
		StringBuilder sb = new StringBuilder();
		int cp;
		while ((cp = rd.read()) != -1) {
			sb.append((char) cp);
		}
		return sb.toString();
	}
}
