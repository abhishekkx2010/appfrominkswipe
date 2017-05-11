package com.inkswipe.SocialSociety;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import android.text.Editable;
import android.text.TextWatcher;
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

import com.flurry.android.FlurryAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import adapter.CustomAdapter;
import adapter.EventListAdapter;
import adapter.PropertyAdapter;
import model.DrawerList;
import model.EventListmodel;
import util.AppPreferences;
import util.Common;
import util.Constants;
import util.LoggerGeneral;
import util.ServiceFacade;

public class EventsList extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private EventListAdapter adapter;

//    EventListAdapter adapter;
    List<EventListmodel> evenlist;

    EventListmodel eventModel;
    LinearLayout prpublish;

    LinearLayout search;

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;

    LinearLayout home;
    Fragment fragment = null;
    TextView appname;
    TextView title;

    RelativeLayout notification;
    EditText searchSociety;
    public boolean chechSearchVisiblity=false;
    String SocietyId;
    LinearLayout createevent,archived;
    TextView noevent;

    AppPreferences appPreferences;
    String  event;
    Context context;

    String eid;
    String esociety_id;
    String etitle;
    String eevent_date;
    String edescription;
    String eevent_image;
    String eaddress;
    String elandmark;
    String estate;
    String ecity;
    String epostal_code;
    String eshared_with;
    String ecreated_by;
    String ecreated_on;
    String estatus;
    String eevent_image_url;
    String euser_name;
    String euser_profile_image;
    String event_date_time;
    String Datess;
    String days_pass;
    String dateofevent;
    String At;

    public static Bundle temp_bundle;
    int savedInstanceCheck=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (getIntent().hasExtra("bundle") && savedInstanceState==null){
            LoggerGeneral.info("isItRight");
            savedInstanceCheck=1;
            savedInstanceState = getIntent().getExtras().getBundle("bundle");
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_list);

        context = EventsList.this;
        Common.internet_check=15;
        appPreferences =AppPreferences.getAppPreferences(context);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        title=((TextView) toolbar.findViewById(R.id.toolbar_title));
        title.setText("Events");
        toolbar.setTitleTextColor(android.graphics.Color.WHITE);
        SocietyId = getIntent().getStringExtra("SocietyId");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.my_statusbar_color));
        }
        noevent  = (TextView)findViewById(R.id.noevent);

        event=getIntent().getStringExtra("event");

        dateofevent = getIntent().getStringExtra("dateofevent");

        if (savedInstanceCheck==1)
        {
            SocietyId=savedInstanceState.getString("SocietyId");
            event=savedInstanceState.getString("event");
            dateofevent=savedInstanceState.getString("dateofevent");
        }
        if(event!=null)
        {

        if(event.equals("yesevent")||event=="yesevent"){


            LoggerGeneral.info("showeve");
        }

        if(event.equals("noevent")||event=="noevent"){
            LoggerGeneral.info("noshoweve");
            noevent.setVisibility(View.VISIBLE);

        }
            }


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

    //    toolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.backarrow));

        toolbar.getNavigationIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //What to do on back clicked
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                if (mDrawerLayout.isDrawerOpen(GravityCompat.END)) {
                    mDrawerLayout.closeDrawers();
                } else if (chechSearchVisiblity == true) {

                    searchSociety.setVisibility(View.GONE);
                    searchSociety.setText("");
                    title.setVisibility(View.VISIBLE);
                    home.setVisibility(View.VISIBLE);
                    notification.setVisibility(View.VISIBLE);
                    search.setVisibility(View.VISIBLE);
                    chechSearchVisiblity = false;
                    adapter = new EventListAdapter(context, evenlist,event,dateofevent);

                    recyclerView.setAdapter(adapter);

                    adapter.notifyDataSetChanged();

                } else {
                    Common.internet_check=0;
                    Intent back = new Intent(EventsList.this, DashBoard.class);
                    back.putExtra("event", 3);
                    back.putExtra("SocietyId",SocietyId);
                    startActivity(back);
                    finish();
                }
            }
        });




        search= (LinearLayout) toolbar.findViewById(R.id.searchSociety);
        searchSociety= (EditText) toolbar.findViewById(R.id.search);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title.setVisibility(View.GONE);
                searchSociety.setVisibility(View.VISIBLE);
                notification.setVisibility(View.GONE);
                search.setVisibility(View.GONE);
                home.setVisibility(View.GONE);
                chechSearchVisiblity = true;
                searchSociety.addTextChangedListener(new TextWatcher() {

                    public void afterTextChanged(Editable s) {
                    }

                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    public void onTextChanged(CharSequence query, int start, int before, int count) {

                        query = query.toString().toLowerCase();

                        final List<EventListmodel> filteredList = new ArrayList<EventListmodel>();

                        for (int i = 0; i < evenlist.size(); i++) {

                            final String text = evenlist.get(i).getEventname().toLowerCase();
                            if (text.contains(query)) {

                                filteredList.add(evenlist.get(i));
                            }
                        }

                        recyclerView.setLayoutManager(new LinearLayoutManager(context));
                        adapter = new EventListAdapter(context, filteredList,event,dateofevent);
                        recyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();  // data set changed
                    }
                });

            }
        });

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
                Common.internet_check=0;
                Intent notification=new Intent(EventsList.this,NotificationApp.class);
                //    notification.putExtra("SocietyId",Soci)
                int test1=appPreferences.getInt("NotificationOld",0);
                test1=test1+Constants.notififcationcount;
                appPreferences.putInt("NotificationOld",test1);
                notification.putExtra("SocietyId", SocietyId);
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
                Common.internet_check=0;
                Intent myProperty=new Intent(EventsList.this,MyProperty.class);
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
                        Common.internet_check=0;
                        LoggerGeneral.info("1");
                        Intent profile=new Intent(EventsList.this,Profile.class);
                        profile.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(profile);
                        finish();
                        break;
                    case 2:
                        Common.internet_check=0;
                        LoggerGeneral.info("2");
                        Intent addSociety=new Intent(EventsList.this,AddSociety.class);
                        startActivity(addSociety);
                        finish();
                        break;
                    case 3:
                        Common.internet_check=0;
                        LoggerGeneral.info("3");
                        Intent myProperty=new Intent(EventsList.this,MyProperty.class);
                        PropertyAdapter.intentCheck=0;
                        startActivity(myProperty);
                        finish();
                        break;
                    case 4:
                        Common.internet_check=0;
                        LoggerGeneral.info("4");
                        Intent events=new Intent(EventsList.this,DashBoard.class);
                        events.putExtra("SocietyId", SocietyId);
                        events.putExtra("event",3);
                        startActivity(events);
                        finish();
                        break;
                    case 5:
                        Common.internet_check=0;
                        LoggerGeneral.info("5");
                        Intent createEvents=new Intent(EventsList.this,CreateEvent.class);
                        createEvents.putExtra("SocietyId", SocietyId);
                        startActivity(createEvents);
                        finish();
                        break;
                    case 6:
                        Common.internet_check=0;

                        LoggerGeneral.info("6");
                        Intent archivedEvents=new Intent(EventsList.this,Archivedevent.class);
                        archivedEvents.putExtra("SocietyId", SocietyId);
                        startActivity(archivedEvents);
                        finish();
                        break;
                    case 7:
                        Common.internet_check=0;
                        LoggerGeneral.info("7");
                        Intent announcement=new Intent(EventsList.this,DashBoard.class);
                        announcement.putExtra("SocietyId", SocietyId);
                        announcement.putExtra("announcement",4);
                        startActivity(announcement);
                        finish();
                        break;
                    case 8:
                        Common.internet_check=0;
                        LoggerGeneral.info("8");
                        Intent createAnnouncement=new Intent(EventsList.this,CreateAnnouncement.class);
                        createAnnouncement.putExtra("SocietyId", SocietyId);
                        startActivity(createAnnouncement);
                        finish();
                        break;
                    case 9:
                        Common.internet_check=0;
                        LoggerGeneral.info("9");
                        Intent polls=new Intent(EventsList.this,DashBoard.class);
                        polls.putExtra("SocietyId", SocietyId);
                        polls.putExtra("polls",2);
                        startActivity(polls);
                        finish();
                        break;
                    case 10:
                        Common.internet_check=0;
                        LoggerGeneral.info("10");
                        Intent createEventPoll=new Intent(EventsList.this,CreatePoll.class);
                        createEventPoll.putExtra("SocietyId", SocietyId);
                        startActivity(createEventPoll);
                        finish();
                        break;
                    case 11:
                        Common.internet_check=0;
                        LoggerGeneral.info("11");
                        Intent archivedEventPoll=new Intent(EventsList.this,ArchivedPolls.class);
                        archivedEventPoll.putExtra("SocietyId", SocietyId);
                        startActivity(archivedEventPoll);
                        finish();
                        break;
                    case 12:
                        Common.internet_check=0;
                        LoggerGeneral.info("12");
                        Intent members=new Intent(EventsList.this,Members.class);
                        members.putExtra("SocietyId", SocietyId);
                        startActivity(members);
                        finish();
                        break;
                    case 13:
                        Common.internet_check=0;
                        LoggerGeneral.info("13");
                        Intent offers=new Intent(EventsList.this,Offer.class);
                        offers.putExtra("SocietyId", SocietyId);
                        startActivity(offers);
                        finish();
                        break;
                    case 14:
                        Common.internet_check=0;
                        LoggerGeneral.info("14");
                        Intent complaint=new Intent(EventsList.this,Complaint.class);
                        complaint.putExtra("SocietyId", SocietyId);
                        startActivity(complaint);
                        finish();
                        break;
                    case 15:
                        Common.internet_check=0;
                        LoggerGeneral.info("15");
                        Intent notification=new Intent(EventsList.this,NotificationApp.class);
                        //    notification.putExtra("SocietyId",Soci)
                        int test1=appPreferences.getInt("NotificationOld",0);
                        test1=test1+Constants.notififcationcount;
                        appPreferences.putInt("NotificationOld",test1);
                        notification.putExtra("SocietyId", SocietyId);
                        Constants.notififcationcount=0;
                        startActivity(notification);
                        finish();
                        break;
                    case 16:
                       /* Common.internet_check=0;
                        LoggerGeneral.info("16");
                        Intent intent = new Intent(EventsList.this, Login.class);
                        appPreferences.remove("user_id");
                        appPreferences.remove("user_name");
                        appPreferences.remove("email_id");
                        appPreferences.remove("property_count");
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


        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.addItemDecoration(new VerticalLineDecorator(2));
        evenlist = new ArrayList<EventListmodel>();

    /*    eventModel = new EventListmodel();
        for(int i = 0;i<=20;i++){

            eventModel.setEventname("BirthDay Party");
            eventModel.setCreatedby("Jhon Doe");
            eventModel.setDate("25-09-2016, 8 pm");
            eventModel.setDescription("We request all the society members to..... join them ......in the birthday party which will be celebrated on..... Saturday ,1st Oct 2016 from 5.30 to 8.30 pm at Sindhu");
            evenlist.add(eventModel);
//          societyModel.setSocimg(String.valueOf(R.mipmap.background_material));
            LoggerGeneral.info("showsoc" +eventModel.getEventname() + "---" + evenlist.get(i).getEventname());


        }*/






        createevent = (LinearLayout)findViewById(R.id.createevent);
        createevent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Common.internet_check=0;
                Intent createevent = new Intent(EventsList.this, CreateEvent.class);
                createevent.putExtra("SocietyId",SocietyId);
                createevent.putExtra("dateofevent",dateofevent);
                createevent.putExtra("event",event);
                startActivity(createevent);
                finish();

            }
        });

        archived = (LinearLayout)findViewById(R.id.archived);
        archived.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Common.internet_check=0;
                Intent archive = new Intent(EventsList.this, Archivedevent.class);
                archive.putExtra("SocietyId",SocietyId);
                archive.putExtra("dateofevent",dateofevent);
                archive.putExtra("event",event);
                startActivity(archive);
                finish();
            }
        });


        if(Common.isOnline(context)){

            new getEventDate().execute();
        }
        else {
            Common.showToast(context,"No internet connection!");
        }

        temp_bundle = new Bundle();
        onSaveInstanceState(temp_bundle);

        registerReceiver(broadcast_reciever2, new IntentFilter("finish_activity2"));

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

            mDialog = new ProgressDialog(EventsList.this,ProgressDialog.THEME_HOLO_DARK);
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
                        Intent register   = new Intent(EventsList.this,Login.class);
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

    @Override
    public void onBackPressed() {
        if(mDrawerLayout.isDrawerOpen(GravityCompat.END))
        {
            mDrawerLayout.closeDrawers();
            return;
        }
        /*else if(chechSearchVisiblity==true)
        {

            searchSociety.setVisibility(View.GONE);
            searchSociety.setText("");
            title.setVisibility(View.VISIBLE);
            home.setVisibility(View.VISIBLE);
            notification.setVisibility(View.VISIBLE);
            search.setVisibility(View.VISIBLE);
            chechSearchVisiblity=false;
            adapter = new EventListAdapter(this, evenlist,event,dateofevent);

            recyclerView.setAdapter(adapter);

            adapter.notifyDataSetChanged();

            return;
        }*/
        else {

            super.onBackPressed();
            Common.internet_check=0;
            LoggerGeneral.info("onbackfinish000");

            Intent back = new Intent(EventsList.this, DashBoard.class);
            back.putExtra("event",3);
            back.putExtra("SocietyId",SocietyId);
            startActivity(back);
            finish();

        }
    }



    @Override
    protected void onStart() {
        super.onStart();
        FlurryAgent.onStartSession(this, "QTPRGMHYD693T2MFCTN2");
        FlurryAgent.setLogEnabled(true);
        FlurryAgent.setLogEvents(true);
        FlurryAgent.setLogLevel(Log.VERBOSE);
     //   registerReceiver(broadcast_reciever2, new IntentFilter("finish_activity2"));
    }

    @Override
    protected void onStop() {
        super.onStop();
        FlurryAgent.onEndSession(this);
    }



    class  getEventDate extends AsyncTask<String, String, JSONObject> {
        String type;
        ProgressDialog mDialog;
        @Override
        protected JSONObject doInBackground(String... params) {
            // TODO Auto-generated method stub

            String url= Constants.Base+Constants.getEventDates;

            JSONObject object = new JSONObject();
            try {

                object.put("society_id",SocietyId);
                object.put("user_id",appPreferences.getString("user_id",""));
                object.put("event_date",dateofevent);



                LoggerGeneral.info("JsonObjectPrintEvent" + object.toString());

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



                            for(int i=jsonArray.length()-1;0<=i;i--){

                                JSONObject jsonObject  = jsonArray.getJSONObject(i);

                                eid                    = jsonObject.getString("id");
                                esociety_id            = jsonObject.getString("society_id");
                                etitle                 = jsonObject.getString("title");
                                eevent_date            = jsonObject.getString("event_date");


                                edescription           = jsonObject.getString("description");
                                eevent_image           = jsonObject.getString("event_image");
                                eaddress               = jsonObject.getString("address");
                                elandmark              = jsonObject.getString("landmark");
                                estate                 = jsonObject.getString("state");
                                ecity                  = jsonObject.getString("city");
                                epostal_code           = jsonObject.getString("postal_code");
                                eshared_with           = jsonObject.getString("shared_with");
                                ecreated_by            = jsonObject.getString("created_by");
                                ecreated_on            = jsonObject.getString("created_on");
                                estatus                = jsonObject.getString("status");
                                eevent_image_url       = jsonObject.getString("event_image_url");
                                event_date_time        = jsonObject.getString("event_date_time");
                                euser_name             = jsonObject.getString("user_name");
                                euser_profile_image    = jsonObject.getString("user_profile_image");
                                Datess                 = jsonObject.getString("date");
                                days_pass              = jsonObject.getString("days_pass");
                                At                     =jsonObject.getString("time");

                                String myFormat1 = "yyyy-MM-dd";
                                String myFormat = "dd MMM, yyyy";
                                SimpleDateFormat sdf1 = new SimpleDateFormat(myFormat1, Locale.US);
                                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                                Date myDate = null;
                                try {
                                    myDate = sdf1.parse(Datess);

                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                Datess=sdf.format(myDate);
                                eventModel = new EventListmodel();



                                eventModel.setEid(eid);
                                eventModel.setEsociety_id(esociety_id);
                                eventModel.setEtitle(etitle);
                                eventModel.setEevent_date(eevent_date);
                                eventModel.setEdescription(edescription);
                                eventModel.setEevent_image(eevent_image);
                                eventModel.setEaddress(eaddress);
                                eventModel.setElandmark(elandmark);
                                eventModel.setEstate(estate);
                                eventModel.setEcity(ecity);
                                eventModel.setEpostal_code(epostal_code);
                                eventModel.setEshared_with(eshared_with);
                                eventModel.setEcreated_by(ecreated_by);
                                eventModel.setEcreated_on(ecreated_on);
                                eventModel.setEstatus(estatus);
                                eventModel.setEevent_image_url(eevent_image_url);
                                eventModel.setEuser_name(euser_name);
                                eventModel.setEuser_profile_image(euser_profile_image);
                                eventModel.setEvent_date_time(event_date_time);
                                eventModel.setsDate(Datess);
                                eventModel.setDays_pass(days_pass);
                                eventModel.setAt(At);
                                evenlist.add(eventModel);


                                if(event.equals("yesevent")||event=="yesevent"){

                                    noevent.setVisibility(View.GONE);
                                    adapter = new EventListAdapter(context, evenlist,event,dateofevent);

                                    recyclerView.setAdapter(adapter);

                                    adapter.notifyDataSetChanged();


                                    LoggerGeneral.info("showeve");
                                }

                                if(event.equals("noevent")||event=="noevent"){


                                    noevent.setVisibility(View.VISIBLE);
                                    LoggerGeneral.info("noshoweve1");


                                }

                            }

                        }

                    }

                    if(account_status == 0) {
                        Common.internet_check=0;
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

    BroadcastReceiver broadcast_reciever2 = new BroadcastReceiver() {

        @Override
        public void onReceive(Context arg0, Intent intent) {
            String action = intent.getAction();
            if (action.equals("finish_activity2")) {

                LoggerGeneral.info("broadcast entered22222");
                finish();
                // DO WHATEVER YOU WANT.
            }
        }
    };
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current game state
        if(event!=null) {
            temp_bundle.putString("event", event);
        }
        if(dateofevent!=null)
        {
            temp_bundle.putString("dateofevent",dateofevent);
        }
        if(SocietyId!=null) {
            temp_bundle.putString("SocietyId", SocietyId);
        }
        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        // Always call the superclass so it can restore the view hierarchy
        super.onRestoreInstanceState(savedInstanceState);

        // Restore state members from saved instance
    }


    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(broadcast_reciever, new IntentFilter("finish_activity"));

        registerReceiver(broadcast_reciever2, new IntentFilter("finish_activity2"));

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcast_reciever);

        unregisterReceiver(broadcast_reciever2);
    }

    BroadcastReceiver broadcast_reciever = new BroadcastReceiver() {

        @Override
        public void onReceive(Context arg0, Intent intent) {
            String action = intent.getAction();
            if (action.equals("finish_activity")) {

                LoggerGeneral.info("broadcast entered");
                finish();
                // DO WHATEVER YOU WANT.
            }
        }
    };

    public  static boolean isRunning(Context ctx) {
        ActivityManager activityManager = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = activityManager.getRunningTasks(Integer.MAX_VALUE);

        for (ActivityManager.RunningTaskInfo task : tasks) {
            if (ctx.getPackageName().equalsIgnoreCase(task.baseActivity.getPackageName()))
                return true;
        }

        return false;
    }

}
