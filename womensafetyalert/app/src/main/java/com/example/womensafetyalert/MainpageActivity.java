package com.example.womensafetyalert;


import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.Priority;
import com.google.android.gms.location.LocationResult;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainpageActivity extends AppCompatActivity {

    private static final int PERMISSION_CODE = 101;
    LocationCallback locationCallback;
    private Button btnSOS;
    private TextView tvAbout, tvHome, tvVideos, tvProfile;
    private LinearLayout recentActivityContainer;

    private FusedLocationProviderClient fusedLocationClient;
    String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainpage);

        userEmail = getIntent().getStringExtra("email");


        btnSOS = findViewById(R.id.btnSOS);
        tvHome = findViewById(R.id.tvHome);
        tvAbout = findViewById(R.id.tvAbout);
        tvVideos = findViewById(R.id.tvVideos);
        tvProfile = findViewById(R.id.tvProfile);

        recentActivityContainer = findViewById(R.id.recentActivityContainer);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        btnSOS.setOnClickListener(v -> checkPermissionsAndSendSOS());

        tvHome.setOnClickListener(v -> {
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        });
        //profile text to profile page
        tvProfile.setOnClickListener(view -> {
            Intent intent = new Intent(this, ProfileActivity.class);

            // Pass user email (IMPORTANT for backend)
            intent.putExtra("email", userEmail);

            startActivity(intent);
        });

        tvAbout.setOnClickListener(v ->
                startActivity(new Intent(this, AboutActivity.class)));
        tvVideos.setOnClickListener(v -> {
            startActivity(new Intent(this, VideosActivity.class));
        });
    }

    /* ================= CHECK PERMISSIONS ================= */

    private void checkPermissionsAndSendSOS() {

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this,
                        Manifest.permission.SEND_SMS)
                        != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.SEND_SMS
                    }, PERMISSION_CODE);

        }
        else if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, 100);
        }
        else{
            getAccurateLocationAndSendSOS();
        }

    }

    /* ================= PERMISSION RESULT ================= */

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_CODE) {
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                getAccurateLocationAndSendSOS();

            } else {
                Toast.makeText(this,
                        "Permissions Required!",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    /* ================= GET ACCURATE GPS LOCATION ================= */

    private void getAccurateLocationAndSendSOS() {

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        LocationRequest locationRequest =
                new LocationRequest.Builder(
                        Priority.PRIORITY_HIGH_ACCURACY, 2000)
                        .setWaitForAccurateLocation(true)
                        .setMaxUpdates(1)
                        .build();

        fusedLocationClient.requestLocationUpdates(
                locationRequest,
                new LocationCallback() {
                    @Override
                    public void onLocationResult(@NonNull LocationResult locationResult) {

                        fusedLocationClient.removeLocationUpdates(this);

                        Location location = locationResult.getLastLocation();

                        if (location != null) {

                            double lat = location.getLatitude();
                            double lng = location.getLongitude();

                            sendSOSMessage(lat, lng);

                        } else {
                            Toast.makeText(MainpageActivity.this,
                                    "Turn ON GPS & Move Outdoors",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                },
                getMainLooper());
    }

    /* ================= SEND SOS MESSAGE ================= */

    private void sendSOSMessage(double lat, double lng) {

        String message = "🚨 HELP ME I AM IN DANGER!\n\n"
                + "Live Location:\n"
                + "https://www.google.com/maps/search/?api=1&query="
                + lat + "+" + lng;

        SharedPreferences sp =
                getSharedPreferences("UserData", MODE_PRIVATE);

        String c1 = sp.getString("contact1", "");
        String c2 = sp.getString("contact2", "");
        String c3 = sp.getString("contact3", "");

        SmsManager smsManager = SmsManager.getDefault();

        ArrayList<String> parts = smsManager.divideMessage(message);

        if (!c1.isEmpty())
            smsManager.sendMultipartTextMessage(c1, null, parts, null, null);

        if (!c2.isEmpty())
            smsManager.sendMultipartTextMessage(c2, null, parts, null, null);

        if (!c3.isEmpty())
            smsManager.sendMultipartTextMessage(c3, null, parts, null, null);

        Toast.makeText(this,
                "🚨 SOS Sent Successfully!",
                Toast.LENGTH_SHORT).show();

        addRecentActivity("🚨SOS Sent with Location");
        makeEmergencyCall();
        startActivity(new Intent(this, CameraActivity.class));

    }

    //    // ================= EMERGENCY CALL =================
    // private static final String EMERGENCY_NUMBER = "+918291418150";//saniya number
    private static final String EMERGENCY_NUMBER = "+918070124747";
    private void makeEmergencyCall() {

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + EMERGENCY_NUMBER));
        startActivity(callIntent);
    }

    /* ================= RECENT ACTIVITY ================= */

    private void addRecentActivity(String message) {

        String time = new SimpleDateFormat(
                "hh:mm a", Locale.getDefault()).format(new Date());

        TextView tv = new TextView(this);
        tv.setText(time + " - " + message);
        tv.setTextSize(16f);
        tv.setPadding(0, 15, 0, 15);
        tv.setTextColor(
                ContextCompat.getColor(this, android.R.color.black));

        recentActivityContainer.addView(tv, 0);
    }
}