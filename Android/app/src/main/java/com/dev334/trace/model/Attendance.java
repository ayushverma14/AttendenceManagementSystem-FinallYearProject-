package com.dev334.trace.model;

import com.dev334.trace.util.app.AppConfig;

import java.util.Date;
import java.util.List;

public class Attendance {
    private String date;
    private String department, semester, subject, token;
    private List<String> pictures;

    public Attendance(String date, String department, String semester, String subject,
                      List<String> urls, String token) {
        this.date = date;
        this.department = department;
        this.semester = semester;
        this.subject = subject;
        this.pictures = urls;
        this.token = token;
    }
}
