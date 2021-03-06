package com.example.sofe4640ucourseproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class VideoView extends AppCompatActivity {

    Button backButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_view);

        android.widget.VideoView videoViewer;
        Bundle extras = getIntent().getExtras();

        videoViewer=findViewById(R.id.videoView3);
        videoViewer.setVideoPath(extras.getString("video"));
        videoViewer.start();
    }

    public void back(View v) {
        startActivity(new Intent(VideoView.this,HomePage.class));
    }
}