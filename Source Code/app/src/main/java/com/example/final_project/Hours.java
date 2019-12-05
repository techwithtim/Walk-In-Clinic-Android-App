package com.example.final_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;

public class Hours extends AppCompatActivity {

    private Button backButton, saveButton, infoButton;
    private Employee activeUser;
    private ArrayList<DataBaseService> services;
    private ArrayList<DataBaseUser> users;
    private WalkInClinic activeClinic;
    private static DatabaseReference databaseWalkIn = FirebaseDatabase.getInstance().getReference("clinics");
    private TimePicker sundayOpening, sundayClosing, mondayOpening, mondayClosing, tuesdayOpening, tuesdayClosing, wednesdayOpening, wednesdayClosing, thursdayOpening, thursdayClosing, fridayOpening, fridayClosing, saturdayOpening, saturdayClosing;
    private ToggleButton sundayOpen, mondayOpen, tuesdayOpen, wednesdayOpen, thursdayOpen, fridayOpen, saturdayOpen;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hours);

        Intent i = getIntent();
        activeUser = (Employee) i.getSerializableExtra("user");
        services = (ArrayList<DataBaseService>) i.getSerializableExtra("services");
        users = (ArrayList<DataBaseUser>) i.getSerializableExtra("users");
        activeClinic = activeUser.getWalkInClinic();

        databaseWalkIn.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    WalkInClinic clinic = postSnapshot.getValue(WalkInClinic.class);
                    if(clinic.getId().equals(activeUser.getClinicId())){
                        activeClinic = clinic;
                        activeUser.setWalkInClinic(clinic);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        // Need to reference each editText field and check them for validity
        sundayOpening = (TimePicker)findViewById(R.id.sundayOpening);
        sundayClosing = (TimePicker)findViewById(R.id.sundayClosing);

        mondayOpening = (TimePicker)findViewById(R.id.mondayOpening);
        mondayClosing = (TimePicker)findViewById(R.id.mondayClosing);

        tuesdayOpening = (TimePicker)findViewById(R.id.tuesdayOpening);
        tuesdayClosing = (TimePicker)findViewById(R.id.tuesdayClosing);

        wednesdayOpening = (TimePicker)findViewById(R.id.wednesdayOpening);
        wednesdayClosing = (TimePicker)findViewById(R.id.wednesdayClosing);

        thursdayOpening = (TimePicker) findViewById(R.id.thursdayOpening);
        thursdayClosing= (TimePicker) findViewById(R.id.thursdayClosing);

        fridayOpening = (TimePicker) findViewById(R.id.fridayOpening);
        fridayClosing = (TimePicker) findViewById(R.id.fridayClosing);

        saturdayClosing = (TimePicker) findViewById(R.id.saturdayClosing);
        saturdayOpening = (TimePicker) findViewById(R.id.saturdayOpening);

        final ArrayList<TimePicker> openField = new ArrayList<>();
        openField.add(sundayOpening);
        openField.add(mondayOpening);
        openField.add(tuesdayOpening);
        openField.add(wednesdayOpening);
        openField.add(thursdayOpening);
        openField.add(fridayOpening);
        openField.add(saturdayOpening);

        final ArrayList<TimePicker> closeField = new ArrayList<>();
        closeField.add(sundayClosing);
        closeField.add(mondayClosing);
        closeField.add(tuesdayClosing);
        closeField.add(wednesdayClosing);
        closeField.add(thursdayClosing);
        closeField.add(fridayClosing);
        closeField.add(saturdayClosing);

        // Need to reference each open/closed button
        sundayOpen = (ToggleButton) findViewById(R.id.closedSunday);
        mondayOpen = (ToggleButton) findViewById(R.id.closedMonday);
        tuesdayOpen = (ToggleButton) findViewById(R.id.closedTuesday);
        wednesdayOpen = (ToggleButton) findViewById(R.id.closedWednesday);
        thursdayOpen = (ToggleButton) findViewById(R.id.closedThursday);
        fridayOpen = (ToggleButton) findViewById(R.id.closedFriday);
        saturdayOpen = (ToggleButton) findViewById(R.id.closedSaturday);

        final ArrayList<ToggleButton> toggleButtons = new ArrayList<>();
        toggleButtons.add(sundayOpen);
        toggleButtons.add(mondayOpen);
        toggleButtons.add(tuesdayOpen);
        toggleButtons.add(wednesdayOpen);
        toggleButtons.add(thursdayOpen);
        toggleButtons.add(fridayOpen);
        toggleButtons.add(saturdayOpen);

        // add the fields to arrayList for easier processing
        ArrayList<String> previousOpeningTimes = activeUser.getOpeningTimes();
        ArrayList<String> previousClosingTimes = activeUser.getClosingTimes();
        ArrayList<Integer> previousClosedDays = activeUser.getClosedDays();
        if(previousOpeningTimes.size()>0){
            for(int j =0; j < closeField.size();j++){
                Integer closed = previousClosedDays.get(j);
                if(closed==0){
                    toggleButtons.get(j).setChecked(true);
                } // set toggle buttons

                String[] split = previousOpeningTimes.get(j).split(":");
                if(split.length == 1){continue;}

                int hour = Integer.parseInt(split[0]);
                int minute = Integer.parseInt(split[1]);

                openField.get(j).setHour(hour);
                openField.get(j).setMinute(minute);
            }
        }
        if (previousClosingTimes.size() > 0){
            for(int j =0; j < closeField.size();j++){
                String[] split = previousClosingTimes.get(j).split(":");
                if(split.length == 1){continue;}

                int hour = Integer.parseInt(split[0]);
                int minute = Integer.parseInt(split[1]);

                closeField.get(j).setHour(hour);
                closeField.get(j).setMinute(minute);
            }
        }


        infoButton = (Button)findViewById(R.id.infoButton);
        infoButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                showInfo();
            }
        });

        // if save button is pressed
        saveButton = (Button)findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                activeUser.updateWalkinClinic();

                String[] close = new String[7];
                String[] open = new String[7];
                ArrayList<Integer> closedDays = new ArrayList<>();

                for(int i = 0; i < closeField.size(); i++){
                    String openTimes = String.valueOf(openField.get(i).getHour() + ":" + (String.valueOf(openField.get(i).getMinute()).length()==1 ? "0" : "") +String.valueOf(openField.get(i).getMinute()));
                    String closeTimes = String.valueOf(closeField.get(i).getHour() + ":" + (String.valueOf(closeField.get(i).getMinute()).length()==1 ? "0" : "" )+ String.valueOf(closeField.get(i).getMinute()));

                    open [i] = openTimes;
                    close[i] = closeTimes;
                    closedDays.add(toggleButtons.get(i).isChecked() ? 0 : 1);
                }

                if (valid_times(open, close, closedDays)){
                    Toast.makeText(getApplicationContext(), "Updated Working Hours", Toast.LENGTH_SHORT).show();

                    // format open and close times properly
                    activeUser.setOpeningTimes(new ArrayList<String>(Arrays.asList(open)));
                    activeUser.setClosingTimes(new ArrayList<String>(Arrays.asList(close)));
                    activeUser.setClosedDays(closedDays);
                    activeUser.updateWalkinClinic();
                    openUser();
                }else{
                    Toast.makeText(getApplicationContext(), "Starting Time After Ending Time", Toast.LENGTH_SHORT).show();
                }
            }
        });


        backButton = (Button) findViewById(R.id.backButton1);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUser();
            }
        });
    }

    public boolean valid_times(String[] open, String[] close, ArrayList<Integer> closed){
        Integer openHour, closeHour, openMinute, closeMinute;
        for(int i =0; i < open.length; i++){
            String[] split1 = open[i].split(":");
            String[] split2 = close[i].split(":");
            openHour = Integer.parseInt(split1[0]);
            closeHour = Integer.parseInt(split2[0]);
            openMinute = Integer.parseInt(split1[1]);
            closeMinute = Integer.parseInt(split2[1]);

            if(closed.get(i).equals(0)) {
                if (openHour.compareTo(closeHour) == 1) {
                    return false;
                } else if (openHour.equals(closeHour)) {
                    if (openMinute >= closeMinute) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public void showInfo(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.info_hours_dialog, null);
        dialogBuilder.setView(dialogView);

        final AlertDialog b = dialogBuilder.create();
        Button closeButton = dialogView.findViewById(R.id.closeButton);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b.dismiss();
            }
        });

        b.show();

    }

    public void openUser() {
        Intent intent = new Intent(this, EmployeeUser.class);
        intent.putExtra("user", activeUser);
        intent.putExtra("users", users);
        intent.putExtra("services", services);
        startActivity(intent);
    }
}
