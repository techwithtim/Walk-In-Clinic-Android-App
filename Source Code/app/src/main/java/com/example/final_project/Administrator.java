package com.example.final_project;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;

public class Administrator extends Person{

    private int numOfActiveUsers;

    private ArrayList <DataBaseUser> users;

    private ArrayList<DataBaseService> services;

    private ArrayList<WalkInClinic> clinics;

    private static DatabaseReference databaseBookings = FirebaseDatabase.getInstance().getReference("bookings");
    private static DatabaseReference databaseClinics = FirebaseDatabase.getInstance().getReference("clinics");

    private ArrayList<Booking> bookings;

    public Administrator(String username, String password){
        this.username = username;
        this.password = password;
        this.name = null;
        this.id = "";
        init();
    }

    public Administrator(String name, String username, String password){
        this.username = username;
        this.password = password;
        this.name = name;
        this.id = "";
        init();
    }

    /**
     * call from constructor to setup admin attributes
     */
    private void init(){
        services = new ArrayList<>();
        users = new ArrayList<>();
        clinics = new ArrayList<>();
        numOfActiveUsers=0;
    }

    public void setServices(ArrayList<DataBaseService> services){
        this.services = services;
    }

    /**
     * Delete a specific service
     * @param id of service to be deleted
     * @return bool if successful
     */
    public boolean deleteService(String id) throws IllegalArgumentException {
        try {
            DatabaseReference dr = databaseClinics.child(id);
            dr.removeValue();
            databaseClinics.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    clinics.clear();
                    for(DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        WalkInClinic clinic = postSnapshot.getValue(WalkInClinic.class);
                        clinics.add(clinic);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) { }
            });
            for(WalkInClinic clinic: clinics){
                try {
                    clinic.getServiceIds().remove(id);
                    DatabaseReference dR = FirebaseDatabase.getInstance().getReference("clinics").child(clinic.getId());
                    dr.setValue(clinic);
                }catch(Exception e){}
            }

            DatabaseReference dR = FirebaseDatabase.getInstance().getReference("services").child(id);
            dR.removeValue();
            return true;
        }catch (Exception db){
            throw new IllegalArgumentException("No ID found. Databse error.");
        }
    }

    /**
     * Delete a specific service
     * @param service to be deleted
     * @return bool if successful
     */
    public boolean deleteService(DataBaseService service) {
        if (service != null){
            String id = service.getId();
            if(id != null){
                numOfActiveUsers--;
                return deleteService(id);
            }
        }
        return false;
    }

    /**
     * Add a specified service to the database
     * NOTE: Services cannot be duplicated, no services
     * with the same name
     * @param name of service
     * @param role of person (DOCTOR, NURSE, STAFF)
     * @return bool if successful
     * @throws IllegalArgumentException if name already exists
     */
    public boolean addService(String name, ServiceRole role) throws IllegalArgumentException{
        if(name != null && role != null){
            Service service = new Service(name, role);
            if (!duplicateService(name)) {
                if (service.save()) {
                    return true;
                }
            } else{
                throw new IllegalArgumentException("Service name already exists");
            }
        }
        return false;
    }

    /**
     * Checks if name of service already exists
     * @param name to check for in hashset
     * @return bool if duplicate
     */
    private boolean duplicateService(String name){
        for(DataBaseService service: services){
            if(service.getName().equals(name)){
                return true;
            }
        }
        return false;
    }

    /**
     * Deletes a user from the database based on their ID
     * @param id of user to delete
     * @return bool if successful
     */
    public boolean deleteUser(String id){
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("users").child(id);
        dR.removeValue();
        return true;
    }

    /**
     * Deletes a clinic from the database based on its ID
     * @param id of clinic to delete
     * @return bool if successful
     */
    public boolean deleteWalkIn(String id){
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("clinics").child(id);
        dR.removeValue();
        return true;
    }

    /**
     * Deletes all bookings from clinic
     * @param id walk in clinic id
     * @return bool if succesful
     */
    public boolean deleteClinicBookings(String id){
        updateBookings();

        for(Booking b: bookings){
            if(b.getWalkInId().equals(id)){
                DatabaseReference dR = FirebaseDatabase.getInstance().getReference("bookings").child(b.getId());
                dR.removeValue();
            }
        }

        return true;
    }

    /**
     * Deletes all bookings from clinic
     * @param id patient id
     * @return bool if succesful
     */
    public boolean deletePatientBookings(String id){
        updateBookings();
        for(Booking b: bookings){
            if(b.getPatientId().equals(id)){
                DatabaseReference dR = FirebaseDatabase.getInstance().getReference("bookings").child(b.getId());
                dR.removeValue();
            }
        }

        return true;
    }

    /**
     * Updates bookings reference
     */
    public void updateBookings(){
        bookings = new ArrayList<>();

        databaseBookings.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                bookings.clear();
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    Booking booking = postSnapshot.getValue(Booking.class);
                    bookings.add(booking);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /**
     * Deletes a specific user account
     * @param user, to be deleted
     * @return bool if successful
     */
    public boolean deleteUser(DataBaseUser user) {
        if (user != null){
            String id = user.getId();
            String clinicId = user.getClinicId();
            if(clinicId!=null && user.getRole().equals("Employee")){
                if (!clinicId.equals("")){
                    deleteWalkIn(clinicId);
                    deleteClinicBookings(clinicId);
                }
            }
            if(user.getRole().equals("Patient")){
                deletePatientBookings(user.getId());
            }
            if(id != null){
                numOfActiveUsers--;
                return deleteUser(id);
            }
        }
        return false;
    }

    /**
     * Updates a service with a new name and role
     * @param service the service to be updated
     * @param name, new name for service
     * @param role, new role for service
     * @return bool if succesful
     * @throws IllegalArgumentException if service name exists
     */
    public boolean updateService(DataBaseService service, String name, ServiceRole role) throws IllegalArgumentException{
        if(duplicateService(name) && !service.getName().equals(name)){
            throw new IllegalArgumentException("Service name already exists");
        }

        if (service != null){
            String id = service.getId();
            DatabaseReference dR = FirebaseDatabase.getInstance().getReference("services").child(id);
            DataBaseService s = new DataBaseService(id, name, role);
            dR.setValue(s);
            return true;
        }
        return false;
    }

    /**
     * Gets a list of all services
     * @return ArrayList of services
     */
    public ArrayList<DataBaseUser> getUsers(){
        return this.users;
    }

    public void setUsers(ArrayList<DataBaseUser> users){
        this.users = users;
        for (DataBaseUser usr: users){  // make to remove the admin user from the list
            if(usr.getId().equals("adminID")){
                users.remove(usr);
                break;
            }
        }
    }

    /**
     * Gets a list of all current services
     * @return ArrayList of services
     */
    public ArrayList<DataBaseService> getServices(){
        return this.services;
    }

    public int getActivateUsers(){
        return this.numOfActiveUsers;
    }

    @Override
    public boolean login(){
        return false;
    }
}