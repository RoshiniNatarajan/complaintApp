package com.example.complaint;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class Login_screen extends AppCompatActivity {

    private TextInputEditText etEmail, etPassword;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        etEmail = findViewById(R.id.l_et_email);
        etPassword = findViewById(R.id.l_et_password);
        Button loginBtn = findViewById(R.id.loginBtn);
        TextView tvForgotPassword = findViewById(R.id.tv_forgot_password);

        // Check if the user is already logged in
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // User is already logged in, navigate to MainActivity3
            startActivity(new Intent(Login_screen.this, MainActivity3.class));
            finish(); // Finish the Login_screen activity
        }

        loginBtn.setOnClickListener(view -> loginUser());
        tvForgotPassword.setOnClickListener(view -> forgotPassword());
    }

    private void loginUser() {
        String email = Objects.requireNonNull(etEmail.getText()).toString().trim();
        String password = Objects.requireNonNull(etPassword.getText()).toString().trim();

        // Check if email and password fields are not empty
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show();
            return;
        }

        // Sign in user with email and password
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        FirebaseUser user = mAuth.getCurrentUser();
                        Toast.makeText(Login_screen.this, "Login successful", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Login_screen.this, MainActivity3.class);
                        startActivity(intent);
                        finish();
                        // Proceed to your next activity or perform desired action
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(Login_screen.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void forgotPassword() {
        String email = Objects.requireNonNull(etEmail.getText()).toString().trim();

        if (email.isEmpty()) {
            Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(Login_screen.this, "Password reset email sent to " + email, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(Login_screen.this, "Failed to send password reset email", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
