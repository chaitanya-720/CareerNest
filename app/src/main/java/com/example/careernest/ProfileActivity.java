package com.example.careernest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {

    private ImageButton btnBack;

    private TextView txtProfileName;
    private TextView txtProfileEmail;

    private LinearLayout cardEditProfile;
    private LinearLayout cardSavedCareers;
    private LinearLayout cardMyProgress;
    private LinearLayout cardCareerQuiz;
    private LinearLayout cardSettings;

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        ThemeManager.applySavedTheme(this);

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_profile);

        // ==========================================
        // DATABASE
        // ==========================================

        databaseHelper = new DatabaseHelper(this);

        // ==========================================
        // CONNECT UI
        // ==========================================

        btnBack = findViewById(R.id.btnBack);

        txtProfileName = findViewById(R.id.txtProfileName);

        txtProfileEmail = findViewById(R.id.txtProfileEmail);

        cardEditProfile = findViewById(R.id.cardEditProfile);

        cardSavedCareers = findViewById(R.id.cardSavedCareers);

        cardMyProgress = findViewById(R.id.cardMyProgress);

        cardCareerQuiz = findViewById(R.id.cardCareerQuiz);

        cardSettings = findViewById(R.id.cardSettings);

        // ==========================================
        // BACK
        // ==========================================

        btnBack.setOnClickListener(v -> finish());

        // ==========================================
        // EDIT PROFILE
        // ==========================================

        cardEditProfile.setOnClickListener(v -> {

            Intent intent = new Intent(
                    ProfileActivity.this,
                    EditProfileActivity.class
            );

            startActivity(intent);
        });

        // ==========================================
        // SAVED CAREERS
        // ==========================================

        cardSavedCareers.setOnClickListener(v -> {

            Intent intent = new Intent(
                    ProfileActivity.this,
                    SavedCareersActivity.class
            );

            startActivity(intent);
        });

        // ==========================================
        // MY PROGRESS
        // ==========================================

        cardMyProgress.setOnClickListener(v -> {

            Intent intent = new Intent(
                    ProfileActivity.this,
                    MyProgressActivity.class
            );

            startActivity(intent);
        });

        // ==========================================
        // CAREER QUIZ
        // ==========================================

        cardCareerQuiz.setOnClickListener(v -> {

            Intent intent = new Intent(
                    ProfileActivity.this,
                    CareerQuizActivity.class
            );

            startActivity(intent);
        });

        // ==========================================
        // SETTINGS
        // ==========================================

        cardSettings.setOnClickListener(v -> {

            Intent intent = new Intent(
                    ProfileActivity.this,
                    SettingsActivity.class
            );

            startActivity(intent);
        });

        // ==========================================
        // LOAD PROFILE
        // ==========================================

        loadProfile();
    }

    // ==========================================
    // REFRESH PROFILE
    // ==========================================

    @Override
    protected void onResume() {

        super.onResume();

        loadProfile();
    }

    // ==========================================
    // LOAD PROFILE FROM SQLITE
    // ==========================================

    private void loadProfile() {

        SharedPreferences session =
                getSharedPreferences(
                        "CareerNestSession",
                        MODE_PRIVATE
                );

        int currentUserId =
                session.getInt(
                        "CURRENT_USER_ID",
                        -1
                );

        // ==========================================
        // NO USER LOGGED IN
        // ==========================================

        if (currentUserId == -1) {

            txtProfileName.setText("CareerNest User");
            txtProfileEmail.setText("Add your email");

            return;
        }

        // ==========================================
        // GET USER FROM DATABASE
        // ==========================================

        Cursor cursor =
                databaseHelper.getUser(currentUserId);

        if (cursor != null && cursor.moveToFirst()) {

            String name =
                    cursor.getString(
                            cursor.getColumnIndexOrThrow("name")
                    );

            String email =
                    cursor.getString(
                            cursor.getColumnIndexOrThrow("email")
                    );

            txtProfileName.setText(name);
            txtProfileEmail.setText(email);
        }

        if (cursor != null) {
            cursor.close();
        }
    }
}