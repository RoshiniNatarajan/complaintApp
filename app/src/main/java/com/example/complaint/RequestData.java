// RequestData.java
package com.example.complaint;

public class RequestData {
    private String id;
    private String staffName;
    private String roomNo;
    private String systemIssue;
    private String email;
    private String contactNo;
    private String currentDate;
    private boolean solved;

    // Default constructor required for calls to DataSnapshot.getValue(RequestData.class)
    public RequestData() {
    }

    public RequestData(String id, String staffName, String roomNo, String systemIssue, String email, String contactNo, String currentDate) {
        this.id = id;
        this.staffName = staffName;
        this.roomNo = roomNo;
        this.systemIssue = systemIssue;
        this.email = email;
        this.contactNo = contactNo;
        this.currentDate = currentDate;
        this.solved = false;
    }

    public String getId() {
        return id;
    }

    public String getStaffName() {
        return staffName;
    }

    public String getRoomNo() {
        return roomNo;
    }

    public String getSystemIssue() {
        return systemIssue;
    }

    public String getEmail() {
        return email;
    }

    public String getContactNo() {
        return contactNo;
    }

    public String getcurrentDate() {
        return currentDate;
    }

    public boolean isSolved() {
        return solved;
    }

    public void setSolved(boolean solved) {
        this.solved = solved;
    }
}
