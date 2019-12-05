package com.example.final_project;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class UserList extends ArrayAdapter<DataBaseUser> {
    private Activity context;
    List<DataBaseUser> users;

    public UserList(Activity context, List<DataBaseUser> users) {
        super(context, R.layout.layout_user_list, users);
        this.context = context;
        this.users = users;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.layout_user_list, null, true);

        TextView textViewName = (TextView) listViewItem.findViewById(R.id.textViewName);
        TextView textViewRole = (TextView) listViewItem.findViewById(R.id.textViewRole);
        TextView textViewUsername = (TextView) listViewItem.findViewById(R.id.textViewUsername);

        DataBaseUser user = users.get(position);
        textViewName.setText("Name: " + user.getName());
        textViewRole.setText("Role: " + String.valueOf(user.getRole()));
        textViewUsername.setText("Username: " + String.valueOf(user.getUsername()));
        return listViewItem;
    }
}
