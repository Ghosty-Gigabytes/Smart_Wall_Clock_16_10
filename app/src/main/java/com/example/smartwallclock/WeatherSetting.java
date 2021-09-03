package com.example.smartwallclock;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class WeatherSetting extends AppCompatActivity {
    private EditText latitude_edittext;
    private EditText longitude_edittext;
    private SharedPreferences location;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_setting);

        location = getSharedPreferences("LOCATION", MODE_PRIVATE);
        latitude_edittext = findViewById(R.id.latitude);


        Button apply = findViewById(R.id.apply);
        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String lat1 = latitude_edittext.getText().toString();
                SharedPreferences.Editor editor = location.edit();

                editor.putString("latDouble", lat1);
                editor.apply();
            }
        });

    }


}