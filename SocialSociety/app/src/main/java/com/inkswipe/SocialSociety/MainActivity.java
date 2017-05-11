package com.inkswipe.SocialSociety;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.flurry.android.FlurryAgent;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.Timer;
import java.util.TimerTask;

import util.AppPreferences;
import util.Common;
import util.ExceptionHandler;
import util.LoggerGeneral;

//RFRD6MNNVSYQ2XRCCFRT

//AIzaSyB2bRbeTimYqXxSyxY1KkwAUxilMQpKu0g

public class MainActivity extends AppCompatActivity implements OnProgressBarListener {
    private NumberProgressBar bnp;
    Timer timer;
    int progress=0;
    AppPreferences appPreferences;
    String loggedin;
    String notification,description,external_uri,SocietyId,fromPushNotification,eventId,eventType,imageurl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        setContentView(R.layout.activity_main);
        Common.appcheck=true;
        appPreferences=AppPreferences.getAppPreferences(MainActivity.this);

        String token = FirebaseInstanceId.getInstance().getToken();

        Log.d("", "Refreshed token:" + token);

        Log.d("Main", "token :" + token);

        appPreferences.putString("Token", token);

        SocietyId=getIntent().getStringExtra("SocietyId");
        eventId=getIntent().getStringExtra("event_id");
        eventType=getIntent().getStringExtra("event_type");
        fromPushNotification=getIntent().getStringExtra("fromPushNotification");

        Log.d("From", ":" + SocietyId+"===="+eventType+"===="+fromPushNotification+"====="+eventId);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.my_statusbar_color1));
        }
       /* Handler handler = new Handler();


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {

                Intent register = new Intent(MainActivity.this,Login.class);
                startActivity(register);
                finish();

            }

        }, 3000);*/
        bnp = (NumberProgressBar) findViewById(R.id.number_progress_bar);
        bnp.setOnProgressBarListener(this);
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        bnp.incrementProgressBy(1);

                        progress = bnp.getProgress();

                        if (progress == 100) {
                            timer.cancel();


                            loggedin = appPreferences.getString("user_id","");

                            //    if(appPreferences.getString("user_id","")!=null ||appPreferences.getString("user_id","")!=""||!appPreferences.getString("user_id","").equals("")||!appPreferences.getString("user_id","").equals(null)){

                            if(loggedin.equals(null)||loggedin.equals("")||loggedin==""||loggedin==null){

                                Intent gocat = new Intent(MainActivity.this, Login.class);
                                startActivity(gocat);
                                finish();
                                LoggerGeneral.info("login22--" + appPreferences.getString("user_id", ""));
                            }
                            else {

                                if(SocietyId!=null && eventType!=null)
                                {
                                    if(eventType.equals("1") || eventType=="1")
                                    {
                                        Intent dashBoard=new Intent(MainActivity.this,DashBoard.class);
                                        dashBoard.putExtra("SocietyId",SocietyId);
                                        startActivity(dashBoard);
                                        finish();
                                    }
                                    else if(eventType.equals("3") || eventType=="3")
                                    {
                                        Intent dashBoard=new Intent(MainActivity.this,EventDetails.class);
                                        dashBoard.putExtra("SocietyId",SocietyId);
                                        dashBoard.putExtra("event_id",eventId);
                                        dashBoard.putExtra("fromPushNotification",fromPushNotification);
                                        startActivity(dashBoard);
                                        finish();
                                    }
                                    else if(eventType.equals("2") || eventType=="2")
                                    {
                                        Intent dashBoard=new Intent(MainActivity.this,PollDetails.class);
                                        dashBoard.putExtra("SocietyId",SocietyId);
                                        dashBoard.putExtra("poll_id",eventId);
                                        dashBoard.putExtra("fromPushNotification",fromPushNotification);
                                        startActivity(dashBoard);
                                        finish();
                                    }
                                    else if(eventType.equals("4") || eventType=="4")
                                    {
                                        Intent dashBoard=new Intent(MainActivity.this,DashBoard.class);
                                        dashBoard.putExtra("SocietyId",SocietyId);
                                        dashBoard.putExtra("announcement",4);
                                        startActivity(dashBoard);
                                        finish();
                                    }
                                }
                                else {
                                    Intent gocat = new Intent(MainActivity.this, Profile.class);
                                    gocat.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    gocat.putExtra("Hooooo", "Hooooo");
                                    startActivity(gocat);
                                    finish();
                                }
                                LoggerGeneral.info("login--"+appPreferences.getString("user_id",""));
                            }

                        }
                    }
                });
            }
        }, 450, 30);

    }

    @Override
    public void onProgressChange(int current, int max) {

    }

    @Override
    protected void onStart() {
        super.onStart();
        FlurryAgent.onStartSession(this, "QTPRGMHYD693T2MFCTN2");
        FlurryAgent.setLogEnabled(true);
        FlurryAgent.setLogEvents(true);
        FlurryAgent.setLogLevel(Log.VERBOSE);

    }

    @Override
    protected void onStop() {
        super.onStop();
        FlurryAgent.onEndSession(this);
    }

}
