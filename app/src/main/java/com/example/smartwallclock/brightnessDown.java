package com.example.smartwallclock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class brightnessDown extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent BDintent) {
        SharedPreferences ScreenBrightness = context.getSharedPreferences("Brightness", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = ScreenBrightness.edit();
        editor.putInt("Brightness", 0);
        editor.apply();
    }
}
