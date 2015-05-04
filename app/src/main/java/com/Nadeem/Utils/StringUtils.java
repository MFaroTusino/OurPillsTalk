package com.Nadeem.Utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.util.Log;
import android.widget.TextView;

@SuppressLint("SimpleDateFormat")
public class StringUtils {

	public final static String USER_TYPE_FB = "user_type_fb";
	public final static String USER_TYPE_EMAIL = "user_type_email";

	public final static String DEFAULT_PIC_URL = "http://enterfind.files.wordpress.com/2013/05/windows8_account_picture.png";
	public final static String PREF_LAST_EMAIL = "last_email";

	public static long VIBRATE_TIME = 300;
	public static String PREFS_NAME = "pref_SoundDown";

	public final static int EQUALIZER_PRIORITY = 5;

	public final static String TWITTER_PUBLIC_KEY = "MftC4aqX2KY0ryfxaNXn2woYS";
	public final static String TWITTER_SECERET_KEY = "hilyt7eN61FqBvcmXUwZx3PO28cEA635iZxBFVYmXVRJ42QNrD";

	public final static String FB_APP_ID = "561630287222300";
	public final static String FB_SECERET_KEY = "ca2c0ade18b772a2c59637cef1cafe23";

	// Setting Prefs
	public static final String PREF_SETTINGS_FB_FIRST_NAME = "user_settings_fb_fname";
	public static final String PREF_SETTINGS_FB_LAST_NAME = "user_settings_fb_lastname";
	public static final String PREF_SETTINGS_FB_PIC_URL = "user_settings_fb_pic_url";
	public static final String PREF_SETTINGS_FB_EMAIL = "user_settings_fb_email";
	public static final String PREF_SETTINGS_FB_USER_ID = "user_settings_fb_id";
	public static final String PREF_SETTINGS_FB_ACCESS_TOKEN = "user_settings_fb_access_token";

	public static final String PREF_SETTINGS_TWITTER_NAME = "user_settings_twitter_name";
	public static final String PREF_SETTINGS_TWITTER_ACCESS_TOKEN = "user_settings_twitter_access_token";

	// LOGIN PREFS NAME
	public static final String PREF_USER_USER_ID = "user_id_in_app";
	public static final String PREF_USER_TYPE = "user_type";
	public static final String PREF_USER_NAME = "user_lastname";
	public static final String PREF_USER_PIC_URL = "user_pic_url";
	public static final String PREF_USER_EMAIL = "user_name";
	public static final String PREF_USER_GENDER = "user_gender";
	public static final String PREF_USER_FB_ID = "user_fb_id";

	// Other Prefs
	public static final String PREF_LOGIN_ID = "id";
	public static final String PREF_LOGIN_NAME = "name";
	public static final String PREF_LOGIN_USER = "user";
	public static final String PREF_LOGIN_SESSION = "session";

	public static final String PREF_CUSTOM_EQ_CHKBOX = "chkbox_custom";
	public static final String PREF_EQ_LAST_SELECTED_NAME = "last_name";

	public static final String PREF_SETTINGS_FB = "fb_enable";
	public static final String PREF_SETTINGS_TWITTER = "twitter_enable";
	public static final String PREF_SETTINGS_AUTOSHARE_FB = "autoshare_to_Facebook";
	public static final String PREF_SETTINGS_AUTOSHARE_TWITTER = "autoshare_to_Twitter";

	public static final String PREF_SETTINGS_AUTOSHARE_INCLUDE_LOCATION = "include_location";
	public static final String PREF_SETTINGS_DEFAULT_SHARE_TEXT = "defaultsharetext";
	public static final String PREF_SETTINGS_EMAIL_FAV = "email_Favorites";
	public static final String PREF_SETTINGS_CLEAR_SEARCH_HISTORY = "clear_search_history";
	public static final String PREF_SETTINGS_CLEAR_FAVS = "clear_Favorites";
	public static final String PREF_SETTINGS_VIBRATE_ON_RESULT = "vibrate";
	public static final String PREF_SETTINGS_PRE_LISTENING = "pre_listening";
	public static final String PREF_SETTINGS_SAVE_SEARCH_LOCATION = "save_search_location";

	// ////
	public static final String PREF_IS_LOGIN = "is_user_logged_in";

	public static final String PREF_PROPOSAL_ID = "proposal_id";

	public static final float bg_alpha = 0.1f;
	// Themes
	public static final String THEME_SEAGREEN = "_1";
	public static final String THEME_BLUE = "_2";
	public static final String THEME_RED = "_5";

	public static final List<String> THEMES_LIST = new ArrayList<String>();

	public static final String CURRENT_THEME = THEME_RED;
	// others

	public static final String LANGUAGE_ENGLISH = "English";
	public static final String LANGUAGE_ARABIC = "Arabic";

	public static String CHK_DONOTSHOW = "true";
	public static String CHK_SHOW = "false";

	public static final String FONT_TIMES = "times.ttf";
	public static final String FONT_ARIAL = "arial.ttf";
	public static final String FONT_TIMES_BOLD = "times_bold.ttf";
	public static final String FONT_ARIAL_BOLD = "arial_bold.ttf";

	// Application Types
	public static final String APPLICATIONTYPE_FULL = "FULL";
	public static final String APPLICATIONTYPE_FREE = "FREE";
	// Theme Types
	public static final String THEMETYPE_GRAY = "GRAY";
	public static final String THEMETYPE_BLUE = "BLUE";

	// Package Names of Applications
	public static final String PACKAGENAME_FULL_VERSION = "com.moe.moe_suppliers";
	// public static final String PACKAGENAME_FREE_VERSION="";

	// Developer Log Strings
	public static final String LOG_MODE_ALL = "LOG_ALL"; // all on
	public static final String LOG_MODE_OFF = "LOG_OFF"; // all off
	public static final String LOG_MODE_MAJOR = "LOG_MAJOR";

	public static String SHARE_TEXT = "https://play.google.com/store/apps/details?id="
			+ PACKAGENAME_FULL_VERSION;

	private static boolean isConnected = false;
	public static String Download_Path_SD = Environment
			.getExternalStorageDirectory().getAbsolutePath() + "/MOE";

	public final static boolean isSDCardAvailable() {
		return Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
	}

	public final static boolean isValidEmail(CharSequence target) {
		if (target == null) {
			return false;
		} else {
			return android.util.Patterns.EMAIL_ADDRESS.matcher(target)
					.matches();
		}
	}

	public static String EncodeString(String ToEncode) {

		try {
			ToEncode = URLEncoder.encode(ToEncode, "utf-8");

		} catch (UnsupportedEncodingException e) {
			Log.e("Encode Url String UnsupportedEncodingException Error:",
					e.getMessage());
		}

		return ToEncode;
	}

	public static String md5(String s) {
		try {
			// Create MD5 Hash
			MessageDigest digest = MessageDigest.getInstance("MD5");
			digest.update(s.getBytes());
			byte messageDigest[] = digest.digest();

			// Create Hex String
			StringBuffer hexString = new StringBuffer();
			for (int i = 0; i < messageDigest.length; i++) {
				hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
			}
			return hexString.toString();

		} catch (NoSuchAlgorithmException e) {
			Log.e("SpotMe Error", e.getMessage());
		}
		return "";
	}

	public static String des(String textToBeEncrypted, String deskey) {
		try {

			// declare Cipher
			Cipher desCipher;

			// Create the cipher
			desCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");

			// Initialize the cipher for encryption
			// desCipher.init(Cipher.ENCRYPT_MODE, deskey);

			// sensitive information
			byte[] text = "No body can see me".getBytes();

			System.out.println("Text [Byte Format] : " + text);
			System.out.println("Text : " + new String(text));

			// Encrypt the text
			byte[] textEncrypted = desCipher.doFinal(text);
			Log.e("textEncrypted", "" + textEncrypted);

			return "" + textEncrypted;

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}

		return "";

	}

	// public static String decrypt(String str) {
	//
	// try {
	//
	// // decode with base64 to get bytes
	// byte[] dec = BASE64DecoderStream.decode(str.getBytes());
	// byte[] utf8 = dcipher.doFinal(dec);
	//
	// // create new string based on the specified charset
	// return new String(utf8, "UTF8");
	//
	// }
	//
	// catch (Exception e) {
	// e.printStackTrace();
	// }
	//
	// return null;
	//
	// }

	public static String getFormatDateFacebook(String Date, String formatStyle) {
		String formatteddate = "";

		try {
			Date date = new SimpleDateFormat("dd/mm/yyyy").parse(Date);
			Log.e("DateFB", "" + date);
			DateFormat newFormat = new SimpleDateFormat(formatStyle);
			formatteddate = newFormat.format(date);

		} catch (ParseException e) {
			e.printStackTrace(); // To change body of catch statement use File |
									// Settings | File Templates.
		}

		return formatteddate;

	}

	public static String getFormatDate(String Date, String formatStyle) {
		String formatteddate = "";

		try {
			Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(Date);
			DateFormat newFormat = new SimpleDateFormat(formatStyle);
			formatteddate = newFormat.format(date);

		} catch (ParseException e) {
			e.printStackTrace(); // To change body of catch statement use File |
									// Settings | File Templates.
		}

		return formatteddate;

	}

	public static String getFormatDate1(String Date, String formatStyle) {
		String formatteddate = "";

		try {
			Date date = new SimpleDateFormat("MMM dd, yyyy").parse(Date);
			DateFormat newFormat = new SimpleDateFormat(formatStyle);
			formatteddate = newFormat.format(date);

		} catch (ParseException e) {
			e.printStackTrace(); // To change body of catch statement use File |
									// Settings | File Templates.
		}

		return formatteddate;

	}

	public static String getFormatDate2(String Date, String formatStyle) {
		String formatteddate = "";

		try {
			Date date = new SimpleDateFormat("yyyy-M-dd").parse(Date);
			DateFormat newFormat = new SimpleDateFormat(formatStyle);
			formatteddate = newFormat.format(date);

		} catch (ParseException e) {
			e.printStackTrace(); // To change body of catch statement use File |
									// Settings | File Templates.
		}

		return formatteddate;

	}

	public static String getFormatTime(String Date, String formatStyle) {
		String formatteddate = "";

		try {
			Date date = new SimpleDateFormat("HH:mm:ss").parse(Date);
			DateFormat newFormat = new SimpleDateFormat(formatStyle);
			formatteddate = newFormat.format(date);

		} catch (ParseException e) {
			e.printStackTrace(); // To change body of catch statement use File |
									// Settings | File Templates.
		}

		return formatteddate;

	}

	public static String getFormatDate3(String Date, String formatStyle) {
		String formatteddate = "";

		try {
			Date date = new SimpleDateFormat("dd-mm-yyyy HH:mm:ss").parse(Date);
			DateFormat newFormat = new SimpleDateFormat(formatStyle);
			formatteddate = newFormat.format(date);

		} catch (ParseException e) {
			e.printStackTrace(); // To change body of catch statement use File |
									// Settings | File Templates.
		}

		return formatteddate;

	}

	@SuppressWarnings("unused")
	public static String getAge(String dateOfBirth) {
		String age = "";

		// /For Age Calculation:
		Date nowDate = new Date();

		int yearDOB = Integer.parseInt(dateOfBirth.substring(0, 4));
		Log.e("birth year", String.valueOf(yearDOB));

		int NowYear = Calendar.getInstance().get(Calendar.YEAR);

		int new_age = 0;

		new_age = NowYear - yearDOB;
		Log.e("this year", String.valueOf(NowYear));

		age = String.valueOf(new_age);
		Log.e("new Age", age);

		return age;

	}

	public static int getAge(int _year, int _month, int _day) {

		GregorianCalendar cal = new GregorianCalendar();
		int y, m, d, a;

		y = cal.get(Calendar.YEAR);
		m = cal.get(Calendar.MONTH) + 1;
		d = cal.get(Calendar.DAY_OF_MONTH);
		cal.set(_year, _month, _day);
		a = y - cal.get(Calendar.YEAR);
		if ((m < cal.get(Calendar.MONTH))
				|| ((m == cal.get(Calendar.MONTH)) && (d < cal
						.get(Calendar.DAY_OF_MONTH)))) {
			--a;
		}
		if (a < 0)
			throw new IllegalArgumentException("Age < 0");
		return a;
	}

	@SuppressWarnings("unused")
	public static String getTime(long diff) {

		long diffSeconds = diff / 1000 % 60;
		long diffMinutes = diff / (60 * 1000) % 60;
		long diffHours = diff / (60 * 60 * 1000) % 24;
		long diffDays = diff / (24 * 60 * 60 * 1000);

		String timeDiference = diffHours + ":" + diffMinutes + ":"
				+ diffSeconds + ":";

		return timeDiference;

	}

	public static Typeface setTypeFace(Context ctx, TextView tv, String fontName) {
		Typeface custom_font = Typeface.createFromAsset(ctx.getAssets(),
				fontName);
		return custom_font;
	}

	public static Typeface setTypeFace_Auto(Context ctx, TextView tv) {
		String fontName = StringUtils.FONT_ARIAL;

		Typeface custom_font = Typeface.createFromAsset(ctx.getAssets(),
				fontName);
		return custom_font;
	}

	public static Typeface setTypeFace_Auto(Context ctx, TextView tv,
			boolean isHeading) {
		if (isHeading) {
			String fontName = "";

			fontName = StringUtils.FONT_ARIAL_BOLD;

			Typeface custom_font = Typeface.createFromAsset(ctx.getAssets(),
					fontName);
			return custom_font;
		} else
			return setTypeFace_Auto(ctx, tv);
	}

	public static Drawable getDrawable(Context context, String Name) {
		Name = Name + StringUtils.CURRENT_THEME;
		Log.e("name with theme", "" + Name);

		final int imageResource = context.getResources().getIdentifier(
				"@drawable/" + Name, null, context.getPackageName());
		Drawable image = context.getResources().getDrawable(imageResource);
		return image;
	}

	public static boolean isNetworkConnected(Activity act) {
		ConnectivityManager cm = (ConnectivityManager) act
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		if (ni == null) {
			// There are no active networks.
			return false;
		} else
			return true;
	}

	private static void set_Themes_List() {
		THEMES_LIST.add(THEME_SEAGREEN);
		THEMES_LIST.add(THEME_BLUE);
		THEMES_LIST.add(THEME_RED);
	}

	public static int getDrawable1(Context context, String Name) {
		String originalName = Name;
		Name = Name + StringUtils.CURRENT_THEME;
		Log.e("name with theme", "" + Name);
		int imageResource = 0;
		try {
			imageResource = context.getResources().getIdentifier(
					"@drawable/" + Name, null, context.getPackageName());
		} catch (Exception e) {
			// TODO: handle exception
			set_Themes_List();
			imageResource = getDefaultDrawable(context, originalName,
					originalName + THEMES_LIST.get(0), 0);
		}

		return imageResource;
	}

	private static int getDefaultDrawable(Context context, String originalName,
			String Name, int i) {
		int imageResource = 0;
		try {
			imageResource = context.getResources().getIdentifier(
					"@drawable/" + Name, null, context.getPackageName());
		} catch (Exception e2) {
			// TODO: handle exception
			getDefaultDrawable(context, originalName, originalName
					+ THEMES_LIST.get(i + 1), i + 1);
		}
		return imageResource;
	}

	// public static void setWallpaperBG(View ctx) {
	// boolean showWallpaper = false;
	// ImageView rel_Parent;
	// rel_Parent = (ImageView) ctx.findViewById(R.id.bg);
	// if (DrawerFragmentActivity.showWallpaper) {
	// rel_Parent.setAlpha(StringUtils.bg_alpha);
	// rel_Parent.setBackgroundResource(R.drawable.bg_quicktour_fragment1);
	// } else {
	// rel_Parent.setAlpha(0.0f);
	// rel_Parent.setBackgroundResource(R.drawable.bg_quicktour_fragment1);
	// }
	// }
	//
	// public static void setWallpaperBG(Window window) {
	// // TODO Auto-generated method stub
	// boolean showWallpaper = false;
	// ImageView rel_Parent;
	// rel_Parent = (ImageView) window.findViewById(R.id.bg);
	// if (DrawerFragmentActivity.showWallpaper) {
	// rel_Parent.setAlpha(StringUtils.bg_alpha);
	// rel_Parent.setBackgroundResource(R.drawable.bg_quicktour_fragment1);
	// } else {
	// rel_Parent.setAlpha(0.0f);
	// rel_Parent.setBackgroundResource(R.drawable.bg_quicktour_fragment1);
	// }
	// }

	public static Boolean isNetworkAvailable(final int timeout) {
		// ask fo message '0' (not connected) or '1' (connected) on 'handler'
		// the answer must be send before before within the 'timeout' (in
		// milliseconds)

		new Thread() {
			private boolean responded = false;

			@Override
			public void run() {
				// set 'responded' to TRUE if is able to connect with google
				// mobile (responds fast)
				new Thread() {
					@Override
					public void run() {
						HttpGet requestForTest = new HttpGet(
								"http://m.google.com");
						try {
							new DefaultHttpClient().execute(requestForTest); // can
																				// last...
							responded = true;
						} catch (Exception e) {
						}
					}
				}.start();

				try {
					int waited = 0;
					while (!responded && (waited < timeout)) {
						sleep(100);
						if (!responded) {
							waited += 100;
						}
					}
				} catch (InterruptedException e) {
				} // do nothing
				finally {
					if (!responded) {
						isConnected = false;
					} else {
						isConnected = true;
					}
				}
			}
		}.start();

		return isConnected;
	}

	public static Bitmap getRoundedShape(Bitmap scaleBitmapImage) {
		int targetWidth = 50;
		int targetHeight = 50;
		Bitmap targetBitmap = Bitmap.createBitmap(targetWidth, targetHeight,
				Bitmap.Config.ARGB_8888);

		Canvas canvas = new Canvas(targetBitmap);
		Path path = new Path();
		path.addCircle(((float) targetWidth - 1) / 2,
				((float) targetHeight - 1) / 2,
				(Math.min(((float) targetWidth), ((float) targetHeight)) / 2),
				Path.Direction.CCW);

		canvas.clipPath(path);
		Bitmap sourceBitmap = scaleBitmapImage;
		canvas.drawBitmap(sourceBitmap, new Rect(0, 0, sourceBitmap.getWidth(),
				sourceBitmap.getHeight()), new Rect(0, 0, targetWidth,
				targetHeight), null);
		return targetBitmap;
	}

}
