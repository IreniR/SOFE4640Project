package com.example.sofe4640ucourseproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

public class ChatPage extends AppCompatActivity {

    String name, description;
    int image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_page);
    }
    private void getData(){
        if(getIntent().hasExtra("myImage") && getIntent().hasExtra("description") && getIntent().hasExtra("name")){
            name = getIntent().getStringExtra("name");
            description = getIntent().getStringExtra("description");
            image = getIntent().getIntExtra("myImage",1);
        }else{
            Toast.makeText(this, "No data", Toast.LENGTH_SHORT).show();
        }
    }
}