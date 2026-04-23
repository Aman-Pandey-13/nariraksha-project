package com.example.womensafetyalert;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    TextView tvName, tvEmail, tvPhone, tvContact1, tvContact2, tvContact3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        tvName = findViewById(R.id.tvName);
        tvEmail = findViewById(R.id.tvEmail);
        tvPhone = findViewById(R.id.tvPhone);
        tvContact1 = findViewById(R.id.tvContact1);
        tvContact2 = findViewById(R.id.tvContact2);
        tvContact3 = findViewById(R.id.tvContact3);

        // 🔥 CALL FUNCTION DIRECTLY (NO PARAM)
        loadUser();
    }

    private void loadUser() {

        String email = getSharedPreferences("UserData", MODE_PRIVATE)
                .getString("email", "");

        // 🔥 DEBUG CHECK
        if (email.isEmpty()) {
            Toast.makeText(this, "Email not found!", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiService api = RetrofitClient.getApiService();

        Map<String, String> map = new HashMap<>();
        map.put("email", email);

        api.getUser(map).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {

                if (response.isSuccessful() && response.body() != null) {

                    UserResponse user = response.body();

                    tvName.setText(user.getName() != null ? user.getName() : "N/A");
                    tvEmail.setText(user.getEmail() != null ? user.getEmail() : "N/A");
                    tvPhone.setText(user.getPhone() != null ? user.getPhone() : "N/A");

                    tvContact1.setText(user.getContact1() != null ? user.getContact1() : "N/A");
                    tvContact2.setText(user.getContact2() != null ? user.getContact2() : "N/A");
                    tvContact3.setText(user.getContact3() != null ? user.getContact3() : "N/A");

                } else {
                    Toast.makeText(ProfileActivity.this,
                            "No data received from server",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Toast.makeText(ProfileActivity.this,
                        "API Error: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }
}