package com.example.careernest;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CareersActivity extends AppCompatActivity {

    private EditText edtSearchCareer;

    private LinearLayout cardSoftware;
    private LinearLayout cardAI;
    private LinearLayout cardData;
    private LinearLayout cardCyber;
    private LinearLayout cardCloud;
    private LinearLayout cardAndroid;
    private LinearLayout cardUIUX;
    private LinearLayout cardDatabase;

    private LinearLayout navHome;
    private LinearLayout navCareers;
    private LinearLayout navRoadmap;
    private LinearLayout navProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        ThemeManager.applySavedTheme(this);

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_careers);

        edtSearchCareer =
                findViewById(R.id.edtSearchCareer);

        cardSoftware =
                findViewById(R.id.cardSoftware);

        cardAI =
                findViewById(R.id.cardAI);

        cardData =
                findViewById(R.id.cardData);

        cardCyber =
                findViewById(R.id.cardCyber);

        cardCloud =
                findViewById(R.id.cardCloud);

        cardAndroid =
                findViewById(R.id.cardAndroid);

        cardUIUX =
                findViewById(R.id.cardUIUX);

        cardDatabase =
                findViewById(R.id.cardDatabase);

        navHome =
                findViewById(R.id.navHome);

        navCareers =
                findViewById(R.id.navCareers);

        navRoadmap =
                findViewById(R.id.navRoadmap);

        navProfile =
                findViewById(R.id.navProfile);


        // Career cards

        cardSoftware.setOnClickListener(v ->
                openCareerDetails(
                        "Software Developer"
                ));

        cardAI.setOnClickListener(v ->
                openCareerDetails(
                        "AI / ML Engineer"
                ));

        cardData.setOnClickListener(v ->
                openCareerDetails(
                        "Data Analyst"
                ));

        cardCyber.setOnClickListener(v ->
                openCareerDetails(
                        "Cybersecurity"
                ));

        cardCloud.setOnClickListener(v ->
                openCareerDetails(
                        "Cloud Engineer"
                ));

        cardAndroid.setOnClickListener(v ->
                openCareerDetails(
                        "Android Developer"
                ));

        cardUIUX.setOnClickListener(v ->
                openCareerDetails(
                        "UI / UX Designer"
                ));

        cardDatabase.setOnClickListener(v ->
                openCareerDetails(
                        "Database Administrator"
                ));


        // Home

        navHome.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            CareersActivity.this,
                            HomeActivity.class
                    );

            startActivity(intent);

            finish();
        });


        // Careers

        navCareers.setOnClickListener(v -> {

            Toast.makeText(
                    CareersActivity.this,
                    "You are already in Careers.",
                    Toast.LENGTH_SHORT
            ).show();
        });


        // Roadmap

        navRoadmap.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            CareersActivity.this,
                            CareerRoadmapActivity.class
                    );

            intent.putExtra(
                    "CAREER_NAME",
                    "Software Developer"
            );

            startActivity(intent);
        });


        // Profile

        navProfile.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            CareersActivity.this,
                            ProfileActivity.class
                    );

            startActivity(intent);
        });
    }


    private void openCareerDetails(
            String careerName
    ) {

        Intent intent =
                new Intent(
                        CareersActivity.this,
                        CareerDetailsActivity.class
                );

        intent.putExtra(
                "CAREER_NAME",
                careerName
        );

        startActivity(intent);
    }
}