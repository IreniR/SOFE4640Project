package com.example.sofe4640ucourseproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordPage extends AppCompatActivity {

    Button resetPasswordBtn;
    ImageButton backToLoginPage;
    EditText resetPasswordEmail;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password_page);

        resetPasswordBtn = (Button) findViewById(R.id.reset_password_button);
        backToLoginPage = (ImageButton) findViewById(R.id.imageButton);

        resetPasswordEmail = (EditText) findViewById(R.id.forgot_email_field);

        mAuth = FirebaseAuth.getInstance();

        backToLoginPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getBaseContext(), LoginPage.class));
            }
        });

        resetPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailReset = resetPasswordEmail.getText().toString().trim();

                if(emailReset.isEmpty()) {
                    Toast.makeText(getBaseContext(), "Email Field Cannot Be Empty", Toast.LENGTH_SHORT);
                }else{
                    mAuth.sendPasswordResetEmail(emailReset).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if(task.isSuccessful()){
                                Toast.makeText(ForgotPasswordPage.this, "Please Check Your Email Account", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getBaseContext(), LoginPage.class));
                            }else{
                                String msg = task.getException().getMessage();
                                Toast.makeText(ForgotPasswordPage.this, "An Error Occured: " + msg, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

    }
}
