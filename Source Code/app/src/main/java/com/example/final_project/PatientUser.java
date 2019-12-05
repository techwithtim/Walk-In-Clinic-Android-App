package com.example.final_project;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.util.ArrayList;


public class PatientUser extends AppCompatActivity {
    private Button button;
    private Patient activeUser;
    private TextView welcome;

    private Button book;
    private Button appointment;

    private ArrayList<DataBaseService> services;
    private ArrayList<DataBaseUser> users;
    private ArrayList<WalkInClinic> clinics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient);

        button = (Button) findViewById(R.id.signoutButton);
        book = (Button) findViewById(R.id.appointmentBtn);
        appointment = (Button) findViewById(R.id.bookButton);

        Intent i = getIntent();
        activeUser = (Patient) i.getSerializableExtra("user");
        services = (ArrayList<DataBaseService>) i.getSerializableExtra("services");
        users = (ArrayList<DataBaseUser>) i.getSerializableExtra("users");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(MainActivity.class);
            }
        });

        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(Appointment.class);
            }
        });

        appointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(CheckIn.class);
            }
        });

        welcome = (TextView) findViewById(R.id.welcomeText);

        activeUser = (Patient) i.getSerializableExtra("user");
        welcome.setText("Welcome " + activeUser.get_username() + " you are logged in as patient.");
    }


    public void openActivity(Class activity){
        Intent intent = new Intent(this, activity);
        intent.putExtra("user", activeUser);
        intent.putExtra("users", users);
        intent.putExtra("services", services);

        startActivity(intent);
    }
}