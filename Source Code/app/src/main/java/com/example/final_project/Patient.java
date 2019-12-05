package com.example.final_project;

public class Patient extends User {

    public Patient(String name, String username, String password){
        super(name,username, password);
        super.addRole("Patient");
    }

    public Patient(String username, String password){
        super(username, password);
        super.addRole("Patient");
    }

}