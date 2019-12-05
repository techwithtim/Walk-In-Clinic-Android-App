package com.example.final_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Services extends AppCompatActivity {

    private Employee activeUser;
    private Button backButton;
    private Button servicesButton;
    ListView listViewServices;
    private ArrayList<DataBaseService> allServices;
    private ArrayList<DataBaseUser> users;
    private static DatabaseReference databaseClinics = FirebaseDatabase.getInstance().getReference("clinics");
    private ArrayList<DataBaseService> services;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services);

        backButton = (Button) findViewById(R.id.backBtn);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUser();
            }
        });

        servicesButton = (Button) findViewById(R.id.servicesBtn);
        servicesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openServices();
            }
        });

        Intent i = getIntent();
        activeUser = (Employee) i.getSerializableExtra("user");
        services = new ArrayList<>();
        allServices = (ArrayList<DataBaseService>) i.getSerializableExtra("services");
        users = (ArrayList<DataBaseUser>) i.getSerializableExtra("users");

        listViewServices = (ListView)findViewById(R.id.serviceList);

        listViewServices.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                DataBaseService service = services.get(i);
                activeUser.getWalkInClinic().removeService(service.getId());
                activeUser.updateWalkinClinic();
                Toast.makeText(getApplicationContext(),"Service Removed", Toast.LENGTH_LONG);
                return true;
            }
        });

        databaseClinics.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                services.clear();
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    WalkInClinic clinic = postSnapshot.getValue(WalkInClinic.class);

                    if (clinic.getId().equals(activeUser.getClinicId())) {
                        for (String id : clinic.getServiceIds()) {
                            for (DataBaseService s : allServices) {
                                if (s.getId().equals(id)) {
                                    services.add(s);
                                }
                            }
                        }
                    }
                }
                ServiceList serviceAdapter = new ServiceList(Services.this, services);
                listViewServices.setAdapter(serviceAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void openUser() {
        Intent intent = new Intent(this, EmployeeUser.class);
        intent.putExtra("user", activeUser);
        intent.putExtra("users", users);
        intent.putExtra("services", allServices);
        startActivity(intent);
    }

    public void openServices() {
        Intent intent = new Intent(this, AddServices.class);
        intent.putExtra("user", activeUser);
        intent.putExtra("users", users);
        intent.putExtra("services", allServices);
        startActivity(intent);
    }
}
