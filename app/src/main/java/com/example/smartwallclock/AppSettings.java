package com.example.smartwallclock;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AppSettings extends AppCompatActivity {
    private Button weather;
    private Button help;
    private Button audio;
    private TextView longitudeview;
    private TextView latitudeview;
    private SharedPreferences location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        hideNavigationBar ();                                                                        //Used to run the app in fullscreen

        location = getSharedPreferences("LOCATION", MODE_PRIVATE);
        //Double latValue = Double.parseDouble(location.getString("latDouble",""));
        longitudeview = findViewById(R.id.longitudeview);
        //latitudeview.setText(latValue);
        weather = (Button) findViewById(R.id.weather_URL);                                           //Used for opening WeatherSettings.java
        weather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWeatherSettings();

            }
        });

        help = (Button) findViewById(R.id.help);                                                     //Used for opening Help.java
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openHelp();
            }
        });

        audio = (Button) findViewById(R.id.audio_settings);
        audio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAudioSettings();
            }
        });

    }

    public void openAudioSettings(){
        Intent audioIntent;
        audioIntent = new Intent(this, audioSetting.class);
        startActivity(audioIntent);
        finish();
    }
    public void openHelp(){                                                                          //Used for opening Help.java
        Intent intent1;
        intent1 = new Intent(this, Help.class);
        startActivity(intent1);
        finish();
    }

    public void openWeatherSettings(){                                                               //Used for opening WeatherSettings.java
        Intent intent;
        intent = new Intent(this, WeatherSetting.class);
        startActivity(intent);
        finish();
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