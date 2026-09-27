package com.example.careernest;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CareerQuizActivity extends AppCompatActivity {

    // ==========================================
    // UI COMPONENTS
    // ==========================================

    private ImageButton btnBack;

    private TextView txtQuestionNumber;
    private TextView txtQuestion;

    private RadioGroup radioGroup;

    private RadioButton option1;
    private RadioButton option2;
    private RadioButton option3;
    private RadioButton option4;

    private TextView btnPrevious;
    private Button btnNext;

    // ==========================================
    // QUIZ VARIABLES
    // ==========================================

    private int currentQuestion = 0;

    private int[] answers = new int[5];

    // ==========================================
    // QUESTIONS
    // ==========================================

    private final String[] questions = {

            "Which activity interests you the most?",

            "Which subject do you enjoy the most?",

            "What type of problem do you prefer solving?",

            "Which technology would you like to learn?",

            "What kind of project would you like to build?"
    };

    // ==========================================
    // OPTIONS
    // ==========================================

    private final String[][] options = {

            {
                    "Building apps or websites",
                    "Analyzing data",
                    "Protecting systems",
                    "Designing interfaces"
            },

            {
                    "Programming",
                    "Mathematics and Data",
                    "Networking and Security",
                    "Design"
            },

            {
                    "Coding problems",
                    "Data and business problems",
                    "Security problems",
                    "User experience problems"
            },

            {
                    "Java / Python",
                    "SQL / Power BI",
                    "Linux / Wireshark",
                    "Figma"
            },

            {
                    "Mobile or Web Application",
                    "Data Dashboard",
                    "Security System",
                    "UI/UX Prototype"
            }
    };

    // ==========================================
    // ON CREATE
    // ==========================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_career_quiz);

        // ==========================================
        // CONNECT UI
        // ==========================================

        btnBack = findViewById(R.id.btnBack);

        txtQuestionNumber =
                findViewById(R.id.txtQuestionNumber);

        txtQuestion =
                findViewById(R.id.txtQuestion);

        radioGroup =
                findViewById(R.id.radioGroup);

        option1 =
                findViewById(R.id.option1);

        option2 =
                findViewById(R.id.option2);

        option3 =
                findViewById(R.id.option3);

        option4 =
                findViewById(R.id.option4);

        btnPrevious =
                findViewById(R.id.btnPrevious);

        btnNext =
                findViewById(R.id.btnNext);

        // ==========================================
        // INITIALIZE ANSWERS
        // ==========================================

        for (int i = 0; i < answers.length; i++) {
            answers[i] = -1;
        }

        // ==========================================
        // LOAD FIRST QUESTION
        // ==========================================

        loadQuestion();

        // ==========================================
        // BACK
        // ==========================================

        btnBack.setOnClickListener(v -> {
            finish();
        });

        // ==========================================
        // PREVIOUS
        // ==========================================

        btnPrevious.setOnClickListener(v -> {

            saveCurrentAnswer();

            if (currentQuestion > 0) {

                currentQuestion--;

                loadQuestion();
            }
        });

        // ==========================================
        // NEXT / SUBMIT
        // ==========================================

        btnNext.setOnClickListener(v -> {

            if (radioGroup.getCheckedRadioButtonId() == -1) {

                Toast.makeText(
                        CareerQuizActivity.this,
                        "Please select an answer",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            saveCurrentAnswer();

            if (currentQuestion < questions.length - 1) {

                currentQuestion++;

                loadQuestion();

            } else {

                showResult();
            }
        });
    }

    // ==========================================
    // LOAD QUESTION
    // ==========================================

    private void loadQuestion() {

        txtQuestionNumber.setText(
                "Question " +
                        (currentQuestion + 1) +
                        " of " +
                        questions.length
        );

        txtQuestion.setText(
                questions[currentQuestion]
        );

        option1.setText(
                options[currentQuestion][0]
        );

        option2.setText(
                options[currentQuestion][1]
        );

        option3.setText(
                options[currentQuestion][2]
        );

        option4.setText(
                options[currentQuestion][3]
        );

        radioGroup.clearCheck();

        // ==========================================
        // RESTORE PREVIOUS ANSWER
        // ==========================================

        if (answers[currentQuestion] == 0) {

            option1.setChecked(true);

        } else if (answers[currentQuestion] == 1) {

            option2.setChecked(true);

        } else if (answers[currentQuestion] == 2) {

            option3.setChecked(true);

        } else if (answers[currentQuestion] == 3) {

            option4.setChecked(true);
        }

        // ==========================================
        // PREVIOUS BUTTON
        // ==========================================

        if (currentQuestion == 0) {

            btnPrevious.setEnabled(false);
            btnPrevious.setAlpha(0.5f);

        } else {

            btnPrevious.setEnabled(true);
            btnPrevious.setAlpha(1.0f);
        }

        // ==========================================
        // NEXT BUTTON
        // ==========================================

        if (currentQuestion == questions.length - 1) {

            btnNext.setText("Submit Quiz");

        } else {

            btnNext.setText("Next");
        }
    }

    // ==========================================
    // SAVE CURRENT ANSWER
    // ==========================================

    private void saveCurrentAnswer() {

        int selectedId =
                radioGroup.getCheckedRadioButtonId();

        if (selectedId == R.id.option1) {

            answers[currentQuestion] = 0;

        } else if (selectedId == R.id.option2) {

            answers[currentQuestion] = 1;

        } else if (selectedId == R.id.option3) {

            answers[currentQuestion] = 2;

        } else if (selectedId == R.id.option4) {

            answers[currentQuestion] = 3;
        }
    }

    // ==========================================
    // SHOW RESULT
    // ==========================================

    private void showResult() {

        int software = 0;
        int data = 0;
        int cybersecurity = 0;
        int design = 0;

        // ==========================================
        // CALCULATE SCORES
        // ==========================================

        for (int answer : answers) {

            if (answer == 0) {

                software++;

            } else if (answer == 1) {

                data++;

            } else if (answer == 2) {

                cybersecurity++;

            } else if (answer == 3) {

                design++;
            }
        }

        // ==========================================
        // FIND RECOMMENDED CAREER
        // ==========================================

        String recommendedCareer;

        if (software >= data &&
                software >= cybersecurity &&
                software >= design) {

            recommendedCareer =
                    "Software Developer";

        } else if (data >= cybersecurity &&
                data >= design) {

            recommendedCareer =
                    "Data Analyst";

        } else if (cybersecurity >= design) {

            recommendedCareer =
                    "Cybersecurity";

        } else {

            recommendedCareer =
                    "UI / UX Designer";
        }

        // ==========================================
        // OPEN RESULT SCREEN
        // ==========================================

        Intent intent = new Intent(
                CareerQuizActivity.this,
                QuizResultActivity.class
        );

        intent.putExtra(
                "RECOMMENDED_CAREER",
                recommendedCareer
        );

        startActivity(intent);

        finish();
    }
}