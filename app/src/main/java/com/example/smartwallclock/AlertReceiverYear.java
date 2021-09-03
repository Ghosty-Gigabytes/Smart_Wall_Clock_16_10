package com.example.smartwallclock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlertReceiverYear extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent yearAlarm) {
        Intent intent = new Intent(context, CountDown10.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //context.startActivity(intent);

    }
}
