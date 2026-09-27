package com.example.careernest;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SignUpActivity extends AppCompatActivity {

    // ==========================================
    // UI COMPONENTS
    // ==========================================

    private EditText edtName;
    private EditText edtEmail;
    private EditText edtPassword;
    private EditText edtConfirmPassword;

    private Button btnCreateAccount;
    private TextView txtLogin;

    // ==========================================
    // DATABASE
    // ==========================================

    private DatabaseHelper databaseHelper;


    // ==========================================
    // ON CREATE
    // ==========================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Apply Light/Dark theme
        ThemeManager.applySavedTheme(this);

        super.onCreate(savedInstanceState);

        // Your actual XML filename
        setContentView(R.layout.activity_signup);


        // ==========================================
        // CONNECT UI
        // ==========================================

        edtName =
                findViewById(R.id.edtName);

        edtEmail =
                findViewById(R.id.edtEmail);

        edtPassword =
                findViewById(R.id.edtPassword);

        edtConfirmPassword =
                findViewById(R.id.edtConfirmPassword);

        btnCreateAccount =
                findViewById(R.id.btnCreateAccount);

        txtLogin =
                findViewById(R.id.txtLogin);


        // ==========================================
        // DATABASE
        // ==========================================

        databaseHelper =
                new DatabaseHelper(this);


        // ==========================================
        // CREATE ACCOUNT BUTTON
        // ==========================================

        btnCreateAccount.setOnClickListener(v -> {

            registerUser();

        });


        // ==========================================
        // LOGIN LINK
        // ==========================================

        txtLogin.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            SignUpActivity.this,
                            LoginActivity.class
                    );

            startActivity(intent);

            finish();

        });
    }


    // ==========================================
    // REGISTER USER
    // ==========================================

    private void registerUser() {

        // ==========================================
        // GET VALUES
        // ==========================================

        String name =
                edtName.getText()
                        .toString()
                        .trim();

        String email =
                edtEmail.getText()
                        .toString()
                        .trim();

        String password =
                edtPassword.getText()
                        .toString()
                        .trim();

        String confirmPassword =
                edtConfirmPassword.getText()
                        .toString()
                        .trim();


        // ==========================================
        // NAME VALIDATION
        // ==========================================

        if (name.isEmpty()) {

            edtName.setError(
                    "Enter your full name"
            );

            edtName.requestFocus();

            return;
        }


        // ==========================================
        // EMAIL VALIDATION
        // ==========================================

        if (email.isEmpty()) {

            edtEmail.setError(
                    "Enter your email"
            );

            edtEmail.requestFocus();

            return;
        }


        // Check email format

        if (!android.util.Patterns.EMAIL_ADDRESS
                .matcher(email)
                .matches()) {

            edtEmail.setError(
                    "Enter a valid email address"
            );

            edtEmail.requestFocus();

            return;
        }


        // ==========================================
        // PASSWORD VALIDATION
        // ==========================================

        if (password.isEmpty()) {

            edtPassword.setError(
                    "Enter your password"
            );

            edtPassword.requestFocus();

            return;
        }


        // Minimum password length

        if (password.length() < 6) {

            edtPassword.setError(
                    "Password must contain at least 6 characters"
            );

            edtPassword.requestFocus();

            return;
        }


        // ==========================================
        // CONFIRM PASSWORD VALIDATION
        // ==========================================

        if (confirmPassword.isEmpty()) {

            edtConfirmPassword.setError(
                    "Confirm your password"
            );

            edtConfirmPassword.requestFocus();

            return;
        }


        // Check passwords

        if (!password.equals(confirmPassword)) {

            edtConfirmPassword.setError(
                    "Passwords do not match"
            );

            edtConfirmPassword.requestFocus();

            return;
        }


        // ==========================================
        // CHECK EMAIL ALREADY EXISTS
        // ==========================================

        if (databaseHelper.emailExists(email)) {

            edtEmail.setError(
                    "Email already registered"
            );

            edtEmail.requestFocus();

            Toast.makeText(
                    SignUpActivity.this,
                    "An account with this email already exists.",
                    Toast.LENGTH_LONG
            ).show();

            return;
        }


        // ==========================================
        // CREATE USER IN DATABASE
        // ==========================================

        long userId =
                databaseHelper.createUser(
                        name,
                        email,
                        password
                );


        // ==========================================
        // ACCOUNT CREATED
        // ==========================================

        if (userId != -1) {

            // Create profile for this user
            databaseHelper.createProfile(
                    (int) userId
            );


            Toast.makeText(
                    SignUpActivity.this,
                    "Account created successfully!",
                    Toast.LENGTH_SHORT
            ).show();


            // ======================================
            // GO TO LOGIN
            // ======================================

            Intent intent =
                    new Intent(
                            SignUpActivity.this,
                            LoginActivity.class
                    );

            startActivity(intent);

            finish();

        } else {

            // ======================================
            // DATABASE ERROR
            // ======================================

            Toast.makeText(
                    SignUpActivity.this,
                    "Unable to create account. Please try again.",
                    Toast.LENGTH_LONG
            ).show();
        }
    }
}