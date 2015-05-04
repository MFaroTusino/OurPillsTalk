package au.com.ourpillstalk.ourpillstalk;


import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Elliott on 1/04/15.
 */
public class SharedPreferencesIO {

    static final String PREF_OUR_PILLS_TALK = "PREF_OUR_PILLS_TALK";
    static final String KEY_TOGGLE_SCAN_DUPLICATES = "KEY_TOGGLE_SCAN_DUPLICATES";
    static final String KEY_SCAN_TOTAL = "KEY_SCAN_TOTAL";
    static final String KEY_LANGUAGE_CODE = "KEY_LANGUAGE_CODE";
    static final String KEY_NEW_USER = "KEY_NEW_USER";
    static final String KEY_SHOW_SEARCH = "KEY_SHOW_SEARCH";
    static final String KEY_REFRESH_LANGUAGE = "KEY_REFRESH_LANGUAGE";

    public static void setRefreshLanguage(boolean refresh, Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SharedPreferencesIO.PREF_OUR_PILLS_TALK, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(KEY_REFRESH_LANGUAGE, refresh);
        editor.commit();
    }
    public static boolean getRefreshLanguage(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SharedPreferencesIO.PREF_OUR_PILLS_TALK, Context.MODE_PRIVATE);
        return prefs.getBoolean(KEY_REFRESH_LANGUAGE, false);
    }

    public static void setShowSearch(boolean showSearch, Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SharedPreferencesIO.PREF_OUR_PILLS_TALK, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(KEY_SHOW_SEARCH, showSearch);
        editor.commit();
    }

    public static boolean getShowSearch(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SharedPreferencesIO.PREF_OUR_PILLS_TALK, Context.MODE_PRIVATE);
        return prefs.getBoolean(KEY_SHOW_SEARCH, false);
    }
    public static void setNewUser(boolean newUser, Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SharedPreferencesIO.PREF_OUR_PILLS_TALK, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(KEY_NEW_USER, newUser);
        editor.commit();
    }

    public static boolean getNewUser(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SharedPreferencesIO.PREF_OUR_PILLS_TALK, Context.MODE_PRIVATE);
        return prefs.getBoolean(KEY_NEW_USER, true);
    }
    public static void setToggleDuplicates(boolean toggle, Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SharedPreferencesIO.PREF_OUR_PILLS_TALK, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(SharedPreferencesIO.KEY_TOGGLE_SCAN_DUPLICATES, toggle);
        editor.commit();
    }

    public static boolean getToggleDuplicates(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SharedPreferencesIO.PREF_OUR_PILLS_TALK, Context.MODE_PRIVATE);
        return prefs.getBoolean(SharedPreferencesIO.KEY_TOGGLE_SCAN_DUPLICATES, false);
    }

    public static void setScanTotal(int total, Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SharedPreferencesIO.PREF_OUR_PILLS_TALK, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(SharedPreferencesIO.KEY_SCAN_TOTAL, total);
        editor.commit();
    }

    public static int getScanTotal(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SharedPreferencesIO.PREF_OUR_PILLS_TALK, Context.MODE_PRIVATE);
        return prefs.getInt(SharedPreferencesIO.KEY_SCAN_TOTAL, 0);
    }

    public static void setLanguageCode(String code, Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SharedPreferencesIO.PREF_OUR_PILLS_TALK, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(SharedPreferencesIO.KEY_LANGUAGE_CODE, code);
        editor.commit();
    }

    public static String getLanguageCode(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SharedPreferencesIO.PREF_OUR_PILLS_TALK, Context.MODE_PRIVATE);
        return prefs.getString(SharedPreferencesIO.KEY_LANGUAGE_CODE, "en");
    }






    /*public static boolean saveScanString(Context context, String scanString) {
        ArrayList<String> allScanData = loadAllScanStrings(context);
        if(allScanData.size() <= 0) {
            //check that we are not overwrite all saved scans by a crashed load, try to load 3 times
            for(int i = 0; i < 3 || allScanData.size() != 0; i++) {
                allScanData = loadAllScanStrings(context);
            }
        }
        allScanData.add(scanString);
        SharedPreferences prefs = context.getSharedPreferences(SCAN_HISTORY_PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        for (int i = 0; i < allScanData.size(); i++) {
            editor.putString(SCAN_HISTORY_KEY + "_" + i, allScanData.get(i));
        }

        editor.putInt(SCAN_HISTORY_SIZE_KEY, allScanData.size());

        return editor.commit();
    }

   public static ArrayList<String> loadAllScanStrings(Context context) {

        SharedPreferences prefs = context.getSharedPreferences(SCAN_HISTORY_PREFERENCE_NAME, Context.MODE_PRIVATE);
        ArrayList<String> scanData = new ArrayList<String>();

        if(prefs.getInt(SCAN_HISTORY_SIZE_KEY, 0) == 0) {
            return scanData;
        } else {

            try {
                for (int i = 0; i < prefs.getInt(SCAN_HISTORY_SIZE_KEY, 0); i++) {
                    scanData.add(i, prefs.getString(SCAN_HISTORY_KEY + "_" + i, null));
                }
            } catch(Exception e) {
                //preferences read error
                e.printStackTrace();
                Log.e("Crash Status", "" + e);
            }
            return scanData;
        }
    }*/
}
