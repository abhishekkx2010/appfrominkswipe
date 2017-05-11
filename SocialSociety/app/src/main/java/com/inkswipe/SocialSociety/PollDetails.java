package com.inkswipe.SocialSociety;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
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
import java.util.Calendar;
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
import util.LoggerGeneral;
import util.ServiceFacade;

public class PollDetails extends AppCompatActivity {

    TextView title;
    RadioButton option1,option2,option3,option4,option5,option6;
    Context context;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    LinearLayout home,bottom,bottom2,submit,publishPoll,sharePoll,close,delete;
    RelativeLayout editDateLayout;
    String textPollTitle,textPollQuestion,textOption1,textOption2,textOption3,textOption4,textOption5,textOption6,availEndDate,availEndDate1,availEndDate12;
    String pollImage="";
    RelativeLayout notification;
    String createPoll,view,creator;
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    DisplayImageOptions options;
    ImageView image;
    TextView creatorName,createdDate,pollTitle,pollQuestion,endDate,vote1,vote2,vote3,vote4,vote5,vote6;
    AppPreferences appPreferences;
    Calendar myCalendar;
    String SocietyId;
    String poll_id;
    Dialog dialog;
    String poll_check;


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
    String   delete_poll;





    public static Bundle temp_bundle;
    int savedInstanceCheck=0;

    String fromNotification;
    String SocietyId1;
    String fromPushNotification;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (getIntent().hasExtra("bundle") && savedInstanceState==null){
            LoggerGeneral.info("isItRight");
            savedInstanceCheck=1;
            savedInstanceState = getIntent().getExtras().getBundle("bundle");
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poll_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        context=PollDetails.this;

        myCalendar = Calendar.getInstance();
        appPreferences=AppPreferences.getAppPreferences(this);
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.fallb)
                .showImageForEmptyUri(R.mipmap.fallb)
                .showImageOnFail(R.mipmap.fallb)
                .cacheInMemory(false)
                .cacheOnDisk(false)
                .considerExifParams(true)
                .displayer(new SimpleBitmapDisplayer())
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context).build();
        ImageLoader.getInstance().init(config);

        title=((TextView) toolbar.findViewById(R.id.toolbar_title));
        title.setText("detailspoll");
        toolbar.setTitleTextColor(android.graphics.Color.WHITE);

        poll_id = getIntent().getStringExtra("poll_id");

        createPoll=getIntent().getStringExtra("createPoll");

        view=getIntent().getStringExtra("view");

        creator=getIntent().getStringExtra("creator");

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

        SocietyId = getIntent().getStringExtra("SocietyId");

        availEndDate12=getIntent().getStringExtra("toShow");

        fromNotification=getIntent().getStringExtra("fromNotification");
        SocietyId1=getIntent().getStringExtra("SocietyIdnotification");
        fromPushNotification=getIntent().getStringExtra("fromPushNotification");
        LoggerGeneral.info("HiiiiTEsat" + createPoll + "-=-=-=" + view + SocietyId + "====" + availEndDate + "===" + availEndDate1 + "--" + SocietyId +"--" + textOption4+"--" + textOption5+"--" + textOption6+"--" + textOption3);

        if(poll_id!=null)
        {
            if(creator!=null)
            {
                LoggerGeneral.info("isItRight17");
                Common.internet_check=17;
            }
            if(view!=null)
            {
                LoggerGeneral.info("isItRight18");
                Common.internet_check=18;
            }
        }
        LoggerGeneral.info("isItRight2"+creator+view+poll_id);
        if(savedInstanceCheck==1)
        {
            LoggerGeneral.info("isItRight3=="+Common.internet_check);
            if(Common.internet_check==17)
            {
                creator=savedInstanceState.getString("creator");
                SocietyId=savedInstanceState.getString("SocietyId");
                poll_id=savedInstanceState.getString("poll_id");
            }
            if(Common.internet_check==18)
            {
                LoggerGeneral.info("isItRight2");
                view=savedInstanceState.getString("view");
                SocietyId=savedInstanceState.getString("SocietyId");
                poll_id=savedInstanceState.getString("poll_id");
                LoggerGeneral.info("isItRight200000"+view+SocietyId+poll_id);
            }
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.getNavigationIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //What to do on back clicked
                if (mDrawerLayout.isDrawerOpen(GravityCompat.END)) {
                    mDrawerLayout.closeDrawers();
                }
                else if(fromPushNotification!=null)
                {
                    Intent notification=new Intent(PollDetails.this,MyProperty.class);
                    startActivity(notification);
                    finish();
                }
                else if(fromNotification!=null)
                {
                    Intent notification=new Intent(PollDetails.this,NotificationApp.class);
                    notification.putExtra("Societyid",SocietyId1);
                    startActivity(notification);
                    finish();
                }
                else {


                    if (createPoll != null) {
                        Intent back = new Intent(PollDetails.this, CreatePoll.class);
                        back.putExtra("poll_title", textPollTitle);
                        back.putExtra("poll_question", textPollQuestion);
                        back.putExtra("endDate", availEndDate);
                        back.putExtra("endDate1", availEndDate1);
                        back.putExtra("toShow", availEndDate12);
                        back.putExtra("option1", textOption1);
                        back.putExtra("option2", textOption2);
                        back.putExtra("pollImage", pollImage);
                        back.putExtra("SocietyId", SocietyId);
                        if (textOption3!=null && textOption3.length()>0) {
                            back.putExtra("option3", textOption3);
                        }
                        if (textOption4!=null && textOption4.length()>0) {
                            back.putExtra("option4", textOption4);
                        }

                        if (textOption5!=null && textOption5.length()>0) {
                            back.putExtra("option5", textOption5);
                        }

                        if (textOption6!=null && textOption6.length()>0) {
                            back.putExtra("option6", textOption6);
                        }


                        startActivity(back);
                        finish();
                    } else {
                        Intent back = new Intent(PollDetails.this, DashBoard.class);
                        back.putExtra("polls", 2);
                        back.putExtra("SocietyId", SocietyId);
                        startActivity(back);
                        finish();
                    }
                }
            }
        });

        toolbar.getNavigationIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.my_statusbar_color));
        }

        option1= (RadioButton) findViewById(R.id.option1);
        option2= (RadioButton) findViewById(R.id.option2);
        option3= (RadioButton) findViewById(R.id.option3);
        option4= (RadioButton) findViewById(R.id.option4);
        option5= (RadioButton) findViewById(R.id.option5);
        option6= (RadioButton) findViewById(R.id.option6);

        creatorName= (TextView) findViewById(R.id.creatorName);


            creatorName.setText(appPreferences.getString("user_name", ""));

        createdDate= (TextView) findViewById(R.id.createdDate);

        Calendar c = Calendar.getInstance();
        LoggerGeneral.info("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("dd MMM, yyyy");
        String formattedDate = df.format(c.getTime());

        createdDate.setText(formattedDate);

        pollTitle= (TextView) findViewById(R.id.pollTitle);

        pollQuestion= (TextView) findViewById(R.id.pollQuestion);

        endDate= (TextView) findViewById(R.id.enddate);

        vote1= (TextView) findViewById(R.id.vote1);

        vote2= (TextView) findViewById(R.id.vote2);

        vote3= (TextView) findViewById(R.id.vote3);

        vote4= (TextView) findViewById(R.id.vote4);

        vote5= (TextView) findViewById(R.id.vote5);

        vote6= (TextView) findViewById(R.id.vote6);

        image= (ImageView) findViewById(R.id.image);


        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog = new Dialog(PollDetails.this);
                dialog.setContentView(R.layout.dpdialog);
                dialog.setCanceledOnTouchOutside(true);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


                ImageView dpImage = (ImageView) dialog.findViewById(R.id.dpimage);

                ImageLoader.getInstance().displayImage(user_profile_image, dpImage, options, animateFirstListener);


                TextView  profilenm = (TextView)dialog.findViewById(R.id.profilemnm);
                profilenm.setText(duser_name);;
                ImageView profileim = (ImageView)dialog.findViewById(R.id.profileim);

                profileim.setVisibility(View.GONE);

                dialog.show();
            }
        });

            ImageLoader.getInstance().displayImage(appPreferences.getString("profile_image_url", ""), image, options, animateFirstListener);

        if(textPollTitle!=null)
        {
            pollTitle.setText(textPollTitle);
        }

        if(textPollQuestion!=null)
        {
            pollQuestion.setText(textPollQuestion);
        }

        if(availEndDate12!=null)
        {
            endDate.setText(availEndDate12);
        }

        if(textOption1!=null)
        {
            option1.setText(textOption1);
        }

        if(textOption2!=null)
        {
            option2.setText(textOption2);
        }

        if(textOption3!=null && textOption3.length()>0)
        {
            option3.setText(textOption3);
        }

        else {
            option3.setVisibility(View.GONE);
            vote3.setVisibility(View.GONE);
        }

        if(textOption4!=null && textOption4.length()>0)
        {
            option4.setText(textOption4);
        }
        else {
            option4.setVisibility(View.GONE);
            vote4.setVisibility(View.GONE);
        }

        if(textOption5!=null && textOption5.length()>0)
        {
            option5.setText(textOption5);
        }
        else {
            option5.setVisibility(View.GONE);
            vote5.setVisibility(View.GONE);
        }

        if(textOption6!=null && textOption6.length()>0)
        {
            option6.setText(textOption6);
        }
        else {
            option6.setVisibility(View.GONE);
            vote6.setVisibility(View.GONE);
        }

        option1.setTypeface(Common.font(context, "arial.ttf"));
        option2.setTypeface(Common.font(context, "arial.ttf"));
        option3.setTypeface(Common.font(context, "arial.ttf"));
        option4.setTypeface(Common.font(context, "arial.ttf"));
        option5.setTypeface(Common.font(context, "arial.ttf"));
        option6.setTypeface(Common.font(context, "arial.ttf"));

        notification= (RelativeLayout) findViewById(R.id.notification);

        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent notification=new Intent(PollDetails.this,NotificationApp.class);
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
                Intent myProperty=new Intent(PollDetails.this,MyProperty.class);
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
                        Intent profile = new Intent(PollDetails.this, Profile.class);
                        profile.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(profile);
                        finish();
                        break;
                    case 2:
                        LoggerGeneral.info("2");
                        Intent addSociety = new Intent(PollDetails.this, AddSociety.class);
                        startActivity(addSociety);
                        finish();
                        break;
                    case 3:
                        LoggerGeneral.info("3");
                        Intent myProperty = new Intent(PollDetails.this, MyProperty.class);
                        PropertyAdapter.intentCheck = 0;
                        startActivity(myProperty);
                        finish();
                        break;
                    case 4:
                        LoggerGeneral.info("4");
                        Intent events = new Intent(PollDetails.this, DashBoard.class);
                        events.putExtra("SocietyId",SocietyId);
                        events.putExtra("event", 3);
                        startActivity(events);
                        finish();
                        break;
                    case 5:
                        LoggerGeneral.info("5");
                        Intent createEvents = new Intent(PollDetails.this, CreateEvent.class);
                        createEvents.putExtra("SocietyId",SocietyId);
                        startActivity(createEvents);
                        finish();
                        break;
                    case 6:
                        LoggerGeneral.info("6");
                        Intent archivedEvents = new Intent(PollDetails.this, Archivedevent.class);
                        archivedEvents.putExtra("SocietyId",SocietyId);
                        startActivity(archivedEvents);
                        finish();
                        break;
                    case 7:
                        LoggerGeneral.info("7");
                        Intent announcement = new Intent(PollDetails.this, DashBoard.class);
                        announcement.putExtra("SocietyId",SocietyId);
                        announcement.putExtra("announcement", 4);
                        startActivity(announcement);
                        finish();
                        break;
                    case 8:
                        LoggerGeneral.info("8");
                        Intent createAnnouncement = new Intent(PollDetails.this, CreateAnnouncement.class);
                        createAnnouncement.putExtra("SocietyId",SocietyId);
                        startActivity(createAnnouncement);
                        finish();
                        break;
                    case 9:
                        LoggerGeneral.info("9");
                        Intent polls = new Intent(PollDetails.this, DashBoard.class);
                        polls.putExtra("SocietyId",SocietyId);
                        polls.putExtra("polls", 2);
                        startActivity(polls);
                        finish();
                        break;
                    case 10:
                        LoggerGeneral.info("10");
                        Intent createEventPoll = new Intent(PollDetails.this, CreatePoll.class);
                        createEventPoll.putExtra("SocietyId",SocietyId);
                        startActivity(createEventPoll);
                        finish();
                        break;
                    case 11:
                        LoggerGeneral.info("11");
                        Intent archivedEventPoll = new Intent(PollDetails.this, ArchivedPolls.class);
                        archivedEventPoll.putExtra("SocietyId",SocietyId);
                        startActivity(archivedEventPoll);
                        finish();
                        break;
                    case 12:
                        LoggerGeneral.info("12");
                        Intent members = new Intent(PollDetails.this, Members.class);
                        members.putExtra("SocietyId",SocietyId);
                        startActivity(members);
                        finish();
                        break;
                    case 13:
                        LoggerGeneral.info("13");
                        Intent offers = new Intent(PollDetails.this, Offer.class);
                        offers.putExtra("SocietyId",SocietyId);
                        startActivity(offers);
                        finish();
                        break;
                    case 14:
                        LoggerGeneral.info("14");
                        Intent complaint = new Intent(PollDetails.this, Complaint.class);
                        complaint.putExtra("SocietyId",SocietyId);
                        startActivity(complaint);
                        finish();
                        break;
                    case 15:
                        LoggerGeneral.info("15");
                        Intent notification=new Intent(PollDetails.this,NotificationApp.class);
                        //    notification.putExtra("SocietyId",Soci)
                        int test1=appPreferences.getInt("NotificationOld",0);
                        test1=test1+Constants.notififcationcount;
                        appPreferences.putInt("NotificationOld", test1);
                        notification.putExtra("SocietyId",SocietyId);
                        Constants.notififcationcount=0;
                        startActivity(notification);
                        finish();
                        break;
                    case 16:
                        LoggerGeneral.info("16");
                      /*  Intent intent = new Intent(PollDetails.this, Login.class);
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

        bottom= (LinearLayout) findViewById(R.id.bottom);
        bottom2= (LinearLayout) findViewById(R.id.bottom2);
        submit= (LinearLayout) findViewById(R.id.submit);
        editDateLayout= (RelativeLayout) findViewById(R.id.editDateLayout);
        publishPoll= (LinearLayout) findViewById(R.id.publishPoll);
        sharePoll= (LinearLayout) findViewById(R.id.SharePoll);
        close= (LinearLayout) findViewById(R.id.closeArchive);
        delete= (LinearLayout) findViewById(R.id.closeDelete);


        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                if(createPoll!=null)
                {
                    updateLabel();
                }
                else {


                    if (Common.isOnline(context)) {

                        String myFormat1 = "yyyy-MM-dd HH:mm:ss";

                        SimpleDateFormat sdf1 = new SimpleDateFormat(myFormat1, Locale.US);

                        Date now = new Date();
                        if(myCalendar.getTime().after(now)) {

                            String ed_date = sdf1.format(myCalendar.getTime());
                            new Editenddate(ed_date).execute();
                        }
                        else {
                            Common.showToast(context, "Previous date not allowed");
                        }
                    } else {
                        Common.showToast(context, "No Internet connection");
                    }
                }
            //
            }

        };




    editDateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Common.isOnline(context)) {
                    new DatePickerDialog(PollDetails.this, R.style.DialogTheme, date, myCalendar
                            .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                }
                else
                {
                    Common.showToast(context,"No internet connection");
                }
            }
        });

        if(createPoll!=null)
        {
            submit.setVisibility(View.GONE);
            bottom.setVisibility(View.GONE);
            bottom2.setVisibility(View.VISIBLE);
            editDateLayout.setVisibility(View.VISIBLE);

            vote1.setText("0 votes");
            vote2.setText("0 votes");
            vote3.setText("0 votes");
            vote4.setText("0 votes");
            vote5.setText("0 votes");
            vote6.setText("0 votes");
        }
        if(creator!=null)
        {
            submit.setVisibility(View.VISIBLE);
            bottom.setVisibility(View.VISIBLE);
            bottom2.setVisibility(View.GONE);
            editDateLayout.setVisibility(View.VISIBLE);

            if(Common.isOnline(context)){

                new  getPollDetails().execute();
            }
            else {
                Common.showToast(context,"No interent connection");
            }

        }
        if(view!=null)
        {
            submit.setVisibility(View.VISIBLE);
            bottom.setVisibility(View.GONE);
            bottom2.setVisibility(View.GONE);
            editDateLayout.setVisibility(View.GONE);

            if(Common.isOnline(context)){

                new  getPollDetails().execute();
            }
            else {
                Common.showToast(context,"No interent connection");
            }

        }

        if(fromNotification!=null)
        {
            submit.setVisibility(View.VISIBLE);
            bottom.setVisibility(View.GONE);
            bottom2.setVisibility(View.GONE);
            editDateLayout.setVisibility(View.GONE);

            if(Common.isOnline(context)){

                new  getPollDetails().execute();
            }
            else {
                Common.showToast(context,"No interent connection");
            }

        }
        if(fromPushNotification!=null)
        {
            submit.setVisibility(View.VISIBLE);
            bottom.setVisibility(View.GONE);
            bottom2.setVisibility(View.GONE);
            editDateLayout.setVisibility(View.GONE);

            if(Common.isOnline(context)){

                new  getPollDetails().execute();
            }
            else {
                Common.showToast(context,"No interent connection");
            }

        }

        publishPoll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Common.isOnline(context)) {

                    new createPoll().execute();
                }
                else {
                    Common.showToast(context,"No internet connection");
                }


            }
        });

        sharePoll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharePoll = new Intent(PollDetails.this, ShareEvent.class);
                sharePoll.putExtra("createPoll",createPoll);
                sharePoll.putExtra("poll","poll");
                sharePoll.putExtra("SocietyId",SocietyId);
                sharePoll.putExtra("poll_title",textPollTitle);
                sharePoll.putExtra("poll_question",textPollQuestion);
                sharePoll.putExtra("endDate",availEndDate);
                sharePoll.putExtra("endDate1",availEndDate1);
                sharePoll.putExtra("toShow", availEndDate12);
                sharePoll.putExtra("option1",textOption1);
                sharePoll.putExtra("option2",textOption2);
                sharePoll.putExtra("pollImage",pollImage);
                if (textOption3!=null && textOption3.length()>0) {
                    sharePoll.putExtra("option3", textOption3);
                }
                if (textOption4!=null && textOption4.length()>0) {
                    sharePoll.putExtra("option4", textOption4);
                }

                if (textOption5!=null && textOption5.length()>0) {
                    sharePoll.putExtra("option5", textOption5);
                }

                if (textOption6!=null && textOption6.length()>0) {
                    sharePoll.putExtra("option6", textOption6);
                }
                startActivity(sharePoll);
                finish();
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Intent polls = new Intent(PollDetails.this, DashBoard.class);
                polls.putExtra("polls", 2);
                startActivity(polls);
                finish();*/


                if(Common.isOnline(context)){


                    new CloseArchivePolls().execute();
                }
                else {
                    Common.showToast(context,"No internet connection");
                }
            }
        });






        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent polls = new Intent(PollDetails.this, DashBoard.class);
                polls.putExtra("polls", 2);
                startActivity(polls);
                finish();*/

                if(Common.isOnline(context)){


                    new CloseDeletePolls().execute();
                }
                else {
                    Common.showToast(context,"No internet connection");
                }
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(option1.isChecked())
                {
                    selected_option=option1.getText().toString();
                }
                if(option2.isChecked())
                {
                    selected_option=option2.getText().toString();
                }
                if(option3.isChecked())
                {
                    selected_option=option3.getText().toString();
                }
                if(option4.isChecked())
                {
                    selected_option=option4.getText().toString();
                }
                if(option5.isChecked())
                {
                    selected_option=option5.getText().toString();
                }
                if(option6.isChecked())
                {
                    selected_option=option6.getText().toString();
                }

                if(selected_option!=null && selected_option.length()>0) {

                    if (Common.isOnline(context)) {

                        new selectOption().execute();
                    } else {
                        Common.showToast(context, "No interent connection");
                    }
                }
                else {
                    Common.showToast(context, "Please select option");
                }

            }
        });

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

            mDialog = new ProgressDialog(PollDetails.this,ProgressDialog.THEME_HOLO_DARK);
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
                        Intent register   = new Intent(PollDetails.this,Login.class);
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

                    int account_status = 1;
                    if(meta.has("account_status")){
                        if(!meta.isNull("account_status")){
                            account_status = meta.getInt("account_status");
                        }
                    }

                    if(account_status==1){
                        if (status_code == 0) {


                            JSONArray  jsonArray = results.getJSONArray("data");

                            for(int i=0;i<=jsonArray.length()-1;i++) {

                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                 did          =           jsonObject.getString("id");
                                 dsociety_id  =           jsonObject.getString("society_id");
                                 poll_title    =          jsonObject.getString("poll_title");
                                 poll_question =          jsonObject.getString("poll_question");
                                 poll_end_date  =         jsonObject.getString("poll_end_date");
                                 poll_image     =         jsonObject.getString("poll_image");
                                 poll_option_1   =        jsonObject.getString("poll_option_1");
                                 poll_option_2  =         jsonObject.getString("poll_option_2");
                                 poll_option_3  =         jsonObject.getString("poll_option_3");
                                 poll_option_4  =         jsonObject.getString("poll_option_4");
                                 poll_option_5  =         jsonObject.getString("poll_option_5");
                                 poll_option_6   =        jsonObject.getString("poll_option_6");
                                 shared_with    =         jsonObject.getString("shared_with");
                                 created_by     =         jsonObject.getString("created_by");
                                 created_on     =         jsonObject.getString("created_on");
                                 dstatus        =         jsonObject.getString("status");
                                 selected_option  =       jsonObject.getString("selected_option");
                                 option1_count    =       jsonObject.getString("option1_count");
                                 option2_count    =       jsonObject.getString("option2_count");
                                 option3_count    =       jsonObject.getString("option3_count");
                                 option4_count    =       jsonObject.getString("option4_count");
                                 option5_count         =  jsonObject.getString("option5_count");
                                 option6_count         =  jsonObject.getString("option6_count");
                                 duser_name            =  jsonObject.getString("user_name");
                                 user_profile_image    =  jsonObject.getString("user_profile_image");
                                 end_date              =  jsonObject.getString("end_date");
                                 archive_poll          =  jsonObject.getString("archive_poll");
                                 delete_poll           =  jsonObject.getString("delete_poll");


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


                                creatorName.setText(duser_name);

                                ImageLoader.getInstance().displayImage(user_profile_image, image, options, animateFirstListener);


                                pollTitle.setText(poll_title);

                                pollQuestion.setText(poll_question);


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
                                    option3.setVisibility(View.VISIBLE);
                                    vote3.setVisibility(View.VISIBLE);
                                    option3.setText(poll_option_3);
                                    vote3.setText(option3_count+" Votes");
                                }
                                else {
                                    option3.setVisibility(View.GONE);
                                    vote3.setVisibility(View.GONE);
                                }
                                if(poll_option_4!=null && poll_option_4.length()>0) {
                                    option4.setVisibility(View.VISIBLE);
                                    vote4.setVisibility(View.VISIBLE);
                                    option4.setText(poll_option_4);
                                    vote4.setText(option4_count+" Votes");
                                }
                                else {
                                    option4.setVisibility(View.GONE);
                                    vote4.setVisibility(View.GONE);
                                }
                                if(poll_option_5!=null && poll_option_5.length()>0) {
                                    option5.setVisibility(View.VISIBLE);
                                    vote5.setVisibility(View.VISIBLE);
                                    option5.setText(poll_option_5);
                                    vote5.setText(option5_count+" Votes");
                                }
                                else {
                                    option5.setVisibility(View.GONE);
                                    vote5.setVisibility(View.GONE);
                                }

                                if(poll_option_6!=null && poll_option_6.length()>0) {
                                    option6.setVisibility(View.VISIBLE);
                                    vote6.setVisibility(View.VISIBLE);
                                    option6.setText(poll_option_6);
                                    vote6.setText(option6_count+" Votes");
                                }
                                else {
                                    option6.setVisibility(View.GONE);
                                    vote6.setVisibility(View.GONE);
                                }


                                if(selected_option!=null && selected_option.length()>0) {

                                    final int sdk = android.os.Build.VERSION.SDK_INT;
                                    if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                                        submit.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.fourborderlightgray));
                                    } else {
                                        submit.setBackground(context.getResources().getDrawable(R.drawable.fourborderlightgray));
                                    }

                                    submit.setEnabled(false);
                                    option1.setClickable(false);
                                    option2.setClickable(false);
                                    option3.setClickable(false);
                                    option4.setClickable(false);
                                    option5.setClickable(false);
                                    option6.setClickable(false);

                                }

                                if(selected_option.equals(option1.getText().toString()) || selected_option==option1.getText().toString())
                                {
                                    option1.setChecked(true);
                                }
                                else if(selected_option.equals(option2.getText().toString()) || selected_option==option2.getText().toString())
                                {
                                    option2.setChecked(true);
                                }
                                else if(selected_option.equals(option3.getText().toString()) || selected_option==option3.getText().toString())
                                {
                                    option3.setChecked(true);
                                }
                                else if(selected_option.equals(option4.getText().toString()) || selected_option==option4.getText().toString())
                                {
                                    option4.setChecked(true);
                                }
                                else if(selected_option.equals(option5.getText().toString()) || selected_option==option5.getText().toString())
                                {
                                    option5.setChecked(true);
                                }
                                else if(selected_option.equals(option6.getText().toString()) || selected_option==option6.getText().toString())
                                {
                                    option6.setChecked(true);
                                }



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





    class  CloseArchivePolls extends AsyncTask<String, String, JSONObject> {
        String type;
        ProgressDialog mDialog;
        String uposttext;
        String upostId;
        String ed_date;



        @Override
        protected JSONObject doInBackground(String... params) {
            // TODO Auto-generated method stub

            String url= Constants.Base+Constants.CloseAndArchive;

            JSONObject object = new JSONObject();
            try {

                //    object.put("society_id",SocietyId);
                object.put("user_id",appPreferences.getString("user_id",""));
                object.put("poll_id",poll_id);


                LoggerGeneral.info("JsonObjectPrintEditProperty" + object.toString());

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



                            Intent polls = new Intent(PollDetails.this, DashBoard.class);
                            polls.putExtra("polls", 2);
                            polls.putExtra("SocietyId",SocietyId);
                            startActivity(polls);
                            finish();

                            Common.showToast(context,"Poll closed and archived successfully");


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


    class  CloseDeletePolls extends AsyncTask<String, String, JSONObject> {
        String type;
        ProgressDialog mDialog;
        String uposttext;
        String upostId;
        String ed_date;



        @Override
        protected JSONObject doInBackground(String... params) {
            // TODO Auto-generated method stub

            String url= Constants.Base+Constants.CloseAndDelete;

            JSONObject object = new JSONObject();
            try {

                //    object.put("society_id",SocietyId);
                object.put("user_id",appPreferences.getString("user_id",""));
                object.put("poll_id",poll_id);


                LoggerGeneral.info("JsonObjectPrintEditProperty" + object.toString());

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



                            Intent polls = new Intent(PollDetails.this, DashBoard.class);
                            polls.putExtra("polls", 2);
                            polls.putExtra("SocietyId",SocietyId);
                            startActivity(polls);
                            finish();

                            Common.showToast(context,"Poll closed and deleted successfully");


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

    class  Editenddate extends AsyncTask<String, String, JSONObject> {
        String type;
        ProgressDialog mDialog;
        String uposttext;
        String upostId;
        String ed_date;

        public Editenddate(String ed_date){
            this.ed_date = ed_date;
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            // TODO Auto-generated method stub

            String url= Constants.Base+Constants.EditEnddatePolls;

            JSONObject object = new JSONObject();
            try {

                //    object.put("society_id",SocietyId);
                object.put("new_end_date",ed_date);
                object.put("poll_id",poll_id);

                object.put("user_id",appPreferences.getString("user_id",""));

                LoggerGeneral.info("JsonObjectPrintEditProperty" + object.toString());

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



                            updateLabel();


                            Common.showToast(context,"End Date changed successfully");


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
            Intent notification=new Intent(PollDetails.this,MyProperty.class);
            startActivity(notification);
            finish();
        }
        else if(fromNotification!=null)
        {
            super.onBackPressed();
            Intent notification=new Intent(PollDetails.this,NotificationApp.class);
            notification.putExtra("Societyid",SocietyId1);
            startActivity(notification);
            finish();
        }
        else {
            super.onBackPressed();
            if(createPoll!=null) {
                Intent back = new Intent(PollDetails.this, CreatePoll.class);
                back.putExtra("poll_title",textPollTitle);
                back.putExtra("poll_question",textPollQuestion);
                back.putExtra("endDate",availEndDate);
                back.putExtra("endDate1",availEndDate1);
                back.putExtra("toShow", availEndDate12);
                back.putExtra("option1",textOption1);
                back.putExtra("option2",textOption2);
                back.putExtra("pollImage",pollImage);
                back.putExtra("SocietyId",SocietyId);
                if (textOption3!=null && textOption3.length()>0) {
                    back.putExtra("option3", textOption3);
                }
                if (textOption4!=null && textOption4.length()>0) {
                    back.putExtra("option4", textOption4);
                }

                if (textOption5!=null && textOption5.length()>0) {
                    back.putExtra("option5", textOption5);
                }

                if (textOption6!=null && textOption6.length()>0) {
                    back.putExtra("option6", textOption6);
                }

                startActivity(back);
                finish();
            }
            else {

                Intent back = new Intent(PollDetails.this, DashBoard.class);
                back.putExtra("polls", 2);
                back.putExtra("SocietyId",SocietyId);
                startActivity(back);
                finish();
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

    private void updateLabel() {

        String myFormat = "dd MMM, yyyy"; //In which you need put here
        String myFormat1 = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        SimpleDateFormat sdf1 = new SimpleDateFormat(myFormat1, Locale.US);

        Date now = new Date();
        if(myCalendar.getTime().after(now)) {
            endDate.setText(sdf.format(myCalendar.getTime()));
            endDate.setTextColor(Color.parseColor("#000000"));
            availEndDate1 = sdf1.format(myCalendar.getTime());
            availEndDate = endDate.getText().toString().trim();
        }
        else {
            Common.showToast(context,"Previous date not allowed");
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
                object.put("shared_with","public");
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

            mDialog = new ProgressDialog(PollDetails.this,ProgressDialog.THEME_HOLO_DARK);
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

                            Intent polls = new Intent(PollDetails.this, DashBoard.class);
                            polls.putExtra("polls", 2);
                            polls.putExtra("SocietyId",SocietyId);
                            startActivity(polls);
                            finish();


                         /* JSONObject data = results.getJSONObject("data");

                            String property_id = data.getString("property_id");

                            appPreferences.putString("property_id",property_id);*/

                        }

                    }

                    if(account_status == 0) {
                        Intent intent = new Intent(PollDetails.this,Login.class);
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

    class  selectOption extends AsyncTask<String, String, JSONObject> {
        String type;
        ProgressDialog mDialog;
        String uposttext;
        String upostId;
        String ed_date;



        @Override
        protected JSONObject doInBackground(String... params) {
            // TODO Auto-generated method stub

            String url= Constants.Base+Constants.selectOption;

            JSONObject object = new JSONObject();
            try {

                //    object.put("society_id",SocietyId);
                object.put("user_id",appPreferences.getString("user_id",""));
                object.put("poll_id",poll_id);
                object.put("selected_option",selected_option);


                LoggerGeneral.info("JsonObjectPrintEditProperty" + object.toString());

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

                            Intent polls = new Intent(PollDetails.this, PollDetails.class);
                            polls.putExtra("poll_id",did);
                            polls.putExtra("SocietyId", SocietyId);
                            if(creator!=null)
                            {
                                polls.putExtra("creator","creator");
                            }
                            else
                            {
                                polls.putExtra("view", "view");
                            }
                            startActivity(polls);
                            finish();

                            Common.showToast(context, "Poll option selected");


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
        if(creator!=null)
        {
            temp_bundle.putString("creator",creator);
        }
        if(view!=null)
        {
            temp_bundle.putString("view","view");
        }
        if(poll_id!=null)
        {
            temp_bundle.putString("poll_id",poll_id);
        }
        if(SocietyId!=null) {
            temp_bundle.putString("SocietyId", SocietyId);
        }

        LoggerGeneral.info("isItRight242424"+view+SocietyId+poll_id);
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
