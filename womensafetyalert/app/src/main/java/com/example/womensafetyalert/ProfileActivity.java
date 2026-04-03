package com.example.womensafetyalert;

import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import okhttp3.ResponseBody;
import retrofit2.*;

public class ProfileActivity extends AppCompatActivity {

    EditText etName, etEmail, etPhone, etC1, etC2, etC3;
    Button btnEditSave;
    Button profileButton;

    boolean isEdit = false;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etC1 = findViewById(R.id.etC1);
        etC2 = findViewById(R.id.etC2);
        etC3 = findViewById(R.id.etC3);
        btnEditSave = findViewById(R.id.btnEditSave);

        email = getSharedPreferences("UserData", MODE_PRIVATE)
                .getString("email", "");

        loadUser();

        disableFields();

        btnEditSave.setOnClickListener(v -> {
            if (!isEdit) {
                enableFields();
                btnEditSave.setText("Save");
                isEdit = true;
            } else {
                updateUser();
                disableFields();
                btnEditSave.setText("Edit");
                isEdit = false;
            }
        });
    }

    private void loadUser() {

        ApiService apiService = RetrofitClient.getApiService();

        apiService.getUser(email).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {

                if (response.isSuccessful() && response.body() != null) {

                    UserResponse user = response.body();

                    etName.setText(user.getName());
                    etEmail.setText(user.getEmail());
                    etPhone.setText(user.getPhone());
                    etC1.setText(user.getContact1());
                    etC2.setText(user.getContact2());
                    etC3.setText(user.getContact3());
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Toast.makeText(ProfileActivity.this,
                        "Error: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void updateUser() {

        ApiService api = RetrofitClient.getApiService();

        UserResponse u = new UserResponse();

        u.setEmail(email);
        u.setName(etName.getText().toString());
        u.setPhone(etPhone.getText().toString());
        u.setContact1(etC1.getText().toString());
        u.setContact2(etC2.getText().toString());
        u.setContact3(etC3.getText().toString());

        api.updateUser(u).enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> res) {

                if (res.isSuccessful()) {
                    Toast.makeText(ProfileActivity.this, "Updated", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ProfileActivity.this, "Update Failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(ProfileActivity.this,
                        "Error: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void disableFields() {
        etName.setEnabled(false);
        etEmail.setEnabled(false);
        etPhone.setEnabled(false);
        etC1.setEnabled(false);
        etC2.setEnabled(false);
        etC3.setEnabled(false);
    }

    private void enableFields() {
        etName.setEnabled(true);
        etPhone.setEnabled(true);
        etC1.setEnabled(true);
        etC2.setEnabled(true);
        etC3.setEnabled(true);
    }
}