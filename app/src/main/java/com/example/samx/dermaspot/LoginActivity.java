package com.example.samx.dermaspot;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class LoginActivity extends AppCompatActivity {
    Button signUp, patientLogin, doctorLogin;
    EditText username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        signUp = (Button) findViewById(R.id.button);
        patientLogin = (Button) findViewById(R.id.button2);
        doctorLogin = (Button) findViewById(R.id.button7);
        username = (EditText) findViewById(R.id.editText);
        password = (EditText) findViewById(R.id.editText2);

        signUp.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                ParseUser user = new ParseUser();
                user.setUsername(username.getText().toString());
                user.setPassword(password.getText().toString());
                user.put("userType", "patient");
                user.signUpInBackground(new SignUpCallback() {
                    public void done(ParseException e) {
                        if (e == null) {
                            // Hooray! Let them use the app now.
                            Toast.makeText(getApplicationContext(), "Successful!", Toast.LENGTH_LONG).show();
                        } else {
                            // Sign up didn't succeed. Look at the ParseException
                            // to figure out what went wrong
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

        patientLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser.logInInBackground(username.getText().toString(), password.getText().toString(), new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        if (e == null) {
                            // check if userType is patient first
                            if (user.getString("userType").equals("patient")) {
                                Boolean isInfoComplete = user.getBoolean("isComplete");
                                if (isInfoComplete) {
                                    // go to PatientActivity2 to take pics
                                    Toast.makeText(getApplicationContext(), "Welcome back!", Toast.LENGTH_LONG).show();
                                    Intent k = new Intent(LoginActivity.this, PatientActivity2.class);
                                    startActivity(k);
                                } else {
                                    // go to PatientActivity to create user profile
                                    Toast.makeText(getApplicationContext(), "Empty userInfo!", Toast.LENGTH_LONG).show();
                                    Intent k = new Intent(LoginActivity.this, PatientActivity.class);
                                    startActivity(k);
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "Please use doctor log in!", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Invalid input, try again!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

        doctorLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser.logInInBackground(username.getText().toString(), password.getText().toString(), new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        if (e == null) {
                            if (user.getString("userType").equals("doctor")) {
                                Boolean isInfoComplete = user.getBoolean("isComplete");
                                if (isInfoComplete) {
                                    // go to DoctorActivity2 to make diagnosis
                                    String toDisplay = "Welcome back Dr." + user.getString("lastName") + "!";
                                    Toast.makeText(getApplicationContext(),toDisplay, Toast.LENGTH_LONG).show();
                                    Intent k = new Intent(LoginActivity.this, DoctorActivity2.class);
                                    startActivity(k);
                                } else {
                                    // go to DoctorActivity to set up profile
                                    Toast.makeText(getApplicationContext(), "Empty userInfo!", Toast.LENGTH_LONG).show();
                                    Intent k = new Intent(LoginActivity.this, DoctorActivity.class);
                                    startActivity(k);
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "Please use patient log in!", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Invalid input, try again!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

    }
}
