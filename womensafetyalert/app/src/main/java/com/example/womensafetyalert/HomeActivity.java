package com.example.womensafetyalert;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    Button btnLearnMore;
    TextView tvAbout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        btnLearnMore = findViewById(R.id.btnLearnMore);
        tvAbout = findViewById(R.id.tvAbout);

        // Learn More → Login
        btnLearnMore.setOnClickListener(view -> {
            Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        // About Text → About Page
        tvAbout.setOnClickListener(view -> {
            Intent intent = new Intent(HomeActivity.this, AboutActivity.class);
            startActivity(intent);
        });
    }
}