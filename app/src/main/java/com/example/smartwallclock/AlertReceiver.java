package com.example.smartwallclock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Handler;

public class AlertReceiver extends BroadcastReceiver {
    private MediaPlayer alarmRinger;
    @Override
    public void onReceive(Context context, Intent alarmIntent) {
        SharedPreferences audio_Preference = context.getSharedPreferences("save", Context.MODE_PRIVATE);

        alarmRinger = MediaPlayer.create(context, R.raw.alarmringer);                                //Used for specifying audio track for hourly updates

        boolean preference = audio_Preference.getBoolean("save", false);
       if (preference == true) {
           runnable.run();
            }
       }

    private final Handler handler = new Handler();
    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            alarmRinger.start();                                                                     //Used for playing the sound
            handler.postDelayed(this, 3600000);                                          //Repeating frequency
        }
    };


}
