package com.example.careernest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LearningResourcesActivity extends AppCompatActivity {

    private ImageButton btnBack;
    private TextView txtCareerName;
    private LinearLayout resourcesContainer;

    private int stepNumber = 0;
    private int resourceIndex = 0;
    private int totalResources = 0;

    private String careerName;

    // SQLite
    private DatabaseHelper databaseHelper;

    // Current logged-in user
    private int currentUserId = -1;

    // Resource completion is kept separately.
    // Once all 3 resources are opened, the roadmap step
    // is marked completed in SQLite.
    private SharedPreferences resourcePreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        ThemeManager.applySavedTheme(this);

        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_learning_resources
        );

        // ==========================================
        // CONNECT UI
        // ==========================================

        btnBack =
                findViewById(R.id.btnBack);

        txtCareerName =
                findViewById(R.id.txtCareerName);

        resourcesContainer =
                findViewById(R.id.resourcesContainer);

        // ==========================================
        // DATABASE
        // ==========================================

        databaseHelper =
                new DatabaseHelper(this);

        // ==========================================
        // CURRENT USER
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
        // GET CAREER
        // ==========================================

        careerName =
                getIntent().getStringExtra(
                        "CAREER_NAME"
                );

        if (careerName == null ||
                careerName.trim().isEmpty()) {

            careerName =
                    "Software Developer";
        }

        // ==========================================
        // GET STEP
        // ==========================================

        stepNumber =
                getIntent().getIntExtra(
                        "STEP_NUMBER",
                        0
                );

        // ==========================================
        // RESOURCE TRACKING
        // ==========================================

        resourcePreferences =
                getSharedPreferences(
                        "CareerNestResourceProgress",
                        MODE_PRIVATE
                );

        txtCareerName.setText(
                careerName
        );

        // ==========================================
        // BACK
        // ==========================================

        btnBack.setOnClickListener(v ->
                finish()
        );

        // ==========================================
        // LOAD RESOURCES
        // ==========================================

        if (stepNumber > 0) {

            loadStepResources(
                    careerName,
                    stepNumber
            );

        } else {

            loadCareerResources(
                    careerName
            );
        }
    }

    // ============================================================
    // STEP-SPECIFIC RESOURCES
    // ============================================================

    private void loadStepResources(
            String careerName,
            int stepNumber
    ) {

        resourcesContainer.removeAllViews();

        resourceIndex = 0;
        totalResources = 0;

        switch (stepNumber) {

            case 1:

                addResource(
                        "💻",
                        "Programming Fundamentals",
                        "Learn variables, conditions, loops, functions and basic programming concepts.",
                        "https://www.freecodecamp.org/learn/"
                );

                addResource(
                        "☕",
                        "Java Programming",
                        "Learn Java syntax, object-oriented programming, collections and exception handling.",
                        "https://dev.java/learn/"
                );

                addResource(
                        "🐍",
                        "Python",
                        "Learn Python fundamentals and build practical programming skills.",
                        "https://www.python.org/about/gettingstarted/"
                );

                break;

            case 2:

                addResource(
                        "🧠",
                        "Data Structures",
                        "Learn arrays, linked lists, stacks, queues, trees and other important data structures.",
                        "https://www.geeksforgeeks.org/data-structures/"
                );

                addResource(
                        "⚡",
                        "Algorithms",
                        "Learn searching, sorting, recursion, graphs and algorithmic problem solving.",
                        "https://www.geeksforgeeks.org/fundamentals-of-algorithms/"
                );

                addResource(
                        "💻",
                        "LeetCode",
                        "Practice programming problems and improve your coding problem-solving skills.",
                        "https://leetcode.com/"
                );

                break;

            case 3:

                addResource(
                        "🌐",
                        "Git & GitHub",
                        "Learn repositories, commits, branches, pull requests and collaboration.",
                        "https://docs.github.com/en/get-started"
                );

                addResource(
                        "🗄️",
                        "SQL & Databases",
                        "Learn relational databases, SQL queries, tables, joins and database concepts.",
                        "https://www.w3schools.com/sql/"
                );

                addResource(
                        "🔌",
                        "REST APIs",
                        "Learn how applications communicate using APIs and HTTP.",
                        "https://developer.mozilla.org/en-US/docs/Learn_web_development/Extensions/Server-side/First_steps/Introduction"
                );

                break;

            case 4:

                addResource(
                        "🚀",
                        "Project Development",
                        "Learn how to plan, build, test and deploy practical software projects.",
                        "https://www.freecodecamp.org/"
                );

                addResource(
                        "📱",
                        "Android Development",
                        "Learn Android application development using Android Studio.",
                        "https://developer.android.com/courses"
                );

                addResource(
                        "🐙",
                        "GitHub Projects",
                        "Learn how to organize projects and showcase your work on GitHub.",
                        "https://docs.github.com/en/repositories"
                );

                break;

            case 5:

                addResource(
                        "📄",
                        "Resume Preparation",
                        "Learn how to create a clear technical resume and present your skills effectively.",
                        "https://www.indeed.com/career-advice/resumes-cover-letters"
                );

                addResource(
                        "🐙",
                        "GitHub Portfolio",
                        "Learn how to organize repositories and present your projects professionally.",
                        "https://docs.github.com/en/repositories"
                );

                addResource(
                        "💼",
                        "Internship Preparation",
                        "Explore career preparation resources and internship guidance.",
                        "https://www.linkedin.com/jobs/"
                );

                break;

            case 6:

                addResource(
                        "💻",
                        "Technical Interview Preparation",
                        "Practice programming, data structures, algorithms and technical interview questions.",
                        "https://leetcode.com/"
                );

                addResource(
                        "🧠",
                        "Aptitude Practice",
                        "Practice quantitative aptitude, logical reasoning and problem-solving questions.",
                        "https://www.indiabix.com/"
                );

                addResource(
                        "🎤",
                        "Interview Preparation",
                        "Learn interview techniques and prepare for common interview questions.",
                        "https://www.indeed.com/career-advice/interviewing"
                );

                break;

            default:

                loadCareerResources(
                        careerName
                );

                break;
        }
    }

    // ============================================================
    // CAREER-WIDE RESOURCES
    // ============================================================

    private void loadCareerResources(
            String careerName
    ) {

        resourcesContainer.removeAllViews();

        switch (careerName) {

            case "Software Developer":

                addResource(
                        "☕",
                        "Java Programming",
                        "Learn Java programming and object-oriented programming concepts.",
                        "https://dev.java/learn/"
                );

                addResource(
                        "🐍",
                        "Python",
                        "Learn Python programming fundamentals.",
                        "https://www.python.org/about/gettingstarted/"
                );

                addResource(
                        "🧠",
                        "Data Structures & Algorithms",
                        "Improve your programming and problem-solving skills.",
                        "https://www.geeksforgeeks.org/data-structures/"
                );

                addResource(
                        "🐙",
                        "GitHub",
                        "Learn Git and GitHub for version control and project collaboration.",
                        "https://docs.github.com/en/get-started"
                );

                addResource(
                        "📱",
                        "Android Development",
                        "Learn Android development using Android Studio.",
                        "https://developer.android.com/courses"
                );

                break;

            case "AI / ML Engineer":

                addResource(
                        "🐍",
                        "Python",
                        "Learn Python programming for AI and machine learning.",
                        "https://www.python.org/about/gettingstarted/"
                );

                addResource(
                        "📊",
                        "NumPy & Pandas",
                        "Learn the main Python libraries used for data analysis.",
                        "https://numpy.org/learn/"
                );

                addResource(
                        "🤖",
                        "Machine Learning",
                        "Learn machine learning concepts and algorithms.",
                        "https://developers.google.com/machine-learning"
                );

                addResource(
                        "🧠",
                        "Deep Learning",
                        "Learn neural networks and deep learning fundamentals.",
                        "https://www.tensorflow.org/learn"
                );

                addResource(
                        "📈",
                        "Data Visualization",
                        "Learn how to visualize and communicate data effectively.",
                        "https://www.kaggle.com/learn/data-visualization"
                );

                break;

            case "Cybersecurity":

                addResource(
                        "🌐",
                        "Computer Networking",
                        "Learn networking concepts important for cybersecurity.",
                        "https://www.cisco.com/c/en/us/training-events/training-certifications/training/courses/networking-basics.html"
                );

                addResource(
                        "🐧",
                        "Linux",
                        "Learn Linux commands and operating system fundamentals.",
                        "https://linuxjourney.com/"
                );

                addResource(
                        "🔐",
                        "Cryptography",
                        "Learn encryption, hashing and cryptographic concepts.",
                        "https://www.khanacademy.org/computing/computer-science/cryptography"
                );

                addResource(
                        "🛡️",
                        "Cybersecurity Fundamentals",
                        "Learn cybersecurity principles, threats and defenses.",
                        "https://www.cisco.com/c/en/us/products/security/what-is-cybersecurity.html"
                );

                addResource(
                        "🌐",
                        "Web Security",
                        "Learn common web application security concepts.",
                        "https://owasp.org/www-project-top-ten/"
                );

                break;

            case "Cloud Engineer":

                addResource(
                        "🐧",
                        "Linux",
                        "Learn Linux administration and command-line fundamentals.",
                        "https://linuxjourney.com/"
                );

                addResource(
                        "🌐",
                        "Networking",
                        "Learn networking concepts required for cloud computing.",
                        "https://www.cloudflare.com/learning/network-layer/what-is-a-computer-network/"
                );

                addResource(
                        "☁️",
                        "Google Cloud",
                        "Learn Google Cloud services and cloud fundamentals.",
                        "https://cloud.google.com/learn"
                );

                addResource(
                        "☁️",
                        "Microsoft Azure",
                        "Learn Azure cloud services and fundamentals.",
                        "https://learn.microsoft.com/en-us/training/azure/"
                );

                addResource(
                        "🐳",
                        "Docker",
                        "Learn containers and Docker fundamentals.",
                        "https://docs.docker.com/get-started/"
                );

                addResource(
                        "☸️",
                        "Kubernetes",
                        "Learn container orchestration with Kubernetes.",
                        "https://kubernetes.io/docs/tutorials/"
                );

                break;

            default:

                addResource(
                        "📚",
                        "Career Fundamentals",
                        "Explore important concepts and skills for building your career.",
                        "https://www.freecodecamp.org/"
                );

                addResource(
                        "💻",
                        "Programming",
                        "Build programming fundamentals and practical coding skills.",
                        "https://www.freecodecamp.org/learn/"
                );

                addResource(
                        "🎓",
                        "Online Learning",
                        "Explore free learning resources and technical courses.",
                        "https://www.khanacademy.org/"
                );

                break;
        }
    }

    // ============================================================
    // CREATE RESOURCE CARD
    // ============================================================

    private void addResource(
            String icon,
            String title,
            String description,
            String url
    ) {

        // Count only roadmap step resources
        if (stepNumber > 0) {

            resourceIndex++;
            totalResources++;
        }

        TextView resourceCard =
                new TextView(this);

        resourceCard.setText(
                icon + "  " + title +
                        "\n\n" +
                        description +
                        "\n\n" +
                        "Open Resource →"
        );

        resourceCard.setTextSize(15);

        resourceCard.setTextColor(
                getResources().getColor(
                        R.color.text_primary
                )
        );

        int padding =
                (int) (
                        18 *
                                getResources()
                                        .getDisplayMetrics()
                                        .density
                );

        resourceCard.setPadding(
                padding,
                padding,
                padding,
                padding
        );

        resourceCard.setBackgroundResource(
                R.drawable.bg_white_card
        );

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

        params.setMargins(
                0,
                0,
                0,
                (int) (
                        12 *
                                getResources()
                                        .getDisplayMetrics()
                                        .density
                )
        );

        resourceCard.setLayoutParams(params);

        final int currentResourceNumber =
                resourceIndex;

        // ========================================================
        // OPEN RESOURCE
        // ========================================================

        resourceCard.setOnClickListener(v -> {

            try {

                Intent intent =
                        new Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse(url)
                        );

                startActivity(intent);

                // Record resource as opened
                if (stepNumber > 0) {

                    markResourceOpened(
                            currentResourceNumber
                    );
                }

            } catch (Exception e) {

                Toast.makeText(
                        LearningResourcesActivity.this,
                        "Unable to open this resource.",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });

        resourcesContainer.addView(
                resourceCard
        );
    }

    // ============================================================
    // MARK RESOURCE AS OPENED
    // ============================================================

    private void markResourceOpened(
            int resourceNumber
    ) {

        if (stepNumber <= 0) {
            return;
        }

        /*
         * Resource-open status is only used to determine
         * whether all resources for this step have been opened.
         *
         * The actual roadmap completion is stored in SQLite.
         */

        String key =
                currentUserId +
                        "_" +
                        careerName +
                        "_step_" +
                        stepNumber +
                        "_resource_" +
                        resourceNumber;

        resourcePreferences
                .edit()
                .putBoolean(
                        key,
                        true
                )
                .apply();

        checkAllResourcesOpened();
    }

    // ============================================================
    // CHECK IF ALL RESOURCES ARE OPENED
    // ============================================================

    private void checkAllResourcesOpened() {

        if (currentUserId == -1) {
            return;
        }

        if (stepNumber <= 0 ||
                totalResources <= 0) {
            return;
        }

        boolean allOpened = true;

        for (
                int i = 1;
                i <= totalResources;
                i++
        ) {

            String key =
                    currentUserId +
                            "_" +
                            careerName +
                            "_step_" +
                            stepNumber +
                            "_resource_" +
                            i;

            boolean opened =
                    resourcePreferences.getBoolean(
                            key,
                            false
                    );

            if (!opened) {

                allOpened = false;
                break;
            }
        }

        // ========================================================
        // ALL RESOURCES COMPLETED
        // ========================================================

        if (allOpened) {

            boolean alreadyCompleted =
                    databaseHelper.isStepCompleted(
                            currentUserId,
                            careerName,
                            stepNumber
                    );

            if (!alreadyCompleted) {

                databaseHelper.setStepCompleted(
                        currentUserId,
                        careerName,
                        stepNumber,
                        true
                );

                Toast.makeText(
                        LearningResourcesActivity.this,
                        "✓ All resources completed! Step marked as completed.",
                        Toast.LENGTH_LONG
                ).show();
            }
        }
    }
}