package com.example.finalprojectandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class Yoga extends AppCompatActivity {

    private CheckBox checkBoxMeditation, checkBoxSuryaNamaskar, checkBoxTreePose, checkBoxCobraPose, checkBoxShukhasana, checkBoxShabasana;
    private EditText editTextCalories;
    private Button buttonCalculate, buttonMain, btnReset;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yoga);

        checkBoxMeditation = findViewById(R.id.checkBoxMeditation);
        checkBoxSuryaNamaskar = findViewById(R.id.checkBoxSuryaNamaskar);
        checkBoxTreePose = findViewById(R.id.checkBoxTreePose);
        checkBoxCobraPose = findViewById(R.id.checkBoxCobraPose);
        checkBoxShukhasana = findViewById(R.id.checkBoxShukhasana);
        checkBoxShabasana = findViewById(R.id.checkBoxShabasana);
        editTextCalories = findViewById(R.id.editTextCaloriesYoga);
        buttonCalculate = findViewById(R.id.caloriesCalculateYoga);
        buttonMain = findViewById(R.id.buttonMainYoga);
        btnReset = findViewById(R.id.buttonResetYoga);

        // Initialize DatabaseHelper
        databaseHelper = new DatabaseHelper(this);

        buttonCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateCalories();
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkBoxMeditation.setChecked(false);
                checkBoxSuryaNamaskar.setChecked(false);
                checkBoxTreePose.setChecked(false);
                checkBoxCobraPose.setChecked(false);
                checkBoxShukhasana.setChecked(false);
                checkBoxShabasana.setChecked(false);
                editTextCalories.setText("");
            }
        });

        buttonMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Yoga.this, Main.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void calculateCalories() {
        int caloriesBurned = 0;

        if (checkBoxMeditation.isChecked()) {
            caloriesBurned += 20; // Example value for Meditation
        }
        if (checkBoxSuryaNamaskar.isChecked()) {
            caloriesBurned += 50; // Example value for Surya Namaskar
        }
        if (checkBoxTreePose.isChecked()) {
            caloriesBurned += 30; // Example value for Tree Pose
        }
        if (checkBoxCobraPose.isChecked()) {
            caloriesBurned += 25; // Example value for Cobra Pose
        }
        if (checkBoxShukhasana.isChecked()) {
            caloriesBurned += 15; // Example value for Shukhasana
        }
        if (checkBoxShabasana.isChecked()) {
            caloriesBurned += 10; // Example value for Shabasana
        }

        editTextCalories.setText(String.valueOf(caloriesBurned));

        // Save the calories to the database
        boolean isInserted = databaseHelper.insertCalories(caloriesBurned);
        if (isInserted) {
            Toast.makeText(this, "Calories saved", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Failed to save calories", Toast.LENGTH_SHORT).show();
        }

        // Clear the EditText field
        editTextCalories.setText("");
    }

    private class DatabaseHelper extends SQLiteOpenHelper {
        private static final String DATABASE_NAME = "calories.db";
        private static final int DATABASE_VERSION = 1;
        private static final String TABLE_NAME = "calories_table";
        private static final String COLUMN_ID = "id";
        private static final String COLUMN_CALORIES = "calories";

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_CALORIES + " INTEGER)";
            db.execSQL(createTable);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }

        public boolean insertCalories(int calories) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMN_CALORIES, calories);

            long result = db.insert(TABLE_NAME, null, contentValues);
            return result != -1;
        }
    }
}
