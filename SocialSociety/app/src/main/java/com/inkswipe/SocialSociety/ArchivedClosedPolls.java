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
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.inkswipe.SocialSociety.pulltozoomview.PullToZoomScrollViewEx;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
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
import java.util.Timer;
import java.util.TimerTask;

import adapter.CustomAdapter;
import adapter.PropertyAdapter;
import model.DrawerList;
import util.AppPreferences;
import util.Common;
import util.Constants;
import util.LoggerGeneral;
import util.ServiceFacade;

public class ArchivedClosedPolls extends AppCompatActivity implements OnProgressBarListener,View.OnClickListener  {

    TextView title;

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private NumberProgressBar bnp,bnp2,bnp3,bnp4,bnp5,bnp6;
    Timer timer,timer2,timer3,timer4,timer5,timer6;
    int progress=0,progress2=0,progress3=0,progress4=0,progress5=0,progress6=0;
    double total=10000;
    double voted=4000,voted2=6000,voted3=8000;
    double votednew1,votednew2,votednew3,votednew4,votednew5,votednew6;
    int percentage,percentage2,percentage3,percentage4,percentage5,percentage6;
    RelativeLayout notification;
    LinearLayout home;
    String SocietyId;
    Context context;
    AppPreferences appPreferences;
    PullToZoomScrollViewEx scrollView;


    String   did;
    String   dsociety_id;
    String   poll_title;
    String   poll_question;
    String   poll_end_date;
    String   poll_image;
    String   poll_option_1;
    String   poll_option_2;
    String   poll_option_3;
    String   poll_option_4;
    String   poll_option_5;
    String   poll_option_6;
    String   shared_with;
    String   created_by;
    String   created_on;
    String   dstatus;
    String   selected_option;
    String   option1_count;
    String   option2_count;
    String   option3_count;
    String   option4_count;
    String   option5_count;
    String   option6_count;
    String   duser_name;
    String   user_profile_image;
    String   end_date;
    String   archive_poll;
    String   delete_poll,members_count;
  //  String  poll_check;

    String poll_id;
    double total_new,total_newVotes;

    TextView creatorName,createdDate,pollTitle,pollQuestion,endDate,vote1,vote2,vote3,vote4,vote5,vote6,option1,option2,option3,option4,option5,option6;

    DisplayImageOptions   options ;
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    ImageView image;
    Dialog  dialog;

    RelativeLayout opt1,opt2,opt3,opt4,opt5,opt6;
    ImageView groupImage,groupImage2,groupImage3,groupImage4,groupImage5,groupImage6;
    ImageView dpImage,profileim;
    public static Bundle temp_bundle;
    int savedInstanceCheck=0;
    TextView dashname,profilenm;
    RelativeLayout publish1;
    Button publisButton;
    String creator;
    String goBack;
    private final String URL_TO_HIT = "http://192.168.10.201/social-society/poonam/get-user-poll";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (getIntent().hasExtra("bundle") && savedInstanceState==null){
            LoggerGeneral.info("isItRight");
            savedInstanceCheck=1;
            savedInstanceState = getIntent().getExtras().getBundle("bundle");
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_archived_closed_polls);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        context = ArchivedClosedPolls.this;
        Common.internet_check=3;
        appPreferences = AppPreferences.getAppPreferences(context);
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.fallb)
                .showImageForEmptyUri(R.mipmap.fallb)
                .showImageOnFail(R.mipmap.fallb)
                .cacheInMemory(false)
                .cacheOnDisk(false)
                .considerExifParams(true)
                .displayer(new SimpleBitmapDisplayer())
                .build();
        poll_id = getIntent().getStringExtra("pollId");

        creator = getIntent().getStringExtra("shareresult");
        goBack = getIntent().getStringExtra("goback");

        //                        LoggerGeneral.info("inone--"+list.getId());

        LoggerGeneral.info("Polllllllid1"+poll_id+savedInstanceState+"====="+savedInstanceCheck);
        if(savedInstanceCheck==1)
        {
            poll_id=savedInstanceState.getString("pollId");
            LoggerGeneral.info("Polllllllid"+poll_id);
        }
        title=((TextView) toolbar.findViewById(R.id.toolbar_title));
        title.setText("Archived Polls Closed");
        toolbar.setTitleTextColor(Color.WHITE);
        SocietyId = getIntent().getStringExtra("SocietyId");
        if (savedInstanceCheck==1)
        {
            SocietyId=savedInstanceState.getString("SocietyId");
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.getNavigationIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //What to do on back clicked
                if(creator!=null) {
                    //if (creator != null || creator.length() > 0) {
                    if(goBack.equalsIgnoreCase("dash")){
                        Intent intent = new Intent(ArchivedClosedPolls.this, DashBoard.class);
                        intent.putExtra("polls", 2);
                        intent.putExtra("SocietyId", SocietyId);
                        Common.internet_check = 0;
                        startActivity(intent);
                        finish();
                    } else {
                        Intent intent = new Intent(ArchivedClosedPolls.this, ArchivedPolls.class);
                        intent.putExtra("SocietyId", SocietyId);
                        Common.internet_check = 0;
                        startActivity(intent);
                        finish();
                    }
                }
                else {
                    Intent intent = new Intent(ArchivedClosedPolls.this, ArchivedPolls.class);
                    intent.putExtra("SocietyId", SocietyId);
                    Common.internet_check = 0;
                    startActivity(intent);
                    finish();
                }

            }
        });


    /*    publisButton = (LinearLayout) findViewById(R.id.publish1);
        publisButton.seton(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent polls = new Intent(ArchivedClosedPolls.this, DashBoard.class);
                Common.internet_check = 0;
                polls.putExtra("SocietyId", SocietyId);
                polls.putExtra("polls", 2);
                startActivity(polls);
                finish();
            }
        });*/

        toolbar.getNavigationIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        LayoutInflater inflater = getLayoutInflater();
        View listHeaderView = inflater.inflate(R.layout.header_list, null, false);

        LinearLayout navHome= (LinearLayout) listHeaderView.findViewById(R.id.Navhome);

        navHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myProperty=new Intent(ArchivedClosedPolls.this,MyProperty.class);
                PropertyAdapter.intentCheck=0;
                Common.internet_check=0;
                startActivity(myProperty);
                finish();
            }
        });









/*
        publisButton = (Button) findViewById(R.id.publisButton);
        publisButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/











        publish1 = (RelativeLayout)findViewById(R.id.publish1);
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
                        Intent profile=new Intent(ArchivedClosedPolls.this,Profile.class);
                        profile.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        Common.internet_check=0;
                        startActivity(profile);
                        finish();
                        break;
                    case 2:
                        LoggerGeneral.info("2");
                        Intent addSociety=new Intent(ArchivedClosedPolls.this,AddSociety.class);
                        Common.internet_check=0;
                        startActivity(addSociety);
                        finish();
                        break;
                    case 3:
                        LoggerGeneral.info("3");
                        Intent myProperty=new Intent(ArchivedClosedPolls.this,MyProperty.class);
                        Common.internet_check=0;
                        PropertyAdapter.intentCheck=0;
                        startActivity(myProperty);
                        finish();
                        break;
                    case 4:
                        LoggerGeneral.info("4");
                        Intent events=new Intent(ArchivedClosedPolls.this,DashBoard.class);
                        Common.internet_check=0;
                        events.putExtra("SocietyId", SocietyId);
                        events.putExtra("event",3);
                        startActivity(events);
                        finish();
                        break;
                    case 5:
                        LoggerGeneral.info("5");
                        Intent createEvents=new Intent(ArchivedClosedPolls.this,CreateEvent.class);
                        Common.internet_check=0;
                        createEvents.putExtra("SocietyId", SocietyId);
                        startActivity(createEvents);
                        finish();
                        break;
                    case 6:
                        LoggerGeneral.info("6");
                        Intent archivedEvents=new Intent(ArchivedClosedPolls.this,Archivedevent.class);
                        Common.internet_check=0;
                        archivedEvents.putExtra("SocietyId", SocietyId);
                        startActivity(archivedEvents);
                        finish();
                        break;
                    case 7:
                        LoggerGeneral.info("7");
                        Intent announcement=new Intent(ArchivedClosedPolls.this,DashBoard.class);
                        Common.internet_check=0;
                        announcement.putExtra("SocietyId", SocietyId);
                        announcement.putExtra("announcement",4);
                        startActivity(announcement);
                        finish();
                        break;
                    case 8:
                        LoggerGeneral.info("8");
                        Intent createAnnouncement=new Intent(ArchivedClosedPolls.this,CreateAnnouncement.class);
                        Common.internet_check=0;
                        createAnnouncement.putExtra("SocietyId", SocietyId);
                        startActivity(createAnnouncement);
                        finish();
                        break;
                    case 9:
                        LoggerGeneral.info("9");
                        Intent polls=new Intent(ArchivedClosedPolls.this,DashBoard.class);
                        Common.internet_check=0;
                        polls.putExtra("SocietyId", SocietyId);
                        polls.putExtra("polls",2);
                        startActivity(polls);
                        finish();
                        break;
                    case 10:
                        LoggerGeneral.info("10");
                        Intent createEventPoll=new Intent(ArchivedClosedPolls.this,CreatePoll.class);
                        Common.internet_check=0;
                        createEventPoll.putExtra("SocietyId", SocietyId);
                        startActivity(createEventPoll);
                        finish();
                        break;
                    case 11:
                        LoggerGeneral.info("11");
                        Intent archivedEventPoll=new Intent(ArchivedClosedPolls.this,ArchivedPolls.class);
                        Common.internet_check=0;
                        archivedEventPoll.putExtra("SocietyId", SocietyId);
                        startActivity(archivedEventPoll);
                        finish();
                        break;
                    case 12:
                        LoggerGeneral.info("12");
                        Intent members=new Intent(ArchivedClosedPolls.this,Members.class);
                        Common.internet_check=0;
                        members.putExtra("SocietyId", SocietyId);
                        startActivity(members);
                        finish();
                        break;
                    case 13:
                        LoggerGeneral.info("13");
                        Intent offers=new Intent(ArchivedClosedPolls.this,Offer.class);
                        Common.internet_check=0;
                        offers.putExtra("SocietyId", SocietyId);
                        startActivity(offers);
                        finish();
                        break;
                    case 14:
                        LoggerGeneral.info("14");
                        Intent complaint=new Intent(ArchivedClosedPolls.this,Complaint.class);
                        Common.internet_check=0;
                        complaint.putExtra("SocietyId", SocietyId);
                        startActivity(complaint);
                        finish();
                        break;
                    case 15:
                        LoggerGeneral.info("15");
                        Intent notification=new Intent(ArchivedClosedPolls.this,NotificationApp.class);
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
                    /*    Intent intent = new Intent(context,Login.class);
                        Common.internet_check=0;
                        appPreferences.remove("user_id");
                        appPreferences.remove("user_name");
                        appPreferences.remove("email_id");
                        appPreferences.remove("storecoverimage");
                        appPreferences.remove("storeimage");
                        appPreferences.remove("profile_image_url");
                        context.startActivity(intent);
                        ((Activity) context).finish();
*/

                        if(Common.isOnline(context)) {

                            new getLogout().execute();

                        }
                        else
                        {
                            Common.showToast(context,"No internet connection");
                        }

                        LoggerGeneral.info("16");
                        break;
                }
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.my_statusbar_color));
        }

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
                Intent notification=new Intent(ArchivedClosedPolls.this,NotificationApp.class);
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







        creatorName = (TextView )findViewById(R.id.creatorName);
        pollTitle= (TextView) findViewById(R.id.pollTitle);

        pollQuestion= (TextView) findViewById(R.id.pollQuestion);

        endDate= (TextView) findViewById(R.id.enddate);
        createdDate = (TextView)findViewById(R.id.createdDate);
        option1  =  (TextView )findViewById(R.id.option1);
        option2  =  (TextView )findViewById(R.id.option2);
        option3  =  (TextView )findViewById(R.id.option3);
        option4  =  (TextView )findViewById(R.id.option4);
        option5  =  (TextView )findViewById(R.id.option5);
        option6  =  (TextView )findViewById(R.id.option6);


        vote1  =  (TextView )findViewById(R.id.vote1);
        vote2  =  (TextView )findViewById(R.id.vote2);
        vote3  =  (TextView )findViewById(R.id.vote3);
        vote4  =  (TextView )findViewById(R.id.vote4);
        vote5  =  (TextView )findViewById(R.id.vote5);
        vote6  =  (TextView )findViewById(R.id.vote6);



        opt1  =   (RelativeLayout )findViewById(R.id.opt1);
        opt2  =   (RelativeLayout )findViewById(R.id.opt2);
        opt3  =   (RelativeLayout )findViewById(R.id.opt3);
        opt4  =   (RelativeLayout )findViewById(R.id.opt4);
        opt5  =   (RelativeLayout )findViewById(R.id.opt5);
        opt6  =   (RelativeLayout )findViewById(R.id.opt6);

  /*      publisButton = (LinearLayout) scrollView.getPullRootView().findViewById(R.id.publishpoll1);
        publisButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent preview = new Intent(ArchivedClosedPolls.this, RegisterPreview.class);
                // preview.putExtra("primage", mImage);
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


                archivedpolllist.add(archivedPolllistmodel);
            }
        });
*/
        publisButton = (Button) findViewById(R.id.publisButton);
        publisButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(Common.isOnline(context)) {
                    new removeArchive().execute();
                }else {
                    Common.showToast(context,"No internet connection");
                }

            }
        });



        image      = (ImageView)findViewById(R.id.image);

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                dialog = new Dialog(ArchivedClosedPolls.this);
                dialog.setContentView(R.layout.dpdialog);
                dialog.setCanceledOnTouchOutside(true);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


                dpImage = (ImageView) dialog.findViewById(R.id.dpimage);

                ImageLoader.getInstance().displayImage(user_profile_image, dpImage, options, animateFirstListener);


                profilenm = (TextView)dialog.findViewById(R.id.profilemnm);
                profilenm.setText(duser_name);;
                profileim = (ImageView)dialog.findViewById(R.id.profileim);

                profileim.setVisibility(View.GONE);

                dialog.show();
            }
        });


        groupImage   =  (ImageView)findViewById(R.id.groupImage);
        groupImage2  =  (ImageView)findViewById(R.id.groupImage2);
        groupImage3  =  (ImageView)findViewById(R.id.groupImage3);
        groupImage4  =  (ImageView)findViewById(R.id.groupImage4);
        groupImage5  =  (ImageView)findViewById(R.id.groupImage5);
        groupImage6  =  (ImageView)findViewById(R.id.groupImage6);

         groupImage.setOnClickListener(this);
         groupImage2.setOnClickListener(this);
         groupImage3.setOnClickListener(this);
         groupImage4.setOnClickListener(this);
         groupImage5.setOnClickListener(this);
         groupImage6.setOnClickListener(this);

        if(Common.isOnline(context)){

            new  getPollDetails().execute();
        }
        else {
            Common.showToast(context,"No internet connection");
        }



     /*   percentage  = (voted/total)*100;

        percentage2 = (voted2/total)*100;

        percentage3 = (voted3/total)*100;*/




        LoggerGeneral.info("showprog0---" + progress + "---" + percentage + "---" + voted / total);
        bnp = (NumberProgressBar) findViewById(R.id.number_progress_bar);
        bnp.setOnProgressBarListener(this);
     /*   timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        bnp.incrementProgressBy(1);

                        progress = bnp.getProgress();

                        LoggerGeneral.info("showprog---" + progress + "---" + percentage);
                        if (progress == percentage) {
                            LoggerGeneral.info("showprog2---" + progress + "---" + percentage);

                            timer.cancel();


                        }
                    }
                });
            }
        }, 450, 40);

*/

        bnp2 = (NumberProgressBar) findViewById(R.id.number_progress_bar2);
        bnp2.setOnProgressBarListener(this);
      /*  timer2 = new Timer();
        timer2.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        bnp2.incrementProgressBy(1);

                        progress2 = bnp2.getProgress();

                        if (progress2 == percentage2) {

                            timer2.cancel();


                        }
                    }
                });
            }
        }, 450, 40);*/


        bnp3 = (NumberProgressBar) findViewById(R.id.number_progress_bar3);
        bnp3.setOnProgressBarListener(this);
      /*  timer3 = new Timer();
        timer3.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        bnp3.incrementProgressBy(1);

                        progress3 = bnp3.getProgress();

                        if (progress3 == percentage3) {

                            timer3.cancel();


                        }
                    }
                });
            }
        }, 450, 40);*/

        bnp4 = (NumberProgressBar) findViewById(R.id.number_progress_bar4);
        bnp4.setOnProgressBarListener(this);
       /* timer4 = new Timer();
        timer4.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        bnp4.incrementProgressBy(1);

                        progress4 = bnp4.getProgress();

                        if (progress4 == percentage4) {

                            timer4.cancel();


                        }
                    }
                });
            }
        }, 450, 40);*/


        bnp5 = (NumberProgressBar) findViewById(R.id.number_progress_bar5);
        bnp5.setOnProgressBarListener(this);
   /*     timer5 = new Timer();
        timer5.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        bnp5.incrementProgressBy(1);

                        progress5 = bnp5.getProgress();

                        if (progress5 == percentage5) {

                            timer5.cancel();


                        }
                    }
                });
            }
        }, 450, 40);*/


        bnp6 = (NumberProgressBar) findViewById(R.id.number_progress_bar6);
        bnp6.setOnProgressBarListener(this);
      /*  timer6 = new Timer();
        timer6.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        bnp6.incrementProgressBy(1);

                        progress6 = bnp6.getProgress();

                        if (progress6 == percentage6) {

                            timer6.cancel();


                        }
                    }
                });
            }
        }, 450, 40);
*/

//        if(creator!=null) {

//            if (creator != null || creator.length() > 0) {


        LoggerGeneral.info("increator--"+creator);
        if(creator.equalsIgnoreCase("one")){

                publish1.setVisibility(View.GONE);
                groupImage.setVisibility(View.GONE);
                groupImage2.setVisibility(View.GONE);
                groupImage3.setVisibility(View.GONE);
                groupImage4.setVisibility(View.GONE);
                groupImage5.setVisibility(View.GONE);
                groupImage6.setVisibility(View.GONE);

            if(goBack.equalsIgnoreCase("arch")){
                groupImage.setVisibility(View.VISIBLE);
                groupImage2.setVisibility(View.VISIBLE);
                groupImage3.setVisibility(View.VISIBLE);
                groupImage4.setVisibility(View.VISIBLE);
                groupImage5.setVisibility(View.VISIBLE);
                groupImage6.setVisibility(View.VISIBLE);
            }


            } else {
                publish1.setVisibility(View.VISIBLE);
                groupImage.setVisibility(View.VISIBLE);
                groupImage2.setVisibility(View.VISIBLE);
                groupImage3.setVisibility(View.VISIBLE);
                groupImage4.setVisibility(View.VISIBLE);
                groupImage5.setVisibility(View.VISIBLE);
                groupImage6.setVisibility(View.VISIBLE);

            }


      //  }
  /*      else {
            publish1.setVisibility(View.VISIBLE);
            groupImage.setVisibility(View.VISIBLE);
            groupImage2.setVisibility(View.VISIBLE);
            groupImage3.setVisibility(View.VISIBLE);
            groupImage4.setVisibility(View.VISIBLE);
            groupImage5.setVisibility(View.VISIBLE);
            groupImage6.setVisibility(View.VISIBLE);

        }
*/        temp_bundle = new Bundle();
        onSaveInstanceState(temp_bundle);
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
    public void onProgressChange(int current, int max) {

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

 /*   public void grClick(View v){
        Intent intent = new Intent(ArchivedClosedPolls.this,CreateGroup.class);
        intent.putExtra("poll_id",did);
        intent.putExtra("SocietyId",dsociety_id);
   //     intent.putExtra()

        startActivity(intent);
        finish();

    }*/
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

         mDialog = new ProgressDialog(ArchivedClosedPolls.this,ProgressDialog.THEME_HOLO_DARK);
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
                     Intent register   = new Intent(ArchivedClosedPolls.this,Login.class);
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
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.groupImage:
                //  onClickList();

                if(votednew1>0) {


                    Intent intent = new Intent(ArchivedClosedPolls.this, CreateGroup.class);
                    intent.putExtra("poll_id", did);
                    intent.putExtra("SocietyId", dsociety_id);
                    intent.putExtra("poll_option", poll_option_1);
                    startActivity(intent);

                }
                else{
                    Common.showToast(context,"Minimum one vote required to create group");
                }


                break;
            case R.id.groupImage2:
                ///  onClickCityList();

                if(votednew2>0) {
                    Intent intent2 = new Intent(ArchivedClosedPolls.this, CreateGroup.class);
                    intent2.putExtra("poll_id", did);
                    intent2.putExtra("SocietyId", dsociety_id);
                    intent2.putExtra("poll_option", poll_option_2);
                    startActivity(intent2);


                }
                else{
                    Common.showToast(context,"Minimum one vote required to create group");
                }

                break;
            case R.id.groupImage3:

                if(votednew3>0) {
                    Intent intent3 = new Intent(ArchivedClosedPolls.this, CreateGroup.class);
                    intent3.putExtra("poll_id", did);
                    intent3.putExtra("SocietyId", dsociety_id);
                    intent3.putExtra("poll_option", poll_option_3);
                    startActivity(intent3);
                }
                else{
                        Common.showToast(context,"Minimum one vote required to create group");
                    }



                break;

            case R.id.groupImage4:

                if(votednew4>0) {
                    Intent intent4 = new Intent(ArchivedClosedPolls.this, CreateGroup.class);
                    intent4.putExtra("poll_id", did);
                    intent4.putExtra("SocietyId", dsociety_id);
                    intent4.putExtra("poll_option", poll_option_4);
                    startActivity(intent4);

                }
                else {
                    Common.showToast(context,"Minimum one vote required to create group");
                }
                break;

            case R.id.groupImage5:

                if(votednew5>0) {
                    Intent intent5 = new Intent(ArchivedClosedPolls.this, CreateGroup.class);
                    intent5.putExtra("poll_id", did);
                    intent5.putExtra("SocietyId", dsociety_id);
                    intent5.putExtra("poll_option", poll_option_5);
                    startActivity(intent5);

                }
                else {
                    Common.showToast(context,"Minimum one vote required to create group");
                }

                break;
            case R.id.groupImage6:

                if(votednew6>0) {
                    Intent intent6 = new Intent(ArchivedClosedPolls.this, CreateGroup.class);
                    intent6.putExtra("poll_id", did);
                    intent6.putExtra("SocietyId", dsociety_id);
                    intent6.putExtra("poll_option", poll_option_6);
                    startActivity(intent6);
                }
                else {
                    Common.showToast(context,"Minimum one vote required to create group");
                }

                break;
        }
    }

  /*  class getPollDopinion extends AsyncTask<String,String, JSONObject >{
        @Override
        protected JSONObject doInBackground(String... params) {
            return null;
        }
    }*/


    class  removeArchive extends AsyncTask<String, String, JSONObject> {
        String type;
        ProgressDialog mDialog;
        String uposttext;
        String upostId;
        String ed_date;



        @Override
        protected JSONObject doInBackground(String... params) {
            // TODO Auto-generated method stub

            String url= Constants.Base+Constants.publishPollResult;

            JSONObject object = new JSONObject();
            try {

                //   object.put("society_id",SocietyId);
                object.put("user_id",appPreferences.getString("user_id",""));
                object.put("poll_id",poll_id);
                object.put("society_id",dsociety_id);
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
                mDialog.dismiss();



                Intent intent  =new Intent(ArchivedClosedPolls.this,DashBoard.class);

                intent.putExtra("polls",2);
                intent.putExtra("SocietyId",dsociety_id);
                startActivity(intent);
                finish();

            }
        }


    }


    class  getPollDetails extends AsyncTask<String, String, JSONObject> {
        String type;
        ProgressDialog mDialog;
        String uposttext;
        String upostId;
        String ed_date;



        @Override
        protected JSONObject doInBackground(String... params) {
            // TODO Auto-generated method stub

            String url= Constants.Base+Constants.GetPollDetails;

            JSONObject object = new JSONObject();
            try {

                //   object.put("society_id",SocietyId);
                object.put("user_id",appPreferences.getString("user_id",""));
                object.put("poll_id",poll_id);
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
               //     int pollstatus = meta.getInt("pollstatus");

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


                                /*Intent polls = new Intent(ArchivedClosedPolls.this, DashBoard.class);
                                Common.internet_check = 0;
                                polls.putExtra("SocietyId", SocietyId);
                                polls.putExtra("polls", 2);
                                startActivity(polls);
                                finish();*/

                                did              =     jsonObject.getString("id");
                                dsociety_id      =     jsonObject.getString("society_id");
                                poll_title        =    jsonObject.getString("poll_title");
                                poll_question     =    jsonObject.getString("poll_question");
                                poll_end_date     =    jsonObject.getString("poll_end_date");
                                poll_image        =    jsonObject.getString("poll_image");
                                poll_option_1      =   jsonObject.getString("poll_option_1");
                                poll_option_2     =    jsonObject.getString("poll_option_2");
                                poll_option_3     =    jsonObject.getString("poll_option_3");
                                poll_option_4     =    jsonObject.getString("poll_option_4");
                                poll_option_5     =    jsonObject.getString("poll_option_5");
                                poll_option_6      =   jsonObject.getString("poll_option_6");
                                shared_with       =    jsonObject.getString("shared_with");
                                created_by        =    jsonObject.getString("created_by");
                                created_on        =    jsonObject.getString("created_on");
                                dstatus           =    jsonObject.getString("status");
                                selected_option     =  jsonObject.getString("selected_option");
                                option1_count       =  jsonObject.getString("option1_count");
                                option2_count       =  jsonObject.getString("option2_count");
                                option3_count       =  jsonObject.getString("option3_count");
                                option4_count       =  jsonObject.getString("option4_count");
                                option5_count        = jsonObject.getString("option5_count");
                                option6_count        = jsonObject.getString("option6_count");
                                duser_name           = jsonObject.getString("user_name");
                                user_profile_image   = jsonObject.getString("user_profile_image");
                                end_date             = jsonObject.getString("end_date");
                                archive_poll         = jsonObject.getString("archive_poll");
                                delete_poll          = jsonObject.getString("delete_poll");
                                //poll_check   = jsonObject.getString("pollcheck ");
                             //   members_count        = jsonObject.getString("members_count");




                                String myFormat1 = "yyyy-MM-dd hh:mm:ss";
                                String myFormat = "dd MMM, yyyy";
                                SimpleDateFormat sdf1 = new SimpleDateFormat(myFormat1, Locale.US);
                                Date myDate = null;
                                try {
                                    myDate = sdf1.parse(created_on);

                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);


                                created_on = sdf.format(myDate);

                                String myFormat11 = "yyyy-MM-dd hh:mm:ss";
                                String myFormat12 = "dd MMM, yyyy";
                                SimpleDateFormat sdf11 = new SimpleDateFormat(myFormat11, Locale.US);
                                SimpleDateFormat sdf12 = new SimpleDateFormat(myFormat12, Locale.US);

                                Date myDate1 = null;
                                try {
                                    myDate1 = sdf11.parse(poll_end_date);

                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                poll_end_date=sdf12.format(myDate1);

                                creatorName.setText(duser_name);

                                ImageLoader.getInstance().displayImage(user_profile_image, image, options, animateFirstListener);


                                pollTitle.setText(poll_title);

                                pollQuestion.setText(poll_question);

                                endDate.setText(poll_end_date);
                                createdDate.setText(created_on);

                                if(poll_option_1!=null && poll_option_1.length()>0) {
                                    option1.setText(poll_option_1);

                                    vote1.setText(option1_count+" Votes");
                                }
                                if(poll_option_2!=null && poll_option_2.length()>0) {
                                    option2.setText(poll_option_2);
                                    vote2.setText(option2_count+" Votes");
                                }
                                if(poll_option_3!=null && poll_option_3.length()>0) {

                                    option3.setText(poll_option_3);
                                    vote3.setText(option3_count+" Votes");
                                }
                                else {
                                    opt3.setVisibility(View.GONE);
                                    option3.setVisibility(View.GONE);
                                    vote3.setVisibility(View.GONE);
                                }
                                if(poll_option_4!=null && poll_option_4.length()>0) {
                                    option4.setText(poll_option_4);
                                    vote4.setText(option4_count+" Votes");
                                }
                                else {
                                    opt4.setVisibility(View.GONE);
                                    option4.setVisibility(View.GONE);
                                    vote4.setVisibility(View.GONE);
                                }
                                if(poll_option_5!=null && poll_option_5.length()>0) {
                                    option5.setText(poll_option_5);
                                    vote5.setText(option5_count+" Votes");
                                }
                                else {
                                    opt5.setVisibility(View.GONE);
                                    option5.setVisibility(View.GONE);
                                    vote5.setVisibility(View.GONE);
                                }

                                if(poll_option_6!=null && poll_option_6.length()>0) {
                                    option6.setText(poll_option_6);
                                    vote6.setText(option6_count+" Votes");
                                }
                                else {
                                    opt6.setVisibility(View.GONE);
                                    option6.setVisibility(View.GONE);
                                    vote6.setVisibility(View.GONE);
                                }



                                votednew1 = Integer.parseInt(option1_count);
                                votednew2 = Integer.parseInt(option2_count);
                                votednew3 = Integer.parseInt(option3_count);
                                votednew4 = Integer.parseInt(option4_count);
                                votednew5 = Integer.parseInt(option5_count);
                                votednew6 = Integer.parseInt(option6_count);


                                total_newVotes = votednew1+votednew2+votednew3+votednew4+votednew5+votednew6;


                                percentage  = (int) ((votednew1/total_newVotes)*100);

                                percentage2 = (int) ((votednew2/total_newVotes)*100);

                                percentage3 = (int) ((votednew3/total_newVotes)*100);

                                percentage4 = (int) ((votednew4/total_newVotes)*100);

                                percentage5 = (int) ((votednew5/total_newVotes)*100);

                                percentage6  = (int) ((votednew6/total_newVotes)*100);


                                LoggerGeneral.info("votes----"+total_new+"---"+percentage+"==="+votednew1+"----"+votednew2+"---"+percentage2+"===="+total_newVotes+"---"+votednew4+"---"+percentage4);

                                timer = new Timer();
                                    timer.schedule(new TimerTask() {
                                        @Override
                                        public void run() {
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {


                                                    if(votednew1>0){
                                                    bnp.incrementProgressBy(1);

                                                    progress = bnp.getProgress();

                                                    LoggerGeneral.info("showprog---" + progress + "---" + percentage);
                                                    if (progress == percentage) {
                                                        LoggerGeneral.info("showprog2---" + progress + "---" + percentage);

                                                        timer.cancel();


                                                    }
                                                }
                                                }
                                            });
                                        }
                                    }, 200, 10);

                                timer2 = new Timer();
                                timer2.schedule(new TimerTask() {
                                    @Override
                                    public void run() {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {

                                                if(votednew2>0){
                                                bnp2.incrementProgressBy(1);

                                                progress2 = bnp2.getProgress();
                                                    LoggerGeneral.info("showprog2---" + progress2 + "---" + percentage2);

                                                if (progress2 == percentage2) {
                                                    LoggerGeneral.info("showprogcancel2---" + progress2 + "---" + percentage2);
                                                    timer2.cancel();
                                                    timer2.purge();

                                                }
                                            }
                                            }
                                        });
                                    }
                                }, 200, 10);



                                timer3 = new Timer();
                                timer3.schedule(new TimerTask() {
                                    @Override
                                    public void run() {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                if(votednew3>0){

                                                bnp3.incrementProgressBy(1);

                                                progress3 = bnp3.getProgress();
                                                    LoggerGeneral.info("showprog3---" + progress3 + "---" + percentage3);

                                                if (progress3 == percentage3) {

                                                    timer3.cancel();


                                                }
                                            }
                                            }
                                        });
                                    }
                                }, 200, 10);

                                timer4 = new Timer();
                                timer4.schedule(new TimerTask() {
                                    @Override
                                    public void run() {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {

                                                if(votednew4>0){
                                                bnp4.incrementProgressBy(1);

                                                progress4 = bnp4.getProgress();
                                                    LoggerGeneral.info("showprog4---" + progress4 + "---" + percentage4);

                                                if (progress4 == percentage4) {

                                                    timer4.cancel();


                                                }
                                            }
                                            }
                                        });
                                    }
                                }, 200, 10);

                                timer5 = new Timer();
                                timer5.schedule(new TimerTask() {
                                    @Override
                                    public void run() {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                if(votednew5!=0){

                                                bnp5.incrementProgressBy(1);

                                                progress5 = bnp5.getProgress();

                                                if (progress5 == percentage5) {

                                                    timer5.cancel();


                                                }
                                            }
                                            }
                                        });
                                    }
                                }, 200, 10);

                                timer6 = new Timer();
                                timer6.schedule(new TimerTask() {
                                    @Override
                                    public void run() {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {

                                                if(votednew6!=0){
                                                bnp6.incrementProgressBy(1);

                                                progress6 = bnp6.getProgress();

                                                if (progress6 == percentage6) {

                                                    timer6.cancel();


                                                }
                                            }
                                            }
                                        });
                                    }
                                }, 200, 10);

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

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current game state
        if(poll_id!=null) {
            temp_bundle.putString("pollId", poll_id);
        }
        if(SocietyId!=null) {
            temp_bundle.putString("SocietyId", SocietyId);
        }
        if(creator!=null){
            temp_bundle.putString("shareresult", creator);
        }
        if(goBack!=null){
            temp_bundle.putString("goback",goBack);
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



    @Override
    public void onBackPressed() {

        super.onBackPressed();

        if(creator!=null) {
          //  if (creator != null || creator.length() > 0) {
            if(goBack.equalsIgnoreCase("dash")){
                Intent intent = new Intent(ArchivedClosedPolls.this, DashBoard.class);
                intent.putExtra("polls", 2);
                intent.putExtra("SocietyId", SocietyId);
                Common.internet_check = 0;
                startActivity(intent);
                finish();
            } else {
                Intent intent = new Intent(ArchivedClosedPolls.this, ArchivedPolls.class);
                intent.putExtra("SocietyId", SocietyId);
                Common.internet_check = 0;
                startActivity(intent);
                finish();
            }
        }
        else {
            Intent intent = new Intent(ArchivedClosedPolls.this, ArchivedPolls.class);
            intent.putExtra("SocietyId", SocietyId);
            Common.internet_check = 0;
            startActivity(intent);
            finish();
        }

    }
}
