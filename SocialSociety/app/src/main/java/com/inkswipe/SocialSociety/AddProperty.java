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
import android.graphics.drawable.ColorDrawable;
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
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
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
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.flurry.android.FlurryAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import adapter.CropOptionAdapter;
import adapter.CustomAdapter;
import adapter.PropertyAdapter;
import model.CropOption;
import model.DrawerList;
import model.PropertyType;
import model.SocietyModel;
import util.AppPreferences;
import util.Common;
import util.Constants;
import util.ExceptionHandler;
import util.LoggerGeneral;
import util.ServiceFacade;

public class AddProperty extends AppCompatActivity  implements View.OnClickListener{

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;

    LinearLayout home;
    Fragment fragment = null;
    TextView appname;
    LinearLayout addprop;
    TextView state,proptype;
    String[] propArray = new String[]{"Flat","Bunglow","Row House","Not in The List"};
    String[] defineYourself = new String[]{"Owner","Sub Owner","Tenant","Sub Tenant"};
    String[] mStringArray,mstrinArraystid;
    EditText name,flatNo;
    TextView dateOfRent,self;

    LinearLayout hideVisible;

    RadioButton yes,no;

    LinearLayout uploadPhoto;

    RelativeLayout notification;
    Context context;

    public static int permissionChecker=0;
    private static final int PERMISSION_REQUEST_CODE = 1;
    Uri imageUri;
    static final int requestCodeForSdCard = 1, requestCodeForCamera = 2, requestCodeForCorp = 3;

    public  ArrayList<SocietyModel> societies;
    DataWrapper dw;

    String societyId;

    AppPreferences appPreferences;


    String propertyImage="",tpropertyname,tpropertytype,tdefine,thouseno,availforrent,availableRentRadio;

    Calendar myCalendar;
    PropertyType propertyType ;
    List<PropertyType>propertyTypes;
    String sdialogtxt;
    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            String year1 = String.valueOf(selectedYear);
            String month1 = String.valueOf(selectedMonth + 1);
            String day1 = String.valueOf(selectedDay);

        }
    };


    View dateView;

    TextView availtxt;
    LinearLayout availradio;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_property);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        myCalendar = Calendar.getInstance();
        context=AddProperty.this;
        appPreferences=AppPreferences.getAppPreferences(context);
        propertyTypes = new ArrayList<PropertyType>();


        societyId = getIntent().getStringExtra("societyId");


        LoggerGeneral.info("socId----"+societyId);

        final Calendar cal = Calendar.getInstance(TimeZone.getDefault());
        dw = (DataWrapper)getIntent().getSerializableExtra("societylist");
        if(societies!=null){
            societies.clear();
        }

        societies =dw.getSocieties();

        TextView title=((TextView) toolbar.findViewById(R.id.toolbar_title));
        title.setText("Add Property");
        toolbar.setTitleTextColor(android.graphics.Color.WHITE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.my_statusbar_color));
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //  toolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.backarrow));
        toolbar.getNavigationIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //What to do on back clicked
                Intent intent = new Intent(AddProperty.this, MySociety.class);
                intent.putExtra("societylist", (new DataWrapper((ArrayList<SocietyModel>) societies)));
                startActivity(intent);
                finish();
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
                Intent notification=new Intent(AddProperty.this,NotificationApp.class);
                //    notification.putExtra("SocietyId",Soci)
                int test1=appPreferences.getInt("NotificationOld",0);
                test1=test1+Constants.notififcationcount;
                appPreferences.putInt("NotificationOld",test1);
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
                if(appPreferences.getInt("property_count",0)>0) {
                    Intent myProperty=new Intent(AddProperty.this,MyProperty.class);
                    PropertyAdapter.intentCheck=0;
                    startActivity(myProperty);
                    finish();
                }
                else
                {
                    Common.showToast(context, "Please add minimum 1 property");
                    mDrawerLayout.closeDrawers();
                }


            }
        });

        home = (LinearLayout)findViewById(R.id.home);
        home.setOnClickListener(homeOnclickListener);

        mDrawerList.addHeaderView(listHeaderView);

        DrawerList list=new DrawerList();

        List<ItemObject> listViewItems = list.drawer();


        mDrawerList.setAdapter(new CustomAdapter(this, listViewItems));

        uploadPhoto = (LinearLayout) findViewById(R.id.uploadphoto);

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



        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // make ToastCustom when click
                /*ToastCustom.makeText(getApplicationContext(), "Position " + position, ToastCustom.LENGTH_LONG).show();
                */

                switch (position) {

                    case 1:
                        LoggerGeneral.info("1");
                        Intent profile = new Intent(AddProperty.this, Profile.class);
                        profile.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(profile);
                        finish();
                        mDrawerLayout.closeDrawers();
                        break;
                    case 2:
                        LoggerGeneral.info("2");
                        Intent addSociety = new Intent(AddProperty.this, AddSociety.class);
                        startActivity(addSociety);
                        finish();
                        break;
                    case 3:
                        LoggerGeneral.info("3");
                        if (appPreferences.getInt("property_count", 0) > 0) {
                            Intent myProperty = new Intent(AddProperty.this, MyProperty.class);
                            PropertyAdapter.intentCheck = 0;
                            startActivity(myProperty);
                            finish();
                        } else {
                            Common.showToast(context, "Please add minimum 1 property");
                            mDrawerLayout.closeDrawers();
                        }
                        break;
                    case 4:
                        LoggerGeneral.info("4");
                        if (appPreferences.getInt("property_count", 0) > 0) {
                            Intent events = new Intent(AddProperty.this, MyProperty.class);
                            Common.showToast(context, "Please select property first");
                            PropertyAdapter.intentCheck = 4;
                            events.putExtra("event", 3);
                            startActivity(events);
                            finish();
                        } else {
                            Common.showToast(context, "Please add minimum 1 property");
                            mDrawerLayout.closeDrawers();
                        }
                        break;
                    case 5:
                        LoggerGeneral.info("5");
                        if (appPreferences.getInt("property_count", 0) > 0) {
                            Intent createEvents = new Intent(AddProperty.this, MyProperty.class);
                            Common.showToast(context, "Please select property first");
                            PropertyAdapter.intentCheck = 5;
                            startActivity(createEvents);
                            finish();
                        } else {
                            Common.showToast(context, "Please add minimum 1 property");
                            mDrawerLayout.closeDrawers();
                        }
                        break;
                    case 6:
                        LoggerGeneral.info("6");
                        if (appPreferences.getInt("property_count", 0) > 0) {
                            Intent archivedEvents = new Intent(AddProperty.this, MyProperty.class);
                            Common.showToast(context, "Please select property first");
                            PropertyAdapter.intentCheck = 6;
                            startActivity(archivedEvents);
                            finish();
                        } else {
                            Common.showToast(context, "Please add minimum 1 property");
                            mDrawerLayout.closeDrawers();
                        }
                        break;
                    case 7:
                        LoggerGeneral.info("7");
                        if (appPreferences.getInt("property_count", 0) > 0) {
                            Intent announcement = new Intent(AddProperty.this, MyProperty.class);
                            Common.showToast(context, "Please select property first");
                            PropertyAdapter.intentCheck = 7;
                            announcement.putExtra("announcement", 4);
                            startActivity(announcement);
                            finish();
                        } else {
                            Common.showToast(context, "Please add minimum 1 property");
                            mDrawerLayout.closeDrawers();
                        }
                        break;
                    case 8:
                        LoggerGeneral.info("8");
                        if (appPreferences.getInt("property_count", 0) > 0) {
                            Intent createAnnouncement = new Intent(AddProperty.this, MyProperty.class);
                            Common.showToast(context, "Please select property first");
                            PropertyAdapter.intentCheck = 8;
                            startActivity(createAnnouncement);
                            finish();
                        } else {
                            Common.showToast(context, "Please add minimum 1 property");
                            mDrawerLayout.closeDrawers();
                        }
                        break;
                    case 9:
                        LoggerGeneral.info("9");
                        if (appPreferences.getInt("property_count", 0) > 0) {
                            Intent polls = new Intent(AddProperty.this, MyProperty.class);
                            Common.showToast(context, "Please select property first");
                            PropertyAdapter.intentCheck = 9;
                            polls.putExtra("polls", 2);
                            startActivity(polls);
                            finish();
                        } else {
                            Common.showToast(context, "Please add minimum 1 property");
                            mDrawerLayout.closeDrawers();
                        }
                        break;
                    case 10:
                        LoggerGeneral.info("10");
                        if (appPreferences.getInt("property_count", 0) > 0) {
                            Intent createEventPoll = new Intent(AddProperty.this, MyProperty.class);
                            Common.showToast(context, "Please select property first");
                            PropertyAdapter.intentCheck = 10;
                            startActivity(createEventPoll);
                            finish();
                        } else {
                            Common.showToast(context, "Please add minimum 1 property");
                            mDrawerLayout.closeDrawers();
                        }
                        break;
                    case 11:
                        LoggerGeneral.info("11");
                        if (appPreferences.getInt("property_count", 0) > 0) {
                            Intent archivedEventPoll = new Intent(AddProperty.this, MyProperty.class);
                            Common.showToast(context, "Please select property first");
                            PropertyAdapter.intentCheck = 11;
                            startActivity(archivedEventPoll);
                            finish();
                        } else {
                            Common.showToast(context, "Please add minimum 1 property");
                            mDrawerLayout.closeDrawers();
                        }
                        break;
                    case 12:
                        LoggerGeneral.info("12");
                        if (appPreferences.getInt("property_count", 0) > 0) {
                            Intent members = new Intent(AddProperty.this, MyProperty.class);
                            Common.showToast(context, "Please select property first");
                            PropertyAdapter.intentCheck = 12;
                            startActivity(members);
                            finish();
                        } else {
                            Common.showToast(context, "Please add minimum 1 property");
                            mDrawerLayout.closeDrawers();
                        }
                        break;
                    case 13:
                        LoggerGeneral.info("13");
                        if (appPreferences.getInt("property_count", 0) > 0) {
                            Intent offers = new Intent(AddProperty.this, MyProperty.class);
                            Common.showToast(context, "Please select property first");
                            PropertyAdapter.intentCheck = 13;
                            startActivity(offers);
                            finish();
                        } else {
                            Common.showToast(context, "Please add minimum 1 property");
                            mDrawerLayout.closeDrawers();
                        }
                        break;
                    case 14:
                        LoggerGeneral.info("14");
                        if (appPreferences.getInt("property_count", 0) > 0) {
                            Intent complaint = new Intent(AddProperty.this, MyProperty.class);
                            Common.showToast(context, "Please select property first");
                            PropertyAdapter.intentCheck = 14;
                            startActivity(complaint);
                            finish();
                        } else {
                            Common.showToast(context, "Please add minimum 1 property");
                            mDrawerLayout.closeDrawers();
                        }
                        break;
                    case 15:
                        LoggerGeneral.info("15");
                        Intent notification=new Intent(AddProperty.this,NotificationApp.class);
                        //    notification.putExtra("SocietyId",Soci)
                        int test1=appPreferences.getInt("NotificationOld",0);
                        test1=test1+Constants.notififcationcount;
                        appPreferences.putInt("NotificationOld",test1);
                        Constants.notififcationcount=0;
                        startActivity(notification);
                        finish();
                        break;
                    case 16:

                       /* Intent intent = new Intent(AddProperty.this, Login.class);
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



        state = (TextView)findViewById(R.id.state);

        Spanned spstate = Html.fromHtml("<font color='#707070'>" + "Select Property Type" + "</font>" + " <font color='#FF0000'>" + "*" + "</font>");

        state.setHint(spstate);

        name= (EditText) findViewById(R.id.popertyName);
        Spanned spname = Html.fromHtml("<font color='#707070'>" + "Wing/Building/House Name" + "</font>" + " <font color='#FF0000'>" + "*" + "</font>");

        name.setHint(spname);


        self= (TextView) findViewById(R.id.definey);
        self.setOnClickListener(this);
        flatNo= (EditText) findViewById(R.id.addhouseno);

        Spanned spflatno = Html.fromHtml("<font color='#707070'>" + "Add House/Flat No." + "</font>" + " <font color='#FF0000'>" + "*" + "</font>");

        flatNo.setHint(spflatno);

        yes= (RadioButton) findViewById(R.id.yes);
        dateView=findViewById(R.id.dateView);

        hideVisible= (LinearLayout) findViewById(R.id.hideVisible);

        no= (RadioButton) findViewById(R.id.no);

        dateOfRent= (TextView) findViewById(R.id.datePickerRent);

        dateOfRent.setTypeface(Common.font(context, "arial.ttf"));

        name.setTypeface(Common.font(context, "arial.ttf"));
        self.setTypeface(Common.font(context, "arial.ttf"));

        flatNo.setTypeface(Common.font(context, "arial.ttf"));

        no.setTypeface(Common.font(context, "arial.ttf"));
        yes.setTypeface(Common.font(context, "arial.ttf"));

        state.setTypeface(Common.font(context, "arial.ttf"));

    /*    name.setFilters(new InputFilter[]{
                new InputFilter() {
                    @Override
                    public CharSequence filter(CharSequence cs, int start,
                                               int end, Spanned spanned, int dStart, int dEnd) {
                        // TODO Auto-generated method stub

                        if (cs.equals("")) { // for backspace
                            return cs;
                        }
                        if (cs.toString().matches("[a-zA-Z ]+")) {
                            LoggerGeneral.info("555enter55555====" + name.length());
                            if (name.length() < 50) {
                                return cs;

                            } else {
                                LoggerGeneral.info("555enter");
                                Toast.makeText(context, "Name should not be more than 50 characters.", Toast.LENGTH_LONG).show();
                                return "";
                            }
                        }
                        return "";
                    }
                }
        });*/


        state.setOnClickListener(this);

        proptype = (TextView)findViewById(R.id.proptype);

        String udata = "Add Property Type";
        SpannableString content = new SpannableString(udata);
        content.setSpan(new UnderlineSpan(), 0, udata.length(), 0);
        proptype.setText(content);
        proptype.setTypeface(Common.font(context, "arial.ttf"));
        proptype.setTextColor(getResources().getColor(R.color.color_primary));
        proptype.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(AddProperty.this);
                dialog.setContentView(R.layout.addproppopup);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

                final EditText dialogtxt = (EditText)dialog.findViewById(R.id.dialogtxt);


                //sdialogtxt = dialogtxt.getText().toString().trim();

                LinearLayout verifyb = (LinearLayout)dialog.findViewById(R.id.verifyb);


                verifyb.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        sdialogtxt = dialogtxt.getText().toString().trim();


                        if (sdialogtxt != null && sdialogtxt.length() > 0) {
                            dialog.dismiss();
                            state.setText(sdialogtxt);
                        }
                        else {
                            Common.showToast(context,"Enter property type");
                        }

                    }
                });




                dialog.show();
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
                updateLabel();
            }

        };

        dateOfRent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                /*DatePickerDialog datePicker = new DatePickerDialog(context,
                        R.style.DialogTheme, datePickerListener,
                        cal.get(Calendar.YEAR),
                        cal.get(Calendar.MONTH),
                        cal.get(Calendar.DAY_OF_MONTH));

                datePicker.setButton(DialogInterface.BUTTON_POSITIVE, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    dateOfRent.setText(Common.getStandardStringFormateDate(cal));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        });
                datePicker.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {


                            }
                        });
                datePicker.show();*/

                new DatePickerDialog(AddProperty.this,R.style.DialogTheme, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                hideVisible.setVisibility(View.VISIBLE);
            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                hideVisible.setVisibility(View.GONE);
            }
        });

        availtxt=(TextView)findViewById(R.id.availtxt);
        availradio = (LinearLayout) findViewById(R.id.availradio);

        addprop  = (LinearLayout)findViewById(R.id.addprop);
        addprop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                tpropertyname = name.getText().toString().trim();
                tpropertytype = state.getText().toString().trim();
                tdefine = self.getText().toString().trim();

                thouseno = flatNo.getText().toString().trim();

                availforrent = dateOfRent.getText().toString().trim();

                if (yes.isChecked()) {
                    availableRentRadio = yes.getText().toString();
                    LoggerGeneral.info("socPost===" + availableRentRadio);

                }

                if (no.isChecked()) {
                    availableRentRadio = no.getText().toString();
                    LoggerGeneral.info("socPost===" + availableRentRadio);
                }

                if(tpropertyname!=null && tpropertyname.length()>0) {

                    if(!state.getText().toString().equalsIgnoreCase("Select property type") && state.length()>0 && tpropertytype.length()>0) {

                        if(self != null && self.length() > 0) {

                            if (!self.getText().toString().equalsIgnoreCase("Define Yourself") && state.length()>0 && thouseno.length() > 0) {

                                if (availableRentRadio != null && availableRentRadio.equals("Yes")) {

                                    availableRentRadio="1";
                                    if (availforrent != null && availforrent.length() > 0) {
                                        if (Common.isOnline(context)) {

                                            new AddProp().execute();
                                        } else {
                                            Common.showToast(context, "No internet connection");
                                        }

                                    } else {
                                        Common.showToast(context, "Select available for rent date");
                                    }

                                } else {
                                    availforrent = "";

                                    availableRentRadio="0";
                                    if (Common.isOnline(context)) {

                                        new AddProp().execute();
                                    } else {
                                        Common.showToast(context, "No internet connection");
                                    }

                                }


                            } else {
                                Common.showToast(context, "Enter house no");
                            }
                        }
                        else {
                            Common.showToast(context, "Define yourself");

                        }
                    }
                    else {
                        Common.showToast(context, "Select property type");

                    }
                }
                else
                {
                    Common.showToast(context, "Enter wing/building/house name");
                }

             /*   Intent myprop = new Intent(AddProperty.this, MyProperty.class);
                startActivity(myprop);
                finish();*/
            }
        });



    }

/*----------------------------------------------------------------------------------------------------------*/

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

            mDialog = new ProgressDialog(AddProperty.this,ProgressDialog.THEME_HOLO_DARK);
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
                        Intent register   = new Intent(AddProperty.this,Login.class);
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




    class  AddProp extends AsyncTask<String, String, JSONObject> {
        String type;
        ProgressDialog mDialog;
        @Override
        protected JSONObject doInBackground(String... params) {
            // TODO Auto-generated method stub

            String url= Constants.Base+Constants.AddProp;

            JSONObject object = new JSONObject();
            try {
                LoggerGeneral.info("socId---"+societyId);

                object.put("property_name",tpropertyname);
                object.put("society_id",societyId);
                object.put("user_id",appPreferences.getString("user_id",""));
                object.put("property_type",tpropertytype);
                object.put("user_type",tdefine);
                object.put("house_no",thouseno);
                object.put("is_available",availableRentRadio);
                object.put("available_from",availforrent);
                object.put("property_image",propertyImage);
                object.put("image_extension","jpg");


                LoggerGeneral.info("JsonObjectPrint" + object.toString());

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

            mDialog = new ProgressDialog(AddProperty.this,ProgressDialog.THEME_HOLO_DARK);
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


                            JSONObject data = results.getJSONObject("data");

                            String property_id = data.getString("property_id");

                            appPreferences.putString("property_id", property_id);

                            appPreferences.putInt("property_count", appPreferences.getInt("property_count", 0) + 1);

                            LoggerGeneral.info("count after add property"+appPreferences.getInt("property_count",0));


                            Intent intent  = new Intent(AddProperty.this,MyProperty.class);
                            startActivity(intent);
                            finish();

                        }

                    }

                    if(account_status == 0) {
                        Intent intent = new Intent(AddProperty.this,Login.class);
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







    @Override
    protected void onStart() {
        super.onStart();
        //FlurryAgent.onStartSession(this, "RFRD6MNNVSYQ2XRCCFRT");
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
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.state:
                //      onClickList();
                propertyTypes = new ArrayList<PropertyType>();

                if(Common.isOnline(context)) {

                    new getPropType().execute();
                }
                else {
                    Common.showToast(context,"No interenet connection");
                }
                break;

            case R.id.definey:
                onClickuserType();

        }
    }




    class  getPropType extends AsyncTask<String, String, JSONObject> {
        String type;
        ProgressDialog mDialog;
        @Override
        protected JSONObject doInBackground(String... params) {
            // TODO Auto-generated method stub

            String url= Constants.Base+Constants.GetPropertyType;

            JSONObject object = new JSONObject();
            try {

                object.put("","");

                LoggerGeneral.info("JsonObjectPrint" + object.toString());

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

            mDialog = new ProgressDialog(AddProperty.this,ProgressDialog.THEME_HOLO_DARK);
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
                            JSONArray data = results.getJSONArray("data");

                            for(int i=0;i<=data.length()-1;i++){


                                propertyType = new PropertyType();
                                JSONObject jsonObject = data.getJSONObject(i);

                                String id = jsonObject.getString("id");

                                String property_typetxt = jsonObject.getString("property_type");

                                propertyType.setId(id);
                                propertyType.setProperty_type(property_typetxt);

                                propertyTypes.add(propertyType);




                            }




                        }

                    }

                    if(account_status == 0) {
                        Intent intent = new Intent(AddProperty.this,Login.class);
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


                onPropClick();

            }
        }

    }

    public void onPropClick(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);


        ArrayList<String>arr= new ArrayList<String>();

        ArrayList<String>arrst = new ArrayList<String>();
        LoggerGeneral.info("showstarr"+propertyTypes.size()+"---"+propertyTypes.toString());
        for(int i=0;i<=propertyTypes.size()-1;i++) {


            String array = propertyTypes.get(i).getProperty_type();

            String arraystid = propertyTypes.get(i).getId();

            arr.add(array);

            arrst.add(arraystid);
        }

        mStringArray = new String[arr.size()];
        mStringArray = arr.toArray(mStringArray);

        mstrinArraystid = new String[arrst.size()];
        mstrinArraystid = arrst.toArray(mstrinArraystid);

        builder.setItems(mStringArray, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                state.setText(mStringArray[which]);
               /* if(state.getText().toString().equalsIgnoreCase("Not in The List")){
                    state.setText("");
                }*/
                // The 'which' argument contains the index position
                // of the selected item
            }
        });

        AlertDialog alert = builder.create();
        alert.show();


    }



    public void onClickuserType(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setItems(defineYourself, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {


                self.setText(defineYourself[which]);
//"Tenant","Sub Tenant"};
                if(self.getText().toString().equalsIgnoreCase("Tenant")||self.getText().toString().equalsIgnoreCase("Sub Tenant")){

                    availtxt.setVisibility(View.INVISIBLE);
                    availradio.setVisibility(View.INVISIBLE);
                    no.setChecked(true);
                    yes.setChecked(false);

                }
                //"Owner","Sub Owner"
                if(self.getText().toString().equalsIgnoreCase("Owner")||self.getText().toString().equalsIgnoreCase("Sub Owner")){
                    availtxt.setVisibility(View.VISIBLE);
                    availradio.setVisibility(View.VISIBLE);
                    no.setChecked(true);
                    yes.setChecked(false);

                }
                // The 'which' argument contains the index position
                // of the selected item
            }
        });

        AlertDialog alert = builder.create();
        alert.show();


    }


    public void onClickList(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setItems(propArray, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {


                state.setText(propArray[which]);

                if(state.getText().toString().equalsIgnoreCase("Not in The List")){
                    state.setText("");
                }
                // The 'which' argument contains the index position
                // of the selected item
            }
        });

        AlertDialog alert = builder.create();
        alert.show();


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
        else {

            super.onBackPressed();
            Intent back = new Intent(AddProperty.this, MySociety.class);
            back.putExtra("societylist", (new DataWrapper((ArrayList<SocietyModel>)societies)));
            startActivity(back);
            finish();
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

        final Dialog mBottomSheetDialog = new Dialog (AddProperty.this,
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

        propertyImage=getStringImage(newBMP);

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

    private void updateLabel() {

        String myFormat = "dd-MM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        Date now = new Date();
        if(myCalendar.getTime().after(now)) {
            dateOfRent.setText(sdf.format(myCalendar.getTime()));
            dateOfRent.setTextColor(Color.parseColor("#525252"));
        }
        else {
            Common.showToast(context,"Previous date not allowed");
        }



    }

    private boolean checkPermission3(){
        int result = ContextCompat.checkSelfPermission(context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED){

            return true;

        } else {

            return false;

        }
    }


}
