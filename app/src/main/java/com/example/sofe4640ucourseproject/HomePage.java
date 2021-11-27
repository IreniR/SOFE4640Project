package com.example.sofe4640ucourseproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class HomePage extends AppCompatActivity {
    RecyclerView recyclerView;
    TextView userName;
    Button logOutUserBtn;
    ImageButton addUser;
    String recyclerName[], recyclerDescription[];


    private FirebaseAuth mAuth;

    int images[] = {R.drawable.userimage,R.drawable.userimage,R.drawable.userimage};
    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);

        List<String> list = new ArrayList<>();

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        DocumentReference codesRef = rootRef.collection("Friends").document(currentUser.getEmail());
        codesRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {

                    Map<String, Object> map = task.getResult().getData();

                    if(map != null){
                        for (Map.Entry<String, Object> entry : map.entrySet()) {
                            list.add(entry.getKey());

                            Log.d("TAG", entry.getKey());
                        }
                    }
                }else{
                    System.out.println("Task was unsuccessful");
                }
                String[] stockArr = new String[list.size()];
                stockArr = list.toArray(stockArr);
                setupView(stockArr);
            }
        });

        addUser = (ImageButton) findViewById(R.id.addUserButton);
        //addUser.findViewById(R.id.addUserButton);
        logOutUserBtn = findViewById(R.id.logout_btn);
        userName = findViewById(R.id.user_name);

        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(this);

        if(signInAccount != null){
            userName.setText(signInAccount.getEmail());
        }

        logOutUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), LoginPage.class);
                startActivity(intent);
            }
        });

        addUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomePage.this,addFriendPage.class));
            }
        });
    }
    public void setupView(String[] stockArr){
        recyclerView = findViewById(R.id.recyclerView_friends);

        recyclerName = stockArr;
        recyclerDescription = new String[] {"welcome to Chat","welcome to Chat","welcome to Chat"};
        UserAdapter myAdapter = new UserAdapter(this, recyclerName, recyclerDescription, images);
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
