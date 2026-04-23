package com.example.womensafetyalert;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import okhttp3.ResponseBody;
import retrofit2.*;

public class OtpActivity extends AppCompatActivity {

    EditText etOtp;
    Button btnVerifyOtp, btnResendOtp;
    String email;

    CountDownTimer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        etOtp = findViewById(R.id.etOtp);
        btnVerifyOtp = findViewById(R.id.btnVerifyOtp);
        btnResendOtp = findViewById(R.id.btnResendOtp);

        email = getIntent().getStringExtra("email");

        if (email == null || email.isEmpty()) {
            Toast.makeText(this, "Session expired. Login again.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        startResendTimer();

        /* ================= VERIFY OTP ================= */

        btnVerifyOtp.setOnClickListener(v -> {

            String otp = etOtp.getText().toString().trim();

            if (otp.isEmpty()) {
                etOtp.setError("Enter OTP");
                return;
            }

            if (otp.length() != 6) {
                etOtp.setError("Enter valid 6-digit OTP");
                return;
            }

            btnVerifyOtp.setEnabled(false);

            OtpRequest request = new OtpRequest(email, otp);
            ApiService apiService = RetrofitClient.getApiService();

            apiService.verifyOtp(request).enqueue(new Callback<ResponseBody>() {

                @Override
                public void onResponse(Call<ResponseBody> call,
                                       Response<ResponseBody> response) {

                    btnVerifyOtp.setEnabled(true);

                    if (response.isSuccessful()) {

                        Toast.makeText(OtpActivity.this,
                                "Login Success",
                                Toast.LENGTH_SHORT).show();

                        // ✅ SAVE LOGIN SESSION (IMPORTANT)
                        SharedPreferences sp = getSharedPreferences("UserData", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();

                        editor.putBoolean("isLoggedIn", true);
                        editor.putString("email", email);   // 🔥 MOST IMPORTANT LINE

                        editor.apply();

                        // ✅ GO TO MAIN PAGE
                        Intent intent = new Intent(OtpActivity.this, MainpageActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    } else {

                        Toast.makeText(OtpActivity.this,
                                "Invalid OTP",
                                Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                    btnVerifyOtp.setEnabled(true);

                    Toast.makeText(OtpActivity.this,
                            "Server Error: " + t.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            });
        });

        /* ================= RESEND OTP ================= */

        btnResendOtp.setOnClickListener(v -> {

            btnResendOtp.setEnabled(false);

            EmailRequest request = new EmailRequest(email);
            ApiService apiService = RetrofitClient.getApiService();

            apiService.resendOtp(request).enqueue(new Callback<ResponseBody>() {

                @Override
                public void onResponse(Call<ResponseBody> call,
                                       Response<ResponseBody> response) {

                    if (response.isSuccessful()) {

                        Toast.makeText(OtpActivity.this,
                                "OTP Resent",
                                Toast.LENGTH_SHORT).show();

                        startResendTimer();

                    } else {

                        btnResendOtp.setEnabled(true);

                        Toast.makeText(OtpActivity.this,
                                "Failed to resend OTP",
                                Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call,
                                      Throwable t) {

                    btnResendOtp.setEnabled(true);

                    Toast.makeText(OtpActivity.this,
                            "Error: " + t.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            });
        });
    }

    /* ================= TIMER (ANTI-SPAM) ================= */

    private void startResendTimer() {

        btnResendOtp.setEnabled(false);

        timer = new CountDownTimer(30000, 1000) { // 30 sec

            @Override
            public void onTick(long millisUntilFinished) {
                btnResendOtp.setText("Resend in " + (millisUntilFinished / 1000) + "s");
            }

            @Override
            public void onFinish() {
                btnResendOtp.setEnabled(true);
                btnResendOtp.setText("Resend OTP");
            }
        }.start();
    }

    @Override
    protected void onDestroy() {
        if (timer != null) timer.cancel();
        super.onDestroy();
    }
}