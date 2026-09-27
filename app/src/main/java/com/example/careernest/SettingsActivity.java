package com.example.careernest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    private ImageButton btnBack;

    private Switch switchNotifications;
    private Switch switchDarkMode;

    private TextView txtResetRoadmap;
    private TextView txtClearSaved;
    private TextView txtAbout;
    private TextView txtLogout;

    private SharedPreferences settingsPreferences;

    private DatabaseHelper databaseHelper;

    private int currentUserId = -1;

    private boolean isLoadingSettings = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        ThemeManager.applySavedTheme(this);

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);

        // ==========================================
        // CONNECT UI
        // ==========================================

        btnBack = findViewById(R.id.btnBack);

        switchNotifications =
                findViewById(R.id.switchNotifications);

        switchDarkMode =
                findViewById(R.id.switchDarkMode);

        txtResetRoadmap =
                findViewById(R.id.txtResetRoadmap);

        txtClearSaved =
                findViewById(R.id.txtClearSaved);

        txtAbout =
                findViewById(R.id.txtAbout);

        txtLogout =
                findViewById(R.id.txtLogout);

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
        // SETTINGS PREFERENCES
        // ==========================================

        settingsPreferences =
                getSharedPreferences(
                        "CareerNestSettings",
                        MODE_PRIVATE
                );

        // ==========================================
        // LOAD SETTINGS
        // ==========================================

        loadSettings();

        // ==========================================
        // BACK
        // ==========================================

        btnBack.setOnClickListener(v -> {
            finish();
        });

        // ==========================================
        // NOTIFICATIONS
        // ==========================================

        switchNotifications.setOnCheckedChangeListener(
                (buttonView, isChecked) -> {

                    if (isLoadingSettings) {
                        return;
                    }

                    settingsPreferences
                            .edit()
                            .putBoolean(
                                    "NOTIFICATIONS",
                                    isChecked
                            )
                            .apply();

                    if (isChecked) {

                        Toast.makeText(
                                SettingsActivity.this,
                                "Notifications enabled",
                                Toast.LENGTH_SHORT
                        ).show();

                    } else {

                        Toast.makeText(
                                SettingsActivity.this,
                                "Notifications disabled",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                }
        );

        // ==========================================
        // DARK MODE
        // ==========================================

        switchDarkMode.setOnCheckedChangeListener(
                (buttonView, isChecked) -> {

                    if (isLoadingSettings) {
                        return;
                    }

                    ThemeManager.setDarkMode(
                            SettingsActivity.this,
                            isChecked
                    );
                }
        );

        // ==========================================
        // RESET ROADMAP
        // ==========================================

        txtResetRoadmap.setOnClickListener(v -> {
            showResetRoadmapDialog();
        });

        // ==========================================
        // CLEAR SAVED CAREERS
        // ==========================================

        txtClearSaved.setOnClickListener(v -> {
            showClearSavedDialog();
        });

        // ==========================================
        // ABOUT
        // ==========================================

        txtAbout.setOnClickListener(v -> {
            showAboutDialog();
        });

        // ==========================================
        // LOGOUT
        // ==========================================

        txtLogout.setOnClickListener(v -> {
            showLogoutDialog();
        });
    }

    // ==========================================
    // LOAD SETTINGS
    // ==========================================

    private void loadSettings() {

        boolean notifications =
                settingsPreferences.getBoolean(
                        "NOTIFICATIONS",
                        true
                );

        boolean darkMode =
                settingsPreferences.getBoolean(
                        "DARK_MODE",
                        false
                );

        isLoadingSettings = true;

        switchNotifications.setChecked(
                notifications
        );

        switchDarkMode.setChecked(
                darkMode
        );

        isLoadingSettings = false;
    }

    // ==========================================
    // RESET ROADMAP DIALOG
    // ==========================================

    private void showResetRoadmapDialog() {

        new AlertDialog.Builder(this)
                .setTitle("Reset Roadmap Progress")
                .setMessage(
                        "This will remove all your saved roadmap progress. Are you sure?"
                )
                .setNegativeButton(
                        "Cancel",
                        null
                )
                .setPositiveButton(
                        "Reset",
                        (dialog, which) -> {

                            // ==================================
                            // CHECK USER
                            // ==================================

                            if (currentUserId == -1) {

                                Toast.makeText(
                                        SettingsActivity.this,
                                        "No user is logged in.",
                                        Toast.LENGTH_SHORT
                                ).show();

                                return;
                            }

                            // ==================================
                            // RESET ONLY CURRENT USER
                            // ==================================

                            databaseHelper.resetAllRoadmaps(
                                    currentUserId
                            );

                            Toast.makeText(
                                    SettingsActivity.this,
                                    "Roadmap progress reset.",
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                )
                .show();
    }

    // ==========================================
    // CLEAR SAVED CAREERS DIALOG
    // ==========================================

    private void showClearSavedDialog() {

        new AlertDialog.Builder(this)
                .setTitle("Clear Saved Careers")
                .setMessage(
                        "This will remove all your saved careers. Are you sure?"
                )
                .setNegativeButton(
                        "Cancel",
                        null
                )
                .setPositiveButton(
                        "Clear",
                        (dialog, which) -> {

                            // ==================================
                            // CHECK USER
                            // ==================================

                            if (currentUserId == -1) {

                                Toast.makeText(
                                        SettingsActivity.this,
                                        "No user is logged in.",
                                        Toast.LENGTH_SHORT
                                ).show();

                                return;
                            }

                            // ==================================
                            // CLEAR ONLY CURRENT USER
                            // ==================================

                            databaseHelper.clearSavedCareers(
                                    currentUserId
                            );

                            Toast.makeText(
                                    SettingsActivity.this,
                                    "Saved careers cleared.",
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                )
                .show();
    }

    // ==========================================
    // LOGOUT DIALOG
    // ==========================================

    private void showLogoutDialog() {

        new AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage(
                        "Are you sure you want to logout?"
                )
                .setNegativeButton(
                        "Cancel",
                        null
                )
                .setPositiveButton(
                        "Logout",
                        (dialog, which) -> logoutUser()
                )
                .show();
    }

    // ==========================================
    // LOGOUT USER
    // ==========================================

    private void logoutUser() {

        // ==========================================
        // CLEAR SESSION ONLY
        // ==========================================

        SharedPreferences session =
                getSharedPreferences(
                        "CareerNestSession",
                        MODE_PRIVATE
                );

        session.edit()
                .clear()
                .apply();

        // ==========================================
        // GO TO LOGIN
        // ==========================================

        Intent intent =
                new Intent(
                        SettingsActivity.this,
                        LoginActivity.class
                );

        // ==========================================
        // CLEAR PREVIOUS ACTIVITIES
        // ==========================================

        intent.setFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK
        );

        startActivity(intent);

        finish();
    }

    // ==========================================
    // ABOUT DIALOG
    // ==========================================

    private void showAboutDialog() {

        new AlertDialog.Builder(this)
                .setTitle("CareerNest")
                .setMessage(
                        "CareerNest\n\n" +
                                "Your Career Starts Here.\n\n" +
                                "Explore careers, take the career quiz, " +
                                "build your roadmap and track your progress.\n\n" +
                                "Version 1.0"
                )
                .setPositiveButton(
                        "OK",
                        null
                )
                .show();
    }
}