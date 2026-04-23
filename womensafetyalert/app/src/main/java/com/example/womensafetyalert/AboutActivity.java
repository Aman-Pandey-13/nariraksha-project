package com.example.womensafetyalert;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AboutActivity extends AppCompatActivity {

    TextView tvHome, tvAbout, tvVideos, tvContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        tvHome = findViewById(R.id.tvHome);
        tvAbout = findViewById(R.id.tvAbout);
        tvVideos = findViewById(R.id.tvVideos);
//        tvContact = findViewById(R.id.tvprofile);

        // Home Click
        tvHome.setOnClickListener(v -> {
            Intent intent = new Intent(AboutActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        });

        // About Click (already here)
        tvAbout.setOnClickListener(v -> {
            // Do nothing
        });
        tvVideos.setOnClickListener(v -> {
            startActivity(new Intent(this, VideosActivity.class));
        });
    }
}