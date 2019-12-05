package com.example.final_project;

import android.provider.ContactsContract;

import java.io.Serializable;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Used strictly for saving user data to the database
 */
public class DataBaseUser implements Serializable {
    private String _name;
    private String _username;
    private String _password;
    private String _id;
    private String _role;
    private String _clinicId;
    private ArrayList<String> openHours, closeHours;
    private ArrayList<Integer> notWorkingDays;

    public DataBaseUser(String id, String name, String username, String password, String role){
        this._name = name;
        this._username = username;
        this._password = password;
        this._role = role;
        this._id = id;
        this._clinicId = "";
        this.openHours = new ArrayList();
        this.closeHours = new ArrayList();
        this.notWorkingDays = new ArrayList<>();
    }

    public DataBaseUser(String id, String username, String password, String role){
        this._username = username;
        this._password = password;
        this._role = role;
        this._id = id;
        this._clinicId = "";
        this.openHours = new ArrayList();
        this.closeHours = new ArrayList();
        this.notWorkingDays = new ArrayList<>();
    }

    public DataBaseUser(String id,String username, String password){
        this._username = username;
        this._password = password;
        this._role = "Admin";
        this._id = id;
        this._clinicId = "";
        this.openHours = new ArrayList();
        this.closeHours = new ArrayList();
        this.notWorkingDays = new ArrayList<>();
    }

    public DataBaseUser(){}

    public void setName(String name){_name = name;}
    public String getName() {return _name;}
    public void setId(String id){_id = id;}
    public String getId(){return _id;}
    public void setUsername(String username){_username = username;}
    public String getUsername(){return _username;}
    public void setPassword(String password){_password = password;}
    public String getPassword() {return _password;}
    public void setRole(String role){this._role = role;}
    public String getRole(){return _role;}
    public String getClinicId(){return _clinicId;}
    public void setClinicId(String id){_clinicId = id;}
    public void setOpenHours(ArrayList<String> opens){this.openHours = opens;}
    public ArrayList<String> getOpenHours(){return this.openHours;}
    public void setCloseHours(ArrayList<String> closes){this.closeHours = closes;}
    public ArrayList<String> getCloseHours(){return this.closeHours;}
    public void setNotWorkingDays (ArrayList<Integer> n){notWorkingDays = n;}
    public ArrayList<Integer> getNotWorkingDays (){return notWorkingDays;}
}
