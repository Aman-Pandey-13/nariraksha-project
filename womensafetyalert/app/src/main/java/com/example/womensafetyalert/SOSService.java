package com.example.womensafetyalert;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

public class SOSService extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Toast.makeText(this, "🚨 Sending SOS...", Toast.LENGTH_SHORT).show();

        // You can reuse your SOS logic here
        Intent i = new Intent(this, MainpageActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);

        stopSelf();

        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}