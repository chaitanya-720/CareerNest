package com.example.careernest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    private LinearLayout cardCareerQuiz;

    private LinearLayout cardSoftware;
    private LinearLayout cardAI;
    private LinearLayout cardCyber;
    private LinearLayout cardCloud;

    private LinearLayout navHome;
    private LinearLayout navCareers;
    private LinearLayout navRoadmap;
    private LinearLayout navProfile;

    private Button btnTakeQuiz;

    // Database
    private DatabaseHelper databaseHelper;

    // Current logged-in user
    private int currentUserId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        ThemeManager.applySavedTheme(this);

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);

        // ==========================================
        // DATABASE
        // ==========================================

        databaseHelper =
                new DatabaseHelper(this);

        // ==========================================
        // GET CURRENT USER
        // ==========================================

        SharedPreferences session =
                getSharedPreferences(
                        "CareerNestSession",
                        MODE_PRIVATE
                );

        currentUserId =
                session.getInt(
                        "CURRENT_USER_ID",
                        -1
                );

        // ==========================================
        // QUIZ CARD
        // ==========================================

        cardCareerQuiz =
                findViewById(R.id.cardCareerQuiz);

        btnTakeQuiz =
                findViewById(R.id.btnTakeQuiz);

        // ==========================================
        // CAREER CARDS
        // ==========================================

        cardSoftware =
                findViewById(R.id.cardSoftware);

        cardAI =
                findViewById(R.id.cardAI);

        cardCyber =
                findViewById(R.id.cardCyber);

        cardCloud =
                findViewById(R.id.cardCloud);

        // ==========================================
        // BOTTOM NAVIGATION
        // ==========================================

        navHome =
                findViewById(R.id.navHome);

        navCareers =
                findViewById(R.id.navCareers);

        navRoadmap =
                findViewById(R.id.navRoadmap);

        navProfile =
                findViewById(R.id.navProfile);

        // ==========================================
        // CAREER QUIZ
        // ==========================================

        cardCareerQuiz.setOnClickListener(v -> {

            openCareerQuiz();

        });

        btnTakeQuiz.setOnClickListener(v -> {

            openCareerQuiz();

        });

        // ==========================================
        // CAREER CARDS
        // ==========================================

        cardSoftware.setOnClickListener(v -> {

            openCareerDetails(
                    "Software Developer"
            );

        });

        cardAI.setOnClickListener(v -> {

            openCareerDetails(
                    "AI / ML Engineer"
            );

        });

        cardCyber.setOnClickListener(v -> {

            openCareerDetails(
                    "Cybersecurity"
            );

        });

        cardCloud.setOnClickListener(v -> {

            openCareerDetails(
                    "Cloud Engineer"
            );

        });

        // ==========================================
        // HOME
        // ==========================================

        navHome.setOnClickListener(v -> {

            Toast.makeText(
                    HomeActivity.this,
                    "You are already on Home.",
                    Toast.LENGTH_SHORT
            ).show();

        });

        // ==========================================
        // CAREERS
        // ==========================================

        navCareers.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            HomeActivity.this,
                            CareersActivity.class
                    );

            startActivity(intent);

        });

        // ==========================================
        // ROADMAP
        // ==========================================

        navRoadmap.setOnClickListener(v -> {

            String lastCareer =
                    "Software Developer";

            if (currentUserId != -1) {

                lastCareer =
                        databaseHelper.getLastRoadmapCareer(
                                currentUserId
                        );
            }

            if (lastCareer == null ||
                    lastCareer.trim().isEmpty()) {

                lastCareer =
                        "Software Developer";
            }

            Intent intent =
                    new Intent(
                            HomeActivity.this,
                            CareerRoadmapActivity.class
                    );

            intent.putExtra(
                    "CAREER_NAME",
                    lastCareer
            );

            startActivity(intent);

        });

        // ==========================================
        // PROFILE
        // ==========================================

        navProfile.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            HomeActivity.this,
                            ProfileActivity.class
                    );

            startActivity(intent);

        });
    }

    // ==========================================
    // OPEN CAREER QUIZ
    // ==========================================

    private void openCareerQuiz() {

        Intent intent =
                new Intent(
                        HomeActivity.this,
                        CareerQuizActivity.class
                );

        startActivity(intent);
    }

    // ==========================================
    // OPEN CAREER DETAILS
    // ==========================================

    private void openCareerDetails(
            String careerName
    ) {

        Intent intent =
                new Intent(
                        HomeActivity.this,
                        CareerDetailsActivity.class
                );

        intent.putExtra(
                "CAREER_NAME",
                careerName
        );

        startActivity(intent);
    }
}