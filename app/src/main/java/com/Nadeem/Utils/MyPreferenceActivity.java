package com.Nadeem.Utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnCancelListener;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.provider.Settings;
import android.util.Log;
import android.view.View.OnClickListener;
import android.widget.Toast;

public abstract class MyPreferenceActivity extends PreferenceActivity {

	Context context;

	// For GPS
	LocationManager lm;
	Location location;

	protected boolean allowSynch = true;

	private CommonRequest_PreferenceActivity commonRequest;

	static SharedPreferences myPrefSpot;
	static SharedPreferences.Editor myEditSpot;

	// Loggin Store
	public static final String KEY_LOGIN_EMAIL = "login_email";
	public static final String KEY_LOGIN_PASSWORD = "login_password";
	public static final String KEY_LOGIN_URL = "login_url";
	public static final String KEY_LOGIN_STATUS = "login_status";
	public static final String KEY_LANGUAGE = "language_selected";
	public static final String KEY_CHECKBOX = "chk";

	String currentLanguage = "";

	public MyPreferenceActivity() {
		super();
		context = this;
	}

	protected void hitUrl(String key, String url) {
		hitUrl(key, url, "");
	}

	public static void writeBoolean2(Context ctx, String key, boolean value) {
		SharedPreferences myPrefSpot = ctx.getSharedPreferences(
				StringUtils.PREFS_NAME, MODE_PRIVATE);
		SharedPreferences.Editor myEditSpot = myPrefSpot.edit();
		myEditSpot.putBoolean(key, value);
		myEditSpot.commit();
	}

	public static Boolean getBoolean2(Context ctx, String key) {
		SharedPreferences myPrefSpot = ctx.getSharedPreferences(
				StringUtils.PREFS_NAME, MODE_PRIVATE);
		return myPrefSpot.getBoolean(key, false);
	}

	protected void hitUrl(String key, String url, String progressTitle) {
		commonRequest = new CommonRequest_PreferenceActivity(context);
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
		return myPrefSpot.getString(key, null);
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

	protected boolean isUerLoggedIn() {
		// TODO Auto-generated method stub
		String isLoggedIn = getString2(context, StringUtils.PREF_IS_LOGIN);

		if (isLoggedIn.equals("") || isLoggedIn.equals(null))
			return false;
		else
			return true;
	}

}
