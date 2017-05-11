package com.inkswipe.SocialSociety;

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

import java.util.ArrayList;
import java.util.List;

import adapter.CustomAdapter;
import adapter.MemberAdapter;
import adapter.MemberAdapter1;
import adapter.PropertyAdapter;
import model.DrawerList;
import model.MembersModel;
import util.AppPreferences;
import util.Common;
import util.Constants;
import util.ExceptionHandler;
import util.LoggerGeneral;
import util.ServiceFacade;

public class Members extends AppCompatActivity {
    RecyclerView recyclerView;
    MemberAdapter adapter;
    MemberAdapter1 adapter1;
  //  MemberAdapter1 adapter1;
    Context context;

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;

    List<MembersModel> membersModelList;
    TextView nogroup;

    MembersModel membersModel;
    String SocietyId;
    LinearLayout home;
    Fragment fragment = null;
    TextView appname;
    LinearLayout search;
    RelativeLayout notification;
    TextView title;
    EditText searchSociety;
    LinearLayout groups,allMembers,acceptedPeople,rejectedPeople,bottom,bottom2;
    public boolean chechSearchVisiblity=false;
    String accepted,rejected,creator,drawer;
    AppPreferences appPreferences;
    String dateofevent,event,event_id;
    String memberId,memberName,email,password,mobile,gender,address,landmark,city,state,pincode,profile_image,device_id,device_type,device_token,status,date_created,date_modified,cover_image,profile_image_url;
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
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        setContentView(R.layout.activity_members);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        SocietyId=getIntent().getStringExtra("SocietyId");


            accepted = getIntent().getStringExtra("accepted");

            rejected = getIntent().getStringExtra("rejected");

            creator = getIntent().getStringExtra("creator");

        event = getIntent().getStringExtra("event");
        event_id = getIntent().getStringExtra("event_id");

        dateofevent = getIntent().getStringExtra("dateofevent");

        if (savedInstanceCheck==1)
        {
            SocietyId=savedInstanceState.getString("SocietyId");
            accepted=savedInstanceState.getString("accepted");
            rejected=savedInstanceState.getString("rejected");
            creator=savedInstanceState.getString("creator");
            event=savedInstanceState.getString("event");
            dateofevent=savedInstanceState.getString("dateofevent");
            event_id=savedInstanceState.getString("event_id");
        }
        LoggerGeneral.info("accepted showing people"+accepted+"==="+rejected+"===="+creator);

        context=Members.this;
        Common.internet_check=8;
        appPreferences=AppPreferences.getAppPreferences(context);

        nogroup = (TextView)findViewById(R.id.nomember);
        title=((TextView) toolbar.findViewById(R.id.toolbar_title));
        title.setText("Members");
        toolbar.setTitleTextColor(android.graphics.Color.WHITE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.my_statusbar_color));
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
                if(mDrawerLayout.isDrawerOpen(GravityCompat.END))
                {
                    mDrawerLayout.closeDrawers();

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
                    adapter = new MemberAdapter(context, membersModelList,1);

                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
                else {
                    if (accepted!=null || rejected!=null)
                    {
                        Intent back = new Intent(Members.this, EventDetails.class);
                        back.putExtra("creator",creator);
                        back.putExtra("SocietyId",SocietyId);
                        back.putExtra("dateofevent",dateofevent);
                        back.putExtra("event",event);
                        back.putExtra("event_id",event_id);
                        Common.internet_check=0;
                        startActivity(back);
                        finish();
                    }else
                    {
                        Intent back = new Intent(Members.this, DashBoard.class);
                        back.putExtra("SocietyId", SocietyId);
                        Common.internet_check=0;
                        startActivity(back);
                        finish();
                    }
                }
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
                Intent notification=new Intent(Members.this,NotificationApp.class);
                //    notification.putExtra("SocietyId",Soci)
                int test1=appPreferences.getInt("NotificationOld",0);
                test1=test1+Constants.notififcationcount;
                appPreferences.putInt("NotificationOld",test1);
                notification.putExtra("SocietyId", SocietyId);
                Common.internet_check=0;
                Constants.notififcationcount=0;
                startActivity(notification);
                finish();
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

                        final List<MembersModel> filteredList = new ArrayList<MembersModel>();

                        for (int i = 0; i < membersModelList.size(); i++) {

                            final String text = membersModelList.get(i).getMemberName().toLowerCase();
                            if (text.contains(query)) {

                                filteredList.add(membersModelList.get(i));
                            }
                        }


                        recyclerView.setLayoutManager(new LinearLayoutManager(context));
                        adapter = new MemberAdapter(context, filteredList,1);
                        recyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();


                      //  recyclerView1.setLayoutManager(new LinearLayoutManager(context));
                   //     adapter1 = new MemberAdapter1(context, filteredList,1);
                 //       recyclerView1.setAdapter(adapter1);
                  //      adapter1.notifyDataSetChanged();// data set changed
                    }
                });

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
                Intent myProperty=new Intent(Members.this,MyProperty.class);
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
                        Intent profile = new Intent(Members.this, Profile.class);
                        profile.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        Common.internet_check=0;
                        startActivity(profile);
                        finish();
                        break;
                    case 2:
                        LoggerGeneral.info("2");
                        Intent addSociety = new Intent(Members.this, AddSociety.class);
                        Common.internet_check=0;
                        startActivity(addSociety);
                        finish();
                        break;
                    case 3:
                        LoggerGeneral.info("3");
                        Intent myProperty = new Intent(Members.this, MyProperty.class);
                        Common.internet_check=0;
                        PropertyAdapter.intentCheck=0;
                        startActivity(myProperty);
                        finish();
                        break;
                    case 4:
                        LoggerGeneral.info("4");
                        Intent events = new Intent(Members.this, DashBoard.class);
                        Common.internet_check=0;
                        events.putExtra("SocietyId",SocietyId);
                        events.putExtra("event", 3);
                        startActivity(events);
                        finish();
                        break;
                    case 5:
                        LoggerGeneral.info("5");
                        Intent createEvents = new Intent(Members.this, CreateEvent.class);
                        Common.internet_check=0;
                        createEvents.putExtra("SocietyId",SocietyId);
                        startActivity(createEvents);
                        finish();
                        break;
                    case 6:
                        LoggerGeneral.info("6");
                        Intent archivedEvents = new Intent(Members.this, Archivedevent.class);
                        Common.internet_check=0;
                        archivedEvents.putExtra("SocietyId",SocietyId);
                        startActivity(archivedEvents);
                        finish();
                        break;
                    case 7:
                        LoggerGeneral.info("7");
                        Intent announcement = new Intent(Members.this, DashBoard.class);
                        Common.internet_check=0;
                        announcement.putExtra("SocietyId",SocietyId);
                        announcement.putExtra("announcement", 4);
                        startActivity(announcement);
                        finish();
                        break;
                    case 8:
                        LoggerGeneral.info("8");
                        Intent createAnnouncement = new Intent(Members.this, CreateAnnouncement.class);
                        Common.internet_check=0;
                        createAnnouncement.putExtra("SocietyId",SocietyId);
                        startActivity(createAnnouncement);
                        finish();
                        break;
                    case 9:
                        LoggerGeneral.info("9");
                        Intent polls = new Intent(Members.this, DashBoard.class);
                        Common.internet_check=0;
                        polls.putExtra("SocietyId",SocietyId);
                        polls.putExtra("polls", 2);
                        startActivity(polls);
                        finish();
                        break;
                    case 10:
                        LoggerGeneral.info("10");
                        Intent createEventPoll = new Intent(Members.this, CreatePoll.class);
                        Common.internet_check=0;
                        createEventPoll.putExtra("SocietyId",SocietyId);
                        startActivity(createEventPoll);
                        finish();
                        break;
                    case 11:
                        LoggerGeneral.info("11");
                        Intent archivedEventPoll = new Intent(Members.this, ArchivedPolls.class);
                        Common.internet_check=0;
                        archivedEventPoll.putExtra("SocietyId",SocietyId);
                        startActivity(archivedEventPoll);
                        finish();
                        break;
                    case 12:
                        LoggerGeneral.info("12");

                        if(accepted!=null || rejected!=null)
                        {
                            Intent members=new Intent(context,Members.class);
                            Common.internet_check=0;
                            members.putExtra("SocietyId",SocietyId);
                            startActivity(members);
                            finish();
                        }
                        mDrawerLayout.closeDrawers();
                        break;
                    case 13:
                        LoggerGeneral.info("13");
                        Intent offers = new Intent(Members.this, Offer.class);
                        Common.internet_check=0;
                        offers.putExtra("SocietyId",SocietyId);
                        startActivity(offers);
                        finish();
                        break;
                    case 14:
                        LoggerGeneral.info("14");
                        Intent complaint = new Intent(Members.this, Complaint.class);
                        Common.internet_check=0;
                        complaint.putExtra("SocietyId",SocietyId);
                        startActivity(complaint);
                        finish();
                        break;
                    case 15:
                        LoggerGeneral.info("15");
                        Intent notification=new Intent(Members.this,NotificationApp.class);
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
                       /* Intent intent = new Intent(Members.this,Login.class);
                        Common.internet_check=0;
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




        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.addItemDecoration(new VerticalLineDecorator(2));
        membersModelList = new ArrayList<MembersModel>();

  /*      recyclerView1 = (RecyclerView) findViewById(R.id.recycler_view1);
        recyclerView1.setHasFixedSize(true);
        recyclerView1.setLayoutManager(new LinearLayoutManager(context));
        recyclerView1.addItemDecoration(new VerticalLineDecorator(2));
        membersModelList = new ArrayList<MembersModel>();*/



        /*for(int i = 0;i<=20;i++){
            membersModel = new MembersModel();
            membersModel.setUserName("Jhon Doe");
            membersModel.setUserPost("Member");
            membersModelList.add(membersModel);
//            societyModel.setSocimg(String.valueOf(R.mipmap.background_material));
            LoggerGeneral.info("showsoc" +membersModel.getUserName() + "---" + membersModelList.get(i).getUserName());


        }*/




        bottom= (LinearLayout) findViewById(R.id.bottom);
        bottom2= (LinearLayout) findViewById(R.id.bottom2);
        acceptedPeople= (LinearLayout) findViewById(R.id.peopleAccepted);
        rejectedPeople= (LinearLayout) findViewById(R.id.peopleRejected);
        groups= (LinearLayout) findViewById(R.id.groups);


        /*adapter = new MemberAdapter(context, membersModelList);

        recyclerView.setAdapter(adapter);

        adapter.notifyDataSetChanged();*/


        if(accepted!=null)
        {
            bottom.setVisibility(View.GONE);
            bottom2.setVisibility(View.VISIBLE);
            if(Common.isOnline(context)){
                membersModelList = new ArrayList<MembersModel>();
                new getPeople("accept").execute();
            }
            else {
                Common.showToast(context,"No internet connection!");
            }

        }
        else if(rejected!=null)
        {
            bottom.setVisibility(View.GONE);
            bottom2.setVisibility(View.VISIBLE);
            if(Common.isOnline(context)){
                membersModelList = new ArrayList<MembersModel>();
                new getPeople("reject").execute();
            }
            else {
                Common.showToast(context,"No internet connection!");
            }
        }
        else
        {
            bottom.setVisibility(View.VISIBLE);
            bottom2.setVisibility(View.GONE);

            if(Common.isOnline(context)){

                new getMyMembers().execute();
               // new getMyMembers1().execute();
            }
            else {
                Common.showToast(context,"No internet connection!");
            }
        }

        acceptedPeople.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Common.isOnline(context)){
                    membersModelList = new ArrayList<MembersModel>();
                    new getPeople("accept").execute();
                }
                else {
                    Common.showToast(context,"No internet connection!");
                }

            }
        });

        rejectedPeople.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Common.isOnline(context)){
                    membersModelList = new ArrayList<MembersModel>();
                    new getPeople("reject").execute();
                }
                else {
                    Common.showToast(context,"No internet connection!");
                }
            }
        });

        groups.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent groups = new Intent(Members.this, Groups.class);
                Common.internet_check=0;
                groups.putExtra("SocietyId",SocietyId);
                startActivity(groups);
                finish();
            }
        });


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

            mDialog = new ProgressDialog(Members.this,ProgressDialog.THEME_HOLO_DARK);
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
                        Intent register   = new Intent(Members.this,Login.class);
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
            adapter = new MemberAdapter(context, membersModelList,1);
            recyclerView.setAdapter(adapter);

            adapter.notifyDataSetChanged();
            return;
        }
        else {
            super.onBackPressed();
            if (accepted!=null || rejected!=null)
            {
                Intent back = new Intent(Members.this, EventDetails.class);
                Common.internet_check=0;
                back.putExtra("creator", creator);
                back.putExtra("SocietyId",SocietyId);
                back.putExtra("dateofevent",dateofevent);
                back.putExtra("event",event);
                back.putExtra("event_id",event_id);
                startActivity(back);
                finish();
            }else
            {
                Intent back = new Intent(Members.this, DashBoard.class);
                Common.internet_check=0;
                back.putExtra("SocietyId", SocietyId);
                startActivity(back);

                finish();
            }
        }

    }


    class  getMyMembers extends AsyncTask<String, String, JSONObject> {
        String type;
        ProgressDialog mDialog;
        @Override
        protected JSONObject doInBackground(String... params) {
            // TODO Auto-generated method stub

            String url= Constants.Base+Constants.GetMembers;

            JSONObject object = new JSONObject();
            try {

                // object.put("user_id",appPreferences.getString("user_id",""));
                // object.put("user_id",appPreferences.getString("user_id",""));

                object.put("society_id",SocietyId);
                object.put("user_id",appPreferences.getString("user_id",""));

                LoggerGeneral.info("JsonObjectPrint" + object.toString()+"====="+url);

            } catch (Exception ex) {

            }

            //String str = '"' + appPreferences.getString("jwt", "") + '"';
            JSONObject jsonObject  = ServiceFacade.getResponsJsonParams(url, object);

            Log.d("hi", "getresponse" + jsonObject);

            Log.d("hi", "getresponse" + jsonObject);

            return jsonObject;
        }
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
        /*    transparent.setVisibility(View.VISIBLE);
            rotateLoading.setVisibility(View.VISIBLE);
            rotateLoading.start();
*/
            mDialog = new ProgressDialog(context,ProgressDialog.THEME_HOLO_DARK);
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
            mDialog.dismiss();


            if(results!=null){

                try {
                    JSONObject jsonObject = results.getJSONObject("meta");


                    int status_code = jsonObject.getInt("status_code");

                    int account_status = 1;
                    if(jsonObject.has("account_status")){
                        if(!jsonObject.isNull("account_status")){
                            account_status = jsonObject.getInt("account_status");
                        }
                    }


                    if(account_status==1){

                        if (status_code == 0) {

                            JSONArray data = results.getJSONArray("data");


                            //JSONArray jsonArray = data.getJSONArray("data");


                            LoggerGeneral.info("showjarray---" + data);
                            membersModelList = new ArrayList<MembersModel>();
                            for (int i = 0; i <= data.length() - 1; i++) {



                                JSONObject jsonObject1 = data.getJSONObject(i);

                                memberId=jsonObject1.getString("user_id");
                                memberName=jsonObject1.getString("name");
                                profile_image=jsonObject1.getString("profile_image");
                                profile_image_url=jsonObject1.getString("profile_image_url");



                                JSONArray property_name_array = jsonObject1.getJSONArray("property_name_array");

                                ArrayList<String> property_name = new ArrayList<String>();

                                for(int x=0;x<property_name_array.length();x++)
                                {
                                    property_name.add(property_name_array.getString(x));
                                }

                                JSONArray property_type_array = jsonObject1.getJSONArray("property_type_array");

                                ArrayList<String> property_type = new ArrayList<String>();

                                for(int x=0;x<property_type_array.length();x++)
                                {
                                    property_type.add(property_type_array.getString(x));
                                }

                                JSONArray house_no_array = jsonObject1.getJSONArray("house_no_array");

                                ArrayList<String> house_no = new ArrayList<String>();

                                for(int x=0;x<house_no_array.length();x++)
                                {
                                    house_no.add(house_no_array.getString(x));
                                }


                                for(int x=0;x<house_no.size();x++)
                                {
                                    membersModel = new MembersModel();
                                    membersModel.setMemberId(memberId);
                                    membersModel.setMemberName(memberName);
                                    membersModel.setProfile_image(profile_image);
                                    membersModel.setProfile_image_url(profile_image_url);
                                    membersModel.setProperty_type_array(property_type.get(x));
                                    membersModel.setProperty_name_array(property_name.get(x));
                                    membersModel.setHouse_no_array(house_no.get(x));



                                    membersModelList.add(membersModel);
                                }



                                ///LoggerGeneral.info("s_societies000---" + membersModelList.size());
                                //LoggerGeneral.info("House Arraya===="+mGson.toJson(membersModel));
                            }



                            if(membersModelList.size()>0) {
                                nogroup.setVisibility(View.GONE);
                                LoggerGeneral.info("s_societies---" + membersModelList);
                                adapter = new MemberAdapter(context, membersModelList, 1);
                      //          adapter1 = new MemberAdapter1(context, membersModelList, 1);
                                recyclerView.setAdapter(adapter);
                       //         recyclerView1.setAdapter(adapter1);

                                adapter.notifyDataSetChanged();
                       //         adapter1.notifyDataSetChanged();

                            }
                            else {
                                nogroup.setVisibility(View.VISIBLE);
                            }

                        }

                        if (status_code == 1) {


                            nogroup.setVisibility(View.VISIBLE);

                        }
                    }

                    if(account_status==0){
                        Intent intent = new Intent(context,Login.class);
                        Common.internet_check=0;
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



    class  getPeople extends AsyncTask<String, String, JSONObject> {
        String type;
        ProgressDialog mDialog;
        String acceptReject;
        public getPeople(String acceptReject)
        {
            this.acceptReject=acceptReject;
        }
        @Override
        protected JSONObject doInBackground(String... params) {
            // TODO Auto-generated method stub

            String url= Constants.Base+Constants.acceptReject;

            JSONObject object = new JSONObject();
            try {

                // object.put("user_id",appPreferences.getString("user_id",""));
                // object.put("user_id",appPreferences.getString("user_id",""));

                object.put("event_id",event_id);
                object.put("user_id",appPreferences.getString("user_id",""));

                if(acceptReject.equals("accept"))
                {
                    object.put("status","accepted");
                }
                if(acceptReject.equals("reject"))
                {
                    object.put("status","rejected");
                }

                LoggerGeneral.info("JsonObjectPrintacceptReject" + object.toString());

            } catch (Exception ex) {

            }

            //String str = '"' + appPreferences.getString("jwt", "") + '"';
            JSONObject jsonObject  = ServiceFacade.getResponsJsonParams(url, object);

            Log.d("hi", "getresponse" + jsonObject);

            Log.d("hi", "getresponse" + jsonObject);

            return jsonObject;
        }
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
        /*    transparent.setVisibility(View.VISIBLE);
            rotateLoading.setVisibility(View.VISIBLE);
            rotateLoading.start();
*/
            mDialog = new ProgressDialog(context,ProgressDialog.THEME_HOLO_DARK);
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
            mDialog.dismiss();


            if(results!=null){

                try {
                    JSONObject jsonObject = results.getJSONObject("meta");


                    int status_code = jsonObject.getInt("status_code");

                    int account_status = 1;
                    if(jsonObject.has("account_status")){
                        if(!jsonObject.isNull("account_status")){
                            account_status = jsonObject.getInt("account_status");
                        }
                    }


                    if(account_status==1){

                        if (status_code == 0) {

                            JSONArray data = results.getJSONArray("data");


                            //JSONArray jsonArray = data.getJSONArray("data");

                            LoggerGeneral.info("showjarray---" + data);
                            membersModelList = new ArrayList<MembersModel>();
                            for (int i = 0; i <= data.length() - 1; i++) {

                                membersModel = new MembersModel();

                                JSONObject jsonObject1 = data.getJSONObject(i);

                                memberId=jsonObject1.getString("id");
                                memberName=jsonObject1.getString("name");
                                email=jsonObject1.getString("email");
                                password=jsonObject1.getString("password");
                                mobile=jsonObject1.getString("mobile");
                                gender=jsonObject1.getString("gender");
                                address=jsonObject1.getString("address");
                                landmark=jsonObject1.getString("landmark");
                                city=jsonObject1.getString("city");
                                state=jsonObject1.getString("state");
                                pincode=jsonObject1.getString("pincode");
                                profile_image=jsonObject1.getString("profile_image");
                                device_id=jsonObject1.getString("device_id");;
                                device_token=jsonObject1.getString("device_token");
                                device_type=jsonObject1.getString("device_type");
                                status=jsonObject1.getString("status");
                                date_modified=jsonObject1.getString("date_modified");
                                date_created=jsonObject1.getString("date_created");
                                cover_image=jsonObject1.getString("cover_image");
                                profile_image_url=jsonObject1.getString("profile_image_url");


                                membersModel.setMemberId(memberId);
                                membersModel.setMemberName(memberName);
                                membersModel.setEmail(email);
                                membersModel.setPassword(password);
                                membersModel.setMobile(mobile);
                                membersModel.setGender(gender);
                                membersModel.setAddress(address);
                                membersModel.setLandmark(landmark);
                                membersModel.setCity(city);
                                membersModel.setState(state);
                                membersModel.setPincode(pincode);
                                membersModel.setProfile_image(profile_image);
                                membersModel.setDevice_id(device_id);
                                membersModel.setDevice_token(device_token);
                                membersModel.setDevice_type(device_type);
                                membersModel.setStatus(status);
                                membersModel.setDate_modified(date_modified);
                                membersModel.setCover_image(cover_image);
                                membersModel.setDate_created(date_created);
                                membersModel.setProfile_image_url(profile_image_url);

                                membersModelList.add(membersModel);


                                LoggerGeneral.info("s_societies000---" + membersModelList.size());

                            }




                            if(membersModelList.size()>0) {
                                recyclerView.setVisibility(View.VISIBLE);
                          //      recyclerView1.setVisibility(View.VISIBLE);
                                nogroup.setVisibility(View.GONE);
                           //     nogroup1.setVisibility(View.GONE);
                                LoggerGeneral.info("s_societies---" + membersModelList);
                                adapter1=new MemberAdapter1(context,membersModelList,1);
                                recyclerView.setAdapter(adapter1);
                     //           recyclerView1.setAdapter(adapter1);

                                adapter1.notifyDataSetChanged();
                            }
                            else {

                            }


                        }

                        if (status_code == 1) {

                            recyclerView.setVisibility(View.GONE);
                         //   recyclerView1.setVisibility(View.GONE);


                            membersModelList = new ArrayList<MembersModel>();
                            LoggerGeneral.info("s_societies---" + membersModelList);
                            nogroup.setVisibility(View.VISIBLE);
                         //   nogroup1.setVisibility(View.VISIBLE);



                        }
                    }

                    if(account_status==0){
                        Intent intent = new Intent(context,Login.class);
                        Common.internet_check=0;
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

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current game state

        if(SocietyId!=null) {
            temp_bundle.putString("SocietyId", SocietyId);
        }

        if(accepted!=null)
        {
            temp_bundle.putString("accepted",accepted);
        }
        if(rejected!=null)
        {
            temp_bundle.putString("rejected",rejected);
        }
        if(dateofevent!=null)
        {
            temp_bundle.putString("dateofevent",dateofevent);
        }
        if(creator!=null)
        {
            temp_bundle.putString("creator",creator);
        }
        if(event!=null)
        {
            temp_bundle.putString("event",event);
        }
        if(event_id!=null)
        {
            temp_bundle.putString("event_id",event_id);
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
