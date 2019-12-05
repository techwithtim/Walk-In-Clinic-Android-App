package com.example.final_project;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AppointmentUpcomingList extends ArrayAdapter<Booking> {
    private Activity context;
    List<Booking> bookings;

    public AppointmentUpcomingList(Activity context, List<Booking> bookings) {
        super(context, R.layout.layout_service_list, bookings);
        this.context = context;
        this.bookings = bookings;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.layout_appointment_list, null, true);

        TextView textViewName = (TextView) listViewItem.findViewById(R.id.clinicName);
        TextView textViewDate = (TextView) listViewItem.findViewById(R.id.dateTime);
        TextView textViewService = (TextView) listViewItem.findViewById(R.id.clinicService);

        Booking booking = bookings.get(position);
        textViewName.setText(booking.getClinic().getName());
        String pattern = "yyyy-MM-dd HH:mm ";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        Date showDate = new Date();
        showDate.setHours(booking.getTime().getHours());
        showDate.setMinutes(booking.getTime().getMinutes());
        showDate.setDate(booking.getTime().getDate());
        showDate.setMonth(booking.getTime().getMonth());
        showDate.setYear(booking.getTime().getYear());

        String date = simpleDateFormat.format(showDate);
        textViewDate.setText(date);
        textViewService.setText(booking.getService().getName());
        return listViewItem;
    }
}
