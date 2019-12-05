package com.example.final_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SignIn extends AppCompatActivity implements View.OnClickListener {
    EditText username, password;
    ArrayList<DataBaseUser> users;
    ArrayList<DataBaseService> services;
    private static DatabaseReference databaseUsers = FirebaseDatabase.getInstance().getReference("users");
    private static DatabaseReference databaseServices = FirebaseDatabase.getInstance().getReference("services");

    private Person activeUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        Button button1 = findViewById(R.id.signInButton);
        Button button3 = findViewById(R.id.BackButton);

        button1.setOnClickListener(this);
        button3.setOnClickListener(this);

        username = (EditText)findViewById(R.id.usernameField2);
        password = (EditText)findViewById(R.id.passwordField2);

        users = new ArrayList<>();
        services = new ArrayList<>();
        updateUsers();
        updateServices();
    }

    public void updateUsers(){
        databaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                users.clear();  // might need to remove
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    DataBaseUser usr = postSnapshot.getValue(DataBaseUser.class);
                    users.add(usr);
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
        switch (v.getId()) {
            case R.id.signInButton:
                if (validateForm()){
                    if(activeUser instanceof Employee){
                        openEmployee();
                    }else if (activeUser instanceof Patient){
                        openPatient();
                    }else{
                        openAdmin();
                    }
                }
                break;
            case R.id.BackButton:
                openMain();
                break;
        }
    }

    public void hideKeyboard(){
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(this.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    public boolean validateForm(){
        String userName = username.getText().toString();
        String Password = password.getText().toString();
        //updateUsers();

        if(userName.equals("") || Password.equals("")){
            Toast.makeText(getApplicationContext(), "Invalid Form", Toast.LENGTH_LONG).show();
            return false;
        }else{
            for(DataBaseUser usr : users){
                String otherUsername = usr.getUsername();
                String pwd = usr.getPassword();
                String name = usr.getName();
                String role = usr.getRole();
                String id = usr.getId();
                String clinicId = usr.getClinicId();
                if (otherUsername.equals(userName)){
                    if (pwd.equals(Password)){
                        // successful login
                        Toast.makeText(getApplicationContext(), "Logged In", Toast.LENGTH_LONG).show();
                        if (role.equals("Employee")){
                            activeUser = new Employee(name, otherUsername, pwd, clinicId);
                        }else if (role.equals("Patient")){
                            activeUser = new Patient(name, otherUsername, pwd);
                        }else{
                            activeUser = new Administrator(otherUsername, pwd);
                        }
                        activeUser.setId(id);
                        return true;
                    }else{
                        Toast.makeText(getApplicationContext(), "Incorrect password", Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
        Toast.makeText(getApplicationContext(), "Invalid Login", Toast.LENGTH_LONG).show();
        return false;
    }

    public void openPatient() {
        Intent intent = new Intent(this, PatientUser.class);
        intent.putExtra("user", activeUser);
        intent.putExtra("users", users);
        intent.putExtra("services", services);
        startActivity(intent);
    }

    public void openEmployee() {
        Intent intent = new Intent(this, EmployeeUser.class);
        intent.putExtra("user", activeUser);
        intent.putExtra("users", users);
        intent.putExtra("services", services);
        startActivity(intent);
    }

    public void openAdmin(){
        Intent intent = new Intent(this, Admin.class);
        intent.putExtra("user", activeUser);
        intent.putExtra("users", users);
        intent.putExtra("services", services);
        startActivity(intent);
    }

    public void openMain() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("user", activeUser);
        startActivity(intent);
    }

}