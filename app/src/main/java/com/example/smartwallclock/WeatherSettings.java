package com.example.smartwallclock;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class WeatherSettings extends AppCompatActivity {
    private Button help_weather;
    private EditText ETURL;
    private Button Apply;
    private SharedPreferences prefs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_settings);

        prefs = getSharedPreferences("WebView_URL", MODE_PRIVATE);                             //SharedPreferences...used for storing User inputted URL
        String URL = prefs.getString("URL", "");

        ETURL = findViewById(R.id.weather_URL);
        ETURL.setText(URL);

        help_weather = findViewById(R.id.help1);                                            //Used for opening Help.java
        help_weather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openHelp();
            }
        });
        Apply = findViewById(R.id.apply1);
        Apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });
        hideNavigationBar();                                                                         //Used to run the app in fullscreen
    }

    public void saveData(){
        String URL = ETURL.getText().toString();                                                     //Get input text

        SharedPreferences.Editor editor = prefs.edit();                                              //Saving data
        editor.putString("URL", URL);
        editor.apply();

        startActivity(new Intent(getApplicationContext(),MainActivity.class));
    }
    public void openHelp(){                                                                          //Used for opening Help.java
        Intent intent3=new Intent(this, Help.class);
        startActivity(intent3);
    }

    private void hideNavigationBar(){                                                                //Used to run the app in fullscreen...sticky immersive to be accurate
        this.getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION|
                        View.SYSTEM_UI_FLAG_FULLSCREEN|
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY|
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION|
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        );
    }
}