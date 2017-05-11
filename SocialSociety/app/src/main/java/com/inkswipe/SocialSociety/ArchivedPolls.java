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

import adapter.ArchivedPollAdapter;
import adapter.CustomAdapter;
import adapter.PropertyAdapter;
import model.ArchivedPolllistmodel;
import model.DrawerList;
import util.AppPreferences;
import util.Common;
import util.Constants;
import util.LoggerGeneral;
import util.ServiceFacade;

public class ArchivedPolls extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    List<ArchivedPolllistmodel> archivedpolllist;

    ArchivedPolllistmodel archiveModel;
    LinearLayout prpublish;

    TextView title;
    LinearLayout search;
    EditText searchSociety;
    RelativeLayout notification;
    LinearLayout home;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    public boolean chechSearchVisiblity=false;
    AppPreferences appPreferences;
    Context context;
    ArchivedPolllistmodel  archivedPolllistmodel;
    String SocietyId;
    TextView nopoll;
    int savedInstanceCheck=0;
    public static Bundle temp_bundle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (getIntent().hasExtra("bundle") && savedInstanceState==null){
            LoggerGeneral.info("isItRight");
            savedInstanceCheck=1;
            savedInstanceState = getIntent().getExtras().getBundle("bundle");
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_archived_polls);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        context =ArchivedPolls.this;

        Common.internet_check=5;
        SocietyId=getIntent().getStringExtra("SocietyId");
        if(savedInstanceCheck==1)
        {
            SocietyId=savedInstanceState.getString("SocietyId");
        }
        appPreferences =AppPreferences.getAppPreferences(context);

        title=((TextView) toolbar.findViewById(R.id.toolbar_title));
        title.setText("Archived Polls");
        toolbar.setTitleTextColor(android.graphics.Color.WHITE);

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
                    adapter = new ArchivedPollAdapter(context, archivedpolllist);

                    recyclerView.setAdapter(adapter);

                    adapter.notifyDataSetChanged();
                } else {

                    Intent back = new Intent(ArchivedPolls.this, DashBoard.class);
                    back.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    back.putExtra("SocietyId", SocietyId);
                    back.putExtra("polls", 2);
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
        nopoll= (TextView) findViewById(R.id.noPoll);
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

                        final List<ArchivedPolllistmodel> filteredList = new ArrayList<ArchivedPolllistmodel>();

                        for (int i = 0; i < archivedpolllist.size(); i++) {

                            final String text = archivedpolllist.get(i).getName().toLowerCase();
                            if (text.contains(query)) {

                                filteredList.add(archivedpolllist.get(i));
                            }
                        }

                        recyclerView.setLayoutManager(new LinearLayoutManager(context));
                        adapter = new ArchivedPollAdapter(context, filteredList);
                        recyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();  // data set changed
                    }
                });

            }
        });

        searchSociety= (EditText) toolbar.findViewById(R.id.search);

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
                Intent notification=new Intent(ArchivedPolls.this,NotificationApp.class);
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
                Intent myProperty=new Intent(ArchivedPolls.this,MyProperty.class);
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
                        Intent profile = new Intent(ArchivedPolls.this, Profile.class);
                        profile.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        Common.internet_check=0;
                        startActivity(profile);
                        finish();
                        break;
                    case 2:
                        LoggerGeneral.info("2");
                        Intent addSociety = new Intent(ArchivedPolls.this, AddSociety.class);
                        Common.internet_check=0;
                        startActivity(addSociety);
                        finish();
                        break;
                    case 3:
                        LoggerGeneral.info("3");
                        Intent myProperty = new Intent(ArchivedPolls.this, MyProperty.class);
                        Common.internet_check=0;
                        PropertyAdapter.intentCheck = 0;
                        startActivity(myProperty);
                        finish();
                        break;
                    case 4:
                        LoggerGeneral.info("4");
                        Intent events = new Intent(ArchivedPolls.this, DashBoard.class);
                        Common.internet_check=0;
                        events.putExtra("event", 3);
                        events.putExtra("SocietyId", SocietyId);
                        startActivity(events);
                        finish();
                        break;
                    case 5:
                        LoggerGeneral.info("5");
                        Intent createEvents = new Intent(ArchivedPolls.this, CreateEvent.class);
                        Common.internet_check=0;
                        createEvents.putExtra("SocietyId", SocietyId);
                        startActivity(createEvents);
                        finish();
                        break;
                    case 6:
                        LoggerGeneral.info("6");
                        Intent archivedEvents = new Intent(ArchivedPolls.this, Archivedevent.class);
                        Common.internet_check=0;
                        archivedEvents.putExtra("SocietyId", SocietyId);
                        startActivity(archivedEvents);
                        finish();
                        break;
                    case 7:
                        LoggerGeneral.info("7");
                        Intent announcement = new Intent(ArchivedPolls.this, DashBoard.class);
                        Common.internet_check=0;
                        announcement.putExtra("SocietyId", SocietyId);
                        announcement.putExtra("announcement", 4);
                        startActivity(announcement);
                        finish();
                        break;
                    case 8:
                        LoggerGeneral.info("8");
                        Intent createAnnouncement = new Intent(ArchivedPolls.this, CreateAnnouncement.class);
                        Common.internet_check=0;
                        createAnnouncement.putExtra("SocietyId", SocietyId);
                        startActivity(createAnnouncement);
                        finish();
                        break;
                    case 9:
                        LoggerGeneral.info("9");
                        Intent polls = new Intent(ArchivedPolls.this, DashBoard.class);
                        Common.internet_check=0;
                        polls.putExtra("SocietyId", SocietyId);
                        polls.putExtra("polls", 2);
                        startActivity(polls);
                        finish();
                        break;
                    case 10:
                        LoggerGeneral.info("10");
                        Intent createEventPoll = new Intent(ArchivedPolls.this, CreatePoll.class);
                        Common.internet_check=0;
                        createEventPoll.putExtra("SocietyId", SocietyId);
                        startActivity(createEventPoll);
                        finish();
                        break;
                    case 11:

                        mDrawerLayout.closeDrawers();
                        /*LoggerGeneral.info("11");
                        Intent archivedEventPoll=new Intent(ArchivedPolls.this,ArchivedPolls.class);
                        startActivity(archivedEventPoll);
                        finish();*/
                        break;
                    case 12:
                        LoggerGeneral.info("12");
                        Intent members = new Intent(ArchivedPolls.this, Members.class);
                        Common.internet_check=0;
                        members.putExtra("SocietyId", SocietyId);
                        startActivity(members);
                        finish();
                        break;
                    case 13:
                        LoggerGeneral.info("13");
                        Intent offers = new Intent(ArchivedPolls.this, Offer.class);
                        Common.internet_check=0;
                        offers.putExtra("SocietyId", SocietyId);
                        startActivity(offers);
                        finish();
                        break;
                    case 14:
                        LoggerGeneral.info("14");
                        Intent complaint = new Intent(ArchivedPolls.this, Complaint.class);
                        Common.internet_check=0;
                        complaint.putExtra("SocietyId", SocietyId);
                        startActivity(complaint);
                        finish();
                        break;
                    case 15:
                        Intent notification=new Intent(ArchivedPolls.this,NotificationApp.class);
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
                      /*  LoggerGeneral.info("16");
                        Intent intent = new Intent(ArchivedPolls.this, Login.class);
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
        archivedpolllist = new ArrayList<ArchivedPolllistmodel>();


/*
        ArchivedPolllistmodel  archivedPolllistmodel= new ArchivedPolllistmodel();
        for(int i = 0;i<=20;i++){
            archivedPolllistmodel.setName("Ramesh Kumar");
            archivedPolllistmodel.setDate("29th Sept 2016");
            archivedPolllistmodel.setSubject("Should we change the name and colour of our society? Please give answer!");
            archivedPolllistmodel.setEnddate("03 Oct 2016");

            archivedpolllist.add(archivedPolllistmodel);
//            societyModel.setSocimg(String.valueOf(R.mipmap.background_material));


        }*/


            if(Common.isOnline(context)) {

                new ArchivedPost().execute();
            }
            else {
                Common.showToast(context,"No internet connection");
            }

        temp_bundle = new Bundle();
        onSaveInstanceState(temp_bundle);


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

            mDialog = new ProgressDialog(ArchivedPolls.this,ProgressDialog.THEME_HOLO_DARK);
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
                        Intent register   = new Intent(ArchivedPolls.this,Login.class);
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

    class  ArchivedPost extends AsyncTask<String, String, JSONObject> {
        String type;
        ProgressDialog mDialog;
        @Override
        protected JSONObject doInBackground(String... params) {
            // TODO Auto-generated method stub

            String url= Constants.Base+Constants.GetUserArchivedPolls;

            JSONObject object = new JSONObject();
            try {

                 // object.put("society_id",SocietyId);
                object.put("society_id", SocietyId);
                object.put("user_id",appPreferences.getString("user_id",""));
               /* object.put("user_id","11");
                object.put("society_id","19");
*/

                LoggerGeneral.info("JsonObjectPrintArchivedPoll" + object.toString());

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
                                archivedPolllistmodel = new ArchivedPolllistmodel();
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                String id                     = jsonObject.getString("id");
                                String society_id             = jsonObject.getString("society_id");
                                String poll_title             = jsonObject.getString("poll_title");
                                String poll_question          = jsonObject.getString("poll_question");
                                String poll_end_date          = jsonObject.getString("poll_end_date");
                                String poll_image             = jsonObject.getString("poll_image");
                                String poll_option_1          = jsonObject.getString("poll_option_1");
                                String poll_option_2          = jsonObject.getString("poll_option_2");
                                String poll_option_3          = jsonObject.getString("poll_option_3");
                                String poll_option_4          = jsonObject.getString("poll_option_4");
                                String poll_option_5          = jsonObject.getString("poll_option_5");
                                String poll_option_6          = jsonObject.getString("poll_option_6");
                                String user_name              = jsonObject.getString("user_name");
                                String user_profile_image     = jsonObject.getString("user_profile_image");
                                String shared_with            = jsonObject.getString("shared_with");
                                String created_by             = jsonObject.getString("created_by");
                                String created_on             = jsonObject.getString("created_on");
                                String status                 = jsonObject.getString("status");
                                String poll_image_url         = jsonObject.getString("poll_image_url");
                                String share_result           = jsonObject.getString("share_result");

                                String myFormat1 = "yyyy-MM-dd hh:mm:ss";
                                String myFormat = "dd MMM, yyyy";
                                SimpleDateFormat sdf1 = new SimpleDateFormat(myFormat1, Locale.US);
                                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                                Date myDate = null;
                                try {
                                    myDate = sdf1.parse(poll_end_date);

                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                poll_end_date=sdf.format(myDate);

                                String myFormat11 = "yyyy-MM-dd hh:mm:ss";
                                String myFormat12 = "dd MMM, yyyy";
                                SimpleDateFormat sdf11 = new SimpleDateFormat(myFormat11, Locale.US);
                                SimpleDateFormat sdf12 = new SimpleDateFormat(myFormat12, Locale.US);

                                Date myDate1 = null;
                                try {
                                    myDate1 = sdf11.parse(created_on);

                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                created_on=sdf12.format(myDate1);


                                archivedPolllistmodel.setId(id);
                                archivedPolllistmodel.setSociety_id(society_id);
                                archivedPolllistmodel.setPoll_title(poll_title);
                                archivedPolllistmodel.setPoll_question(poll_question);
                                archivedPolllistmodel.setPoll_end_date(poll_end_date);
                                archivedPolllistmodel.setPoll_image(poll_image);
                                archivedPolllistmodel.setPoll_option_1(poll_option_1);
                                archivedPolllistmodel.setPoll_option_2(poll_option_2);
                                archivedPolllistmodel.setPoll_option_3(poll_option_3);
                                archivedPolllistmodel.setPoll_option_4(poll_option_4);
                                archivedPolllistmodel.setPoll_option_5(poll_option_5);
                                archivedPolllistmodel.setPoll_option_6(poll_option_6);
                                archivedPolllistmodel.setUser_name(user_name);
                                archivedPolllistmodel.setUser_profile_image(user_profile_image);
                                archivedPolllistmodel.setShared_with(shared_with);
                                archivedPolllistmodel.setCreated_by(created_by);
                                archivedPolllistmodel.setCreated_on(created_on);
                                archivedPolllistmodel.setStatus(status);
                                archivedPolllistmodel.setPoll_image_url(poll_image_url);
                                archivedPolllistmodel.setShare_result(share_result);



                                archivedpolllist.add(archivedPolllistmodel);



                            }

                            adapter = new ArchivedPollAdapter(context, archivedpolllist);

                            recyclerView.setAdapter(adapter);

                            adapter.notifyDataSetChanged();
                        }

                        if(status_code==1){
                            nopoll.setVisibility(View.VISIBLE);
                            Common.showToast(context,"No polls found");
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
                        ((Activity) context).finish();
                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mDialog.dismiss();


            }
        }

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
            adapter = new ArchivedPollAdapter(this, archivedpolllist);

            recyclerView.setAdapter(adapter);

            adapter.notifyDataSetChanged();
            return;
        }
        else {

            super.onBackPressed();
            Intent back = new Intent(ArchivedPolls.this, DashBoard.class);
            back.putExtra("SocietyId", SocietyId);
            back.putExtra("polls",2);
            back.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Common.internet_check=0;
            startActivity(back);
            finish();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current game state

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
