package com.example.final_project;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ServiceList extends ArrayAdapter<DataBaseService> {
    private Activity context;
    List<DataBaseService> services;

    public ServiceList(Activity context, List<DataBaseService> services) {
        super(context, R.layout.layout_service_list, services);
        this.context = context;
        this.services = services;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.layout_service_list, null, true);

        TextView textViewName = (TextView) listViewItem.findViewById(R.id.textViewName);
        TextView textViewRole = (TextView) listViewItem.findViewById(R.id.textViewRole);

        DataBaseService service = services.get(position);
        textViewName.setText("Name: " + service.getName());
        textViewRole.setText("Performed By: " + String.valueOf(service.getRole()));
        return listViewItem;
    }
}
