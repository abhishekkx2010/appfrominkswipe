package com.inkswipe.SocialSociety;

import android.app.Activity;
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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import adapter.CustomAdapter;
import adapter.NotificationAdapter;
import adapter.PropertyAdapter;
import model.DrawerList;
import model.NotificationModel;
import util.AppPreferences;
import util.Common;
import util.Constants;
import util.ExceptionHandler;
import util.LoggerGeneral;
import util.ServiceFacade;

public class NotificationApp extends AppCompatActivity {

    LinearLayout home;
    Fragment fragment = null;
    TextView appname;

    RecyclerView recyclerView;
    List<NotificationModel> notificationModels;
    NotificationAdapter adapter;
    Context context;
    AppPreferences appPreferences;

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    String SocietyId;

    String id;
    String society_name;
    String user_idd;
    String ssociety_d;
    String message;
    String event_type;
    String event_id;
    String created_on;
    String date;
   // String time;
    String user_name;
    String notification_message;
    TextView nonotify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        setContentView(R.layout.activity_notification);
        context=NotificationApp.this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView title=((TextView) toolbar.findViewById(R.id.toolbar_title));
        title.setText("Notifications");
        toolbar.setTitleTextColor(android.graphics.Color.WHITE);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

     //   toolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.backarrow));

        toolbar.getNavigationIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //What to do on back clicked
                Intent intent = new Intent(NotificationApp.this, Profile.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra("SocietyId",SocietyId);
                startActivity(intent);
                finish();
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.my_statusbar_color));
        }

        appPreferences = AppPreferences.getAppPreferences(this);


        SocietyId = getIntent().getStringExtra("SocietyId");
        LoggerGeneral.info("nitificationSocietyId"+SocietyId);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        LayoutInflater inflater = getLayoutInflater();
        View listHeaderView = inflater.inflate(R.layout.header_list, null, false);

        LinearLayout navHome= (LinearLayout) listHeaderView.findViewById(R.id.Navhome);

        navHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myProperty=new Intent(NotificationApp.this,MyProperty.class);
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
                        Intent profile = new Intent(NotificationApp.this, Profile.class);
                        profile.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        profile.putExtra("SocietyId", SocietyId);
                        startActivity(profile);
                        finish();
                        //   mDrawerLayout.closeDrawers();
                        break;
                    case 2:
                        LoggerGeneral.info("2");
                        Intent addSociety = new Intent(NotificationApp.this, AddSociety.class);
                        addSociety.putExtra("SocietyId", SocietyId);
                        startActivity(addSociety);
                        finish();
                        break;
                    case 3:
                        LoggerGeneral.info("3");
                        if (appPreferences.getInt("property_count", 0) > 0) {
                            Intent myProperty = new Intent(NotificationApp.this, MyProperty.class);
                            myProperty.putExtra("SocietyId", SocietyId);
                            PropertyAdapter.intentCheck = 0;
                            startActivity(myProperty);
                            finish();
                        } else {
                            Common.showToast(context, "Please add minimum 1 property");
                            mDrawerLayout.closeDrawers();
                        }
                        break;
                    case 4:
                        LoggerGeneral.info("4");
                        if (appPreferences.getInt("property_count", 0) > 0) {
                            Intent events = new Intent(NotificationApp.this, MyProperty.class);
                            events.putExtra("SocietyId", SocietyId);
                            Common.showToast(context, "Please select property first");
                            PropertyAdapter.intentCheck = 4;
                            events.putExtra("event", 3);
                            startActivity(events);
                            finish();
                        } else {
                            Common.showToast(context, "Please add minimum 1 property");
                            mDrawerLayout.closeDrawers();
                        }
                        break;
                    case 5:
                        LoggerGeneral.info("5");
                        if (appPreferences.getInt("property_count", 0) > 0) {
                            Intent createEvents = new Intent(NotificationApp.this, MyProperty.class);
                            createEvents.putExtra("SocietyId", SocietyId);
                            Common.showToast(context, "Please select property first");
                            PropertyAdapter.intentCheck = 5;
                            startActivity(createEvents);
                            finish();
                        } else {
                            Common.showToast(context, "Please add minimum 1 property");
                            mDrawerLayout.closeDrawers();
                        }
                        break;
                    case 6:
                        LoggerGeneral.info("6");
                        if (appPreferences.getInt("property_count", 0) > 0) {
                            Intent archivedEvents = new Intent(NotificationApp.this, MyProperty.class);
                            archivedEvents.putExtra("SocietyId", SocietyId);
                            Common.showToast(context, "Please select property first");
                            PropertyAdapter.intentCheck = 6;
                            startActivity(archivedEvents);
                            finish();
                        } else {
                            Common.showToast(context, "Please add minimum 1 property");
                            mDrawerLayout.closeDrawers();
                        }
                        break;
                    case 7:
                        LoggerGeneral.info("7");
                        if (appPreferences.getInt("property_count", 0) > 0) {
                            Intent announcement = new Intent(NotificationApp.this, MyProperty.class);
                            announcement.putExtra("SocietyId", SocietyId);
                            Common.showToast(context, "Please select property first");
                            PropertyAdapter.intentCheck = 7;
                            announcement.putExtra("announcement", 4);
                            startActivity(announcement);
                            finish();
                        } else {
                            Common.showToast(context, "Please add minimum 1 property");
                            mDrawerLayout.closeDrawers();
                        }
                        break;
                    case 8:
                        LoggerGeneral.info("8");
                        if (appPreferences.getInt("property_count", 0) > 0) {
                            Intent createAnnouncement = new Intent(NotificationApp.this, MyProperty.class);
                            createAnnouncement.putExtra("SocietyId", SocietyId);
                            Common.showToast(context, "Please select property first");
                            PropertyAdapter.intentCheck = 8;
                            startActivity(createAnnouncement);
                            finish();
                        } else {
                            Common.showToast(context, "Please add minimum 1 property");
                            mDrawerLayout.closeDrawers();
                        }
                        break;
                    case 9:
                        LoggerGeneral.info("9");
                        if (appPreferences.getInt("property_count", 0) > 0) {
                            Intent polls = new Intent(NotificationApp.this, MyProperty.class);
                            Common.showToast(context, "Please select property first");
                            polls.putExtra("SocietyId", SocietyId);
                            PropertyAdapter.intentCheck = 9;
                            polls.putExtra("polls", 2);
                            startActivity(polls);
                            finish();
                        } else {
                            Common.showToast(context, "Please add minimum 1 property");
                            mDrawerLayout.closeDrawers();
                        }
                        break;
                    case 10:
                        LoggerGeneral.info("10");
                        if (appPreferences.getInt("property_count", 0) > 0) {
                            Intent createEventPoll = new Intent(NotificationApp.this, MyProperty.class);
                            Common.showToast(context, "Please select property first");
                            createEventPoll.putExtra("SocietyId", SocietyId);
                            PropertyAdapter.intentCheck = 10;
                            startActivity(createEventPoll);
                            finish();
                        } else {
                            Common.showToast(context, "Please add minimum 1 property");
                            mDrawerLayout.closeDrawers();
                        }
                        break;
                    case 11:
                        LoggerGeneral.info("11");
                        if (appPreferences.getInt("property_count", 0) > 0) {
                            Intent archivedEventPoll = new Intent(NotificationApp.this, MyProperty.class);
                            Common.showToast(context, "Please select property first");
                            archivedEventPoll.putExtra("SocietyId", SocietyId);
                            PropertyAdapter.intentCheck = 11;
                            startActivity(archivedEventPoll);
                            finish();
                        } else {
                            Common.showToast(context, "Please add minimum 1 property");
                            mDrawerLayout.closeDrawers();
                        }
                        break;
                    case 12:
                        LoggerGeneral.info("12");
                        if (appPreferences.getInt("property_count", 0) > 0) {
                            Intent members = new Intent(NotificationApp.this, MyProperty.class);
                            Common.showToast(context, "Please select property first");
                            members.putExtra("SocietyId", SocietyId);
                            PropertyAdapter.intentCheck = 12;
                            startActivity(members);
                            finish();
                        } else {
                            Common.showToast(context, "Please add minimum 1 property");
                            mDrawerLayout.closeDrawers();
                        }
                        break;
                    case 13:
                        LoggerGeneral.info("13");
                        if (appPreferences.getInt("property_count", 0) > 0) {
                            Intent offers = new Intent(NotificationApp.this, MyProperty.class);
                            Common.showToast(context, "Please select property first");
                            PropertyAdapter.intentCheck = 13;
                            offers.putExtra("SocietyId", SocietyId);
                            startActivity(offers);
                            finish();
                        } else {
                            Common.showToast(context, "Please add minimum 1 property");
                            mDrawerLayout.closeDrawers();
                        }
                        break;
                    case 14:
                        LoggerGeneral.info("14");
                        if (appPreferences.getInt("property_count", 0) > 0) {
                            Intent complaint = new Intent(NotificationApp.this, MyProperty.class);
                            Common.showToast(context, "Please select property first");
                            PropertyAdapter.intentCheck = 14;
                            complaint.putExtra("SocietyId", SocietyId);
                            startActivity(complaint);
                            finish();
                        } else {
                            Common.showToast(context, "Please add minimum 1 property");
                            mDrawerLayout.closeDrawers();
                        }
                        break;
                    case 15:
                        LoggerGeneral.info("15");
                        /*Intent notification=new Intent(NotificationApp.this,NotificationApp.class);
                        startActivity(notification);
                        finish();*/
                        mDrawerLayout.closeDrawers();
                        break;
                    case 16:

                      /*  Intent intent = new Intent(NotificationApp.this, Login.class);
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


        nonotify = (TextView)findViewById(R.id.nonotify);

        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        /*notificationModels= new ArrayList<NotificationModel>();
        for(int i=0;i<=5;i++)
        {
            NotificationModel notificationModel=new NotificationModel();
            notificationModel.setUserName("Nitin Chorge");
            notificationModel.setDate("22 Nov,2016");
            notificationModel.setTime("8:00 pm");
            notificationModel.setDescription("publish a new Event");
            notificationModels.add(notificationModel);
        }
        LoggerGeneral.info("List Size" + notificationModels.size());
        adapter=new NotificationAdapter(context,notificationModels);
        recyclerView.setAdapter(adapter);
*/

        notificationModels= new ArrayList<NotificationModel>();

        if(Common.isOnline(context)){


            new getNotifications().execute();
        }
        else {
            Common.showToast(context,"No internet connection");
        }

       /* if(tdefine.equalsIgnoreCase("Owner") || tdefine.equalsIgnoreCase("Sub Owner")){
            availtxt.setVisibility(View.VISIBLE);
            availradio.setVisibility(View.VISIBLE);
            chooseDate.setVisibility(View.VISIBLE);

        }
        else {
            availradio.setVisibility(View.GONE);
            availtxt.setVisibility(View.GONE);
            chooseDate.setVisibility(View.INVISIBLE);
        }*/


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
            Intent intent = new Intent(NotificationApp.this, Profile.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.putExtra("SocietyId",SocietyId);
            startActivity(intent);
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
                object.put("user_id",appPreferences.getString("user_id",""));

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

            mDialog = new ProgressDialog(NotificationApp.this,ProgressDialog.THEME_HOLO_DARK);
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
                        Intent register   = new Intent(NotificationApp.this,Login.class);
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


    class  getNotifications extends AsyncTask<String, String, JSONObject> {
        String type;
        ProgressDialog mDialog;
        @Override
        protected JSONObject doInBackground(String... params) {
            // TODO Auto-generated method stub

            String url= Constants.Base+Constants.getNotification;


         //   String url ="http://192.168.10.201/social-society/poonam/get-user-notification";
            JSONObject object = new JSONObject();
            try {

              //  object.put("society_id",SocietyId);
              //  object.put("user_id",appPreferences.getString("user_id",""));

                //object.put("user_id","18");
                object.put("user_id",appPreferences.getString("user_id",""));
                LoggerGeneral.info("JsonObjectPrintNotification" + object.toString());

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

            mDialog = new ProgressDialog(context,ProgressDialog.THEME_HOLO_DARK);
            mDialog.setMessage("Processing...");
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


                    appPreferences.putString("eventResponse",results.toString());
                    JSONObject meta = results.getJSONObject("meta");

                    int status_code = meta.getInt("status_code");

                    int account_status = 1;
                    if(meta.has("account_status")){
                        if(!meta.isNull("account_status")){
                            account_status = meta.getInt("account_status");
                        }
                    }

                    if(account_status==1){
                        if (status_code == 0) {


                            JSONArray jsonArray = results.getJSONArray("data");



                            for(int i=0;i<=jsonArray.length()-1;i++) {

                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                id = jsonObject.getString("id");
                                if(jsonObject.has("name")) {
                                    if (!jsonObject.isNull("name")) {


                                           society_name = jsonObject.getString("name");
                                    }
                                }
                                else {
                                    society_name="";
                                }
                                society_name = jsonObject.getString("name");
                                notification_message = jsonObject.getString("notification_message");
                                user_idd              = jsonObject.getString("user_id");

                                ssociety_d            = jsonObject.getString("society_id");
                                message               = jsonObject.getString("message");
                                event_type            = jsonObject.getString("event_type");
                                event_id              = jsonObject.getString("event_id");


                                created_on           = jsonObject.getString("created_on");
                                date                 = jsonObject.getString("date");
                             //   time                 = jsonObject.getString("time");
                                user_name            = jsonObject.getString("user_name")+(",");





                                NotificationModel notificationModel=new NotificationModel();


                                notificationModel.setId(id);
                                notificationModel.setSocietyName(society_name);
                                notificationModel.setNotification_message(notification_message);
                                notificationModel.setUser_idd(user_idd);
                                notificationModel.setSsociety_d(ssociety_d);
                                notificationModel.setMessage(message);
                                notificationModel.setCreated_on(created_on);
                                notificationModel.setNdate(date);
                               // notificationModel.setNtime(time);
                                notificationModel.setEvent_type(event_type);
                                notificationModel.setEvent_id(event_id);
                                notificationModel.setUser_name(user_name);


                                notificationModels.add(notificationModel);




                                if(notificationModels!=null ||notificationModels.size()>0) {

                                    adapter = new NotificationAdapter(context, notificationModels,SocietyId);

                                    recyclerView.setAdapter(adapter);

                                    adapter.notifyDataSetChanged();

                                }
                                else {

                                    nonotify.setVisibility(View.VISIBLE);
                                }


                            }

                        }

                        if(status_code==1){


                            nonotify.setVisibility(View.VISIBLE);
                        }

                    }

                    if(account_status == 0) {
                        Intent intent = new Intent(context,Login.class);
                        appPreferences.remove("user_id");
                        appPreferences.remove("user_name");
                        appPreferences.remove("email_id");
                        appPreferences.remove("storecoverimage");
                        appPreferences.remove("storeimage");
                        appPreferences.remove("profile_image_url");
                        startActivity(intent);
                        ((Activity)context).finish();
                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mDialog.dismiss();


            }
        }

    }



}
