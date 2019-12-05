package com.example.final_project;

import android.provider.ContactsContract;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Used strictly for saving user data to the database
 */

public class DataBaseService implements Serializable {
    private String _name;
    private Category _category;
    private String _id;
    private ServiceRole _role;

    public DataBaseService(String id, String name, ServiceRole role){
        this._name = name;
        this._id = id;
        this._role = role;
    }

    public DataBaseService(String id, String name, ServiceRole role, Category category){
        this._name = name;
        this._category = category;
        this._role = role;
        this._id = id;
    }

    public void print(){
        System.out.println("Name: " + _name + ", Role: " + _role + ", ID: " + _id);
    }

    public DataBaseService(){}

    public void setName(String name){_name = name;}
    public String getName() {return _name;}
    public void setId(String id){_id = id;}
    public String getId(){return _id;}
    public void setCategory(Category category){this._category = category;}
    public Category getCategory(){return _category;}
    public void setRole(ServiceRole role){this._role = role;}
    public ServiceRole getRole(){return this._role;}
}




