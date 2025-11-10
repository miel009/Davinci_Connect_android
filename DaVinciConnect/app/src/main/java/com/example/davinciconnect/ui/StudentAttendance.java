package com.example.davinciconnect.ui;

public class StudentAttendance {
    private String studentName;
    private boolean isPresent;
    private boolean isAbsent;

    public StudentAttendance(String studentName) {
        this.studentName = studentName;
        this.isPresent = false;
        this.isAbsent = false;
    }

    public String getStudentName() {
        return studentName;
    }

    public boolean isPresent() {
        return isPresent;
    }

    public void setPresent(boolean present) {
        isPresent = present;
    }

    public boolean isAbsent() {
        return isAbsent;
    }

    public void setAbsent(boolean absent) {
        isAbsent = absent;
    }
}
