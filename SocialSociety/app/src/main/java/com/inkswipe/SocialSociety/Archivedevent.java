package com.inkswipe.SocialSociety;

import android.app.Activity;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import adapter.ArchivedAdapter;
import adapter.CustomAdapter;
import adapter.PropertyAdapter;
import model.Archivedlistmodel;
import model.DrawerList;
import util.AppPreferences;
import util.Common;
import util.Constants;
import util.ExceptionHandler;
import util.LoggerGeneral;
import util.ServiceFacade;

public class Archivedevent extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    Context context;
    List<Archivedlistmodel> archivedlist;

    Archivedlistmodel archiveModel;
    LinearLayout prpublish;

    TextView title;
    LinearLayout search;
    EditText searchSociety;
    RelativeLayout notification;
    LinearLayout home;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    public boolean chechSearchVisiblity=false;
    String SocietyId;
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
    String Ttime,month;

    AppPreferences appPreferences;
    int savedInstanceCheck=0;
    String dateofevent,event;
    TextView noEvent;
    public static Bundle temp_bundle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (getIntent().hasExtra("bundle") && savedInstanceState==null){
            LoggerGeneral.info("isItRight");
            savedInstanceCheck=1;
            savedInstanceState = getIntent().getExtras().getBundle("bundle");
        }
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        setContentView(R.layout.activity_archivedevent);
        context = Archivedevent.this;
        Common.internet_check=4;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        appPreferences=AppPreferences.getAppPreferences(this);
        title=((TextView) toolbar.findViewById(R.id.toolbar_title));
        title.setText("Archived Event");
        toolbar.setTitleTextColor(android.graphics.Color.WHITE);
        SocietyId = getIntent().getStringExtra("SocietyId");



        dateofevent = getIntent().getStringExtra("dateofevent");
        event = getIntent().getStringExtra("event");

        if(savedInstanceCheck==1)
        {
            SocietyId = savedInstanceState.getString("SocietyId");
            dateofevent=savedInstanceState.getString("dateofevent");
            event=savedInstanceState.getString("event");
        }

        LoggerGeneral.info("Hiiiiiii fortune" + event+"date event====="+dateofevent);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
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
                    adapter = new ArchivedAdapter(context, archivedlist, archivedlist.get(0).getMonth(),SocietyId,event,dateofevent);

                    recyclerView.setAdapter(adapter);

                    adapter.notifyDataSetChanged();
                } else if (dateofevent != null) {

                    Intent back = new Intent(Archivedevent.this, EventsList.class);
                    back.putExtra("SocietyId", SocietyId);

                    back.putExtra("dateofevent", dateofevent);
                    back.putExtra("event", event);
                    Common.internet_check=0;
                    startActivity(back);
                    finish();
                } else {

                    Intent back = new Intent(Archivedevent.this, DashBoard.class);
                    back.putExtra("SocietyId", SocietyId);
                    back.putExtra("event", 3);
                    Common.internet_check=0;
                    startActivity(back);
                    finish();
                }
            }
        });

        toolbar.getNavigationIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.my_statusbar_color));
        }
        search= (LinearLayout) toolbar.findViewById(R.id.searchSociety);
        searchSociety= (EditText) toolbar.findViewById(R.id.search);
        noEvent= (TextView) findViewById(R.id.noEvent);
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

                        final List<Archivedlistmodel> filteredList = new ArrayList<Archivedlistmodel>();

                        for (int i = 0; i < archivedlist.size(); i++) {

                            final String text = archivedlist.get(i).getEventname().toLowerCase();
                            if (text.contains(query)) {

                                filteredList.add(archivedlist.get(i));
                            }
                        }

                        recyclerView.setLayoutManager(new LinearLayoutManager(context));
                        adapter = new ArchivedAdapter(context, filteredList,archivedlist.get(0).getMonth(),SocietyId,event,dateofevent);
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
                Intent notification=new Intent(Archivedevent.this,NotificationApp.class);
                //    notification.putExtra("SocietyId",Soci)
                int test1=appPreferences.getInt("NotificationOld",0);
                test1=test1+Constants.notififcationcount;
                appPreferences.putInt("NotificationOld",test1);
                notification.putExtra("SocietyId", SocietyId);
                Constants.notififcationcount=0;
                Common.internet_check=0;
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
                Intent myProperty=new Intent(Archivedevent.this,MyProperty.class);
                PropertyAdapter.intentCheck=0;
                Common.internet_check=0;
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
                        Intent profile=new Intent(Archivedevent.this,Profile.class);
                        profile.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        Common.internet_check=0;
                        startActivity(profile);
                        finish();
                        break;
                    case 2:
                        LoggerGeneral.info("2");
                        Intent addSociety=new Intent(Archivedevent.this,AddSociety.class);
                        Common.internet_check=0;
                        startActivity(addSociety);
                        finish();
                        break;
                    case 3:
                        LoggerGeneral.info("3");
                        Intent myProperty=new Intent(Archivedevent.this,MyProperty.class);
                        Common.internet_check=0;
                        PropertyAdapter.intentCheck=0;
                        startActivity(myProperty);
                        finish();
                        break;
                    case 4:
                        LoggerGeneral.info("4");
                        Intent events=new Intent(Archivedevent.this,DashBoard.class);
                        Common.internet_check=0;
                        events.putExtra("SocietyId", SocietyId);
                        events.putExtra("event",3);
                        startActivity(events);
                        finish();
                        break;
                    case 5:
                        LoggerGeneral.info("5");
                        Intent createEvents=new Intent(Archivedevent.this,CreateEvent.class);
                        Common.internet_check=0;
                        createEvents.putExtra("SocietyId", SocietyId);
                        startActivity(createEvents);
                        finish();
                        break;
                    case 6:
                        LoggerGeneral.info("6");
                        mDrawerLayout.closeDrawers();
                        /*Intent archivedEvents=new Intent(Profile.this,Archivedevent.class);
                        startActivity(archivedEvents);
                        finish();*/
                        break;
                    case 7:
                        LoggerGeneral.info("7");
                        Intent announcement=new Intent(Archivedevent.this,DashBoard.class);
                        Common.internet_check=0;
                        announcement.putExtra("SocietyId", SocietyId);
                        announcement.putExtra("announcement",4);
                        startActivity(announcement);
                        finish();
                        break;
                    case 8:
                        LoggerGeneral.info("8");
                        Intent createAnnouncement=new Intent(Archivedevent.this,CreateAnnouncement.class);
                        Common.internet_check=0;
                        createAnnouncement.putExtra("SocietyId", SocietyId);
                        startActivity(createAnnouncement);
                        finish();
                        break;
                    case 9:
                        LoggerGeneral.info("9");
                        Intent polls=new Intent(Archivedevent.this,DashBoard.class);
                        Common.internet_check=0;
                        polls.putExtra("SocietyId", SocietyId);
                        polls.putExtra("polls",2);
                        startActivity(polls);
                        finish();
                        break;
                    case 10:
                        LoggerGeneral.info("10");
                        Intent createEventPoll=new Intent(Archivedevent.this,CreatePoll.class);
                        Common.internet_check=0;
                        createEventPoll.putExtra("SocietyId", SocietyId);
                        startActivity(createEventPoll);
                        finish();
                        break;
                    case 11:
                        LoggerGeneral.info("11");
                        Intent archivedEventPoll=new Intent(Archivedevent.this,ArchivedPolls.class);
                        Common.internet_check=0;
                        archivedEventPoll.putExtra("SocietyId", SocietyId);
                        startActivity(archivedEventPoll);
                        finish();
                        break;
                    case 12:
                        LoggerGeneral.info("12");
                        Intent members=new Intent(Archivedevent.this,Members.class);
                        Common.internet_check=0;
                        members.putExtra("SocietyId", SocietyId);
                        startActivity(members);
                        finish();
                        break;
                    case 13:
                        LoggerGeneral.info("13");
                        Intent offers=new Intent(Archivedevent.this,Offer.class);
                        Common.internet_check=0;
                        offers.putExtra("SocietyId", SocietyId);
                        startActivity(offers);
                        finish();
                        break;
                    case 14:
                        LoggerGeneral.info("14");
                        Intent complaint=new Intent(Archivedevent.this,Complaint.class);
                        Common.internet_check=0;
                        complaint.putExtra("SocietyId", SocietyId);
                        startActivity(complaint);
                        finish();
                        break;
                    case 15:
                        LoggerGeneral.info("15");
                        Intent notification=new Intent(Archivedevent.this,NotificationApp.class);
                        Common.internet_check=0;
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
                        LoggerGeneral.info("16");
                      /*  Intent intent = new Intent(Archivedevent.this, Login.class);
                        Common.internet_check=0;
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
        archivedlist = new ArrayList<Archivedlistmodel>();

       // Archivedlistmodel  archivedlistmodel= new Archivedlistmodel();
        /*for(int i = 0;i<=20;i++){
            archivedlistmodel.setEventofmonth("Nov,2016");
            archivedlistmodel.setEventname("BirthDay Party");
            archivedlistmodel.setCreatedby("Jhon Doe");
            archivedlistmodel.setDate("25-09-2016");
            archivedlistmodel.setTime("8 pm");
            archivedlistmodel.setAddress("The Akshay patra Foundation,Plot # 2,Kesari Nilayam Krishna Nagar Colony,Picket,Secundarabad,Hyderabad-500015 ");

            archivedlist.add(archivedlistmodel);
//            societyModel.setSocimg(String.valueOf(R.mipmap.background_material));
            LoggerGeneral.info("showsoc" + archivedlistmodel.getEventname() + "---" + archivedlist.get(i).getEventname());


        }*/


        if(Common.isOnline(context)){

            new  getArchiveEvent().execute();
        }
        else {
            Common.showToast(context,"No interent connection");
        }


        /*adapter = new ArchivedAdapter(this, archivedlist);

        recyclerView.setAdapter(adapter);

        adapter.notifyDataSetChanged();
*/

        temp_bundle = new Bundle();
        onSaveInstanceState(temp_bundle);

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

            mDialog = new ProgressDialog(Archivedevent.this,ProgressDialog.THEME_HOLO_DARK);
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
                        Intent register   = new Intent(Archivedevent.this,Login.class);
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
        else if(chechSearchVisiblity==true)
        {

            searchSociety.setVisibility(View.GONE);
            searchSociety.setText("");
            title.setVisibility(View.VISIBLE);
            home.setVisibility(View.VISIBLE);
            notification.setVisibility(View.VISIBLE);
            search.setVisibility(View.VISIBLE);
            chechSearchVisiblity=false;
            adapter = new ArchivedAdapter(this, archivedlist,archivedlist.get(0).getMonth(),SocietyId,event,dateofevent);

            recyclerView.setAdapter(adapter);

            adapter.notifyDataSetChanged();
            return;
        }
        else if(dateofevent!=null){
            super.onBackPressed();
            Intent back = new Intent(Archivedevent.this, EventsList.class);
            Common.internet_check=0;
            back.putExtra("SocietyId", SocietyId);
            back.putExtra("dateofevent",dateofevent);
            back.putExtra("event",event);
            startActivity(back);
            finish();
        }
        else {

            super.onBackPressed();
            Intent back = new Intent(Archivedevent.this, DashBoard.class);
            Common.internet_check=0;
            back.putExtra("SocietyId", SocietyId);
            back.putExtra("event",3);
            startActivity(back);
            finish();
        }
    }

    class  getArchiveEvent extends AsyncTask<String, String, JSONObject> {
        String type;
        ProgressDialog mDialog;
        @Override
        protected JSONObject doInBackground(String... params) {
            // TODO Auto-generated method stub

            String url= Constants.Base+Constants.getArchiveEvent;

            JSONObject object = new JSONObject();
            try {

                object.put("society_id",SocietyId);
                object.put("user_id",appPreferences.getString("user_id",""));



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

            mDialog = new ProgressDialog(Archivedevent.this,ProgressDialog.THEME_HOLO_DARK);
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



                            for(int i=0;i<=jsonArray.length()-1;i++){

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
                                Datess = jsonObject.getString("date");
                                Ttime                  = jsonObject.getString("time");
                                month = jsonObject.getString("month");


                                int totalLength=event_date_time.length();

                                totalLength=totalLength-9;

                                event_date_time=event_date_time.substring(0,totalLength);

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


                                Archivedlistmodel  archivedlistmodel= new Archivedlistmodel();



                                archivedlistmodel.setEid(eid);
                                archivedlistmodel.setEsociety_id(esociety_id);
                                archivedlistmodel.setEtitle(etitle);
                                archivedlistmodel.setEevent_date(eevent_date);
                                archivedlistmodel.setEdescription(edescription);
                                archivedlistmodel.setEevent_image(eevent_image);
                                archivedlistmodel.setEaddress(eaddress);
                                archivedlistmodel.setElandmark(elandmark);
                                archivedlistmodel.setEstate(estate);
                                archivedlistmodel.setEcity(ecity);
                                archivedlistmodel.setEpostal_code(epostal_code);
                                archivedlistmodel.setEshared_with(eshared_with);
                                archivedlistmodel.setEcreated_by(ecreated_by);
                                archivedlistmodel.setEcreated_on(ecreated_on);
                                archivedlistmodel.setEstatus(estatus);
                                archivedlistmodel.setEevent_image_url(eevent_image_url);
                                archivedlistmodel.setEuser_name(euser_name);
                                archivedlistmodel.setEuser_profile_image(euser_profile_image);
                                archivedlistmodel.setEvent_date_time(event_date_time);
                                archivedlistmodel.setsDate(Datess);
                                archivedlistmodel.setMonth(month);
                                archivedlistmodel.setTtime(Ttime);

                                archivedlist.add(archivedlistmodel);



                            }

                            adapter = new ArchivedAdapter(context, archivedlist,archivedlist.get(0).getMonth(),SocietyId,event,dateofevent);

                            recyclerView.setAdapter(adapter);

                            adapter.notifyDataSetChanged();


                        }
                        if (status_code == 1) {
                            noEvent.setVisibility(View.VISIBLE);

                        }

                    }

                    if(account_status == 0) {
                        Intent intent = new Intent(context,Login.class);
                        Common.internet_check=0;
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

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current game state

        if(SocietyId!=null)
        {
            temp_bundle.putString("SocietyId",SocietyId);
        }

        if(dateofevent!=null)
        {
            temp_bundle.putString("dateofevent",dateofevent);
        }

        if(event!=null)
        {
            temp_bundle.putString("event",event);
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

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcast_reciever);

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




}
