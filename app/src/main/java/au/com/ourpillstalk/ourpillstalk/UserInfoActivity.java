package au.com.ourpillstalk.ourpillstalk;

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
    Button userInfoButton;
    EditText nameEditText, cityEditText, suburbEditText, emergencyNumberEditText, medicationsEditText;
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
        suburbEditText = (EditText) findViewById(R.id.suburbEditText);
        emergencyNumberEditText = (EditText) findViewById(R.id.emrgencyContactEditText);
        medicationsEditText = (EditText) findViewById(R.id.currentMedicationEditText);
        userInfoButton = (Button) findViewById(R.id.userInfoButton);
        setOnClickListeners();
    }

    private void setOnClickListeners() {
        userInfoButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case(R.id.userInfoButton): {
                SharedPreferencesIO.setNewUser(false, getApplicationContext());
                String[] userInfo = {nameEditText.getText().toString(), cityEditText.getText().toString(), suburbEditText.getText().toString(), emergencyNumberEditText.getText().toString(), medicationsEditText.getText().toString()};
                FileIO.saveUserInformation(userInfo, getApplicationContext());
                Intent showMain = new Intent(this, MainActivity.class);
                startActivity(showMain);
                break;
            }

        }
    }

}
