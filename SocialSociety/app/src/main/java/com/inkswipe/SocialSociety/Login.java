package com.inkswipe.SocialSociety;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flurry.android.FlurryAgent;
import com.google.firebase.analytics.FirebaseAnalytics;

import org.json.JSONException;
import org.json.JSONObject;

import util.AppPreferences;
import util.Common;
import util.Constants;
import util.ExceptionHandler;
import util.LoggerGeneral;
import util.ServiceFacade;


public class Login extends AppCompatActivity {
    Context context;
    TextView forgetPassword;
    TextView register,newHere;
    MyEditTextView email,password;
    LinearLayout loginRegister;
    String emailtxt,passwordtxt;
    AppPreferences appPreferences;
    String android_id;
    private FirebaseAnalytics mFirebaseAnalytics;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        setContentView(R.layout.activity_login);
        context = Login.this;
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        android_id = Settings.Secure.getString(this.getContentResolver(),Settings.Secure.ANDROID_ID);
        appPreferences = AppPreferences.getAppPreferences(context);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

     /*   MyApplication application = (MyApplication) getApplication();
        mTracker = application.getDefaultTracker();*/
        TextView title=((TextView) toolbar.findViewById(R.id.toolbar_title));
        title.setText("Login");
        title.setTypeface(Common.font(context, "arial.ttf"), 1);
        toolbar.setTitleTextColor(android.graphics.Color.WHITE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.my_statusbar_color));
        }



        LoggerGeneral.info("inLogin");
        register  = (TextView)findViewById(R.id.register);
        register.setTypeface(Common.font(context, "arial.ttf"));
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent login = new Intent(Login.this, Register.class);
                login.putExtra("register", "register");
                startActivity(login);
                finish();
            }
        });

        forgetPassword= (TextView) findViewById(R.id.forgetpassword);
        forgetPassword.setTypeface(Common.font(context, "arial.ttf"));
        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent login = new Intent(Login.this, ForgetPassword.class);
                startActivity(login);
                finish();
            }
        });

        newHere = (TextView)findViewById(R.id.newHere);
        newHere.setTypeface(Common.font(context, "arial.ttf"));

        email = (MyEditTextView)findViewById(R.id.email);
        password = (MyEditTextView)findViewById(R.id.password);




        loginRegister = (LinearLayout)findViewById(R.id.loginRegister);
        loginRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailtxt = email.getText().toString().toLowerCase().trim();

                passwordtxt = password.getText().toString().trim().replace(" ", "");
                if(emailtxt!=null && email.length()>0) {

                    if(Common.emailValidator(emailtxt)) {
                        if(passwordtxt!=null && passwordtxt.length()>0) {


                          /*  Intent register   = new Intent(Login.this,Profile.class);
                            startActivity(register);
                            finish();
*/
                            if(Common.isOnline(context)) {
                                new getLogin("Login").execute();

                              //  In
                            }
                            else {
                                Common.showToast(context,"No internet connection!");
                            }
                        }
                        else {
                            Common.showToast(context,"Enter password");
                        }
                    }
                    else {
                        Common.showToast(context,"Enter a valid email id");
                    }
                }
                else {
                    Common.showToast(context,"Enter email id");
                }
            }
        });

        mFirebaseAnalytics.setUserProperty("screen_open", "1");

        recordScreen();
    }

    private void sendScreenImageName() {
        String name = getClass().getName();

        // [START screen_view_hit]
        LoggerGeneral.info("Setting screen name: " + name);
      //  mTracker.setScreenName("Screen~" + name);
      //  mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        // [END screen_view_hit]
    }

    BroadcastReceiver broadcast_reciever2 = new BroadcastReceiver() {

        @Override
        public void onReceive(Context arg0, Intent intent) {
            String action = intent.getAction();
            if (action.equals("finish_activity2")) {

                LoggerGeneral.info("broadcast entered");
                finish();
                // DO WHATEVER YOU WANT.
            }
        }
    };
    @Override
    protected void onResume() {
        super.onResume();

        registerReceiver(broadcast_reciever2, new IntentFilter("finish_activity2"));

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();


        unregisterReceiver(broadcast_reciever2);
    }


    @Override
    public void onBackPressed() {

        if(Build.VERSION.SDK_INT >23)
        {
            AlertDialog.Builder ab = new AlertDialog.Builder(Login.this,R.style.MyAlertDialogStyle);
            ab.setMessage("Are you sure to exit?");
            ab.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    //if you want to kill app . from other then your main avtivity.(Launcher)
                    android.os.Process.killProcess(android.os.Process.myPid());
                    System.exit(1);
                    finish();

                    //if you want to finish just current activity

                    //Profile.this.finish();
                }
            });
            ab.setNegativeButton("no", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });


            ab.show();
        }
        else {

            AlertDialog.Builder ab = new AlertDialog.Builder(Login.this);
            ab.setMessage("Are you sure to exit?");
            ab.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    //if you want to kill app . from other then your main avtivity.(Launcher)
                    android.os.Process.killProcess(android.os.Process.myPid());
                    System.exit(1);
                    finish();

                    //if you want to finish just current activity

                    //Profile.this.finish();
                }
            });
            ab.setNegativeButton("no", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });


            ab.show();
        }
    }

    public void recordScreen()
    {

        // [START image_view_event]
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "1");
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Login");
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }

    class  getLogin extends AsyncTask<String, String, JSONObject> {
        String type;
        ProgressDialog mDialog;
        getLogin(String type){

            this.type=type;
        }



        @Override
        protected JSONObject doInBackground(String... params) {
            // TODO Auto-generated method stub
            //   String url = "http://fortune4tech.in/fast5/frontend/www/api/login";
            //   String url = "http://120.88.39.187/peep/code/frontend/public/v1/login";


            String url = Constants.Base+Constants.Login;

            LoggerGeneral.info("Url data Cpture"+url);

            JSONObject object = new JSONObject();
            try {

                object.put("email",emailtxt);
                object.put("password",passwordtxt);
                object.put("device_id",android_id);
                object.put("device_type","1");
                object.put("device_token",appPreferences.getString("Token",""));


                LoggerGeneral.info("JsonObjectPrint" + object.toString());

            } catch (Exception ex) {

            }

            String str = '"' + appPreferences.getString("jwt", "") + '"';
            JSONObject jsonObject  = ServiceFacade.getResponsJsonParams(url, object);

            Log.d("hi", "getresponse" + jsonObject);

            Log.d("hi", "getresponse" + jsonObject);

            return jsonObject;

        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

            mDialog = new ProgressDialog(Login.this,ProgressDialog.THEME_HOLO_DARK);
            mDialog.setMessage("Processing...");
            mDialog.show();
            mDialog.setCancelable(false);
            mDialog.setCanceledOnTouchOutside(false);
        }

        @Override
        protected void onPostExecute(JSONObject results) {
            // TODO Auto-generated method stub
            super.onPostExecute(results);
            Log.d("hi", "getresults" + results);
            mDialog.dismiss();



            if(results!=null){

                try {
                    JSONObject jsonObject =results.getJSONObject("meta");


                    int  status_code  = jsonObject.getInt("status_code");


                    if(status_code==0){


                        JSONObject data =results.getJSONObject("data");

                        Common.showToast(context,"You have been successfully logged in");

                        String userid    =data.getString("user_id");
                        String user_name =data.getString("user_name");
                        String email_id  =data.getString("email_id");


                        appPreferences.putString("user_id",userid);
                        appPreferences.putString("user_name",user_name);
                        appPreferences.putString("email_id",email_id);
                        appPreferences.remove("storeimagecoverres");
                        appPreferences.remove("storeimageres");
                        appPreferences.remove("profile_image_url");


                        Intent register   = new Intent(Login.this,Profile.class);
                        register.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(register);
                        finish();
                    }

                    if(status_code==1){


                        Common.showToast(context,"Invalid login credentials");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

          }

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
