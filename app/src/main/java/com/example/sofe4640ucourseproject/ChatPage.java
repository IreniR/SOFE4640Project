package com.example.sofe4640ucourseproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
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

    EditText textDesc;
    ImageButton send_message_btn, back;

    RecyclerView chatMessagesDisplay;
    MessageAdapter messageAdapter;
    ArrayList<Message> chatList;


    UUID hash = UUID.randomUUID();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_page);

        receiverName=findViewById(R.id.receiverText);
        back=findViewById(R.id.backButton);
        textDesc = findViewById(R.id.messageBox);
        send_message_btn = findViewById(R.id.send_msg_btn);

        chatList = new ArrayList<>();
        chatMessagesDisplay = findViewById(R.id.chatRecyclerView);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        chatMessagesDisplay.setLayoutManager(manager);
        chatMessagesDisplay.setHasFixedSize(true);
        messageAdapter = new MessageAdapter(getApplicationContext(), chatList);
        chatMessagesDisplay.setAdapter(messageAdapter);

        getData();
        receiverName.setText(name);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ChatPage.this,HomePage.class));
            }
        });

        send_message_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setChatHistory();

                if(textDesc.getText().toString().isEmpty()){
                    Toast.makeText(ChatPage.this, "Cannot Send Empty Text", Toast.LENGTH_SHORT).show();
                }else{
                    setMessageDetails();
                    textDesc.getText().clear();

                    Toast.makeText(ChatPage.this, "Message Sent", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setChatHistory() {
        // FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
         //Query query = rootRef.collection("Messages").document(FirebaseAuth.getInstance().getCurrentUser().getEmail()).


    }

    private void setMessageDetails() {
        Map<String, Object> messageRec = new HashMap<>();
        Map<String, Object> message = new HashMap<>();

        message.put("message", textDesc.getText().toString());
        message.put("audio","Sample audio");
        message.put("image","sample image");
        message.put("receiver", name);
        message.put("sender", FirebaseAuth.getInstance().getCurrentUser().getEmail());
        message.put("timestamp", FieldValue.serverTimestamp());

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

    @Override
    protected void onStart() {
        super.onStart();
        messageAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onStop() {
        super.onStop();
        messageAdapter.notifyDataSetChanged();
    }
}