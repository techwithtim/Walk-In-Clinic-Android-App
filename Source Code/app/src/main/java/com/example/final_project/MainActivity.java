package com.example.final_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button1 = findViewById(R.id.SignInButton);
        Button button2 = findViewById(R.id.SignUpButton);


        button1.setOnClickListener(this);
        button2.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.SignInButton:
                openSignIn();
                break;
            case R.id.SignUpButton:
                openSignUp();
                break;
        }
    }

    public void openSignIn() {
        Intent intent = new Intent(this, SignIn.class);
        startActivity(intent);
    }

    public void openSignUp() {
        Intent intent = new Intent(this, SignUp.class);
        startActivity(intent);
    }

}