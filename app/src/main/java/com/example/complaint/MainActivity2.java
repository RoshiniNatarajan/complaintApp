package com.example.complaint;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity2 extends AppCompatActivity {

    ImageButton adminButton;
    ImageButton staffButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        adminButton = findViewById(R.id.admin);
        staffButton = findViewById(R.id.staffButton);

        adminButton.setOnClickListener(v -> {
            // Navigate to Admin login page
            Intent intent = new Intent(MainActivity2.this, AdminLoginActivity.class);
            startActivity(intent);
        });

        staffButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to Staff login page
                Intent intent = new Intent(MainActivity2.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
