package com.example.final_project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;

import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import androidx.annotation.NonNull;


public class SignUp extends AppCompatActivity implements View.OnClickListener {

    private HashSet<String> usernamesUsed;
    private static DatabaseReference databaseUsers = FirebaseDatabase.getInstance().getReference("users");
    private static DatabaseReference databaseServices = FirebaseDatabase.getInstance().getReference("services");
    private ArrayList<DataBaseUser> users;
    private ArrayList<DataBaseService> services;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Button button1 = findViewById(R.id.createButton);
        Button button2 = findViewById(R.id.backButton);

        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        usernamesUsed = new HashSet<>();
        users = new ArrayList<>();
        services = new ArrayList<>();
        updateUsers();
        updateServices();
    }

    public void updateUsers(){
        databaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                users.clear();
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    DataBaseUser usr = postSnapshot.getValue(DataBaseUser.class);
                    users.add(usr);
                    usernamesUsed.add(usr.getUsername());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void updateServices(){
        databaseServices.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                services.clear();  // might need to remove
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    DataBaseService service = postSnapshot.getValue(DataBaseService.class);
                    services.add(service);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(View v) {

        RadioButton rb1;
        RadioButton rb2;

        rb1 = findViewById(R.id.EmployeeRB);
        rb2 =  findViewById(R.id.PatientRB);

        switch (v.getId()) {
            case R.id.createButton:
                if(validateForm()) {
                    if (rb1.isChecked())
                        openEmployee();
                    else if (rb2.isChecked())
                        openPatient();
                    else {
                        Toast.makeText(getApplicationContext(), "Please Choose a Role", Toast.LENGTH_LONG).show();
                    }
                }
                break;
            case R.id.backButton:
                openMain();
                break;
        }
    }

    private boolean validateForm(){
        EditText name = (EditText)findViewById(R.id.addressField2);
        EditText username = (EditText)findViewById(R.id.usernameField2);
        EditText password = (EditText)findViewById(R.id.passwordField2);
        String userName = username.getText().toString();
        String Password = password.getText().toString();
        String Name = name.getText().toString();

        if(userName.equals("") || Password.equals("") || Name.equals("")){
            Toast.makeText(getApplicationContext(), "Invalid Form", Toast.LENGTH_LONG).show();
            return false;
        }

        // validate password
        if(Password.length() < 5){
            Toast.makeText(getApplicationContext(), "Password Must Have Minimum 5 Characters", Toast.LENGTH_LONG).show();
            return false;
        }

        if (Password.contains(" ")){
            Toast.makeText(getApplicationContext(), "Password Cannot Contain Spaces", Toast.LENGTH_LONG).show();
            return false;
        }

        // make sure username is alphanumeric
        for(Character c: userName.toCharArray()){
            if(!Character.isLetterOrDigit(c)){
                Toast.makeText(getApplicationContext(), "Username Must Be Alphanumeric", Toast.LENGTH_LONG).show();
                return false;
            }
        }

        return true;
    }

    public void openPatient() {
        EditText name = (EditText)findViewById(R.id.addressField2);
        EditText username = (EditText)findViewById(R.id.usernameField2);
        EditText password = (EditText)findViewById(R.id.passwordField2);
        String userName = username.getText().toString();
        String Password = password.getText().toString();
        String Name = name.getText().toString();
        updateUsers();

        Patient newPatient = new Patient(Name, userName, Password);
        if (!usernamesUsed.contains(userName)){  // tries to save user in database
            if(newPatient.save()){
                name.setText("");
                password.setText("");
                username.setText("");
                Toast.makeText(getApplicationContext(), "Account Created", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(this, PatientUser.class);
                intent.putExtra("user", newPatient);
                startActivity(intent);
            } else{
                Toast.makeText(getApplicationContext(), "Error Creating An Account", Toast.LENGTH_LONG).show();
            }

        }else{  // if user account already exists
            Toast.makeText(getApplicationContext(), "Username Already Exists", Toast.LENGTH_LONG).show();
        }

    }

    public void openEmployee() {
        EditText name = (EditText)findViewById(R.id.addressField2);
        EditText username = (EditText)findViewById(R.id.usernameField2);
        EditText password = (EditText)findViewById(R.id.passwordField2);
        String userName = username.getText().toString();
        String Password = password.getText().toString();
        String Name = name.getText().toString();

        WalkInClinic clinic = new WalkInClinic();
        clinic.setName("");
        clinic.setAddress("");
        clinic.setPhoneNumber("");

        Employee newEmployee = new Employee(Name, userName, Password);

        System.out.println(usernamesUsed.contains(userName));
        if(!usernamesUsed.contains(userName)){
            if (newEmployee.save()) {  // tries to save user in database
                newEmployee.createWalkInClinic(clinic);
                name.setText("");
                password.setText("");
                username.setText("");
                Toast.makeText(getApplicationContext(), "Account Created", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(this, EmployeeUser.class);
                intent.putExtra("user", newEmployee);
                intent.putExtra("users", users);
                intent.putExtra("services", services);
                startActivity(intent);
            } else{
                Toast.makeText(getApplicationContext(), "Error Creating An Account", Toast.LENGTH_LONG).show();
            }
        }else{ // If user account already exists
            Toast.makeText(getApplicationContext(), "Account Already Exists", Toast.LENGTH_LONG).show();
        }

    }

    public void openMain() {
        EditText name = (EditText)findViewById(R.id.addressField2);
        EditText username = (EditText)findViewById(R.id.usernameField2);
        EditText password = (EditText)findViewById(R.id.passwordField2);
        name.setText("");
        password.setText("");
        username.setText("");

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}

