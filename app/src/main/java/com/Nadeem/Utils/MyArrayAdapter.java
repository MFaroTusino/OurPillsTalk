package com.Nadeem.Utils;

import java.util.List;

import android.app.Activity;
import android.content.SharedPreferences;
import android.widget.ArrayAdapter;
import android.widget.Toast;

public abstract class MyArrayAdapter extends ArrayAdapter<Object> {

	Activity context;
	CommonRequestAdapter commonRequest;
	static SharedPreferences myPrefSpot;
	static SharedPreferences.Editor myEditSpot;

	public MyArrayAdapter(Activity context, int resource,
			int textViewResourceId, List objects) {
		// TODO Auto-generated constructor stub
		super(context, resource, textViewResourceId, objects);
		init(context);
	}

	public MyArrayAdapter(Activity context, int resource,
			int textViewResourceId, Object[] objects) {
		// TODO Auto-generated constructor stub
		super(context, resource, textViewResourceId, objects);
		init(context);
	}

	private void init(Activity context) {
		// TODO Auto-generated method stub
		this.context = context;
	}

	public void hitUrl(String key, String url, String progressTitle) {
		commonRequest = new CommonRequestAdapter(context, this);
		commonRequest.setActivity(context);
		commonRequest.setUrl(url);
		commonRequest.setKey(key);
		commonRequest.setProgressTitle(progressTitle);
		commonRequest.execute("");
	}

	public static void writeString(String key, String value) {
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

	public static int getInt(String key) {
		return myPrefSpot.getInt(key, 0);
	}

	public static Boolean getBoolean(String key) {
		return myPrefSpot.getBoolean(key, false);
	}

	public void toast(String message) {
		Toast.makeText(context, message, Toast.LENGTH_LONG).show();
	}

	public void toast(int message) {
		Toast.makeText(context, message, Toast.LENGTH_LONG).show();
	}

	public abstract void onTaskComplete(String result, String key);

}
