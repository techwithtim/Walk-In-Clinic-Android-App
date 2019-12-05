package com.example.final_project;

// Widget imports
import android.widget.EditText;
import android.widget.Button;
import android.widget.AdapterView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.sql.Time;
import java.util.ArrayList;

public class Employee extends User{

    private ArrayList<DataBaseService> walkinclinicServices;
    private WalkInClinic walkInClinic;
    private static DatabaseReference databaseWalkIn;
    private static DatabaseReference databaseUsers;
    private String clinicId;
    private ArrayList<String> openHours, closeHours;
    private ArrayList<Integer> notWorkingDays;

    public Employee(String name, String username, String password, String clinicId){
        super(name, username, password);
        this.clinicId = clinicId;
        init();
    }

    public Employee(String name, String username, String password){
        super(name, username, password);
        super.addRole("Employee");
        init();
    }

    public Employee(String username, String password){
        super(username, password);
        init();
    }

    private void init(){
        try{
            databaseWalkIn = FirebaseDatabase.getInstance().getReference("clinics");
            databaseUsers = FirebaseDatabase.getInstance().getReference("users");
        }catch(Exception e){}
        super.addRole("Employee");
        this.clinicId = clinicId;
        walkInClinic = null;
        openHours = new ArrayList<>();
        closeHours = new ArrayList<>();
        notWorkingDays = new ArrayList<>();
    }

    public void setOpenHours(ArrayList<String> opens){this.openHours = opens;}
    public ArrayList<String> getOpenHours(){return this.openHours;}
    public void setCloseHours(ArrayList<String> closes){this.closeHours = closes;}
    public ArrayList<String> getCloseHours(){return this.closeHours;}

    public ArrayList<Integer> getNotWorkingDays(){return notWorkingDays;}
    public void setNotWorkingDays(ArrayList<Integer> n){notWorkingDays = n;}
    public void setClinicId(String id){this.clinicId = id;}

    public void update(){
        try{
            DatabaseReference dR = databaseUsers.child(id);
            DataBaseUser usr = new DataBaseUser(id, name, username, password, getRole());
            usr.setClinicId(getClinicId());
            usr.setCloseHours(closeHours);
            usr.setOpenHours(openHours);
            usr.setNotWorkingDays(notWorkingDays);
            dR.setValue(usr);
        }catch (Exception ex){
            throw new IllegalArgumentException("Clinic doesn't exist");
        }
    }


    public String getClinicId(){return clinicId;}

    public WalkInClinic getWalkInClinic(){
        return walkInClinic;
    }

    public void setWalkInClinic(WalkInClinic clinic){
        walkInClinic = clinic;
    }

    public void createWalkInClinic(WalkInClinic clinic) throws IllegalStateException{
        if (walkInClinic != null){
            throw new IllegalStateException("Clinic already exists");
        }
        walkInClinic = clinic;
        String id_ = databaseWalkIn.push().getKey();  // get unique database key
        clinic.setId(id_);
        this.clinicId = id_;
        databaseWalkIn.child(id_).setValue(clinic);  // save in database
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("users").child(getId());
        DataBaseUser usr = new DataBaseUser(getId(), get_name(), get_username(), get_password(), getRole());
        usr.setClinicId(id_);
        dR.setValue(usr);
    }

    public void updateWalkinClinic(){
        try{
            DatabaseReference dR = databaseWalkIn.child(walkInClinic.getId());
            dR.setValue(walkInClinic);
        }catch (Exception ex){
            throw new IllegalArgumentException("Clinic doesn't exist");
        }
    }

    public void setOpeningTimes(ArrayList<String> opening) throws IllegalArgumentException{
        if (opening.size() != 7){
            throw new IllegalArgumentException("Length of array must be 7");
        }
        walkInClinic.setOpeningTimes(opening);
    }

    public void setClosingTimes(ArrayList<String> closing) throws IllegalArgumentException{
        if (closing.size() != 7){
            throw new IllegalArgumentException("Length of array must be 7");
        }
        walkInClinic.setClosingTimes(closing);
    }

    public void setClosedDays(ArrayList<Integer> days) throws IllegalArgumentException{
        if (days.size() != 7){
            throw new IllegalArgumentException("Length of array must be 7");
        }
        walkInClinic.setClosedDays(days);
    }

    public ArrayList<String> getClosingTimes(){
        return walkInClinic.getClosingTimes();
    }

    public ArrayList<String> getOpeningTimes(){
        return walkInClinic.getOpeningTimes();
    }

    public ArrayList<Integer> getClosedDays(){return walkInClinic.getClosedDays();}

}