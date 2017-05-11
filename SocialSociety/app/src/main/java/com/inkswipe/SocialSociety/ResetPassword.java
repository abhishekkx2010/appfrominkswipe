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
import android.text.InputFilter;
import android.text.Spanned;
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

public class ResetPassword extends AppCompatActivity {

    LinearLayout login;
    EditText otp,password,confPassword;
    Context context;
    String emailtxt;
    AppPreferences appPreferences;
    String u_otp,u_pass,u_conpass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        setContentView(R.layout.activity_reset_password);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context=ResetPassword.this;

        appPreferences = AppPreferences.getAppPreferences(context);

        emailtxt = getIntent().getStringExtra("emailtxt");

        TextView title=((TextView) toolbar.findViewById(R.id.toolbar_title));
        title.setText("Reset Password");
        toolbar.setTitleTextColor(android.graphics.Color.WHITE);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

      //  toolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.backarrow));
        toolbar.getNavigationIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //What to do on back clicked
                Intent intent = new Intent(ResetPassword.this, ForgetPassword.class);
                startActivity(intent);
                finish();
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.my_statusbar_color));
        }

        otp= (EditText) findViewById(R.id.otp);
        password= (EditText) findViewById(R.id.password);
        confPassword= (EditText) findViewById(R.id.confpassword);

        otp.setTypeface(Common.font(context, "arial.ttf"));
        password.setTypeface(Common.font(context, "arial.ttf"));
        confPassword.setTypeface(Common.font(context, "arial.ttf"));

        /*InputFilter filter = new InputFilter() {
            boolean canEnterSpace = false;

            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {

                if(password.getText().toString().equals(""))
                {
                    canEnterSpace = false;
                }

                StringBuilder builder = new StringBuilder();

                for (int i = start; i < end; i++) {
                    char currentChar = source.charAt(i);

                    if (Character.isLetterOrDigit(currentChar) || currentChar == '_') {
                        builder.append(currentChar);
                        canEnterSpace = true;
                    }

                    if(Character.isWhitespace(currentChar) && canEnterSpace) {
                        Common.showToast(context,"Password should not contain spaces");
                        builder.append("");
                    }


                }
                return builder.toString();
            }

        };*/


      //  password.setFilters(new InputFilter[]{filter});


        login =(LinearLayout)findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                u_otp = otp.getText().toString().trim();
                u_pass = password.getText().toString().trim();
                u_conpass = confPassword.getText().toString().trim();


                if(Common.isOnline(context)) {

                    if(u_otp!=null && u_otp.length()>0) {

                        if(u_pass!=null && u_pass.length()>0) {
                            if(u_pass.length()>7) {

                                if(u_conpass!=null && u_conpass.length()>0) {

                                    if(u_pass.equals(u_conpass)) {
                                        new resetPassword().execute();
                                    }
                                    else {
                                        Common.showToast(context,"Password does not match the confirm password");
                                    }
                                }
                                else {
                                    Common.showToast(context,"Enter confirm password");
                                }
                            }
                            else {
                                Common.showToast(context,"Enter password of minimum 8 characters");
                            }
                        }
                        else {
                            Common.showToast(context,"Enter password");
                        }
                    }
                    else {
                        Common.showToast(context,"Enter Otp");
                    }


                }
                else {
                    Common.showToast(context,"No internet connection");
                }

             /*   Intent login = new Intent(ResetPassword.this,Login.class);
                startActivity(login);
                finish();*/
            }
        });

    }
    class  resetPassword extends AsyncTask<String, String, JSONObject> {
        String type;
        ProgressDialog mDialog;
        @Override
        protected JSONObject doInBackground(String... params) {
            // TODO Auto-generated method stub

            String url= Constants.Base+Constants.Resetpassword;

            JSONObject object = new JSONObject();
            try {

                object.put("email",emailtxt);
                object.put("password",u_pass);
                object.put("confirm_password",u_conpass);
                object.put("otp",u_otp);

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

            mDialog = new ProgressDialog(ResetPassword.this,ProgressDialog.THEME_HOLO_DARK);
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


               //     String message = meta.getString("message");
                    int status_code = meta.getInt("status_code");

                 //   if(message.equalsIgnoreCase("Password has been Reset..")){

                    if(status_code==0){
                        Common.showToast(context,"Password reset successfully");
                        Intent reset = new Intent(ResetPassword.this,Login.class);

                        startActivity(reset);
                        finish();

                    }

                    if(status_code==1){

                        Common.showToast(context,"Enter valid OTP");

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
