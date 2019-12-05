package com.example.final_project;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public class WalkInClinic implements Serializable {
    private String _id;
    private String _name;
    private String _address;
    private ArrayList<PaymentMethod> _paymentMethods;
    private String _phoneNumber;
    private ArrayList<InsuranceType> _insuranceTypes;
    private ArrayList<String> _serviceIds, _closingTimes, _openingTimes;
    private ArrayList<Integer>  _closedDays;
    private Integer _rating, _numOfRatings;
    private static int DEFAULT_RATING = 3;
    private ArrayList<Integer> _allRatings;

    public WalkInClinic(String name, String address, ArrayList<PaymentMethod> paymentMethods, String phoneNumber, ArrayList<InsuranceType> insuranceTypes, ArrayList<String> openingTimes, ArrayList<String> closingTimes){
        _name = name;
        _address = address;
        _paymentMethods = paymentMethods;
        _phoneNumber = phoneNumber;
        _insuranceTypes = insuranceTypes;
        _serviceIds = new ArrayList<>();
        _openingTimes = openingTimes;
        _closingTimes = closingTimes;
        _closedDays = new ArrayList<>(Arrays.asList(0,0,0,0,0,0,0));;
        _id = null;
        _rating = DEFAULT_RATING;
        _numOfRatings = 0;
        _allRatings = new ArrayList<>();
    }

    public WalkInClinic(String name, String address, ArrayList<PaymentMethod> paymentMethods, String phoneNumber, ArrayList<InsuranceType> insuranceTypes){
        _name = name;
        _address = address;
        _paymentMethods = paymentMethods;
        _phoneNumber = phoneNumber;
        _insuranceTypes = insuranceTypes;
        _serviceIds = new ArrayList<>();
        _openingTimes = new ArrayList<>(Arrays.asList("","","","","","",""));
        _closingTimes = new ArrayList<>(Arrays.asList("","","","","","",""));
        _closedDays = new ArrayList<>(Arrays.asList(1,1,1,1,1,1,1));
        _id = null;
        _rating = DEFAULT_RATING;
        _numOfRatings = 0;
        _allRatings = new ArrayList<>(Arrays.asList(0,0,0,0,0));
    }


    public WalkInClinic(String name, String address, ArrayList<PaymentMethod> paymentMethods, String phoneNumber, ArrayList<InsuranceType> insuranceTypes, ArrayList<String> openingTimes, ArrayList<String> closingTimes, ArrayList<String> serviceIds){
        _name = name;
        _address = address;
        _paymentMethods = paymentMethods;
        _phoneNumber = phoneNumber;
        _insuranceTypes = insuranceTypes;
        _serviceIds = serviceIds;
        _openingTimes = openingTimes;
        _closingTimes = closingTimes;
        _closedDays = new ArrayList<>(Arrays.asList(1,1,1,1,1,1,1));
        _id = null;
        _rating = DEFAULT_RATING;
        _numOfRatings = 0;
        _allRatings = new ArrayList<>(Arrays.asList(0,0,0,0,0));
    }

    public WalkInClinic(){
        _serviceIds = new ArrayList<>();
        _closingTimes = new ArrayList<>(Arrays.asList("","","","","","",""));
        _openingTimes = new ArrayList<>(Arrays.asList("","","","","","",""));
        _paymentMethods = new ArrayList<>();
        _insuranceTypes = new ArrayList<>();
        _closedDays = new ArrayList<>(Arrays.asList(1,1,1,1,1,1,1));
        _id = null;
        _rating = DEFAULT_RATING;
        _numOfRatings = 0;
        _allRatings = new ArrayList<>(Arrays.asList(0,0,0,0,0));
    }

    public void setId(String id){_id = id;}
    public void setName(String name){_name = name;}
    public void setAddress(String address){_address = address;}
    public void setPaymentMethods(ArrayList<PaymentMethod> methods){_paymentMethods = methods;}
    public void setPhoneNumber(String phoneNumber){_phoneNumber = phoneNumber;}
    public void setInsuranceTypes(ArrayList<InsuranceType> insuranceTypes){_insuranceTypes = insuranceTypes;}
    public void setServiceIds(ArrayList<String> val){_serviceIds = val;}
    public void setClosingTimes(ArrayList<String> closingTimes){_closingTimes = closingTimes;}
    public void setOpeningTimes(ArrayList<String> openingTimes){_openingTimes = openingTimes;}
    public void setClosedDays(ArrayList<Integer> days){_closedDays = days;}
    public void setRating(Integer rate){_rating = rate;}
    public void setNumOfRatings(Integer num){_numOfRatings = num;}
    public void setAllRatings(ArrayList<Integer> arr){_allRatings = arr;}
    public void addService(String id){
        _serviceIds.add(id);
    }

    public void removeService(String id) throws IllegalArgumentException{
        try {
            _serviceIds.remove(id);
        } catch(Exception ex){
            throw new IllegalArgumentException("No service with that Id");
        }
    }

    public void decrementRating(int rate){
        _numOfRatings--;
        _allRatings.set(rate-1, _allRatings.get(rate-1)-1);
    }

    public void addRating(int rate){
        _numOfRatings++;
        _allRatings.set(rate-1, _allRatings.get(rate-1)+1);
        int sum = 0;
        for(int j = 0; j<5;j++){
            sum = sum + _allRatings.get(j) * (j+1);
        }
        _rating = Math.round(sum / _numOfRatings);
    }


    public String getId(){return _id;}
    public String getName(){return _name;}
    public String getAddress(){return _address;}
    public ArrayList<PaymentMethod> getPaymentMethods(){return _paymentMethods;}
    public String getPhoneNumber(){return _phoneNumber;}
    public ArrayList<InsuranceType> getInsuranceTypes(){return _insuranceTypes;}
    public ArrayList<String> getServiceIds(){return _serviceIds;}
    public ArrayList<String> getClosingTimes(){return _closingTimes;}
    public ArrayList<String> getOpeningTimes(){return _openingTimes;}  // times represented like [Sunday, Monday, Tuesday, Wednesday, Thursday, Friday,  Saturday]
    public ArrayList<Integer> getClosedDays(){return _closedDays;}
    public Integer getRating(){return _rating;}
    public Integer getNumOfRatings(){return _numOfRatings;}
    public ArrayList<Integer> getAllRatings(){return _allRatings;}
}
