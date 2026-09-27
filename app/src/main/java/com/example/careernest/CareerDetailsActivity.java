package com.example.careernest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CareerDetailsActivity extends AppCompatActivity {

    // ==========================================
    // UI COMPONENTS
    // ==========================================

    private ImageButton btnBack;
    private ImageButton btnSave;

    private TextView txtCareerIcon;
    private TextView txtCareerName;
    private TextView txtCareerCategory;
    private TextView txtDescription;
    private TextView txtSkills;
    private TextView txtTechnologies;
    private TextView txtRoles;
    private TextView txtRoadmap;

    private Button btnLearningResources;
    private Button btnStartRoadmap;

    // ==========================================
    // DATABASE
    // ==========================================

    private DatabaseHelper databaseHelper;

    // ==========================================
    // CURRENT USER
    // ==========================================

    private int currentUserId = -1;

    // ==========================================
    // CAREER
    // ==========================================

    private String careerName;

    private boolean isSaved = false;

    // ==========================================
    // ON CREATE
    // ==========================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        ThemeManager.applySavedTheme(this);

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_career_details);

        // ==========================================
        // DATABASE
        // ==========================================

        databaseHelper = new DatabaseHelper(this);

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
        // CONNECT UI
        // ==========================================

        btnBack = findViewById(R.id.btnBack);

        btnSave = findViewById(R.id.btnSave);

        txtCareerIcon =
                findViewById(R.id.txtCareerIcon);

        txtCareerName =
                findViewById(R.id.txtCareerName);

        txtCareerCategory =
                findViewById(R.id.txtCareerCategory);

        txtDescription =
                findViewById(R.id.txtDescription);

        txtSkills =
                findViewById(R.id.txtSkills);

        txtTechnologies =
                findViewById(R.id.txtTechnologies);

        txtRoles =
                findViewById(R.id.txtRoles);

        txtRoadmap =
                findViewById(R.id.txtRoadmap);

        btnLearningResources =
                findViewById(R.id.btnLearningResources);

        btnStartRoadmap =
                findViewById(R.id.btnStartRoadmap);

        // ==========================================
        // BUTTON TEXT COLORS
        // ==========================================

        btnLearningResources.setTextColor(
                getResources().getColor(
                        R.color.text_primary
                )
        );

        btnStartRoadmap.setTextColor(
                getResources().getColor(
                        R.color.surface
                )
        );

        // ==========================================
        // GET CAREER NAME
        // ==========================================

        careerName =
                getIntent().getStringExtra(
                        "CAREER_NAME"
                );

        if (careerName == null ||
                careerName.trim().isEmpty()) {

            careerName = "Software Developer";
        }

        // ==========================================
        // LOAD CAREER INFORMATION
        // ==========================================

        loadCareerData(careerName);

        // ==========================================
        // LOAD SAVED STATE FROM SQLITE
        // ==========================================

        loadSavedState();

        // ==========================================
        // BACK BUTTON
        // ==========================================

        btnBack.setOnClickListener(v -> finish());

        // ==========================================
        // SAVE BUTTON
        // ==========================================

        btnSave.setOnClickListener(v -> {

            if (currentUserId == -1) {

                Toast.makeText(
                        CareerDetailsActivity.this,
                        "Please login first.",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            if (isSaved) {

                // ==================================
                // REMOVE CAREER
                // ==================================

                boolean removed =
                        databaseHelper.removeCareer(
                                currentUserId,
                                careerName
                        );

                if (removed) {

                    isSaved = false;

                    updateSaveButton();

                    Toast.makeText(
                            CareerDetailsActivity.this,
                            "Career removed from saved careers.",
                            Toast.LENGTH_SHORT
                    ).show();
                }

            } else {

                // ==================================
                // SAVE CAREER
                // ==================================

                boolean saved =
                        databaseHelper.saveCareer(
                                currentUserId,
                                careerName
                        );

                if (saved) {

                    isSaved = true;

                    updateSaveButton();

                    Toast.makeText(
                            CareerDetailsActivity.this,
                            "Career saved!",
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }
        });

        // ==========================================
        // LEARNING RESOURCES
        // ==========================================

        btnLearningResources.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            CareerDetailsActivity.this,
                            LearningResourcesActivity.class
                    );

            intent.putExtra(
                    "CAREER_NAME",
                    careerName
            );

            startActivity(intent);
        });

        // ==========================================
        // START ROADMAP
        // ==========================================

        btnStartRoadmap.setOnClickListener(v -> {

            if (currentUserId == -1) {

                Toast.makeText(
                        CareerDetailsActivity.this,
                        "Please login first.",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            // ======================================
            // SAVE LAST ROADMAP CAREER TO SQLITE
            // ======================================

            databaseHelper.setLastRoadmapCareer(
                    currentUserId,
                    careerName
            );

            Intent intent =
                    new Intent(
                            CareerDetailsActivity.this,
                            CareerRoadmapActivity.class
                    );

            intent.putExtra(
                    "CAREER_NAME",
                    careerName
            );

            startActivity(intent);
        });
    }

    // ==========================================
    // LOAD SAVED STATE
    // ==========================================

    private void loadSavedState() {

        if (currentUserId == -1) {

            isSaved = false;

            updateSaveButton();

            return;
        }

        isSaved =
                databaseHelper.isCareerSaved(
                        currentUserId,
                        careerName
                );

        updateSaveButton();
    }

    // ==========================================
    // UPDATE SAVE STAR
    // ==========================================

    private void updateSaveButton() {

        if (isSaved) {

            // ======================================
            // SAVED
            // ======================================

            btnSave.setImageResource(
                    android.R.drawable.btn_star_big_on
            );

            btnSave.setColorFilter(
                    Color.rgb(
                            79,
                            53,
                            216
                    )
            );

            btnSave.setContentDescription(
                    "Remove saved career"
            );

        } else {

            // ======================================
            // NOT SAVED
            // ======================================

            btnSave.setImageResource(
                    android.R.drawable.btn_star_big_off
            );

            btnSave.setColorFilter(
                    Color.rgb(
                            107,
                            109,
                            133
                    )
            );

            btnSave.setContentDescription(
                    "Save career"
            );
        }
    }

    // ==========================================
    // LOAD CAREER DATA
    // ==========================================

    private void loadCareerData(String careerName) {

        txtCareerName.setText(careerName);

        switch (careerName) {

            // ==========================================
            // SOFTWARE DEVELOPER
            // ==========================================

            case "Software Developer":

                txtCareerIcon.setText("💻");

                txtCareerCategory.setText(
                        "Software & Technology"
                );

                txtDescription.setText(
                        "Software developers design, build, test and maintain software applications for users and organizations."
                );

                txtSkills.setText(
                        "✓ Programming\n" +
                                "✓ Problem Solving\n" +
                                "✓ Data Structures & Algorithms\n" +
                                "✓ Database Management\n" +
                                "✓ Git & GitHub"
                );

                txtTechnologies.setText(
                        "Java  •  Python  •  SQL  •  Git  •  Android"
                );

                txtRoles.setText(
                        "• Software Developer\n" +
                                "• Backend Developer\n" +
                                "• Full Stack Developer\n" +
                                "• Application Developer"
                );

                txtRoadmap.setText(
                        "01  Programming Basics\n\n" +
                                "02  Data Structures & Algorithms\n\n" +
                                "03  Development Skills\n\n" +
                                "04  Build Projects\n\n" +
                                "05  Internship Preparation\n\n" +
                                "06  Job Preparation"
                );

                break;

            // ==========================================
            // AI / ML ENGINEER
            // ==========================================

            case "AI / ML Engineer":

                txtCareerIcon.setText("🤖");

                txtCareerCategory.setText(
                        "Artificial Intelligence"
                );

                txtDescription.setText(
                        "AI and Machine Learning engineers develop intelligent systems that learn from data and solve complex problems."
                );

                txtSkills.setText(
                        "✓ Python\n" +
                                "✓ Mathematics\n" +
                                "✓ Statistics\n" +
                                "✓ Machine Learning\n" +
                                "✓ Problem Solving"
                );

                txtTechnologies.setText(
                        "Python  •  NumPy  •  Pandas  •  TensorFlow  •  Scikit-learn"
                );

                txtRoles.setText(
                        "• Machine Learning Engineer\n" +
                                "• AI Engineer\n" +
                                "• Data Scientist\n" +
                                "• ML Developer"
                );

                txtRoadmap.setText(
                        "01  Python Programming\n\n" +
                                "02  Mathematics & Statistics\n\n" +
                                "03  Data Analysis\n\n" +
                                "04  Machine Learning\n\n" +
                                "05  AI Projects\n\n" +
                                "06  Job Preparation"
                );

                break;

            // ==========================================
            // DATA ANALYST
            // ==========================================

            case "Data Analyst":

                txtCareerIcon.setText("📊");

                txtCareerCategory.setText(
                        "Data & Analytics"
                );

                txtDescription.setText(
                        "Data analysts collect, process and analyze data to help organizations make informed decisions."
                );

                txtSkills.setText(
                        "✓ SQL\n" +
                                "✓ Excel\n" +
                                "✓ Data Visualization\n" +
                                "✓ Statistics\n" +
                                "✓ Analytical Thinking"
                );

                txtTechnologies.setText(
                        "SQL  •  Excel  •  Python  •  Power BI  •  Tableau"
                );

                txtRoles.setText(
                        "• Data Analyst\n" +
                                "• Business Analyst\n" +
                                "• BI Analyst\n" +
                                "• Reporting Analyst"
                );

                txtRoadmap.setText(
                        "01  Excel Basics\n\n" +
                                "02  SQL\n\n" +
                                "03  Statistics\n\n" +
                                "04  Data Visualization\n\n" +
                                "05  Data Projects\n\n" +
                                "06  Interview Preparation"
                );

                break;

            // ==========================================
            // CYBERSECURITY
            // ==========================================

            case "Cybersecurity":

                txtCareerIcon.setText("🔐");

                txtCareerCategory.setText(
                        "Cybersecurity"
                );

                txtDescription.setText(
                        "Cybersecurity professionals protect systems, networks and data from security threats."
                );

                txtSkills.setText(
                        "✓ Networking\n" +
                                "✓ Linux\n" +
                                "✓ Security Fundamentals\n" +
                                "✓ Cryptography\n" +
                                "✓ Ethical Hacking"
                );

                txtTechnologies.setText(
                        "Linux  •  Wireshark  •  Kali Linux  •  SIEM  •  Python"
                );

                txtRoles.setText(
                        "• Security Analyst\n" +
                                "• SOC Analyst\n" +
                                "• Security Engineer\n" +
                                "• Penetration Tester"
                );

                txtRoadmap.setText(
                        "01  Networking\n\n" +
                                "02  Linux\n\n" +
                                "03  Security Fundamentals\n\n" +
                                "04  Security Tools\n\n" +
                                "05  Security Projects\n\n" +
                                "06  Certifications & Jobs"
                );

                break;

            // ==========================================
            // CLOUD ENGINEER
            // ==========================================

            case "Cloud Engineer":

                txtCareerIcon.setText("☁️");

                txtCareerCategory.setText(
                        "Cloud Computing"
                );

                txtDescription.setText(
                        "Cloud engineers design, deploy and maintain applications and infrastructure on cloud platforms."
                );

                txtSkills.setText(
                        "✓ Networking\n" +
                                "✓ Linux\n" +
                                "✓ Cloud Computing\n" +
                                "✓ DevOps\n" +
                                "✓ Infrastructure"
                );

                txtTechnologies.setText(
                        "AWS  •  Azure  •  GCP  •  Docker  •  Kubernetes"
                );

                txtRoles.setText(
                        "• Cloud Engineer\n" +
                                "• Cloud Administrator\n" +
                                "• DevOps Engineer\n" +
                                "• Cloud Architect"
                );

                txtRoadmap.setText(
                        "01  Networking\n\n" +
                                "02  Linux\n\n" +
                                "03  Cloud Fundamentals\n\n" +
                                "04  AWS / Azure / GCP\n\n" +
                                "05  Cloud Projects\n\n" +
                                "06  Certification & Jobs"
                );

                break;

            // ==========================================
            // ANDROID DEVELOPER
            // ==========================================

            case "Android Developer":

                txtCareerIcon.setText("📱");

                txtCareerCategory.setText(
                        "Mobile Development"
                );

                txtDescription.setText(
                        "Android developers create mobile applications for Android smartphones, tablets and other devices."
                );

                txtSkills.setText(
                        "✓ Java / Kotlin\n" +
                                "✓ XML\n" +
                                "✓ Android Studio\n" +
                                "✓ APIs\n" +
                                "✓ Databases"
                );

                txtTechnologies.setText(
                        "Java  •  Kotlin  •  XML  •  Android Studio  •  Firebase"
                );

                txtRoles.setText(
                        "• Android Developer\n" +
                                "• Mobile Application Developer\n" +
                                "• Android UI Developer\n" +
                                "• Mobile Software Engineer"
                );

                txtRoadmap.setText(
                        "01  Java / Kotlin\n\n" +
                                "02  XML & UI Design\n\n" +
                                "03  Android Studio\n\n" +
                                "04  APIs & Databases\n\n" +
                                "05  Build Android Projects\n\n" +
                                "06  Internship & Jobs"
                );

                break;

            // ==========================================
            // UI / UX DESIGNER
            // ==========================================

            case "UI / UX Designer":

                txtCareerIcon.setText("🎨");

                txtCareerCategory.setText(
                        "Design"
                );

                txtDescription.setText(
                        "UI/UX designers create user interfaces and experiences that are useful, accessible and visually engaging."
                );

                txtSkills.setText(
                        "✓ UI Design\n" +
                                "✓ UX Research\n" +
                                "✓ Wireframing\n" +
                                "✓ Prototyping\n" +
                                "✓ Design Thinking"
                );

                txtTechnologies.setText(
                        "Figma  •  Adobe XD  •  Photoshop  •  Illustrator"
                );

                txtRoles.setText(
                        "• UI Designer\n" +
                                "• UX Designer\n" +
                                "• Product Designer\n" +
                                "• UX Researcher"
                );

                txtRoadmap.setText(
                        "01  Design Fundamentals\n\n" +
                                "02  UX Research\n\n" +
                                "03  Wireframes\n\n" +
                                "04  Figma & Prototyping\n\n" +
                                "05  Portfolio Projects\n\n" +
                                "06  Job Preparation"
                );

                break;

            // ==========================================
            // DATABASE ADMINISTRATOR
            // ==========================================

            case "Database Administrator":

                txtCareerIcon.setText("🗄️");

                txtCareerCategory.setText(
                        "Database & IT"
                );

                txtDescription.setText(
                        "Database administrators manage, secure and maintain databases used by organizations and applications."
                );

                txtSkills.setText(
                        "✓ SQL\n" +
                                "✓ DBMS\n" +
                                "✓ Database Security\n" +
                                "✓ Backup & Recovery\n" +
                                "✓ Performance Tuning"
                );

                txtTechnologies.setText(
                        "MySQL  •  PostgreSQL  •  Oracle  •  SQL Server"
                );

                txtRoles.setText(
                        "• Database Administrator\n" +
                                "• Database Developer\n" +
                                "• Database Engineer\n" +
                                "• SQL Developer"
                );

                txtRoadmap.setText(
                        "01  SQL Fundamentals\n\n" +
                                "02  DBMS Concepts\n\n" +
                                "03  Advanced SQL\n\n" +
                                "04  Database Administration\n\n" +
                                "05  Database Projects\n\n" +
                                "06  Job Preparation"
                );

                break;

            // ==========================================
            // DEFAULT
            // ==========================================

            default:

                txtCareerIcon.setText("💼");

                txtCareerCategory.setText(
                        "Career"
                );

                txtDescription.setText(
                        "Explore this career and discover the skills required to build your future."
                );

                txtSkills.setText(
                        "Skills will be added soon."
                );

                txtTechnologies.setText(
                        "Technologies will be added soon."
                );

                txtRoles.setText(
                        "Career roles will be added soon."
                );

                txtRoadmap.setText(
                        "Career roadmap will be added soon."
                );

                break;
        }
    }
}