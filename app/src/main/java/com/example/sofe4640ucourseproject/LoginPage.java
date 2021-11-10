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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginPage extends AppCompatActivity implements View.OnClickListener{

    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    Button loginBtn, registerBtn, forgotButton;
    private EditText validEmail, validPassword;

    private final static int RC_SIGN_IN =1;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        loginBtn = (Button) findViewById(R.id.login_btn);
        loginBtn.setOnClickListener(this);
        registerBtn = (Button) findViewById(R.id.register_btn);
        registerBtn.setOnClickListener(this);
        validEmail = (EditText) findViewById(R.id.user_email);
        validPassword = (EditText) findViewById(R.id.user_password);
        forgotButton = (Button) findViewById(R.id.forgot_password_btn);
        forgotButton.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();

//        createRequest();

        findViewById(R.id.google_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

    }

//    private void createRequest() {
//        // Configure Google Sign In
//        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestIdToken(getString(R.string.default_web_client_id))
//                .requestEmail()
//                .build();
//
//        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
//    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(getBaseContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {

        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent intent = new Intent(getApplicationContext(), HomePage.class);
                            startActivity(intent);

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginPage.this, "Unable To Login With Google", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.forgot_password_btn:
                Intent navigateToResetPassword = new Intent(getApplicationContext(), ForgotPasswordPage.class);
                startActivity(navigateToResetPassword);
                break;
            case R.id.register_btn:
                Intent intent = new Intent(getApplicationContext(), RegisterPage.class);
                startActivity(intent);
                break;
            case R.id.login_btn:
                loginUser();
                break;
        }
    }

    private void loginUser() {

        String email = validEmail.getText().toString().trim();
        String password = validPassword.getText().toString().trim();

        if(email.isEmpty()){
            validEmail.setError("Email Field Cannot Be Empty ");
            validEmail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            validEmail.setError("Valid Email is required");
            validEmail.requestFocus();
            return;
        }
        if(password.isEmpty()){
            validPassword.setError("Password Field Cannot Be Empty ");
            validPassword.requestFocus();
            return;
        }
        if(password.length() < 6){
            validPassword.setError("Password is not valid");
            validPassword.requestFocus();
            return;
        }

        //authenticate user
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                     Toast.makeText(getBaseContext(), "Login Successful", Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(getApplicationContext(), HomePage.class));
                }else{
                    Toast.makeText(getBaseContext(), "Not A Valid User, Please Register", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.
                INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        return true;
    }
}

