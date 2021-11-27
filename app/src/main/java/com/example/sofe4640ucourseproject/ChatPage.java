package com.example.sofe4640ucourseproject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ChatPage extends AppCompatActivity {

    String name, description, sender;
    int image;
    TextView receiverName;
    Button back;

    UUID hash = UUID.randomUUID();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_page);

        receiverName=findViewById(R.id.receiverText);
        back=findViewById(R.id.backButton);

        getData();
        receiverName.setText(name);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ChatPage.this,HomePage.class));
            }
        });

        Map<String, Object> messageRec = new HashMap<>();
        Map<String, Object> message = new HashMap<>();

        message.put("message","Hello World!");
        message.put("audio","Sample audio");
        message.put("image","sample image");
        message.put("receiver", name);
        message.put("sender", FirebaseAuth.getInstance().getCurrentUser().getEmail());

        messageRec.put(hash.toString(), message);

        FirebaseFirestore col = FirebaseFirestore.getInstance();
        col.collection("Messages").document(sender).set(messageRec, SetOptions.merge());
    }
    private void getData(){
        if(getIntent().hasExtra("myImage") && getIntent().hasExtra("description") && getIntent().hasExtra("receiver")){
            name = getIntent().getStringExtra("receiver");
            description = getIntent().getStringExtra("description");
            image = getIntent().getIntExtra("myImage",1);
            sender = getIntent().getStringExtra("sender");
        }else{
            Toast.makeText(this, "No data", Toast.LENGTH_SHORT).show();
        }
    }
}