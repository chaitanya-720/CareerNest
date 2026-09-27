package com.example.careernest;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class QuizResultActivity extends AppCompatActivity {

    private ImageButton btnBack;

    private TextView txtResultTitle;
    private TextView txtCareerIcon;
    private TextView txtRecommendedCareer;
    private TextView txtDescription;

    private Button btnExploreCareer;
    private TextView btnRetakeQuiz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        ThemeManager.applySavedTheme(this);

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_quiz_result);

        // ==============================
        // CONNECT UI COMPONENTS
        // ==============================

        btnBack = findViewById(R.id.btnBack);

        txtResultTitle = findViewById(R.id.txtResultTitle);
        txtCareerIcon = findViewById(R.id.txtCareerIcon);
        txtRecommendedCareer =
                findViewById(R.id.txtRecommendedCareer);
        txtDescription =
                findViewById(R.id.txtDescription);

        btnExploreCareer =
                findViewById(R.id.btnExploreCareer);

        btnRetakeQuiz =
                findViewById(R.id.btnRetakeQuiz);

        // ==============================
        // GET RECOMMENDED CAREER
        // ==============================

        String career = getIntent().getStringExtra(
                "RECOMMENDED_CAREER"
        );

        if (career == null || career.isEmpty()) {
            career = "Software Developer";
        }

        final String recommendedCareer = career;

        // ==============================
        // DISPLAY CAREER
        // ==============================

        txtResultTitle.setText("Your Career Match");

        txtRecommendedCareer.setText(
                recommendedCareer
        );

        // ==============================
        // CAREER INFORMATION
        // ==============================

        switch (recommendedCareer) {

            case "Software Developer":

                txtCareerIcon.setText("💻");

                txtDescription.setText(
                        "You may enjoy building applications, " +
                                "solving programming problems and creating " +
                                "software solutions."
                );

                break;

            case "Data Analyst":

                txtCareerIcon.setText("📊");

                txtDescription.setText(
                        "You may enjoy working with data, " +
                                "finding patterns and creating insights " +
                                "that support decision making."
                );

                break;

            case "Cybersecurity":

                txtCareerIcon.setText("🔐");

                txtDescription.setText(
                        "You may enjoy protecting systems, " +
                                "analyzing security problems and learning " +
                                "about networks and cybersecurity."
                );

                break;

            case "UI / UX Designer":

                txtCareerIcon.setText("🎨");

                txtDescription.setText(
                        "You may enjoy designing interfaces, " +
                                "understanding users and creating " +
                                "better digital experiences."
                );

                break;

            default:

                txtCareerIcon.setText("💼");

                txtDescription.setText(
                        "Explore this career to learn about " +
                                "its skills, technologies and opportunities."
                );

                break;
        }

        // ==============================
        // BACK
        // ==============================

        btnBack.setOnClickListener(v -> {
            finish();
        });

        // ==============================
        // EXPLORE CAREER
        // ==============================

        btnExploreCareer.setOnClickListener(v -> {

            // Save this career as the latest roadmap career
            getSharedPreferences("CareerNestProfile", MODE_PRIVATE)
                    .edit()
                    .putString("LAST_ROADMAP_CAREER", recommendedCareer)
                    .apply();

            Intent intent = new Intent(
                    QuizResultActivity.this,
                    CareerDetailsActivity.class
            );

            intent.putExtra(
                    "CAREER_NAME",
                    recommendedCareer
            );

            startActivity(intent);
        });

        // ==============================
        // RETAKE QUIZ
        // ==============================

        btnRetakeQuiz.setOnClickListener(v -> {

            Intent intent = new Intent(
                    QuizResultActivity.this,
                    CareerQuizActivity.class
            );

            startActivity(intent);

            finish();
        });
    }
}