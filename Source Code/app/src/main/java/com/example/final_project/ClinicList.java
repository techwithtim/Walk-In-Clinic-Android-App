package com.example.final_project;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ClinicList extends ArrayAdapter<WalkInClinic> {
    private Activity context;
    List<WalkInClinic> clinics;
    private ImageView star1, star2, star3, star4, star5;

    public ClinicList(Activity context, List<WalkInClinic> clinics) {
        super(context, R.layout.layout_clinic_list, clinics);
        this.context = context;
        this.clinics = clinics;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.layout_clinic_list, null, true);

        TextView textViewName = (TextView) listViewItem.findViewById(R.id.clinicName);
        TextView textViewAddress= (TextView) listViewItem.findViewById(R.id.dateTime);

        WalkInClinic clinic = clinics.get(position);
        textViewName.setText(clinic.getName());
        textViewAddress.setText(String.valueOf(clinic.getAddress()));

        ArrayList<ImageView> stars = new ArrayList<>();
        star1 = listViewItem.findViewById(R.id.star1);
        star2 = listViewItem.findViewById(R.id.star2);
        star3 = listViewItem.findViewById(R.id.star3);
        star4 = listViewItem.findViewById(R.id.star4);
        star5 = listViewItem.findViewById(R.id.star5);
        stars.add(star1); stars.add(star2); stars.add(star3); stars.add(star4); stars.add(star5);

        Integer rating = clinic.getRating();
        for(int x = 0; x < Math.min(rating, 5); x++){
            stars.get(x).setImageDrawable(ContextCompat.getDrawable(context,android.R.drawable.btn_star_big_on));
        }

        return listViewItem;
    }
}
