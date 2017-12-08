package com.example.samx.dermaspot;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.BitmapFactory;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class PatientActivity2 extends AppCompatActivity {
    TextView name, aage, gendr, filename, comment;
    Button logout, dermaspot;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient2);
        name = (TextView) findViewById(R.id.textView3);
        gendr = (TextView) findViewById(R.id.textView6);
        comment = (TextView) findViewById(R.id.textView13);
        aage = (TextView) findViewById(R.id.textView8);
        logout = (Button) findViewById(R.id.button4);
        dermaspot=findViewById(R.id.button6);
        imageView = findViewById(R.id.imageView4);

        dermaspot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent derma = new Intent (PatientActivity2.this, CaptureDetectActivity.class);
                startActivity(derma);
            }
        });

        final ParseUser user = ParseUser.getCurrentUser();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Comment");
        query.whereEqualTo("userId", user.getObjectId());
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(final List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    if (objects.size() != 0) {
                        user.put("comment", objects.get(0).getString("content"));
                        comment.setText(user.getString("comment"));
                    } else {
                        user.put("comment", "N/A");
                        comment.setText(user.getString("comment"));
                    }
                } else {
                    // Something went wrong.
                    Toast.makeText(getApplicationContext(), "No user found!", Toast.LENGTH_LONG).show();
                }
            }
        });

        name.setText(user.getString("firstName") + " " + user.getString("lastName"));
        aage.setText(user.getNumber("age").toString());
        gendr.setText(user.getString("gender"));
        ParseQuery<ParseObject> imageQuery = ParseQuery.getQuery("Image");
        imageQuery.whereEqualTo("userId", user.getObjectId());
        imageQuery.findInBackground(new FindCallback<ParseObject>() {
            public void done(final List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    if (objects.size() != 0) {
                        ParseFile img = objects.get(0).getParseFile("filename");
                        user.put("picture", img);
                        loadImages( img, imageView);
                    } else {
                        user.put("comment", "N/A");
                        comment.setText(user.getString("comment"));
                    }
                } else {
                    // Something went wrong.
                    Toast.makeText(getApplicationContext(), "No user found!", Toast.LENGTH_LONG).show();
                }
            }
        });


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseUser.logOut();
                //ParseUser currentUser = ParseUser.getCurrentUser(); // this will now be null
                Intent k = new Intent(PatientActivity2.this, LoginActivity.class);
                startActivity(k);
            }
        });
    }
    private void loadImages(ParseFile thumbnail, final ImageView img) {

        if (thumbnail != null) {
            thumbnail.getDataInBackground(new GetDataCallback() {
                @Override
                public void done(byte[] data, ParseException e) {
                    if (e == null) {
                        Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
                        img.setImageBitmap(bmp);
                    } else {
                    }
                }
            });
        } else {
            img.setImageResource(R.drawable.logo);
        }
    }// load image
}


