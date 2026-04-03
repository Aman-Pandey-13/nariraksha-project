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

        btnVerifyOtp.setOnClickListener(v -> {

            String otp = etOtp.getText().toString().trim();

            if (otp.isEmpty()) {
                Toast.makeText(this, "Enter OTP", Toast.LENGTH_SHORT).show();
                return;
            }

            String email = getIntent().getStringExtra("email");

            OtpRequest request = new OtpRequest(email, otp);

            ApiService apiService = RetrofitClient.getApiService();

            apiService.verifyOtp(request).enqueue(new retrofit2.Callback<okhttp3.ResponseBody>() {

                @Override
                public void onResponse(retrofit2.Call<okhttp3.ResponseBody> call,
                                       retrofit2.Response<okhttp3.ResponseBody> response) {

                    if (response.isSuccessful()) {

                        Toast.makeText(OtpActivity.this,
                                "Login Success",
                                Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(OtpActivity.this, MainpageActivity.class);

                        // 🔥 IMPORTANT (prevents going back)
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                        startActivity(intent);

                    } else {

                        Toast.makeText(OtpActivity.this,
                                "Invalid OTP",
                                Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(retrofit2.Call<okhttp3.ResponseBody> call,
                                      Throwable t) {

                    Toast.makeText(OtpActivity.this,
                            "Error: " + t.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            });
        });
    }
}