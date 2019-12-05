package com.example.final_project;

import android.app.Activity;
import android.app.AlertDialog;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.LayoutInflater;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AppointmentFinishedList extends ArrayAdapter<Booking> {
    private Activity context;
    List<Booking> bookings;
    private View listViewItem;
    private WalkInClinic clinic;
    private static DatabaseReference databaseWalkIn = FirebaseDatabase.getInstance().getReference("clinics");
    private static DatabaseReference databaseBookings = FirebaseDatabase.getInstance().getReference("bookings");
    private boolean rated;

    public AppointmentFinishedList(Activity context, List<Booking> bookings) {
        super(context, R.layout.layout_appointment_finished_list, bookings);
        this.context = context;
        this.bookings = bookings;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        listViewItem = inflater.inflate(R.layout.layout_appointment_finished_list, null, true);

        rated = false;

        TextView textViewName = (TextView) listViewItem.findViewById(R.id.clinicName);
        TextView textViewService = (TextView) listViewItem.findViewById(R.id.clinicService);
        TextView textViewTime = (TextView) listViewItem.findViewById(R.id.dateTime);

        Booking booking = bookings.get(position);
        clinic = booking.getClinic();
        DataBaseService service = booking.getService();
        textViewName.setText(clinic.getName());
        textViewService.setText(service.getName());
        String pattern = "yyyy-MM-dd HH:mm ";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        Date showDate = new Date();
        showDate.setHours(booking.getTime().getHours());
        showDate.setMinutes(booking.getTime().getMinutes());
        showDate.setDate(booking.getTime().getDate());
        showDate.setMonth(booking.getTime().getMonth());
        showDate.setYear(booking.getTime().getYear());

        String date = simpleDateFormat.format(showDate);
        textViewTime.setText(date);

        Button rate = listViewItem.findViewById(R.id.rateBtn);

        if(booking.getRating()!=0){
            rate.setText("Rated: " + booking.getRating() + " Stars");
        }

        rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRatingDialog(position);
            }
        });


        return listViewItem;
    }

    private void showRatingDialog(int pos) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = context.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.rating_dialog, null);
        dialogBuilder.setView(dialogView);

        final AlertDialog b = dialogBuilder.create();
        b.show();

        Booking booking = bookings.get(pos);
        boolean edit = booking.getRating()==0;

        ImageButton star1, star2, star3, star4, star5;
        star1 = dialogView.findViewById(R.id.star1);
        star2 = dialogView.findViewById(R.id.star2);
        star3 = dialogView.findViewById(R.id.star3);
        star4 = dialogView.findViewById(R.id.star4);
        star5 = dialogView.findViewById(R.id.star5);
        ArrayList<ImageButton> stars = new ArrayList<>();
        stars.add(star1); stars.add(star2); stars.add(star3); stars.add(star4); stars.add(star5);

        if(!edit) {
            setStars(stars, booking.getRating());
            TextView title = (TextView)dialogView.findViewById(R.id.textViewTitle);
            title.setText("Thanks For Your Feedback!");
        }


        star1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickStar(1, edit, stars, booking);
                if(edit) b.dismiss();
            }
        });

        star2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickStar(2, edit, stars, booking);
                if(edit) b.dismiss();
            }
        });

        star3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickStar(3, edit, stars, booking);
                if(edit) b.dismiss();
            }
        });

        star4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickStar(4, edit, stars, booking);
                if(edit) b.dismiss();
            }
        });

        star5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickStar(5, edit, stars, booking);
                if(edit) b.dismiss();
            }
        });

        Button close = dialogView.findViewById(R.id.closeButton2);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b.dismiss();
            }
        });

    }

    private void clickStar(int star, boolean edit, ArrayList<ImageButton> stars, Booking booking){
        if (edit) {
            setStars(stars, star);
            clinic.addRating(star);
            booking.setRating(star);
            updateDB(booking);
        }else{
            Toast.makeText(context, "You Have Already Rated, Thank You!", Toast.LENGTH_LONG).show();
        }

    }

    private void setStars(ArrayList<ImageButton> stars, int rate){
        for(int i = 0; i < stars.size(); i++){
            System.out.println("WORKED " + i);
            if(i < rate){
                stars.get(i).setImageDrawable(ContextCompat.getDrawable(context,android.R.drawable.btn_star_big_on));
            }else{
                stars.get(i).setImageDrawable(ContextCompat.getDrawable(context,android.R.drawable.btn_star_big_off));
            }
        }
    }

    private void updateDB(Booking booking){

        DatabaseReference dR = databaseWalkIn.child(clinic.getId());
        dR.setValue(clinic);

        DatabaseReference dr = databaseBookings.child(booking.getId());
        dr.setValue(booking);

    }
}
