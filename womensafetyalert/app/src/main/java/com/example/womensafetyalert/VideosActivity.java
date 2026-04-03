package com.example.womensafetyalert;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;

public class VideosActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<VideoModel> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videos);

        recyclerView = findViewById(R.id.recyclerView);

        // ✅ VERY IMPORTANT
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();

        list.add(new VideoModel(R.raw.escape, R.drawable.punch, "Wrist Escape"));
//        list.add(new VideoModel(R.raw.punch, R.drawable.escape, "Punch Technique"));
        list.add(new VideoModel(R.raw.kick, R.drawable.kick, "Kick Defense"));

        VideoAdapter adapter = new VideoAdapter(this, list);
        recyclerView.setAdapter(adapter);
    }
}