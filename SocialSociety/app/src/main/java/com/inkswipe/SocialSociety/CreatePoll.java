package com.inkswipe.SocialSociety;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
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
import android.widget.Toast;

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
import model.CropOption;
import model.DrawerList;
import util.AppPreferences;
import util.Common;
import util.Constants;
import util.LoggerGeneral;
import util.ServiceFacade;

public class CreatePoll extends AppCompatActivity {

    Context context;
    TextView uploadPhoto;
    LinearLayout nextb,cancelb,add;
    String SocietyId;
    EditText pollTitle,pollQuestion,option1,option2,option3,option4,option5,option6;
    String textPollTitle,textPollQuestion,textOption1,textOption2,textOption3,textOption4,textOption5,textOption6,availEndDate,availEndDate1,availEndDate12;
    TextView endDate;
    Calendar myCalendar;
    String pollImage="";

    ArrayList<String> optionCheck;

    LinearLayout borderpolldetail;

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;

    RelativeLayout notification;
    LinearLayout home;
    public static int permissionChecker=0;
    private static final int PERMISSION_REQUEST_CODE = 1;
    Uri imageUri;
    static final int requestCodeForSdCard = 1, requestCodeForCamera = 2, requestCodeForCorp = 3;
    AppPreferences appPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_poll);
        context  = CreatePoll.this;
        appPreferences=AppPreferences.getAppPreferences(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        SocietyId = getIntent().getStringExtra("SocietyId");
        myCalendar = Calendar.getInstance();
        LoggerGeneral.info("Society post idCreatepost22---" + SocietyId);
        TextView title = ((TextView) toolbar.findViewById(R.id.toolbar_title));
        title.setText("Create Poll");
        title.setTypeface(Common.font(context, "arial.ttf"), 1);
        toolbar.setTitleTextColor(android.graphics.Color.WHITE);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.my_statusbar_color));
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //   toolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.backarrow));
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.getNavigationIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //What to do on back clicked
                if (mDrawerLayout.isDrawerOpen(GravityCompat.END)) {
                    mDrawerLayout.closeDrawers();
                    return;
                } else {
                    Intent back = new Intent(CreatePoll.this, DashBoard.class);
                    back.putExtra("polls", 2);
                    back.putExtra("SocietyId", SocietyId);
                    startActivity(back);
                    finish();
                }
            }
        });


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

        availEndDate12=getIntent().getStringExtra("toShow");

        pollImage=getIntent().getStringExtra("pollImage");

        LoggerGeneral.info("HIiiiiii create Poll"+availEndDate+"==="+availEndDate1);

        LoggerGeneral.info("HiiiiTEsacustomt"  + SocietyId + "====" + availEndDate + "===" + availEndDate1 + "--" + SocietyId +"--" + textOption4+"--" + textOption5+"--" + textOption6+"--" + textOption3);

        uploadPhoto = (TextView) findViewById(R.id.uploadPhoto);
        String udata1 = "Upload Photo";
        SpannableString content1 = new SpannableString(udata1);
        content1.setSpan(new UnderlineSpan(), 0, udata1.length(), 0);
        uploadPhoto.setText(content1);
        uploadPhoto.setTypeface(Common.font(context, "arial.ttf"));
        uploadPhoto.setTextColor(getResources().getColor(R.color.color_primary));

        nextb= (LinearLayout) findViewById(R.id.nextb);
        cancelb= (LinearLayout) findViewById(R.id.cancelb);
        add= (LinearLayout) findViewById(R.id.addpoll);

        borderpolldetail= (LinearLayout) findViewById(R.id.borderpolldetails);

        borderpolldetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pollQuestion.requestFocus();

                final InputMethodManager inputMethodManager =
                        (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);

                inputMethodManager.showSoftInput(pollQuestion, InputMethodManager.SHOW_FORCED);
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent polls = new Intent(CreatePoll.this, DashBoard.class);
                polls.putExtra("polls", 2);
                startActivity(polls);
                finish();
            }
        });


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

        cancelb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent polls = new Intent(CreatePoll.this, DashBoard.class);
                polls.putExtra("polls", 2);
                polls.putExtra("SocietyId", SocietyId);
                startActivity(polls);
                finish();

            }
        });

        pollTitle= (EditText) findViewById(R.id.polltitle);
        pollQuestion= (EditText) findViewById(R.id.question);
        option1= (EditText) findViewById(R.id.option1);
        option2= (EditText) findViewById(R.id.option2);
        option3= (EditText) findViewById(R.id.option3);
        option4= (EditText) findViewById(R.id.option4);
        option5= (EditText) findViewById(R.id.option5);
        option6= (EditText) findViewById(R.id.option6);
        endDate= (TextView) findViewById(R.id.enddate);

        if(textPollTitle!=null)
        {
            pollTitle.setText(textPollTitle);
        }

        if(textPollQuestion!=null)
        {
            pollQuestion.setText(textPollQuestion);
        }

        if(availEndDate!=null)
        {
            endDate.setText(availEndDate);
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

        if(textOption4!=null && textOption4.length()>0)
        {
            option4.setText(textOption4);
        }

        if(textOption5!=null && textOption5.length()>0)
        {
            option5.setText(textOption5);
        }
        if(textOption6!=null && textOption6.length()>0)
        {
            option6.setText(textOption6);
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
                Intent notification=new Intent(CreatePoll.this,NotificationApp.class);
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
                Intent myProperty=new Intent(CreatePoll.this,MyProperty.class);
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
                        Intent profile=new Intent(CreatePoll.this,Profile.class);
                        profile.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(profile);
                        finish();
                        break;
                    case 2:
                        LoggerGeneral.info("2");
                        Intent addSociety=new Intent(CreatePoll.this,AddSociety.class);
                        startActivity(addSociety);
                        finish();
                        break;
                    case 3:
                        LoggerGeneral.info("3");
                        Intent myProperty=new Intent(CreatePoll.this,MyProperty.class);
                        PropertyAdapter.intentCheck=0;
                        startActivity(myProperty);
                        finish();
                        break;
                    case 4:
                        LoggerGeneral.info("4");
                        Intent events=new Intent(CreatePoll.this,DashBoard.class);
                        events.putExtra("SocietyId", SocietyId);
                        events.putExtra("event",3);
                        startActivity(events);
                        finish();
                        break;
                    case 5:
                        LoggerGeneral.info("5");
                        Intent createEvents=new Intent(CreatePoll.this,CreateEvent.class);
                        createEvents.putExtra("SocietyId",SocietyId);
                        startActivity(createEvents);
                        finish();
                        mDrawerLayout.closeDrawers();
                        break;
                    case 6:
                        LoggerGeneral.info("6");
                        Intent archivedEvents=new Intent(CreatePoll.this,Archivedevent.class);
                        archivedEvents.putExtra("SocietyId", SocietyId);
                        startActivity(archivedEvents);
                        finish();
                        break;
                    case 7:
                        LoggerGeneral.info("7");
                        Intent announcement=new Intent(CreatePoll.this,DashBoard.class);
                        announcement.putExtra("SocietyId", SocietyId);
                        announcement.putExtra("announcement",4);
                        startActivity(announcement);
                        finish();
                        break;
                    case 8:
                        LoggerGeneral.info("8");
                        Intent createAnnouncement=new Intent(CreatePoll.this,CreateAnnouncement.class);
                        createAnnouncement.putExtra("SocietyId", SocietyId);
                        startActivity(createAnnouncement);
                        finish();
                        break;
                    case 9:
                        LoggerGeneral.info("9");
                        Intent polls=new Intent(CreatePoll.this,DashBoard.class);
                        polls.putExtra("SocietyId", SocietyId);
                        polls.putExtra("polls",2);
                        startActivity(polls);
                        finish();
                        break;
                    case 10:
                        LoggerGeneral.info("10");
                        mDrawerLayout.closeDrawers();
                        break;
                    case 11:
                        LoggerGeneral.info("11");
                        Intent archivedEventPoll=new Intent(CreatePoll.this,ArchivedPolls.class);
                        archivedEventPoll.putExtra("SocietyId", SocietyId);
                        startActivity(archivedEventPoll);
                        finish();
                        break;
                    case 12:
                        LoggerGeneral.info("12");
                        Intent members=new Intent(CreatePoll.this,Members.class);
                        members.putExtra("SocietyId", SocietyId);
                        startActivity(members);
                        finish();
                        break;
                    case 13:
                        LoggerGeneral.info("13");
                        Intent offers=new Intent(CreatePoll.this,Offer.class);
                        offers.putExtra("SocietyId", SocietyId);
                        startActivity(offers);
                        finish();
                        break;
                    case 14:
                        LoggerGeneral.info("14");
                        Intent complaint=new Intent(CreatePoll.this,Complaint.class);
                        complaint.putExtra("SocietyId", SocietyId);
                        startActivity(complaint);
                        finish();
                        break;
                    case 15:
                        LoggerGeneral.info("15");
                        Intent notification=new Intent(CreatePoll.this,NotificationApp.class);
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
                      /*  LoggerGeneral.info("16");
                        Intent intent = new Intent(CreatePoll.this, Login.class);
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

        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(CreatePoll.this,R.style.DialogTheme, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });




        nextb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                optionCheck=new ArrayList<String>();

                int check=0;

                textPollTitle=pollTitle.getText().toString();
                textPollQuestion=pollQuestion.getText().toString();
                textOption1=option1.getText().toString();
                textOption2=option2.getText().toString();
                textOption3=option3.getText().toString();
                textOption4=option4.getText().toString();
                textOption5=option5.getText().toString();
                textOption6=option6.getText().toString();

                optionCheck.add(textOption1);
                optionCheck.add(textOption2);
                optionCheck.add(textOption3);
                optionCheck.add(textOption4);
                optionCheck.add(textOption5);
                optionCheck.add(textOption6);

                availEndDate = endDate.getText().toString().trim();


                if(textPollTitle!=null && textPollTitle.length()>0)
                {
                    if (availEndDate != null && availEndDate.length() > 0) {
                        if (textPollQuestion != null && textPollQuestion.length() > 0) {

                            if(option1!=null && option1.length()>0)
                            {
                                if(option2!=null && option2.length()>0) {
                                    /*if(option3!=null && option3.length()>0)
                                    {
                                        if(option4!=null && option4.length()>0)
                                        {
                                            if(option5!=null && option5.length()>0)
                                            {
                                                if(option6!=null && option6.length()>0)
                                                {*/

                                    for (int i = 0; i < 6; i++) {
                                        for (int j = 0; j < 6; j++) {
                                            if(optionCheck.get(i).length()>0 && optionCheck.get(j).length()>0) {
                                                if(i!=j) {
                                                    if (optionCheck.get(i).equals(optionCheck.get(j))) {
                                                        check = 1;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    if (check == 0) {
                                        Intent intent = new Intent(CreatePoll.this, PollDetails.class);
                                        intent.putExtra("createPoll", "createPoll");
                                        intent.putExtra("poll_title", textPollTitle);
                                        intent.putExtra("poll_question", textPollQuestion);
                                        intent.putExtra("endDate", availEndDate);
                                        intent.putExtra("endDate1", availEndDate1);
                                        intent.putExtra("toShow", availEndDate12);
                                        intent.putExtra("option1", textOption1);
                                        intent.putExtra("option2", textOption2);
                                        intent.putExtra("option3", textOption3);
                                        intent.putExtra("option4", textOption4);
                                        intent.putExtra("option5", textOption5);
                                        intent.putExtra("option6", textOption6);
                                        intent.putExtra("pollImage", pollImage);
                                        intent.putExtra("SocietyId", SocietyId);
                                        startActivity(intent);
                                        finish();
                                               /* }
                                                else {
                                                    Intent intent=new Intent(CreatePoll.this,PollDetails.class);
                                                    intent.putExtra("createPoll","createPoll");
                                                    intent.putExtra("poll_title",textPollTitle);
                                                    intent.putExtra("poll_question",textPollQuestion);
                                                    intent.putExtra("endDate",availEndDate);
                                                    intent.putExtra("endDate1",availEndDate1);
                                                    intent.putExtra("option1",textOption1);
                                                    intent.putExtra("option2",textOption2);
                                                    intent.putExtra("option3",textOption3);
                                                    intent.putExtra("option4",textOption4);
                                                    intent.putExtra("option5",textOption5);
                                                    intent.putExtra("pollImage",pollImage);
                                                    intent.putExtra("SocietyId",SocietyId);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            }
                                            else {
                                                Intent intent=new Intent(CreatePoll.this,PollDetails.class);
                                                intent.putExtra("createPoll","createPoll");
                                                intent.putExtra("poll_title",textPollTitle);
                                                intent.putExtra("poll_question",textPollQuestion);
                                                intent.putExtra("endDate",availEndDate);
                                                intent.putExtra("endDate1",availEndDate1);
                                                intent.putExtra("option1",textOption1);
                                                intent.putExtra("option2",textOption2);
                                                intent.putExtra("option3",textOption3);
                                                intent.putExtra("option4",textOption4);
                                                intent.putExtra("pollImage",pollImage);
                                                intent.putExtra("SocietyId",SocietyId);
                                                startActivity(intent);
                                                finish();
                                            }
                                        }
                                        else {
                                            Intent intent=new Intent(CreatePoll.this,PollDetails.class);
                                            intent.putExtra("createPoll","createPoll");
                                            intent.putExtra("poll_title",textPollTitle);
                                            intent.putExtra("poll_question",textPollQuestion);
                                            intent.putExtra("endDate",availEndDate);
                                            intent.putExtra("endDate1",availEndDate1);
                                            intent.putExtra("option1",textOption1);
                                            intent.putExtra("option2",textOption2);
                                            intent.putExtra("option3",textOption3);
                                            intent.putExtra("pollImage",pollImage);
                                            intent.putExtra("SocietyId",SocietyId);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }
                                    else {
                                        Intent intent=new Intent(CreatePoll.this,PollDetails.class);
                                        intent.putExtra("createPoll","createPoll");
                                        intent.putExtra("poll_title",textPollTitle);
                                        intent.putExtra("poll_question",textPollQuestion);
                                        intent.putExtra("endDate",availEndDate);
                                        intent.putExtra("endDate1",availEndDate1);
                                        intent.putExtra("option1",textOption1);
                                        intent.putExtra("option2",textOption2);
                                        intent.putExtra("pollImage",pollImage);
                                        intent.putExtra("SocietyId",SocietyId);
                                        startActivity(intent);
                                        finish();
                                    }
*/
                                    }
                                    else {
                                        Common.showToast(context, "Enter different options");
                                    }

                                }
                                else {
                                    Common.showToast(context, "Enter minimum 2 options");
                                }
                            }
                            else {
                                Common.showToast(context, "Enter minimum 2 options");
                            }

                        } else {
                            Common.showToast(context, "Enter poll question");
                        }
                    }
                    else {
                        Common.showToast(context, "Select end date");
                    }
                }
                else {
                    Common.showToast(context,"Enter poll title");
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

            mDialog = new ProgressDialog(CreatePoll.this,ProgressDialog.THEME_HOLO_DARK);
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
                        Intent register   = new Intent(CreatePoll.this,Login.class);
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

    private void updateLabel() throws ParseException {

        String myFormat = "dd-MM-yyyy"; //In which you need put here
        String myFormat12 = "dd MMM, yyyy";
        String myFormat1 = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        SimpleDateFormat sdf1 = new SimpleDateFormat(myFormat1, Locale.US);
        SimpleDateFormat sdf12 = new SimpleDateFormat(myFormat12, Locale.US);
        Date now = new Date();
        if(myCalendar.getTime().after(now)) {
            endDate.setText(sdf.format(myCalendar.getTime()));
            endDate.setTextColor(Color.parseColor("#000000"));
            availEndDate1 = sdf1.format(myCalendar.getTime());
            availEndDate = endDate.getText().toString().trim();
            availEndDate12=sdf12.format(myCalendar.getTime());

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

        final Dialog mBottomSheetDialog = new Dialog (CreatePoll.this,
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

        pollImage=getStringImage(newBMP);

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
    public void onBackPressed() {

        if(mDrawerLayout.isDrawerOpen(GravityCompat.END))
        {
            mDrawerLayout.closeDrawers();
            return;
        }
        else {
            super.onBackPressed();
            Intent back = new Intent(CreatePoll.this, DashBoard.class);
            back.putExtra("polls", 2);
            back.putExtra("SocietyId", SocietyId);
            startActivity(back);
            finish();
        }

    }

}
