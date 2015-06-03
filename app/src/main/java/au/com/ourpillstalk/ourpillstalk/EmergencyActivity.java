package au.com.ourpillstalk.ourpillstalk;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Elliott on 1/04/15.
 */
public class EmergencyActivity extends AppCompatActivity implements View.OnClickListener {

    FrameLayout emergencyFrameLayout;
    ListView scanListView;
    TextView userInfoDisplayText;
    //ArrayAdapter<String> scansAdaptor;
    ScanPreviewAdaptor scanPreviewAdaptor;
    static final int NUM_SCANS = 50;
    String[] scanIndex;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Emergency");
        setContentView(R.layout.activity_emergency);
        emergencyFrameLayout = (FrameLayout) findViewById(R.id.emergencyFrameLayout);
        scanListView = (ListView) findViewById(R.id.scanDataListView);
        userInfoDisplayText = (TextView) findViewById(R.id.userInfoDisplay);
        scanIndex = FileIO.getIndexArray(getApplicationContext());
        if (!scanIndex[0].equals("-1")) {
            createAdaptor();
        }
        setUserInfoText();

        setOnClickListeners();

    }

    private void setUserInfoText() {
        String userInfo = FileIO.getUserInformation(getApplicationContext());
        if(!userInfo.equals("")) {
            userInfoDisplayText.setText(FileIO.getUserInformation(getApplicationContext()));
        }
    }

    private void createAdaptor() {
        //scansAdaptor = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, FileIO.getFileBodies(getLastNScansInverted(scanIndex), getApplicationContext()));
        //scanListView = (ListView) findViewById(R.id.scanDataListView);
        scanIndex = FileIO.getRemovedDuplicateIndex(getApplicationContext());
        String[] scans = FileIO.getCompleteScanFiles(getLastNScansInverted(scanIndex), true, getApplicationContext());
        ArrayList<String> fileBodies = new ArrayList<String>();

        for(int i = 0; i < scans.length; i++) {
            fileBodies.add(scans[i]);
        }

        scanPreviewAdaptor = new ScanPreviewAdaptor(getApplicationContext(), R.layout.scan_emergency_view_row, R.id.scanPreviewViewText, fileBodies, this);
        scanListView.setAdapter(scanPreviewAdaptor);

    }

    private void setOnClickListeners() {
        emergencyFrameLayout.setOnClickListener(this);
    }




    private String[] getLastNScansInverted(String[] scanIndex) {
        String[] showScansInverted;
        if(scanIndex.length < NUM_SCANS) {
            showScansInverted = new String[scanIndex.length];
            for(int i = 0; i < scanIndex.length; i++) {
                showScansInverted[i] = scanIndex[(scanIndex.length - 1) - i];
            }

        } else {
            showScansInverted = new String[NUM_SCANS];
            for(int i = 0; i < NUM_SCANS; i++) {
                showScansInverted[i] = scanIndex[(scanIndex.length - 1) - i];
            }
        }
        return showScansInverted;
    }

    @Override
    public void onClick(View v) {

    }

    private class ScanPreviewAdaptor extends ArrayAdapter<String> {
        int resource; //layout
        int id; //textView
        Context context;
        List<String> fileBodies;
        Activity activity;

        public ScanPreviewAdaptor(Context context, int resource, int id, List<String> fileBodies, Activity activity) {
            super(context, resource, id, fileBodies);
            this.context = context;
            this.fileBodies = fileBodies;
            this.resource = resource;
            this.id = id;
            this.activity = activity;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(resource, parent, false);
            TextView scanPreviewViewText = (TextView) rowView.findViewById(id);
            ImageButton deleteButton = (ImageButton) rowView.findViewById(R.id.deleteButton);
            scanPreviewViewText.setText(fileBodies.get(position));

            scanPreviewViewText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String[] scanIndex = FileIO.getIndexArray(context);
                    String fileName = scanIndex[(scanIndex.length - 1) - position];
                    Intent showClickedScan = new Intent(context, ScanActivity.class); //changed
                    showClickedScan.putExtra("fileName", fileName);
                    //showClickedScan.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    activity.startActivity(showClickedScan);
                }
            });

            return rowView;
        }
    }
}
