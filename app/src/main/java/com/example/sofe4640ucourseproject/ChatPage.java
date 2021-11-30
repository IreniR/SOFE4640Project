package com.example.sofe4640ucourseproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

public class ChatPage extends AppCompatActivity {

    String name, description, sender;
    int image;
    TextView receiverName;

    EditText textDesc;
    ImageButton send_message_btn, back;

    RecyclerView chatMessagesDisplay;
    MessageAdapter messageAdapter;
    ArrayList<Message> chatList;
    Message newMessage;

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_page);

        receiverName = findViewById(R.id.receiverText);
        receiverName.setText(name);

        back = findViewById(R.id.backButton);
        textDesc = findViewById(R.id.messageBox);
        send_message_btn = findViewById(R.id.send_msg_btn);

        chatList = new ArrayList<>();
        db = FirebaseFirestore.getInstance();

        chatMessagesDisplay = findViewById(R.id.chatRecyclerView);
        chatMessagesDisplay.setHasFixedSize(true);

        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
        manager.setStackFromEnd(true);
        chatMessagesDisplay.setLayoutManager(manager);

        messageAdapter = new MessageAdapter(getApplicationContext(), chatList);
        chatMessagesDisplay.setAdapter(messageAdapter);
        getData();
        setChatHistory();

        messageAdapter.notifyDataSetChanged();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ChatPage.this, HomePage.class));
            }
        });

        send_message_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setChatHistory();

                if (textDesc.getText().toString().isEmpty()) {
                    Toast.makeText(ChatPage.this, "Cannot Send Empty Text", Toast.LENGTH_SHORT).show();
                } else {
                    setMessageDetails();
                    textDesc.getText().clear();
                    setChatHistory();
                    Toast.makeText(ChatPage.this, "Message Sent", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void setChatHistory() {
        setChatHistorySent();
          setChatHistoryReceive(); // show it as receiver but shown as sender
    }

    private void setChatHistoryReceive() {

        FirebaseFirestore.getInstance().collection("Messages").document(name).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                chatList = new ArrayList<>();

                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()) {
                        Map<String, Object> friendsMap = documentSnapshot.getData();

                        for (Object value : friendsMap.values()) {
                            Gson gson = new Gson();
                            String json = gson.toJson(value);
                            JSONObject jsonObject;
                            try {
                                jsonObject = new JSONObject(json);

                                if (jsonObject.get("receiver").equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())) {
                                    newMessage = new Message(jsonObject.get("message").toString(), FirebaseAuth.getInstance().getCurrentUser().getEmail());
                                    chatList.add(newMessage);
                                    messageAdapter = new MessageAdapter(ChatPage.this, chatList);
                                    chatMessagesDisplay.setAdapter(messageAdapter);
                                    messageAdapter.notifyDataSetChanged();
                                    System.out.println("It gets the senders email");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        });
        messageAdapter.notifyDataSetChanged();


    }

    private void setChatHistorySent() {
        FirebaseFirestore.getInstance().collection("Messages").document(FirebaseAuth.getInstance().getCurrentUser().getEmail()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                chatList = new ArrayList<>();

                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()) {
                        Map<String, Object> friendsMap = documentSnapshot.getData();
                        for (Object value : friendsMap.values()) {
                            Gson gson = new Gson();
                            String json = gson.toJson(value);
                            JSONObject jsonObject;
                            try {
                                jsonObject = new JSONObject(json);

                                if (jsonObject.get("receiver").equals(name)) {
                                    newMessage = new Message(jsonObject.get("message").toString(), FirebaseAuth.getInstance().getCurrentUser().getEmail());
                                    chatList.add(newMessage);
                                    messageAdapter.update(chatList);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        //TODO order the timestamps

                        System.out.println(chatList);
                        messageAdapter = new MessageAdapter(ChatPage.this, chatList);
                        chatMessagesDisplay.setAdapter(messageAdapter);
                        messageAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
        messageAdapter.notifyDataSetChanged();
    }

    private void setMessageDetails() {
        Map<String, Object> messageRec = new HashMap<>();
        Map<String, Object> message = new HashMap<>();
        UUID hash = UUID.randomUUID();
        message.put("message", textDesc.getText().toString());
        message.put("audio", "Sample audio");
        message.put("image", "sample image");
        message.put("receiver", name);
        message.put("sender", FirebaseAuth.getInstance().getCurrentUser().getEmail());
        message.put("timestamp", FieldValue.serverTimestamp());

        messageRec.put(hash.toString(), message);
        db.collection("Messages").document(sender).set(messageRec, SetOptions.merge());
        messageAdapter.notifyDataSetChanged();

    }

    private void getData() {
        if (getIntent().hasExtra("myImage") && getIntent().hasExtra("description") && getIntent().hasExtra("receiver")) {
            name = getIntent().getStringExtra("receiver");
            description = getIntent().getStringExtra("description");
            image = getIntent().getIntExtra("myImage", 1);
            sender = getIntent().getStringExtra("sender");
        } else {
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