package com.inkswipe.SocialSociety;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import adapter.CustomAdapter;
import adapter.PropertyAdapter;
import model.DrawerList;
import util.AppPreferences;
import util.Common;
import util.Constants;
import util.ExceptionHandler;
import util.LoggerGeneral;
import util.ServiceFacade;

public class Complaint extends AppCompatActivity {

    LinearLayout home;
    Fragment fragment = null;
    TextView appname;

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;

    EditText complaintSubject,complaintDescription;

    String subject,description;

    RelativeLayout notification;
    LinearLayout createComplaint;

    Context context;

    AppPreferences appPreferences;

    String societyId;

    LinearLayout complaintDescriptionlin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        setContentView(R.layout.activity_complaint);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        context=Complaint.this;
        appPreferences=AppPreferences.getAppPreferences(context);
        societyId=getIntent().getStringExtra("SocietyId");
        TextView title=((TextView) toolbar.findViewById(R.id.toolbar_title));
        title.setText("Complaint");
        toolbar.setTitleTextColor(android.graphics.Color.WHITE);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

   //  toolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.backarrow));


        complaintDescriptionlin = (LinearLayout)findViewById(R.id.complaintDescriptionlin);



        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //What to do on back clicked
                if(mDrawerLayout.isDrawerOpen(GravityCompat.END))
                {
                    mDrawerLayout.closeDrawers();
                    return;
                }
                else {
                    
                    Intent back = new Intent(Complaint.this, DashBoard.class);
                    back.putExtra("SocietyId",societyId);
                    startActivity(back);
                    finish();
                }
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.my_statusbar_color));
        }

        toolbar.getNavigationIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);

        notification= (RelativeLayout) findViewById(R.id.notification);

        RelativeLayout notificationcount= (RelativeLayout) findViewById(R.id.notificationcount);

        TextView notificationtext= (TextView) findViewById(R.id.notificationtext);

        if(Constants.notififcationcount>0) {
            notificationcount.setVisibility(View.VISIBLE);
            notificationtext.setText(String.valueOf(Constants.notififcationcount));
        }
        else {
            notificationcount.setVisibility(View.GONE);
        }

        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent notification=new Intent(Complaint.this,NotificationApp.class);
                //    notification.putExtra("SocietyId",Soci)
                int test1=appPreferences.getInt("NotificationOld",0);
                test1=test1+Constants.notififcationcount;
                appPreferences.putInt("NotificationOld",test1);
                notification.putExtra("SocietyId", societyId);
                Constants.notififcationcount=0;
                startActivity(notification);
                finish();
            }
        });

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        LayoutInflater inflater = getLayoutInflater();
        View listHeaderView = inflater.inflate(R.layout.header_list, null, false);

        LinearLayout navHome= (LinearLayout) listHeaderView.findViewById(R.id.Navhome);

        navHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myProperty=new Intent(Complaint.this,MyProperty.class);
                PropertyAdapter.intentCheck=0;
                startActivity(myProperty);
                finish();
            }
        });

        home = (LinearLayout)findViewById(R.id.home);
        home.setOnClickListener(homeOnclickListener);

        mDrawerList.addHeaderView(listHeaderView);

        DrawerList list=new DrawerList();

        List<ItemObject> listViewItems = list.drawer();


        mDrawerList.setAdapter(new CustomAdapter(this, listViewItems));


        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // make ToastCustom when click
                /*ToastCustom.makeText(getApplicationContext(), "Position " + position, ToastCustom.LENGTH_LONG).show();
                */

                switch (position) {

                    case 1:
                        LoggerGeneral.info("1");
                        Intent profile = new Intent(Complaint.this, Profile.class);
                        profile.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(profile);
                        finish();
                        break;
                    case 2:
                        LoggerGeneral.info("2");
                        Intent addSociety = new Intent(Complaint.this, AddSociety.class);
                        startActivity(addSociety);
                        finish();
                        break;
                    case 3:
                        LoggerGeneral.info("3");
                        Intent myProperty = new Intent(Complaint.this, MyProperty.class);
                        PropertyAdapter.intentCheck = 0;
                        startActivity(myProperty);
                        finish();
                        break;
                    case 4:
                        LoggerGeneral.info("4");
                        Intent events = new Intent(Complaint.this, DashBoard.class);
                        events.putExtra("SocietyId", societyId);
                        events.putExtra("event", 3);
                        startActivity(events);
                        finish();
                        break;
                    case 5:
                        LoggerGeneral.info("5");
                        Intent createEvents = new Intent(Complaint.this, CreateEvent.class);
                        createEvents.putExtra("SocietyId", societyId);
                        startActivity(createEvents);
                        finish();
                        break;
                    case 6:
                        LoggerGeneral.info("6");
                        Intent archivedEvents = new Intent(Complaint.this, Archivedevent.class);
                        archivedEvents.putExtra("SocietyId", societyId);
                        startActivity(archivedEvents);
                        finish();
                        break;
                    case 7:
                        LoggerGeneral.info("7");
                        Intent announcement = new Intent(Complaint.this, DashBoard.class);
                        announcement.putExtra("SocietyId", societyId);
                        announcement.putExtra("announcement", 4);
                        startActivity(announcement);
                        finish();
                        break;
                    case 8:
                        LoggerGeneral.info("8");
                        Intent createAnnouncement = new Intent(Complaint.this, CreateAnnouncement.class);
                        createAnnouncement.putExtra("SocietyId", societyId);
                        startActivity(createAnnouncement);
                        finish();
                        break;
                    case 9:
                        LoggerGeneral.info("9");
                        Intent polls = new Intent(Complaint.this, DashBoard.class);
                        polls.putExtra("SocietyId", societyId);
                        polls.putExtra("polls", 2);
                        startActivity(polls);
                        finish();
                        break;
                    case 10:
                        LoggerGeneral.info("10");
                        Intent createEventPoll = new Intent(Complaint.this, CreatePoll.class);
                        createEventPoll.putExtra("SocietyId", societyId);
                        startActivity(createEventPoll);
                        finish();
                        break;
                    case 11:
                        LoggerGeneral.info("11");
                        Intent archivedEventPoll = new Intent(Complaint.this, ArchivedPolls.class);
                        archivedEventPoll.putExtra("SocietyId", societyId);
                        startActivity(archivedEventPoll);
                        finish();
                        break;
                    case 12:
                        LoggerGeneral.info("12");
                        Intent members = new Intent(Complaint.this, Members.class);
                        members.putExtra("SocietyId", societyId);
                        startActivity(members);
                        finish();
                        break;
                    case 13:
                        LoggerGeneral.info("13");
                        Intent offers = new Intent(Complaint.this, Offer.class);
                        offers.putExtra("SocietyId", societyId);
                        startActivity(offers);
                        finish();
                        break;
                    case 14:
                        LoggerGeneral.info("14");
                        mDrawerLayout.closeDrawers();
                        /*Intent complaint=new Intent(Complaint.this,Complaint.class);
                        startActivity(complaint);
                        finish();*/
                        break;
                    case 15:
                        LoggerGeneral.info("15");
                        Intent notification=new Intent(Complaint.this,NotificationApp.class);
                        //    notification.putExtra("SocietyId",Soci)
                        int test1=appPreferences.getInt("NotificationOld",0);
                        test1=test1+Constants.notififcationcount;
                        appPreferences.putInt("NotificationOld",test1);
                        notification.putExtra("SocietyId", societyId);
                        Constants.notififcationcount=0;
                        startActivity(notification);
                        finish();
                        break;
                    case 16:
                       /* LoggerGeneral.info("16");
                        Intent intent = new Intent(Complaint.this, Login.class);
                        appPreferences.remove("user_id");
                        appPreferences.remove("user_name");
                        appPreferences.remove("email_id");
                        appPreferences.remove("storecoverimage");
                        appPreferences.remove("storeimage");
                        appPreferences.remove("profile_image_url");
                        startActivity(intent);
                        finish();*/

                        if(Common.isOnline(context)) {

                            new getLogout().execute();

                        }
                        else
                        {
                            Common.showToast(context,"No internet connection");
                        }

                        break;
                }
            }
        });

        createComplaint= (LinearLayout) findViewById(R.id.complaint);

        complaintSubject= (EditText) findViewById(R.id.complaintSubject);

        complaintDescription= (EditText) findViewById(R.id.complaintDescription);



        complaintDescriptionlin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                complaintDescription.requestFocus();

                final InputMethodManager inputMethodManager =
                        (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);

                inputMethodManager.showSoftInput(complaintDescription, InputMethodManager.SHOW_FORCED);
            }
        });


        createComplaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                subject=complaintSubject.getText().toString();

                description=complaintDescription.getText().toString();

                if(subject!=null && subject.length()>0)
                {
                    if(description!=null && description.length()>0) {

                        if(Common.isOnline(context)){
                            new addComplaint().execute();

                        }
                        else {
                            Common.showToast(context, "No internet connection");
                        }

                    }
                    else
                    {
                        Common.showToast(context, "Enter description");
                    }

                }
                else
                {
                    Common.showToast(context, "Enter subject");
                }
            }
        });

    }


    View.OnClickListener homeOnclickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(mDrawerLayout.isDrawerOpen(GravityCompat.END)){
                mDrawerLayout.closeDrawers();
            }else{
                InputMethodManager inputMethodManager3 = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager3.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                mDrawerLayout.openDrawer(GravityCompat.END);
            }
        }
    };

    @Override
    public void onBackPressed() {
        if(mDrawerLayout.isDrawerOpen(GravityCompat.END))
        {
            mDrawerLayout.closeDrawers();
            return;
        }
        else {

            super.onBackPressed();
            Intent back = new Intent(Complaint.this, DashBoard.class);
            back.putExtra("SocietyId",societyId);
            startActivity(back);
            finish();
        }
    }


    public  class  getLogout extends AsyncTask<String, String, JSONObject> {
        String type;
        ProgressDialog mDialog;


        @Override
        protected JSONObject doInBackground(String... params) {
            // TODO Auto-generated method stub
            //   String url = "http://fortune4tech.in/fast5/frontend/www/api/login";
            //   String url = "http://120.88.39.187/peep/code/frontend/public/v1/login";


            String url = Constants.Base+Constants.Logout;

            LoggerGeneral.info("Url data Cpture"+url);

            JSONObject object = new JSONObject();
            try {

                //   object.put("user_id",appPreferences.getString("user_id",""));
                object.put("user_id",appPreferences.getString("user_id",appPreferences.getString("user_id","")));

                LoggerGeneral.info("JsonObjectPrint" + object.toString());


                LoggerGeneral.info("JsonObjectPrintLogout" + object.toString());

            } catch (Exception ex) {

            }

            //   String str = '"' + appPreferences.getString("jwt", "") + '"';
            JSONObject jsonObject  = ServiceFacade.getResponsJsonParams(url, object);

            Log.d("hi", "getresponse" + jsonObject);

            Log.d("hi", "getresponse" + jsonObject);

            return jsonObject;

        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

            mDialog = new ProgressDialog(Complaint.this,ProgressDialog.THEME_HOLO_DARK);
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


                        //  JSONObject data =results.getJSONObject("data");
                        appPreferences.remove("user_id");
                        appPreferences.remove("user_name");
                        appPreferences.remove("email_id");
                        appPreferences.remove("property_count");
                        appPreferences.remove("storecoverimage");
                        appPreferences.remove("storeimage");
                        appPreferences.remove("profile_image_url");

                        Common.showToast(context,"You have successfully logged out!");
                        Intent register   = new Intent(Complaint.this,Login.class);
                        register.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(register);

                        finish();

                    }

                    if(status_code==1){


                        Common.showToast(context,"Logout failed");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

        }

    }

    class  addComplaint extends AsyncTask<String, String, JSONObject> {
        String type;
        ProgressDialog mDialog;




        @Override
        protected JSONObject doInBackground(String... params) {
            // TODO Auto-generated method stub
            //   String url = "http://fortune4tech.in/fast5/frontend/www/api/login";
            //   String url = "http://120.88.39.187/peep/code/frontend/public/v1/login";


            String url = Constants.Base+Constants.CreateComplain;


            JSONObject object = new JSONObject();
            try {

                object.put("society_id",societyId);
                object.put("subject",subject);
                object.put("description",description);
                object.put("user_id",appPreferences.getString("user_id",""));



                LoggerGeneral.info("JsonObjectPrint" + object.toString());

            } catch (Exception ex) {

            }

            //String str = '"' + appPreferences.getString("jwt", "") + '"';
            JSONObject jsonObject = ServiceFacade.getResponsJsonParams(url, object);

            Log.d("hi", "getresponse" + jsonObject);

            Log.d("hi", "getresponse" + jsonObject);

            return jsonObject;

        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

            mDialog = new ProgressDialog(Complaint.this, ProgressDialog.THEME_HOLO_DARK);
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
                    JSONObject jsonObject = results.getJSONObject("meta");


                    int status_code = jsonObject.getInt("status_code");

                    int account_status = jsonObject.getInt("account_status");

                    String message = jsonObject.getString("message");


                    if (account_status == 1){


                        if (status_code == 0) {


                            //JSONObject data = results.getJSONObject("data");



                            Intent dashboard=new Intent(Complaint.this,DashBoard.class);
                            dashboard.putExtra("SocietyId",societyId);
                            startActivity(dashboard);
                            finish();




                        /*Intent register   = new Intent(AddSociety.this,Profile.class);
                        startActivity(register);
                        finish();*/
                        }

                        if (status_code == 1) {


                            Common.showToast(context, "Invalid  Credentials");
                        }

                    }


                    if(account_status==0){
                        Intent intent = new Intent(Complaint.this,Login.class);
                        appPreferences.remove("user_id");
                        appPreferences.remove("user_name");
                        appPreferences.remove("email_id");
                        appPreferences.remove("storecoverimage");
                        appPreferences.remove("storeimage");
                        appPreferences.remove("profile_image_url");
                        startActivity(intent);
                        finish();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

        }

    }




}
