package com.inkswipe.SocialSociety;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.flurry.android.FlurryAgent;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import adapter.CustomAdapter;
import adapter.PropertyAdapter;
import model.DrawerList;
import util.AppPreferences;
import util.Common;
import util.Constants;
import util.ExceptionHandler;
import util.LoggerGeneral;
import util.ServiceFacade;

public class EventDetails extends AppCompatActivity {

    LinearLayout home;
    Fragment fragment = null;
    TextView appname;
    LinearLayout shareevent,publishEvent,deleteEvent,bottomLayout1,bottomLayout2,acceptReject,accept,reject,peopleAccepted,peopleRejected;

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;

    LinearLayout archiveEvent;

    RelativeLayout notification;
    String preview,eventList,creator;
    String SocietyId;
    String eventCreator;
    int slecthour,selectminuite;
    String eventImage,eventTitleText,eventDate1,eventDate2,evenTexttDate,eventTextTime,evenTextDescription,evenTextAddress,evenTextLandmark,evenTextState,evenTextCity,evenTextPincode;
    String eventId,eventSocietyId;
    ImageView eventImageView;
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    TextView eventNameTextView,eventCreatorNameTextView,eventDateTextView,eventDescriptionTextView,eventAddressTextView,eventTime;
    AppPreferences appPreferences;
    String eventAcceptStatus;
    Context context;
    private DisplayImageOptions options;
    String event_id;
    String dateofevent,archiveEventcheck;
    public static Bundle temp_bundle;
    int savedInstanceCheck=0;
    String event;
    String time;
    String checkclickAccept;
    String fromNotification;
    String SocietyId1;
    String fromPushNotification;
    String checkDate;
    String event_time;
   // String event_status;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (getIntent().hasExtra("bundle") && savedInstanceState==null){
            LoggerGeneral.info("isItRight");
            savedInstanceCheck=1;
            savedInstanceState = getIntent().getExtras().getBundle("bundle");
        }
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        setContentView(R.layout.activity_event_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context=EventDetails.this;
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.cover_image)
                .showImageForEmptyUri(R.mipmap.cover_image)
                .showImageOnFail(R.mipmap.cover_image)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .displayer(new SimpleBitmapDisplayer())
                .build();
        appPreferences=AppPreferences.getAppPreferences(this);
        preview=getIntent().getStringExtra("preview");
        eventList=getIntent().getStringExtra("eventList");
        creator=getIntent().getStringExtra("creator");

        dateofevent = getIntent().getStringExtra("dateofevent");
        event_id =getIntent().getStringExtra("event_id");
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
        selectminuite=getIntent().getIntExtra("eventMiniute", 0);
        eventDate1=getIntent().getStringExtra("eventTimeToDet");
        archiveEventcheck=getIntent().getStringExtra("archiveEvent");
        eventDate2=getIntent().getStringExtra("sendApi");
        event=getIntent().getStringExtra("event");
        time=getIntent().getStringExtra("time");
        SocietyId = getIntent().getStringExtra("SocietyId");
        fromNotification=getIntent().getStringExtra("fromNotification");
        SocietyId1=getIntent().getStringExtra("SocietyIdnotification");
        fromPushNotification=getIntent().getStringExtra("fromPushNotification");
       // event_status=getIntent().getStringExtra("eventstatus");
        LoggerGeneral.info("Event Detail Data"+eventTextTime+"==========="+eventImage);

        if(event_id!=null)
        {
            if(archiveEventcheck!=null)
            {
                Common.internet_check=16;
            }
            if(creator!=null) {
                Common.internet_check = 9;
            }
            if(eventList!=null)
            {
                Common.internet_check=10;
            }
        }

        if (savedInstanceCheck==1)
        {
            if(Common.internet_check==9) {
                SocietyId = savedInstanceState.getString("SocietyId");
                event_id=savedInstanceState.getString("event_id");
                creator=savedInstanceState.getString("creator");
                dateofevent=savedInstanceState.getString("dateofevent");
                event=savedInstanceState.getString("event");
                time=savedInstanceState.getString("time");
            }
            if(Common.internet_check==10) {
                SocietyId = savedInstanceState.getString("SocietyId");
                event_id=savedInstanceState.getString("event_id");
                eventList=savedInstanceState.getString("eventList");
                dateofevent=savedInstanceState.getString("dateofevent");
                event=savedInstanceState.getString("event");
                time=savedInstanceState.getString("time");
            }
            if(Common.internet_check==16)
            {
                SocietyId = savedInstanceState.getString("SocietyId");
                event_id=savedInstanceState.getString("event_id");
                creator=savedInstanceState.getString("creator");
                dateofevent=savedInstanceState.getString("dateofevent");
                archiveEventcheck=savedInstanceState.getString("archiveEvent");
                time=savedInstanceState.getString("time");
            }
            LoggerGeneral.info(SocietyId+"======"+event_id+"======"+creator+"======"+dateofevent+"======"+archiveEventcheck+"======" + eventList);
        }
        LoggerGeneral.info("hiiievent" + event + SocietyId + "======" + event_id + "======" + creator + "======" + dateofevent+"======"+archiveEventcheck+"======1212"+eventList);
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
        ImageLoader.getInstance().init(config);
        TextView title=((TextView) toolbar.findViewById(R.id.toolbar_title));
        title.setText("");
        toolbar.setTitleTextColor(android.graphics.Color.WHITE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.my_statusbar_color));
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

     //   toolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.backarrow));

        toolbar.getNavigationIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //What to do on back clicked
                if(mDrawerLayout.isDrawerOpen(GravityCompat.END))
                {
                    mDrawerLayout.closeDrawers();
                    return;
                }
                else if(fromPushNotification!=null)
                {
                    Intent notification=new Intent(EventDetails.this,MyProperty.class);
                    startActivity(notification);
                    finish();
                }
                else if(fromNotification!=null)
                {
                    Intent notification=new Intent(EventDetails.this,NotificationApp.class);
                    notification.putExtra("SocietyId",SocietyId1);
                    startActivity(notification);
                    finish();
                }

                else if(archiveEventcheck!=null) {
                    Intent back = new Intent(EventDetails.this, Archivedevent.class);
                    back.putExtra("SocietyId",SocietyId);
                    back.putExtra("dateofevent", dateofevent);
                    back.putExtra("event",event);
                    Common.internet_check=0;
                    startActivity(back);
                    finish();
                }
                else if(creator!=null || eventList!=null) {
                    Intent back = new Intent(EventDetails.this, EventsList.class);
                    back.putExtra("SocietyId",SocietyId);
                    back.putExtra("dateofevent",dateofevent);
                    Common.internet_check=0;
                    //back.putExtra("event","yesevent");
                    back.putExtra("event",event);
                    startActivity(back);
                    finish();
                }
                else if(preview!=null) {
                    Intent shareEvent = new Intent(EventDetails.this, CreateEvent.class);
                    shareEvent.putExtra("preview",preview);
                    shareEvent.putExtra("event","event");
                    shareEvent.putExtra("preview","preview");
                    shareEvent.putExtra("SocietyId",SocietyId);
                    shareEvent.putExtra("eventTitle",eventTitleText);
                    shareEvent.putExtra("eventDate",evenTexttDate);
                    shareEvent.putExtra("eventTime",eventTextTime);
                    shareEvent.putExtra("eventdescription",evenTextDescription);
                    shareEvent.putExtra("eventAddress",evenTextAddress);
                    shareEvent.putExtra("eventLandmark",evenTextLandmark);
                    shareEvent.putExtra("eventState",evenTextState);
                    shareEvent.putExtra("eventCity",evenTextCity);
                    shareEvent.putExtra("eventPincode",evenTextPincode);
                    shareEvent.putExtra("eventHour",slecthour);
                    shareEvent.putExtra("eventMiniute",selectminuite);
                    shareEvent.putExtra("eventTimeToDet",eventDate1);
                    shareEvent.putExtra("sendApi",eventDate2);
                    shareEvent.putExtra("eventImage",eventImage);
                    startActivity(shareEvent);
                    Common.internet_check=0;
                    finish();
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
                Intent notification=new Intent(EventDetails.this,NotificationApp.class);
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
                Intent myProperty=new Intent(EventDetails.this,MyProperty.class);
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

        eventImageView= (ImageView) findViewById(R.id.eventImage);

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
                        Intent profile = new Intent(EventDetails.this, Profile.class);
                        profile.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(profile);
                        Common.internet_check = 0;
                        finish();
                        break;
                    case 2:
                        LoggerGeneral.info("2");
                        Intent addSociety = new Intent(EventDetails.this, AddSociety.class);
                        Common.internet_check = 0;
                        startActivity(addSociety);
                        finish();
                        break;
                    case 3:
                        LoggerGeneral.info("3");
                        Intent myProperty = new Intent(EventDetails.this, MyProperty.class);
                        Common.internet_check = 0;
                        PropertyAdapter.intentCheck = 0;
                        startActivity(myProperty);
                        finish();
                        break;
                    case 4:
                        LoggerGeneral.info("4");
                        Intent events = new Intent(EventDetails.this, DashBoard.class);
                        Common.internet_check = 0;
                        events.putExtra("SocietyId", SocietyId);
                        events.putExtra("event", 3);
                        startActivity(events);
                        finish();
                        break;
                    case 5:
                        LoggerGeneral.info("5");
                        Intent createEvents = new Intent(EventDetails.this, CreateEvent.class);
                        Common.internet_check = 0;
                        createEvents.putExtra("SocietyId", SocietyId);
                        startActivity(createEvents);
                        finish();
                        break;
                    case 6:
                        LoggerGeneral.info("6");
                        Intent archivedEvents = new Intent(EventDetails.this, Archivedevent.class);
                        Common.internet_check = 0;
                        archivedEvents.putExtra("SocietyId", SocietyId);
                        startActivity(archivedEvents);
                        finish();
                        break;
                    case 7:
                        LoggerGeneral.info("7");
                        Intent announcement = new Intent(EventDetails.this, DashBoard.class);
                        Common.internet_check = 0;
                        announcement.putExtra("SocietyId", SocietyId);
                        announcement.putExtra("announcement", 4);
                        startActivity(announcement);
                        finish();
                        break;
                    case 8:
                        LoggerGeneral.info("8");
                        Intent createAnnouncement = new Intent(EventDetails.this, CreateAnnouncement.class);
                        Common.internet_check = 0;
                        createAnnouncement.putExtra("SocietyId", SocietyId);
                        startActivity(createAnnouncement);
                        finish();
                        break;
                    case 9:
                        LoggerGeneral.info("9");
                        Intent polls = new Intent(EventDetails.this, DashBoard.class);
                        Common.internet_check = 0;
                        polls.putExtra("SocietyId", SocietyId);
                        polls.putExtra("polls", 2);
                        startActivity(polls);
                        finish();
                        break;
                    case 10:
                        LoggerGeneral.info("10");
                        Intent createEventPoll = new Intent(EventDetails.this, CreatePoll.class);
                        Common.internet_check = 0;
                        createEventPoll.putExtra("SocietyId", SocietyId);
                        startActivity(createEventPoll);
                        finish();
                        break;
                    case 11:
                        LoggerGeneral.info("11");
                        Intent archivedEventPoll = new Intent(EventDetails.this, ArchivedPolls.class);
                        Common.internet_check = 0;
                        archivedEventPoll.putExtra("SocietyId", SocietyId);
                        startActivity(archivedEventPoll);
                        finish();
                        break;
                    case 12:
                        LoggerGeneral.info("12");
                        Intent members = new Intent(EventDetails.this, Members.class);
                        Common.internet_check = 0;
                        members.putExtra("SocietyId", SocietyId);
                        startActivity(members);
                        finish();
                        break;
                    case 13:
                        LoggerGeneral.info("13");
                        Intent offers = new Intent(EventDetails.this, Offer.class);
                        Common.internet_check = 0;
                        offers.putExtra("SocietyId", SocietyId);
                        startActivity(offers);
                        finish();
                        break;
                    case 14:
                        LoggerGeneral.info("14");
                        Intent complaint = new Intent(EventDetails.this, Complaint.class);
                        Common.internet_check = 0;
                        complaint.putExtra("SocietyId", SocietyId);
                        startActivity(complaint);
                        finish();
                        break;
                    case 15:
                        LoggerGeneral.info("15");
                        Intent notification = new Intent(EventDetails.this, NotificationApp.class);
                        Common.internet_check = 0;
                        //    notification.putExtra("SocietyId",Soci)
                        int test1 = appPreferences.getInt("NotificationOld", 0);
                        test1 = test1+Constants.notififcationcount;
                        appPreferences.putInt("NotificationOld", test1);
                        notification.putExtra("SocietyId", SocietyId);
                        Constants.notififcationcount = 0;
                        startActivity(notification);
                        finish();
                        break;
                    case 16:
                       /* LoggerGeneral.info("16");
                        Intent intent = new Intent(EventDetails.this, Login.class);
                        Common.internet_check = 0;
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
        archiveEvent= (LinearLayout) findViewById(R.id.archiveEvent);
        shareevent= (LinearLayout) findViewById(R.id.shareevent);
        publishEvent= (LinearLayout) findViewById(R.id.publishevent);
        deleteEvent= (LinearLayout) findViewById(R.id.deleteEvent);
        bottomLayout1= (LinearLayout) findViewById(R.id.bottomLayout1);
        bottomLayout2= (LinearLayout) findViewById(R.id.bottomLayout2);
        acceptReject= (LinearLayout) findViewById(R.id.acceptReject);
        accept= (LinearLayout) findViewById(R.id.accept);
        reject= (LinearLayout) findViewById(R.id.reject);
        peopleAccepted= (LinearLayout) findViewById(R.id.peopleAccepted);
        peopleRejected= (LinearLayout) findViewById(R.id.peopleRejected);

        eventNameTextView= (TextView) findViewById(R.id.eventname);

        eventCreatorNameTextView= (TextView) findViewById(R.id.createdby);

        eventDateTextView= (TextView) findViewById(R.id.date);

        eventDescriptionTextView= (TextView) findViewById(R.id.description);

        eventAddressTextView= (TextView) findViewById(R.id.addresstxt);
        eventTime= (TextView) findViewById(R.id.time);

        if(creator!=null)
        {
            archiveEvent.setVisibility(View.GONE);
            shareevent.setVisibility(View.GONE);
            publishEvent.setVisibility(View.GONE);
            deleteEvent.setVisibility(View.VISIBLE);
            bottomLayout1.setVisibility(View.GONE);
            bottomLayout2.setVisibility(View.VISIBLE);
            acceptReject.setVisibility(View.GONE);

            if(Common.isOnline(context)){

                new  getEventDetails().execute();
            }
            else {
                Common.showToast(context,"No interent connection");
            }
        }

        if(archiveEventcheck!=null)
        {
            archiveEvent.setVisibility(View.GONE);
            shareevent.setVisibility(View.GONE);
            publishEvent.setVisibility(View.GONE);
            deleteEvent.setVisibility(View.GONE);
            bottomLayout1.setVisibility(View.GONE);
            bottomLayout2.setVisibility(View.GONE);
            acceptReject.setVisibility(View.GONE);

            if(Common.isOnline(context)){

                new  getEventDetails().execute();
            }
            else {
                Common.showToast(context,"No interent connection");
            }
        }

        if(preview!=null)
        {
            archiveEvent.setVisibility(View.GONE);
            shareevent.setVisibility(View.VISIBLE);
            publishEvent.setVisibility(View.VISIBLE);
            deleteEvent.setVisibility(View.GONE);
            bottomLayout1.setVisibility(View.GONE);
            bottomLayout2.setVisibility(View.GONE);
            acceptReject.setVisibility(View.GONE);

            eventNameTextView.setText(eventTitleText);

            if(eventImage!=null) {
                if(eventImage.length()>0) {
                    byte[] decodedString = Base64.decode(eventImage, Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    eventImageView.setImageBitmap(decodedByte);
                }
                else {
                    eventImageView.setVisibility(View.GONE);
                }
            }
            else {
                eventImageView.setVisibility(View.GONE);
            }

            eventCreatorNameTextView.setText(appPreferences.getString("user_name", ""));
            eventDateTextView.setText(eventDate1);
            if(slecthour>12)
            {

                eventTime.setText((slecthour-12)+":"+selectminuite+"PM");
            }

            else {
                eventTime.setText(slecthour +":"+selectminuite+"AM");
            }

            eventDescriptionTextView.setText(evenTextDescription);

            eventAddressTextView.setText(evenTextAddress + ", " + evenTextLandmark + ", " + evenTextCity + "\n" + evenTextState + "-" + evenTextPincode);

        }

        if(eventList!=null)
        {
            archiveEvent.setVisibility(View.GONE);
            shareevent.setVisibility(View.GONE);
            publishEvent.setVisibility(View.GONE);
            deleteEvent.setVisibility(View.GONE);
            bottomLayout1.setVisibility(View.GONE);
            bottomLayout2.setVisibility(View.GONE);
            acceptReject.setVisibility(View.VISIBLE);

            if(Common.isOnline(context)){

                new  getEventDetails().execute();
            }
            else {
                Common.showToast(context,"No interent connection");
            }




        }

        if(fromNotification!=null)
        {
            archiveEvent.setVisibility(View.GONE);
            shareevent.setVisibility(View.GONE);
            publishEvent.setVisibility(View.GONE);
            deleteEvent.setVisibility(View.GONE);
            bottomLayout1.setVisibility(View.GONE);
            bottomLayout2.setVisibility(View.GONE);
            acceptReject.setVisibility(View.VISIBLE);

            if(Common.isOnline(context)){

                new  getEventDetails().execute();
            }
            else {
                Common.showToast(context,"No interent connection");
            }




        }
        if(fromPushNotification!=null)
        {
            archiveEvent.setVisibility(View.GONE);
            shareevent.setVisibility(View.GONE);
            publishEvent.setVisibility(View.GONE);
            deleteEvent.setVisibility(View.GONE);
            bottomLayout1.setVisibility(View.GONE);
            bottomLayout2.setVisibility(View.GONE);
            acceptReject.setVisibility(View.VISIBLE);

            if(Common.isOnline(context)){

                new  getEventDetails().execute();
            }
            else {
                Common.showToast(context,"No interent connection");
            }



        }


        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
       //         acceptReject.setVisibility(View.GONE);

                if(Common.isOnline(context)){

                    new submitEvent("accepted").execute();
                    checkclickAccept="accepted";
                }
                else {

                }

            }
        });

        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
       //         acceptReject.setVisibility(View.GONE);

                if(Common.isOnline(context)){

                    new submitEvent("rejected").execute();
                    checkclickAccept="rejected";
                }
                else {

                }
            }
        });

        archiveEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent archiveEvent=new Intent(EventDetails.this,Archivedevent.class);
                Common.internet_check=0;
                startActivity(archiveEvent);
                finish();
            }
        });

        publishEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Common.isOnline(context)){

                    new  createEvent().execute();
                }
                else {
                    Common.showToast(context,"No interent connection");
                }


               /* Intent publishEvent = new Intent(EventDetails.this, DashBoard.class);
                publishEvent.putExtra("event",3);
                startActivity(publishEvent);
                finish();*/
            }
        });

        shareevent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareEvent = new Intent(EventDetails.this, ShareEvent.class);
                shareEvent.putExtra("preview",preview);
                shareEvent.putExtra("event","event");
                shareEvent.putExtra("preview","preview");
                shareEvent.putExtra("SocietyId",SocietyId);
                shareEvent.putExtra("eventTitle",eventTitleText);
                shareEvent.putExtra("eventDate",evenTexttDate);
                shareEvent.putExtra("eventTime",eventTextTime);
                shareEvent.putExtra("eventdescription",evenTextDescription);
                shareEvent.putExtra("eventAddress",evenTextAddress);
                shareEvent.putExtra("eventLandmark",evenTextLandmark);
                shareEvent.putExtra("eventState",evenTextState);
                shareEvent.putExtra("eventCity",evenTextCity);
                shareEvent.putExtra("eventPincode",evenTextPincode);
                shareEvent.putExtra("eventHour",slecthour);
                shareEvent.putExtra("eventMiniute",selectminuite);
                shareEvent.putExtra("eventTimeToDet",eventDate1);
                shareEvent.putExtra("sendApi",eventDate2);
                shareEvent.putExtra("eventImage",eventImage);
                Common.internet_check=0;
                startActivity(shareEvent);
                finish();

            }
        });

        peopleAccepted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent members=new Intent(EventDetails.this,Members.class);
                members.putExtra("accepted","accepted");
                members.putExtra("creator",creator);
                members.putExtra("event_id",event_id);
                members.putExtra("SocietyId",SocietyId);
                members.putExtra("dateofevent", dateofevent);
                members.putExtra("event","yesevent");
                Common.internet_check=0;
                startActivity(members);
                finish();
            }
        });

        peopleRejected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent members=new Intent(EventDetails.this,Members.class);
                members.putExtra("rejected","rejected");
                members.putExtra("creator",creator);
                members.putExtra("event_id",event_id);
                members.putExtra("SocietyId",SocietyId);
                members.putExtra("dateofevent", dateofevent);
                members.putExtra("event","yesevent");
                Common.internet_check=0;
                startActivity(members);
                finish();
            }
        });



        deleteEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(Common.isOnline(context)){

                   new deleteEvent().execute();

                }
                else {
                    Common.showToast(context,"No internet connection!");
                }
            }
        });


    //    registerReceiver(broadcast_reciever, new IntentFilter("finish_activity"));

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

            mDialog = new ProgressDialog(EventDetails.this,ProgressDialog.THEME_HOLO_DARK);
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
                        Intent register   = new Intent(EventDetails.this,Login.class);
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
        else if(fromPushNotification!=null)
        {
            super.onBackPressed();
            Intent notification=new Intent(EventDetails.this,MyProperty.class);
            startActivity(notification);
            finish();
        }
        else if(fromNotification!=null)
        {
            super.onBackPressed();
            Intent notification=new Intent(EventDetails.this,NotificationApp.class);
            notification.putExtra("Societyid",SocietyId1);
            startActivity(notification);
            finish();
        }
        else if(archiveEventcheck!=null) {
            super.onBackPressed();
            Intent back = new Intent(EventDetails.this, Archivedevent.class);
            back.putExtra("SocietyId",SocietyId);
            back.putExtra("dateofevent", dateofevent);
            back.putExtra("event",event);
            Common.internet_check=0;
            startActivity(back);
            finish();
        }
        else if(creator!=null || eventList!=null) {
            super.onBackPressed();
            LoggerGeneral.info("Showeadshj"+dateofevent);
            Intent back = new Intent(EventDetails.this, EventsList.class);
            back.putExtra("SocietyId",SocietyId);
            back.putExtra("dateofevent", dateofevent);
            Common.internet_check=0;
            back.putExtra("event","yesevent");
            back.putExtra("event",event);
            startActivity(back);
            finish();
        }
        else if(preview!=null) {
            super.onBackPressed();
            Intent shareEvent = new Intent(EventDetails.this, CreateEvent.class);
            shareEvent.putExtra("preview",preview);
            shareEvent.putExtra("event","event");
            shareEvent.putExtra("preview","preview");
            shareEvent.putExtra("SocietyId",SocietyId);
            shareEvent.putExtra("eventTitle",eventTitleText);
            shareEvent.putExtra("eventDate",evenTexttDate);
            shareEvent.putExtra("eventTime",eventTextTime);
            shareEvent.putExtra("eventdescription",evenTextDescription);
            shareEvent.putExtra("eventAddress",evenTextAddress);
            shareEvent.putExtra("eventLandmark",evenTextLandmark);
            shareEvent.putExtra("eventState",evenTextState);
            shareEvent.putExtra("eventCity",evenTextCity);
            shareEvent.putExtra("eventPincode",evenTextPincode);
            shareEvent.putExtra("eventHour",slecthour);
            shareEvent.putExtra("eventMiniute",selectminuite);
            shareEvent.putExtra("eventTimeToDet",eventDate1);
            shareEvent.putExtra("sendApi",eventDate2);
            shareEvent.putExtra("eventImage",eventImage);
            Common.internet_check=0;
            startActivity(shareEvent);
            finish();
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
                object.put("shared_with","public");



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

            mDialog = new ProgressDialog(EventDetails.this,ProgressDialog.THEME_HOLO_DARK);
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
                            Intent event = new Intent(EventDetails.this, DashBoard.class);
                            event.putExtra("event",3);
                            event.putExtra("SocietyId",SocietyId);
                            Common.internet_check=0;
                            startActivity(event);
                            finish();


                         /* JSONObject data = results.getJSONObject("data");

                            String property_id = data.getString("property_id");

                            appPreferences.putString("property_id",property_id);*/

                        }

                    }

                    if(account_status == 0) {
                        Intent intent = new Intent(EventDetails.this,Login.class);
                        appPreferences.remove("user_id");
                        appPreferences.remove("user_name");
                        appPreferences.remove("email_id");
                        appPreferences.remove("storecoverimage");
                        appPreferences.remove("storeimage");
                        appPreferences.remove("profile_image_url");
                        Common.internet_check=0;
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

    class  getEventDetails extends AsyncTask<String, String, JSONObject> {
        String type;
        ProgressDialog mDialog;
        String uposttext;
        String upostId;
        String ed_date;



        @Override
        protected JSONObject doInBackground(String... params) {
            // TODO Auto-generated method stub

            String url= Constants.Base+Constants.getEventDetails;

            JSONObject object = new JSONObject();
            try {

                //   object.put("society_id",SocietyId);
                object.put("user_id",appPreferences.getString("user_id",""));
                object.put("event_id",event_id);
                //object.put("user_id","10");
                //object.put("poll_id","2");

                LoggerGeneral.info("JsonObjectPrintgetPollDetails" + object.toString());

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

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
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

                            for(int i=0;i<=jsonArray.length()-1;i++) {

                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                    eventId=jsonObject.getString("id");
                                    eventSocietyId=jsonObject.getString("society_id");
                                    eventTitleText=jsonObject.getString("title");
                                    evenTexttDate=jsonObject.getString("event_date_time");
                                    evenTextDescription=jsonObject.getString("description");
                                    eventImage=jsonObject.getString("event_image_url");
                                    evenTextAddress=jsonObject.getString("address");
                                    evenTextLandmark=jsonObject.getString("landmark");
                                    evenTextState=jsonObject.getString("state");
                                    evenTextCity=jsonObject.getString("city");
                                    evenTextPincode=jsonObject.getString("postal_code");
                                    eventCreator=jsonObject.getString("user_name");
                                    eventAcceptStatus=jsonObject.getString("user_response");
                                    checkDate=jsonObject.getString("event_date");
                                if(jsonObject.has("event_time")) {
                                    event_time=jsonObject.getString("event_time");
                                }

                                eventNameTextView.setText(eventTitleText);

                                ImageLoader.getInstance().displayImage(eventImage, eventImageView, options, animateFirstListener);

                                if(eventImage.length()>0) {
                                    ImageLoader.getInstance().displayImage(eventImage, eventImageView, options, animateFirstListener);
                                }
                                else {
                                    eventImageView.setVisibility(View.GONE);
                                }

                                eventCreatorNameTextView.setText(eventCreator);




                                eventDescriptionTextView.setText(evenTextDescription);

                                eventAddressTextView.setText(evenTextAddress+", "+evenTextLandmark+", "+evenTextCity+"\n"+evenTextState+"-"+evenTextPincode);

                                if(eventAcceptStatus.equals("accepted") || eventAcceptStatus.equals("rejected"))
                                {
                                    acceptReject.setVisibility(View.GONE);
                                }

                                String myFormat1 = "yyyy-MM-dd hh:mm:ss";
                                String myFormat = "dd MMM, yyyy";
                                SimpleDateFormat sdf1 = new SimpleDateFormat(myFormat1, Locale.US);
                                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                                Date myDate = null;
                                try {
                                    myDate = sdf1.parse(checkDate);

                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                checkDate=sdf.format(myDate);
                                eventDateTextView.setText(checkDate);
                                if(event_time!=null && event_time.length()>0) {
                                    eventTime.setText(event_time);
                                }
                                else {
                                        eventTime.setText(time);
                                    if (fromNotification != null) {
                                        //   if(eventTime.getText().toString()==null){

                                        String checkDatenew = jsonObject.getString("event_date");
                                        String myFormat10 = "yyyy-MM-dd hh:mm:ss";
                                        String myFormat0 = "hh:mm:ss a";
                                        SimpleDateFormat sdf10 = new SimpleDateFormat(myFormat10, Locale.US);
                                        SimpleDateFormat sdf0 = new SimpleDateFormat(myFormat0, Locale.US);
                                        Date myDate0 = null;
                                        try {
                                            myDate0 = sdf10.parse(checkDatenew);

                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                        checkDatenew = sdf0.format(myDate0);
                                 /*   String am_pm = "";
                                    if (checkDatenew.get(Calendar.AM_PM) == Calendar.AM)
                                        am_pm = "AM";
                                    else if (checkDatenew.get(Calendar.AM_PM) == Calendar.PM)
                                        am_pm = "PM";*/
                                        LoggerGeneral.info("showdateneww000111---" + checkDatenew);

                                        String str = changeCharInPosition(5, ' ', checkDatenew);
                                        String str1 = changeCharInPosition(6, ' ', str);
                                        String str2 = changeCharInPosition(7, ' ', str1);

                                        StringBuilder sb = new StringBuilder(checkDatenew);
                                        sb.deleteCharAt(5);
                                        sb.deleteCharAt(5);
                                        sb.deleteCharAt(5);

                                        eventTime.setText(sb.toString().trim());
                                        LoggerGeneral.info("showdateneww000---" + checkDatenew);

                                        //        }

                                        LoggerGeneral.info("showdateneww---" + checkDatenew);
                                    }
                                }

                                Date now=new Date();
                             if(myDate.before(now))
                             {
                                 acceptReject.setVisibility(View.GONE);
                                 deleteEvent.setVisibility(View.GONE);
                             }

                                LoggerGeneral.info("eventIdd=="+eventId);

                            }




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
                        Common.internet_check=0;
                        context.startActivity(intent);
                        ((Activity) context).finish();
                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mDialog.dismiss();


            }
        }

    }
    public String changeCharInPosition(int position, char ch, String str){
        char[] charArray = str.toCharArray();
        charArray[position] = ch;
        return new String(charArray);
    }
    class  deleteEvent extends AsyncTask<String, String, JSONObject> {
        String type;
        ProgressDialog mDialog;




        @Override
        protected JSONObject doInBackground(String... params) {
            // TODO Auto-generated method stub

            String url= Constants.Base+Constants.deleteEvent;

            JSONObject object = new JSONObject();
            try {

                //   object.put("society_id",SocietyId);
                LoggerGeneral.info("eventId--" + eventId);
                object.put("user_id",appPreferences.getString("user_id",""));
                object.put("event_id",event_id);


                LoggerGeneral.info("JsonObjectPrintgetPollDetails" + object.toString());

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

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
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


                            Common.showToast(context, "Event deleted successfully");
                            Intent intent = new Intent(EventDetails.this,EventsList.class);

                            intent.putExtra("SocietyId", SocietyId);
                            intent.putExtra("dateofevent", dateofevent);
                            intent.putExtra("event","yesevent");
                           // intent.putExtra()
                            Common.internet_check=0;
                            startActivity(intent);
                            finish();

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
                        Common.internet_check=0;
                        context.startActivity(intent);
                        ((Activity) context).finish();
                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mDialog.dismiss();


            }
        }

    }

    class  submitEvent extends AsyncTask<String, String, JSONObject> {
        String type;
        ProgressDialog mDialog;
        String status;

        public submitEvent(String status){
            this.status = status;
        }


        @Override
        protected JSONObject doInBackground(String... params) {
            // TODO Auto-generated method stub

            String url= Constants.Base+Constants.submitEvent;

            JSONObject object = new JSONObject();
            try {

                //   object.put("society_id",SocietyId);
                LoggerGeneral.info("eventId--"+eventId);
                object.put("user_id",appPreferences.getString("user_id",""));
                object.put("event_id",event_id);
           //object.put("eventstatus",event_status);

                object.put("status",status);


                LoggerGeneral.info("JsonObjectPrintgetPollDetails" + object.toString());

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

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
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
                    String eventstatus = meta.getString("eventstatus");
                    //int eventstatus = -1;
                    int account_status = 1;
                    if(meta.has("account_status")){
                        if(!meta.isNull("account_status")){
                            account_status = meta.getInt("account_status");
                       //     eventstatus= meta.getString("eventstatus");
                        }
                    }


                    if(account_status==1){
                        if (status_code == 0) {
                            if (eventstatus.equalsIgnoreCase("-1")) {

                                if (checkclickAccept.equals("accepted") || checkclickAccept.equals("rejected")) {
                                    final Dialog dialog = new Dialog(EventDetails.this);
                                    dialog.setContentView(R.layout.event_accept_popup1);
                                    dialog.setCanceledOnTouchOutside(true);
                                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));








                                    LinearLayout ok = (LinearLayout) dialog.findViewById(R.id.ok1);

                                    ok.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent event = new Intent(EventDetails.this, DashBoard.class);
                                            event.putExtra("event",3);
                                            event.putExtra("SocietyId",SocietyId);
                                            Common.internet_check=0;
                                            startActivity(event);
                                            finish();
                                        }
                                    });
                                    dialog.show();
                                }

                                acceptReject.setVisibility(View.GONE);

                              //  Common.showToast(context,"No internet connection!");
                            } else {{

                                if (checkclickAccept.equals("accepted")) {
                                    //  Common.showToast(context, "Event accepted");
                                    final Dialog dialog = new Dialog(EventDetails.this);
                                    dialog.setContentView(R.layout.event_accept_popup);
                                    dialog.setCanceledOnTouchOutside(true);
                                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


                                    LinearLayout ok = (LinearLayout) dialog.findViewById(R.id.ok);

                                    ok.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialog.dismiss();
                                        }
                                    });


                                    dialog.show();

                                }
                            }

                            if (checkclickAccept.equals("rejected")) {
                                Common.showToast(context, "Event rejected");
                            }
                            acceptReject.setVisibility(View.GONE);
                        }}
                    }

             /*       ==============================================================================================*/

                      /*  if (eventstatus == -1) {


                            if (checkclickAccept.equals("accepted")) {
                                //  Common.showToast(context, "Event accepted");
                                final Dialog dialog = new Dialog(EventDetails.this);
                                dialog.setContentView(R.layout.event_accept_popup1);
                                dialog.setCanceledOnTouchOutside(true);
                                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


                                LinearLayout ok = (LinearLayout) dialog.findViewById(R.id.ok1);

                                ok.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                    }
                                });


                                dialog.show();

                            }
                        }*/


                  /*  ==============================================================================================*/

                    if(account_status == 0) {
                        Intent intent = new Intent(context,Login.class);
                        appPreferences.remove("user_id");
                        appPreferences.remove("user_name");
                        appPreferences.remove("email_id");
                        appPreferences.remove("storecoverimage");
                        appPreferences.remove("storeimage");
                        appPreferences.remove("profile_image_url");
                        Common.internet_check=0;
                        context.startActivity(intent);
                        ((Activity) context).finish();
                    }




                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mDialog.dismiss();


            }
        }

    }
    private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

        static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            if (loadedImage != null) {
                ImageView imageView = (ImageView) view;
                boolean firstDisplay = !displayedImages.contains(imageUri);
                if (firstDisplay) {
                    FadeInBitmapDisplayer.animate(imageView, 500);
                    displayedImages.add(imageUri);
                }
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current game state
       if(event_id!=null) {
           temp_bundle.putString("event_id", event_id);

       }

        if(creator!=null) {
            temp_bundle.putString("creator", creator);
        }
        if(dateofevent!=null) {
            temp_bundle.putString("dateofevent",dateofevent);
        }
        if(eventList!=null)
        {
            temp_bundle.putString("eventList",eventList);
        }
        if(SocietyId!=null) {
            temp_bundle.putString("SocietyId", SocietyId);
        }
        if(archiveEventcheck!=null)
        {
            temp_bundle.putString("archiveEvent",archiveEventcheck);
        }
        if(time!=null)
        {
            temp_bundle.putString("time",time);
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
