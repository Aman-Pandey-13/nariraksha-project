package com.example.womensafetyalert;

import android.net.Uri;
import android.os.Bundle;
import android.widget.VideoView;
import android.widget.MediaController;

import androidx.appcompat.app.AppCompatActivity;

public class PlayerActivity extends AppCompatActivity {

    VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        videoView = findViewById(R.id.videoView);

        int videoRes = getIntent().getIntExtra("video", 0);

        if (videoRes == 0) return; // safety check

        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + videoRes);

        videoView.setVideoURI(uri);

        MediaController controller = new MediaController(this);
        videoView.setMediaController(controller);

        videoView.start();
    }
}