package com.example.final_project;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class Profile extends AppCompatActivity {

    private Button backButton, saveButton, pickButton;
    private EditText nameField, phoneField, addressField;
    private CheckBox cash, debit, credit, insurance1, insurance2;
    private Employee activeUser;
    private WalkInClinic clinic;
    private ArrayList<DataBaseService> services;
    private ArrayList<DataBaseUser> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        nameField = (EditText) findViewById(R.id.nameField);
        phoneField = (EditText) findViewById(R.id.phoneNumField);
        addressField = (EditText) findViewById(R.id.addressField2);

        cash = (CheckBox) findViewById(R.id.cashCB);
        debit = (CheckBox) findViewById(R.id.debitCB);
        credit = (CheckBox) findViewById(R.id.creditCB);
        insurance1 = (CheckBox) findViewById(R.id.insuranceCB);
        insurance2 = (CheckBox) findViewById(R.id.insurance2CB);

        saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateClinic();
            }
        });

        backButton = (Button) findViewById(R.id.backButton1);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUser();
            }
        });

        pickButton = (Button) findViewById(R.id.pickLocation);
        pickButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMaps();
            }
        });

        Intent i = getIntent();
        activeUser = (Employee) i.getSerializableExtra("user");
        services = (ArrayList<DataBaseService>) i.getSerializableExtra("services");
        users = (ArrayList<DataBaseUser>) i.getSerializableExtra("users");

        if(activeUser.getWalkInClinic()!=null){
            clinic = activeUser.getWalkInClinic();
            addressField.setText(clinic.getAddress());
            phoneField.setText(clinic.getPhoneNumber());
            nameField.setText(clinic.getName());

            ArrayList<PaymentMethod> paymentMethods = clinic.getPaymentMethods();
            if(paymentMethods.contains(PaymentMethod.CASH)){
                cash.setChecked(true);
            }
            if(paymentMethods.contains(PaymentMethod.DEBIT)){
                debit.setChecked(true);
            }
            if(paymentMethods.contains(PaymentMethod.CREDIT)){
                credit.setChecked(true);
            }

            ArrayList<InsuranceType> insurances = clinic.getInsuranceTypes();
            if(insurances.contains(InsuranceType.TYPE1)){
                insurance1.setChecked(true);
            }
            if(insurances.contains(InsuranceType.TYPE2)){
                insurance2.setChecked(true);
            }
        }else{
            clinic = new WalkInClinic();
        }
    }

    public ArrayList<DataBaseUser> getUsers(){
        return users;
    }

    public void onClick(View v){}

    public void updateClinic(){
        ArrayList<PaymentMethod> paymentMethods = new ArrayList<>();
        ArrayList<InsuranceType> insuranceTypes = new ArrayList<>();

        if(!addressField.getText().toString().equals("") && !nameField.getText().toString().equals("") && !phoneField.getText().toString().equals("")){
            if(cash.isChecked()){
                paymentMethods.add(PaymentMethod.CASH);
            }
            if(debit.isChecked()){
                paymentMethods.add(PaymentMethod.DEBIT);
            }
            if(credit.isChecked()){
                paymentMethods.add(PaymentMethod.CREDIT);
            }
            if(paymentMethods.size() == 0){
                Toast.makeText(getApplicationContext(), "Choose at Least One Payment Method", Toast.LENGTH_LONG).show();
                return;
            }

            if(insurance1.isChecked()){
                insuranceTypes.add(InsuranceType.TYPE1);
            }
            if(insurance2.isChecked()){
                insuranceTypes.add(InsuranceType.TYPE2);
            }
            if(insuranceTypes.size()==0){
                Toast.makeText(getApplicationContext(), "Choose at Least One Insurance Type", Toast.LENGTH_LONG).show();
                return;
            }

            if(!validPhone(phoneField.getText().toString())){
                Toast.makeText(getApplicationContext(),"Invalid Phone Number", Toast.LENGTH_LONG).show();
                return;
            }

            if (!validateAddress(addressField.getText().toString())){
                Toast.makeText(getApplicationContext(),"Invalid Address", Toast.LENGTH_LONG).show();
                return;
            }

            clinic.setAddress(addressField.getText().toString());
            clinic.setName(nameField.getText().toString());
            clinic.setPhoneNumber(phoneField.getText().toString());
            clinic.setPaymentMethods(paymentMethods);
            clinic.setInsuranceTypes(insuranceTypes);
            try {
                activeUser.updateWalkinClinic();  // if clinic is already in database
            }catch(IllegalArgumentException ex){ // if clinic isn't yet in database
                activeUser.createWalkInClinic(clinic);
            }
            Toast.makeText(getApplicationContext(),"Profile Updated", Toast.LENGTH_LONG).show();
            openUser();

        }else{
            Toast.makeText(getApplicationContext(), "Please Complete Each Field", Toast.LENGTH_LONG).show();
        }
    }

    public void openUser() {
        Intent intent = new Intent(this, EmployeeUser.class);
        intent.putExtra("user", activeUser);
        intent.putExtra("users", users);
        intent.putExtra("services", services);
        startActivity(intent);
    }

    public static boolean validateAddress( String address )
    {
        String[] split = address.split(" ");
        try{
            Integer.parseInt(split[0]);
        }catch(Exception e){
            return false;
        }

        return address.length() > 4 && split.length>1;
    }

    public void openMaps() {
        Uri gmmIntentUri = Uri.parse("http://maps.google.co.in/maps?q="+addressField.getText());
        // Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        // Make the Intent explicit by setting the Google Maps package
        mapIntent.setPackage("com.google.android.apps.maps");
        // Attempt to start an activity that can handle the Intent
        startActivity(mapIntent);
    }

    /**
     * validate a phone number
     * @param phone string
     * @return boolean
     */
    public boolean validPhone(String phone){
        if(phone.length() < 10){
            return false;
        }

        String digits = "";
        ArrayList<Character> valid = new ArrayList<>(Arrays.asList('1','2','3','4','5','6','7','8','9','0'));

        Character last = '_';
        int count = 0;
        for(Character c:phone.toCharArray()){
            if(!valid.contains(c)){
                boolean one = c.equals('(') && (count==0 || count ==2);
                boolean two = c.equals(')') && (count==4 || count == 6);
                boolean three = c.equals('+') && count==0;
                boolean four = c.equals('-') && digits.length()%3 == 0;

                if(!(one || two || three || four)){
                    return false;
                }

            }else{
                if(Character.isDigit(c)){
                    if(!last.equals('+')){
                        digits = digits + c.toString();
                    }
                }
            }
            last = c;
            count++;
        }
        if(digits.length() == 10){
            return true;
        }

        return false;
    }

    /**
     * Returns a new string with a formatted phone number
     * @param phone string
     * @return new formatted string
     */
    public String makeValidPhone(String phone){
        return phone;
    }

    /**
     * Example of changing walk in clinic times
     */
    public void test(){
        ArrayList<String> closing = new ArrayList<String>();
        ArrayList<String> opening = new ArrayList<String>();

        closing.add("12:00");
        closing.add("14:00");
        closing.add("14:00");
        closing.add("14:00");
        closing.add("16:00");
        closing.add("16:00");
        closing.add("16:00");

        opening.add("7:00");
        opening.add("7:00");
        opening.add("7:00");
        opening.add("7:00");
        opening.add("7:00");
        opening.add("7:00");
        opening.add("7:00");

        activeUser.setClosingTimes(closing);
        activeUser.setOpeningTimes(opening);
        activeUser.updateWalkinClinic();
    }
}
