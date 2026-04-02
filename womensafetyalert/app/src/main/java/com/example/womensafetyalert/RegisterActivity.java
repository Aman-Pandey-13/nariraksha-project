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

public class RegisterActivity extends AppCompatActivity {

    EditText etUsername, etEmail, etPhone ,etPassword;
    EditText etContact1, etRelation1;
    EditText etContact2, etRelation2;
    EditText etContact3, etRelation3;
    Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etUsername = findViewById(R.id.etUsername);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etPhone = findViewById(R.id.etPhone);

        etContact1 = findViewById(R.id.etContact1);
        etRelation1 = findViewById(R.id.etRelation1);

        etContact2 = findViewById(R.id.etContact2);
        etRelation2 = findViewById(R.id.etRelation2);

        etContact3 = findViewById(R.id.etContact3);
        etRelation3 = findViewById(R.id.etRelation3);

        btnRegister = findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(v -> {

            String name = etUsername.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            String contact1 = etContact1.getText().toString().trim();
            String contact2 = etContact2.getText().toString().trim();
            String contact3 = etContact3.getText().toString().trim();

            if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty()) {
                Toast.makeText(this,"All fields required",Toast.LENGTH_SHORT).show();
                return;
            }

            /* ================= SAVE CONTACTS LOCALLY ================= */

            SharedPreferences sp = getSharedPreferences("UserData", MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();

            editor.putString("contact1", contact1);
            editor.putString("contact2", contact2);
            editor.putString("contact3", contact3);

            editor.apply();

            /* ================= SEND TO SERVER ================= */

            RegisterRequest request = new RegisterRequest(
                    name,
                    email,
                    password,
                    phone,
                    contact1,
                    contact2,
                    contact3
            );

            ApiService apiService = RetrofitClient.getApiService();

            apiService.registerUser(request).enqueue(new Callback<ResponseBody>() {

                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    if(response.isSuccessful()){

                        Toast.makeText(RegisterActivity.this,
                                "Registered Successfully",
                                Toast.LENGTH_SHORT).show();

                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                        finish();
                    }
                    else{

                        Toast.makeText(RegisterActivity.this,
                                "Registration Failed",
                                Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                    Toast.makeText(RegisterActivity.this,
                            "Server Error: "+t.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            });

        });

    }
}