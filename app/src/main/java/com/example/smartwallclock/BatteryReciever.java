package com.example.smartwallclock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.BatteryManager;
import android.widget.ImageView;

import static android.os.BatteryManager.*;

public class BatteryReciever extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        ImageView powerstatus = ((MainActivity)context).findViewById(R.id.power_status);

        String action = intent.getAction();

        if (action != null && action.equals(Intent.ACTION_BATTERY_CHANGED)){
            int level = intent.getIntExtra(EXTRA_LEVEL,-1);
            int scale = intent.getIntExtra(EXTRA_SCALE,-1);
            int status = intent.getIntExtra(EXTRA_STATUS, -1);
            int percentage = level*100/scale;

            Resources resources = context.getResources();

              switch (status) {
                  case BATTERY_STATUS_CHARGING:
                      powerstatus.setImageDrawable(resources.getDrawable(R.drawable.battery_ac));
                      break;
                  case BATTERY_STATUS_DISCHARGING:
                      if (percentage >= 75) {
                          powerstatus.setImageDrawable(resources.getDrawable(R.drawable.battery_100));
                      } else if (75 > percentage && percentage >= 50) {
                          powerstatus.setImageDrawable(resources.getDrawable(R.drawable.battery_75));
                      } else if (50 > percentage && percentage >= 25) {
                          powerstatus.setImageDrawable(resources.getDrawable(R.drawable.battery_25));
                      } else if (25 > percentage && percentage >= 10) {
                          powerstatus.setImageDrawable(resources.getDrawable(R.drawable.battery_low));
                      } else {
                          powerstatus.setImageDrawable(resources.getDrawable(R.drawable.battery_critical));
                      }
                      break;
                  case BATTERY_STATUS_NOT_CHARGING:
                      if (percentage >= 75) {
                          powerstatus.setImageDrawable(resources.getDrawable(R.drawable.battery_100));
                      } else if (75 > percentage && percentage >= 50) {
                          powerstatus.setImageDrawable(resources.getDrawable(R.drawable.battery_75));
                      } else if (50 > percentage && percentage >= 25) {
                          powerstatus.setImageDrawable(resources.getDrawable(R.drawable.battery_25));
                      } else if (25 > percentage && percentage >= 10) {
                          powerstatus.setImageDrawable(resources.getDrawable(R.drawable.battery_low));
                      } else {
                          powerstatus.setImageDrawable(resources.getDrawable(R.drawable.battery_critical));
                      }
                      break;
              }
        }
    }
}
