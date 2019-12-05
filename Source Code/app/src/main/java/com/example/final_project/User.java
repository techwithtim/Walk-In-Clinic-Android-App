package com.example.final_project;
import androidx.annotation.NonNull;

import java.util.List;

// Firebase imports
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;


public class User extends Person {

    private String userRole;

    private static DatabaseReference databaseUsers;

    public User(String name, String username, String password){
        this.name = name;
        this.username = username;
        this.password = password;
        this.userRole = "";
        this.id = "";
        init();
    }

    public User(String username, String password){
        this.username = username;
        this.password = password;
        this.name = null;
        this.id = "";
        init();
    }

    private void init(){
        try{
            databaseUsers = FirebaseDatabase.getInstance().getReference("users");
        }catch(Exception e){}
    }


    public void addRole(String role){
        this.userRole = role;
    }

    public String getRole(){
        return this.userRole;
    }

    public boolean save(){
        if (this.name != null && this.username != null && this.password!=null && this.userRole != null){
            String id = databaseUsers.push().getKey();  // get unique database key
            DataBaseUser DB = new DataBaseUser(id, name, username, password, userRole);
            this.id = id;
            databaseUsers.child(id).setValue(DB);  // save in database
            return true;
        }
        return false;
    }


    @Override
    public boolean login() {
        return false;
    }

}