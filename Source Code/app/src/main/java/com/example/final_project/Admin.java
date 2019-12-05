package com.example.final_project;

import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class Admin extends AppCompatActivity {

    private Button button, userButton, serviceButton;
    private Administrator activeUser;
    private TextView welcome;
    private ArrayList<DataBaseService> services;
    private ArrayList<DataBaseUser> users;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        button = (Button) findViewById(R.id.signoutButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMain();
            }
        });

        userButton = (Button) findViewById(R.id.usersBtn);
        userButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(ViewUsers.class);
            }
        });

        serviceButton = (Button) findViewById(R.id.serviceBtn);
        serviceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(ViewServices.class);
            }
        });

        Intent i = getIntent();
        activeUser = (Administrator) i.getSerializableExtra("user");
        services = (ArrayList<DataBaseService>) i.getSerializableExtra("services");
        users = (ArrayList<DataBaseUser>) i.getSerializableExtra("users");

    }


    // HOW TO CHANGE SCREENS AND PASS THROUGH NEEDED VALUES
    // You'll need to pass the activeUser to each screen
    // you go to. This is because activeUser has the methods
    // to update services/users and has access to the list
    // of all users and services.

    /**
     * Use this to open another activity. I've set the intent to pass
     * through the activeUser object so you can reference it from the
     * other activities.
     * @param activity name of the class
     *      Example usage
     *            openActivity(Admin.class)
     *
     * Look at this classes onCreate to see how to get the information
     * from another activity.
     */
    public void openActivity(Class activity){
        Intent intent = new Intent(this, activity);
        intent.putExtra("user", activeUser);
        intent.putExtra("services", services);
        intent.putExtra("users", users);
        startActivity(intent);
    }

    public void openMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}