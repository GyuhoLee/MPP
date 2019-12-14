package com.example.gradenotifications;

public class ItemObject {
    private String subject;
    private String grade;

    public ItemObject(String subject, String grade){
        this.subject = subject;
        this.grade = grade;
    }

    public String getSubject(){
        return subject;
    }

    public String getGrade(){
        return grade;
    }
}
