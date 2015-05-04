package com.Nadeem.Utils;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View.OnClickListener;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.regex.Pattern;
public abstract class MyActivity extends ActionBarActivity {

	Context context;

	// For GPS
	LocationManager lm;
	Location location;

	boolean isUserLoggedIn = false;
	boolean isUserFromFb = false;

	protected boolean allowSynch = true;

	private CommonRequest commonRequest;

	static SharedPreferences myPrefSpot;
	static SharedPreferences.Editor myEditSpot;

	// Loggin Store
	public static final String KEY_LOGIN_EMAIL = "login_email";
	public static final String KEY_LOGIN_PASSWORD = "login_password";
	public static final String KEY_LOGIN_URL = "login_url";
	public static final String KEY_LOGIN_STATUS = "login_status";
	public static final String KEY_LANGUAGE = "language_selected";
	public static final String KEY_CHECKBOX = "chk";

	protected static final int NAME_LENGTH = 3;
	protected static final int PASSWORD_LENGTH = 8;



    String currentLanguage = "";

	public MyActivity() {
		super();
		context = this;
	}

	protected void hitUrl(String key, String url) {
		hitUrl(key, url, "");
	}

	protected void hitUrl(String key, String url, String progressTitle) {
		commonRequest = new CommonRequest(context);
		commonRequest.setActivity(this);
		commonRequest.setUrl(url);
		commonRequest.setKey(key);
		commonRequest.setProgressTitle(progressTitle);
		commonRequest.execute("");
	}

	public static void writeString2(Context ctx, String key, String value) {
		SharedPreferences myPrefSpot = ctx.getSharedPreferences(
				StringUtils.PREFS_NAME, MODE_PRIVATE);
		SharedPreferences.Editor myEditSpot = myPrefSpot.edit();
		myEditSpot.putString(key, value);
		myEditSpot.commit();
	}

	public static void writeInt(String key, int value) {
		myEditSpot.putInt(key, value);
		myEditSpot.commit();
	}

	public static void writeBoolean(String key, Boolean value) {
		myEditSpot.putBoolean(key, value);
		myEditSpot.commit();
	}

	public static String getString(String key) {
		return myPrefSpot.getString(key, "");
	}

	public static String getString2(Context ctx, String key) {
		SharedPreferences myPrefSpot = ctx.getSharedPreferences(
				StringUtils.PREFS_NAME, MODE_PRIVATE);
		return myPrefSpot.getString(key, "");
	}

	public static int getInt(String key) {
		return myPrefSpot.getInt(key, 0);
	}

	public static Boolean getBoolean(String key) {
		return myPrefSpot.getBoolean(key, false);
	}

	public abstract void onTaskComplete(String result, String key);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		myPrefSpot = getSharedPreferences(StringUtils.PREFS_NAME, MODE_PRIVATE);
		myEditSpot = myPrefSpot.edit();

		getPrefs();
	}

	private void getPrefs() {
		// TODO Auto-generated method stub
		String isLoggedIn = getString(StringUtils.PREF_USER_TYPE);

		log("isLoggedIn", "" + isLoggedIn);

		if (isLoggedIn != null) {
			if (isLoggedIn.equalsIgnoreCase(StringUtils.USER_TYPE_EMAIL)) {
				isUserFromFb = false;
				isUserLoggedIn = true;
			} else if (isLoggedIn.equalsIgnoreCase(StringUtils.USER_TYPE_FB)) {
				isUserFromFb = true;
				isUserLoggedIn = true;
			} else {
				isUserLoggedIn = false;
			}
		} else {
			isUserLoggedIn = false;
		}
	}

	public static void writeString(String key, String value) {
		myEditSpot.putString(key, value);
		myEditSpot.commit();
	}

	@Override
	protected void onPause() {

		allowSynch = false;
		if (null != commonRequest) {
			try {
				commonRequest.cancel(true);
			} catch (Exception e) {
			}
		}
		super.onPause();
	}

	protected void confirmation(String title, String message,
			DialogInterface.OnClickListener oKListener,
			DialogInterface.OnClickListener cancelListener) {
		new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_menu_help)
				.setTitle(title).setMessage(message)
				.setPositiveButton("OK", oKListener)
				.setNegativeButton("Cancel", cancelListener).show();
	}

	protected void confirmation(String title, String message,
			DialogInterface.OnClickListener oKListener) {
		confirmation(title, message, oKListener, null);
	}

	protected void warning(String title, String message,
			OnClickListener onClickListener) {

		warning(title, message, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
			}
		});
	}

	protected void warning(String title, String message) {
		new AlertDialog.Builder(this)
				.setIcon(android.R.drawable.ic_dialog_alert).setTitle(title)
				.setMessage(message)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub

					}
				}).show();
	}

	protected void warning(String title, String message,
			DialogInterface.OnClickListener oKListener) {
		new AlertDialog.Builder(this)
				.setIcon(android.R.drawable.ic_dialog_alert).setTitle(title)
				.setMessage(message).setPositiveButton("OK", oKListener).show();
	}

	public void toast(String message) {
		Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
	}

	public void toast(int message) {
		Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
	}

	@SuppressLint("NewApi")
	public boolean isEmail(String email) {

		Pattern pattern = Patterns.EMAIL_ADDRESS;
		return pattern.matcher(email).matches();

	}

	@SuppressLint("NewApi")
	public boolean isPhone(String phone) {

		Pattern pattern = Patterns.PHONE;
		return pattern.matcher(phone).matches();

	}

	public boolean isAge(String str_Age) {
		int age = Integer.parseInt(str_Age);
		if (age > 1 && age < 150) {
			return true;
		} else
			return false;
	}

	public boolean isName(String str_Name) {
		if (!isEmpty(str_Name)) {
			// Checking names limit
			if (str_Name.length() >= NAME_LENGTH) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	public boolean isPasswordMatching(String pass1, String pass2,
			boolean checkPasswordLimit) {

		if (checkPasswordLimit) {
			if (pass1.length() >= PASSWORD_LENGTH
					&& pass2.length() >= PASSWORD_LENGTH) {
				return isPasswordMatching(pass1, pass2);
			} else {
				return false;
			}
		} else {
			return isPasswordMatching(pass1, pass2);
		}
	}

	public boolean isPasswordMatching(String pass1, String pass2) {
		if (!isEmpty(pass1) && !isEmpty(pass2)) {
			if (pass1.equals(pass2)) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	public void showSettingsAlert() {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

		alertDialog.setCancelable(false);
		alertDialog.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface arg0) {

				// finish();

			}
		});
		// Setting Dialog Title
		alertDialog.setTitle("Location");

		// Setting Dialog Message
		alertDialog
				.setMessage("Unable find your location. Do you want to go to settings menu?");

		// On pressing Settings button
		alertDialog.setPositiveButton("Settings",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						finish();
						Intent intent = new Intent(
								Settings.ACTION_LOCATION_SOURCE_SETTINGS);
						context.startActivity(intent);
					}
				});

		// on pressing cancel button
		alertDialog.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						finish();
						dialog.cancel();
					}
				});

		// Showing Alert Message
		alertDialog.show();
	}

	public static boolean isSDCardAvailable() {
		Boolean isSDPresent = android.os.Environment.getExternalStorageState()
				.equals(android.os.Environment.MEDIA_MOUNTED);

		return isSDPresent;
	}

	public void log(int text) {
		log("" + text);
	}

	public void log(long text) {
		log("" + text);
	}

	public void log(double text) {
		log("" + text);
	}

	public void log(float text) {
		log("" + text);
	}

	public void log(String text) {
		log("" + getApplicationContext().getClass().getSimpleName(), "" + text);
	}

	public void log(int key, String text) {
		log("" + key, "" + text);
	}

	public void log(long key, int text) {
		log("" + key, "" + text);
	}

	public void log(float key, int text) {
		log("" + key, "" + text);
	}

	public void log(double key, int text) {
		log("" + key, "" + text);
	}

	public void log(String key, int text) {
		log("" + key, "" + text);
	}

	public void log(String key, long text) {
		log("" + key, "" + text);
	}

	public void log(String key, double text) {
		log("" + key, "" + text);
	}

	public void log(String key, float text) {
		log("" + key, "" + text);
	}

	public void log(String key, String text) {
		Log.e(key, text);
	}

	public void startMyActivity(Class<?> classToLoad) {
		startMyActivity(classToLoad, false);
	}

	public void startMyActivity(Class<?> classToLoad, boolean finishCurrentAct) {
		Intent i = new Intent(getApplicationContext(), classToLoad);
		startActivity(i);
		if (finishCurrentAct) {
			finish();
		}
	}

	public void startMyActivity(Class<?> classToLoad, Bundle b) {

		if (b != null) {
			startMyActivity(classToLoad, b, false);
		} else {
			startMyActivity(classToLoad, false);
		}
	}

	public void startMyActivity(Class<?> classToLoad, Bundle b,
			boolean finishCurrentAct) {

		if (b != null) {
			Intent i = new Intent(getApplicationContext(), classToLoad);
			i.putExtras(b);
			startActivity(i);
			if (finishCurrentAct) {
				finish();
			}
		} else {
			startMyActivity(classToLoad, finishCurrentAct);
		}
	}

	public boolean isEmpty(String str) {
		boolean temp = true;

		if (TextUtils.isEmpty(str.trim())) {
			temp = true;
		} else {
			temp = false;
		}
		return temp;
	}

	public String replace(String str, String old, String newChar) {
		if (!isEmpty(old) && !isEmpty(newChar)) {
			try {
				return str.replace(old, URLEncoder.encode(newChar, "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return "";
			}
		} else
			return "";
	}

	protected boolean isUserLoggedIn() {
		// TODO Auto-generated method stub
		String isLoggedIn = getString2(context, StringUtils.PREF_IS_LOGIN);

		if (isLoggedIn.equals("") || isLoggedIn.equals(null))
			return false;
		else
			return true;
	}

}
