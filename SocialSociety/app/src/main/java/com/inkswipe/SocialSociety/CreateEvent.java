package com.inkswipe.SocialSociety;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.flurry.android.FlurryAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import adapter.CropOptionAdapter;
import adapter.CustomAdapter;
import adapter.PropertyAdapter;
import model.CityModel;
import model.CropOption;
import model.DrawerList;
import model.StateModel;
import util.AppPreferences;
import util.Common;
import util.Constants;
import util.ExceptionHandler;
import util.LoggerGeneral;
import util.ServiceFacade;

public class CreateEvent extends AppCompatActivity  implements View.OnClickListener{

    LinearLayout home;
    Fragment fragment = null;
    TextView appname;

    List<StateModel> statesarray;
    List<CityModel> cityarray;

    String[] mStringArray, mstrinArraystid;
    String[] mcityStringArray, mcitystrinArraystid;

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;

    Context context;

    LinearLayout bordercreateevent;

    LinearLayout preview,addevent;

    EditText eventTitle,eventAddress,landmark,pincode,description;

    TextView eventDate,EventTime,pretxt;

    TextView eventDescription,uploadPhoto,state,city;

    String android_id, state_id, country_id;

    RelativeLayout notification;
    String SocietyId;
    Calendar myCalendar;

    public static int permissionChecker=0;
    private static final int PERMISSION_REQUEST_CODE = 1;
    Uri imageUri;
    static final int requestCodeForSdCard = 1, requestCodeForCamera = 2, requestCodeForCorp = 3;

    int slecthour,selectminuite;

    String dateofevent;
    String event;
    AppPreferences appPreferences;
    String savestateid;
    String eventImage="",eventTitleText,evenTexttDate,eventDate1,eventDate2,eventTextTime,evenTextDescription,evenTextAddress,evenTextLandmark,evenTextState,evenTextCity,evenTextPincode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        setContentView(R.layout.activity_create_event);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context=CreateEvent.this;
        appPreferences=AppPreferences.getAppPreferences(this);
        myCalendar = Calendar.getInstance();
        TextView title=((TextView) toolbar.findViewById(R.id.toolbar_title));
        title.setText("Create Event");
        toolbar.setTitleTextColor(android.graphics.Color.WHITE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.my_statusbar_color));
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        SocietyId = getIntent().getStringExtra("SocietyId");
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
        eventDate2=getIntent().getStringExtra("sendApi");

        dateofevent  =getIntent().getStringExtra("dateofevent");
        event        = getIntent().getStringExtra("event");



    //    toolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.backarrow));

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
                else if(dateofevent!=null){

                    Intent back = new Intent(CreateEvent.this, EventsList.class);
                    back.putExtra("SocietyId", SocietyId);

                    back.putExtra("dateofevent",dateofevent);
                    back.putExtra("event",event);
                    startActivity(back);
                    finish();
                }
                else {

                    Intent back = new Intent(CreateEvent.this, DashBoard.class);
                    back.putExtra("SocietyId", SocietyId);
                    back.putExtra("event",3);
                    startActivity(back);
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
                Intent notification=new Intent(CreateEvent.this,NotificationApp.class);
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
                Intent myProperty=new Intent(CreateEvent.this,MyProperty.class);
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
                        Intent profile = new Intent(CreateEvent.this, Profile.class);
                        profile.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(profile);
                        finish();
                        break;
                    case 2:
                        LoggerGeneral.info("2");
                        Intent addSociety = new Intent(CreateEvent.this, AddSociety.class);
                        startActivity(addSociety);
                        finish();
                        break;
                    case 3:
                        LoggerGeneral.info("3");
                        Intent myProperty = new Intent(CreateEvent.this, MyProperty.class);
                        PropertyAdapter.intentCheck = 0;
                        startActivity(myProperty);
                        finish();
                        break;
                    case 4:
                        LoggerGeneral.info("4");
                        Intent events = new Intent(CreateEvent.this, DashBoard.class);
                        events.putExtra("SocietyId", SocietyId);
                        events.putExtra("event", 3);
                        startActivity(events);
                        finish();
                        break;
                    case 5:
                        LoggerGeneral.info("5");
                       /* Intent createEvents=new Intent(Profile.this,CreateEvent.class);
                        startActivity(createEvents);
                        finish();*/
                        mDrawerLayout.closeDrawers();
                        break;
                    case 6:
                        LoggerGeneral.info("6");
                        Intent archivedEvents = new Intent(CreateEvent.this, Archivedevent.class);
                        archivedEvents.putExtra("SocietyId", SocietyId);
                        startActivity(archivedEvents);
                        finish();
                        break;
                    case 7:
                        LoggerGeneral.info("7");
                        Intent announcement = new Intent(CreateEvent.this, DashBoard.class);
                        announcement.putExtra("SocietyId", SocietyId);
                        announcement.putExtra("announcement", 4);
                        startActivity(announcement);
                        finish();
                        break;
                    case 8:
                        LoggerGeneral.info("8");
                        Intent createAnnouncement = new Intent(CreateEvent.this, CreateAnnouncement.class);
                        createAnnouncement.putExtra("SocietyId", SocietyId);
                        startActivity(createAnnouncement);
                        finish();
                        break;
                    case 9:
                        LoggerGeneral.info("9");
                        Intent polls = new Intent(CreateEvent.this, DashBoard.class);
                        polls.putExtra("SocietyId", SocietyId);
                        polls.putExtra("polls", 2);
                        startActivity(polls);
                        finish();
                        break;
                    case 10:
                        LoggerGeneral.info("10");
                        Intent createEventPoll = new Intent(CreateEvent.this, CreatePoll.class);
                        createEventPoll.putExtra("SocietyId", SocietyId);
                        startActivity(createEventPoll);
                        finish();
                        break;
                    case 11:
                        LoggerGeneral.info("11");
                        Intent archivedEventPoll = new Intent(CreateEvent.this, ArchivedPolls.class);
                        archivedEventPoll.putExtra("SocietyId", SocietyId);
                        startActivity(archivedEventPoll);
                        finish();
                        break;
                    case 12:
                        LoggerGeneral.info("12");
                        Intent members = new Intent(CreateEvent.this, Members.class);
                        members.putExtra("SocietyId", SocietyId);
                        startActivity(members);
                        finish();
                        break;
                    case 13:
                        LoggerGeneral.info("13");
                        Intent offers = new Intent(CreateEvent.this, Offer.class);
                        offers.putExtra("SocietyId", SocietyId);
                        startActivity(offers);
                        finish();
                        break;
                    case 14:
                        LoggerGeneral.info("14");
                        Intent complaint = new Intent(CreateEvent.this, Complaint.class);
                        complaint.putExtra("SocietyId", SocietyId);
                        startActivity(complaint);
                        finish();
                        break;
                    case 15:
                        Intent notification=new Intent(CreateEvent.this,NotificationApp.class);
                        //    notification.putExtra("SocietyId",Soci)
                        int test1=appPreferences.getInt("NotificationOld",0);
                        test1=test1+Constants.notififcationcount;
                        appPreferences.putInt("NotificationOld", test1);
                        notification.putExtra("SocietyId", SocietyId);
                        Constants.notififcationcount=0;
                        startActivity(notification);
                        finish();
                        break;
                    case 16:
                       /* LoggerGeneral.info("16");
                        Intent intent = new Intent(CreateEvent.this, Login.class);
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

        pretxt= (TextView) findViewById(R.id.pretxt);

        eventTitle= (EditText) findViewById(R.id.eventtitle);

        eventDate= (TextView) findViewById(R.id.eventdate);

        EventTime= (TextView) findViewById(R.id.eventtime);

        description= (EditText) findViewById(R.id.description);

        eventAddress= (EditText) findViewById(R.id.address);

        landmark= (EditText) findViewById(R.id.landmark);

        pincode= (EditText) findViewById(R.id.pincode);

        eventTitle.setTypeface(Common.font(context, "arial.ttf"));

        eventDate.setTypeface(Common.font(context, "arial.ttf"));
        EventTime.setTypeface(Common.font(context, "arial.ttf"));
        description.setTypeface(Common.font(context, "arial.ttf"));
        eventAddress.setTypeface(Common.font(context, "arial.ttf"));
        landmark.setTypeface(Common.font(context, "arial.ttf"));

        pincode.setTypeface(Common.font(context, "arial.ttf"));

        state= (TextView) findViewById(R.id.state);
        city= (TextView) findViewById(R.id.city);

        uploadPhoto= (TextView) findViewById(R.id.uploadPhoto);

        eventDescription= (TextView) findViewById(R.id.enterDescription);

        state.setTypeface(Common.font(context, "arial.ttf"));

        city.setTypeface(Common.font(context, "arial.ttf"));

        pretxt.setText("Preview & Share");

        state.setOnClickListener(this);
        city.setOnClickListener(this);

        if(event!=null)
        {

            if(event.equals("yesevent")||event=="yesevent"){


                LoggerGeneral.info("showeve");
            }

            if(event.equals("noevent")||event=="noevent"){
                LoggerGeneral.info("noshoweve");

                String myFormat1 = "dd MMM, yyyy";
                String myFormat = "yyyy-MM-dd";
                String myFormat2 = "dd-MM-yyyy";
                SimpleDateFormat sdf1 = new SimpleDateFormat(myFormat, Locale.US);
                Date myDate = null;
                try {
                    myDate = sdf1.parse(dateofevent);

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat1, Locale.US);
                SimpleDateFormat sdf2 = new SimpleDateFormat(myFormat2, Locale.US);
                Date now = new Date();
                if(myDate.after(now)) {

                    eventDate.setText(sdf2.format(myDate));
                    eventDate.setClickable(true);
                    eventDate1 = sdf.format(myDate);
                    eventDate2 = sdf2.format(myDate);
                }
                else {
                    Common.showToast(context, "Previous date not allowed");
                }


                LoggerGeneral.info("changesd fpormat of date"+eventDate1);
            }

            if (dateofevent!=null)
            {
                LoggerGeneral.info("noshoweve");

                String myFormat1 = "dd MMM, yyyy";
                String myFormat = "yyyy-MM-dd";
                String myFormat2 = "dd-MM-yyyy";
                SimpleDateFormat sdf1 = new SimpleDateFormat(myFormat, Locale.US);
                Date myDate = null;
                try {
                    myDate = sdf1.parse(dateofevent);

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat1, Locale.US);
                SimpleDateFormat sdf2 = new SimpleDateFormat(myFormat2, Locale.US);
                Date now = new Date();
                if(myDate.after(now)) {

                    eventDate.setText(sdf2.format(myDate));
                    eventDate.setClickable(true);
                    eventDate1 = sdf.format(myDate);
                    eventDate2 = dateofevent;
                }
                else {
                    Common.showToast(context, "Previous date not allowed");
                }

            }
        }

        uploadPhoto.setTypeface(Common.font(context, "arial.ttf"));
        String udata1 = "Upload photo";
        SpannableString content1 = new SpannableString(udata1);
        content1.setSpan(new UnderlineSpan(), 0, udata1.length(), 0);
        uploadPhoto.setText(content1);
        uploadPhoto.setTypeface(Common.font(context, "arial.ttf"));
        uploadPhoto.setTextColor(getResources().getColor(R.color.color_primary));

        uploadPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                if (Build.VERSION.SDK_INT >= 23) {
                    // Do some stuff


                    if (!checkPermission2()) {

                        requestPermission();

                    } else {
                        selectImage();
                    }
                } else {
                    selectImage();
                }
            }
        });


        eventDescription.setTypeface(Common.font(context, "arial.ttf"));

        bordercreateevent= (LinearLayout) findViewById(R.id.bordercreateevent);

        bordercreateevent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                description.requestFocus();

                final InputMethodManager inputMethodManager =
                        (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);

                inputMethodManager.showSoftInput(description, InputMethodManager.SHOW_FORCED);
            }
        });

        preview = (LinearLayout)findViewById(R.id.preview);
        preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                eventTitleText=eventTitle.getText().toString();
                evenTexttDate=eventDate.getText().toString();
                //eventTextTime=EventTime.getText().toString();
                evenTextDescription=description.getText().toString();
                evenTextAddress=eventAddress.getText().toString();
                evenTextLandmark=landmark.getText().toString();
                evenTextState=state.getText().toString();
                evenTextCity=city.getText().toString();
                evenTextPincode=pincode.getText().toString();

                if(eventTitleText!=null && eventTitleText.length()>0)
                {
                    if(evenTexttDate!=null && evenTexttDate.length()>0)
                    {
                        if(eventTextTime!=null && eventTextTime.length()>0)
                        {
                            if(evenTextDescription!=null && evenTextDescription.length()>0)
                            {
                                if(evenTextAddress!=null && evenTextAddress.length()>0)
                                {
                                    if(evenTextLandmark!=null && evenTextLandmark.length()>0)
                                    {
                                        if(!state.getText().toString().equalsIgnoreCase("State") && state.length()>0)
                                        {
                                            if(!(city.getText().toString().equalsIgnoreCase("Select City")) && city.length()>0)
                                            {
                                                if(evenTextPincode!=null && evenTextPincode.length()>0)
                                                {
                                                    if(!evenTextPincode.matches("[0]+") && evenTextPincode.length() ==6)
                                                    {
                                                    Intent preview = new Intent(CreateEvent.this, EventDetails.class);
                                                    preview.putExtra("preview","preview");
                                                    preview.putExtra("SocietyId",SocietyId);
                                                    preview.putExtra("eventTitle",eventTitleText);
                                                    preview.putExtra("eventDate",evenTexttDate);
                                                    preview.putExtra("eventTime",eventTextTime);
                                                    preview.putExtra("eventdescription",evenTextDescription);
                                                    preview.putExtra("eventAddress",evenTextAddress);
                                                    preview.putExtra("eventLandmark",evenTextLandmark);
                                                    preview.putExtra("eventState",evenTextState);
                                                    preview.putExtra("eventCity",evenTextCity);
                                                    preview.putExtra("eventPincode",evenTextPincode);
                                                    preview.putExtra("eventHour",slecthour);
                                                    preview.putExtra("eventMiniute",selectminuite);
                                                    preview.putExtra("eventTimeToDet",eventDate1);
                                                    preview.putExtra("sendApi",eventDate2);
                                                    preview.putExtra("eventImage",eventImage);
                                                    startActivity(preview);
                                                    finish();
                                                }
                                                else {
                                                        Common.showToast(context, "Invalid pincode");
                                                  }

                                                }
                                                else {
                                                    Common.showToast(context, "Enter pincode");
                                                }

                                            }
                                            else {
                                                Common.showToast(context, "Select city");
                                            }

                                        }
                                        else {
                                            Common.showToast(context, "Select state");
                                        }
                                    }
                                    else {
                                        Common.showToast(context, "Enter Event landmark");
                                    }
                                }
                                else {
                                    Common.showToast(context, "Enter Event address");
                                }
                            }
                            else {
                                Common.showToast(context, "Enter Event description");
                            }
                        }
                        else {
                            Common.showToast(context, "Select Event time");
                        }
                    }
                    else {
                        Common.showToast(context, "Select Event date");
                    }
                }
                else {
                    Common.showToast(context, "Enter Event title");
                }





               /* Intent preview = new Intent(CreateEvent.this, EventDetails.class);
                preview.putExtra("preview","preview");
                startActivity(preview);
                finish();*/
            }
        });

        EventTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(CreateEvent.this,R.style.DialogTheme1, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        if(selectedHour>12)
                        {
                            EventTime.setText( (selectedHour-12) + ":" + selectedMinute + ":00 PM" );
                        }
                        else {
                            EventTime.setText(selectedHour + ":" + selectedMinute + ":00 AM");
                        }
                        slecthour=selectedHour;
                        selectminuite=selectedMinute;
                            eventTextTime=selectedHour + ":" + selectedMinute + ":00";
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                try {
                    updateLabel();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

        };



        eventDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(CreateEvent.this,R.style.DialogTheme, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        addevent = (LinearLayout)findViewById(R.id.addevent);
        addevent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addevent = new Intent(CreateEvent.this, DashBoard.class);
                addevent.putExtra("event",3);
                startActivity(addevent);
                finish();
            }
        });


        if(eventTitleText!=null)
        {
            eventTitle.setText(eventTitleText);
        }

        if(evenTexttDate!=null)
        {
            eventDate.setText(evenTexttDate);
        }
        if(eventTextTime!=null)
        {
            if(slecthour>12)
            {
                EventTime.setText( (slecthour-12) + ":" + selectminuite + ":00 PM" );
            }
            else {
                EventTime.setText(slecthour + ":" + selectminuite + ":00 AM");
            }
        }

        if(evenTextDescription!=null)
        {
            description.setText(evenTextDescription);
        }

        if(evenTextAddress!=null)
        {
            eventAddress.setText(evenTextAddress);
        }

        if(evenTextLandmark!=null)
        {
            landmark.setText(evenTextLandmark);
        }
        if(evenTextState!=null)
        {
            state.setText(evenTextState);
        }
        if(evenTextCity!=null)
        {
            city.setText(evenTextCity);
        }
        if(evenTextPincode!=null)
        {
            pincode.setText(evenTextPincode);
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

            mDialog = new ProgressDialog(CreateEvent.this,ProgressDialog.THEME_HOLO_DARK);
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
                        Intent register   = new Intent(CreateEvent.this,Login.class);
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
        else if(dateofevent!=null){
            super.onBackPressed();
            Intent back = new Intent(CreateEvent.this, EventsList.class);
            back.putExtra("SocietyId", SocietyId);

            back.putExtra("dateofevent",dateofevent);
            back.putExtra("event",event);
            startActivity(back);
            finish();
        }
        else {

            super.onBackPressed();
            Intent back = new Intent(CreateEvent.this, DashBoard.class);
            back.putExtra("SocietyId", SocietyId);
            back.putExtra("event",3);
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

    }

    @Override
    protected void onStop() {
        super.onStop();
        FlurryAgent.onEndSession(this);
    }

    private void updateLabel() throws ParseException {

        String myFormat = "dd-MM-yyyy"; //In which you need put here
        String myFormat1 = "dd MMM, yyyy";
        String myFormat2= "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        SimpleDateFormat sdf1 = new SimpleDateFormat(myFormat1, Locale.US);
        SimpleDateFormat sdf2 = new SimpleDateFormat(myFormat2, Locale.US);
        Date now = new Date();
        if(myCalendar.getTime().after(now)) {
            eventDate.setText(sdf.format(myCalendar.getTime()));
            eventDate.setTextColor(Color.parseColor("#000000"));
            eventDate1 = sdf1.format(myCalendar.getTime());
            eventDate2 = sdf2.format(myCalendar.getTime());
            //availEndDate = endDate.getText().toString().trim();
        }
        else {
            Common.showToast(context,"Previous date not allowed");
        }
    }

    private void requestPermission() {

        if (permissionChecker == 1) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                permissionChecker = 0;
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);

            } else {
                permissionChecker = 0;
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
            }
        } else {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.CAMERA)) {

                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, PERMISSION_REQUEST_CODE);

            } else {

                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, PERMISSION_REQUEST_CODE);
            }
        }
    }


    private boolean checkPermission2() {
        int result = ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA);
        if (result == PackageManager.PERMISSION_GRANTED) {

            return true;

        } else {

            return false;

        }
    }

    private void selectImage(){


        View view = getLayoutInflater ().inflate (R.layout.alertimage1, null);
        LinearLayout camera = (LinearLayout)view.findViewById( R.id.camera);
        LinearLayout gallery = (LinearLayout)view.findViewById( R.id.gallery);
        LinearLayout cancel = (LinearLayout)view.findViewById( R.id.cancel);

        final Dialog mBottomSheetDialog = new Dialog (CreateEvent.this,
                R.style.MaterialDialogSheet);
        mBottomSheetDialog.setContentView (view);
        mBottomSheetDialog.setCancelable(true);
        mBottomSheetDialog.getWindow ().setLayout(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mBottomSheetDialog.getWindow ().setGravity(Gravity.BOTTOM);
        mBottomSheetDialog.show();


        camera.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mBottomSheetDialog.cancel();
                if (Build.VERSION.SDK_INT >= 23) {
                    // Do some stuff



                    if (!checkPermission3()) {
                        permissionChecker = 1;
                        requestPermission();

                    } else {

                        File f = new File(Common.getChacheDir(context), "abc.jpg");
                        if (f.exists()) {
                            f.delete();
                        }

                        f = Common.createNewFileOrOverwrite(Common.getChacheDir(context), "abc.jpg");
                        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        imageUri = Uri.fromFile(f);
                        i.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                        startActivityForResult(i, requestCodeForCamera);
                    }
                } else {


                    File f = new File(Common.getChacheDir(context), "abc.jpg");
                    if (f.exists()) {
                        f.delete();
                    }

                    f = Common.createNewFileOrOverwrite(Common.getChacheDir(context), "abc.jpg");
                    Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    imageUri = Uri.fromFile(f);
                    i.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    startActivityForResult(i, requestCodeForCamera);
                }
            }
        });

        gallery.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mBottomSheetDialog.cancel();
                if (Build.VERSION.SDK_INT >= 23) {
                    // Do some stuff



                    if (!checkPermission3()) {
                        permissionChecker = 1;
                        requestPermission();

                    } else {

                        Intent gallery_Intent = new Intent(getApplicationContext(), GalleryUtil.class);
                        startActivityForResult(gallery_Intent, requestCodeForSdCard);
                    }
                }
                else {
                    Intent gallery_Intent = new Intent(getApplicationContext(), GalleryUtil.class);
                    startActivityForResult(gallery_Intent, requestCodeForSdCard);
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mBottomSheetDialog.cancel();
            }
        });

        mBottomSheetDialog.show();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == requestCodeForSdCard && resultCode == RESULT_OK && data != null) {


            String picturePath = data.getStringExtra("picturePath");
            //perform Crop on the Image Selected from Gallery
            doCrop1(picturePath);


        } else if (requestCode == requestCodeForCamera && resultCode == RESULT_OK) {

            LoggerGeneral.info("comek here 1");
            /*File f = new File(Common.getChacheDir(context), "abc.jpg");
            imageUri = Uri.fromFile(f);
            Bitmap newBMP = null;
            newBMP = Common.decodeFile(f);
            Common.saveBitmapToFile(newBMP, f);*/
            doCrop(imageUri);


        } else if (requestCode == requestCodeForCorp && resultCode == RESULT_OK) {

            LoggerGeneral.info("requestcode for corp ");
            try {
                if (data != null) {
                    LoggerGeneral.info("data != null");
                    Bitmap newBMP = null;
                    Bitmap rotBMP = null;

                    Uri imageUri = data.getData();


                    if(imageUri!=null) {
                        newBMP = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);

                    }
                    else {
                        newBMP = data.getExtras().getParcelable("data");
                    }
                    //newBMP = data.getExtras().getParcelable("data");

                    // File uri=new File(imageUri.getPath());
                    //imgPreview.setImageBitmap(newBMP);


                    File f = Common.createNewFileOrOverwrite(Common.getChacheDir(context), "abc.jpg");
                    Common.saveBitmapToFile(newBMP, f);
                    startUploadActivity(newBMP);


                } else {
                    LoggerGeneral.info("data == null");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


        } else {
            LoggerGeneral.info("failed");
        }

    }

    public void startUploadActivity(Bitmap newBMP) {
        Common.showToast(context, "Image selected");

        eventImage=getStringImage(newBMP);



        Bitmap resizedBMP = getResizedBitmap(newBMP, 500,500);


        ByteArrayOutputStream bs = new ByteArrayOutputStream();

		/*FileOutputStream bs;
		try {
			bs =  new FileOutputStream(f);
			resizedBMP.compress(Bitmap.CompressFormat.JPEG,100, bs);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}*/

       /* resizedBMP.compress(Bitmap.CompressFormat.JPEG, 100, bs);
        mBitmap = BitmapFactory.decodeByteArray(
                bs.toByteArray(), 0,
                bs.toByteArray().length);*/
        // FileOutputStream out = new FileOutputStream(new File();)

        /*if(Common.isOnline(context)) {
            new GetImageUpload().execute();
        }
        else {
            Common.showToast(context,"No internet connection");
        }*/
        //   editimage.setImageBitmap(mBitmap);

        //rphoto.setImageBitmap(newBMP);
    }
    public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height,
                matrix, false);

        return resizedBitmap;
    }

    private void doCrop1(final String mImageCaptureUri) {
        try {
            //Start Crop Activity

            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            // indicate image type and Uri
            LoggerGeneral.info("Hiiiiiiii" + mImageCaptureUri);
            File f = new File(mImageCaptureUri);
            Uri contentUri = Uri.fromFile(f);

            cropIntent.setDataAndType(contentUri, "image/*");


            // set crop properties
            cropIntent.putExtra("crop", "true");
            // indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 9);
            cropIntent.putExtra("aspectY", 5);
            // indicate output X and Y
            cropIntent.putExtra("outputX", 400);
            cropIntent.putExtra("outputY", 200);

            // retrieve data on return


            cropIntent.putExtra("return-data", true);
            // start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, requestCodeForCorp);
        }
        // respond to users whose devices do not support the crop action
        catch (ActivityNotFoundException anfe) {
            // display an error message
            String errorMessage = "your device doesn't support the crop action!";
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private void doCrop(final Uri mImageCaptureUri) {
        final ArrayList<CropOption> cropOptions = new ArrayList<CropOption>();

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setType("image/*");

        List<ResolveInfo> list = getPackageManager().queryIntentActivities(intent, 0);

        int size = list.size();

        if (size == 0) {
            Toast.makeText(this, "Can not find image crop app", Toast.LENGTH_SHORT).show();

            return;
        } else {


            intent.putExtra("outputX", 400);
            intent.putExtra("outputY", 200);
            intent.putExtra("aspectX", 9);
            intent.putExtra("aspectY", 5);
            intent.putExtra("scale", false);
            //intent.putExtra("extra_size_limit", 512);
            intent.putExtra("return-data", true);
            //intent.putExtra("crop", true);
            intent.setData(mImageCaptureUri);

            if (size == 1) {
                Intent i = new Intent(intent);
                ResolveInfo res = list.get(0);

                i.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));

                startActivityForResult(i, requestCodeForCorp);
            } else {
                for (ResolveInfo res : list) {
                    final CropOption co = new CropOption();

                    co.title = getPackageManager().getApplicationLabel(res.activityInfo.applicationInfo);
                    co.icon = getPackageManager().getApplicationIcon(res.activityInfo.applicationInfo);
                    co.appIntent = new Intent(intent);

                    LoggerGeneral.info(res.activityInfo.packageName + " " + res.activityInfo.name);
                    co.appIntent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));

                    cropOptions.add(co);
                }

                CropOptionAdapter adapter = new CropOptionAdapter(getApplicationContext(), cropOptions);

                AlertDialog.Builder builder = new AlertDialog.Builder(this,AlertDialog.THEME_DEVICE_DEFAULT_DARK);
                builder.setTitle("Choose Crop App");
                builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        startActivityForResult(cropOptions.get(item).appIntent, requestCodeForCorp);
                    }
                });
                builder.setOnKeyListener(new DialogInterface.OnKeyListener() {
                    @Override
                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                        return false;
                    }
                });
                builder.setOnCancelListener(new DialogInterface.OnCancelListener() {

                    public void onCancel(DialogInterface dialog) {
                        if (mImageCaptureUri != null) {

                            getContentResolver().delete(mImageCaptureUri, null, null);

                        }
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
            }
        }
    }
    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        // String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        Log.e("string", "in Byte" + imageBytes);
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);

       /* final int lnth=bmp.getByteCount();
        ByteBuffer dst= ByteBuffer.allocate(lnth);
        bmp.copyPixelsToBuffer(dst);
        byte[] barray=dst.array();
        String encodedImage = Base64.encodeToString(barray, Base64.DEFAULT);*/
        return encodedImage;
    }

    private boolean checkPermission3(){
        int result = ContextCompat.checkSelfPermission(context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED){

            return true;

        } else {

            return false;

        }
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.state:
                //  onClickList();

                statesarray = new ArrayList<StateModel>();

                if (state_id != null) {
                    state_id = null;
                }
                if (Common.isOnline(context)) {
                    new getState().execute();

                } else {
                    Common.showToast(context, "No internet connection !");
                }

                break;
            case R.id.city:
                ///  onClickCityList();
                cityarray = new ArrayList<CityModel>();
                if(state_id==null){
                    state_id=savestateid;
                }
                if (Common.isOnline(context)) {
                    new getCity().execute();
                } else {
                    Common.showToast(context, "No internet connection !");
                }

                break;
        }

    }

    class getState extends AsyncTask<String, String, JSONObject> {
        String type;
        ProgressDialog mDialog;

        @Override
        protected JSONObject doInBackground(String... params) {
            // TODO Auto-generated method stub

            String url = Constants.Base + Constants.Statelist;

            JSONObject object = new JSONObject();
            try {

                object.put("country_id", "1");
                object.put("skip", "0");
                object.put("take", "1000");
                LoggerGeneral.info("JsonObjectPrint" + object.toString());

            } catch (Exception ex) {

            }

            //String str = '"' + appPreferences.getString("jwt", "") + '"';
            JSONObject jsonObject = ServiceFacade.getResponsJsonParams(url, object);

            Log.d("hi", "getresponse" + jsonObject);

            Log.d("hi", "getresponse" + jsonObject);

            if (jsonObject != null) {
                if (jsonObject.has("Data")) {
                    try {
                        Log.d("hi", "getresponse11" + jsonObject);

                        String response = jsonObject.toString();
                        Log.d("hi", "getresponse22" + response);

                    } catch (Exception e) {
                    }
                }
            }
            return jsonObject;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

            mDialog = new ProgressDialog(CreateEvent.this, ProgressDialog.THEME_HOLO_DARK);
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
            //mDialog.dismiss();

            if (results != null) {

                try {
                    JSONArray jsonArray = results.getJSONArray("data");


                    LoggerGeneral.info("arraystate-----" + jsonArray);


                    JSONObject jsonObject = null;

                    for (int i = 0; i <= jsonArray.length() - 1; i++) {

                        jsonObject = jsonArray.getJSONObject(i);

                        String stateid = jsonObject.getString("state_id");

                        String CountryId = jsonObject.getString("country_id");

                        String statename = jsonObject.getString("state_name");


                        StateModel stateModel = new StateModel();

                        stateModel.setStateid(stateid);
                        stateModel.setCountryid(CountryId);
                        stateModel.setStatename(statename);


                        statesarray.add(stateModel);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mDialog.dismiss();
                onStateClick();
            }
        }

    }

    public void onStateClick() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        ArrayList<String> arr = new ArrayList<String>();

        ArrayList<String> arrst = new ArrayList<String>();
        LoggerGeneral.info("showstarr" + statesarray.size() + "---" + statesarray.toString());
        for (int i = 0; i <= statesarray.size() - 1; i++) {


            String array = statesarray.get(i).getStatename();

            String arraystid = statesarray.get(i).getStateid();

            arr.add(array);

            arrst.add(arraystid);
        }

        mStringArray = new String[arr.size()];
        mStringArray = arr.toArray(mStringArray);

        mstrinArraystid = new String[arrst.size()];
        mstrinArraystid = arrst.toArray(mstrinArraystid);

        if(savestateid!=null){
            state_id=savestateid;
        }

        LoggerGeneral.info("arrayyy---" + mStringArray + "---" + arr.size());
        builder.setItems(mStringArray, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {


                state.setText(mStringArray[which]);
                //  cityArray = hashMap.get(state.getText().toString());
                city.setText("");
                city.setHint("Select City");

                if (state_id != null) {
                    state_id = null;
                }
                state_id = mstrinArraystid[which];

                savestateid = state_id;

                LoggerGeneral.info("showstateid" + state_id);
                //  city.setText(cityArray[0]);
                // The 'which' argument contains the index position
                // of the selected item
            }
        });

        AlertDialog alert = builder.create();
        alert.show();

    }

    class getCity extends AsyncTask<String, String, JSONObject> {
        String type;
        ProgressDialog mDialog;

        @Override
        protected JSONObject doInBackground(String... params) {
            // TODO Auto-generated method stub

            String url = Constants.Base + Constants.CityList;

            JSONObject object = new JSONObject();
            try {

                object.put("country_id", "1");
                object.put("state_id", state_id);
                object.put("skip", "0");
                object.put("take", "1000");
                LoggerGeneral.info("JsonObjectPrint" + object.toString());

            } catch (Exception ex) {

            }

            //String str = '"' + appPreferences.getString("jwt", "") + '"';
            JSONObject jsonObject = ServiceFacade.getResponsJsonParams(url, object);

            Log.d("hi", "getresponse" + jsonObject);

            Log.d("hi", "getresponse" + jsonObject);

            if (jsonObject != null) {
                if (jsonObject.has("Data")) {
                    try {
                        Log.d("hi", "getresponse11" + jsonObject);

                        String response = jsonObject.toString();
                        Log.d("hi", "getresponse22" + response);

                    } catch (Exception e) {
                    }
                }
            }
            return jsonObject;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

            mDialog = new ProgressDialog(CreateEvent.this, ProgressDialog.THEME_HOLO_DARK);
            mDialog.setMessage("Please wait...");
            mDialog.show();
            mDialog.setCancelable(false);
            mDialog.setCanceledOnTouchOutside(false);
        }

        @Override
        protected void onPostExecute(JSONObject results) {
            // TODO Auto-generated method stub
            super.onPostExecute(results);
            Log.d("hi", "getresultscity" + results);


            if (results != null) {

                try {
                    JSONArray jsonArray = results.getJSONArray("data");


                    LoggerGeneral.info("arraycity-----" + jsonArray);


                    JSONObject jsonObject = null;

                    for (int i = 0; i <= jsonArray.length() - 1; i++) {

                        jsonObject = jsonArray.getJSONObject(i);

                        String CityId = jsonObject.getString("city_id");

                        String CStateId = jsonObject.getString("state_id");

                        String CountryId = jsonObject.getString("country_id");

                        String CityName = jsonObject.getString("city_name");


                        CityModel cityModel = new CityModel();

                        cityModel.setCityId(CityId);
                        cityModel.setCityName(CityName);
                        cityModel.setCountryId(CountryId);
                        cityModel.setStateId(CStateId);

                        cityarray.add(cityModel);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mDialog.dismiss();
                onCityClick();
            }
        }

    }

    public void onCityClick() {

        if (!state.getText().toString().equalsIgnoreCase("") && !state.getText().toString().equalsIgnoreCase("State")) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            ArrayList<String> arrc = new ArrayList<String>();

            ArrayList<String> arrstc = new ArrayList<String>();
            LoggerGeneral.info("showstarr" + cityarray.size() + "---" + cityarray.toString());
            for (int i = 0; i <= cityarray.size() - 1; i++) {


                String array = cityarray.get(i).getCityName();

                String arraystid = cityarray.get(i).getCityId();

                arrc.add(array);

                arrstc.add(arraystid);
            }

            if (mcityStringArray != null) {
                mcityStringArray = null;
            }
            if (mcitystrinArraystid != null) {
                mcitystrinArraystid = null;
            }


            mcityStringArray = new String[arrc.size()];
            mcityStringArray = arrc.toArray(mcityStringArray);

            mcitystrinArraystid = new String[arrstc.size()];
            mcitystrinArraystid = arrstc.toArray(mcitystrinArraystid);


            LoggerGeneral.info("arrayyyc---" + mcityStringArray + "---" + arrc.size());
            builder.setItems(mcityStringArray, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {


                    city.setText(mcityStringArray[which]);


                }
            });

            AlertDialog alert = builder.create();
            alert.show();

        } else {
            Common.showToast(context, "Select State first");
        }

    }
}
