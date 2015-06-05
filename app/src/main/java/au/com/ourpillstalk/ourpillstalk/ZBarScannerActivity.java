package au.com.ourpillstalk.ourpillstalk;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;

import me.dm7.barcodescanner.zbar.BarcodeFormat;
import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;

/**
 * Created by Elliott on 28/04/15.
 */
public class ZBarScannerActivity extends Activity implements ZBarScannerView.ResultHandler, View.OnClickListener {
    private ZBarScannerView mScannerView;
    private int flashCount = 0;
    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        mScannerView = new ZBarScannerView(this);    // Programmatically initialize the scanner view
        setContentView(mScannerView);                // Set the scanner view as the content view
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();          // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }


    @Override
    public void handleResult(Result rawResult) {
        // Do something with the result here
        if(rawResult.getBarcodeFormat() == BarcodeFormat.QRCODE) {

            String scanResult = rawResult.getContents();
            ToneGenerator tone = new ToneGenerator(AudioManager.STREAM_ALARM, 100);
            tone.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 400);
            Vibrator v = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
            v.vibrate(500);

            //save scan to file if Our Pills Talk QR code and pass the file name to the Scan Activity (the last scan of the index)
            Intent showScan = new Intent(this, ScanActivity.class);
            if(FileIO.isPrescriptionScanXML(scanResult)) {
                FileIO.saveQRScan(scanResult, getApplicationContext());

                String[] index = FileIO.getIndexArray(getApplicationContext());
                String indexData = "";
                for (int i = 0; i < index.length; i++) {
                    indexData = indexData + index[i];
                }
                showScan.putExtra("fileName", index[index.length - 1]);

            } else {
                showScan.putExtra("fileName", "-1");
            }

            startActivity(showScan);

            Log.v("Scan result", rawResult.getContents()); // Prints scan results
            Log.v("Bar code Format", rawResult.getBarcodeFormat().getName()); // Prints the scan format (qrcode, pdf417 etc.)

        }
    }

    @Override
    public void onClick(View view) {
        if(flashCount%2 == 0) {
            mScannerView.setFlash(true);
        } else {
            mScannerView.setFlash(false);
        }

        flashCount++;
    }
}
