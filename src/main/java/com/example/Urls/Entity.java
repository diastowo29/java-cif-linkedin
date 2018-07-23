package com.example.Urls;

public class Entity {
	// watsons
	// String clientId = "186865125399490";
	// String clientSecret = "8ab339714df67aa953c7842d193c470f";

	// gw
	public String CLIENT_ID = "376575612769500";
	public String CLIENT_SECRET = "c95fc0a354beb66dc9bb490e85762ec3";

	public String HEROKUDOMAIN = "https://java-cif-linkedin.herokuapp.com/";

	public String CALLBACKURL = "https://java-cif-linkedin.herokuapp.com/instagram/callback";

	public String FB_API_DOMAIN = "https://graph.facebook.com/v3.0";
	public String GET_ACC_TOKEN_API = FB_API_DOMAIN + "/oauth/access_token?client_id=" + CLIENT_ID + "&redirect_uri="
			+ CALLBACKURL + "&client_secret=" + CLIENT_SECRET + "&code=";
	public String GET_ACC_ID_API = FB_API_DOMAIN + "/me/accounts?fields=connected_instagram_account,name&access_token=";

	public String GetMediaUrl(String accId, String token) {
		String mediaApi = accId + "?fields=media{caption,comments{text,user,username}}&access_token=" + token;
		return mediaApi;
	}
}
