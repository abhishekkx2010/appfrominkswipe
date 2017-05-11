package com.inkswipe.SocialSociety;

import android.app.DatePickerDialog;
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
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import android.widget.DatePicker;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.TimeZone;

import adapter.CustomAdapter;
import adapter.PropertyAdapter;
import model.DrawerList;
import util.AppPreferences;
import util.Common;
import util.Constants;
import util.ExceptionHandler;
import util.LoggerGeneral;
import util.ServiceFacade;

public class DashBoard extends AppCompatActivity {
    private TabLayout allTabs;
    Context context;
    ImageView tabImageview0,tabImageview1,tabImageview2,tabImageview3,tabImageview01,tabImageview11,tabImageview21,tabImageview31;
    TextView tabTextView0,tabTextView3,tabTextView2,tabTextView1,tabTextView01,tabTextView11,tabTextView21,tabTextView31;
    private Fragment post,opinionPost,announcement,event;
    private int[] tabIcons = {
            R.mipmap.post,
            R.mipmap.polls,
            R.mipmap.event,
            R.mipmap.announcement};
    private int[] tabIcons1 = {
            R.mipmap.postgreen,
            R.mipmap.pollsgreen,
            R.mipmap.eventgreen,
            R.mipmap.announcementgreen};
    Calendar myCalendar;
    Calendar cal;
    DisplayImageOptions options;
    LinearLayout home;
    Fragment fragment = null;
    TextView appname;
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    ImageView rphoto;
    RelativeLayout notification;
    AppPreferences appPreferences;
    String SocietyId ;
    TextView dashname,profilenm;
    Dialog  dialog;
    ImageView dpImage,profileim;
    public static Bundle temp_bundle;
    int savedInstanceCheck=0;
    int eventCheck;
    int announcementCheck;
    int pollsCheck;
    String fromNotification;
    String SocietyId1;

    TextView societydashname;

    public static boolean asyncpost,asyncpoll,asyncevent,asyncannounce;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (getIntent().hasExtra("bundle") && savedInstanceState==null){
            LoggerGeneral.info("isItRight");
            savedInstanceCheck=1;
            savedInstanceState = getIntent().getExtras().getBundle("bundle");
        }
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        setContentView(R.layout.activity_dash_board);

        context=DashBoard.this;
        Common.internet_check=11;
        appPreferences  =AppPreferences.getAppPreferences(context);
        cal = Calendar.getInstance(TimeZone.getDefault()); // Get current date
        myCalendar = Calendar.getInstance();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView title=((TextView) toolbar.findViewById(R.id.toolbar_title));
        title.setText("");
        toolbar.setTitleTextColor(android.graphics.Color.WHITE);
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.fallb)
                .showImageForEmptyUri(R.mipmap.fallb)
                .showImageOnFail(R.mipmap.fallb)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .displayer(new SimpleBitmapDisplayer())
                .build();

        eventCheck = getIntent().getIntExtra("event", 0);

        announcementCheck=getIntent().getIntExtra("announcement", 0);

        SocietyId = getIntent().getStringExtra("SocietyId");

        fromNotification=getIntent().getStringExtra("fromNotification");

        pollsCheck=getIntent().getIntExtra("polls", 0);
        SocietyId1=getIntent().getStringExtra("SocietyIdnotification");

        Common.internet_check=11;

        if(eventCheck==3)
        {
            Common.internet_check=13;
        }

        if(pollsCheck==2)
        {
            Common.internet_check=12;
        }

        if(announcementCheck==4)
        {
            Common.internet_check=14;
        }

        if(savedInstanceCheck==1)
        {
            if(Common.internet_check==11)
            {
                SocietyId=savedInstanceState.getString("SocietyId");
            }
            if(Common.internet_check==12)
            {
                SocietyId=savedInstanceState.getString("SocietyId");
                pollsCheck=savedInstanceState.getInt("polls");
            }
            if(Common.internet_check==13)
            {
               SocietyId=savedInstanceState.getString("SocietyId");
                eventCheck=savedInstanceState.getInt("event");
            }
            if(Common.internet_check==14)
            {
                SocietyId=savedInstanceState.getString("SocietyId");
                announcementCheck=savedInstanceState.getInt("announcement");
            }
        }

        LoggerGeneral.info("SocityDashboardkk----"+SocietyId);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.getNavigationIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
    //    toolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.backarrow));

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //What to do on back clicked

                if(fromNotification!=null)
                {
                    Intent notification=new Intent(DashBoard.this,NotificationApp.class);
                    notification.putExtra("Societyid",SocietyId1);
                    startActivity(notification);
                    finish();
                }
                else {
                    Intent intent = new Intent(DashBoard.this, MyProperty.class);
                    PropertyAdapter.intentCheck = 0;
                    Common.internet_check = 0;
                    startActivity(intent);
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
                Intent notification=new Intent(DashBoard.this,NotificationApp.class);
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
        registerReceiver(broadcast_reciever2, new IntentFilter("finish_activity2"));
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        LayoutInflater inflater = getLayoutInflater();
        View listHeaderView = inflater.inflate(R.layout.header_list, null, false);

        LinearLayout navHome= (LinearLayout) listHeaderView.findViewById(R.id.Navhome);

        navHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myProperty=new Intent(DashBoard.this,MyProperty.class);
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
                        Intent profile=new Intent(DashBoard.this,Profile.class);
                        profile.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        Common.internet_check=0;
                        startActivity(profile);
                        finish();
                        break;
                    case 2:
                        LoggerGeneral.info("2");
                        Intent addSociety=new Intent(DashBoard.this,AddSociety.class);
                        Common.internet_check=0;
                        startActivity(addSociety);
                        finish();
                        break;
                    case 3:
                        LoggerGeneral.info("3");
                        Intent myProperty=new Intent(DashBoard.this,MyProperty.class);
                        Common.internet_check=0;
                        PropertyAdapter.intentCheck=0;
                        startActivity(myProperty);
                        finish();
                        break;
                    case 4:
                        LoggerGeneral.info("4");
                        mDrawerLayout.closeDrawers();
                        replaceFragment(event);

                        pollsCheck=0;
                        announcementCheck=0;
                        eventCheck=3;
                        Common.internet_check=13;
                        temp_bundle.putInt("polls",0);
                        temp_bundle.putInt("event",3);
                        temp_bundle.putInt("announcement",0);
                        tabTextView0.setVisibility(View.VISIBLE);
                        tabTextView01.setVisibility(View.GONE);
                        tabTextView1.setVisibility(View.VISIBLE);
                        tabTextView11.setVisibility(View.GONE);
                        tabTextView2.setVisibility(View.GONE);
                        tabTextView21.setVisibility(View.VISIBLE);
                        tabTextView3.setVisibility(View.VISIBLE);
                        tabTextView31.setVisibility(View.GONE);

                        tabImageview0.setVisibility(View.VISIBLE);
                        tabImageview01.setVisibility(View.GONE);
                        tabImageview1.setVisibility(View.VISIBLE);
                        tabImageview11.setVisibility(View.GONE);
                        tabImageview2.setVisibility(View.GONE);
                        tabImageview21.setVisibility(View.VISIBLE);
                        tabImageview3.setVisibility(View.VISIBLE);
                        tabImageview31.setVisibility(View.GONE);
                        /*Intent events=new Intent(DashBoard.this,DashBoard.class);
                        events.putExtra("event",3);
                        startActivity(events);
                        finish();*/
                        break;
                    case 5:
                        LoggerGeneral.info("5");
                        Intent createEvents=new Intent(DashBoard.this,CreateEvent.class);
                        Common.internet_check=0;
                        createEvents.putExtra("SocietyId",SocietyId);
                        startActivity(createEvents);
                        finish();
                        break;
                    case 6:
                        LoggerGeneral.info("6");
                        Intent archivedEvents=new Intent(DashBoard.this,Archivedevent.class);
                        Common.internet_check=0;
                        archivedEvents.putExtra("SocietyId",SocietyId);
                        startActivity(archivedEvents);
                        finish();
                        break;
                    case 7:
                        LoggerGeneral.info("7");
                        mDrawerLayout.closeDrawers();
                        replaceFragment(announcement);

                        pollsCheck=0;
                        announcementCheck=4;
                        eventCheck=0;
                        Common.internet_check=14;
                        temp_bundle.putInt("polls",0);
                        temp_bundle.putInt("event", 0);
                        temp_bundle.putInt("announcement",4);
                        tabTextView0.setVisibility(View.VISIBLE);
                        tabTextView01.setVisibility(View.GONE);
                        tabTextView1.setVisibility(View.VISIBLE);
                        tabTextView11.setVisibility(View.GONE);
                        tabTextView2.setVisibility(View.VISIBLE);
                        tabTextView21.setVisibility(View.GONE);
                        tabTextView3.setVisibility(View.GONE);
                        tabTextView31.setVisibility(View.VISIBLE);

                        tabImageview0.setVisibility(View.VISIBLE);
                        tabImageview01.setVisibility(View.GONE);
                        tabImageview1.setVisibility(View.VISIBLE);
                        tabImageview11.setVisibility(View.GONE);
                        tabImageview2.setVisibility(View.VISIBLE);
                        tabImageview21.setVisibility(View.GONE);
                        tabImageview3.setVisibility(View.GONE);
                        tabImageview31.setVisibility(View.VISIBLE);
                        /*Intent announcement=new Intent(Profile.this,DashBoard.class);
                        announcement.putExtra("announcement",4);
                        startActivity(announcement);
                        finish();*/
                        break;
                    case 8:
                        LoggerGeneral.info("8");
                        Intent createAnnouncement=new Intent(DashBoard.this,CreateAnnouncement.class);
                        Common.internet_check=0;
                        createAnnouncement.putExtra("SocietyId",SocietyId);
                        startActivity(createAnnouncement);
                        finish();
                        break;
                    case 9:
                        LoggerGeneral.info("9");
                        mDrawerLayout.closeDrawers();
                        replaceFragment(opinionPost);

                        pollsCheck=2;
                        announcementCheck=0;
                        eventCheck=0;
                        Common.internet_check=12;
                        temp_bundle.putInt("polls",2);
                        temp_bundle.putInt("event", 0);
                        temp_bundle.putInt("announcement",0);
                        tabTextView0.setVisibility(View.VISIBLE);
                        tabTextView01.setVisibility(View.GONE);
                        tabTextView1.setVisibility(View.GONE);
                        tabTextView11.setVisibility(View.VISIBLE);
                        tabTextView2.setVisibility(View.VISIBLE);
                        tabTextView21.setVisibility(View.GONE);
                        tabTextView3.setVisibility(View.VISIBLE);
                        tabTextView31.setVisibility(View.GONE);

                        tabImageview0.setVisibility(View.VISIBLE);
                        tabImageview01.setVisibility(View.GONE);
                        tabImageview1.setVisibility(View.GONE);
                        tabImageview11.setVisibility(View.VISIBLE);
                        tabImageview2.setVisibility(View.VISIBLE);
                        tabImageview21.setVisibility(View.GONE);
                        tabImageview3.setVisibility(View.VISIBLE);
                        tabImageview31.setVisibility(View.GONE);
                        /*Intent polls=new Intent(Profile.this,DashBoard.class);
                        polls.putExtra("polls",2);
                        startActivity(polls);
                        finish();*/
                        break;
                    case 10:
                        LoggerGeneral.info("10");
                        Intent createEventPoll=new Intent(DashBoard.this,CreatePoll.class);
                        Common.internet_check=0;
                        createEventPoll.putExtra("SocietyId",SocietyId);
                        startActivity(createEventPoll);
                        finish();
                        break;
                    case 11:
                        LoggerGeneral.info("11");
                        Intent archivedEventPoll=new Intent(DashBoard.this,ArchivedPolls.class);
                        Common.internet_check=0;
                        archivedEventPoll.putExtra("SocietyId", SocietyId);
                        startActivity(archivedEventPoll);
                        finish();
                        break;
                    case 12:
                        LoggerGeneral.info("12");
                        Intent members=new Intent(DashBoard.this,Members.class);
                        Common.internet_check=0;
                        members.putExtra("SocietyId",SocietyId);
                        startActivity(members);
                        finish();
                        break;
                    case 13:
                        LoggerGeneral.info("13");
                        Intent offers=new Intent(DashBoard.this,Offer.class);
                        Common.internet_check=0;
                        offers.putExtra("SocietyId",SocietyId);
                        startActivity(offers);
                        finish();
                        break;
                    case 14:
                        LoggerGeneral.info("14");
                        Intent complaint=new Intent(DashBoard.this,Complaint.class);
                        Common.internet_check=0;
                        complaint.putExtra("SocietyId",SocietyId);
                        startActivity(complaint);
                        finish();
                        break;
                    case 15:
                        LoggerGeneral.info("15");
                        Intent notification=new Intent(DashBoard.this,NotificationApp.class);
                        Common.internet_check=0;
                        //    notification.putExtra("SocietyId",Soci)
                        int test1=appPreferences.getInt("NotificationOld",0);
                        test1=test1+Constants.notififcationcount;
                        appPreferences.putInt("NotificationOld",test1);
                        notification.putExtra("SocietyId",SocietyId);
                        Constants.notififcationcount=0;
                        startActivity(notification);
                        finish();
                        break;
                    case 16:
                      /*  LoggerGeneral.info("16");
                        Intent intent = new Intent(DashBoard.this,Login.class);
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


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.my_statusbar_color));
        }

        allTabs = (TabLayout) findViewById(R.id.tabs);
        setupTabLayout();

        TabLayout.Tab tab0 = allTabs.getTabAt(0);
        RelativeLayout relativeLayout0 = (RelativeLayout)
                LayoutInflater.from(this).inflate(R.layout.tablayout, allTabs, false);

        tabImageview0 = (ImageView) relativeLayout0.findViewById(R.id.tab_icon);
        tabImageview0.setImageResource(tabIcons[0]);

        tabTextView0= (TextView) relativeLayout0.findViewById(R.id.title);
        tabTextView0.setText("Post");
        tabImageview01 = (ImageView) relativeLayout0.findViewById(R.id.tab_icon1);
        tabImageview01.setImageResource(tabIcons1[0]);

        tabTextView01= (TextView) relativeLayout0.findViewById(R.id.title1);
        tabTextView01.setText("Post");
        tab0.setCustomView(relativeLayout0);



        TabLayout.Tab tab1 = allTabs.getTabAt(1);
        RelativeLayout relativeLayout1 = (RelativeLayout)
                LayoutInflater.from(this).inflate(R.layout.tablayout, allTabs, false);

        tabImageview1 = (ImageView) relativeLayout1.findViewById(R.id.tab_icon);
        tabImageview1.setImageResource(tabIcons[1]);

        tabTextView1= (TextView) relativeLayout1.findViewById(R.id.title);
        tabTextView1.setText("Opinion Polls");
        tabImageview11 = (ImageView) relativeLayout1.findViewById(R.id.tab_icon1);
        tabImageview11.setImageResource(tabIcons1[1]);

        tabTextView11= (TextView) relativeLayout1.findViewById(R.id.title1);
        tabTextView11.setText("Opinion Polls");
        tab1.setCustomView(relativeLayout1);




        TabLayout.Tab tab2 = allTabs.getTabAt(2);
        RelativeLayout relativeLayout2 = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.tablayout, allTabs, false);

        tabImageview2= (ImageView) relativeLayout2.findViewById(R.id.tab_icon);
        tabImageview2.setImageResource(tabIcons[2]);

        tabTextView2= (TextView) relativeLayout2.findViewById(R.id.title);
        tabTextView2.setText("Event");
        tabImageview21= (ImageView) relativeLayout2.findViewById(R.id.tab_icon1);
        tabImageview21.setImageResource(tabIcons1[2]);

        tabTextView21= (TextView) relativeLayout2.findViewById(R.id.title1);
        tabTextView21.setText("Event");
        tab2.setCustomView(relativeLayout2);

        TabLayout.Tab tab3 = allTabs.getTabAt(3);
        RelativeLayout relativeLayout3 = (RelativeLayout)
                LayoutInflater.from(this).inflate(R.layout.tablayout, allTabs, false);

        tabImageview3= (ImageView) relativeLayout3.findViewById(R.id.tab_icon);
        tabImageview3.setImageResource(tabIcons[3]);

        tabTextView3= (TextView) relativeLayout3.findViewById(R.id.title);
        tabTextView3.setText("Announcements");
        tabImageview31= (ImageView) relativeLayout3.findViewById(R.id.tab_icon1);
        tabImageview31.setImageResource(tabIcons1[3]);

        tabTextView31= (TextView) relativeLayout3.findViewById(R.id.title1);
        tabTextView31.setText("Announcements");
        tab3.setCustomView(relativeLayout3);



        bindWidgetsWithAnEvent();

        if(announcementCheck==4)
        {
            replaceFragment(announcement);
            tabTextView0.setVisibility(View.VISIBLE);
            tabTextView01.setVisibility(View.GONE);
            tabTextView1.setVisibility(View.VISIBLE);
            tabTextView11.setVisibility(View.GONE);
            tabTextView2.setVisibility(View.VISIBLE);
            tabTextView21.setVisibility(View.GONE);
            tabTextView3.setVisibility(View.GONE);
            tabTextView31.setVisibility(View.VISIBLE);

            tabImageview0.setVisibility(View.VISIBLE);
            tabImageview01.setVisibility(View.GONE);
            tabImageview1.setVisibility(View.VISIBLE);
            tabImageview11.setVisibility(View.GONE);
            tabImageview2.setVisibility(View.VISIBLE);
            tabImageview21.setVisibility(View.GONE);
            tabImageview3.setVisibility(View.GONE);
            tabImageview31.setVisibility(View.VISIBLE);

            if(Post.postAsyncTask!=null) {
                Post.postAsyncTask.cancel(true);
            }
            if(Event.eventAsyncTask!=null) {
                Event.eventAsyncTask.cancel(true);
            }
            if(Opinionposts.userPost!=null) {
                Opinionposts.userPost.cancel(true);
            }

        }
        else if(pollsCheck==2)
        {
            replaceFragment(opinionPost);
            tabTextView0.setVisibility(View.VISIBLE);
            tabTextView01.setVisibility(View.GONE);
            tabTextView1.setVisibility(View.VISIBLE);
            tabTextView11.setVisibility(View.GONE);
            tabTextView2.setVisibility(View.VISIBLE);
            tabTextView21.setVisibility(View.GONE);
            tabTextView3.setVisibility(View.GONE);
            tabTextView31.setVisibility(View.VISIBLE);

            tabImageview0.setVisibility(View.VISIBLE);
            tabImageview01.setVisibility(View.GONE);
            tabImageview1.setVisibility(View.VISIBLE);
            tabImageview11.setVisibility(View.GONE);
            tabImageview2.setVisibility(View.VISIBLE);
            tabImageview21.setVisibility(View.GONE);
            tabImageview3.setVisibility(View.GONE);
            tabImageview31.setVisibility(View.VISIBLE);
            if(Post.postAsyncTask!=null) {
                Post.postAsyncTask.cancel(true);
            }
            if(Event.eventAsyncTask!=null) {
                Event.eventAsyncTask.cancel(true);
            }
            if(Announcement.getMyAnnouncement!=null) {
                Announcement.getMyAnnouncement.cancel(true);
            }
        }
        else if(eventCheck==3)
        {
            replaceFragment(event);
            tabTextView0.setVisibility(View.VISIBLE);
            tabTextView01.setVisibility(View.GONE);
            tabTextView1.setVisibility(View.VISIBLE);
            tabTextView11.setVisibility(View.GONE);
            tabTextView2.setVisibility(View.GONE);
            tabTextView21.setVisibility(View.VISIBLE);
            tabTextView3.setVisibility(View.VISIBLE);
            tabTextView31.setVisibility(View.GONE);

            tabImageview0.setVisibility(View.VISIBLE);
            tabImageview01.setVisibility(View.GONE);
            tabImageview1.setVisibility(View.VISIBLE);
            tabImageview11.setVisibility(View.GONE);
            tabImageview2.setVisibility(View.GONE);
            tabImageview21.setVisibility(View.VISIBLE);
            tabImageview3.setVisibility(View.VISIBLE);
            tabImageview31.setVisibility(View.GONE);
            if(Post.postAsyncTask!=null) {
                Post.postAsyncTask.cancel(true);
            }
            if(Announcement.getMyAnnouncement!=null) {
                Announcement.getMyAnnouncement.cancel(true);
            }
            if(Opinionposts.userPost!=null) {
                Opinionposts.userPost.cancel(true);
            }
        }
        else {
            replaceFragment(post);
            tabTextView0.setVisibility(View.GONE);
            tabTextView01.setVisibility(View.VISIBLE);
            tabTextView1.setVisibility(View.VISIBLE);
            tabTextView11.setVisibility(View.GONE);
            tabTextView2.setVisibility(View.VISIBLE);
            tabTextView21.setVisibility(View.GONE);
            tabTextView3.setVisibility(View.VISIBLE);
            tabTextView31.setVisibility(View.GONE);

            tabImageview0.setVisibility(View.GONE);
            tabImageview01.setVisibility(View.VISIBLE);
            tabImageview1.setVisibility(View.VISIBLE);
            tabImageview11.setVisibility(View.GONE);
            tabImageview2.setVisibility(View.VISIBLE);
            tabImageview21.setVisibility(View.GONE);
            tabImageview3.setVisibility(View.VISIBLE);
            tabImageview31.setVisibility(View.GONE);

            if(Event.eventAsyncTask!=null) {
                Event.eventAsyncTask.cancel(true);
            }
            if(Announcement.getMyAnnouncement!=null) {
                Announcement.getMyAnnouncement.cancel(true);
            }
            if(Opinionposts.userPost!=null) {
                Opinionposts.userPost.cancel(true);
            }
        }


        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
        ImageLoader.getInstance().init(config);

        rphoto = (ImageView)findViewById(R.id.rphoto);


        if(Common.isOnline(context)) {
            ImageLoader.getInstance().displayImage(appPreferences.getString("profile_image_url", ""), rphoto, options, animateFirstListener);
        }
        else
        {
            if(appPreferences.getString("profile_image_url", "")!=null ||!appPreferences.getString("profile_image_url","").equals("profile_image_url")||appPreferences.getString("profile_image_url", "")!="" ){

                rphoto.setImageBitmap(StringToBitMap(appPreferences.getString("storeimage","")));
        }
        }
        dashname = (TextView)findViewById(R.id.dashname);

        societydashname = (TextView)findViewById(R.id.societydashname);
        dashname.setText(appPreferences.getString("user_name",""));


        societydashname.setText(appPreferences.getString("societynamedash",""));
        rphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new Dialog(DashBoard.this);
                dialog.setContentView(R.layout.dpdialog);
                dialog.setCanceledOnTouchOutside(true);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


                dpImage = (ImageView) dialog.findViewById(R.id.dpimage);

                ImageLoader.getInstance().displayImage(appPreferences.getString("profile_image_url",""), dpImage, options, animateFirstListener);

             /*   if(Common.isOnline(context)) {
                    ImageLoader.getInstance().displayImage(appPreferences.getString("profile_image_url", ""), rphoto, options, animateFirstListener);
                }
                else {
                    if (appPreferences.getString("profile_image_url", "") != null  || appPreferences.getString("profile_image_url", "") != "") {

                        rphoto.setImageBitmap(StringToBitMap(appPreferences.getString("storeimage", "")));
                    }
                }*/

                profilenm = (TextView)dialog.findViewById(R.id.profilemnm);
                profilenm.setText(appPreferences.getString("user_name",""));;
                profileim = (ImageView)dialog.findViewById(R.id.profileim);


                profileim.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        Intent intent = new Intent(context,Profile.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        Common.internet_check=0;
                        startActivity(intent);
                        finish();

                    }
                });


                dialog.show();
            }
        });

        temp_bundle = new Bundle();
        onSaveInstanceState(temp_bundle);
    }
    public Bitmap StringToBitMap(String encodedString){
        try {
            byte [] encodeByte= Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
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

            mDialog = new ProgressDialog(DashBoard.this,ProgressDialog.THEME_HOLO_DARK);
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
                        Intent register   = new Intent(DashBoard.this,Login.class);
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
    protected void onResume() {
        super.onResume();
        registerReceiver(broadcast_reciever2, new IntentFilter("finish_activity2"));
        bindWidgetsWithAnEvent();
        registerReceiver(broadcast_reciever, new IntentFilter("finish_activity"));


    }


    private void setupTabLayout()
    {
        LoggerGeneral.info("intabchange"+SocietyId);
        post = new Post(SocietyId);
        opinionPost = new Opinionposts(SocietyId);
        event = new Event(SocietyId);
        announcement = new Announcement(SocietyId);
        allTabs.addTab(allTabs.newTab(),true);
        allTabs.addTab(allTabs.newTab());
        allTabs.addTab(allTabs.newTab());
        allTabs.addTab(allTabs.newTab());
    }

    private void bindWidgetsWithAnEvent() {
        allTabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                setCurrentTabFragment(tab.getPosition());
                switch (tab.getPosition()) {
                    case 0:
                        LoggerGeneral.info("tab0");
                        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                        temp_bundle.putInt("polls", 0);
                        temp_bundle.putInt("event", 0);
                        temp_bundle.putInt("announcement", 0);
                        Common.internet_check = 11;
                        tabTextView0.setVisibility(View.GONE);
                        tabTextView01.setVisibility(View.VISIBLE);
                        tabTextView1.setVisibility(View.VISIBLE);
                        tabTextView11.setVisibility(View.GONE);
                        tabTextView2.setVisibility(View.VISIBLE);
                        tabTextView21.setVisibility(View.GONE);
                        tabTextView3.setVisibility(View.VISIBLE);
                        tabTextView31.setVisibility(View.GONE);

                        tabImageview0.setVisibility(View.GONE);
                        tabImageview01.setVisibility(View.VISIBLE);
                        tabImageview1.setVisibility(View.VISIBLE);
                        tabImageview11.setVisibility(View.GONE);
                        tabImageview2.setVisibility(View.VISIBLE);
                        tabImageview21.setVisibility(View.GONE);
                        tabImageview3.setVisibility(View.VISIBLE);
                        tabImageview31.setVisibility(View.GONE);

                        if(Event.eventAsyncTask!=null) {
                            Event.eventAsyncTask.cancel(true);
                        }
                        if(Announcement.getMyAnnouncement!=null) {
                            Announcement.getMyAnnouncement.cancel(true);
                        }
                        if(Opinionposts.userPost!=null) {
                            Opinionposts.userPost.cancel(true);
                        }

                        break;

                    case 1:
                        LoggerGeneral.info("tab1");
                        InputMethodManager inputMethodManager0 = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                        inputMethodManager0.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

                        pollsCheck = 2;
                        announcementCheck = 0;
                        eventCheck = 0;
                        Common.internet_check = 12;
                        temp_bundle.putInt("polls", 2);
                        temp_bundle.putInt("event", 0);
                        temp_bundle.putInt("announcement", 0);
                        tabTextView0.setVisibility(View.VISIBLE);
                        tabTextView01.setVisibility(View.GONE);
                        tabTextView1.setVisibility(View.GONE);
                        tabTextView11.setVisibility(View.VISIBLE);
                        tabTextView2.setVisibility(View.VISIBLE);
                        tabTextView21.setVisibility(View.GONE);
                        tabTextView3.setVisibility(View.VISIBLE);
                        tabTextView31.setVisibility(View.GONE);

                        tabImageview0.setVisibility(View.VISIBLE);
                        tabImageview01.setVisibility(View.GONE);
                        tabImageview1.setVisibility(View.GONE);
                        tabImageview11.setVisibility(View.VISIBLE);
                        tabImageview2.setVisibility(View.VISIBLE);
                        tabImageview21.setVisibility(View.GONE);
                        tabImageview3.setVisibility(View.VISIBLE);
                        tabImageview31.setVisibility(View.GONE);

                        if(Post.postAsyncTask!=null) {
                            Post.postAsyncTask.cancel(true);
                        }
                        if(Event.eventAsyncTask!=null) {
                            Event.eventAsyncTask.cancel(true);
                        }
                        if(Announcement.getMyAnnouncement!=null) {
                            Announcement.getMyAnnouncement.cancel(true);
                        }

                        break;

                    case 2:
                        LoggerGeneral.info("tab2");
                        InputMethodManager inputMethodManager1 = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                        inputMethodManager1.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

                        pollsCheck = 0;
                        announcementCheck = 0;
                        eventCheck = 3;
                        Common.internet_check = 13;
                        temp_bundle.putInt("polls", 0);
                        temp_bundle.putInt("event", 3);
                        temp_bundle.putInt("announcement", 0);
                        tabTextView0.setVisibility(View.VISIBLE);
                        tabTextView01.setVisibility(View.GONE);
                        tabTextView1.setVisibility(View.VISIBLE);
                        tabTextView11.setVisibility(View.GONE);
                        tabTextView2.setVisibility(View.GONE);
                        tabTextView21.setVisibility(View.VISIBLE);
                        tabTextView3.setVisibility(View.VISIBLE);
                        tabTextView31.setVisibility(View.GONE);

                        tabImageview0.setVisibility(View.VISIBLE);
                        tabImageview01.setVisibility(View.GONE);
                        tabImageview1.setVisibility(View.VISIBLE);
                        tabImageview11.setVisibility(View.GONE);
                        tabImageview2.setVisibility(View.GONE);
                        tabImageview21.setVisibility(View.VISIBLE);
                        tabImageview3.setVisibility(View.VISIBLE);
                        tabImageview31.setVisibility(View.GONE);

                        if(Post.postAsyncTask!=null) {
                            Post.postAsyncTask.cancel(true);
                        }
                        if(Announcement.getMyAnnouncement!=null) {
                            Announcement.getMyAnnouncement.cancel(true);
                        }
                        if(Opinionposts.userPost!=null) {
                            Opinionposts.userPost.cancel(true);
                        }
                        break;

                    case 3:
                        LoggerGeneral.info("tab4");
                        InputMethodManager inputMethodManager4 = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                        inputMethodManager4.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

                        pollsCheck = 0;
                        announcementCheck = 4;
                        eventCheck = 0;
                        Common.internet_check = 14;
                        temp_bundle.putInt("polls", 0);
                        temp_bundle.putInt("event", 0);
                        temp_bundle.putInt("announcement", 4);
                        tabTextView0.setVisibility(View.VISIBLE);
                        tabTextView01.setVisibility(View.GONE);
                        tabTextView1.setVisibility(View.VISIBLE);
                        tabTextView11.setVisibility(View.GONE);
                        tabTextView2.setVisibility(View.VISIBLE);
                        tabTextView21.setVisibility(View.GONE);
                        tabTextView3.setVisibility(View.GONE);
                        tabTextView31.setVisibility(View.VISIBLE);

                        tabImageview0.setVisibility(View.VISIBLE);
                        tabImageview01.setVisibility(View.GONE);
                        tabImageview1.setVisibility(View.VISIBLE);
                        tabImageview11.setVisibility(View.GONE);
                        tabImageview2.setVisibility(View.VISIBLE);
                        tabImageview21.setVisibility(View.GONE);
                        tabImageview3.setVisibility(View.GONE);
                        tabImageview31.setVisibility(View.VISIBLE);

                        if(Post.postAsyncTask!=null) {
                            Post.postAsyncTask.cancel(true);
                        }
                        if(Event.eventAsyncTask!=null) {
                            Event.eventAsyncTask.cancel(true);
                        }
                        if(Opinionposts.userPost!=null) {
                            Opinionposts.userPost.cancel(true);
                        }
                        break;
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                /*for (int i = 0; i < allTabs.getTabCount(); i++) {
                    if(tab.getPosition()!=i) {
                        tab = allTabs.getTabAt(i);
                        LinearLayout relativeLayout = (LinearLayout)
                                LayoutInflater.from(instance).inflate(R.layout.tablayout, allTabs, false);

                        tabImageview = (ImageView) relativeLayout.findViewById(R.id.tab_icon);
                        tabTextView = (TextView) relativeLayout.findViewById(R.id.tab_name);
                        tabImageview.setImageResource(tabIcons[i]);
                        tabTextView.setText(tabName[i]);
                        tab.setCustomView(relativeLayout);
            *//*tab.select();*//*
                    }
                }*/
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {


                setCurrentTabFragment(tab.getPosition());
                switch (tab.getPosition()) {
                    case 0:
                        LoggerGeneral.info("tab0");
                        temp_bundle.putInt("polls", 0);
                        temp_bundle.putInt("event", 0);
                        temp_bundle.putInt("announcement", 0);
                        Common.internet_check = 11;
                        tabTextView0.setVisibility(View.GONE);
                        tabTextView01.setVisibility(View.VISIBLE);
                        tabTextView1.setVisibility(View.VISIBLE);
                        tabTextView11.setVisibility(View.GONE);
                        tabTextView2.setVisibility(View.VISIBLE);
                        tabTextView21.setVisibility(View.GONE);
                        tabTextView3.setVisibility(View.VISIBLE);
                        tabTextView31.setVisibility(View.GONE);

                        tabImageview0.setVisibility(View.GONE);
                        tabImageview01.setVisibility(View.VISIBLE);
                        tabImageview1.setVisibility(View.VISIBLE);
                        tabImageview11.setVisibility(View.GONE);
                        tabImageview2.setVisibility(View.VISIBLE);
                        tabImageview21.setVisibility(View.GONE);
                        tabImageview3.setVisibility(View.VISIBLE);
                        tabImageview31.setVisibility(View.GONE);

                        if(Event.eventAsyncTask!=null) {
                            Event.eventAsyncTask.cancel(true);
                        }
                        if(Announcement.getMyAnnouncement!=null) {
                            Announcement.getMyAnnouncement.cancel(true);
                        }
                        if(Opinionposts.userPost!=null) {
                            Opinionposts.userPost.cancel(true);
                        }

                        break;

                    case 1:
                        LoggerGeneral.info("tab1");
                        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                        pollsCheck = 2;
                        announcementCheck = 0;
                        eventCheck = 0;
                        Common.internet_check = 12;
                        temp_bundle.putInt("polls", 2);
                        temp_bundle.putInt("event", 0);
                        temp_bundle.putInt("announcement", 0);
                        tabTextView0.setVisibility(View.VISIBLE);
                        tabTextView01.setVisibility(View.GONE);
                        tabTextView1.setVisibility(View.GONE);
                        tabTextView11.setVisibility(View.VISIBLE);
                        tabTextView2.setVisibility(View.VISIBLE);
                        tabTextView21.setVisibility(View.GONE);
                        tabTextView3.setVisibility(View.VISIBLE);
                        tabTextView31.setVisibility(View.GONE);

                        tabImageview0.setVisibility(View.VISIBLE);
                        tabImageview01.setVisibility(View.GONE);
                        tabImageview1.setVisibility(View.GONE);
                        tabImageview11.setVisibility(View.VISIBLE);
                        tabImageview2.setVisibility(View.VISIBLE);
                        tabImageview21.setVisibility(View.GONE);
                        tabImageview3.setVisibility(View.VISIBLE);
                        tabImageview31.setVisibility(View.GONE);

                        if(Post.postAsyncTask!=null) {
                            Post.postAsyncTask.cancel(true);
                        }
                        if(Event.eventAsyncTask!=null) {
                            Event.eventAsyncTask.cancel(true);
                        }
                        if(Announcement.getMyAnnouncement!=null) {
                            Announcement.getMyAnnouncement.cancel(true);
                        }
                        break;

                    case 2:
                        LoggerGeneral.info("tab2");
                        InputMethodManager inputMethodManager1 = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                        inputMethodManager1.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                        pollsCheck = 0;
                        announcementCheck = 0;
                        eventCheck = 3;
                        Common.internet_check = 13;
                        temp_bundle.putInt("polls", 0);
                        temp_bundle.putInt("event", 3);
                        temp_bundle.putInt("announcement", 0);
                        tabTextView0.setVisibility(View.VISIBLE);
                        tabTextView01.setVisibility(View.GONE);
                        tabTextView1.setVisibility(View.VISIBLE);
                        tabTextView11.setVisibility(View.GONE);
                        tabTextView2.setVisibility(View.GONE);
                        tabTextView21.setVisibility(View.VISIBLE);
                        tabTextView3.setVisibility(View.VISIBLE);
                        tabTextView31.setVisibility(View.GONE);

                        tabImageview0.setVisibility(View.VISIBLE);
                        tabImageview01.setVisibility(View.GONE);
                        tabImageview1.setVisibility(View.VISIBLE);
                        tabImageview11.setVisibility(View.GONE);
                        tabImageview2.setVisibility(View.GONE);
                        tabImageview21.setVisibility(View.VISIBLE);
                        tabImageview3.setVisibility(View.VISIBLE);
                        tabImageview31.setVisibility(View.GONE);

                        if(Post.postAsyncTask!=null) {
                            Post.postAsyncTask.cancel(true);
                        }
                        if(Announcement.getMyAnnouncement!=null) {
                            Announcement.getMyAnnouncement.cancel(true);
                        }
                        if(Opinionposts.userPost!=null) {
                            Opinionposts.userPost.cancel(true);
                        }
                        break;

                    case 3:
                        LoggerGeneral.info("tab4");
                        InputMethodManager inputMethodManager4 = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                        inputMethodManager4.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                        pollsCheck = 0;
                        announcementCheck = 4;
                        eventCheck = 0;
                        Common.internet_check = 14;
                        temp_bundle.putInt("polls", 0);
                        temp_bundle.putInt("event", 0);
                        temp_bundle.putInt("announcement", 4);
                        tabTextView0.setVisibility(View.VISIBLE);
                        tabTextView01.setVisibility(View.GONE);
                        tabTextView1.setVisibility(View.VISIBLE);
                        tabTextView11.setVisibility(View.GONE);
                        tabTextView2.setVisibility(View.VISIBLE);
                        tabTextView21.setVisibility(View.GONE);
                        tabTextView3.setVisibility(View.GONE);
                        tabTextView31.setVisibility(View.VISIBLE);

                        tabImageview0.setVisibility(View.VISIBLE);
                        tabImageview01.setVisibility(View.GONE);
                        tabImageview1.setVisibility(View.VISIBLE);
                        tabImageview11.setVisibility(View.GONE);
                        tabImageview2.setVisibility(View.VISIBLE);
                        tabImageview21.setVisibility(View.GONE);
                        tabImageview3.setVisibility(View.GONE);
                        tabImageview31.setVisibility(View.VISIBLE);

                        if(Post.postAsyncTask!=null) {
                            Post.postAsyncTask.cancel(true);
                        }
                        if(Event.eventAsyncTask!=null) {
                            Event.eventAsyncTask.cancel(true);
                        }
                        if(Opinionposts.userPost!=null) {
                            Opinionposts.userPost.cancel(true);
                        }
                        break;
                }


                LoggerGeneral.info("reselected");
            }
        });
    }

    private void setCurrentTabFragment(int tabPosition)
    {
        switch (tabPosition)
        {   case 0 :
            replaceFragment(post);
            break;
            case 1 :
                replaceFragment(opinionPost);
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                break;
            case 2 :
                replaceFragment(event);

                InputMethodManager inputMethodManager2 = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager2.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                break;
            case 3 :
                replaceFragment(announcement);
                InputMethodManager inputMethodManager3 = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager3.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                break;
        }
    }
    public void replaceFragment(Fragment fragment)
    {
        Post.postimage="";
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.frame_container, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_NONE);
        ft.commit();
    }
    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            String year1 = String.valueOf(selectedYear);
            String month1 = String.valueOf(selectedMonth + 1);
            String day1 = String.valueOf(selectedDay);

        }
    };

    View.OnClickListener homeOnclickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(mDrawerLayout.isDrawerOpen(GravityCompat.END)){
                mDrawerLayout.closeDrawers();
            }else{
                InputMethodManager inputMethodManager4 = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager4.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                mDrawerLayout.openDrawer(GravityCompat.END);
            }
        }
    };


    BroadcastReceiver broadcast_reciever2 = new BroadcastReceiver() {

        @Override
        public void onReceive(Context arg0, Intent intent) {
            String action = intent.getAction();
            if (action.equals("finish_activity2")) {

                LoggerGeneral.info("broadcast entered");
                finish();
                // DO WHATEVER YOU WANT.
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
        else if(fromNotification!=null)
        {
            super.onBackPressed();
            Intent notification=new Intent(DashBoard.this,NotificationApp.class);
            notification.putExtra("Societyid",SocietyId1);
            startActivity(notification);
            finish();
        }
        else {

            super.onBackPressed();
            Intent back = new Intent(DashBoard.this, MyProperty.class);
            PropertyAdapter.intentCheck=0;
            Common.internet_check=0;
            startActivity(back);
            finish();
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

        if(eventCheck==3)
        {
            temp_bundle.putInt("event",eventCheck);
        }
        if(announcementCheck==4)
        {
            temp_bundle.putInt("announcement",announcementCheck);
        }
        if(pollsCheck==2)
        {
            temp_bundle.putInt("polls",pollsCheck);
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


}
