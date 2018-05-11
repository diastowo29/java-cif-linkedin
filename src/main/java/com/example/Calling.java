package com.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Calling {
	public String callingGet(String newUrl) {
		String response = "";
		try {
			System.out.println("CALLING GET: " + newUrl);
			URL url = new URL(newUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");

			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
			String output = "";
			System.out.println("Output from Server .... \n");
			while ((output = br.readLine()) != null) {
				response = output;
				System.out.println("2: " + response);
			}
			System.out.println("3: " + response);

			conn.disconnect();

		} catch (MalformedURLException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();

		}

		System.out.println("4: " + response);
		return response;
	}
}
