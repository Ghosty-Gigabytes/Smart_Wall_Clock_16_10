package com.example.smartwallclock;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

public class audioSetting extends AppCompatActivity {
    Switch audio_switch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.audio_settinga);

        audio_switch = findViewById(R.id.switch1);

        SharedPreferences audioPreference = getSharedPreferences("save", MODE_PRIVATE);

        audio_switch.setChecked(audioPreference.getBoolean("value", true));
        audio_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (audio_switch.isChecked()){
                    SharedPreferences.Editor editor = getSharedPreferences("save", MODE_PRIVATE).edit();
                    editor.putBoolean("save", true);
                    editor.apply();
                    audio_switch.setChecked(true);
                }
                else{
                    SharedPreferences.Editor editor = getSharedPreferences("save", MODE_PRIVATE).edit();
                    editor.putBoolean("save", false);
                    editor.apply();
                    audio_switch.setChecked(false);
                }
            }
        });
    }
}