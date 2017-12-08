package com.example.samx.dermaspot;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseUser;

public class PatientActivity extends AppCompatActivity {
    EditText firstName, lastName, gender, age;
    Button create;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient);
        firstName = (EditText) findViewById(R.id.editText3);
        lastName = (EditText) findViewById(R.id.editText4);
        age = (EditText) findViewById(R.id.editText5);
        gender = (EditText) findViewById(R.id.editText6);
        create = (Button) findViewById(R.id.button3);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser user = ParseUser.getCurrentUser();
                String fname = firstName.getText().toString();
                String lname = lastName.getText().toString();
                Number aage = Integer.parseInt(age.getText().toString());
                String gendr = gender.getText().toString();
                user.put("firstName", fname);
                user.put("lastName", lname);
                user.put("age", aage);
                user.put("gender", gendr);
                // set isComplete to true if user info is complete
                if (fname != null && fname.length() != 0 && lname != null && lname.length() != 0 &&
                        aage != null && gendr != null && gendr.length() != 0) {
                    user.put("isComplete", true);
                    Intent k = new Intent(PatientActivity.this, PatientActivity2.class);
                    startActivity(k);
                } else {
                    Toast.makeText(getApplicationContext(), "Please fill all fields!", Toast.LENGTH_LONG).show();
                }
                user.saveInBackground();
            }
        });
    }
}
