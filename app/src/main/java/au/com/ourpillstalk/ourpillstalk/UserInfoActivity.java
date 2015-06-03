package au.com.ourpillstalk.ourpillstalk;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by Elliott on 21/04/15.
 */
public class UserInfoActivity extends AppCompatActivity implements View.OnClickListener{
    Button userInfoButton, privButton;

    EditText nameEditText, cityEditText, stateEditText, emergencyNumberEditText, medicationsEditText, allergiesEditText;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Welcome to Our Pills Talk!");
        setContentView(R.layout.activity_user_info);
        if(!SharedPreferencesIO.getNewUser(getApplicationContext())) {
            Intent showMain = new Intent(this, MainActivity.class);
            startActivity(showMain);
        }
        nameEditText = (EditText) findViewById(R.id.nameEditText);
        cityEditText = (EditText) findViewById(R.id.cityEditText);
        stateEditText = (EditText) findViewById(R.id.stateEditText);
        emergencyNumberEditText = (EditText) findViewById(R.id.emrgencyContactEditText);
        medicationsEditText = (EditText) findViewById(R.id.currentMedicationEditText);
        allergiesEditText = (EditText) findViewById(R.id.allergyText);
        userInfoButton = (Button) findViewById(R.id.userInfoButton);
        privButton = (Button) findViewById(R.id.privButton);
        setOnClickListeners();
    }

    private void setOnClickListeners() {
        userInfoButton.setOnClickListener(this);
        privButton.setOnClickListener(this);
    }




    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case(R.id.userInfoButton): {
                SharedPreferencesIO.setNewUser(false, getApplicationContext());
                String[] userInfo = {nameEditText.getText().toString(), cityEditText.getText().toString(), stateEditText.getText().toString(), emergencyNumberEditText.getText().toString(), medicationsEditText.getText().toString(), allergiesEditText.getText().toString()};
                FileIO.saveUserInformation(userInfo, getApplicationContext());
                Intent showMain = new Intent(this, MainActivity.class);
                startActivity(showMain);
                break;
            }
            case(R.id.privButton): {
                DialogFragment privDialog = new CustomDialog();
                CustomDialog.setInstance("privacy");
                privDialog.show(getFragmentManager(), "privacy");
                break;
            }

        }
    }

}
