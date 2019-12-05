package com.example.final_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class EmployeeUser extends AppCompatActivity {

    private Button button;
    private TextView welcome;
    private Employee activeUser;
    private ArrayList<DataBaseService> services;
    private ArrayList<DataBaseUser> users;
    private Button button2, button3, button4, button5;
    private static DatabaseReference databaseWalkIn = FirebaseDatabase.getInstance().getReference("clinics");
    private boolean profileComplete = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee);

        welcome = (TextView) findViewById(R.id.welcomeText);

        Intent i = getIntent();
        activeUser = (Employee) i.getSerializableExtra("user");
        services = (ArrayList<DataBaseService>) i.getSerializableExtra("services");
        users = (ArrayList<DataBaseUser>) i.getSerializableExtra("users");

        button = (Button) findViewById(R.id.signoutButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(MainActivity.class);
            }
        });

        button2 = (Button) findViewById(R.id.profileButton);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(Profile.class);
            }
        });

        button3 = (Button) findViewById(R.id.hoursButton);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (profileComplete)
                    openActivity(Hours.class);
                else{
                    Toast.makeText(getApplicationContext(), "Please complete your profile first", Toast.LENGTH_LONG).show();
                }

            }
        });

        button4 = (Button) findViewById(R.id.servicesButton);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (profileComplete)
                    openActivity(Services.class);
                else{
                    Toast.makeText(getApplicationContext(), "Please complete your profile first", Toast.LENGTH_LONG).show();
                }
            }
        });

        button5 = (Button) findViewById(R.id.employeeHoursBtn);
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(profileComplete) {
                    openActivity(EmployeeHours.class);
                }else{
                    Toast.makeText(getApplicationContext(), "Please complete your profile first", Toast.LENGTH_LONG).show();
                }
            }
        });

        databaseWalkIn.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    WalkInClinic clinic = postSnapshot.getValue(WalkInClinic.class);
                    if (clinic.getId().equals(activeUser.getClinicId())){// DON'T REMOVE THIS
                        activeUser.setWalkInClinic(clinic);
                        if(!activeUser.getWalkInClinic().getName().equals("")){
                            profileComplete = true;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        welcome.setText("Welcome " + activeUser.get_username() + " you are logged in as employee.");


    }

    public void openActivity(Class activity){
        Intent intent = new Intent(this, activity);
        intent.putExtra("user", activeUser);
        intent.putExtra("services", services);
        intent.putExtra("users", users);

        startActivity(intent);
    }

}