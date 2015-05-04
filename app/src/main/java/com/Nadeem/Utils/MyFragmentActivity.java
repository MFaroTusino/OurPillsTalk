package com.Nadeem.Utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.regex.Pattern;

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
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public abstract class MyFragmentActivity extends FragmentActivity {

	Context context;

	protected boolean isUserLoggedIn = false;
	protected boolean isUserFromFb = false;

	// For GPS
	LocationManager lm;
	Location location;

	protected boolean allowSynch = true;

	private CommonRequestFragmentActivity commonRequest;

	static SharedPreferences myPrefSpot;
	static SharedPreferences.Editor myEditSpot;

	// Loggin Store
	public static final String KEY_LOGIN_EMAIL = "login_email";
	public static final String KEY_LOGIN_PASSWORD = "login_password";
	public static final String KEY_LOGIN_URL = "login_url";
	public static final String KEY_LOGIN_STATUS = "login_status";
	public static final String KEY_LANGUAGE = "language_selected";
	public static final String KEY_CHECKBOX = "chk";

	public static String currentLanguage = "";

	int framelayoutID = 0;

	public int getFramelayoutID() {
		return framelayoutID;
	}

	public void setFramelayoutID(int framelayoutID) {
		this.framelayoutID = framelayoutID;
	}

	protected void loadFragment(Fragment f) {
		if (f != null && getFramelayoutID() != 0 || getFramelayoutID() < 0) {
			FragmentManager fragmentManager = getSupportFragmentManager();
			fragmentManager.beginTransaction().replace(getFramelayoutID(), f)
					.commit();
		} else {
			log("Load Fragment - MyFragAct",
					"Either Fragment is null or FramelayoutID is null");
		}
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

	protected void loadFragment(Fragment f, int framelayout_id) {
		if (framelayout_id == 0 || framelayout_id < 0) {
			loadFragment(f);
		} else {
			if (f != null) {
				FragmentManager fragmentManager = getSupportFragmentManager();
				fragmentManager.beginTransaction().replace(framelayout_id, f)
						.commit();
			}
		}
	}

	public MyFragmentActivity() {
		super();
		context = this;
	}

	protected void hitUrl(String key, String url) {
		hitUrl(key, url, "");
	}
 
	public void hitUrl(String key, String url, String progressTitle) {
		commonRequest = new CommonRequestFragmentActivity(context);
		commonRequest.setActivity(this);
		commonRequest.setUrl(url);
		commonRequest.setKey(key);
		commonRequest.setProgressTitle(progressTitle);
		commonRequest.execute("");
	}

	public abstract void onTaskComplete(String result, String key);

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		myPrefSpot = getSharedPreferences(StringUtils.PREFS_NAME, MODE_PRIVATE);
		myEditSpot = myPrefSpot.edit();
		// setSharedPreferences(getSharedPreferences(StringUtils.PREFS_NAME,0));
		getPrefs();
		super.onCreate(savedInstanceState);
	}

	public static String getKeyLoginEmail() {
		return getString(KEY_LOGIN_EMAIL);
	}

	public static String getKeyLoginPassword() {
		return getString(KEY_LOGIN_PASSWORD);
	}

	public static void writeString(String key, String value) {
		myEditSpot.putString(key, value);
		myEditSpot.commit();
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
			OnClickListener onClickListener) {
		warning(title, message, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
			}
		});
	}

	protected void warning(String title, String message,
			DialogInterface.OnClickListener oKListener) {
		new AlertDialog.Builder(this)
				.setIcon(android.R.drawable.ic_dialog_alert).setTitle(title)
				.setMessage(message).setPositiveButton("OK", oKListener).show();
	}

	public void toast(String message) {
		Toast.makeText(this, message, Toast.LENGTH_LONG).show();
	}

	public void toast(int message) {
		Toast.makeText(this, message, Toast.LENGTH_LONG).show();
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

	public EditText setMyGravity1(EditText edt, String hint) {
		EditText temp = edt;
		temp.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
		temp.setHint(hint);

		return temp;
	}

	public void setEditTextGravity(EditText edt, String hint) {
		edt.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
		edt.setHint(hint);
	}

	public void setEditTextGravity(EditText edt, int hint) {
		edt.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
		edt.setHint(getString(hint));
	}

	public void setTextViewGravity(TextView tv, int text) {
		tv.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
		tv.setText(getString(text));
	}

	public String getText(EditText edt) {
		String temp = "";

		temp = edt.getText().toString();
		if (!TextUtils.isEmpty(temp.trim())) {
			return temp;
		} else {
			edt.setError("Empty");
			return "";
		}
	}

	public String getText(Spinner sp) {
		String temp = "";

		temp = sp.getSelectedItem().toString();
		if (!TextUtils.isEmpty(temp.trim())) {
			return temp;
		} else {
			return "";
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

	public String getSessionString() {
		return getString(StringUtils.PREF_LOGIN_SESSION);
	}

	protected void onSessionExpire(final int currentTab) {
		if (currentLanguage.equals(StringUtils.LANGUAGE_ARABIC))
			Toast.makeText(
					getApplicationContext(),
					"وقد انتهت جلسة العمل الخاصة بك، الرجاء تسجيل الدخول مرة أخرى.",
					Toast.LENGTH_LONG).show();
		else {
			Toast.makeText(getApplicationContext(),
					"Your session has expired, Please login again.",
					Toast.LENGTH_LONG).show();
		}

		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				// Intent i = new Intent(getApplicationContext(),
				// LoginScreen.class);
				// i.putExtra("tab_load", currentTab);
				// startActivity(i);
				// finish();
			}
		}, Toast.LENGTH_LONG);
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

	protected boolean isUerLoggedIn() {
		// TODO Auto-generated method stub
		String isLoggedIn = getString2(context, StringUtils.PREF_IS_LOGIN);

		if (isLoggedIn.equals("") || isLoggedIn.equals(null))
			return false;
		else
			return true;
	}

}
