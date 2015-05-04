package com.Nadeem.Utils;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

@SuppressLint("ValidFragment")
public class CommonRequestFragment extends AsyncTask<String, String, String> {

	private String url;
	private String key;
	Activity context;

	private String progressTitle;

	private ProgressDialog dialog;
	private MyFragment fragment;
	private Fragment fragmentContext;

	public CommonRequestFragment(Activity ctx) {
		super();
		this.context = (Activity) ctx;
	}

	public void closeDialog() {
		if (null != dialog) {
			dialog.dismiss();
		}
	}

	@Override
	protected void onCancelled() {
		super.onCancelled();
		closeDialog();
	}

	@Override
	protected String doInBackground(String... arg0) {
		String _url = url;

		Log.e("do In Background url ", _url);

		DefaultHttpClient client = new DefaultHttpClient();
		String responseString = null;
		try {
			HttpGet get = new HttpGet(_url);
			HttpResponse response = client.execute(get);
			HttpEntity entity = response.getEntity();
			responseString = EntityUtils.toString(entity);
		} catch (ClientProtocolException e) {
			Log.e("CommonRequest ClientProtocolException => ", e.getMessage());

			// Toast.makeText(fragmentContext,"Please make sure your interent is working before proceeding",Toast.LENGTH_LONG).show();
			return ("error");
		} catch (IOException e) {
			Log.e("CommonRequest IOException => ", e.getMessage());
			// Toast.makeText(fragmentContext,"Please make sure your interent is working before proceeding",Toast.LENGTH_LONG).show();
			return ("error");
		}
		return responseString;

	}

	@Override
	protected void onPreExecute() {
		if (StringUtils.isNetworkConnected(context)) {
			if (null != fragment) {
				if (null != progressTitle) {
					dialog = new ProgressDialog(context);
					String title = progressTitle;
					if (title.trim().length() < 1) {
						title = "Please wait for few seconds...";
					}
					dialog.setMessage(title);

					dialog.setCanceledOnTouchOutside(false);

					try {
						dialog.show();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		} else {
			Toast.makeText(
					context,
					"No Internet connection found, please connect to Internet and try again.",
					Toast.LENGTH_LONG).show();

			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					// Intent i = new Intent(context, NewSuperMainScreen.class);
					// context.startActivity(i);
					context.finish();
				}
			}, 150);
		}
	}

	@Override
	protected void onPostExecute(String result) {
		closeDialog();
		if (result.contentEquals("error")) {

			// Toast.makeText(context, R.string.toast_no_internet,
			// Toast.LENGTH_LONG).show();
		} else {
			if (!isCancelled()) {
				if (null != fragment && null != result) {
					fragment.onTaskComplete(result, key);

				}
			}
		}
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public void setProgressTitle(String progressTitle) {
		this.progressTitle = progressTitle;
	}

	public void setActivity(MyFragment myFragment) {
		this.fragment = myFragment;
	}

}
