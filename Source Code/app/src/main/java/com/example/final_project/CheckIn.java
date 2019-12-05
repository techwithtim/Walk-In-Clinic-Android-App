package com.example.final_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CheckIn extends AppCompatActivity {

    private ArrayList<DataBaseService> services;
    private ArrayList<DataBaseUser> users;
    private ArrayList<WalkInClinic> clinics;
    private ArrayList<WalkInClinic> shownClinics;
    private ArrayList<Booking> bookings;
    private Patient activeUser;

    private EditText name, address;
    private Button search, back;
    private CheckBox sundayCB, mondayCB, tuesdayCB, wednesdayCB, thursdayCB, fridayCB, saturdayCB;
    private ArrayList<Boolean> checkBtnValues;

    private static DatabaseReference databaseClinics = FirebaseDatabase.getInstance().getReference("clinics");
    ListView listViewClinics;
    ClinicList adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_in);

        Intent i = getIntent();
        activeUser = (Patient) i.getSerializableExtra("user");
        services = (ArrayList<DataBaseService>) i.getSerializableExtra("services");
        users = (ArrayList<DataBaseUser>) i.getSerializableExtra("users");
        bookings = (ArrayList<Booking>)i.getSerializableExtra("bookings");
        if(bookings == null){
            bookings = new ArrayList<>();
        }
        clinics = (ArrayList<WalkInClinic>)i.getSerializableExtra("clinics");
        if(clinics == null){
            clinics = new ArrayList<>();
        }
        shownClinics = new ArrayList<>();

        // refrence all checkbuttons
        sundayCB = findViewById(R.id.sundayCB);
        mondayCB = findViewById(R.id.mondayCB);
        tuesdayCB = findViewById(R.id.tuesdayCB);
        wednesdayCB = findViewById(R.id.wednesdayCB);
        thursdayCB = findViewById(R.id.thursdayCB);
        fridayCB = findViewById(R.id.fridayCB);
        saturdayCB = findViewById(R.id.saturdayCB);

        updateCheckButtons();

        // reference edit text
        name = findViewById(R.id.nameField2);
        address = findViewById(R.id.addressField);


        // button links
        search = findViewById(R.id.searchBtn);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search();
            }
        });

        back = findViewById(R.id.backBtn);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(PatientUser.class);
            }
        });

        listViewClinics = (ListView)findViewById(R.id.clinicList);

        listViewClinics.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                WalkInClinic clinic = shownClinics.get(position);
                Intent intent = new Intent(getApplicationContext(), SpecificClinic.class);
                intent.putExtra("user", activeUser);
                intent.putExtra("users", users);
                intent.putExtra("services", services);
                intent.putExtra("clinic", clinic);

                startActivity(intent);
            }
        });

        databaseClinics.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                clinics.clear();
                shownClinics.clear();
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    WalkInClinic clinic = postSnapshot.getValue(WalkInClinic.class);
                    if(!clinic.getName().equals("")){
                        if(clinic.getServiceIds().size()!=0 && clinic.getClosedDays().contains(0)){
                            shownClinics.add(clinic);
                        }

                        clinics.add(clinic);
                    }
                }
                adapter = new ClinicList(CheckIn.this, shownClinics);
                listViewClinics.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
        search();

    }

    private void updateCheckButtons(){
        checkBtnValues = new ArrayList<>();
        checkBtnValues.add(sundayCB.isChecked());
        checkBtnValues.add(mondayCB.isChecked());
        checkBtnValues.add(tuesdayCB.isChecked());
        checkBtnValues.add(wednesdayCB.isChecked());
        checkBtnValues.add(thursdayCB.isChecked());
        checkBtnValues.add(fridayCB.isChecked());
        checkBtnValues.add(saturdayCB.isChecked());
    }

    private void search(){
        updateCheckButtons();
        shownClinics.clear();
        for(WalkInClinic clinic: clinics){
            boolean show = true;
            if (!clinic.getName().toLowerCase().contains(name.getText().toString().toLowerCase().trim()) ){
                show = false;
            }

            if(!clinic.getAddress().toLowerCase().contains(address.getText().toString().toLowerCase().trim())){
                show = false;
            }

            // check open days
            ArrayList<Integer> closedDays = clinic.getClosedDays();
            for(int i = 0; i < 7; i++){
                if(checkBtnValues.get(i) && closedDays.get(i) == 1){
                    show = false;
                }
            }

            if(clinic.getServiceIds().size()==0){
                show=false; // no services no way to book appointment
            }

            if (show){
                shownClinics.add(clinic);
            }
        }
        try{
            adapter.notifyDataSetChanged();
        }catch (Exception e){

        }

    }

    public void openActivity(Class activity){
        Intent intent = new Intent(this, activity);
        intent.putExtra("user", activeUser);
        intent.putExtra("users", users);
        intent.putExtra("services", services);
        intent.putExtra("bookings", bookings);
        intent.putExtra("clinics", clinics);
        startActivity(intent);
    }
}
