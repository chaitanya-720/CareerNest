package com.example.careernest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SavedCareersActivity extends AppCompatActivity {

    // ==========================================
    // UI COMPONENTS
    // ==========================================

    private ImageButton btnBack;

    private LinearLayout savedCareersContainer;

    private TextView txtEmpty;
    private TextView txtEmptyMessage;

    // ==========================================
    // DATABASE
    // ==========================================

    private DatabaseHelper databaseHelper;

    // ==========================================
    // CURRENT USER
    // ==========================================

    private int currentUserId = -1;

    // ==========================================
    // ON CREATE
    // ==========================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        ThemeManager.applySavedTheme(this);

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_saved_careers);

        // ==========================================
        // DATABASE
        // ==========================================

        databaseHelper =
                new DatabaseHelper(this);

        // ==========================================
        // GET CURRENT USER ID
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

        savedCareersContainer =
                findViewById(R.id.savedCareersContainer);

        txtEmpty =
                findViewById(R.id.txtEmpty);

        txtEmptyMessage =
                findViewById(R.id.txtEmptyMessage);

        // ==========================================
        // BACK BUTTON
        // ==========================================

        btnBack.setOnClickListener(v -> {
            finish();
        });

        // ==========================================
        // LOAD SAVED CAREERS
        // ==========================================

        loadSavedCareers();
    }

    // ==========================================
    // ON RESUME
    // ==========================================

    @Override
    protected void onResume() {

        super.onResume();

        loadSavedCareers();
    }

    // ==========================================
    // LOAD SAVED CAREERS FROM SQLITE
    // ==========================================

    private void loadSavedCareers() {

        // ==========================================
        // CLEAR OLD CARDS
        // ==========================================

        savedCareersContainer.removeAllViews();

        // ==========================================
        // CHECK LOGIN
        // ==========================================

        if (currentUserId == -1) {

            showEmptyState();

            return;
        }

        // ==========================================
        // GET SAVED CAREERS
        // ==========================================

        Cursor cursor =
                databaseHelper.getSavedCareers(
                        currentUserId
                );

        int savedCount = 0;

        if (cursor != null) {

            while (cursor.moveToNext()) {

                int careerColumn =
                        cursor.getColumnIndex(
                                "career_name"
                        );

                if (careerColumn >= 0) {

                    String careerName =
                            cursor.getString(
                                    careerColumn
                            );

                    if (careerName != null &&
                            !careerName.trim().isEmpty()) {

                        addCareerCard(careerName);

                        savedCount++;
                    }
                }
            }

            cursor.close();
        }

        // ==========================================
        // EMPTY STATE
        // ==========================================

        if (savedCount == 0) {

            showEmptyState();

        } else {

            hideEmptyState();
        }
    }

    // ==========================================
    // SHOW EMPTY STATE
    // ==========================================

    private void showEmptyState() {

        txtEmpty.setVisibility(
                TextView.VISIBLE
        );

        txtEmptyMessage.setVisibility(
                TextView.VISIBLE
        );
    }

    // ==========================================
    // HIDE EMPTY STATE
    // ==========================================

    private void hideEmptyState() {

        txtEmpty.setVisibility(
                TextView.GONE
        );

        txtEmptyMessage.setVisibility(
                TextView.GONE
        );
    }

    // ==========================================
    // ADD CAREER CARD
    // ==========================================

    private void addCareerCard(
            String careerName
    ) {

        // ==========================================
        // CARD LAYOUT
        // ==========================================

        LinearLayout card =
                new LinearLayout(this);

        card.setOrientation(
                LinearLayout.HORIZONTAL
        );

        card.setGravity(
                Gravity.CENTER_VERTICAL
        );

        card.setPadding(
                20,
                18,
                20,
                18
        );

        card.setBackgroundResource(
                R.drawable.bg_white_card
        );

        LinearLayout.LayoutParams cardParams =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

        cardParams.setMargins(
                0,
                0,
                0,
                14
        );

        card.setLayoutParams(
                cardParams
        );

        // ==========================================
        // CAREER ICON
        // ==========================================

        TextView icon =
                new TextView(this);

        icon.setText(
                getCareerIcon(careerName)
        );

        icon.setTextSize(30);

        icon.setGravity(
                Gravity.CENTER
        );

        LinearLayout.LayoutParams iconParams =
                new LinearLayout.LayoutParams(
                        60,
                        60
                );

        icon.setLayoutParams(
                iconParams
        );

        // ==========================================
        // CAREER INFORMATION
        // ==========================================

        LinearLayout info =
                new LinearLayout(this);

        info.setOrientation(
                LinearLayout.VERTICAL
        );

        LinearLayout.LayoutParams infoParams =
                new LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1
                );

        infoParams.setMargins(
                16,
                0,
                10,
                0
        );

        info.setLayoutParams(
                infoParams
        );

        // ==========================================
        // CAREER NAME
        // ==========================================

        TextView name =
                new TextView(this);

        name.setText(
                careerName
        );

        name.setTextColor(
                getResources().getColor(
                        R.color.text_primary
                )
        );

        name.setTextSize(17);

        name.setTypeface(
                null,
                Typeface.BOLD
        );

        // ==========================================
        // CAREER CATEGORY
        // ==========================================

        TextView category =
                new TextView(this);

        category.setText(
                getCareerCategory(careerName)
        );

        category.setTextColor(
                getResources().getColor(
                        R.color.text_secondary
                )
        );

        category.setTextSize(13);

        category.setPadding(
                0,
                5,
                0,
                0
        );

        // ==========================================
        // ADD INFORMATION
        // ==========================================

        info.addView(name);

        info.addView(category);

        // ==========================================
        // ARROW
        // ==========================================

        TextView arrow =
                new TextView(this);

        arrow.setText("›");

        arrow.setTextSize(32);

        arrow.setTextColor(
                getResources().getColor(
                        R.color.primary
                )
        );

        arrow.setGravity(
                Gravity.CENTER
        );

        // ==========================================
        // ADD VIEWS TO CARD
        // ==========================================

        card.addView(icon);

        card.addView(info);

        card.addView(arrow);

        // ==========================================
        // CARD CLICK
        // ==========================================

        card.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            SavedCareersActivity.this,
                            CareerDetailsActivity.class
                    );

            intent.putExtra(
                    "CAREER_NAME",
                    careerName
            );

            startActivity(intent);
        });

        // ==========================================
        // ADD CARD TO CONTAINER
        // ==========================================

        savedCareersContainer.addView(
                card
        );
    }

    // ==========================================
    // GET CAREER ICON
    // ==========================================

    private String getCareerIcon(
            String careerName
    ) {

        switch (careerName) {

            case "Software Developer":
                return "💻";

            case "AI / ML Engineer":
                return "🤖";

            case "Data Analyst":
                return "📊";

            case "Cybersecurity":
                return "🔐";

            case "Cloud Engineer":
                return "☁️";

            case "Android Developer":
                return "📱";

            case "UI / UX Designer":
                return "🎨";

            case "Database Administrator":
                return "🗄️";

            default:
                return "💼";
        }
    }

    // ==========================================
    // GET CAREER CATEGORY
    // ==========================================

    private String getCareerCategory(
            String careerName
    ) {

        switch (careerName) {

            case "Software Developer":
                return "Software & Technology";

            case "AI / ML Engineer":
                return "Artificial Intelligence";

            case "Data Analyst":
                return "Data & Analytics";

            case "Cybersecurity":
                return "Cybersecurity";

            case "Cloud Engineer":
                return "Cloud Computing";

            case "Android Developer":
                return "Mobile Development";

            case "UI / UX Designer":
                return "Design";

            case "Database Administrator":
                return "Database & IT";

            default:
                return "Career";
        }
    }
}