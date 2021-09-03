package com.example.smartwallclock;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
import android.provider.Settings;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import pl.droidsonroids.gif.GifImageView;


public class MainActivity extends AppCompatActivity{                                                                                //Designed By Yatharth Shahi
    private WebView webView;                                                                                                        //Comments added on 21/6/2021
    private Button button;                                                                                                          //Project Smart Wall Clock
    private BatteryReciever mBatteryReciever = new BatteryReciever();//Receiver for battery Stats                                   //Project started on 10/6/2021
    private IntentFilter mIntentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);//Intent Filter for Battery Stats          //This isn't a responsive design...this works on a 1280x800px display (16:10) only in landscape
    private GifImageView loopView;                                                                                                  //Different version which works on 1920x1080px display (16:9) is also present
    private VideoView videoView;
    private MediaController mediaController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);//Default
        setContentView(R.layout.activity_main);//Default

        webView = findViewById(R.id.webview);                                                        //Don't remove...will break the app

        videoView = findViewById(R.id.BGvideoView);
        Uri localUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.bg2);
        videoView.setVideoURI(localUri);
        videoView.start();
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });
        MediaPlayer mediaPlayer = new MediaPlayer();

        button = findViewById(R.id.settings);                                               //Settings button
        button.setVisibility(View.VISIBLE);
        button.setEnabled(true);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_setting = new Intent(MainActivity.this, AppSettings.class);
                startActivity(intent_setting);//Used to trigger the settings button to open settings.java activity
            }
        });

        Calendar alarm = Calendar.getInstance();                                                     //Alarm for hourly updates
        alarm.set(Calendar.HOUR_OF_DAY, 15);
        alarm.set(Calendar.MINUTE, 0);
        alarm.set(Calendar.SECOND,0);
        startAlarm(alarm);

        Calendar year_Alarm = Calendar.getInstance();
        year_Alarm.set(Calendar.MONTH, Calendar.JULY);
        year_Alarm.set(Calendar.DATE, 25);
        year_Alarm.set(Calendar.HOUR_OF_DAY, 15);
        year_Alarm.set(Calendar.MINUTE, 25);
        year_Alarm.set(Calendar.SECOND, 49);
        //startAlarmY(year_Alarm);

        Calendar brightness_down = Calendar.getInstance();
        brightness_down.set(Calendar.HOUR_OF_DAY, 11);
        brightness_down.set(Calendar.MINUTE, 55);
        startAlarmBD(brightness_down);

        Calendar brightness_up = Calendar.getInstance();
        brightness_up.set(Calendar.HOUR_OF_DAY, 12);
        brightness_up.set(Calendar.MINUTE, 00);
        startAlarmBU(brightness_up);

        askPermission(this);

        mRunnable.run();                                                                             //Runnable for Weather Update
        runnable.run();                                                                              //Runnable for Network indicator

    }

    public void askPermission(Context c){
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
            if(Settings.System.canWrite(c)){

            }else{
                Intent i = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                c.startActivity(i);
            }
        }
    }

    @Override
    protected void onResume() {                                                                      //For Battery Indicator
        super.onResume();
        registerReceiver(mBatteryReciever, mIntentFilter);
    }

    @Override
    protected void onPause() {                                                                       //For battery indicator
        unregisterReceiver(mBatteryReciever);
        super.onPause();
    }

    //public void settings(){

    //}
    private void hideNavigationBar(){                                                               //Used to run the app in fullscreen...sticky immersive to be accurate
        this.getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION|
                        View.SYSTEM_UI_FLAG_FULLSCREEN|
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY|
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION|
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        );
    }

    private final Handler mHandler = new Handler();

    private final Runnable mRunnable = new Runnable() { //For refreshing Weather Updates
        @Override
        public void run() {
            Toast.makeText(MainActivity.this, "WebView Refreshed", Toast.LENGTH_SHORT).show();//Toast used for prompting that the webview has been refreshed
            SharedPreferences prefs = getSharedPreferences("WebView_URL", MODE_PRIVATE);
            String URL = prefs.getString("URL", "https://www.msn.com/en-in/weather/today/Indira-Puram,Uttar-Pradesh,India/we-city?form=PRWLAS&iso=IN&el=BrSTQuEAtMKg0ieFIZL7dsSW4wabraPd56ExjVOnG498zXJC6snI8phK%2Fm3kngtYUxMgtIHuTpASUdkcj74e18ed11%2FJGPgvtNUExJNmB6MbbUPuzf2%2B2TVwoTiWCxnz");

            webView.setInitialScale(136);                                                            //WebView initial/default zoom level...might be needed to change according to different screen sizes and screen DPI
            webView.loadUrl(URL);                                                                    //webview URL...currently used for weather forcast...loads up msn weather...uses device location to determine weather forcast location

            mHandler.postDelayed(this, 900000);                                          //timing for refresh delay...automatic webview refresh...currently set to 15 mins
        }
    };

    private final Handler handler = new Handler();

    private final Runnable runnable = new Runnable() {                                               //Triggers network status indicator and fullscreen commands
        @Override
        public void run() {
            checkNetworkConnectionState();

            Calendar calendar = Calendar.getInstance();                                                  //Calender instance to determine current date
            String currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime()); //Used to get Date in required format from the Calender instance...to change the format, change the 'Full' in (DateFormat.Full)

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH");
            int currentTime = Integer.parseInt(simpleDateFormat.format(calendar.getTime()));

            TextView textviewdate = findViewById(R.id.text_date);                                        //Textview identifier for displaying date
            textviewdate.setText(currentDate);                                                           //Used to display current date, day, month and year...uses String 'currentDate' from above to display date

            hideNavigationBar (); //Used to run the app in fullscreen
            if (currentTime >= 22 || 07>= currentTime){
                SharedPreferences ScreenBrightness = getSharedPreferences("Brightness", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = ScreenBrightness.edit();
                editor.putInt("Brightness", 0);
                editor.apply();
            }
            else {SharedPreferences ScreenBrightness = getSharedPreferences("Brightness", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = ScreenBrightness.edit();
                editor.putInt("Brightness", 255);
                editor.apply();
            }
            SharedPreferences ScreenBrightness = getSharedPreferences("Brightness", MODE_PRIVATE);
            int BRIGHTNESS = ScreenBrightness.getInt("Brightness", 255);
            Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS,BRIGHTNESS);

            handler.postDelayed(this, 1);
        }
    };

    private void checkNetworkConnectionState() {                                                     //Network status indicator
        ImageView wifistate = (ImageView) findViewById(R.id.wifi_status);

        ConnectivityManager connectivityManager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeInfo = connectivityManager.getActiveNetworkInfo();

        if (activeInfo != null && activeInfo.isConnected()) {
            wifistate.setImageResource(R.drawable.wi_fi_connected);
        } else {
            wifistate.setImageResource(R.drawable.wi_fi_disconnected);
        }

    }

    private void startAlarmBD(Calendar brightness_down) {
        AlarmManager Bd = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent BDintent = new Intent(this, brightnessDown.class);
        PendingIntent BDpendingIntent = PendingIntent.getBroadcast(this, 2, BDintent, 0);
        Bd.setExact(AlarmManager.RTC_WAKEUP, brightness_down.getTimeInMillis(), BDpendingIntent);
    }

    private void startAlarmBU(Calendar brightness_up) {
        AlarmManager Bu = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent BUintent = new Intent(this, brightnessUP.class);
        PendingIntent BUpendingIntent = PendingIntent.getBroadcast(this, 3, BUintent, 0);
        Bu.setExact(AlarmManager.RTC_WAKEUP, brightness_up.getTimeInMillis(), BUpendingIntent);
    }

    private void startAlarm (Calendar alarm){                                                         //Hourly Notification using Ding-Dong clock sound
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent alarmIntent = new Intent(this, AlertReceiver.class);                     //Used for making sound
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarm.getTimeInMillis(), pendingIntent);

    }
    /*private void startAlarmY (Calendar year_Alarm){
        AlarmManager YalarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent yearAlarm = new Intent(this, AlertReceiverYear.class);
        PendingIntent YpendingIntent = PendingIntent.getBroadcast(this, 1, yearAlarm, 0);
        YalarmManager.setExact(AlarmManager.RTC_WAKEUP, year_Alarm.getTimeInMillis(), YpendingIntent);
    }*/

}
