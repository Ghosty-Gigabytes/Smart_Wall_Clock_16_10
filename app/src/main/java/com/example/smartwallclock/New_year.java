package com.example.smartwallclock;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class New_year extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_year);
/*
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i=new Intent(New_year.this,
                        MainActivity.class);
                //Intent is used to switch from one activity to another.

                startActivity(i);
                //invoke the SecondActivity.

                finish();
                //the current activity will get finished.
            }
        }, 5000);
*/    }
}