package au.com.ourpillstalk.ourpillstalk;

import android.app.ProgressDialog;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.memetix.mst.language.Language;
import com.memetix.mst.translate.Translate;
import java.util.Locale;

/**
 * Created by Elliott on 12/04/15.
 */
public class ScanActivity extends AppCompatActivity implements View.OnClickListener, TextToSpeech.OnInitListener {

    TextView scanTextView;
    Button playButton, stopButton;
    TextToSpeech textToSpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Scan");
        setContentView(R.layout.activity_scan);
        Bundle passedBundle = getIntent().getExtras();
        String fileName = "";
        fileName = passedBundle.getString("fileName");
        scanTextView = (TextView) findViewById(R.id.lastScanTextView);
        playButton = (Button) findViewById(R.id.playButton);
        stopButton = (Button) findViewById(R.id.stopButton);

        if(!fileName.equals("-1")) {
            scanTextView.setText(FileIO.getFileBody(fileName, getApplicationContext()));
        } else {
            scanTextView.setText("No scan history");
        }
        setOnClickListeners();
        translate();
        startTextToSpeech();
    }

    private void setOnClickListeners() {
        scanTextView.setOnClickListener(this);
        playButton.setOnClickListener(this);
        stopButton.setOnClickListener(this);
    }

    private void startTextToSpeech() {
        if(textToSpeech !=null){
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        textToSpeech = new TextToSpeech(getApplicationContext(), this);
    }

    private void translate() {
        //if internet connection and not already English
        if(hasConnection() && !SharedPreferencesIO.getLanguageCode(getApplicationContext()).equals("en")) {
            TranslateAsync translateAsync = new TranslateAsync();
            translateAsync.execute();
        }



        /*if(hasConnection() && !getString("langcode").equals("en")) {
            TranslateAsync translateAsync = new TranslateAsync();
            translateAsync.execute();
        }*/
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

    @Override
    public void onPause() {
        if(textToSpeech !=null){
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onPause();
    }
    @Override
    public void onInit(int status) {

        if (status == TextToSpeech.SUCCESS) {
            //String language = getString("langcode");
            String language = SharedPreferencesIO.getLanguageCode(getApplicationContext());
            Locale locale = new Locale(language);
            int result = textToSpeech.setLanguage(locale);
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Toast.makeText(getApplicationContext(), "Text to speak language not supported", Toast.LENGTH_LONG).show();
            } else {
                textToSpeech(scanTextView);
            }
        } else {
            Toast.makeText(getApplicationContext(), "Text to startTextToSpeech 3", Toast.LENGTH_LONG).show();
            Log.e("TTS", "Initilization Failed!");
        }

    }

    private void textToSpeech(TextView textView) {
        String text = textView.getText().toString();
        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }

    /**
     * Does translation in of text background
     */
    private class TranslateAsync extends AsyncTask<String, Void, String> {
        String translatedText;
        ProgressDialog pDialog;

        protected String doInBackground(String... urls) {
            try {
                Translate.setClientId("com_steve_OurPillsTalk007");
                Translate.setClientSecret("QR2+jqRWpURZXpfEOMD7h6+9Nqy83hYBw3UuKcYaQIw=");

                String languageCode = SharedPreferencesIO.getLanguageCode(getApplicationContext());
                //String languageCode = getString("langcode");

                if (languageCode.equalsIgnoreCase("")) {
                    languageCode = "en";
                }

                Language l;
                l = Language.fromString(languageCode);
                translatedText = Translate.execute(scanTextView.getText().toString(), l);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ScanActivity.this);
            pDialog.setMessage("Please wait ...");
            pDialog.setCancelable(false);
            pDialog.show();
        }
        protected void onPostExecute(String result) {
            scanTextView.setText(translatedText);
            startTextToSpeech();
            pDialog.dismiss();

        }

    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.lastScanTextView: {
                break;
            }
            case R.id.playButton: {
               startTextToSpeech();
                break;
            }
            case R.id.stopButton: {
                if(textToSpeech !=null){
                    textToSpeech.stop();
                    textToSpeech.shutdown();
                }
                break;
            }
        }
    }
}
