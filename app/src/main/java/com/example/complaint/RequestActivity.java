package com.example.complaint;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RequestActivity extends AppCompatActivity {

    private List<RequestData> allRequests;
    private DatabaseReference databaseReference;
    private final String CHANNEL_ID = "issue_solved_channel";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);

        allRequests = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("requests");

        Button buttonBack = findViewById(R.id.buttonBack);
        buttonBack.setOnClickListener(v -> finish());

        fetchRequestsFromFirebase();
        createNotificationChannel(); // Ensure the notification channel is created
    }

    private void fetchRequestsFromFirebase() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                allRequests.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    RequestData requestData = dataSnapshot.getValue(RequestData.class);
                    if (requestData != null) {
                        allRequests.add(requestData);
                    }
                }
                displayRequests();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(RequestActivity.this, "Failed to fetch data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayRequests() {
        LinearLayout issuesLayout = findViewById(R.id.issuesLayout);
        issuesLayout.removeAllViews();

        for (RequestData requestData : allRequests) {
            LinearLayout requestLayout = new LinearLayout(this);
            requestLayout.setOrientation(LinearLayout.VERTICAL);
            requestLayout.setPadding(16, 16, 16, 16);
            requestLayout.setBackgroundColor(getResources().getColor(android.R.color.white));

            TextView textViewStaffName = new TextView(this);
            textViewStaffName.setText("Staff Name: " + requestData.getStaffName());
            requestLayout.addView(textViewStaffName);

            TextView textViewRoomNo = new TextView(this);
            textViewRoomNo.setText("Room No: " + requestData.getRoomNo());
            requestLayout.addView(textViewRoomNo);

            TextView textViewSystemIssue = new TextView(this);
            textViewSystemIssue.setText("Issue: " + requestData.getSystemIssue());
            requestLayout.addView(textViewSystemIssue);

            TextView textViewEmail = new TextView(this);
            textViewEmail.setText("Email: " + requestData.getEmail());
            requestLayout.addView(textViewEmail);

            TextView textViewContactNo = new TextView(this);
            textViewContactNo.setText("Contact No: " + requestData.getContactNo());
            requestLayout.addView(textViewContactNo);

            TextView textViewCurrentDate = new TextView(this);
            textViewCurrentDate.setText("Selected Date: " + requestData.getcurrentDate());
            requestLayout.addView(textViewCurrentDate);

            Button buttonSolved = new Button(this);
            buttonSolved.setText("Solved");
            buttonSolved.setOnClickListener(v -> markRequestAsSolved(requestData, buttonSolved));
            requestLayout.addView(buttonSolved);

            if (requestData.isSolved()) {
                buttonSolved.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
                buttonSolved.setEnabled(false);
            } else {
                buttonSolved.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
                buttonSolved.setEnabled(true);
            }

            issuesLayout.addView(requestLayout);
        }
    }

    private void markRequestAsSolved(RequestData requestData, Button buttonSolved) {
        requestData.setSolved(true);
        databaseReference.child(requestData.getId()).setValue(requestData)
                .addOnSuccessListener(aVoid -> {
                    ((LinearLayout) buttonSolved.getParent()).removeAllViews();
                    sendNotificationToUser(requestData); // Send notification after updating Firebase
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(RequestActivity.this, "Failed to update request: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void sendNotificationToUser(RequestData requestData) {
        String message = "Your system issue has been solved! Please fill out the feedback form.";

        Intent intent = new Intent(this, FeedbackFormActivity.class);
        intent.putExtra("enableFeedbackForm", true);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Use these flags

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("Issue Solved")
                .setContentText(message)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setVibrate(new long[]{1000, 1000});

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(1, notificationBuilder.build());
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Issue Solved Notifications";
            String description = "Notifications for solved issues";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
