package com.example.samx.dermaspot;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class DoctorActivity2 extends AppCompatActivity {
    ListView listView;
    Button logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor2);

        logout = findViewById(R.id.button5);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseUser.logOut();
                Intent home = new Intent(DoctorActivity2.this, LoginActivity.class);
                startActivity(home);
                finish();
            }
        });
        final Context _this = this;
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("userType", "patient");
        query.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> objects, ParseException e) {
                if (e == null) {
                    // The query was successful.
                    ListView listView = (ListView) findViewById(R.id.ListView);
                    final String[] patients = new String[objects.size()];
                    final String[] pID = new String[objects.size()];
                    for (int i = 0; i < objects.size(); i++) {
                        pID[i] = objects.get(i).getObjectId();
                        patients[i] = objects.get(i).getString("firstName") + " " +
                                objects.get(i).getString("lastName");
                    }
                    ArrayAdapter adapter = new ArrayAdapter(_this, android.R.layout.simple_list_item_1, patients);
                    listView.setAdapter(adapter);

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            // 1
                            String selectedUserId = pID[position];

                            // 2
                            Intent k = new Intent(_this, PatientDataActivity.class);

                            // 3
                            k.putExtra("objectId", selectedUserId);

                            // 4
                            startActivity(k);
                        }

                    });
                } else {
                    // Something went wrong.
                }
            }
        });
        //final ParseUser user = ParseUser.getCurrentUser();

    }
}
