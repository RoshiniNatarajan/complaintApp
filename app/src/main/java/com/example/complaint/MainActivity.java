package com.example.complaint;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class  MainActivity extends AppCompatActivity {

    private TextInputEditText etEmail, etPassword, etConfirmPassword;

    // Firebase
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("users");

        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        etConfirmPassword = findViewById(R.id.et_cpassword);
        Button btnSignup = findViewById(R.id.btn_signup);
        Button btnAlreadyUser = findViewById(R.id.already_user);

        btnSignup.setOnClickListener(view -> signUpUser());

        // Handle already user button click
        btnAlreadyUser.setOnClickListener(view -> loginPageOpen());
    }

    private void signUpUser() {
        final String email = Objects.requireNonNull(etEmail.getText()).toString().trim();
        final String password = Objects.requireNonNull(etPassword.getText()).toString().trim();
        String confirmPassword = Objects.requireNonNull(etConfirmPassword.getText()).toString().trim();

        if (!password.equals(confirmPassword)) {
            etConfirmPassword.setError("Passwords do not match");
            return;
        }

        if (!isPasswordValid(password)) {
            etPassword.setError("Password must contain at least one uppercase letter, one lowercase letter, one number, and one special character");
            return;
        }

        // Create user with email and password
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        FirebaseUser user = mAuth.getCurrentUser();

                        // Store user details in database
                        if (user != null) {
                            String userId = user.getUid();
                            String userEmail = user.getEmail();

                            // Store user details in database
                            mDatabase.child(userId).child("email").setValue(email);
                            mDatabase.child(userId).child("password").setValue(password);

                            Toast.makeText(MainActivity.this, "User registered successfully", Toast.LENGTH_SHORT).show();

                            // Clear fields
                            etEmail.setText("");
                            etPassword.setText("");
                            etConfirmPassword.setText("");

                            // Move to login page
                            Intent intent = new Intent(MainActivity.this, Login_screen.class);
                            startActivity(intent);
                            finish(); // Finish current activity to prevent returning to it by pressing back button
                        }

                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(MainActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void loginPageOpen() {
        Intent intent = new Intent(MainActivity.this, Login_screen.class);
        startActivity(intent);
    }

    private boolean isPasswordValid(String password) {
        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);
        return matcher.matches();
    }
}
