package com.example.samx.dermaspot;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseAnalytics;
import com.parse.ParseUser;

public class MainActivity extends AppCompatActivity {
    private final int SPLASH_DISPLAY_LENGTH = 2000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // parse server setup
        Parse.enableLocalDatastore(this);
        Parse.initialize(new Parse.Configuration.Builder(getApplicationContext())
                .applicationId( "e890aec3a736beec457250acc45e48c50244b409")
                .clientKey("10a848c84c6076e3fdb2eba5074837b4ffaf9f32")
                .server("http://34.227.172.130:80/parse/")
                .build()
        );
        ParseAnalytics.trackAppOpenedInBackground(getIntent());
        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();
        defaultACL.setPublicReadAccess(true);
        defaultACL.setPublicWriteAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);
        new Handler().postDelayed((new Runnable() {
            @Override
            public void run() {
                Intent k = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(k);
                finish();
            }
        }), SPLASH_DISPLAY_LENGTH);

    }
}
