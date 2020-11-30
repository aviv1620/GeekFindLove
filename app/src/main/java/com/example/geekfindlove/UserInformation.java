package com.example.geekfindlove;

public class UserInformation {
    private String id;
    private String fn;
    private String ln;
    private String email;
    private String gender;
    private boolean isAdmin = false;

    public boolean isAdmin() { return isAdmin; }

    public void setAdmin(boolean admin) { isAdmin = admin; }

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

    public UserInformation(){

    }

    public UserInformation(String fn, String ln, String email, String gender, String id) {
        this.fn = fn;
        this.ln = ln;
        this.email = email;
        this.gender = gender;
    }

    public String toString() {
        return "Name: " + this.fn + " " + this.ln + " Email: " + this.email + " Gender: " + this.gender;
    }

}
