package com.example.womensafetyalert;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    EditText etEmail, etPassword;
    Button btnLogin;
    TextView tvRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegister = findViewById(R.id.tvRegister);

        SharedPreferences sp = getSharedPreferences("UserData", MODE_PRIVATE);
        boolean isLoggedIn = sp.getBoolean("isLoggedIn", false);

//         ✅ AUTO LOGIN FIX
        if (isLoggedIn) {
            startActivity(new Intent(this, MainpageActivity.class));
            finish();
            return; // VERY IMPORTANT
        }

        // ✅ Click listeners AFTER login check
        btnLogin.setOnClickListener(v -> loginUser());

        tvRegister.setOnClickListener(v ->
                startActivity(new Intent(this, RegisterActivity.class)));
    }

    /* ================= LOGIN FUNCTION ================= */

    private void loginUser() {

        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // ✅ Validation
        if (email.isEmpty()) {
            etEmail.setError("Email required");
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Invalid Email");
            return;
        }

        if(email.isEmpty() || password.isEmpty()){
            Toast.makeText(this,"Enter all fields",Toast.LENGTH_SHORT).show();
            return;
        }

        LoginRequest request = new LoginRequest(email, password);
        ApiService apiService = RetrofitClient.getApiService();

        btnLogin.setEnabled(false);

        apiService.loginUser(request).enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call,
                                   Response<ResponseBody> response) {

                btnLogin.setEnabled(true);

                try {
                    if (response.isSuccessful()) {

                        Toast.makeText(LoginActivity.this,
                                "OTP Sent to Email",
                                Toast.LENGTH_SHORT).show();

                        Intent intent =
                                new Intent(LoginActivity.this, OtpActivity.class);
                        intent.putExtra("email", email);
                        startActivity(intent);

                    } else {

                        String error = "Login Failed";

                        if (response.errorBody() != null) {
                            error = response.errorBody().string();
                        }

                        Toast.makeText(LoginActivity.this,
                                error,
                                Toast.LENGTH_LONG).show();
                    }

                } catch (IOException e) {
                    Toast.makeText(LoginActivity.this,
                            "Response Error",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                btnLogin.setEnabled(true);

                Toast.makeText(LoginActivity.this,
                        "Server Error: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }
}