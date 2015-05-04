package au.com.ourpillstalk.ourpillstalk;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.Nadeem.Utils.MyActivity;
import com.memetix.mst.MicrosoftTranslatorAPI;
import com.memetix.mst.language.Language;
import com.memetix.mst.translate.Translate;

import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class z_Deprecated_SettingsActivity extends MyActivity implements OnInitListener, OnClickListener {
    ToggleButton inverse, audio;
    Boolean inverse_B = false, audio_B = false;
    ImageView deleteall;
    ImageView back;
    Spinner spinner;
    ArrayAdapter<String> sa;
    TextToSpeech mTextToSpeech;
    int selected_lang;
    ArrayList<Locale> localeList;
    TextView readtext;
    String spinner_string;
    List<Language> myarraylist;
    ProgressDialog pDialog;
    private Map<Language, String> localizedCache;
    ArrayList<String> listtoshow;
    ToggleButton toggleDuplicatesButton;
    Button resetUserInfoButton, deleteAllScansButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        toggleDuplicatesButton = (ToggleButton) findViewById(R.id.toggleDuplicatesButton);
        resetUserInfoButton = (Button) findViewById(R.id.resetUserInfoButton);
        deleteAllScansButton = (Button) findViewById(R.id.deleteAllScansButton);

        audio = (ToggleButton) findViewById(R.id.toggleButton1);
        inverse = (ToggleButton) findViewById(R.id.toggleButton2);
        deleteall = (ImageView) findViewById(R.id.deleteall);
        back = (ImageView) findViewById(R.id.back);
        spinner = (Spinner) findViewById(R.id.spinner1);
        // readtext = (TextView) findViewById(R.id.textView5);
        localizedCache = new ConcurrentHashMap<Language, String>();
        inverse_B = getBoolean("inverse_check");
        audio_B = getBoolean("audio_check");
        audio.setChecked(audio_B);
        inverse.setChecked(inverse_B);
        mTextToSpeech = new TextToSpeech(this, this);
        if(SharedPreferencesIO.getToggleDuplicates(getApplicationContext())) {
            toggleDuplicatesButton.setChecked(true);
        } else {
            toggleDuplicatesButton.setChecked(false);
        }
        toggleDuplicatesButton.setOnClickListener(this);
        resetUserInfoButton.setOnClickListener(this);
        deleteAllScansButton.setOnClickListener(this);
        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub

                spinner_string = spinner.getItemAtPosition(arg2).toString();
                writeString("spinner_string", spinner_string);

                selected_lang = spinner.getSelectedItemPosition();
                writeInt("language_type", selected_lang);
                // log(spinner_string);
                // String langcode = localeList.get(arg2).getLanguage();
                // writeString("langcode", langcode);

                Language langcode_L = myarraylist.get(arg2);
                String langcode = langcode_L.toString();
                //writeString("langcode", langcode);
                SharedPreferencesIO.setLanguageCode(langcode, getApplicationContext());
                // toast(langcode);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });
        audio.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                // TODO Auto-generated method stub
                writeBoolean("audio_check", arg1);
            }
        });
    }

    @Override
    public void onTaskComplete(String result, String key) {


    }

    @Override
    public void onInit(int arg0) {
        GettingAllLang gal = new GettingAllLang();
        gal.execute();
    }

    private int getIndex(Spinner spinner, String spinnergettext) {

        int index = 0;

        for (int i = 0; i < spinner.getCount(); i++) {
            String a = (String) spinner.getItemAtPosition(i);

            // String qwe = spinner.getItemAtPosition(i);
            if (a.equalsIgnoreCase(spinnergettext)) {
                index = i;
            }
        }
        return index;
    }

    @Override
    public void onClick(View view) {

        switch(view.getId()) {
            case R.id.toggleDuplicatesButton: {
                if(toggleDuplicatesButton.isChecked()) {
                    SharedPreferencesIO.setToggleDuplicates(true, getApplicationContext());
                } else {
                    SharedPreferencesIO.setToggleDuplicates(false, getApplicationContext());
                }
                break;
            }
            case (R.id.deleteAllScansButton): {

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Are you sure you want to erase your scan history?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        FileIO.deleteScanIndex(getApplicationContext());
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
                break;
            }
            case (R.id.resetUserInfoButton): {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Are you sure you want to reset user information?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SharedPreferencesIO.setNewUser(true, getApplicationContext());
                        Intent showInfoActivity = new Intent(getApplicationContext(), UserInfoActivity.class);
                        showInfoActivity.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        startActivity(showInfoActivity);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();

                break;
            }
        }
    }

    private class GettingAllLang extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            Translate.setClientId("com_steve_OurPillsTalk007");
            Translate
                    .setClientSecret("QR2+jqRWpURZXpfEOMD7h6+9Nqy83hYBw3UuKcYaQIw=");
            try {

                myarraylist = new ArrayList<Language>();
                // myarraylist = Language.getLanguageCodesForTranslation();

                Language l = Language.ENGLISH;
                listtoshow = new ArrayList<String>();
                Map<String, Language> map = Language.values(l);
                Iterator it = map.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry pairs = (Map.Entry) it.next();
                    //Language language = Language.fromString(pairs.getValue().toString());

                    //String languageNameTranslated = Translate.execute((String) pairs.getKey(), l);
                    listtoshow.add((String) pairs.getKey()); //(String)
                    myarraylist.add((Language) pairs.getValue());

                    it.remove(); // avoids a ConcurrentModificationException
                }
                listtoshow.remove(0);
                myarraylist.remove(0);
            } catch (Exception e) {
                e.printStackTrace();

            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            // onInit(0);
            try {
                pDialog.dismiss();
                // toast(listtoshow.size());
                sa = new ArrayAdapter<String>(getApplicationContext(),
                        R.layout.dropdownselected, R.id.tv_Time, listtoshow);
                spinner.setAdapter(sa);
                String spinnergettext = getString("spinner_string");

                // int a = Desired_StringtoSpinner(spinnergettext);
                spinner.setSelection(getIndex(spinner, spinnergettext));
            } catch (Exception e) {
                // TODO: handle exception\
                pDialog.dismiss();
                toast("Internet Connection Error");
            }

            // // textToSpeech.setLanguage(Locale.ENGLISH);
            //
            // textToSpeech.setLanguage(localeList.get(selected_lang));

        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            pDialog = new ProgressDialog(z_Deprecated_SettingsActivity.this);
            pDialog.setMessage("Loading languages ...");
            pDialog.setCancelable(false);
            pDialog.show();

        }
    }

    public static void printMap(Map mp) {
        Iterator it = mp.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry) it.next();
            System.out.println(pairs.getKey() + " = " + pairs.getValue());
            it.remove(); // avoids a ConcurrentModificationException
        }
    }

    public String getName(Language locale) throws Exception {
        String localizedName = null;
        if (this.localizedCache.containsKey(locale)) {
            localizedName = this.localizedCache.get(locale);
        } else {
            if (locale == Language.AUTO_DETECT) {
                localizedName = "Auto Detect";
            } else {
                // If not in the cache, pre-load all the Language names for this
                // locale
                String[] names = LanguageService.execute(Language.values(),
                        locale);
                int i = 0;
                for (Language lang : Language.values()) {
                    if (lang != Language.AUTO_DETECT) {
                        localizedCache.put(locale, names[i]);
                        i++;
                    }
                }
                localizedName = this.localizedCache.get(locale);
            }
        }
        return localizedName;
    }

    private final static class LanguageService extends MicrosoftTranslatorAPI {
        private static final String SERVICE_URL = "http://api.microsofttranslator.com/V2/Ajax.svc/GetLanguageNames?";

        /**
         * Detects the language of a supplied String.
         *            The String to detect the language of.
         * @return A DetectResult object containing the language, confidence and
         *         reliability.
         * @throws Exception
         *             on error.
         */
        public static String[] execute(final Language[] targets,
                                       final Language locale) throws Exception {
            // Run the basic service validations first
            validateServiceState();
            String[] localizedNames = new String[0];
            if (locale == Language.AUTO_DETECT) {
                return localizedNames;
            }

            final String targetString = buildStringArrayParam(Language.values());

            final URL url = new URL(SERVICE_URL
                    + (apiKey != null ? PARAM_APP_ID
                    + URLEncoder.encode(apiKey, ENCODING) : "")
                    + PARAM_LOCALE
                    + URLEncoder.encode(locale.toString(), ENCODING)
                    + PARAM_LANGUAGE_CODES
                    + URLEncoder.encode(targetString, ENCODING));
            localizedNames = retrieveStringArr(url);
            return localizedNames;
        }

    }

}
