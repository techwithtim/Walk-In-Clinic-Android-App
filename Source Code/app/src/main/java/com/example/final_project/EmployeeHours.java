package com.example.final_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;

import android.widget.TimePicker;
import android.widget.ToggleButton;

public class EmployeeHours extends AppCompatActivity {

    private Employee activeUser;
    private ArrayList<DataBaseService> services;
    private ArrayList<DataBaseUser> users;
    private Button back, save;
    private TimePicker sundayOpening, sundayClosing, mondayOpening, mondayClosing, tuesdayOpening, tuesdayClosing, wednesdayOpening, wednesdayClosing, thursdayOpening, thursdayClosing,
            fridayOpening, fridayClosing, saturdayOpening, saturdayClosing;

    private ToggleButton mon, tues, wed, thurs, fri, sat, sun;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_hours);

        Intent i = getIntent();
        activeUser = (Employee) i.getSerializableExtra("user");
        services = (ArrayList<DataBaseService>) i.getSerializableExtra("services");
        users = (ArrayList<DataBaseUser>) i.getSerializableExtra("users");

        sundayOpening = (TimePicker)findViewById(R.id.sundayOpenTime);
        sundayClosing = (TimePicker)findViewById(R.id.sundayCloseTime);

        mondayOpening = (TimePicker) findViewById(R.id.mondayOpenTime);
        mondayClosing = (TimePicker)findViewById(R.id.mondayCloseTime);

        tuesdayOpening = (TimePicker)findViewById(R.id.tuesdayOpenTime);
        tuesdayClosing = (TimePicker)findViewById(R.id.tuesdayCloseTime);

        wednesdayOpening = (TimePicker)findViewById(R.id.wednesdayOpenTime);
        wednesdayClosing = (TimePicker)findViewById(R.id.wednesdayCloseTime);

        thursdayOpening = (TimePicker) findViewById(R.id.thursdayOpenTime);
        thursdayClosing= (TimePicker) findViewById(R.id.thursdayCloseTime);

        fridayOpening = (TimePicker) findViewById(R.id.fridayOpenTime);
        fridayClosing = (TimePicker) findViewById(R.id.fridayCloseTime);

        saturdayClosing = (TimePicker) findViewById(R.id.saturdayOpenTime);
        saturdayOpening = (TimePicker) findViewById(R.id.saturdayCloseTime);

        final ArrayList<TimePicker> openHourField = new ArrayList<>();
        openHourField.add(sundayOpening);
        openHourField.add(mondayOpening);
        openHourField.add(tuesdayOpening);
        openHourField.add(wednesdayOpening);
        openHourField.add(thursdayOpening);
        openHourField.add(fridayOpening);
        openHourField.add(saturdayClosing);

        final ArrayList<TimePicker> closeHourField = new ArrayList<>();
        closeHourField.add(sundayClosing);
        closeHourField.add(mondayClosing);
        closeHourField.add(tuesdayClosing);
        closeHourField.add(wednesdayClosing);
        closeHourField.add(thursdayClosing);
        closeHourField.add(fridayClosing);
        closeHourField.add(saturdayOpening);

        mon = (ToggleButton) findViewById(R.id.mondayToggle);
        tues = (ToggleButton) findViewById(R.id.tuesdayToggle);
        wed = (ToggleButton) findViewById(R.id.wednesdayToggle);
        thurs = (ToggleButton) findViewById(R.id.thursdayToggle);
        fri = (ToggleButton) findViewById(R.id.fridayToggle);
        sat = (ToggleButton) findViewById(R.id.saturdayToggle);
        sun = (ToggleButton) findViewById(R.id.sundayToggle);

        final ArrayList<ToggleButton> toggleButtons = new ArrayList<>();
        toggleButtons.add(sun);
        toggleButtons.add(mon);
        toggleButtons.add(tues);
        toggleButtons.add(wed);
        toggleButtons.add(thurs);
        toggleButtons.add(fri);
        toggleButtons.add(sat);

        ArrayList<String> oldOpenTimes = activeUser.getOpenHours();
        ArrayList<String> oldCloseTimes = activeUser.getCloseHours();
        ArrayList<Integer> oldToggles = activeUser.getNotWorkingDays();
        if(oldOpenTimes.size()>0){
            for(int j =0; j < closeHourField.size();j++){
                String[] split = oldOpenTimes.get(j).split(":");
                int hour = Integer.parseInt(split[0]);
                int minute = Integer.parseInt(split[1]);
                openHourField.get(j).setHour(hour);
                openHourField.get(j).setMinute(minute);
            }
        }
        if (oldCloseTimes.size() > 0){
            for(int j =0; j < closeHourField.size();j++){
                String[] split = oldCloseTimes.get(j).split(":");
                int hour = Integer.parseInt(split[0]);
                int minute = Integer.parseInt(split[1]);
                closeHourField.get(j).setHour(hour);
                closeHourField.get(j).setMinute(minute);
            }
        }
        if(oldToggles.size() > 0){
            for(int j = 0; j < oldToggles.size(); j++){
                if(oldToggles.get(j).equals(1)){
                    toggleButtons.get(j).setChecked(false);
                }else{
                    toggleButtons.get(j).setChecked(true);
                }
            }
        }

        back = (Button) findViewById(R.id.backBtn);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(EmployeeUser.class);
            }
        });

        save = (Button) findViewById(R.id.saveBtn);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                activeUser.update();

                String[] close = new String[7];
                String[] open = new String[7];
                ArrayList<Integer> closedDays = new ArrayList<>();

                for(int i = 0; i < closeHourField.size(); i++){
                    String openTimes = String.valueOf(openHourField.get(i).getHour() + ":" + String.valueOf(openHourField.get(i).getMinute()));
                    String closeTimes = String.valueOf(closeHourField.get(i).getHour() + ":" + String.valueOf(closeHourField.get(i).getMinute()));

                    open [i] = openTimes;
                    close[i] = closeTimes;
                    closedDays.add(toggleButtons.get(i).isChecked() ? 0 : 1);
                }
                System.out.println(closedDays.toString());

                if (valid_times(open, close, closedDays)){
                    activeUser.setNotWorkingDays(closedDays);
                    activeUser.setCloseHours(new ArrayList<String>(Arrays.asList(close)));
                    activeUser.setOpenHours(new ArrayList<String>(Arrays.asList(open)));
                    activeUser.update();
                    Toast.makeText(getApplicationContext(), "Updated Working Hours", Toast.LENGTH_SHORT).show();
                    openActivity(EmployeeUser.class);
                }else{
                    Toast.makeText(getApplicationContext(), "Starting Time After Ending Time", Toast.LENGTH_SHORT).show();
                }
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

    public void openActivity(Class activity){
        Intent intent = new Intent(this, activity);
        intent.putExtra("user", activeUser);
        intent.putExtra("services", services);
        intent.putExtra("users", users);

        startActivity(intent);
    }
}
