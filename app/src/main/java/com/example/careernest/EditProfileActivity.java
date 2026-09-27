package com.example.careernest;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EditProfileActivity extends AppCompatActivity {

    // ==========================================
    // UI COMPONENTS
    // ==========================================

    private ImageButton btnBack;

    private EditText edtName;
    private EditText edtEmail;
    private EditText edtCollege;
    private EditText edtCourse;
    private EditText edtSkills;

    private Button btnSaveProfile;

    // ==========================================
    // DATABASE
    // ==========================================

    private DatabaseHelper databaseHelper;

    // ==========================================
    // CURRENT USER
    // ==========================================

    private int currentUserId;

    // ==========================================
    // ON CREATE
    // ==========================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        ThemeManager.applySavedTheme(this);

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_edit_profile);

        // ==========================================
        // DATABASE
        // ==========================================

        databaseHelper = new DatabaseHelper(this);

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

        btnBack = findViewById(R.id.btnBack);

        edtName = findViewById(R.id.edtName);

        edtEmail = findViewById(R.id.edtEmail);

        edtCollege = findViewById(R.id.edtCollege);

        edtCourse = findViewById(R.id.edtCourse);

        edtSkills = findViewById(R.id.edtSkills);

        btnSaveProfile =
                findViewById(R.id.btnSaveProfile);

        // ==========================================
        // LOAD EXISTING PROFILE
        // ==========================================

        loadProfile();

        // ==========================================
        // BACK BUTTON
        // ==========================================

        btnBack.setOnClickListener(v -> finish());

        // ==========================================
        // SAVE PROFILE
        // ==========================================

        btnSaveProfile.setOnClickListener(v -> saveProfile());
    }

    // ==========================================
    // LOAD PROFILE FROM SQLITE
    // ==========================================

    private void loadProfile() {

        if (currentUserId == -1) {

            Toast.makeText(
                    this,
                    "No user is logged in.",
                    Toast.LENGTH_SHORT
            ).show();

            finish();

            return;
        }

        // ==========================================
        // LOAD NAME + EMAIL
        // ==========================================

        Cursor userCursor =
                databaseHelper.getUser(currentUserId);

        if (userCursor != null &&
                userCursor.moveToFirst()) {

            String name =
                    userCursor.getString(
                            userCursor.getColumnIndexOrThrow("name")
                    );

            String email =
                    userCursor.getString(
                            userCursor.getColumnIndexOrThrow("email")
                    );

            edtName.setText(name);
            edtEmail.setText(email);
        }

        if (userCursor != null) {
            userCursor.close();
        }

        // ==========================================
        // LOAD COLLEGE + COURSE + SKILLS
        // ==========================================

        Cursor profileCursor =
                databaseHelper.getProfile(currentUserId);

        if (profileCursor != null &&
                profileCursor.moveToFirst()) {

            String college =
                    profileCursor.getString(
                            profileCursor.getColumnIndexOrThrow("college")
                    );

            String course =
                    profileCursor.getString(
                            profileCursor.getColumnIndexOrThrow("course")
                    );

            String skills =
                    profileCursor.getString(
                            profileCursor.getColumnIndexOrThrow("skills")
                    );

            edtCollege.setText(
                    college == null ? "" : college
            );

            edtCourse.setText(
                    course == null ? "" : course
            );

            edtSkills.setText(
                    skills == null ? "" : skills
            );
        }

        if (profileCursor != null) {
            profileCursor.close();
        }
    }

    // ==========================================
    // SAVE PROFILE
    // ==========================================

    private void saveProfile() {

        String name =
                edtName.getText()
                        .toString()
                        .trim();

        String email =
                edtEmail.getText()
                        .toString()
                        .trim();

        String college =
                edtCollege.getText()
                        .toString()
                        .trim();

        String course =
                edtCourse.getText()
                        .toString()
                        .trim();

        String skills =
                edtSkills.getText()
                        .toString()
                        .trim();

        // ==========================================
        // VALIDATION
        // ==========================================

        if (name.isEmpty()) {

            edtName.setError(
                    "Please enter your name"
            );

            edtName.requestFocus();

            return;
        }

        if (email.isEmpty()) {

            edtEmail.setError(
                    "Please enter your email"
            );

            edtEmail.requestFocus();

            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS
                .matcher(email)
                .matches()) {

            edtEmail.setError(
                    "Enter a valid email address"
            );

            edtEmail.requestFocus();

            return;
        }

        if (currentUserId == -1) {

            Toast.makeText(
                    this,
                    "No user is logged in.",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        // ==========================================
        // CHECK EMAIL
        // ==========================================

        if (databaseHelper.emailExistsForOtherUser(
                email,
                currentUserId)) {

            Toast.makeText(
                    this,
                    "This email is already used by another account.",
                    Toast.LENGTH_LONG
            ).show();

            edtEmail.requestFocus();

            return;
        }

        // ==========================================
        // UPDATE USER TABLE
        // ==========================================

        boolean userUpdated =
                databaseHelper.updateUserBasicInfo(
                        currentUserId,
                        name,
                        email
                );

        // ==========================================
        // UPDATE PROFILE TABLE
        // ==========================================

        boolean profileUpdated =
                databaseHelper.updateProfile(
                        currentUserId,
                        college,
                        course,
                        skills
                );

        // ==========================================
        // UPDATE SESSION
        // ==========================================

        if (userUpdated) {

            SharedPreferences session =
                    getSharedPreferences(
                            "CareerNestSession",
                            MODE_PRIVATE
                    );

            session.edit()
                    .putString(
                            "CURRENT_USER_NAME",
                            name
                    )
                    .putString(
                            "CURRENT_USER_EMAIL",
                            email.toLowerCase()
                    )
                    .apply();
        }

        // ==========================================
        // SUCCESS
        // ==========================================

        if (userUpdated && profileUpdated) {

            Toast.makeText(
                    EditProfileActivity.this,
                    "Profile updated successfully!",
                    Toast.LENGTH_SHORT
            ).show();

            finish();

        } else {

            Toast.makeText(
                    EditProfileActivity.this,
                    "Unable to update profile.",
                    Toast.LENGTH_LONG
            ).show();
        }
    }
}