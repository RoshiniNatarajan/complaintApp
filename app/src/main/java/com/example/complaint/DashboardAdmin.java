package com.example.complaint;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class DashboardAdmin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboardadmin);

        Button buttonRequests = findViewById(R.id.buttonRequests);
        Button buttonFeedback = findViewById(R.id.buttonFeedback);

        buttonRequests.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardAdmin.this, RequestActivity.class);
            startActivity(intent);
        });

        buttonFeedback.setOnClickListener(v -> {
            Log.d("DashboardAdmin", "Feedback button clicked");
            Intent intent = new Intent(DashboardAdmin.this, FeedbackDisplayActivity.class);
            startActivity(intent);
        });
    }
}
