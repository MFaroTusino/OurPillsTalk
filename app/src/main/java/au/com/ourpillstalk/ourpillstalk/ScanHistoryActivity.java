package au.com.ourpillstalk.ourpillstalk;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Elliott on 13/04/15.
 */
public class ScanHistoryActivity extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener {

    ListView scanListView;
    ScanPreviewAdaptor scanPreviewAdaptor;
    ToggleButton toggleDuplicates;
    Button searchButton;
    EditText searchEditText;
    String searchKeyword = "";
    LinearLayout scanHistoryLayout;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setTitle("Scan History");
        //FileIO.deleteOldScans(12, getApplicationContext());
        setContentView(R.layout.activity_scan_history_new);
        scanListView = (ListView) findViewById(R.id.scanDataListView);
        toggleDuplicates = (ToggleButton) findViewById(R.id.toggleDuplicates);
        searchButton = (Button) findViewById(R.id.searchButton);
        searchEditText = (EditText) findViewById(R.id.searchScanEditText);
        scanHistoryLayout = (LinearLayout) findViewById(R.id.scanHistoryLayout);
        SharedPreferencesIO.setShowSearch(false, getApplicationContext());
        reBuild();
    }

    /*@Override
    protected void onResume() {
        if (!scanIndex[0].equals("-1")) {
            createAdaptor();
        }
        super.onResume();
    }*/

    public void reBuild() {

        String[] index = FileIO.getIndexArray(getApplicationContext());
        boolean duplicateToggle = SharedPreferencesIO.getToggleDuplicates(getApplicationContext()); //prefs.getBoolean(SharedPreferencesIO.KEY_TOGGLE_SCAN_DUPLICATES, false);
        boolean showSearch = SharedPreferencesIO.getShowSearch(getApplicationContext());
        if(!index[0].equals("-1")) {

            if(duplicateToggle) {
                toggleDuplicates.setChecked(true);
                index = FileIO.getRemovedDuplicateIndex(getApplication());
            } else {
                toggleDuplicates.setChecked(false);
            }

            if(showSearch) {
                index = FileIO.searchIndexedScansForKeyWord(searchKeyword, index, getApplicationContext());
            }
            // if(!index[0].equals("-1")) {
            createAdaptor(index);
            //}
        } else {

            setContentView(R.layout.activity_scan_history_new);
            scanListView = (ListView) findViewById(R.id.scanDataListView);
            toggleDuplicates = (ToggleButton) findViewById(R.id.toggleDuplicates);
        }
        setOnClickListeners();
    }

    private void createAdaptor(String[] index) {
        //scansAdaptor = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, FileIO.getFileBodies(getLastNScansInverted(scanIndex), getApplicationContext()));
        //scanListView = (ListView) findViewById(R.id.scanDataListView);

        //scanIndex = FileIO.getIndexArray(getApplicationContext());
        String[] invertedIndex = FileIO.getIndexInverted(index);
        String[] invertedFileHeadersBodies;

        if(!index[0].equals("-1")) {
            //invertedFileHeadersBodies = FileIO.getFileBodies(getScansInverted(index), getApplicationContext());
            invertedFileHeadersBodies = FileIO.getCompleteScanFiles(invertedIndex, true, getApplicationContext());
        } else {
            invertedFileHeadersBodies = new String[1];
            invertedFileHeadersBodies[0] = "No Scan Found";
        }
        ArrayList<String> files = new ArrayList<String>();

        for(int i = 0; i < invertedFileHeadersBodies.length; i++) {
            files.add(invertedFileHeadersBodies[i]);
        }

        scanPreviewAdaptor = new ScanPreviewAdaptor(getApplicationContext(), R.layout.scan_history_view_row, R.id.scanPreviewViewText, files, this, index);
        if(index[0].equals("-1")) {
            scanPreviewAdaptor.hideDelete = true;
        }
        scanListView.setAdapter(scanPreviewAdaptor);

    }

    private void setOnClickListeners() {
        toggleDuplicates.setOnClickListener(this);
        searchButton.setOnClickListener(this);
        searchEditText.setOnClickListener(this);
        searchEditText.setOnFocusChangeListener(this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager im = (InputMethodManager)getSystemService(Context.
                INPUT_METHOD_SERVICE);
        im.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        scanHistoryLayout.requestFocus();
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_settings:
                Toast.makeText(getApplicationContext(), "menu pressed", Toast.LENGTH_LONG).show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.toggleDuplicates: {
                SharedPreferencesIO.setToggleDuplicates(toggleDuplicates.isChecked(), getApplicationContext());
                reBuild();
                break;
            }case R.id.searchScanEditText: {
                //InputMethodManager im = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                //im.showSoftInputFromInputMethod(searchEditText.getWindowToken(), 0);
                searchEditText.setText("");

                break;
            } case R.id.searchButton:{

                SharedPreferencesIO.setShowSearch(true, getApplicationContext());
                searchKeyword = searchEditText.getText().toString();
                InputMethodManager im = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                im.hideSoftInputFromWindow(searchEditText.getWindowToken(), 0);
                scanHistoryLayout.requestFocus();
                reBuild();
                break;
            }

        }
    }

    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        switch(view.getId()) {
            case R.id.searchScanEditText: {
                if(hasFocus) {
                    searchEditText.setText("");
                }
            }
        }
    }

    private class ScanPreviewAdaptor extends ArrayAdapter<String> {
        int resource; //layout
        int id; //textView
        boolean hideDelete;
        Context context;
        List<String> fileBodies;
        Activity activity;
        String[] index;

        public ScanPreviewAdaptor(Context context, int resource, int id, List<String> fileBodies, Activity activity, String[] index) {
            super(context, resource, id, fileBodies);
            this.context = context;
            this.fileBodies = fileBodies;
            this.resource = resource;
            this.id = id;
            this.activity = activity;
            this.index = index;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(resource, parent, false);

            TextView scanPreviewViewText = (TextView) rowView.findViewById(id);
            ImageButton deleteButton = (ImageButton) rowView.findViewById(R.id.deleteButton);
            if(hideDelete) {
                deleteButton.setVisibility(View.INVISIBLE);
            }
            scanPreviewViewText.setText(fileBodies.get(position));

            scanPreviewViewText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(!searchEditText.hasFocus()) {
                        String[] scanIndex = index;
                        String fileName = scanIndex[(scanIndex.length - 1) - position];
                        Intent showClickedScan = new Intent(context, ScanActivity.class); //changed
                        showClickedScan.putExtra("fileName", fileName);
                        //showClickedScan.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        activity.startActivity(showClickedScan);
                    } else {
                        InputMethodManager im = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        im.hideSoftInputFromWindow(searchEditText.getWindowToken(), 0);
                        scanHistoryLayout.requestFocus();
                    }
                }
            });

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String[] scanIndex = FileIO.getIndexArray(context);
                    String fileName = scanIndex[(scanIndex.length - 1) - position];
                    FileIO.deleteScanInIndex(fileName, context);
                    ScanHistoryActivity scanHistoryActivity = (ScanHistoryActivity) activity;
                    scanHistoryActivity.reBuild();
                }
            });
            return rowView;
        }
    }
}
