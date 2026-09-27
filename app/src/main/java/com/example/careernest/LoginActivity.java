package com.example.careernest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.text.InputType;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText edtEmail;
    private EditText edtPassword;
    private Button btnLogin;
    private TextView txtSignUp;

    private DatabaseHelper databaseHelper;

    // Password visibility
    private boolean isPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        ThemeManager.applySavedTheme(this);

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        // ==========================================
        // CONNECT UI
        // ==========================================

        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        txtSignUp = findViewById(R.id.txtSignUp);

        databaseHelper = new DatabaseHelper(this);

        // ==========================================
        // PASSWORD SHOW / HIDE
        // ==========================================

        edtPassword.setOnTouchListener((v, event) -> {

            if (event.getAction() == MotionEvent.ACTION_UP) {

                // Check if the user tapped the right drawable
                if (event.getRawX() >=
                        (edtPassword.getRight()
                                - edtPassword.getCompoundDrawables()[2].getBounds().width()
                                - edtPassword.getPaddingEnd())) {

                    togglePasswordVisibility();

                    return true;
                }
            }

            return false;
        });

        // ==========================================
        // LOGIN BUTTON
        // ==========================================

        btnLogin.setOnClickListener(v -> loginUser());

        // ==========================================
        // SIGN UP
        // ==========================================

        txtSignUp.setOnClickListener(v -> {

            Intent intent = new Intent(
                    LoginActivity.this,
                    SignUpActivity.class
            );

            startActivity(intent);
        });
    }

    // ==========================================
    // TOGGLE PASSWORD VISIBILITY
    // ==========================================

    private void togglePasswordVisibility() {

        if (isPasswordVisible) {

            // Hide password
            edtPassword.setInputType(
                    InputType.TYPE_CLASS_TEXT |
                            InputType.TYPE_TEXT_VARIATION_PASSWORD
            );

            edtPassword.setCompoundDrawablesWithIntrinsicBounds(
                    android.R.drawable.ic_lock_lock,
                    0,
                    android.R.drawable.ic_menu_view,
                    0
            );

            isPasswordVisible = false;

        } else {

            // Show password
            edtPassword.setInputType(
                    InputType.TYPE_CLASS_TEXT |
                            InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            );

            edtPassword.setCompoundDrawablesWithIntrinsicBounds(
                    android.R.drawable.ic_lock_lock,
                    0,
                    android.R.drawable.ic_menu_close_clear_cancel,
                    0
            );

            isPasswordVisible = true;
        }

        // Keep cursor at the end
        edtPassword.setSelection(
                edtPassword.getText().length()
        );
    }

    // ==========================================
    // LOGIN USER
    // ==========================================

    private void loginUser() {

        String email =
                edtEmail.getText().toString().trim();

        String password =
                edtPassword.getText().toString().trim();

        // ==========================================
        // VALIDATION
        // ==========================================

        if (email.isEmpty()) {

            edtEmail.setError("Enter your email");
            edtEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {

            edtPassword.setError("Enter your password");
            edtPassword.requestFocus();
            return;
        }

        // ==========================================
        // CHECK DATABASE
        // ==========================================

        Cursor cursor =
                databaseHelper.loginUser(
                        email,
                        password
                );

        if (cursor.moveToFirst()) {

            int userId =
                    cursor.getInt(
                            cursor.getColumnIndexOrThrow("id")
                    );

            String name =
                    cursor.getString(
                            cursor.getColumnIndexOrThrow("name")
                    );

            String userEmail =
                    cursor.getString(
                            cursor.getColumnIndexOrThrow("email")
                    );

            cursor.close();

            // ======================================
            // SAVE CURRENT LOGIN SESSION
            // ======================================

            SharedPreferences session =
                    getSharedPreferences(
                            "CareerNestSession",
                            MODE_PRIVATE
                    );

            session.edit()
                    .putBoolean(
                            "IS_LOGGED_IN",
                            true
                    )
                    .putInt(
                            "CURRENT_USER_ID",
                            userId
                    )
                    .putString(
                            "CURRENT_USER_NAME",
                            name
                    )
                    .putString(
                            "CURRENT_USER_EMAIL",
                            userEmail
                    )
                    .apply();

            // ======================================
            // SUCCESS
            // ======================================

            Toast.makeText(
                    LoginActivity.this,
                    "Welcome back, " + name + "!",
                    Toast.LENGTH_SHORT
            ).show();

            Intent intent =
                    new Intent(
                            LoginActivity.this,
                            HomeActivity.class
                    );

            startActivity(intent);

            finish();

        } else {

            cursor.close();

            Toast.makeText(
                    LoginActivity.this,
                    "Invalid email or password.",
                    Toast.LENGTH_LONG
            ).show();
        }
    }
}