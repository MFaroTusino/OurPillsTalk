package au.com.ourpillstalk.ourpillstalk;

import android.app.ProgressDialog;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.memetix.mst.language.Language;
import com.memetix.mst.translate.Translate;

import java.util.ArrayList;
import java.util.Locale;

public class HelpActivity extends AppCompatActivity implements TextToSpeech.OnInitListener,OnTouchListener {
    ImageView back;
    TextView help_text;
    // TextView last_scanT;
    TextToSpeech textToSpeech;
    // String last_scan;
    ImageView cmi;
    ArrayList<Locale> localeList;
    ArrayList<String> langlist_codeS;
    ArrayList<Language> langlist_code;
    public ProgressDialog pDialog;
    String converted;
    Boolean speak_available;
    String langcodeS;
    String langcodeS3;
    String text = "Our Pills Talk is designed to help you manage your prescription medications. This app allows you to scanTextView special QR codes which contain information about your prescribed medicines. To start scanning your prescription simply go to the home screen, select Scan My Pill and hold your device over the QR code. For more accurate readings place your device 3 to 4 inches away and keep your hand steady while the camera focuses and scans. Once you have scanned your prescription, you are able to view your history of all the scans you made. This feature can be accessed by pressing Scan History button on the home screen. You can manage your scans by deleting or rearranging them. This app has been designed to work with voice over feature to assist vision impaired individuals and speak each selected element for improved clarity.";

    final static float STEP = 200;

    float mRatio = 1.0f;
    int mBaseDist;
    float mBaseRatio;
    float fontsize = 13;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Help");
        setContentView(R.layout.activity_help_me);
        back = (ImageView) findViewById(R.id.back);
        help_text = (TextView) findViewById(R.id.textView1);
        textToSpeech = new TextToSpeech(this, this);
        back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                textToSpeech.stop();
                finish();
            }
        });

        help_text.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getPointerCount() == 2) {
                    int action = event.getAction();
                    int pureaction = action & MotionEvent.ACTION_MASK;

                    Log.e("" + action, "" + MotionEvent.ACTION_MASK);
                    Log.e("pureactio", "" + pureaction);

                    if (pureaction == MotionEvent.ACTION_POINTER_DOWN) {
                        mBaseDist = getDistance(event);
                        mBaseRatio = mRatio;
                    } else if (pureaction == MotionEvent.ACTION_POINTER_UP) {

                    } else {
                        float delta = (getDistance(event) - mBaseDist) / STEP;
                        float multi = (float) Math.pow(2, delta);
                        mRatio = Math.min(1024.0f,
                                Math.max(0.1f, mBaseRatio * multi));
                        help_text.setTextSize(mRatio + 30);

                        // mytv2.setPadding(0, 0, 0, 0);

                    }
                }
                return true;
            }
        });
    }

    @Override
    protected void onPause() {
        if(textToSpeech !=null){
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        textToSpeech.stop();
        finish();
    }

    public boolean hasConnection() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);

        NetworkInfo wifiNetwork = cm
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiNetwork != null && wifiNetwork.isConnected()) {
            return true;
        }

        NetworkInfo mobileNetwork = cm
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (mobileNetwork != null && mobileNetwork.isConnected()) {
            return true;
        }

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnected()) {
            return true;
        }

        return false;
    }

    private class GettingAllLang extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            Locale[] locales = Locale.getAvailableLocales();
            localeList = new ArrayList<Locale>();
            langlist_codeS = new ArrayList<String>();

            langlist_code = new ArrayList<Language>();
            for (Locale locale : locales) {

                try {
                    int res = textToSpeech.isLanguageAvailable(locale);
                    if (res == TextToSpeech.LANG_COUNTRY_AVAILABLE) {
                        localeList.add(locale);
                    }

                } catch (Exception e) {

                    Log.e("Locale_P", "" + locale);
                    // TODO: handle exception
                }
            }
            int qw = localeList.size();

            for (int i = 0; i < localeList.size(); i++) {
                langlist_codeS.add(localeList.get(i).getLanguage());
            }

            Translate.setClientId("com_steve_OurPillsTalk007");
            Translate
                    .setClientSecret("QR2+jqRWpURZXpfEOMD7h6+9Nqy83hYBw3UuKcYaQIw=");
            try {
                langcodeS = SharedPreferencesIO.getLanguageCode(getApplicationContext());
                //langcodeS = getString("langcode");
                if (langcodeS.equalsIgnoreCase("")) {
                    langcodeS = "en";
                }

                Language stoL;
                stoL = Language.fromString(langcodeS);
                converted = Translate.execute(text, stoL);

                for (int i = 0; i < langlist_codeS.size(); i++) {
                    String check = langlist_codeS.get(i);
                    String[] langcodeS2 = langcodeS.split("-");
                    // String[] splited = check.split("-");
                    langcodeS3 = langcodeS2[0];
                    if (check.equalsIgnoreCase(langcodeS3)) {
                        speak_available = true;

                        break;
                    } else {
                        speak_available = false;

                    }
                }

            } catch (Exception e) {
                e.printStackTrace();

            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            // onInit(0);\
            if (hasConnection()) {
                try {
                    pDialog.dismiss();
                    help_text.setVisibility(View.VISIBLE);
                    help_text.setText(converted);
                    // int selected_lang = getInt("language_type");
                    Locale la = Locale.CHINESE;
                    // toast("" + la);
                    // toast(langcodeS3);
                    Locale a = new Locale(langcodeS3);
                    if (speak_available) {
                        textToSpeech.setLanguage(a);
                        textToSpeech.speak(converted,
                                TextToSpeech.QUEUE_FLUSH, null);
                    } else {
                        Toast.makeText(getApplicationContext(),
                                "Speech Not Available", Toast.LENGTH_LONG)
                                .show();
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                    pDialog.dismiss();
                    //toast("Internet Connection Error");
                }

            } else {
                pDialog.dismiss();
                //toast("No Internet Connection");
            }

        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            pDialog = new ProgressDialog(HelpActivity.this);
            pDialog.setMessage("Please wait ...");
            pDialog.setCancelable(false);
            pDialog.show();
        }
    }
    int getDistance(MotionEvent event) {
        int dx = (int) (event.getX(0) - event.getX(1));
        int dy = (int) (event.getY(0) - event.getY(1));
        return (int) (Math.sqrt(dx * dx + dy * dy));
    }


    @Override
    public void onInit(int arg0) {
        // TODO Auto-generated method stub

        GettingAllLang gal = new GettingAllLang();
        gal.execute();

    }

    @Override
    public boolean onTouch(View arg0, MotionEvent arg1) {
        // TODO Auto-generated method stub

        return false;
    }

}
