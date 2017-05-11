package com.inkswipe.SocialSociety;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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

import java.util.ArrayList;
import java.util.List;

import adapter.CustomAdapter;
import adapter.GroupAdapter;
import adapter.PropertyAdapter;
import adapter.ShareeventAdapter;
import model.DrawerList;
import model.GroupsModel;
import model.MembersModel;
import model.ShareeventModel;
import util.AppPreferences;
import util.Common;
import util.Constants;
import util.ExceptionHandler;
import util.LoggerGeneral;
import util.ServiceFacade;

public class ShareEvent extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ShareeventAdapter adapter;
    private RecyclerView.Adapter adapter1;
    GroupsModel groupsModel;
    static List<GroupsModel> groupsModelList;
    List<MembersModel> membersModelList;
    Context context;
    String SocietyId;
    List<MembersModel> sharevent;
    MembersModel membersModel;
    String textPollTitle,textPollQuestion,eventDate2,textOption1,textOption2,textOption3,textOption4,textOption5,textOption6,availEndDate,availEndDate1,availEndDate12;
    String pollImage="";
    String memberId,memberName,email,password,mobile,gender,address,landmark,city,state,pincode,profile_image,device_id,device_type,device_token,status,date_created,date_modified,cover_image,profile_image_url;
    ShareeventModel shareeventModel;
    AppPreferences appPreferences;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    String groupId,groupSocietyId,groupName,groupImage,groupStatus,groupCreatedBy,groupCreatedOn,groupImageUrl,groupUserName;
    public static List<String> shareList;

    EditText searchSociety;
    TextView title;

    public boolean chechSearchVisiblity=false;

    LinearLayout listOfMember,groups;

    RelativeLayout notification;
    LinearLayout home,search;
    public static LinearLayout prPublish;
    String preview,event,poll,createPoll;
    TextView nomembers;
    public static int groupMemberCheck;
    int slecthour,selectminuite;
    String eventImage,eventTitleText,eventDate1,evenTexttDate,eventTextTime,evenTextDescription,evenTextAddress,evenTextLandmark,evenTextState,evenTextCity,evenTextPincode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        setContentView(R.layout.activity_share_event);
        context = ShareEvent.this;
        appPreferences=AppPreferences.getAppPreferences(context);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        preview=getIntent().getStringExtra("preview");
        event=getIntent().getStringExtra("event");
        createPoll=getIntent().getStringExtra("createPoll");
        poll=getIntent().getStringExtra("poll");
        SocietyId=getIntent().getStringExtra("SocietyId");

        LoggerGeneral.info("Society id on shareEvent"+SocietyId+"======="+availEndDate+"==="+availEndDate1);

        textPollTitle=getIntent().getStringExtra("poll_title");

        textPollQuestion=getIntent().getStringExtra("poll_question");

        textOption1=getIntent().getStringExtra("option1");

        textOption2=getIntent().getStringExtra("option2");

        textOption3=getIntent().getStringExtra("option3");

        textOption4=getIntent().getStringExtra("option4");

        textOption5=getIntent().getStringExtra("option5");

        textOption6=getIntent().getStringExtra("option6");

        availEndDate=getIntent().getStringExtra("endDate");

        availEndDate1=getIntent().getStringExtra("endDate1");

        pollImage=getIntent().getStringExtra("pollImage");

        availEndDate12=getIntent().getStringExtra("toShow");

        eventTitleText= getIntent().getStringExtra("eventTitle");
        evenTexttDate=getIntent().getStringExtra("eventDate");
        eventTextTime=getIntent().getStringExtra("eventTime");
        evenTextDescription=getIntent().getStringExtra("eventdescription");
        evenTextAddress=getIntent().getStringExtra("eventAddress");
        evenTextLandmark=getIntent().getStringExtra("eventLandmark");
        evenTextState=getIntent().getStringExtra("eventState");
        evenTextCity=getIntent().getStringExtra("eventCity");
        evenTextPincode=getIntent().getStringExtra("eventPincode");
        eventImage=getIntent().getStringExtra("eventImage");
        slecthour=getIntent().getIntExtra("eventHour", 0);
        selectminuite=getIntent().getIntExtra("eventMiniute",0);
        eventDate1=getIntent().getStringExtra("eventTimeToDet");
        eventDate2=getIntent().getStringExtra("sendApi");


        LoggerGeneral.info("Society id on shareEvent"+SocietyId+"======="+availEndDate+"==="+availEndDate1);

        title=((TextView) toolbar.findViewById(R.id.toolbar_title));

        if(textPollTitle!=null){
            title.setText("Share Poll");
        }
        else{
            title.setText("Share Event");
        }
        nomembers= (TextView) findViewById(R.id.nomembers);

        //title.setText("Share Event");
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
                    adapter = new ShareeventAdapter(context, sharevent);

                    recyclerView.setAdapter(adapter);

                    adapter.notifyDataSetChanged();

                }
                else {
                    if(event!=null) {
                        Intent back = new Intent(ShareEvent.this, EventDetails.class);
                        back.putExtra("preview", preview);
                        back.putExtra("preview",preview);
                        back.putExtra("event","event");
                        back.putExtra("preview","preview");
                        back.putExtra("SocietyId",SocietyId);
                        back.putExtra("eventTitle",eventTitleText);
                        back.putExtra("eventDate",evenTexttDate);
                        back.putExtra("eventTime",eventTextTime);
                        back.putExtra("eventdescription",evenTextDescription);
                        back.putExtra("eventAddress",evenTextAddress);
                        back.putExtra("eventLandmark",evenTextLandmark);
                        back.putExtra("eventState",evenTextState);
                        back.putExtra("eventCity",evenTextCity);
                        back.putExtra("eventPincode",evenTextPincode);
                        back.putExtra("eventHour",slecthour);
                        back.putExtra("eventMiniute",selectminuite);
                        back.putExtra("eventTimeToDet",eventDate1);
                        back.putExtra("sendApi",eventDate2);
                        back.putExtra("eventImage",eventImage);
                        startActivity(back);
                        finish();
                    }
                    if(poll!=null)
                    {
                        Intent back = new Intent(ShareEvent.this, PollDetails.class);
                        back.putExtra("createPoll", createPoll);
                        back.putExtra("poll_title",textPollTitle);
                        back.putExtra("poll_question",textPollQuestion);
                        back.putExtra("endDate",availEndDate);
                        back.putExtra("endDate1",availEndDate1);
                        back.putExtra("toShow",availEndDate12);
                        back.putExtra("option1",textOption1);
                        back.putExtra("option2",textOption2);
                        back.putExtra("pollImage",pollImage);
                        back.putExtra("SocietyId",SocietyId);
                        if(textOption3!=null && textOption3.length()>0)
                        {
                            back.putExtra("option3",textOption3);
                        }
                        if(textOption4!=null && textOption4.length()>0)
                        {
                            back.putExtra("option4",textOption4);
                        }
                        if(textOption5!=null && textOption5.length()>0)
                        {
                            back.putExtra("option5",textOption5);
                        }
                        if(textOption6!=null && textOption6.length()>0)
                        {
                            back.putExtra("option6",textOption6);
                        }
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
                Intent notification=new Intent(ShareEvent.this,NotificationApp.class);
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
                        adapter = new ShareeventAdapter(context, filteredList);
                        recyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();  // data set changed
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
                Intent myProperty=new Intent(ShareEvent.this,MyProperty.class);
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
                        Intent profile = new Intent(ShareEvent.this, Profile.class);
                        profile.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(profile);
                        finish();
                        break;
                    case 2:
                        LoggerGeneral.info("2");
                        Intent addSociety = new Intent(ShareEvent.this, AddSociety.class);
                        startActivity(addSociety);
                        finish();
                        break;
                    case 3:
                        LoggerGeneral.info("3");
                        Intent myProperty = new Intent(ShareEvent.this, MyProperty.class);
                        PropertyAdapter.intentCheck = 0;
                        startActivity(myProperty);
                        finish();
                        break;
                    case 4:
                        LoggerGeneral.info("4");
                        Intent events = new Intent(ShareEvent.this, DashBoard.class);
                        events.putExtra("event", 3);
                        startActivity(events);
                        finish();
                        break;
                    case 5:
                        LoggerGeneral.info("5");
                        Intent createEvents = new Intent(ShareEvent.this, CreateEvent.class);
                        startActivity(createEvents);
                        finish();
                        break;
                    case 6:
                        LoggerGeneral.info("6");
                        Intent archivedEvents = new Intent(ShareEvent.this, Archivedevent.class);
                        startActivity(archivedEvents);
                        finish();
                        break;
                    case 7:
                        LoggerGeneral.info("7");
                        Intent announcement = new Intent(ShareEvent.this, DashBoard.class);
                        announcement.putExtra("announcement", 4);
                        startActivity(announcement);
                        finish();
                        break;
                    case 8:
                        LoggerGeneral.info("8");
                        Intent createAnnouncement = new Intent(ShareEvent.this, CreateAnnouncement.class);
                        startActivity(createAnnouncement);
                        finish();
                        break;
                    case 9:
                        LoggerGeneral.info("9");
                        Intent polls = new Intent(ShareEvent.this, DashBoard.class);
                        polls.putExtra("polls", 2);
                        startActivity(polls);
                        finish();
                        break;
                    case 10:
                        LoggerGeneral.info("10");
                        Intent createEventPoll = new Intent(ShareEvent.this, CreatePoll.class);
                        startActivity(createEventPoll);
                        finish();
                        break;
                    case 11:
                        LoggerGeneral.info("11");
                        Intent archivedEventPoll = new Intent(ShareEvent.this, ArchivedPolls.class);
                        startActivity(archivedEventPoll);
                        finish();
                        break;
                    case 12:
                        LoggerGeneral.info("12");
                        Intent members = new Intent(ShareEvent.this, Members.class);
                        startActivity(members);
                        finish();
                        break;
                    case 13:
                        LoggerGeneral.info("13");
                        Intent offers = new Intent(ShareEvent.this, Offer.class);
                        startActivity(offers);
                        finish();
                        break;
                    case 14:
                        LoggerGeneral.info("14");
                        Intent complaint = new Intent(ShareEvent.this, Complaint.class);
                        startActivity(complaint);
                        finish();
                        break;
                    case 15:
                        LoggerGeneral.info("15");
                        Intent notification=new Intent(ShareEvent.this,NotificationApp.class);
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
                      /*  Intent intent = new Intent(ShareEvent.this, Login.class);
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
        groupsModelList = new ArrayList<GroupsModel>();

        sharevent = new ArrayList<MembersModel>();

        /*for(int i = 0;i<=20;i++){
            shareeventModel = new ShareeventModel();
            shareeventModel.setName("Rakesh Mourya");
            shareeventModel.setIsImageChanged(false);
            sharevent.add(shareeventModel);
//            societyModel.setSocimg(String.valueOf(R.mipmap.background_material));



        }*/







        if(Common.isOnline(context)){

            new getMyMembers().execute();
        }
        else {
            Common.showToast(context,"No internet connection!");
        }





       adapter = new ShareeventAdapter(context, sharevent);

        recyclerView.setAdapter(adapter);

        adapter.notifyDataSetChanged();

        prPublish= (LinearLayout) findViewById(R.id.prpublish);

        listOfMember= (LinearLayout) findViewById(R.id.listOfMembers);

        groups= (LinearLayout) findViewById(R.id.groups);

        listOfMember.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                GroupAdapter.check=0;
                final int sdk = android.os.Build.VERSION.SDK_INT;
                if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    ShareEvent.prPublish.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.fourborderlightgray));
                } else {
                    ShareEvent.prPublish.setBackground(context.getResources().getDrawable(R.drawable.fourborderlightgray));
                }

                if(Common.isOnline(context)){

                    new getMyMembers().execute();
                }
                else {
                    Common.showToast(context,"No internet connection!");
                }

            }
        });

        groups.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                ShareeventAdapter.check=0;
                final int sdk = android.os.Build.VERSION.SDK_INT;
                if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    ShareEvent.prPublish.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.fourborderlightgray));
                } else {
                    ShareEvent.prPublish.setBackground(context.getResources().getDrawable(R.drawable.fourborderlightgray));
                }
                groupsModel = new GroupsModel();
                /*for(int i = 0;i<=10;i++){

                    groupsModel = new GroupsModel();
                    groupsModel.setGroupName("Jhon Doe"+i);
                    groupsModel.setDescription("Member");
                    groupsModel.setId(i);
                    groupsModelList.add(groupsModel);
//            societyModel.setSocimg(String.valueOf(R.mipmap.background_material));
                    LoggerGeneral.info("showsoc" +groupsModel.getGroupName() + "---" + groupsModelList.get(i).getGroupName());


                }

                adapter1 = new GroupAdapter(context, groupsModelList,0);

                recyclerView.setAdapter(adapter1);

                adapter1.notifyDataSetChanged();*/
                if(Common.isOnline(context)){

                    new getMyGroup().execute();
                }
                else {
                    Common.showToast(context,"No internet connection!");
                }
            }
        });

        prPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (createPoll != null) {
                    if (ShareeventAdapter.check != 0) {

                        LoggerGeneral.info("memebereid" + shareList.size() + "-----");

                        if (Common.isOnline(context)) {

                            new createPoll().execute();
                        } else {
                            Common.showToast(context, "No internet connection!");
                        }



                    /*if (event != null) {
                        Intent events = new Intent(ShareEvent.this, DashBoard.class);
                        events.putExtra("event", 3);
                        startActivity(events);
                        finish();
                    }
                    if (poll != null) {
                        Intent polls = new Intent(ShareEvent.this, DashBoard.class);
                        polls.putExtra("polls", 2);
                        startActivity(polls);
                        finish();
                    }*/
                    } else if (GroupAdapter.check != 0) {

                        LoggerGeneral.info("memebereid" + shareList.size() + "-----");

                        if (Common.isOnline(context)) {

                            new createPoll().execute();
                        } else {
                            Common.showToast(context, "No internet connection!");
                        }
                    } else {
                        Common.showToast(context, "Please select Minimum 1 member");
                    }
                }
                if (event != null) {
                    if (ShareeventAdapter.check != 0) {

                        LoggerGeneral.info("memebereid" + shareList.size() + "-----");

                        if (Common.isOnline(context)) {

                            new createEvent().execute();
                        } else {
                            Common.showToast(context, "No internet connection!");
                        }



                    /*if (event != null) {
                        Intent events = new Intent(ShareEvent.this, DashBoard.class);
                        events.putExtra("event", 3);
                        startActivity(events);
                        finish();
                    }
                    if (poll != null) {
                        Intent polls = new Intent(ShareEvent.this, DashBoard.class);
                        polls.putExtra("polls", 2);
                        startActivity(polls);
                        finish();
                    }*/
                    } else if (GroupAdapter.check != 0) {

                        LoggerGeneral.info("memebereid" + shareList.size() + "-----");

                        if (Common.isOnline(context)) {

                            new createEvent().execute();
                        } else {
                            Common.showToast(context, "No internet connection!");
                        }
                    } else {
                        Common.showToast(context, "Please select Minimum 1 member");
                    }
                }
            }

        });


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

            mDialog = new ProgressDialog(ShareEvent.this,ProgressDialog.THEME_HOLO_DARK);
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
                        Intent register   = new Intent(ShareEvent.this,Login.class);
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
        else if(chechSearchVisiblity==true)
        {

            searchSociety.setVisibility(View.GONE);
            searchSociety.setText("");
            title.setVisibility(View.VISIBLE);
            home.setVisibility(View.VISIBLE);
            notification.setVisibility(View.VISIBLE);
            search.setVisibility(View.VISIBLE);
            chechSearchVisiblity=false;
            adapter = new ShareeventAdapter(this, sharevent);

            recyclerView.setAdapter(adapter);

            adapter.notifyDataSetChanged();

            return;
        }
        else {
            super.onBackPressed();
            if(event!=null) {
                Intent back = new Intent(ShareEvent.this, EventDetails.class);
                back.putExtra("preview", preview);
                back.putExtra("preview",preview);
                back.putExtra("event","event");
                back.putExtra("preview","preview");
                back.putExtra("SocietyId",SocietyId);
                back.putExtra("eventTitle",eventTitleText);
                back.putExtra("eventDate",evenTexttDate);
                back.putExtra("eventTime",eventTextTime);
                back.putExtra("eventdescription",evenTextDescription);
                back.putExtra("eventAddress",evenTextAddress);
                back.putExtra("eventLandmark",evenTextLandmark);
                back.putExtra("eventState",evenTextState);
                back.putExtra("eventCity",evenTextCity);
                back.putExtra("eventPincode",evenTextPincode);
                back.putExtra("eventHour",slecthour);
                back.putExtra("eventMiniute",selectminuite);
                back.putExtra("eventTimeToDet",eventDate1);
                back.putExtra("sendApi",eventDate2);
                back.putExtra("eventImage",eventImage);
                startActivity(back);
                finish();
            }
            if(poll!=null)
            {
                Intent back = new Intent(ShareEvent.this, PollDetails.class);
                back.putExtra("createPoll", createPoll);
                back.putExtra("poll_title",textPollTitle);
                back.putExtra("poll_question",textPollQuestion);
                back.putExtra("endDate",availEndDate);
                back.putExtra("endDate1",availEndDate1);
                back.putExtra("toShow",availEndDate12);
                back.putExtra("option1",textOption1);
                back.putExtra("option2",textOption2);
                back.putExtra("pollImage",pollImage);
                back.putExtra("SocietyId",SocietyId);
                if(textOption3!=null && textOption3.length()>0)
                {
                    back.putExtra("option3",textOption3);
                }
                if(textOption4!=null && textOption4.length()>0)
                {
                    back.putExtra("option4",textOption4);
                }
                if(textOption5!=null && textOption5.length()>0)
                {
                    back.putExtra("option5",textOption5);
                }
                if(textOption6!=null && textOption6.length()>0)
                {
                    back.putExtra("option6",textOption6);
                }
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

                LoggerGeneral.info("JsonObjectPrint" + object.toString());

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

                                memberId = jsonObject1.getString("user_id");
                                memberName = jsonObject1.getString("name");
                                profile_image = jsonObject1.getString("profile_image");
                                profile_image_url = jsonObject1.getString("profile_image_url");


                                JSONArray property_name_array = jsonObject1.getJSONArray("property_name_array");

                                ArrayList<String> property_name = new ArrayList<String>();

                                for (int x = 0; x < property_name_array.length(); x++) {
                                    property_name.add(property_name_array.getString(x));
                                }

                                JSONArray property_type_array = jsonObject1.getJSONArray("property_type_array");

                                ArrayList<String> property_type = new ArrayList<String>();

                                for (int x = 0; x < property_type_array.length(); x++) {
                                    property_type.add(property_type_array.getString(x));
                                }

                                JSONArray house_no_array = jsonObject1.getJSONArray("house_no_array");

                                ArrayList<String> house_no = new ArrayList<String>();

                                for (int x = 0; x < house_no_array.length(); x++) {
                                    house_no.add(house_no_array.getString(x));
                                }


                                for (int x = 0; x < house_no.size(); x++) {
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


                            }

                                if(!memberId.equals(appPreferences.getString("user_id",""))){

                                    membersModelList.add(membersModel);
                                }

//                                membersModelList.add(membersModel);


                                LoggerGeneral.info("s_societies000---" + membersModelList.size());

                            if(membersModelList.size()==0)
                            {
                                nomembers.setText("No members added");
                                nomembers.setVisibility(View.VISIBLE);
                            }
                            nomembers.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                            LoggerGeneral.info("s_societies---" + membersModelList);
                            adapter=new ShareeventAdapter(context,membersModelList);
                            recyclerView.setAdapter(adapter);

                            adapter.notifyDataSetChanged();


                        }

                        if (status_code == 1) {

                            nomembers.setText("No members added");
                            nomembers.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);

                        }
                    }

                    if(account_status==0){
                        Intent intent = new Intent(context,Login.class);
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

    class  createPoll extends AsyncTask<String, String, JSONObject> {
        String type;
        ProgressDialog mDialog;
        @Override
        protected JSONObject doInBackground(String... params) {
            // TODO Auto-generated method stub

            String url= Constants.Base+Constants.publishPoll;

            JSONObject object = new JSONObject();
            try {

                object.put("society_id",SocietyId);
                object.put("user_id",appPreferences.getString("user_id",""));
                object.put("poll_title",textPollTitle);
                object.put("poll_question",textPollQuestion);
                object.put("shared_with","private");
                object.put("poll_end_date",availEndDate1);
                object.put("poll_option_1",textOption1);
                object.put("poll_option_2", textOption2);
                if(textOption3!=null) {
                    object.put("poll_option_3",textOption3);
                }
                if(textOption4!=null) {
                    object.put("poll_option_4",textOption4);
                }

                if(textOption5!=null) {
                    object.put("poll_option_5",textOption5);
                }
                if(textOption6!=null) {
                    object.put("poll_option_6",textOption6);
                }

                object.put("poll_image",pollImage);
                object.put("image_extension","jpg");

                List<String> indexList=new ArrayList<String>();


                JSONObject privateList=new JSONObject();

                for(int i=0;i<=shareList.size()-1;i++)
                {

                    privateList.put(String.valueOf(i),shareList.get(i));
                }

                if(groupMemberCheck==1) {

                    object.put("private_users", privateList);
                }

                if(groupMemberCheck==0)
                {
                    object.put("private_group", privateList);
                }

                //      object.put("property_id",property_id);


                LoggerGeneral.info("JsonObjectPrintAddPoll" + object.toString());

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

            mDialog = new ProgressDialog(ShareEvent.this,ProgressDialog.THEME_HOLO_DARK);
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



                            if (poll != null) {
                                Intent polls = new Intent(ShareEvent.this, DashBoard.class);
                                polls.putExtra("polls", 2);
                                polls.putExtra("SocietyId",SocietyId);
                                startActivity(polls);
                                finish();
                            }


                         /* JSONObject data = results.getJSONObject("data");

                            String property_id = data.getString("property_id");

                            appPreferences.putString("property_id",property_id);*/

                        }

                    }

                    if(account_status == 0) {
                        Intent intent = new Intent(ShareEvent.this,Login.class);
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
                mDialog.dismiss();


            }
        }

    }


    class  getMyGroup extends AsyncTask<String, String, JSONObject> {
        String type;
        ProgressDialog mDialog;
        @Override
        protected JSONObject doInBackground(String... params) {
            // TODO Auto-generated method stub

            String url= Constants.Base+Constants.getGroups;

            JSONObject object = new JSONObject();
            try {

                // object.put("user_id",appPreferences.getString("user_id",""));
                // object.put("user_id",appPreferences.getString("user_id",""));

                object.put("society_id",SocietyId);

                object.put("user_id",appPreferences.getString("user_id",""));

                LoggerGeneral.info("JsonObjectPrint" + object.toString());

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
                            groupsModelList = new ArrayList<GroupsModel>();
                            for (int i = 0; i <= data.length() - 1; i++) {

                                groupsModel = new GroupsModel();

                                JSONObject jsonObject1 = data.getJSONObject(i);

                                groupId=jsonObject1.getString("id");
                                groupSocietyId=jsonObject1.getString("society_id");
                                groupName=jsonObject1.getString("group_name");
                                groupImage=jsonObject1.getString("group_image");
                                groupStatus=jsonObject1.getString("status");
                                groupCreatedBy=jsonObject1.getString("created_by");
                                groupCreatedOn=jsonObject1.getString("created_on");
                                groupImageUrl=jsonObject1.getString("group_image_url");
                                groupUserName=jsonObject1.getString("user_name");

                                groupsModel.setIdd(i);
                                groupsModel.setGroupId(groupId);
                                groupsModel.setGroupSocietyId(groupSocietyId);
                                groupsModel.setGroupName(groupName);
                                groupsModel.setGroupImage(groupImage);
                                groupsModel.setGroupStatus(groupStatus);
                                groupsModel.setGroupCreatedBy(groupCreatedBy);
                                groupsModel.setGroupCreatedOn(groupCreatedOn);
                                groupsModel.setGroupImageUrl(groupImageUrl);
                                groupsModel.setGroupUserName(groupUserName);

                                groupsModelList.add(groupsModel);


                                LoggerGeneral.info("s_societies000---" + groupsModelList.size());

                            }

                            if(groupsModelList.size()==0)
                            {
                                nomembers.setText("No members added");
                                nomembers.setVisibility(View.VISIBLE);
                            }
                            nomembers.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                            LoggerGeneral.info("s_societies---" + groupsModelList);
                            adapter1=new GroupAdapter(context,groupsModelList,0,SocietyId);
                            recyclerView.setAdapter(adapter1);

                            adapter.notifyDataSetChanged();


                        }

                        if (status_code == 1) {

                        nomembers.setText("No groups added");
                        nomembers.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);



                        }
                    }

                    if(account_status==0){
                        Intent intent = new Intent(context,Login.class);
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


    class  createEvent extends AsyncTask<String, String, JSONObject> {
        String type;
        ProgressDialog mDialog;
        @Override
        protected JSONObject doInBackground(String... params) {
            // TODO Auto-generated method stub

            String url= Constants.Base+Constants.createEvent;

            JSONObject object = new JSONObject();
            try {

                object.put("society_id",SocietyId);
                object.put("user_id",appPreferences.getString("user_id",""));
                object.put("title",eventTitleText);
                object.put("event_date",eventDate2+","+eventTextTime);
                object.put("description",evenTextDescription);
                object.put("event_image",eventImage);
                object.put("image_extension","jpg");
                object.put("address",evenTextAddress);
                object.put("landmark",evenTextLandmark);
                object.put("state",evenTextState);
                object.put("city",evenTextCity);
                object.put("postal_code",evenTextPincode);
                object.put("shared_with","private");


                JSONObject privateList=new JSONObject();

                for(int i=0;i<=shareList.size()-1;i++)
                {

                    privateList.put(String.valueOf(i),shareList.get(i));
                }

                if(groupMemberCheck==1) {

                    object.put("private_users", privateList);
                }

                if(groupMemberCheck==0)
                {
                    object.put("private_group", privateList);
                }


                //      object.put("property_id",property_id);


                LoggerGeneral.info("JsonObjectPrintAddPoll" + object.toString());

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

            mDialog = new ProgressDialog(ShareEvent.this,ProgressDialog.THEME_HOLO_DARK);
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
                            Common.showToast(context, "Event created successfully");
                            Intent event = new Intent(ShareEvent.this, DashBoard.class);
                            event.putExtra("event",3);
                            event.putExtra("SocietyId",SocietyId);
                            startActivity(event);
                            finish();


                         /* JSONObject data = results.getJSONObject("data");

                            String property_id = data.getString("property_id");

                            appPreferences.putString("property_id",property_id);*/

                        }

                    }

                    if(account_status == 0) {
                        Intent intent = new Intent(ShareEvent.this,Login.class);
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
                mDialog.dismiss();


            }
        }

    }




}


