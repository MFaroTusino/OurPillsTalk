package au.com.ourpillstalk.ourpillstalk;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.content.Intent;


/**
 * Created by michaelfaro-tusino on 5/05/15.
 */
public class CustomDialog extends DialogFragment {
    public static String popupInstance;
    public static String drugName;

    public static void setInstance(String instance) {
        popupInstance = instance.toLowerCase();
    }

    public static void setInstance(String instance, String fileName) {
        popupInstance = instance.toLowerCase();
        drugName = fileName.toLowerCase();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedState) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
        //Check which activity calls the popup dialog and customise it accordingly
        if (popupInstance == "privacy") {
            //User Info Popup
            alertBuilder.setTitle(R.string.privDialogTitle)
                    .setMessage(R.string.privPolicy)
                    .setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int id) {

                        }
                    });


        } else if (popupInstance == "user") {
            alertBuilder.setTitle(R.string.eraseTitle)
                    .setMessage(R.string.eraseUserInfo);
            alertBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    SharedPreferencesIO.setNewUser(true, getActivity().getApplicationContext());
                    Intent showInfoActivity = new Intent(getActivity().getApplicationContext(), UserInfoActivity.class);
                    showInfoActivity.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(showInfoActivity);
                }
            });
            alertBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                }
            });
        } else if (popupInstance == "scans") {
            alertBuilder.setTitle(R.string.eraseTitle)
                    .setMessage(R.string.eraseScanInfo);
            alertBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    FileIO.deleteScanIndex(getActivity().getApplicationContext());
                }
            });
            alertBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                }
            });

        } else if(popupInstance == "cmi") {
            alertBuilder.setTitle("Leaving Our Pills Talk")
                    .setMessage("The following CMI is not affiliated with Our Pills Talk.\n\nLeave Our Pills Talk to view CMI?");
            alertBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                    Intent i = new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://www.medicines.org.au/mob/consumer-results.cfm?searchtext=" + drugName));
                    getActivity().startActivity(i);
                }
            });
            alertBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                }
            });
        } else if(popupInstance == "incorrectQR") {
            alertBuilder.setTitle("Foreign QR code")
                    .setMessage("Please scan a compatable Our Pills Talk QR Code");
            alertBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                }
            });
            alertBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                }
            });
        }
        return alertBuilder.create();
    }
}
