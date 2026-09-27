package com.example.careernest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CareerRoadmapActivity extends AppCompatActivity {

    private ImageButton btnBack;
    private TextView txtCareerName;
    private TextView txtProgress;
    private ProgressBar progressRoadmap;

    private Button btnLearn1;
    private Button btnStep1;
    private Button btnLearn2;
    private Button btnStep2;
    private Button btnLearn3;
    private Button btnStep3;
    private Button btnLearn4;
    private Button btnStep4;
    private Button btnLearn5;
    private Button btnStep5;
    private Button btnLearn6;
    private Button btnStep6;

    private DatabaseHelper databaseHelper;

    private int currentUserId = -1;
    private int completedSteps = 0;

    private String careerName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        ThemeManager.applySavedTheme(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_career_roadmap);

        // ---------------------------------------------------------
        // DATABASE
        // ---------------------------------------------------------

        databaseHelper = new DatabaseHelper(this);

        // ---------------------------------------------------------
        // CURRENT LOGGED-IN USER
        // ---------------------------------------------------------

        SharedPreferences session =
                getSharedPreferences("CareerNestSession", MODE_PRIVATE);

        currentUserId =
                session.getInt("CURRENT_USER_ID", -1);

        // ---------------------------------------------------------
        // FIND VIEWS
        // ---------------------------------------------------------

        btnBack = findViewById(R.id.btnBack);

        txtCareerName = findViewById(R.id.txtCareerName);
        txtProgress = findViewById(R.id.txtProgress);
        progressRoadmap = findViewById(R.id.progressRoadmap);

        btnLearn1 = findViewById(R.id.btnLearn1);
        btnStep1 = findViewById(R.id.btnStep1);

        btnLearn2 = findViewById(R.id.btnLearn2);
        btnStep2 = findViewById(R.id.btnStep2);

        btnLearn3 = findViewById(R.id.btnLearn3);
        btnStep3 = findViewById(R.id.btnStep3);

        btnLearn4 = findViewById(R.id.btnLearn4);
        btnStep4 = findViewById(R.id.btnStep4);

        btnLearn5 = findViewById(R.id.btnLearn5);
        btnStep5 = findViewById(R.id.btnStep5);

        btnLearn6 = findViewById(R.id.btnLearn6);
        btnStep6 = findViewById(R.id.btnStep6);

        // ---------------------------------------------------------
        // GET CAREER NAME
        // ---------------------------------------------------------

        careerName =
                getIntent().getStringExtra("CAREER_NAME");

        if (careerName == null ||
                careerName.trim().isEmpty()) {

            if (currentUserId != -1) {

                careerName =
                        databaseHelper.getLastRoadmapCareer(
                                currentUserId
                        );

            } else {

                careerName = "Software Developer";
            }
        }

        if (careerName == null ||
                careerName.trim().isEmpty()) {

            careerName = "Software Developer";
        }

        txtCareerName.setText(careerName);

        // ---------------------------------------------------------
        // PROGRESS BAR
        // ---------------------------------------------------------

        progressRoadmap.setMax(100);

        // Load progress from SQLite
        loadProgress();

        // ---------------------------------------------------------
        // BACK BUTTON
        // ---------------------------------------------------------

        btnBack.setOnClickListener(v -> finish());

        // ---------------------------------------------------------
        // LEARNING RESOURCE BUTTONS
        // ---------------------------------------------------------

        btnLearn1.setOnClickListener(
                v -> openLearningResources(1)
        );

        btnLearn2.setOnClickListener(
                v -> openLearningResources(2)
        );

        btnLearn3.setOnClickListener(
                v -> openLearningResources(3)
        );

        btnLearn4.setOnClickListener(
                v -> openLearningResources(4)
        );

        btnLearn5.setOnClickListener(
                v -> openLearningResources(5)
        );

        btnLearn6.setOnClickListener(
                v -> openLearningResources(6)
        );

        // ---------------------------------------------------------
        // ROADMAP STEP BUTTONS
        // ---------------------------------------------------------

        btnStep1.setOnClickListener(
                v -> toggleStep(btnStep1, 1)
        );

        btnStep2.setOnClickListener(
                v -> toggleStep(btnStep2, 2)
        );

        btnStep3.setOnClickListener(
                v -> toggleStep(btnStep3, 3)
        );

        btnStep4.setOnClickListener(
                v -> toggleStep(btnStep4, 4)
        );

        btnStep5.setOnClickListener(
                v -> toggleStep(btnStep5, 5)
        );

        btnStep6.setOnClickListener(
                v -> toggleStep(btnStep6, 6)
        );
    }

    // =============================================================
    // RELOAD PROGRESS WHEN ACTIVITY RESUMES
    // =============================================================

    @Override
    protected void onResume() {
        super.onResume();

        if (databaseHelper != null) {
            loadProgress();
        }
    }

    // =============================================================
    // OPEN LEARNING RESOURCES
    // =============================================================

    private void openLearningResources(int stepNumber) {

        Intent intent = new Intent(
                CareerRoadmapActivity.this,
                LearningResourcesActivity.class
        );

        intent.putExtra(
                "CAREER_NAME",
                careerName
        );

        intent.putExtra(
                "STEP_NUMBER",
                stepNumber
        );

        startActivity(intent);
    }

    // =============================================================
    // TOGGLE ROADMAP STEP
    // =============================================================

    private void toggleStep(
            Button button,
            int stepNumber
    ) {

        if (currentUserId == -1) {

            Toast.makeText(
                    CareerRoadmapActivity.this,
                    "Please login first.",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        // Check current status from SQLite
        boolean isCompleted =
                databaseHelper.isStepCompleted(
                        currentUserId,
                        careerName,
                        stepNumber
                );

        // Change status
        databaseHelper.setStepCompleted(
                currentUserId,
                careerName,
                stepNumber,
                !isCompleted
        );

        // Reload UI
        loadProgress();
    }

    // =============================================================
    // LOAD ALL ROADMAP PROGRESS
    // =============================================================

    private void loadProgress() {

        completedSteps = 0;

        loadStep(btnStep1, 1);
        loadStep(btnStep2, 2);
        loadStep(btnStep3, 3);
        loadStep(btnStep4, 4);
        loadStep(btnStep5, 5);
        loadStep(btnStep6, 6);

        updateProgress();
    }

    // =============================================================
    // LOAD INDIVIDUAL STEP
    // =============================================================

    private void loadStep(
            Button button,
            int stepNumber
    ) {

        boolean completed = false;

        if (currentUserId != -1) {

            completed =
                    databaseHelper.isStepCompleted(
                            currentUserId,
                            careerName,
                            stepNumber
                    );
        }

        if (completed) {

            // -----------------------------------------------------
            // COMPLETED STATE
            // -----------------------------------------------------

            button.setText("✓ Completed");

            button.setTextColor(Color.WHITE);

            button.setTextSize(16);

            button.setGravity(Gravity.CENTER);

            button.setBackground(
                    createButtonBackground(
                            Color.rgb(22, 179, 100),
                            Color.rgb(22, 179, 100)
                    )
            );

            button.setTag("completed");

            completedSteps++;

        } else {

            // -----------------------------------------------------
            // NOT COMPLETED STATE
            // -----------------------------------------------------

            button.setText("Mark as Complete");

            button.setTextColor(
                    getResources().getColor(
                            R.color.text_primary
                    )
            );

            button.setTextSize(16);

            button.setGravity(Gravity.CENTER);

            button.setBackground(
                    createButtonBackground(
                            getResources().getColor(
                                    R.color.surface
                            ),
                            getResources().getColor(
                                    R.color.primary
                            )
                    )
            );

            button.setTag(null);
        }
    }

    // =============================================================
    // CREATE BUTTON BACKGROUND
    // =============================================================

    private GradientDrawable createButtonBackground(
            int backgroundColor,
            int borderColor
    ) {

        GradientDrawable drawable =
                new GradientDrawable();

        drawable.setShape(
                GradientDrawable.RECTANGLE
        );

        drawable.setColor(backgroundColor);

        drawable.setCornerRadius(
                12 *
                        getResources()
                                .getDisplayMetrics()
                                .density
        );

        drawable.setStroke(
                (int) (
                        1 *
                                getResources()
                                        .getDisplayMetrics()
                                        .density
                ),
                borderColor
        );

        return drawable;
    }

    // =============================================================
    // UPDATE PROGRESS
    // =============================================================

    private void updateProgress() {

        int progress =
                (completedSteps * 100) / 6;

        progressRoadmap.setProgress(progress);

        txtProgress.setText(
                progress + "% Complete"
        );
    }
}