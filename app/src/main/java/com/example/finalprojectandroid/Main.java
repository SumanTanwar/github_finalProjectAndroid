package com.example.finalprojectandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Main extends AppCompatActivity {

    Button btnRunning, btnSkipping, btnSwimming,
            btnCycling, btnExercise, btnYoga,
            btnWeeklyReport;
    FirebaseAuth auth;
    TextView textView, txtSignOut, txtMyProfile, txtTemprature;
    FirebaseUser user;
    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseApp.initializeApp(this);

        auth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        user = auth.getCurrentUser();

        txtTemprature = findViewById(R.id.temprature);
        txtMyProfile = findViewById(R.id.my_profile);
        textView = findViewById(R.id.user_details);
        btnWeeklyReport = findViewById(R.id.weeklyreport);
        txtSignOut = findViewById(R.id.signout);
        btnRunning = findViewById(R.id.running);


        if (user == null) {
            Intent intent = new Intent(getApplicationContext(), login.class);
            startActivity(intent);
            finish();
        } else {

            mDatabase.child("users").child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String name = dataSnapshot.child("name").getValue(String.class);
                        String email = dataSnapshot.child("email").getValue(String.class);
                        String password = dataSnapshot.child("password").getValue(String.class);

                        textView.setText("Welcome " + name);

                        txtMyProfile.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(Main.this, MyProfile.class);
                                intent.putExtra("name", name);
                                intent.putExtra("email", email);
                                intent.putExtra("password", password);
                                startActivity(intent);
                                finish();
                            }
                        });
                    } else {
                        textView.setText("User");
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(Main.this, "Failed to fetch user data", Toast.LENGTH_SHORT).show();
                }
            });


            new FetchWeatherTask().execute("Montreal");
        }


        btnRunning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Main.this, RunningActivity.class);
                startActivity(intent);
            }
        });

        btnWeeklyReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Main.this, Report_Main.class);
                startActivity(intent);
            }
        });

        txtSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), login.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private class FetchWeatherTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String apiKey = "f64bf0682e08dc140031412b5a1c04ff"; // Replace with your actual API key
            String location = params[0];
            String apiURL = "https://api.openweathermap.org/data/2.5/weather?q=" + location + "&appid=" + apiKey;

            try {
                URL url = new URL(apiURL);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = connection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                bufferedReader.close();
                return stringBuilder.toString();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result != null) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONObject mainObject = jsonObject.getJSONObject("main");
                    double temp = mainObject.getDouble("temp");
                    double realTemp = temp - 273.15; // Convrsion of Klvin to Clsis......

                    JSONArray weatherArray = jsonObject.getJSONArray("weather");
                    JSONObject weatherObject = weatherArray.getJSONObject(0);
                    String weatherDesc = weatherObject.getString("description");

                    // Format temperature to a whole number
                    String weatherInfo = String.format("%.0f", realTemp) + " °C";
                    txtTemprature.setText(weatherInfo);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(Main.this, "Error parsing weather data", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(Main.this, "Failed to retrieve data", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // Handle back press if needed
    }
}
