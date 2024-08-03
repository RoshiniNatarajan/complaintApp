package com.example.complaint;

public class FeedbackData {
    private String staffName;
    private String department;
    private String roomNo;
    private String email;
    private String mobileNo;
    private String serviceFeedback;

    // Default constructor required for calls to DataSnapshot.getValue(FeedbackData.class)
    public FeedbackData() {
        // No-argument constructor
    }

    // Parametrized constructor for creating new FeedbackData objects
    public FeedbackData(String staffName, String department, String roomNo, String email, String mobileNo, String serviceFeedback) {
        this.staffName = staffName;
        this.department = department;
        this.roomNo = roomNo;
        this.email = email;
        this.mobileNo = mobileNo;
        this.serviceFeedback = serviceFeedback;
    }

    // Getters and Setters
    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(String roomNo) {
        this.roomNo = roomNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getServiceFeedback() {
        return serviceFeedback;
    }

    public void setServiceFeedback(String serviceFeedback) {
        this.serviceFeedback = serviceFeedback;
    }
}
