package au.com.ourpillstalk.ourpillstalk;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

/**
 * Created by michaelfaro-tusino on 5/05/15.
 */
public class PrivacyPolicyDialog extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedState){
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
        alertBuilder.setTitle(R.string.privDialogTitle)
                .setMessage(R.string.privPolicy)

                .setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        return alertBuilder.create();

    }
}
