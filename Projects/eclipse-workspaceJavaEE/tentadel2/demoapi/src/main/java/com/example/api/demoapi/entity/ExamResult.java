package com.example.api.demoapi.entity;

public class ExamResult {
    private String student;
    private String grade;

    public ExamResult() {
    }

    public ExamResult(String student, String grade) {
        this.student = student;
        this.grade = grade;
    }

    public String getStudent() {
        return student;
    }

    public void setStudent(String student) {
        this.student = student;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }
}
