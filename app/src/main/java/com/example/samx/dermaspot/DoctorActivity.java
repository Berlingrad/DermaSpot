package com.example.samx.dermaspot;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseUser;

public class DoctorActivity extends AppCompatActivity {
    EditText firstname, lastname, emailAddress;
    Button create;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor);
        firstname = (EditText) findViewById(R.id.editText7);
        lastname = (EditText) findViewById(R.id.editText8);
        emailAddress = (EditText) findViewById(R.id.editText9);
        create = (Button) findViewById(R.id.button8);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseUser user = ParseUser.getCurrentUser();
                String fname = firstname.getText().toString();
                String lname = lastname.getText().toString();
                String email = emailAddress.getText().toString();
                user.put("firstName", fname);
                user.put("lastName", lname);
                user.put("email", email);
                if (fname != null && fname.length() != 0 && lname != null && lname.length() != 0 &&
                        email != null && email.length() != 0) {
                    user.put("isComplete", true);
                    Intent k = new Intent(DoctorActivity.this, DoctorActivity2.class);
                    startActivity(k);
                } else {
                    Toast.makeText(getApplicationContext(), "Please fill all fields!", Toast.LENGTH_LONG).show();
                }
                user.saveInBackground();
            }
        });
    }
}
