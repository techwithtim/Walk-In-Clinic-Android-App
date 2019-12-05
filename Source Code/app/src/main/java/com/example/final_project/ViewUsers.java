package com.example.final_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewUsers extends AppCompatActivity {
    private Administrator activeUser;
    private ArrayList<DataBaseUser> users;
    private static DatabaseReference databaseUsers = FirebaseDatabase.getInstance().getReference("users");
    ListView listViewUsers;
    Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_users);

        users = new ArrayList<>();

        listViewUsers = (ListView)findViewById(R.id.userList);

        listViewUsers.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                DataBaseUser user = users.get(i);
                showUpdateDeleteDialog(user);
                return true;
            }
        });

        back = (Button) findViewById(R.id.backBtn);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();
            }
        });

        Intent i = getIntent();
        activeUser = (Administrator) i.getSerializableExtra("user");
        users = (ArrayList<DataBaseUser>) i.getSerializableExtra("users");
        activeUser.setUsers(users);

        databaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                users.clear();
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    DataBaseUser usr = postSnapshot.getValue(DataBaseUser.class);

                    if(!usr.getRole().equals("Admin")){
                        users.add(usr); // add all users expect admin
                    }
                }
                activeUser.setUsers(users);
                UserList usersAdapter = new UserList(ViewUsers.this, users);
                listViewUsers.setAdapter(usersAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void showUpdateDeleteDialog(DataBaseUser user) {
        activeUser.deleteUser(user);
        Toast.makeText(this, "User deleted", Toast.LENGTH_LONG).show();
    }

    public void goBack(){
        Intent i = new Intent(this, Admin.class);
        i.putExtra("user", activeUser);
        i.putExtra("users", activeUser.getUsers());
        i.putExtra("services", activeUser.getServices());
        startActivity(i);
    }
}
