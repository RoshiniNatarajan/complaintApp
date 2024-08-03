package com.example.complaint;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class FeedbackFormActivity extends AppCompatActivity {

    private EditText editTextStaffName, editTextDepartment, editTextRoomNo, editTextEmail, editTextMobileNo, editTextServiceFeedback;
    private List<FeedbackData> allFeedbacks;
    private DatabaseReference databaseReference;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_form);

        editTextStaffName = findViewById(R.id.editTextStaffName);
        editTextDepartment = findViewById(R.id.editTextDepartment);
        editTextRoomNo = findViewById(R.id.editTextRoomNo);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextMobileNo = findViewById(R.id.editTextMobileNo);
        editTextServiceFeedback = findViewById(R.id.editTextServiceFeedback);
        Button buttonSubmitFeedback = findViewById(R.id.buttonSubmitFeedback);

        allFeedbacks = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("feedbacks");

        buttonSubmitFeedback.setOnClickListener(v -> submitFeedback());
    }

    private void submitFeedback() {
        String staffName = editTextStaffName.getText().toString().trim();
        String department = editTextDepartment.getText().toString().trim();
        String roomNo = editTextRoomNo.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String mobileNo = editTextMobileNo.getText().toString().trim();
        String serviceFeedback = editTextServiceFeedback.getText().toString().trim();

        if (staffName.isEmpty() || department.isEmpty() || roomNo.isEmpty() || email.isEmpty() || mobileNo.isEmpty() || serviceFeedback.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        FeedbackData feedbackData = new FeedbackData(staffName, department, roomNo, email, mobileNo, serviceFeedback);
        String feedbackId = databaseReference.push().getKey();

        if (feedbackId != null) {
            databaseReference.child(feedbackId).setValue(feedbackData).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(FeedbackFormActivity.this, "Feedback submitted successfully", Toast.LENGTH_SHORT).show();
                    allFeedbacks.add(feedbackData);  // Ensure the list is initialized before this
                } else {
                    Toast.makeText(FeedbackFormActivity.this, "Failed to submit feedback", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
