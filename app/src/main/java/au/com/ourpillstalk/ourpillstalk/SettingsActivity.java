package au.com.ourpillstalk.ourpillstalk;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.ToggleButton;
import android.app.DialogFragment;

import com.memetix.mst.language.Language;
import com.memetix.mst.translate.Translate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Elliott on 29/04/15.
 * Modified by MichaelFaro-Tusino on 8/05/15.
 */
public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {
    Spinner languageChoiceSpinner;
    Button eraseAllScans, resetUserInfo;
    ToggleButton toggleDuplicatesButton;
    ArrayList<String> languageStrings;
    ArrayList<Language> language;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_new);
        languageChoiceSpinner = (Spinner) findViewById(R.id.languageChoiceSpinner);
        toggleDuplicatesButton = (ToggleButton) findViewById(R.id.toggleDuplicatesButton);
        eraseAllScans = (Button) findViewById(R.id.deleteAllScansButton);
        resetUserInfo = (Button) findViewById(R.id.resetUserInfoButton);
        if(SharedPreferencesIO.getToggleDuplicates(getApplicationContext())) {
            toggleDuplicatesButton.setChecked(true);
        } else {
            toggleDuplicatesButton.setChecked(false);
        }
        setOnClickListeners();
        GetLanguageAsync getLanguages = new GetLanguageAsync();

        if(hasConnection()) {
            getLanguages.execute();
        }

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
    private void setSpinnerChoiceSelection() {
        String languageCode = SharedPreferencesIO.getLanguageCode(getApplicationContext());
        ArrayList<String> allLanguageCodes = FileIO.getAvailableLanguagesCodes(getApplicationContext());
        int indexOfLanguage = allLanguageCodes.indexOf(languageCode);
        if(indexOfLanguage != -1) {
            languageChoiceSpinner.setSelection(indexOfLanguage);
        } else {
            languageChoiceSpinner.setSelection(9); //9 = english
        }
    }

    private void setOnClickListeners() {

        languageChoiceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String languageSelected = languageChoiceSpinner.getItemAtPosition(i).toString();
                String languageCode = FileIO.getAvailableLanguagesCodes(getApplicationContext()).get(i);
                SharedPreferencesIO.setLanguageCode(languageCode, getApplicationContext());
                languageChoiceSpinner.setSelection(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        toggleDuplicatesButton.setOnClickListener(this);
        eraseAllScans.setOnClickListener(this);
        resetUserInfo.setOnClickListener(this);

    }

    private void createAdaptor(ArrayList<String> language) {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_language_choice, R.id.languageSpinnerText, language);
        languageChoiceSpinner.setAdapter(arrayAdapter);
        languageChoiceSpinner.setSelection(0, false);
    }
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.toggleDuplicatesButton: {
                if (toggleDuplicatesButton.isChecked()) {
                    SharedPreferencesIO.setToggleDuplicates(true, getApplicationContext());
                } else {
                    SharedPreferencesIO.setToggleDuplicates(false, getApplicationContext());
                }
                break;
            }
            case (R.id.deleteAllScansButton): {
                DialogFragment deleteScansDialog = new CustomDialog();
                CustomDialog.setInstance("scans");
                deleteScansDialog.show(getFragmentManager(), "deleteScans");
                break;
            }
            case (R.id.resetUserInfoButton): {
                DialogFragment deleteUserDialog = new CustomDialog();
                CustomDialog.setInstance("user");
                deleteUserDialog.show(getFragmentManager(), "eraseUser");
                break;
            }
        }
    }
    private class GetLanguageAsync extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            Translate.setClientId("com_steve_OurPillsTalk007");
            Translate.setClientSecret("QR2+jqRWpURZXpfEOMD7h6+9Nqy83hYBw3UuKcYaQIw=");
            try {
                language = new ArrayList<Language>();
                languageStrings = new ArrayList<String>();
                Language l = Language.ENGLISH;
                Map<String, Language> map = Language.values(l);
                Iterator it = map.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry pairs = (Map.Entry) it.next();
                    //Language language = Language.fromString(pairs.getValue().toString());
                    //String languageNameTranslated = Translate.execute((String) pairs.getKey(), l);
                    languageStrings.add((String) pairs.getKey()); //(String)
                    language.add((Language) pairs.getValue());
                    it.remove(); // avoids a ConcurrentModificationException
                }
                languageStrings.remove(0);
                language.remove(0);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            FileIO.saveAvailableLanguages(languageStrings, language, getApplicationContext());
            createAdaptor(FileIO.getAvailableLanguages(getApplicationContext()));
            setSpinnerChoiceSelection();
        }
    }
}
