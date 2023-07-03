package com.example.tasker.Model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserModel {
    private String id;
    private String email;
    private String username;

    public UserModel() {
    }

    public UserModel(String id, String email, String username) {
        this.id = id;
        this.email = email;
        this.username = username;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void saveUser(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("users").child(getId()).setValue(this);
    }
}
