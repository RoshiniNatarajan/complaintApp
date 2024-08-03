package com.example.complaint;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class MainActivity3 extends AppCompatActivity {
    private TextInputEditText editTextStaffName, editTextRoomNo, editTextSystemIssue, editTextEmail, editTextContactNo;
    private List<RequestData> requestDataList;
    private DatabaseReference databaseReference; // Firebase Database Reference

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        editTextStaffName = findViewById(R.id.editTextStaffName);
        editTextRoomNo = findViewById(R.id.editTextRoomNo);
        editTextSystemIssue = findViewById(R.id.editTextSystemIssue);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextContactNo = findViewById(R.id.editTextContactNo);
        DatePicker datePicker = findViewById(R.id.datePicker);
        Button buttonSend = findViewById(R.id.buttonLogin);
        Button close=findViewById(R.id.ButtonClose);
        close.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity3.this, MainActivity3.class);
            startActivity(intent);
            finish();
        });
        requestDataList = getAllStoredRequests();

        // Initialize the database reference
        databaseReference = FirebaseDatabase.getInstance().getReference().child("requests");

        buttonSend.setOnClickListener(v -> storeValues());
    }

    private void storeValues() {
        String staffName = Objects.requireNonNull(editTextStaffName.getText()).toString().trim();
        String roomNo = Objects.requireNonNull(editTextRoomNo.getText()).toString().trim();
        String systemIssue = Objects.requireNonNull(editTextSystemIssue.getText()).toString().trim();
        String email = Objects.requireNonNull(editTextEmail.getText()).toString().trim();
        String contactNo = Objects.requireNonNull(editTextContactNo.getText()).toString().trim();
        Calendar calendar = Calendar.getInstance();
        String currentDate = String.format("%d/%02d/%02d", calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));

        if (staffName.isEmpty() || roomNo.isEmpty() || systemIssue.isEmpty() || email.isEmpty() || contactNo.isEmpty()) {
            Toast.makeText(MainActivity3.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
        } else {
            String id = databaseReference.push().getKey(); // Generate a unique ID
            RequestData newRequest = new RequestData( id,staffName, roomNo, systemIssue, email, contactNo, currentDate);
            requestDataList.add(newRequest);

            new AlertDialog.Builder(MainActivity3.this)
                    .setTitle("Confirm")
                    .setMessage("Send these issues to the admin?")
                    .setPositiveButton("Send", (dialog, which) -> sendRequestsToAdmin())
                    .setNegativeButton("Cancel", null)
                    .show();
        }
    }

    private void sendRequestsToAdmin() {
        SharedPreferences sharedPreferences = getSharedPreferences("RequestData", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String requestDataJson = gson.toJson(requestDataList);
        editor.putString("requestDataList", requestDataJson);
        editor.apply();

        for (RequestData requestData : requestDataList) {
            if (requestData.getId() != null) {
                databaseReference.child(requestData.getId()).setValue(requestData)
                        .addOnSuccessListener(aVoid -> {
                            // Request successfully saved
                            Toast.makeText(MainActivity3.this, "Request sent to admin", Toast.LENGTH_SHORT).show();
                        })
                        .addOnFailureListener(e -> {
                            // Failed to save request
                            Toast.makeText(MainActivity3.this, "Failed to send request: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
            }
        }
    }

    private List<RequestData> getAllStoredRequests() {
        List<RequestData> storedRequests = new ArrayList<>();
        SharedPreferences sharedPreferences = getSharedPreferences("RequestData", MODE_PRIVATE);
        String requestDataJson = sharedPreferences.getString("requestDataList", null);

        if (requestDataJson != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<RequestData>>() {}.getType();
            storedRequests = gson.fromJson(requestDataJson, type);
        }

        return storedRequests;
    }
}
