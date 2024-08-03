package com.example.complaint;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AdminLoginActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminlogin);

        // Retrieve the SharedPreferences instance using the class-level variable
        sharedPreferences = getSharedPreferences("AdminLogin", MODE_PRIVATE);

        Button buttonLogin = findViewById(R.id.buttonLogin);

        buttonLogin.setOnClickListener(v -> login());
    }

    private void login() {
        String username = "admin"; // Simulating successful login
        String password = "p@$$word"; // Simulating successful login

        // Validate username and password
        if (username.equals("admin") && password.equals("p@$$word")) {
            // Check if the SharedPreferences instance is not null
            if (sharedPreferences != null) {
                // Set loggedIn flag to true
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("loggedIn", true);
                editor.apply();

                // Proceed to request activity
                Intent intent = new Intent(AdminLoginActivity.this, DashboardAdmin.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, "SharedPreferences is null", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show();
        }
    }
}
