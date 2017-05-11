package com.inkswipe.SocialSociety;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flurry.android.FlurryAgent;

import org.json.JSONException;
import org.json.JSONObject;

import util.AppPreferences;
import util.Common;
import util.Constants;
import util.ExceptionHandler;
import util.LoggerGeneral;
import util.ServiceFacade;

public class ForgetPassword extends AppCompatActivity {
    LinearLayout login,resetpass;
    AppPreferences appPreferences;
    Context context;
    String emailtxt;
    EditText email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        setContentView(R.layout.activity_forget_password);

        context = ForgetPassword.this;
        appPreferences = AppPreferences.getAppPreferences(context);

        email = (EditText)findViewById(R.id.email);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView title=((TextView) toolbar.findViewById(R.id.toolbar_title));
        title.setText("Forgot Password");
        toolbar.setTitleTextColor(android.graphics.Color.WHITE);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

    //    toolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.backarrow));
        toolbar.getNavigationIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //What to do on back clicked
                Intent intent = new Intent(ForgetPassword.this, Login.class);
                startActivity(intent);
                finish();
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.my_statusbar_color));
        }

        login= (LinearLayout) findViewById(R.id.loginButton);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent login = new Intent(ForgetPassword.this,Login.class);
                startActivity(login);
                finish();
            }
        });
        resetpass= (LinearLayout) findViewById(R.id.resetpass);
        resetpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                emailtxt  = email.getText().toString().trim();


                if(Common.isOnline(context)) {

                    if(emailtxt!=null && emailtxt.length()>0) {

                        if(Common.emailValidator(emailtxt)) {
                            new forgetpassword().execute();
                        }
                        else {
                            Common.showToast(context,"Enter a valid email id");
                        }
                    }
                    else {
                        Common.showToast(context,"Enter email id");
                    }
                }
                else {
                    Common.showToast(context,"No internet connection");
                }

             /*   Intent login = new Intent(ForgetPassword.this,ResetPassword.class);
                startActivity(login);
                finish();*/
            }
        });





    }

    class  forgetpassword extends AsyncTask<String, String, JSONObject> {
        String type;
        ProgressDialog mDialog;
        @Override
        protected JSONObject doInBackground(String... params) {
            // TODO Auto-generated method stub

            String url= Constants.Base+Constants.ForgetPassword;

            JSONObject object = new JSONObject();
            try {

                object.put("email",emailtxt);

                LoggerGeneral.info("JsonObjectPrint" + object.toString());

            } catch (Exception ex) {

            }

            String str = '"' + appPreferences.getString("jwt", "") + '"';
            JSONObject jsonObject  = ServiceFacade.getResponsJsonParams(url, object);

            Log.d("hi", "getresponse" + jsonObject);

            Log.d("hi", "getresponse" + jsonObject);

            if (jsonObject != null) {
                if (jsonObject.has("Data")) {
                    try {
                        Log.d("hi", "getresponse11" + jsonObject);

                        String response = jsonObject.toString();
                        Log.d("hi", "getresponse22" + response);

                    } catch (Exception e) {
                    }
                }
            }
            return jsonObject;
        }
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

            mDialog = new ProgressDialog(ForgetPassword.this,ProgressDialog.THEME_HOLO_DARK);
            mDialog.setMessage("Please wait...");
            mDialog.show();
            mDialog.setCancelable(false);
            mDialog.setCanceledOnTouchOutside(false);
        }

        @Override
        protected void onPostExecute(JSONObject results) {
            // TODO Auto-generated method stub
            super.onPostExecute(results);
            Log.d("hi", "getresultsstate" + results);
            //mDialog.dismiss();

            if(results!=null){

                try {

                    JSONObject meta = results.getJSONObject("meta");


//                    String message = meta.getString("message");

                    int status_code = meta.getInt("status_code");


//                    if(message.equalsIgnoreCase("Email has been sent to your email id")){

                    if(status_code==0){

                        Intent forget = new Intent(ForgetPassword.this,ResetPassword.class);
                        forget.putExtra("emailtxt",emailtxt);
                        startActivity(forget);
                        finish();

                    }
                    else
                    {
                        Common.showToast(context,"Email id not register");
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mDialog.dismiss();


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
