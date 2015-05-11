package au.com.ourpillstalk.ourpillstalk;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.content.Intent;


/**
 * Created by michaelfaro-tusino on 5/05/15.
 */
public class CustomDialog extends DialogFragment {
    public static String popupInstance;

    public static void setInstance(String instance) {
        popupInstance = instance.toLowerCase();
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

        }
        return alertBuilder.create();
    }
}
