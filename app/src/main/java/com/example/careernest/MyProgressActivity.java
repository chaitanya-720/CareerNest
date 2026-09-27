package com.example.careernest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MyProgressActivity extends AppCompatActivity {

    // ==========================================
    // UI COMPONENTS
    // ==========================================

    private ImageButton btnBack;

    private TextView txtCareerName;

    private TextView txtProgressPercentage;
    private TextView txtCompletedSteps;
    private TextView txtRemainingSteps;

    private ProgressBar progressOverall;

    private TextView txtNextStepNumber;
    private TextView txtNextStepTitle;
    private TextView txtNextStepDescription;

    private Button btnContinueLearning;

    private TextView txtStep1;
    private TextView txtStep2;
    private TextView txtStep3;
    private TextView txtStep4;
    private TextView txtStep5;
    private TextView txtStep6;

    private TextView txtCompletionMessage;

    // ==========================================
    // DATABASE
    // ==========================================

    private DatabaseHelper databaseHelper;

    // ==========================================
    // CURRENT USER
    // ==========================================

    private int currentUserId = -1;

    // ==========================================
    // CURRENT CAREER
    // ==========================================

    private String careerName;

    // ==========================================
    // CURRENT NEXT STEP
    // ==========================================

    private int nextStep = 0;

    // ==========================================
    // ON CREATE
    // ==========================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        ThemeManager.applySavedTheme(this);

        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_my_progress
        );

        // ==========================================
        // DATABASE
        // ==========================================

        databaseHelper =
                new DatabaseHelper(this);

        // ==========================================
        // GET CURRENT LOGGED-IN USER
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
        // CONNECT UI
        // ==========================================

        btnBack =
                findViewById(R.id.btnBack);

        txtCareerName =
                findViewById(R.id.txtCareerName);

        txtProgressPercentage =
                findViewById(R.id.txtProgressPercentage);

        txtCompletedSteps =
                findViewById(R.id.txtCompletedSteps);

        txtRemainingSteps =
                findViewById(R.id.txtRemainingSteps);

        progressOverall =
                findViewById(R.id.progressOverall);

        txtNextStepNumber =
                findViewById(R.id.txtNextStepNumber);

        txtNextStepTitle =
                findViewById(R.id.txtNextStepTitle);

        txtNextStepDescription =
                findViewById(R.id.txtNextStepDescription);

        btnContinueLearning =
                findViewById(R.id.btnContinueLearning);

        txtStep1 =
                findViewById(R.id.txtStep1);

        txtStep2 =
                findViewById(R.id.txtStep2);

        txtStep3 =
                findViewById(R.id.txtStep3);

        txtStep4 =
                findViewById(R.id.txtStep4);

        txtStep5 =
                findViewById(R.id.txtStep5);

        txtStep6 =
                findViewById(R.id.txtStep6);

        txtCompletionMessage =
                findViewById(R.id.txtCompletionMessage);

        // ==========================================
        // GET CURRENT CAREER
        // ==========================================

        careerName =
                getIntent().getStringExtra(
                        "CAREER_NAME"
                );

        /*
         * If My Progress was opened without a specific
         * career, get the last roadmap career from SQLite.
         */

        if (careerName == null ||
                careerName.trim().isEmpty()) {

            if (currentUserId != -1) {

                careerName =
                        databaseHelper.getLastRoadmapCareer(
                                currentUserId
                        );

            } else {

                careerName =
                        "Software Developer";
            }
        }

        if (careerName == null ||
                careerName.trim().isEmpty()) {

            careerName =
                    "Software Developer";
        }

        txtCareerName.setText(
                careerName
        );

        // ==========================================
        // BACK BUTTON
        // ==========================================

        btnBack.setOnClickListener(v ->
                finish()
        );

        // ==========================================
        // CONTINUE LEARNING
        // ==========================================

        btnContinueLearning.setOnClickListener(v -> {

            if (nextStep > 0) {

                openLearningResources(
                        nextStep
                );
            }
        });

        // ==========================================
        // LOAD PROGRESS
        // ==========================================

        loadProgress();
    }

    // ==========================================
    // ON RESUME
    // ==========================================

    @Override
    protected void onResume() {

        super.onResume();

        if (databaseHelper != null) {

            loadProgress();
        }
    }

    // ==========================================
    // LOAD CURRENT CAREER PROGRESS
    // ==========================================

    private void loadProgress() {

        // ------------------------------------------
        // If no user is logged in
        // ------------------------------------------

        if (currentUserId == -1) {

            txtProgressPercentage.setText("0%");

            txtCompletedSteps.setText("0");

            txtRemainingSteps.setText("6");

            progressOverall.setMax(100);

            progressOverall.setProgress(0);

            boolean step1 = false;
            boolean step2 = false;
            boolean step3 = false;
            boolean step4 = false;
            boolean step5 = false;
            boolean step6 = false;

            nextStep =
                    findNextIncompleteStep(
                            step1,
                            step2,
                            step3,
                            step4,
                            step5,
                            step6
                    );

            updateNextStep();

            updateStep(
                    txtStep1,
                    "1. Programming Basics",
                    false
            );

            updateStep(
                    txtStep2,
                    "2. Data Structures & Algorithms",
                    false
            );

            updateStep(
                    txtStep3,
                    "3. Development Tools",
                    false
            );

            updateStep(
                    txtStep4,
                    "4. Build Real Projects",
                    false
            );

            updateStep(
                    txtStep5,
                    "5. Internship Preparation",
                    false
            );

            updateStep(
                    txtStep6,
                    "6. Job Preparation",
                    false
            );

            return;
        }

        // ==========================================
        // GET STEP STATUS FROM SQLITE
        // ==========================================

        boolean step1 =
                isStepCompleted(1);

        boolean step2 =
                isStepCompleted(2);

        boolean step3 =
                isStepCompleted(3);

        boolean step4 =
                isStepCompleted(4);

        boolean step5 =
                isStepCompleted(5);

        boolean step6 =
                isStepCompleted(6);

        // ==========================================
        // COUNT COMPLETED STEPS
        // ==========================================

        int completed = 0;

        if (step1) {
            completed++;
        }

        if (step2) {
            completed++;
        }

        if (step3) {
            completed++;
        }

        if (step4) {
            completed++;
        }

        if (step5) {
            completed++;
        }

        if (step6) {
            completed++;
        }

        // ==========================================
        // CALCULATE PROGRESS
        // ==========================================

        int progress =
                (completed * 100) / 6;

        int remaining =
                6 - completed;

        // ==========================================
        // UPDATE SUMMARY
        // ==========================================

        txtProgressPercentage.setText(
                progress + "%"
        );

        txtCompletedSteps.setText(
                String.valueOf(completed)
        );

        txtRemainingSteps.setText(
                String.valueOf(remaining)
        );

        progressOverall.setMax(100);

        progressOverall.setProgress(
                progress
        );

        // ==========================================
        // FIND NEXT INCOMPLETE STEP
        // ==========================================

        nextStep =
                findNextIncompleteStep(
                        step1,
                        step2,
                        step3,
                        step4,
                        step5,
                        step6
                );

        // ==========================================
        // UPDATE NEXT STEP
        // ==========================================

        updateNextStep();

        // ==========================================
        // UPDATE ROADMAP OVERVIEW
        // ==========================================

        updateStep(
                txtStep1,
                "1. Programming Basics",
                step1
        );

        updateStep(
                txtStep2,
                "2. Data Structures & Algorithms",
                step2
        );

        updateStep(
                txtStep3,
                "3. Development Tools",
                step3
        );

        updateStep(
                txtStep4,
                "4. Build Real Projects",
                step4
        );

        updateStep(
                txtStep5,
                "5. Internship Preparation",
                step5
        );

        updateStep(
                txtStep6,
                "6. Job Preparation",
                step6
        );
    }

    // ==========================================
    // FIND FIRST INCOMPLETE STEP
    // ==========================================

    private int findNextIncompleteStep(
            boolean step1,
            boolean step2,
            boolean step3,
            boolean step4,
            boolean step5,
            boolean step6
    ) {

        if (!step1) {
            return 1;
        }

        if (!step2) {
            return 2;
        }

        if (!step3) {
            return 3;
        }

        if (!step4) {
            return 4;
        }

        if (!step5) {
            return 5;
        }

        if (!step6) {
            return 6;
        }

        return 0;
    }

    // ==========================================
    // UPDATE NEXT STEP
    // ==========================================

    private void updateNextStep() {

        if (nextStep == 0) {

            txtNextStepNumber.setText(
                    "🎉 ROADMAP COMPLETE"
            );

            txtNextStepTitle.setText(
                    "Congratulations!"
            );

            txtNextStepDescription.setText(
                    "You have completed all six steps of your " +
                            careerName +
                            " roadmap."
            );

            btnContinueLearning.setText(
                    "Roadmap Completed"
            );

            btnContinueLearning.setEnabled(
                    false
            );

            txtCompletionMessage.setVisibility(
                    TextView.VISIBLE
            );

            return;
        }

        // ==========================================
        // ROADMAP NOT COMPLETE
        // ==========================================

        txtCompletionMessage.setVisibility(
                TextView.GONE
        );

        btnContinueLearning.setEnabled(
                true
        );

        btnContinueLearning.setText(
                "📚  Learn Resources"
        );

        switch (nextStep) {

            case 1:

                txtNextStepNumber.setText(
                        "STEP 1 • START HERE"
                );

                txtNextStepTitle.setText(
                        "Programming Basics"
                );

                txtNextStepDescription.setText(
                        "Learn variables, conditions, loops, " +
                                "functions and object-oriented programming."
                );

                break;

            case 2:

                txtNextStepNumber.setText(
                        "STEP 2 • NEXT"
                );

                txtNextStepTitle.setText(
                        "Data Structures & Algorithms"
                );

                txtNextStepDescription.setText(
                        "Build your problem-solving skills with " +
                                "arrays, strings, linked lists, stacks, " +
                                "queues and algorithms."
                );

                break;

            case 3:

                txtNextStepNumber.setText(
                        "STEP 3 • NEXT"
                );

                txtNextStepTitle.setText(
                        "Development Tools"
                );

                txtNextStepDescription.setText(
                        "Learn Git, GitHub, databases, APIs and " +
                                "modern development workflows."
                );

                break;

            case 4:

                txtNextStepNumber.setText(
                        "STEP 4 • NEXT"
                );

                txtNextStepTitle.setText(
                        "Build Real Projects"
                );

                txtNextStepDescription.setText(
                        "Create practical projects that demonstrate " +
                                "your programming and problem-solving skills."
                );

                break;

            case 5:

                txtNextStepNumber.setText(
                        "STEP 5 • NEXT"
                );

                txtNextStepTitle.setText(
                        "Internship Preparation"
                );

                txtNextStepDescription.setText(
                        "Prepare your resume, GitHub portfolio " +
                                "and internship applications."
                );

                break;

            case 6:

                txtNextStepNumber.setText(
                        "STEP 6 • FINAL"
                );

                txtNextStepTitle.setText(
                        "Job Preparation"
                );

                txtNextStepDescription.setText(
                        "Prepare for technical interviews, " +
                                "aptitude tests and HR interviews."
                );

                break;
        }
    }

    // ==========================================
    // OPEN LEARNING RESOURCES
    // ==========================================

    private void openLearningResources(
            int stepNumber
    ) {

        Intent intent =
                new Intent(
                        MyProgressActivity.this,
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

    // ==========================================
    // CHECK STEP COMPLETION FROM SQLITE
    // ==========================================

    private boolean isStepCompleted(
            int stepNumber
    ) {

        if (currentUserId == -1) {
            return false;
        }

        return databaseHelper.isStepCompleted(
                currentUserId,
                careerName,
                stepNumber
        );
    }

    // ==========================================
    // UPDATE STEP STATUS
    // ==========================================

    private void updateStep(
            TextView textView,
            String stepName,
            boolean completed
    ) {

        if (completed) {

            textView.setText(
                    "✓  " +
                            stepName +
                            "     Completed"
            );

            textView.setTextColor(
                    getResources().getColor(
                            R.color.success
                    )
            );

        } else {

            textView.setText(
                    "○  " +
                            stepName +
                            "     Not started"
            );

            textView.setTextColor(
                    getResources().getColor(
                            R.color.text_secondary
                    )
            );
        }
    }
}