package com.example.final_project;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Service {
    private String name;
    private Category category;
    private ServiceRole role;
    private static DatabaseReference databaseServices = FirebaseDatabase.getInstance().getReference("services");

    public Service(String name, ServiceRole role){
        this.name = name;
        this.role = role;
        this.category = Category.GENERAL;
    }

    public Service(String name, ServiceRole role, Category category){
        this.name = name;
        this.role = role;
        this.category = category;
    }

    public String getName(){
        return this.name;
    }

    public Category getCategory(){
        return this.category;
    }

    public ServiceRole getRole(){
        return this.role;
    }

    public void setName(String name){
        this.name  = name;
    }

    public void setCategory(Category category){
        this.category = category;
    }

    public void setRole(ServiceRole role){
        this.role = role;
    }

    public boolean save(){
        if (this.name != null && this.category != null && this.role != null){
            String id = databaseServices.push().getKey();  // get unique database key
            DataBaseService DB = new DataBaseService(id, name, role, category);
            databaseServices.child(id).setValue(DB);  // save in database
            return true;
        }
        return false;
    }
}
