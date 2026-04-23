package com.example.womensafetyalert;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class PowerButtonReceiver extends BroadcastReceiver {

    private static long lastPressTime = 0;
    private static int pressCount = 0;

    @Override
    public void onReceive(Context context, Intent intent) {

        long currentTime = System.currentTimeMillis();

        if (currentTime - lastPressTime < 1500) {
            pressCount++;
        } else {
            pressCount = 1;
        }

        lastPressTime = currentTime;

        if (pressCount >= 3) {

            Toast.makeText(context, "🚨 SOS Triggered!", Toast.LENGTH_SHORT).show();

            Intent i = new Intent(context, SOSService.class);
            context.startService(i);

            pressCount = 0;
        }
    }
}