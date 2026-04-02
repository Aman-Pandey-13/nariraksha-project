package com.example.womensafetyalert;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

public class OtpActivity extends AppCompatActivity {

    EditText etOtp;
    Button btnVerifyOtp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        etOtp = findViewById(R.id.etOtp);
        btnVerifyOtp = findViewById(R.id.btnVerifyOtp);

        String email = getIntent().getStringExtra("email");

        btnVerifyOtp.setOnClickListener(v -> {
            String otp = etOtp.getText().toString().trim();

            if(otp.isEmpty()) {
                Toast.makeText(this, "Enter OTP", Toast.LENGTH_SHORT).show();
            } else {
                // TODO: Call backend verify-otp API with email + otp

                Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show();

                // Go to Home/Dashboard
                startActivity(new Intent(this, MainpageActivity.class));
                finish();
            }
        });
    }
}