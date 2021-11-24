package com.example.sofe4640ucourseproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class addFriendPage extends AppCompatActivity {

    ImageButton addFriend;
    EditText searchFriend;
    String token;
    private FirebaseAuth mAuth;

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addfriendpage);

        addFriend = findViewById(R.id.veriFriendButton);
        searchFriend = findViewById(R.id.friendEmail);

        addFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                token=searchFriend.getText().toString();
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                mAuth = FirebaseAuth.getInstance();
                FirebaseUser currentUser = mAuth.getCurrentUser();

                DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users");
                Query friendQuery = usersRef.orderByChild("email").equalTo(token);

                friendQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot friendSnapshot : dataSnapshot.getChildren()) {
                            System.out.println(friendSnapshot.getKey());
                            System.out.println(friendSnapshot.child("name").getValue(String.class));
                            if (dataSnapshot.getChildrenCount()>0) {
                                Map<String, Object> friends = new HashMap<>();
                                friends.put(token,friendSnapshot.child("name").getValue(String.class));
                                db.collection("Friends").document(currentUser.getEmail())
                                        .set(friends, SetOptions.merge());

                                Toast.makeText(addFriendPage.this, friendSnapshot.child("name").getValue(String.class) + " was added!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(addFriendPage.this,HomePage.class));
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        throw databaseError.toException();
                    }
                });
            }
        });

    }
}
