package com.example.geekfindlove;

public class UserInformation {
    private String id;
    private String fn;
    private String ln;
    private String email;
    private String gender;
    private boolean finished_registration;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFn() {
        return fn;
    }

    public void setFn(String fn) {
        this.fn = fn;
    }

    public String getLn() {
        return ln;
    }

    public void setLn(String ln) {
        this.ln = ln;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public boolean isFinished_registration() {
        return finished_registration;
    }

    public void setFinished_registration(boolean finished_registration) {
        this.finished_registration = finished_registration;
    }

    public UserInformation(String fn, String ln, String email, String gender, String id) {
        this.fn = fn;
        this.ln = ln;
        this.email = email;
        this.gender = gender;
        this.finished_registration=false;
    }

    public String toString() {
        return "Name: " + this.fn + " " + this.ln + " Email: " + this.email + " Gender: " + this.gender;
    }

}
