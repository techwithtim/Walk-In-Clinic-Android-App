package com.example.final_project;

import java.io.Serializable;
import java.util.Date;


public class Booking implements Serializable {
    private Integer _rating;
    private String _walkInId, _patientId, _serviceId;
    private Date _time;
    private WalkInClinic clinic;
    private Patient patient;
    private DataBaseService service;
    private String _id;

    public Booking(String walkInId, String patientId, String serviceId){
        _walkInId = walkInId;
        _patientId = patientId;
        _serviceId = serviceId;
        _rating = 0;  // means no rating yet
        _id = null;
    }

    public Booking(){
        _walkInId =  null;
        _patientId = null;
        _serviceId = null;
        _rating = 0;  // means no rating yet
        _id = null;
    }


    public String getWalkInId(){return _walkInId;}
    public String getPatientId(){return _patientId;}
    public String getServiceId(){return _serviceId;}
    public Integer getRating(){return _rating;}
    public Date getTime(){return _time;}
    public String getId(){return _id;}

    public void setWalkInId(String id){_walkInId = id;}
    public void setRating(Integer rate){_rating = rate;}
    public void setServiceId(String id){_serviceId = id;}
    public void setTime(Date time){_time = time;}
    public void setPatientId(String id){_patientId = id;}
    public void setId(String id){_id = id;}

    public void setClinic(WalkInClinic w){clinic = w;}
    public void setPatient(Patient p){patient = p;}
    public void setService(DataBaseService s){service = s;}

    public WalkInClinic getClinic(){return clinic;}
    public Patient getPatient(){return patient;}
    public DataBaseService getService(){return service;}

    // compares times
    public boolean equals(Booking booking){
        return this._time.getTime() == booking.getTime().getTime();
    }
}
