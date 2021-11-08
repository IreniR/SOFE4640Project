package com.example.sofe4640ucourseproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;

public class HomePage extends AppCompatActivity {

    TextView userName;
    Button logOutUserBtn;

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);

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
    }
}
