package com.example.samx.dermaspot;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.*;

import java.util.List;

public class PatientDataActivity extends AppCompatActivity {
    TextView name, age, gender, pre_comment;
    EditText commentButton;
    ImageView pic;
    Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_data);
        name = (TextView) findViewById(R.id.textView25);
        gender = (TextView) findViewById(R.id.textView26);
        age = (TextView) findViewById(R.id.textView27);
        save = (Button) findViewById(R.id.button10);
        pre_comment = (TextView) findViewById(R.id.textView5);
        pic = (ImageView) findViewById(R.id.imageView2);
        commentButton = (EditText) findViewById(R.id.editText12);
        Intent k = getIntent();

        final String selectedUserId = k.getStringExtra("objectId");
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("objectId", selectedUserId);
        query.findInBackground(new FindCallback<ParseUser>() {
            public void done(final List<ParseUser> objects, ParseException e) {
                if (e == null) {
                    // The query was successful.
                    //Toast.makeText(getApplicationContext(), selectedUserId, Toast.LENGTH_LONG).show();
                    ParseUser selectedUser = objects.get(0);
                    name.setText(selectedUser.getString("firstName") + " " + selectedUser.getString("lastName"));
                    gender.setText(selectedUser.getString("gender"));
                    age.setText(selectedUser.getNumber("age").toString());
                    String s = selectedUser.getString("comment") == null ? "N/A"
                            : selectedUser.getString("comment");
                    pre_comment.setText(s);
                    save.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ParseObject comment = new ParseObject("Comment");
                            comment.put("content", commentButton.getText().toString());
                            comment.put("userId", selectedUserId);
                            comment.saveInBackground();
                        }
                    });
                } else {
                    // Something went wrong.
                    Toast.makeText(getApplicationContext(), "No user found!", Toast.LENGTH_LONG).show();
                }
            }
        });

        ParseQuery<ParseObject> imageQuery = ParseQuery.getQuery("Image");
        imageQuery.whereEqualTo("userId", selectedUserId);
        imageQuery.findInBackground(new FindCallback<ParseObject>() {
            public void done(final List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    if (objects.size() != 0) {
                        ParseFile img = objects.get(0).getParseFile("filename");

                        loadImages( img, pic);
                    } else {
                        Toast.makeText(getApplicationContext(), "Image not found!", Toast.LENGTH_LONG).show();
                    }
                } else {
                    // Something went wrong.
                    Toast.makeText(getApplicationContext(), "No user found!", Toast.LENGTH_LONG).show();
                }
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