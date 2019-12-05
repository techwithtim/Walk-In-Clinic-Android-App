package com.example.final_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;


public class Appointment extends AppCompatActivity {

    private Patient activeUser;
    private ArrayList<DataBaseService> services;
    private ArrayList<DataBaseUser> users;
    private ArrayList<WalkInClinic> clinics;

    private static DatabaseReference databaseClinics = FirebaseDatabase.getInstance().getReference("clinics");
    ListView listViewAppointmentsUpcoming;
    AppointmentUpcomingList adapterUpcoming;

    private static DatabaseReference databaseBookings = FirebaseDatabase.getInstance().getReference("bookings");
    ListView listViewAppointmentsDone;
    AppointmentFinishedList adapterDone;

    private ArrayList<Booking> currentBookings, pastBookings, bookings;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);

        Intent i = getIntent();
        activeUser = (Patient) i.getSerializableExtra("user");
        services = (ArrayList<DataBaseService>) i.getSerializableExtra("services");
        users = (ArrayList<DataBaseUser>) i.getSerializableExtra("users");
        currentBookings = new ArrayList<>();
        pastBookings = new ArrayList<>();
        clinics = new ArrayList<>();
        bookings = new ArrayList<>();

        Button backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(PatientUser.class);
            }
        });


        databaseClinics.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                clinics.clear();
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    WalkInClinic clinic = postSnapshot.getValue(WalkInClinic.class);
                    clinics.add(clinic);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });

        databaseBookings.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                pastBookings.clear();
                currentBookings.clear();
                bookings.clear();
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Booking booking = postSnapshot.getValue(Booking.class);
                    Date currentDate = new Date(System.currentTimeMillis());
                    Date bookingTime = booking.getTime();
                    if (booking.getPatientId().equals(activeUser.getId())) {  // if the booking is for this appointment
                        if (booking.getTime().compareTo(new Date()) == 1) {  // if date is passed current time
                            currentBookings.add(booking);
                        } else {
                            pastBookings.add(booking);
                        }
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

                        if(booking.getService()==null){ // if service booking is deleted, delete booking
                            deleteAppointment(booking);
                        }
                    }
                }
                sortByDate(currentBookings, 1);
                sortByDate(pastBookings, -1);

                adapterDone = new AppointmentFinishedList(Appointment.this, pastBookings);
                listViewAppointmentsDone.setAdapter(adapterDone);

                adapterUpcoming = new AppointmentUpcomingList(Appointment.this, currentBookings);
                listViewAppointmentsUpcoming.setAdapter(adapterUpcoming);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        listViewAppointmentsDone= findViewById(R.id.pastAptList);
        listViewAppointmentsDone.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openRemoveDialog(pastBookings.get(position));
            }
        });

        listViewAppointmentsUpcoming = findViewById(R.id.upcomingAptList);
        listViewAppointmentsUpcoming.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openDialog(currentBookings.get(position));
            }
        });
    }


    private void sortByDate(ArrayList<Booking> bookings, int order){
        int n = bookings.size();
        for (int i = 1; i < n; ++i) {
            Booking booking = bookings.get(i);
            Date key = booking.getTime();
            int j = i - 1;

            /* Move elements of arr[0..i-1], that are
               greater than key, to one position ahead
               of their current position */
            while (j >= 0 && bookings.get(j).getTime().compareTo(key) == order) {
                 bookings.set(j+1, bookings.get(j));
                 j = j - 1;
            }
            bookings.set(j + 1, booking);
        }
    }

    public void openRemoveDialog(Booking booking){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.remove_dialog, null);
        dialogBuilder.setView(dialogView);

        final AlertDialog b = dialogBuilder.create();
        b.show();

        Button close, remove;

        close = dialogView.findViewById(R.id.closeButton);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b.dismiss();
            }
        });

        remove = dialogView.findViewById(R.id.removeBtn);
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(deleteAppointment(booking)) {
                    Toast.makeText(getApplicationContext(), "Appointment Removed", Toast.LENGTH_LONG).show();
                    b.dismiss();
                }else{
                    Toast.makeText(getApplicationContext(), "Unexpected Error, Try Again", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void openDialog(Booking booking){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.appointment_dialog, null);
        dialogBuilder.setView(dialogView);

        final AlertDialog b = dialogBuilder.create();
        b.show();

        TextView name, address, service, time;
        name = dialogView.findViewById(R.id.clinicName);
        address = dialogView.findViewById(R.id.clinicAddress);
        service = dialogView.findViewById(R.id.clinicService);
        time = dialogView.findViewById(R.id.dateTime);

        name.setText("Clinic: " + booking.getClinic().getName());
        address.setText("Address: " + booking.getClinic().getAddress());
        service.setText("Service: " + booking.getService().getName());
        String pattern = "yyyy-MM-dd HH:mm ";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        Date showDate = new Date();
        showDate.setHours(booking.getTime().getHours());
        showDate.setMinutes(booking.getTime().getMinutes());
        showDate.setDate(booking.getTime().getDate());
        showDate.setMonth(booking.getTime().getMonth());
        showDate.setYear(booking.getTime().getYear());

        String date = simpleDateFormat.format(showDate);
        time.setText("Date/Time: " + date);

        Button cancelBtn = dialogView.findViewById(R.id.cancelBtn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(deleteAppointment(booking)) {
                    Toast.makeText(getApplicationContext(), "Appointment Has Been Cancelled", Toast.LENGTH_LONG).show();
                    b.dismiss();
                }else{
                    Toast.makeText(getApplicationContext(), "An Unexpected Error Occurred", Toast.LENGTH_LONG).show();
                }
                adapterUpcoming.notifyDataSetChanged();
            }
        });


        Button closeBtn = dialogView.findViewById(R.id.closeButton2);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b.dismiss();
            }
        });
    }

    public boolean deleteAppointment(Booking booking){
        try {
            DatabaseReference dR = databaseBookings.child(booking.getId());
            dR.removeValue();
            return true;
        }catch(Exception e){
            return false;
        }
    }

    public void openActivity(Class activity){
        Intent intent = new Intent(this, activity);
        intent.putExtra("user", activeUser);
        intent.putExtra("services", services);
        intent.putExtra("users", users);
        intent.putExtra("bookings", bookings);
        intent.putExtra("clinics", clinics);


        startActivity(intent);
    }

}
