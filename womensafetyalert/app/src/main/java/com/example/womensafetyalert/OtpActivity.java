package com.example.womensafetyalert;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OtpActivity extends AppCompatActivity {

    EditText etOtp;
    Button btnVerifyOtp, btnResendOtp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        etOtp = findViewById(R.id.etOtp);
        btnVerifyOtp = findViewById(R.id.btnVerifyOtp);
        btnResendOtp = findViewById(R.id.btnResendOtp);

        String email = getIntent().getStringExtra("email");

        /* ================= VERIFY OTP ================= */

        btnVerifyOtp.setOnClickListener(v -> {

            String otp = etOtp.getText().toString().trim();

            if (otp.isEmpty()) {
                Toast.makeText(this, "Enter OTP", Toast.LENGTH_SHORT).show();
                return;
            }

            OtpRequest request = new OtpRequest(email, otp);

            ApiService apiService = RetrofitClient.getApiService();

            apiService.verifyOtp(request).enqueue(new Callback<ResponseBody>() {

                @Override
                public void onResponse(Call<ResponseBody> call,
                                       Response<ResponseBody> response) {

                    if (response.isSuccessful()) {

                        Toast.makeText(OtpActivity.this,
                                "Login Success",
                                Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(OtpActivity.this, MainpageActivity.class);

                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        SharedPreferences sp = getSharedPreferences("UserData", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();

                        editor.putBoolean("isLoggedIn", true);
                        editor.putString("email", email);

                        editor.apply();

                        startActivity(intent);

                    } else {

                        Toast.makeText(OtpActivity.this,
                                "Invalid OTP",
                                Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call,
                                      Throwable t) {

                    Toast.makeText(OtpActivity.this,
                            "Error: " + t.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            });
        });

        /* ================= RESEND OTP ================= */

        btnResendOtp.setOnClickListener(v -> {

            EmailRequest request = new EmailRequest(email);

            ApiService apiService = RetrofitClient.getApiService();

            apiService.resendOtp(request).enqueue(new Callback<ResponseBody>() {

                @Override
                public void onResponse(Call<ResponseBody> call,
                                       Response<ResponseBody> response) {

                    if (response.isSuccessful()) {

                        Toast.makeText(OtpActivity.this,
                                "OTP Resent Successfully",
                                Toast.LENGTH_SHORT).show();

                    } else {

                        Toast.makeText(OtpActivity.this,
                                "Failed to resend OTP",
                                Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call,
                                      Throwable t) {

                    Toast.makeText(OtpActivity.this,
                            "Error: " + t.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            });
        });
    }
}