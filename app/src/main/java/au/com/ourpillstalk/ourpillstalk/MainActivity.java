package au.com.ourpillstalk.ourpillstalk;


import android.app.ProgressDialog;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.memetix.mst.language.Language;
import com.memetix.mst.translate.Translate;

public class MainActivity extends AppCompatActivity implements OnClickListener {

    ImageView scanImage, scanHistoryImage, emergencyImage, settingsImage, helpMeImage;
    TextView scanText, last_scanText, scanHistoryText, settingsText, helpMeText, emergencyText;
    TranslateAsync translate;
    //FrameLayout mainFrameLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Our Pills Talk");
        setContentView(R.layout.activity_main);
        if(SharedPreferencesIO.getNewUser(getApplicationContext())) {
            Intent showInfoActivity = new Intent(this, UserInfoActivity.class);
            showInfoActivity.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(showInfoActivity);
        }
        //mainFrameLayout = (FrameLayout) findViewById(R.id.mainFrameLayout);
        scanImage = (ImageView) findViewById(R.id.ButtonScan);
        emergencyImage = (ImageView) findViewById(R.id.emergencyButton);
        scanHistoryImage = (ImageView) findViewById(R.id.historyButton);
        settingsImage = (ImageView) findViewById(R.id.settingsButton);
        helpMeImage = (ImageView) findViewById(R.id.helpButton);
        emergencyText = (TextView) findViewById(R.id.emergencyText);
        scanText = (TextView) findViewById(R.id.ButtonScanText);
        last_scanText = (TextView) findViewById(R.id.lastScanButtonText);
        scanHistoryText = (TextView) findViewById(R.id.historyButtonText);
        settingsText = (TextView) findViewById(R.id.settingsButtonText);
        helpMeText = (TextView) findViewById(R.id.helpButtonText);
        //loadFakeScans();
        translate();
        setOnClickListeners();
    }

    private void translate() {
        if(!SharedPreferencesIO.getLanguageCode(getApplicationContext()).equals("en")) {
            if(translate != null) {
                translate.cancel(true);
            }
            translate = new TranslateAsync();
            translate.execute();
        }

        /*if(!getString("langcode").equals("en")) {
            if(translate != null) {
                translate.cancel(true);
            }
            translate = new TranslateAsync();
            translate.execute();
        }*/
    }

    private void setOnClickListeners() {
        //mainFrameLayout.setOnClickListener(this);
        emergencyText.setOnClickListener(this);
        scanImage.setOnClickListener(this);
        scanHistoryImage.setOnClickListener(this);
        settingsImage.setOnClickListener(this);
        helpMeImage.setOnClickListener(this);
        emergencyImage.setOnClickListener(this);
        scanText.setOnClickListener(this);
        scanHistoryText.setOnClickListener(this);
        settingsText.setOnClickListener(this);
        helpMeText.setOnClickListener(this);
        last_scanText.setOnClickListener(this);
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

    /*public Drawable getSelectedItemDrawable() {
        int[] attrs = new int[]{R.attr.selectableItemBackground};
        TypedArray ta = getApplicationContext().obtainStyledAttributes(attrs);
        Drawable selectedItemDrawable = ta.getDrawable(0);
        ta.recycle();
        return selectedItemDrawable;
    }*/
    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.emergencyButton || view.getId() == R.id.emergencyText) {
            Intent intent = new Intent(this, EmergencyActivity.class);
            startActivity(intent);
        } else if(view.getId() == R.id.ButtonScan || view.getId() == R.id.ButtonScanText) {
            try {
                Intent intent = new Intent(this, ZBarScannerActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "ERROR:" + e, Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        } else if(view.getId() == R.id.historyButton || view.getId() == R.id.historyButtonText) {
            Intent intent = new Intent(this, ScanHistoryActivity.class);
            startActivity(intent);
        } else if(view.getId() == R.id.settingsButton || view.getId() == R.id.settingsButtonText) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        } else if(view.getId() == R.id.helpButton || view.getId() == R.id.helpButtonText) {
            Intent intent = new Intent(this, HelpActivity.class);
            startActivity(intent);
        }
    }
   /* @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                String vQRContent = intent.getStringExtra("SCAN_RESULT");

                //save scan to file and pass the file name to the Scan Activity (the last scan of the index)
                FileIO.saveQRScan(vQRContent, getApplicationContext());
                Intent showScan = new Intent(this, ScanActivity.class);
                String[] index = FileIO.getIndexArray(getApplicationContext());

                String indexData = "";
                for(int i = 0; i < index.length; i++) {
                    indexData = indexData + index[i];
                }
                //Toast.makeText(getApplicationContext(), index[index.length-1], Toast.LENGTH_LONG).show();
                showScan.putExtra("fileName", index[index.length-1]);
                startActivity(showScan);

               // Intent vTTSIntent = new Intent(this, LastScanActivity.class); //changed
                //vTTSIntent.putExtra("ExternalData", vQRContent);
                //startActivity(vTTSIntent);
                }
            } else if (resultCode == RESULT_CANCELED) {

            }
        }*/


    private class TranslateAsync extends AsyncTask<String, Void, String>  {
        ProgressDialog pDialog;
        String[] translatedText = new String[5];
        String languageCode;
        protected String doInBackground(String... urls) {
            try {
                Translate.setClientId("com_steve_OurPillsTalk007");
                Translate.setClientSecret("QR2+jqRWpURZXpfEOMD7h6+9Nqy83hYBw3UuKcYaQIw=");
                languageCode = SharedPreferencesIO.getLanguageCode(getApplicationContext());
                if (languageCode.equalsIgnoreCase("")) {
                    languageCode = "en";
                }

                if(languageCode.equals(FileIO.getSavedMainTranslationLanguageCode(getApplicationContext())) ) {
                    translatedText = FileIO.getMainMenuTranslation(getApplicationContext());

                } else if(!hasConnection()) {
                    translatedText = FileIO.getMainMenuTranslation(getApplicationContext());
                } else {
                    Language l;
                    l = Language.fromString(languageCode);
                    translatedText[0] = Translate.execute(scanText.getText().toString(), l);
                    translatedText[1] = Translate.execute(emergencyText.getText().toString(), l);
                    translatedText[2] = Translate.execute(scanHistoryText.getText().toString(), l);
                    translatedText[3] = Translate.execute(settingsText.getText().toString(), l);
                    translatedText[4] = Translate.execute(helpMeText.getText().toString(), l);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Please wait ...");
            pDialog.setCancelable(false);
            pDialog.show();
            //}
        }
        protected void onPostExecute(String result) {
            FileIO.saveMainMenuTranslation(translatedText, languageCode, getApplicationContext());
            translatedText = FileIO.getMainMenuTranslation(getApplicationContext());
            scanText.setText(translatedText[0]);
            emergencyText.setText(translatedText[1]);
            scanHistoryText.setText(translatedText[2]);
            settingsText.setText(translatedText[3]);
            helpMeText.setText(translatedText[4]);
            pDialog.dismiss();
        }
    }
}