package com.example.careernest;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class OnboardingActivity extends AppCompatActivity {

    private TextView txtSkip;
    private ImageButton btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_onboarding);

        txtSkip = findViewById(R.id.txtSkip);
        btnNext = findViewById(R.id.btnNext);

        // Skip onboarding
        txtSkip.setOnClickListener(v -> openLogin());

        // Continue
        btnNext.setOnClickListener(v -> openLogin());
    }

    private void openLogin() {

        Intent intent =
                new Intent(OnboardingActivity.this, LoginActivity.class);

        startActivity(intent);

        finish();
    }
}