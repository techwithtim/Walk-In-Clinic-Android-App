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

public class AddServices extends AppCompatActivity {

    private Employee activeUser;
    private Button backButton;
    private Button servicesButton;
    private ArrayList<DataBaseService> services;
    private ArrayList<DataBaseUser> users;
    private ArrayList<DataBaseService> allServices;

    private static DatabaseReference databaseServices = FirebaseDatabase.getInstance().getReference("services");
    ListView listViewServices;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_services);

        Intent i = getIntent();
        activeUser = (Employee) i.getSerializableExtra("user");

        allServices = (ArrayList<DataBaseService>) i.getSerializableExtra("services");
        services = new ArrayList<>();
        users = (ArrayList<DataBaseUser>) i.getSerializableExtra("users");
        listViewServices = (ListView)findViewById(R.id.serviceList);

        listViewServices.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                DataBaseService service = services.get(i);
                if(!activeUser.getWalkInClinic().getServiceIds().contains(service.getId())){
                    activeUser.getWalkInClinic().addService(service.getId());
                    activeUser.updateWalkinClinic();
                    Toast.makeText(getApplicationContext(),"Service Added", Toast.LENGTH_LONG).show();
                    openUserServices();
                }else{
                    Toast.makeText(getApplicationContext(), "Service is Already Added", Toast.LENGTH_LONG).show();
                }
                return true;
            }
        });

        backButton = (Button) findViewById(R.id.backBtn);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUserServices();
            }
        });

        databaseServices.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                services.clear();
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    DataBaseService service = postSnapshot.getValue(DataBaseService.class);
                    if(!activeUser.getWalkInClinic().getServiceIds().contains(service.getId())){
                        services.add(service);
                    }
                }
                ServiceList serviceAdapter = new ServiceList(AddServices.this, services);
                listViewServices.setAdapter(serviceAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public void openUserServices() {
        Intent intent = new Intent(this, Services.class);
        intent.putExtra("user", activeUser);
        intent.putExtra("users", users);
        intent.putExtra("services", allServices);
        startActivity(intent);
    }

}
