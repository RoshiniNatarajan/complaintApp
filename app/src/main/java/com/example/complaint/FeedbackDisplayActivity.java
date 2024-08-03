package com.example.complaint;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FeedbackDisplayActivity extends AppCompatActivity {

    private List<FeedbackData> allFeedbacks;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_feedback_display);

        allFeedbacks = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("feedbacks");
        Button buttonBack = findViewById(R.id.buttonBack);
        buttonBack.setOnClickListener(v -> finish());

        fetchFeedbacksFromFirebase();
    }

    private void fetchFeedbacksFromFirebase() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                allFeedbacks.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    FeedbackData feedbackData = dataSnapshot.getValue(FeedbackData.class);
                    if (feedbackData != null) {
                        allFeedbacks.add(feedbackData);
                    }
                }
                displayFeedbacks();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(FeedbackDisplayActivity.this, "Failed to fetch feedbacks", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void displayFeedbacks() {
        LinearLayout feedbackLayout = findViewById(R.id.issLayout);
        feedbackLayout.removeAllViews();

        for (FeedbackData feedbackData : allFeedbacks) {
            LinearLayout feedbackItemLayout = new LinearLayout(this);
            feedbackItemLayout.setOrientation(LinearLayout.VERTICAL);
            feedbackItemLayout.setPadding(16, 16, 16, 16);
            feedbackItemLayout.setBackgroundColor(getResources().getColor(android.R.color.white));
            feedbackItemLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));

            TextView textViewStaffName = new TextView(this);
            textViewStaffName.setText("Staff Name: " + feedbackData.getStaffName());
            feedbackItemLayout.addView(textViewStaffName);

            TextView textViewDepartment = new TextView(this);
            textViewDepartment.setText("Department: " + feedbackData.getDepartment());
            feedbackItemLayout.addView(textViewDepartment);

            TextView textViewRoomNo = new TextView(this);
            textViewRoomNo.setText("Room No: " + feedbackData.getRoomNo());
            feedbackItemLayout.addView(textViewRoomNo);

            TextView textViewEmail = new TextView(this);
            textViewEmail.setText("Email: " + feedbackData.getEmail());
            feedbackItemLayout.addView(textViewEmail);

            TextView textViewMobileNo = new TextView(this);
            textViewMobileNo.setText("Mobile No: " + feedbackData.getMobileNo());
            feedbackItemLayout.addView(textViewMobileNo);

            TextView textViewServiceFeedback = new TextView(this);
            textViewServiceFeedback.setText("Service Feedback: " + feedbackData.getServiceFeedback());
            feedbackItemLayout.addView(textViewServiceFeedback);

            feedbackLayout.addView(feedbackItemLayout);
        }
    }
}
