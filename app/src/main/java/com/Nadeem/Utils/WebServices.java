package com.Nadeem.Utils;

public class WebServices {
	public static String SEARCH_TEXT = ":text:";
	public static String URL_TEXT = ":URL:";
	String SEARCH_LIMIT = ":result_limit:";
	public static String RESULT_LIMIT = "20";

	public static final String CLIENT_ID = "f11622e7313a6b068b6a9f64f10d8d3a";
	public static final String CLIENT_SECRET = "d25e7a0ca3370a4b3ec33e57d2009bcc";

	// ////// Sound Cloud Search ///////////
	public static final String SEARCH_URL = "http://api.soundcloud.com/tracks.json?client_id="
			+ CLIENT_ID + "&q=:text:&limit=" + RESULT_LIMIT;

	public static final String DIRECT_RESOLVE_URL = "https://api.sndcdn.com/resolve?url=:URL:&_status_code_map%5B302%5D=200&_status_format=json&client_id="
			+ CLIENT_ID;

	public static final String BASE_URL = "http://axupdates.com/projects/";

	/*
	 * WebServices Inner Links
	 */

	// Sign Up
	public static final String SIGN_UP_EMAIL_URL = BASE_URL
			+ "Sounddown/index.php/api/signup?name=:name:&age=:age:&gender=:male:&image_url=:image_url:&email=:email:&password=:password:";
	public static final String SIGN_UP_FB_URL = BASE_URL
			+ "Sounddown/index.php/api/signup?name=:name:&age=:age:&gender=:male:&image_url=:image_url:&email=:email:&fb_id=:fb_id:";

	// Sign IN
	public static final String SIGN_IN_FB_URL = BASE_URL
			+ "Sounddown/index.php/api/login?fb_id=:fb_id:";
	public static final String SIGN_IN_EMAIL_URL = BASE_URL
			+ "Sounddown/index.php/api/login?email=:email:&password=:password:";

	// Forget Password
	public static final String FORGET_PASSWORD_URL = BASE_URL
			+ "Sounddown/index.php/api/login?fb_id=:fb_id:";

	// UPDATE Password
	public static final String UPDATE_PASSWORD_URL = BASE_URL
			+ "Sounddown/index.php/api/login?fb_id=:fb_id:";

}
