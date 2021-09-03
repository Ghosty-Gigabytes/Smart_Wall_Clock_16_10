package com.example.smartwallclock;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;

import pl.droidsonroids.gif.GifImageView;


public class MainActivity2 extends AppCompatActivity{                                                                               //Designed By Yatharth Shahi
    private WebView webView;                                                                                                        //Comments added on 21/6/2021
    private Button button;                                                                                                          //Project Smart Wall Clock
    private BatteryReciever mBatteryReciever = new BatteryReciever();//Receiver for battery Stats                                   //Project started on 10/6/2021
    private IntentFilter mIntentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);//Intent Filter for Battery Stats          //This isn't a responsive design...this works on a 1280x800px display (16:10) only in landscape
    private GifImageView loopView;                                                                                                  //Different version which works on 1920x1080px display (16:9) is also present
    private VideoView videoView;
    private MediaController mediaController;


    //Your Key
    String Key = "";

    String url1 = "https://samples.openweathermap.org/data/2.5/weather?q=London&appid=439d4b804bc8187953eb36d2a8c26a02";



    TextView txtCity,txtTime,txtValue,txtValueFeelLike,txtValueHumidity,txtValueVision;

    String nameIcon = "10d";

    EditText editText;

    Button btnLoading;

    ImageView imgIcon;

    RelativeLayout relativeLayoutLayoutMain;
    RelativeLayout relativeLayout;

    public class DownloadImage extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... strings) {
            Bitmap bitmap = null;

            URL url;

            HttpURLConnection httpURLConnection;

            InputStream inputStream;

            try {
                Log.i("LINK",strings[0]);
                url = new URL(strings[0]);

                httpURLConnection = (HttpURLConnection) url.openConnection();

                inputStream = httpURLConnection.getInputStream();

                bitmap = BitmapFactory.decodeStream(inputStream);


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return bitmap;
        }
    }

    public class DownloadTask extends AsyncTask<String, Void , String> {
        @Override
        protected String doInBackground(String... strings) {

            String result = "";

            URL url;

            HttpURLConnection httpURLConnection;

            InputStream inputStream;

            InputStreamReader inputStreamReader;

            try {

                url = new URL(strings[0]);

                httpURLConnection = (HttpURLConnection) url.openConnection();

                inputStream = httpURLConnection.getInputStream();

                inputStreamReader = new InputStreamReader(inputStream);

                int data = inputStreamReader.read();

                while(data != -1) {

                    result += (char) data;

                    data = inputStreamReader.read();

                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return result;
        }
    }

    public void loading(View view) {

        editText.setVisibility(View.INVISIBLE);
        btnLoading.setVisibility(View.INVISIBLE);
        relativeLayout.setVisibility(View.VISIBLE);
        relativeLayoutLayoutMain.setBackgroundColor(Color.parseColor("#E6E6E6"));

        SharedPreferences location = getSharedPreferences("LOCATION", MODE_PRIVATE);

        Double lat = Double.parseDouble(location.getString("latDouble", ""));
        Double lon = Double.parseDouble(location.getString("lonDouble", ""));

        String url = "https://api.openweathermap.org/data/2.5/onecall?lat=" + lat +"&lon="+ lon +"&units=metric&appid=" + Key;

        DownloadTask downloadTask = new DownloadTask();

        try {

            String result = "abc";

            result = downloadTask.execute(url).get();

            Log.i("Result:",result);

            JSONObject jsonObject = new JSONObject(result);

            JSONObject main = jsonObject.getJSONObject("main");

            JSONObject weather = jsonObject.getJSONObject("weather");

            String temp = main.getString("temp");

            String humidity = main.getString("humidity");

            String feel_like = main.getString("feels_like");

            String visibility = jsonObject.getString("visibility");

            String Place = jsonObject.getString("name");

            nameIcon = jsonObject.getJSONArray("weather").getJSONObject(0).getString("icon");

            Log.i("Name Icon",nameIcon);


            txtCity.setText(Place);

            txtValue.setText(temp + "°");

            txtValueVision.setText(visibility);

            txtValueHumidity.setText(humidity);

            txtValueFeelLike.setText(feel_like);

            DownloadImage downloadImage = new DownloadImage();

            String urlIcon = " https://openweathermap.org/img/wn/"+ nameIcon +"@2x.png";

            Bitmap bitmap = downloadImage.execute(urlIcon).get();

            imgIcon.setImageBitmap(bitmap);

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);//Default
        setContentView(R.layout.activity_main);//Default

        SharedPreferences prefs = getSharedPreferences("WebView_URL", MODE_PRIVATE);           //SharedPreferences...used for storing User inputted URL
        String URL = prefs.getString("URL","https://www.msn.com/en-in/weather/today/Indira-Puram,Uttar-Pradesh,India/we-city?form=PRWLAS&iso=IN&el=BrSTQuEAtMKg0ieFIZL7dsSW4wabraPd56ExjVOnG498zXJC6snI8phK%2Fm3kngtYUxMgtIHuTpASUdkcj74e18ed11%2FJGPgvtNUExJNmB6MbbUPuzf2%2B2TVwoTiWCxnz");

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

        button = (Button) findViewById(R.id.settings);                                               //Settings button
        button.setVisibility(View.VISIBLE);
        button.setEnabled(true);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settings();
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

    public void settings(){
        Intent intent_settings = new Intent(this, AppSettings.class);
        startActivity(intent_settings);//Used to trigger the settings button to open settings.java activity
    }
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
            Toast.makeText(MainActivity2.this, "WebView Refreshed", Toast.LENGTH_SHORT).show();//Toast used for prompting that the webview has been refreshed

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
