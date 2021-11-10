package com.example.sofe4640ucourseproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterPage extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth mAuth;
    private EditText registerUserEmail, registerPassword, registerConfirmPassword, registerName;
    private Button registerUserBtn, returnToLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_page);

        registerName = (EditText) findViewById(R.id.register_name);
        registerUserEmail = (EditText) findViewById(R.id.register_email);
        registerPassword = (EditText) findViewById(R.id.register_password);
        registerConfirmPassword = (EditText) findViewById(R.id.register_confirm_psswrd);

        registerUserBtn = (Button) findViewById(R.id.register_user_btn);
        returnToLogin = (Button) findViewById(R.id.alreadyMemberBtn);


        mAuth = FirebaseAuth.getInstance();
        
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.alreadyMemberBtn:
                startActivity(new Intent(getApplicationContext(), LoginPage.class));
                break;
            case R.id.register_user_btn:
                registerUser();
                break;
        }
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.
                INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        return true;
    }

    private void registerUser() {

        String email = registerUserEmail.getText().toString().trim();
        String name = registerName.getText().toString().trim();
        String password = registerPassword.getText().toString().trim();
        String confirmPassword = registerConfirmPassword.getText().toString().trim();


        if(email.isEmpty()){
            registerUserEmail.setError("Email Field Cannot Be Empty");
            registerUserEmail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            registerUserEmail.setError("Enter Valid Email");
            registerUserEmail.requestFocus();
            return;
        }

        if(password.isEmpty()){
            registerPassword.setError("Password Field Cannot Be Empty");
            registerPassword.requestFocus();
            return;
        }
        if(password.length() < 6){
            registerPassword.setError("Password Needs to be at least 6 characters long");
            registerPassword.requestFocus();
            return;
        }

        if(confirmPassword.isEmpty()){
            registerConfirmPassword.setError("Confirm Password Field Cannot Be Empty");
            registerConfirmPassword.requestFocus();
            return;
        }
        if(!password.equals(confirmPassword)){
            registerConfirmPassword.setError("Password and Confirm Password must be the same");
            registerConfirmPassword.requestFocus();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, confirmPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    User user = new User(name, email, mAuth.getUid());
                    FirebaseDatabase.getInstance().getReference("Users").child(mAuth.getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(getBaseContext(), "Register Successful", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), LoginPage.class));
                            }else{
                                Toast.makeText(getBaseContext(), "Register Unsuccessful", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else{
                    Toast.makeText(getBaseContext(), "Register Unsuccessful", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}
