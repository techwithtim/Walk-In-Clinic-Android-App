package com.example.final_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import org.joda.time.DateTime;
import android.widget.ImageView;
import android.widget.Spinner;
import org.joda.time.Minutes;
import android.widget.TextView;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.time.DayOfWeek;
import java.util.concurrent.TimeUnit;

public class SpecificClinic extends AppCompatActivity {

    private Patient activeUser;
    private ArrayList<DataBaseService> services;
    private ArrayList<DataBaseUser> users;
    private WalkInClinic clinic;
    private ArrayList<Booking> bookings;
    private ArrayList<WalkInClinic> clinics;

    private TextView name, address, sunday, monday, tuesday, wednesday, thursday, friday, saturday, waitTime;
    private ArrayList<TextView> times;
    private ImageView star1, star2, star3, star4, star5;

    private Button date, time, book, back;

    private Spinner serviceSpinner;

    private static DatabaseReference databaseBookings = FirebaseDatabase.getInstance().getReference("bookings");
    private static DatabaseReference databaseServices = FirebaseDatabase.getInstance().getReference("services");

    private DatePicker aptDate;
    private TimePicker aptTime;

    private Date currentDate = new Date(System.currentTimeMillis());
    private Date savedDate;
    private boolean pickedTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific_clinic);

        Intent i = getIntent();
        activeUser = (Patient) i.getSerializableExtra("user");
        services = (ArrayList<DataBaseService>) i.getSerializableExtra("services");
        if (services==null){
            services = new ArrayList<>();
        }
        users = (ArrayList<DataBaseUser>) i.getSerializableExtra("users");
        clinic = (WalkInClinic) i.getSerializableExtra("clinic");
        times = new ArrayList<>();
        bookings = (ArrayList<Booking>)i.getSerializableExtra("bookings");
        if(bookings == null){
            bookings = new ArrayList<>();
        }
        clinics = (ArrayList<WalkInClinic>)i.getSerializableExtra("clinics");
        if(clinics == null){
            clinics = new ArrayList<>();
        }

        waitTime = findViewById(R.id.waitTime);

        pickedTime = false;

        name = findViewById(R.id.clinicName);
        address = findViewById(R.id.address);

        sunday = findViewById(R.id.sunHours);
        monday = findViewById(R.id.monHours);
        tuesday = findViewById(R.id.tuesHours);
        wednesday = findViewById(R.id.wedHours);
        thursday = findViewById(R.id.thursHours);
        friday = findViewById(R.id.friHours);
        saturday = findViewById(R.id.satHours);

        times.add(sunday); times.add(monday); times.add(tuesday); times.add(wednesday); times.add(thursday); times.add(friday); times.add(saturday);

        time = findViewById(R.id.timeBtn);
        date = findViewById(R.id.dateBtn);
        book = findViewById(R.id.bookBtn);


        // set all properties
        name.setText(clinic.getName());
        address.setText(clinic.getAddress());

        ArrayList<Integer> closed = clinic.getClosedDays();
        ArrayList<String> openTimes = clinic.getOpeningTimes();
        ArrayList<String> closedTimes = clinic.getClosingTimes();
        for(int j = 0; j < 7; j++){
            if(closed.get(j) == 1 || openTimes.get(j).equals("") || closedTimes.get(j).equals("")){
                times.get(j).setText("Closed");
                times.get(j).setTextColor(Color.RED);
            }else{
                times.get(j).setText(openTimes.get(j) + " - " + closedTimes.get(j));
                times.get(j).setTextColor(Color.BLACK);
            }
        }

        // set rating
        ArrayList<ImageView> stars = new ArrayList<>();
        star1 = findViewById(R.id.star1);
        star2 = findViewById(R.id.star2);
        star3 = findViewById(R.id.star3);
        star4 = findViewById(R.id.star4);
        star5 = findViewById(R.id.star5);
        stars.add(star1); stars.add(star2); stars.add(star3); stars.add(star4); stars.add(star5);



        Integer rating = clinic.getRating();
        for(int x = 0; x < rating; x++){
            stars.get(x).setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),android.R.drawable.btn_star_big_on));
        }

        databaseServices.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                services.clear();
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    DataBaseService service = postSnapshot.getValue(DataBaseService.class);
                    services.add(service);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        databaseBookings.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                bookings.clear();
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    Booking booking = postSnapshot.getValue(Booking.class);
                    bookings.add(booking);

                    for (WalkInClinic clinic : clinics) {
                        if (booking.getWalkInId().equals(clinic.getId())) {
                            booking.setClinic(clinic);
                        }
                    }

                    for (DataBaseService service : services) {
                        if (booking.getServiceId().equals(service.getId())) {
                            booking.setService(service);
                        }
                    }
                }
                waitTime.setText("Current Waiting Time: " + getWaitingTime());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        // set spinner contents
        serviceSpinner = findViewById(R.id.service_spinner);
        ArrayList<String> spinnerList = new ArrayList<>();
        for(String serviceId: clinic.getServiceIds()){
            for(DataBaseService service: services){
                if(service.getId().equals(serviceId)){
                    spinnerList.add(service.getName());
                }
            }
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerList);
        serviceSpinner.setAdapter(arrayAdapter);

        date.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                showDate();
            }
        });

        Button location = findViewById(R.id.locationBtn);
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMaps();
            }
        });

        time.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(savedDate!=null){
                    showTime();
                }else{
                    Toast.makeText(getApplicationContext(), "Please Choose a Date", Toast.LENGTH_LONG).show();
                }

            }
        });

        back = (Button) findViewById(R.id.backButton2);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(CheckIn.class);
            }
        });

        Button book = findViewById(R.id.bookBtn);
        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (savedDate == null){
                    Toast.makeText(getApplicationContext(), "Please Choose a Date", Toast.LENGTH_LONG).show();
                    return;
                }

                if(!pickedTime){
                    Toast.makeText(getApplicationContext(), "Please Pick a Time", Toast.LENGTH_LONG).show();
                    return;
                }

                Booking b = new Booking();
                b.setWalkInId(clinic.getId());
                b.setPatientId(activeUser.getId());
                b.setTime(savedDate);
                b.getTime().setYear(currentDate.getYear());

                // make sure booking isn't already taken
                for(Booking booking: bookings){
                    if(booking.equals(b)){  // bookings are at same time
                        Toast.makeText(getApplicationContext(),"This Appointment Is Not Available. Please Try a Different Date and/or Time.", Toast.LENGTH_LONG).show();
                        return;
                    }
                }

                for(DataBaseService service: services){
                    try {
                        String spinnerName = serviceSpinner.getSelectedItem().toString();
                        if (service.getName().equals(spinnerName)) {
                            b.setServiceId(service.getId());
                            Toast.makeText(getApplicationContext(), "Appointment Booked", Toast.LENGTH_LONG).show();
                            break;
                        }
                    }catch(Exception e){
                        Toast.makeText(getApplicationContext(),"Please Pick a Service", Toast.LENGTH_LONG).show();
                        return;
                    }
                }

                if(b.getServiceId()!=null){
                    String id = databaseBookings.push().getKey();  // get unique database key
                    b.setId(id);
                    databaseBookings.child(id).setValue(b);
                    openActivity(PatientUser.class);
                }else{
                    Toast.makeText(getApplicationContext(),"Please Pick a Service", Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    public void openMaps() {
        Uri gmmIntentUri = Uri.parse("http://maps.google.co.in/maps?q="+address.getText());
        // Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        // Make the Intent explicit by setting the Google Maps package
        mapIntent.setPackage("com.google.android.apps.maps");
        // Attempt to start an activity that can handle the Intent
        startActivity(mapIntent);
    }

    public void showDate() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.activity_pick_date, null);
        dialogBuilder.setView(dialogView);
        final AlertDialog b = dialogBuilder.create();

        aptDate = dialogView.findViewById(R.id.datePicker);
        aptDate.setMinDate(System.currentTimeMillis());

        if(savedDate != null){
            aptDate.updateDate(savedDate.getYear(), savedDate.getMonth(), savedDate.getDate());
        }

        Button close = dialogView.findViewById(R.id.closeButton);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b.dismiss();
            }
        });

        Button saveDate = dialogView.findViewById(R.id.saveDate);
        saveDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int day = aptDate.getDayOfMonth();
                int month = aptDate.getMonth();

                Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                c.set(year, month, day);
                c.add(Calendar.DAY_OF_WEEK_IN_MONTH, day);

                Date date = new Date(year, month, day);

                ArrayList<String> daysOfWeek = new ArrayList<>(Arrays.asList("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"));
                int indexDay = daysOfWeek.indexOf(String.format("%tA", c));

                ArrayList<Integer> closed = clinic.getClosedDays();

                boolean validDate = closed.get(indexDay) != 1;

                if(validDate){
                    savedDate = date;
                    aptDate.updateDate(year, day, month);
                    Toast.makeText(getApplicationContext(), "Date Saved", Toast.LENGTH_LONG).show();
                    b.dismiss();
                }else{
                    Toast.makeText(getApplicationContext(), "Clinic is Closed That Day", Toast.LENGTH_LONG).show();
                }
            }
        });
        b.show();
    }


    public void showTime() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.activity_pick_time, null);
        dialogBuilder.setView(dialogView);
        final AlertDialog b = dialogBuilder.create();

        currentDate = new Date();

        aptTime = dialogView.findViewById(R.id.timePicker);
        if(pickedTime){
            aptTime.setCurrentHour(savedDate.getHours());
            aptTime.setCurrentMinute(savedDate.getMinutes());
        }

        Button close = dialogView.findViewById(R.id.closeButton);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b.dismiss();
            }
        });

        Button saveTime = dialogView.findViewById(R.id.saveTime);
        saveTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int minute = aptTime.getMinute();
                int hour = aptTime.getHour();
                int year = savedDate.getYear();
                int month = savedDate.getMonth();
                int day = savedDate.getDate();

                Calendar c = Calendar.getInstance();
                c.set(year, month, day);
                c.add(Calendar.DAY_OF_WEEK_IN_MONTH, day);

                ArrayList<String> daysOfWeek = new ArrayList<>(Arrays.asList("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"));
                int indexDay = daysOfWeek.indexOf(String.format("%tA", c));
                System.out.println(indexDay);
                String openTime = clinic.getOpeningTimes().get(indexDay);
                String closeTime = clinic.getClosingTimes().get(indexDay);

                boolean valid = true;
                int checkHour, checkMinute;

                String[] parse = openTime.split(":");
                checkHour = Integer.parseInt(parse[0]);
                checkMinute = Integer.parseInt(parse[1]);
                if (checkHour > hour || (checkHour == hour && checkMinute > minute)) {
                    valid = false;
                }

                parse = closeTime.split(":");
                checkHour = Integer.parseInt(parse[0]);
                checkMinute = Integer.parseInt(parse[1]);

                if (checkHour < hour || (checkHour == hour && checkMinute < minute)) {
                    valid = false;
                }


                if (valid) {
                    // round to nearest 15
                    int roundedMinutes = getNear15Minute(hour*60 + minute);
                    hour = roundedMinutes/60;
                    minute = roundedMinutes%60;

                    savedDate.setHours(hour);
                    savedDate.setMinutes(minute);
                    if((savedDate.getMonth()>=currentDate.getMonth() && savedDate.getYear()>=currentDate.getYear() && (savedDate.getDate() != currentDate.getDate()) || (savedDate.getDate() == currentDate.getDate() && (currentDate.getHours() < hour || (currentDate.getHours() == hour && currentDate.getMinutes() < minute))))){
                        pickedTime = true;
                        Toast.makeText(getApplicationContext(), "Time Saved", Toast.LENGTH_LONG).show();
                        b.dismiss();
                    }else{
                        Toast.makeText(getApplicationContext(), "Time Not Available", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "Clinic Is Closed At That Time", Toast.LENGTH_LONG).show();
                }
            }
        });

        b.show();
    }

    public static int getNear15Minute(int minutes){
        int mod = minutes%15;
        int res = 0 ;
        if(mod >=8){
            res = minutes+(15 - mod);
        }else{
            res = minutes-mod;
        }
        return res; //return rounded minutes
    }

    private String getWaitingTime(){
        Date check;
        int waiting = 0;

        ArrayList<Booking> bookingsToday = new ArrayList<>();
        long currentYear = currentDate.getYear();

        for(Booking b: bookings){
            check = b.getTime();
            int day = check.getDate();
            int month = check.getMonth();
            long year = check.getYear();
            int minute = check.getMinutes();
            int hour = check.getHours();
            if(day == currentDate.getDate() && year == currentYear && month == currentDate.getMonth() && currentYear==year && b.getWalkInId().equals(clinic.getId())){
                int count = 0;
                // insert in sorted order
                for(Booking last: bookingsToday){
                    if(b.getTime().compareTo(last.getTime()) == 1){
                        count++;
                    }else{
                        break;
                    }
                }
                bookingsToday.add(count,b);
            }
        }

        boolean ahead = false;
        check = new Date();
        for (Booking b: bookingsToday){
            DateTime d1 = new DateTime(check);
            DateTime d2 = new DateTime(b.getTime());

            int diff = Minutes.minutesBetween(d1, d2).getMinutes() % 60;
            if(diff <= 16 && diff >= 0){
                waiting = waiting + diff;
                ahead = true;
                check = b.getTime();
            }else if(diff<0 && Math.abs(diff) <= 15){
                waiting= waiting + 15 + diff;
            }
        }
        if(ahead){waiting= waiting+15;}
        return String.valueOf(waiting) + " minutes";
    }

    public void openActivity(Class activity){
        Intent intent = new Intent(this, activity);
        intent.putExtra("user", activeUser);
        intent.putExtra("users", users);
        intent.putExtra("services", services);
        intent.putExtra("clinics", clinics);
        intent.putExtra("bookings", bookings);

        startActivity(intent);
    }

}