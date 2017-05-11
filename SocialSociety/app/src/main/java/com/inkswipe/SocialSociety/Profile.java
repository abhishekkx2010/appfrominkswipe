package com.inkswipe.SocialSociety;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.flurry.android.FlurryAgent;
import com.inkswipe.SocialSociety.pulltozoomview.PullToZoomScrollViewEx;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import adapter.CropOptionAdapter;
import adapter.CustomAdapter;
import adapter.PropertyAdapter;
import model.CropOption;
import model.DrawerList;
import util.AppPreferences;
import util.Common;
import util.Constants;
import util.ExceptionHandler;
import util.LoggerGeneral;
import util.ServiceFacade;

public class Profile extends AppCompatActivity {
    private PullToZoomScrollViewEx scrollView;
    RelativeLayout editprofile;
    LinearLayout searchsociety,myProperty;
    LinearLayout home;
    ImageView propertyicon;
    TextView myproptxt;
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    RelativeLayout camera;
    public static Context context;
    static ImageView coverImage;
    public static int permissionChecker=0;
    private static final int PERMISSION_REQUEST_CODE = 1;
    static AlertDialog ad;
    Uri imageUri,imageUri1;
    public static Bundle temp_bundle;
    static final int requestCodeForSdCard = 1, requestCodeForCamera = 2, requestCodeForCorp = 3;
    Bitmap mBitmap;
    static ImageView rphoto;
    String destinationImagePath;
    LinearLayout coverImageChoose;
    static int imageChecker=0;
    String device_id;
    RelativeLayout notificationcount;
    TextView notificationtext;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    Bitmap  strbitmap,strbitmapcover;
    Bitmap bitmap,bitmapcover;
    RelativeLayout notification;
    String Hooooo;
    /*private DrawerLayout mDrawerLayout;

    Fragment fragment = null;
    TextView appname;
    ExpandableListView expListView;
    HashMap<String, List<String>> listDataChild;
    ExpandableListAdapter listAdapter;
    List<String> listDataHeader;*/

    static  AppPreferences appPreferences;
    DisplayImageOptions options,optionsc;
    AlertDialog alert;

    String id,name,email,mobile ,gender,address,landmark,state,city,pincode,profile_image_url,cover_image_url;
    int propertyCount;
    MyTextView nametxt,emailidtxt,mobiletxt,gendertxt,adderesstxt,landmarktxt,statetxt,citytxt,pincodetxt;

    Bitmap coverstrbitmap,prstrbitmap;
    String coverImageChange,profileImageChange;
	int savedInstanceCheck=0;
    ProfileAsyctask profileAsyncTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (getIntent().hasExtra("bundle") && savedInstanceState==null){
            savedInstanceCheck=1;
            LoggerGeneral.info("isItRight");
            savedInstanceState = getIntent().getExtras().getBundle("bundle");
        }
        super.onCreate(savedInstanceState);

        PropertyAdapter.intentCheck=0;
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        setContentView(R.layout.activity_profile);
        context=Profile.this;
        Common.internet_check=1;
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
        ImageLoader.getInstance().init(config);

        appPreferences = AppPreferences.getAppPreferences(context);

        device_id = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.fallb)
                .showImageForEmptyUri(R.mipmap.fallb)
                .showImageOnFail(R.mipmap.fallb)
                .cacheInMemory(false)
                .cacheOnDisk(false)
                .considerExifParams(true)
                .displayer(new SimpleBitmapDisplayer())
                .build();

        optionsc = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.cover_image)
                .showImageForEmptyUri(R.mipmap.cover_image)
                .showImageOnFail(R.mipmap.cover_image)
                .cacheInMemory(false)
                .cacheOnDisk(false)
                .considerExifParams(true)
                .displayer(new SimpleBitmapDisplayer())
                .build();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        loadViewForCode();
        TextView title=((TextView) toolbar.findViewById(R.id.toolbar_title));
        title.setText(this.getTitle());
        title.setGravity(Gravity.START);
        toolbar.setTitleTextColor(android.graphics.Color.WHITE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Hooooo=getIntent().getStringExtra("Hooooo");


        String one = "Your one time password is 530485";
        String newString = one.replaceAll("[^0-9]", "").trim();


        LoggerGeneral.info("shownum---"+newString);
        if(savedInstanceCheck==1)
        {
            Hooooo=savedInstanceState.getString("Hooooo");
        }

      //  toolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.backarrow));
      //  toolbar.getNavigationIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
       // setUpDrawer();


/*

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //What to do on back clicked
           */
/*     Intent intent = new Intent(Profile.this, Login.class);
                startActivity(intent);
                finish();*//*


                if(mDrawerLayout.isDrawerOpen(GravityCompat.END))
                {
                    mDrawerLayout.closeDrawers();
                    return;
                }
                else {

                    //super.onBackPressed();
                    AlertDialog.Builder ab = new AlertDialog.Builder(Profile.this);
                    ab.setMessage("Are you sure to exit?");
                    ab.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            //if you want to kill app . from other then your main avtivity.(Launcher)
                            android.os.Process.killProcess(android.os.Process.myPid());
                            System.exit(1);

                            //if you want to finish just current activity

                            //Profile.this.finish();
                        }
                    });
                    ab.setNegativeButton("no", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    ab.show();
                }
            }
        });
*/

        LoggerGeneral.info("Profile tag==="+Hooooo);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.my_statusbar_color));
        }

        notification= (RelativeLayout) findViewById(R.id.notification);

        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent notification=new Intent(Profile.this,NotificationApp.class);
            //    notification.putExtra("SocietyId",Soci)
                int test1=appPreferences.getInt("NotificationOld",0);
                test1=test1+Constants.notififcationcount;
                appPreferences.putInt("NotificationOld",test1);
                Constants.notififcationcount=0;
                Common.internet_check=0;
                startActivity(notification);
                finish();
            }
        });

        notificationcount= (RelativeLayout) findViewById(R.id.notificationcount);

        notificationtext= (TextView) findViewById(R.id.notificationtext);

        if(Common.isOnline(context)) {
            new getNotifications().execute();
        }
        else {
            Common.showToast(context, "No internet connection");
        }

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        LayoutInflater inflater = getLayoutInflater();
        View listHeaderView = inflater.inflate(R.layout.header_list, null, false);

        LinearLayout navHome= (LinearLayout) listHeaderView.findViewById(R.id.Navhome);

        navHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(appPreferences.getInt("property_count",0)>0) {
                Intent myProperty=new Intent(Profile.this,MyProperty.class);
                    PropertyAdapter.intentCheck=0;
                    Common.internet_check=0;
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


        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // make ToastCustom when click
                /*ToastCustom.makeText(getApplicationContext(), "Position " + position, ToastCustom.LENGTH_LONG).show();
                */

                switch (position) {

                    case 1:
                        LoggerGeneral.info("1");
                       /* Intent profile=new Intent(Profile.this,MyProperty.class);
                        startActivity(profile);
                        finish();*/
                        mDrawerLayout.closeDrawers();
                        break;
                    case 2:
                        LoggerGeneral.info("2");
                            Intent addSociety = new Intent(Profile.this, AddSociety.class);
                            startActivity(addSociety);
                        Common.internet_check=0;
                            finish();
                        break;
                    case 3:
                        LoggerGeneral.info("3");
                        if(appPreferences.getInt("property_count",0)>0) {
                        Intent myProperty=new Intent(Profile.this,MyProperty.class);
                        PropertyAdapter.intentCheck=0;
                            Common.internet_check=0;
                        startActivity(myProperty);
                        finish();
                        }
                        else
                        {
                            Common.showToast(context, "Please add minimum 1 property");
                            mDrawerLayout.closeDrawers();
                        }
                        break;
                    case 4:
                        LoggerGeneral.info("4");
                        if(appPreferences.getInt("property_count",0)>0) {
                        Intent events=new Intent(Profile.this,MyProperty.class);
                        events.putExtra("event",3);
                        Common.showToast(context, "Please select property first");
                        PropertyAdapter.intentCheck=4;
                            Common.internet_check=0;
                        startActivity(events);
                        finish();
                        }
                        else
                        {
                            Common.showToast(context, "Please add minimum 1 property");
                            mDrawerLayout.closeDrawers();
                        }
                        break;
                    case 5:
                        LoggerGeneral.info("5");
                        if(appPreferences.getInt("property_count",0)>0) {
                        Intent createEvents=new Intent(Profile.this,MyProperty.class);
                        Common.showToast(context, "Please select property first");
                        PropertyAdapter.intentCheck=5;
                            Common.internet_check=0;
                        startActivity(createEvents);
                        finish();
                        }
                        else
                        {
                            Common.showToast(context, "Please add minimum 1 property");
                            mDrawerLayout.closeDrawers();
                        }
                        break;
                    case 6:
                        LoggerGeneral.info("6");
                        if(appPreferences.getInt("property_count",0)>0) {
                        Intent archivedEvents=new Intent(Profile.this,MyProperty.class);
                        Common.showToast(context, "Please select property first");
                        PropertyAdapter.intentCheck=6;
                            Common.internet_check=0;
                        startActivity(archivedEvents);
                        finish();
                        }
                        else
                        {
                            Common.showToast(context, "Please add minimum 1 property");
                            mDrawerLayout.closeDrawers();
                        }
                        break;
                    case 7:
                        LoggerGeneral.info("7");
                        if(appPreferences.getInt("property_count",0)>0) {
                        Intent announcement=new Intent(Profile.this,MyProperty.class);
                        Common.showToast(context, "Please select property first");
                        PropertyAdapter.intentCheck=7;
                        announcement.putExtra("announcement",4);
                            Common.internet_check=0;
                        startActivity(announcement);
                        finish();
                        }
                        else
                        {
                            Common.showToast(context, "Please add minimum 1 property");
                            mDrawerLayout.closeDrawers();
                        }
                        break;
                    case 8:
                        LoggerGeneral.info("8");
                        if(appPreferences.getInt("property_count",0)>0) {
                        Intent createAnnouncement=new Intent(Profile.this,MyProperty.class);
                        Common.showToast(context, "Please select property first");
                        PropertyAdapter.intentCheck=8;
                            Common.internet_check=0;
                        startActivity(createAnnouncement);
                        finish();
                        }
                        else
                        {
                            Common.showToast(context, "Please add minimum 1 property");
                            mDrawerLayout.closeDrawers();
                        }
                        break;
                    case 9:
                        LoggerGeneral.info("9");
                        if(appPreferences.getInt("property_count",0)>0) {
                        Intent polls=new Intent(Profile.this,MyProperty.class);
                        Common.showToast(context, "Please select property first");
                        PropertyAdapter.intentCheck=9;
                        polls.putExtra("polls",2);
                            Common.internet_check=0;
                        startActivity(polls);
                        finish();
                        }
                        else
                        {
                            Common.showToast(context, "Please add minimum 1 property");
                            mDrawerLayout.closeDrawers();
                        }
                        break;
                    case 10:
                        LoggerGeneral.info("10");
                        if(appPreferences.getInt("property_count",0)>0) {
                        Intent createEventPoll=new Intent(Profile.this,MyProperty.class);
                        Common.showToast(context, "Please select property first");
                        PropertyAdapter.intentCheck=10;
                            Common.internet_check=0;
                        startActivity(createEventPoll);
                        finish();
                        }
                        else
                        {
                            Common.showToast(context, "Please add minimum 1 property");
                            mDrawerLayout.closeDrawers();
                        }
                        break;
                    case 11:
                        LoggerGeneral.info("11");
                        if(appPreferences.getInt("property_count",0)>0) {
                        Intent archivedEventPoll=new Intent(Profile.this,MyProperty.class);
                        Common.showToast(context, "Please select property first");
                        PropertyAdapter.intentCheck=11;
                            Common.internet_check=0;
                        startActivity(archivedEventPoll);
                        finish();
                        }
                        else
                        {
                            Common.showToast(context, "Please add minimum 1 property");
                            mDrawerLayout.closeDrawers();
                        }
                        break;
                    case 12:
                        LoggerGeneral.info("12");
                        if(appPreferences.getInt("property_count",0)>0) {
                        Intent members=new Intent(Profile.this,MyProperty.class);
                        Common.showToast(context, "Please select property first");
                        PropertyAdapter.intentCheck=12;
                            Common.internet_check=0;
                        startActivity(members);
                        finish();
                        }
                        else
                        {
                            Common.showToast(context, "Please add minimum 1 property");
                            mDrawerLayout.closeDrawers();
                        }
                        break;
                    case 13:
                        LoggerGeneral.info("13");
                        if(appPreferences.getInt("property_count",0)>0) {
                        Intent offers=new Intent(Profile.this,MyProperty.class);
                        Common.showToast(context, "Please select property first");
                        PropertyAdapter.intentCheck=13;
                            Common.internet_check=0;
                        startActivity(offers);
                        finish();
                        }
                        else
                        {
                            Common.showToast(context, "Please add minimum 1 property");
                            mDrawerLayout.closeDrawers();
                        }
                        break;
                    case 14:
                        LoggerGeneral.info("14");
                        if(appPreferences.getInt("property_count",0)>0) {
                        Intent complaint=new Intent(Profile.this,MyProperty.class);
                        Common.showToast(context, "Please select property first");
                        PropertyAdapter.intentCheck=14;
                            Common.internet_check=0;
                        startActivity(complaint);
                        finish();
                        }
                        else
                        {
                            Common.showToast(context, "Please add minimum 1 property");
                            mDrawerLayout.closeDrawers();
                        }
                        break;
                    case 15:
                        LoggerGeneral.info("15");
                        Intent notification=new Intent(Profile.this,NotificationApp.class);
                        //    notification.putExtra("SocietyId",Soci)
                        int test1=appPreferences.getInt("NotificationOld",0);
                        test1=test1+Constants.notififcationcount;
                        appPreferences.putInt("NotificationOld", test1);
                        Constants.notififcationcount=0;
                        Common.internet_check=0;
                        startActivity(notification);
                        finish();
                        break;
                    case 16:

                       /* Intent intent = new Intent(Profile.this,Login.class);
                        appPreferences.remove("user_id");
                        appPreferences.remove("user_name");
                        appPreferences.remove("email_id");
                        appPreferences.remove("property_count");
                        appPreferences.remove("storecoverimage");
                        appPreferences.remove("storeimage");
                        appPreferences.remove("profile_image_url");
                        Common.internet_check=0;
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

        scrollView = (PullToZoomScrollViewEx) findViewById(R.id.scroll_view);
        DisplayMetrics localDisplayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(localDisplayMetrics);
        int mScreenHeight = localDisplayMetrics.heightPixels;
        int mScreenWidth = localDisplayMetrics.widthPixels;
        LinearLayout.LayoutParams localObject = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (9.0F * (mScreenWidth / 16.0F)));
        scrollView.setHeaderLayoutParams(localObject);

        editprofile = (RelativeLayout)scrollView.getRootView().findViewById(R.id.editprofile);
        if(Common.isOnline(context)) {
            editprofile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent editpr = new Intent(Profile.this, EditProfile.class);


                    editpr.putExtra("id", id);
                    editpr.putExtra("name", name);
                    editpr.putExtra("email", email);
                    editpr.putExtra("mobile", mobile);
                    editpr.putExtra("gender", gender);
                    editpr.putExtra("address", address);
                    editpr.putExtra("landmark", landmark);
                    editpr.putExtra("city", city);
                    editpr.putExtra("state", state);
                    editpr.putExtra("pincode", pincode);


                    Common.internet_check = 0;


                    startActivity(editpr);
                    finish();
                }
            });
        }
        else
        {
            Common.showToast(context,"No internet connection");
        }

        searchsociety = (LinearLayout)scrollView.getRootView().findViewById(R.id.searchsociety);
        searchsociety.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent searchsoc = new Intent(Profile.this,SearchSociety.class);
                Common.internet_check=0;
                startActivity(searchsoc);
                finish();
            }
        });





        propertyicon = (ImageView)scrollView.getRootView().findViewById(R.id.propertyicon);
        myproptxt    = (TextView) scrollView.getRootView().findViewById(R.id.myproptxt);
    /*    if(appPreferences.getInt("property_count",0)>0) {

            LoggerGeneral.info("pcount11---" + appPreferences.getInt("property_count", 0));

            propertyicon.setBackgroundResource(R.mipmap.my_property);
            myproptxt.setTextColor(getResources().getColor(R.color.black));

        }
        else
        {
            LoggerGeneral.info("pcount22---" + appPreferences.getInt("property_count", 0));

            propertyicon.setBackgroundResource(R.mipmap.mypropgrey);
            myproptxt.setTextColor(getResources().getColor(R.color.color_hint));

        }*/
        myProperty  = (LinearLayout)scrollView.getRootView().findViewById(R.id.myProperty);
        if(Common.isOnline(context)) {
            myProperty.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (appPreferences.getInt("property_count", 0) > 0) {


                        Intent myprop = new Intent(Profile.this, MyProperty.class);
                        Common.internet_check = 0;
                        startActivity(myprop);
                        finish();
                    } else {

                        Common.showToast(context, "Please add minimum 1 property");
                    }
                }
            });
        }
        else {
            Common.showToast(context,"No internet Connection");
        }
        camera = (RelativeLayout) findViewById(R.id.camera);
        rphoto = (ImageView) findViewById(R.id.userImage);
        coverImage= (ImageView) findViewById(R.id.background);
        coverImageChoose= (LinearLayout) findViewById(R.id.coverImage);


        coverImageChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                imageChecker=2;

                if (Build.VERSION.SDK_INT >= 23) {
                    // Do some stuff


                    if (!checkPermission2()) {

                        requestPermission();

                    } else {
                        imageChecker=2;
                        selectImage();
                    }
                } else {
                    imageChecker=2;
                    selectImage();
                }


            }
        });



        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                imageChecker = 1;

                if (Build.VERSION.SDK_INT >= 23) {
                    // Do some stuff


                    if (!checkPermission2()) {

                        requestPermission();

                    } else {
                        imageChecker = 1;
                        selectImage();
                    }
                } else {
                    imageChecker = 1;
                    selectImage();
                }


            }
        });

        nametxt =(MyTextView)scrollView.getPullRootView().findViewById(R.id.name);
        emailidtxt = (MyTextView)scrollView.getPullRootView().findViewById(R.id.email);
        mobiletxt = (MyTextView)scrollView.getPullRootView().findViewById(R.id.mobile);
        gendertxt = (MyTextView)scrollView.getPullRootView().findViewById(R.id.gender);
        adderesstxt = (MyTextView)scrollView.getPullRootView().findViewById(R.id.address);
        landmarktxt = (MyTextView)scrollView.getPullRootView().findViewById(R.id.landmark);
        statetxt  =(MyTextView)scrollView.getPullRootView().findViewById(R.id.state);
        citytxt = (MyTextView)scrollView.getPullRootView().findViewById(R.id.city);
        pincodetxt = (MyTextView)scrollView.getPullRootView().findViewById(R.id.pincode);

       /* if(!appPreferences.getString("profile_image_url", "").equals(null)||appPreferences.getString("profile_image_url", "")!=null) {
            ImageLoader.getInstance().displayImage(profile_image_url, rphoto, options, animateFirstListener);

        }
        if(!appPreferences.getString("cover_image_url", "").equals(null)||appPreferences.getString("cover_image_url", "")!=null) {

            ImageLoader.getInstance().displayImage(cover_image_url, coverImage, optionsc, animateFirstListener);
        }*/



     /*  if(appPreferences.getString("storecoverimage","")!=null||!appPreferences.getString("storecoverimage","").equals(null)){



           coverImage.setImageBitmap(StringToBitMap(appPreferences.getString("storecoverimage","")));

           LoggerGeneral.info("preferencecoverimage===" + StringToBitMap(appPreferences.getString("storecoverimage", "")));

       }
        if(appPreferences.getString("storeimage","")!=null||!appPreferences.getString("storeimage","").equals(null)){




            rphoto.setImageBitmap(StringToBitMap(appPreferences.getString("storeimage","")));

            LoggerGeneral.info("preferenceimage===" + StringToBitMap(appPreferences.getString("storeimage", "")));

        }*/
        if(StringToBitMap(appPreferences.getString("storeimage", ""))==null ){

            rphoto.setImageResource(R.mipmap.fallb);
        }



      //  LoggerGeneral.info("show111--"+appPreferences.getString("storeimageres","")+"---"+appPreferences.getString("storeimagecoverres",""));

      //  appPreferences.putString("storeimagecoverres", storeImagecover);

        if(Common.isOnline(context)) {

            if (!appPreferences.getString("profile_image_url", "").equals(null) || appPreferences.getString("profile_image_url", "") != null) {
                ImageLoader.getInstance().displayImage(profile_image_url, rphoto, options, animateFirstListener);

            }
            if (!appPreferences.getString("cover_image_url", "").equals(null) || appPreferences.getString("cover_image_url", "") != null) {

                ImageLoader.getInstance().displayImage(cover_image_url, coverImage, optionsc, animateFirstListener);
            }
        }
        if(appPreferences.getString("storeimageres", "")!=null){
            rphoto.setImageBitmap(StringToBitMap(appPreferences.getString("storeimageres","")));


            LoggerGeneral.info("show222-" + appPreferences.getString("storeimageres", ""));

        }
        // appPreferences.putString("storeimageres", storeImage);
        if(appPreferences.getString("storeimagecoverres", "")!=null){
            coverImage.setImageBitmap(StringToBitMap(appPreferences.getString("storeimagecoverres","")));

            LoggerGeneral.info("show333-" + appPreferences.getString("storeimageres", ""));

        }

      //  profileAsyncTask = new

         if(Common.isOnline(context)) {
             profileAsyncTask = new ProfileAsyctask();
             profileAsyncTask.execute();
           //  new getProfile().execute();
         } else {
             Common.showToast(context,"No internet connection");
         }


        try {
            String jwt1 = ServiceFacade.JwtHeadernormal("1");
            String jwt2 = ServiceFacade.JwtHeadernormal("2");
            String jwt3 = ServiceFacade.JwtHeadernormal("3");
            String jwt4 = ServiceFacade.JwtHeadernormal("4");
            String jwt8 = ServiceFacade.JwtHeadernormal("8");
            String jwt9 = ServiceFacade.JwtHeadernormal("9");
            String jwt10 = ServiceFacade.JwtHeadernormal("10");
            String jwt11 = ServiceFacade.JwtHeadernormal("11");
            String jwt12 = ServiceFacade.JwtHeadernormal("12");
            String jwt13 = ServiceFacade.JwtHeadernormal("13");
            String jwt14 = ServiceFacade.JwtHeadernormal("14");
            String jwt15 = ServiceFacade.JwtHeadernormal("15");
            String jwt20 = ServiceFacade.JwtHeadernormal("20");
            String jwt21 = ServiceFacade.JwtHeadernormal("21");
            String jwt22 = ServiceFacade.JwtHeadernormal("22");
            String jwt23 = ServiceFacade.JwtHeadernormal("23");
            String jwt24 = ServiceFacade.JwtHeadernormal("24");
            String jwt25 = ServiceFacade.JwtHeadernormal("25");


            String jwt30 = ServiceFacade.JwtHeadernormal("30");


            String jwt100 = ServiceFacade.JwtHeadernormal("100");

            String jwt1000 = ServiceFacade.JwtHeadernormal("1001");

            String jwt10001 = ServiceFacade.JwtHeadernormal("10001");

            LoggerGeneral.info("jwt1 ---"+jwt1 );
            LoggerGeneral.info("jwt2  --"+jwt2 );
            LoggerGeneral.info("jwt3 ---"+jwt3 );
            LoggerGeneral.info("jwt4 ---"+jwt4 );
            LoggerGeneral.info("jwt8 ---"+jwt8 );
            LoggerGeneral.info("jwt9 ---"+jwt9 );
            LoggerGeneral.info("jwt10---"+jwt10);
            LoggerGeneral.info("jwt11---"+jwt11);
            LoggerGeneral.info("jwt12---"+jwt12);
            LoggerGeneral.info("jwt13---"+jwt13);
            LoggerGeneral.info("jwt14---"+jwt14);
            LoggerGeneral.info("jwt15---"+jwt15);
            LoggerGeneral.info("jwt20---"+jwt20);
            LoggerGeneral.info("jwt21---"+jwt21);
            LoggerGeneral.info("jwt22---"+jwt22);
            LoggerGeneral.info("jwt23---"+jwt23);
            LoggerGeneral.info("jwt24---"+jwt24);
            LoggerGeneral.info("jwt25---"+jwt25);

            LoggerGeneral.info("jwt30---"+jwt30);

            LoggerGeneral.info("jwt100---"+jwt100);

            LoggerGeneral.info("jwt1000---"+jwt1000);

            LoggerGeneral.info("jwt10001---"+jwt10001);



                /*  jwt1 ---eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyaWQiOiIxIiwidXNlcm5hbWUiOiJndWVzdCJ9.lnhymTo17L6nwr+zk4K7fCnqac+rmbFvCCa9gBFZnvU=
                    jwt2  --eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyaWQiOiIyIiwidXNlcm5hbWUiOiJndWVzdCJ9.+Dwhtmr3k++NYSzixidu84JSQoPv97AIjAgyxmLqoIE=
                    jwt3 ---eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyaWQiOiIzIiwidXNlcm5hbWUiOiJndWVzdCJ9.FfBzqpe+79LYsfjltqO4JzqoqVBpLGAn4sA8e40+Faw=
                    jwt4 ---eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyaWQiOiI0IiwidXNlcm5hbWUiOiJndWVzdCJ9.Ey567r0ZmxsHaHuSj1EjF312ODWXknPwXXztnDriNDw=
                    jwt8 ---eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyaWQiOiI4IiwidXNlcm5hbWUiOiJndWVzdCJ9.FiJOAA1g5DWkrhQX6o7AEP3ym4PHRuxfOLma05I5kNs=
                    jwt9 ---eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyaWQiOiI5IiwidXNlcm5hbWUiOiJndWVzdCJ9.iAVCHqPl0xJfY/OOTMeD5DTro0+MrjqFJavMNKSBA8k=
                    jwt10---eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyaWQiOiIxMCIsInVzZXJuYW1lIjoiZ3Vlc3QifQ.IDMZargdDqtwHrxufUiRq4k8eFTx9Eir97k4S9ZvM34=
                    jwt11---eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyaWQiOiIxMSIsInVzZXJuYW1lIjoiZ3Vlc3QifQ.NIDrzgGG6oR2p7Uav+c15Hwun3UPjddSEpaM51G+KTw=
                    jwt12---eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyaWQiOiIxMiIsInVzZXJuYW1lIjoiZ3Vlc3QifQ.YvXEPJHx1BQy5eXV/VL1dnUumYc8ccFbT7KcxmrMBEE=
                    jwt13---eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyaWQiOiIxMyIsInVzZXJuYW1lIjoiZ3Vlc3QifQ.+hKBaDb8ExAJd3Y1oevBZZwG4lgVDSwSD7/bP4P1iaw=
                    jwt14---eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyaWQiOiIxNCIsInVzZXJuYW1lIjoiZ3Vlc3QifQ.JU0HR5HMtR+aG5kHHVcFmT7rtaqliDL57luiOrMJpg0=
                    jwt15---eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyaWQiOiIxNSIsInVzZXJuYW1lIjoiZ3Vlc3QifQ.uJ70m8+f0x+L+rXR0PH/dK5/qA4zG0hGbsTJ0eUx99Y=
                    jwt20---eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyaWQiOiIyMCIsInVzZXJuYW1lIjoiZ3Vlc3QifQ.R3MLBcrizvFJ0bgZykd8FHUiZKRgjjd24RTBuQ5Zsrg=
                    jwt21---eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyaWQiOiIyMSIsInVzZXJuYW1lIjoiZ3Vlc3QifQ.QI72GGepUPzzabyxg9UHZjWkhEOH+uOhRqaZxzkTqQE=
                    jwt22---eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyaWQiOiIyMiIsInVzZXJuYW1lIjoiZ3Vlc3QifQ.4Mt6fucTfxnicaMQmGVlCGTqWPUkl+r7sanfx6WUgBw=
                    jwt23---eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyaWQiOiIyMyIsInVzZXJuYW1lIjoiZ3Vlc3QifQ.0i5dtIUJOR1p+la6qb0eG9MxW3NB4mlYaWlYKDVppmU=
                    jwt24---eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyaWQiOiIyNCIsInVzZXJuYW1lIjoiZ3Vlc3QifQ.lnyO1ZtxKgPS07p3vFHnHZd3bwZ+5o6e66tGQr4nr4w=
                    jwt25---eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyaWQiOiIyNSIsInVzZXJuYW1lIjoiZ3Vlc3QifQ.qKumwnkikxi7zns/sADJlGExpbEhlo3qkAqlnXsvHiE=
*/













        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }

        temp_bundle = new Bundle();
        onSaveInstanceState(temp_bundle);
    }
/*==================================================================================================================*/
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current game state
        temp_bundle.putString("Hooooo", Hooooo);

        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        // Always call the superclass so it can restore the view hierarchy
        super.onRestoreInstanceState(savedInstanceState);

        // Restore state members from saved instance
       Hooooo=savedInstanceState.getString("Hooooo");
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


    public Bitmap StringToBitMap(String encodedString){
        try {
            byte [] encodeByte=Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap=BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
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
    class  ProfileAsyctask extends AsyncTask<String, String, JSONObject> {
        String type;
        ProgressDialog mDialog;



        @Override
        protected JSONObject doInBackground(String... params) {
            // TODO Auto-generated method stub

            String url= Constants.Base+Constants.GetProfile;

            JSONObject object = new JSONObject();
            try {

                object.put("user_id",appPreferences.getString("user_id",""));

                LoggerGeneral.info("JsonObjectPrint" + object.toString());

            } catch (Exception ex) {

            }

            String str = '"' + appPreferences.getString("jwt", "") + '"';
            JSONObject jsonObject  = ServiceFacade.getResponsJsonParams(url, object);

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
        protected void onCancelled(){

            super.onCancelled();
            mDialog.dismiss();
            LoggerGeneral.info("onCancelled");

        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

            mDialog = new ProgressDialog(Profile.this,ProgressDialog.THEME_HOLO_DARK);
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

                    /*String status_code1 = meta.getString("status_code");

                    LoggerGeneral.info("Status checking "+status_code1);*/

                    if(account_status==1){
                    if (status_code == 0) {
                        JSONObject data = results.getJSONObject("data");

                        id                 = data.getString("id");
                        name               = data.getString("name");
                        email              = data.getString("email");
                        mobile             = data.getString("mobile");
                        gender             = data.getString("gender");
                        address            = data.getString("address");
                        landmark           = data.getString("landmark");
                        city               = data.getString("city");
                        state              = data.getString("state");
                        pincode            = data.getString("pincode");
                        profile_image_url  = data.getString("profile_image_url");
                        cover_image_url    = data.getString("cover_image_url");
                        propertyCount       =data.getInt("property_count");
                        appPreferences.putInt("property_count",propertyCount);


                        nametxt.setText(name);
                        emailidtxt.setText(email);
                        mobiletxt.setText(mobile);

                        if (gender != null) {
                            gendertxt.setText(gender);

                        }
                        if (address != null) {
                            adderesstxt.setText(address);
                        }

                        if (landmark != null) {
                            landmarktxt.setText(landmark);
                        }

                        if (state != null) {

                            statetxt.setText(state);
                        }

                        if (city != null) {
                            citytxt.setText(city);
                        }
                        if (pincode != null) {
                            pincodetxt.setText(pincode);
                        }
                        ImageLoader.getInstance().displayImage(profile_image_url, rphoto, options, animateFirstListener);
                        ImageLoader.getInstance().displayImage(cover_image_url, coverImage, optionsc, animateFirstListener);



                        if(!appPreferences.getString("profile_image_url", "").equals(null)||appPreferences.getString("profile_image_url", "")!=null) {

                            appPreferences.remove("profile_image_url");
                        }
                        if(!appPreferences.getString("cover_image_url", "").equals(null)||appPreferences.getString("cover_image_url", "")!=null) {

                            appPreferences.remove("cover_image_url");
                        }
                        appPreferences.putString("profile_image_url", profile_image_url);
                        appPreferences.putString("cover_image_url",cover_image_url);

                        if(propertyCount>0) {

                            LoggerGeneral.info("pcount11---" + appPreferences.getInt("property_count", 0));

                            propertyicon.setBackgroundResource(R.mipmap.my_property);
                            myproptxt.setTextColor(getResources().getColor(R.color.black));

                        }
                        else
                        {
                            LoggerGeneral.info("pcount22---" + appPreferences.getInt("property_count", 0));

                            propertyicon.setBackgroundResource(R.mipmap.mypropgrey);
                            myproptxt.setTextColor(getResources().getColor(R.color.color_hint));

                        }
                    }


                    }

                    if(account_status == 0) {
                        Intent intent = new Intent(Profile.this,Login.class);
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





    private void selectImage(){



        View view = getLayoutInflater ().inflate (R.layout.alertimage1, null);
        LinearLayout camera = (LinearLayout)view.findViewById( R.id.camera);
        LinearLayout gallery = (LinearLayout)view.findViewById( R.id.gallery);
        LinearLayout cancel = (LinearLayout)view.findViewById( R.id.cancel);

        final Dialog mBottomSheetDialog = new Dialog (Profile.this,
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
                    LoggerGeneral.info("Image uri22"+imageUri.toString());
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
                } else {

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


            if (imageChecker==2) {

                doCrop11(picturePath);
            }else
            {
                doCrop12(picturePath);
            }

        } else if (requestCode == requestCodeForCamera && resultCode == RESULT_OK) {

            LoggerGeneral.info("comek here 1");
            /*File f = new File(Common.getChacheDir(context), "abc.jpg");
            imageUri = Uri.fromFile(f);
            Bitmap newBMP = null;
            newBMP = Common.decodeFile(f);
            Common.saveBitmapToFile(newBMP, f);*/
            //doCrop(imageUri);
            if (imageChecker==2) {

               doCrop(imageUri);
            }else
            {
                doCrop1(imageUri);
            }

        } else if (requestCode == requestCodeForCorp && resultCode == RESULT_OK) {

            LoggerGeneral.info("requestcode for corp ");
            try {
                if (data != null) {
                    LoggerGeneral.info("data != null");
                    Bitmap newBMP = null;
                    Bitmap rotBMP = null;

                    Uri imageUri = data.getData();

                    Toast.makeText(context,"Enter Here",Toast.LENGTH_LONG);
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
                    //coverImage.setImageBitmap(newBMP);


                } else {
                    LoggerGeneral.info("data == null");
                }
            } catch (Exception e)

            {

            }
        }

        else {
            LoggerGeneral.info("failed");
        }

    }

    public void startUploadActivity(Bitmap newBMP) {

        Bitmap resizedBMP = getResizedBitmap(newBMP, 500, 500);


        ByteArrayOutputStream bs = new ByteArrayOutputStream();

		/*FileOutputStream bs;
		try {
			bs =  new FileOutputStream(f);
			resizedBMP.compress(Bitmap.CompressFormat.JPEG,100, bs);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}*/

        resizedBMP.compress(Bitmap.CompressFormat.JPEG, 100, bs);
        mBitmap = BitmapFactory.decodeByteArray(
                bs.toByteArray(), 0,
                bs.toByteArray().length);

        if(imageChecker==2) {

            coverImage.setImageBitmap(newBMP);

         /*   if (coverImage.getDrawable() instanceof BitmapDrawable) {
                coverstrbitmap = ((BitmapDrawable) rphoto.getDrawable()).getBitmap();
            } else {
                Drawable d = coverImage.getDrawable();
                coverstrbitmap = Bitmap.createBitmap(d.getIntrinsicWidth(), d.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(coverstrbitmap);
                d.draw(canvas);
            }
*/

            coverImageChange = getStringImage(newBMP);


         //   appPreferences.putString("storecoverimage",coverImageChange);

            LoggerGeneral.info("bytecover" + coverImageChange);

            if(Common.isOnline(context)) {

                new profileEdit("coverIm", coverImageChange).execute();
            }
            else {
                Common.showToast(context,"No internet connection");
            }

        }
        else
        {


           rphoto.setImageBitmap(newBMP);


          /*  if (rphoto.getDrawable() instanceof BitmapDrawable) {
                prstrbitmap = ((BitmapDrawable) rphoto.getDrawable()).getBitmap();
            } else {
                Drawable d = rphoto.getDrawable();
                prstrbitmap = Bitmap.createBitmap(d.getIntrinsicWidth(), d.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(prstrbitmap);
                d.draw(canvas);
            }
*/

            profileImageChange = getStringImage(newBMP);
            LoggerGeneral.info("byteprofile" + profileImageChange);


        //    appPreferences.putString("storeimage",profileImageChange);

            if(Common.isOnline(context)) {
                new profileEdit("prIm", profileImageChange).execute();
            }
            else {
                Common.showToast(context,"No internet connection");
            }
        }

    }


    class  profileEdit extends AsyncTask<String, String, JSONObject> {
        String type;
        ProgressDialog mDialog;

        String im;

        String c_image,p_image;
        String ch_image;

        public profileEdit(String im,String ch_image){
            this.im =im;
            this.ch_image = ch_image;


        }
        @Override
        protected JSONObject doInBackground(String... params) {
            // TODO Auto-generated method stub

            String url= Constants.Base+Constants.EditProfile;

            JSONObject object = new JSONObject();
            try {

                object.put("name",name);
                object.put("email",email);
                object.put("mobile",mobile);
                object.put("user_id",appPreferences.getString("user_id",""));
                object.put("device_id",device_id);
                object.put("device_type","1");
                object.put("device_token",appPreferences.getString("Token",""));

                if(gender !=null){
                    object.put("gender",gender);
                }
                if(address!=null){
                    object.put("address",address);
                }

                if(landmark!=null){
                    object.put("landmark",landmark);

                }
                if(pincode!=null){
                    object.put("pincode",pincode);
                }
                if(state!=null){
                    object.put("state",state);
                }

                if(city!=null){
                    object.put("city",city);
                }

                if(imageChecker==2){

                    object.put("cover_extension","jpg");
                    object.put("cover_image",ch_image);
                    object.put("profile_extension","jpg");
                    object.put("profile_image", "");

               //     appPreferences.putString("storecoverimage", coverImageChange);
                    LoggerGeneral.info("bytecover2"+coverImageChange+"====="+ch_image);
                }

              else {

             //     appPreferences.putString("storeimage",profileImageChange);
                    object.put("profile_extension","jpeg");
                    object.put("profile_image",ch_image);
                    object.put("cover_extension","jpeg");
                    object.put("cover_image","");


                    LoggerGeneral.info("byteprofile2" + profileImageChange+"===="+ch_image);
                }


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
        /*    transparent.setVisibility(View.VISIBLE);
            rotateLoading.setVisibility(View.VISIBLE);
            rotateLoading.start();
*/
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
            mDialog.dismiss();

            if(results!=null){


                try {
                    JSONObject jsonObject =results.getJSONObject("meta");


                    int  status_code  = jsonObject.getInt("status_code");

                    int account_status = 1;
                    if(jsonObject.has("account_status")){
                        if(!jsonObject.isNull("account_status")){
                            account_status = jsonObject.getInt("account_status");
                        }
                    }

                    if(account_status==1) {
                        if (status_code == 0) {
                            LoggerGeneral.info("coverIm000" + coverImageChange + "=====" + ch_image+"---"+imageChecker);

                            if(im == "coverIm"||im.equals("coverIm")){

                                appPreferences.putString("cover_image_url",cover_image_url);
                                appPreferences.putString("storecoverimage", coverImageChange);
                                appPreferences.putString("storeimagecoverres",coverImageChange);
                                LoggerGeneral.info("coverIm11111" + coverImageChange + "=====" + ch_image + "---" + imageChecker);
                            }

                            else {

                                appPreferences.putString("storeimage", profileImageChange);
                                appPreferences.putString("profile_image_url",profile_image_url);
                                appPreferences.putString("storeimageres",profileImageChange);
                                LoggerGeneral.info("prIm222222" + profileImageChange + "====" + ch_image+"--"+imageChecker);
                            }
                            JSONObject data = results.getJSONObject("data");

                            String profile_upload_message = data.getString("profile_upload_message");

                            String cover_upload_message = data.getString("cover_upload_message");


                            JSONObject data1 = data.getJSONObject("data");


                        /*    LoggerGeneral.info("coverIm000" + coverImageChange + "=====" + ch_image+"---"+imageChecker);
                            if(im == "coverIm"||im.equals("coverIm")){

                                appPreferences.putString("cover_image_url",cover_image_url);
                                appPreferences.putString("storecoverimage", coverImageChange);
                             LoggerGeneral.info("coverIm11111" + coverImageChange + "=====" + ch_image+"---"+imageChecker);
                            }

                            else {

                                appPreferences.putString("storeimage", profileImageChange);
                                appPreferences.putString("profile_image_url",profile_image_url);

                                LoggerGeneral.info("prIm222222" + profileImageChange + "====" + ch_image+"--"+imageChecker);
                            }
*/

                            if (profile_upload_message.equalsIgnoreCase("Image upload Successful.")) {
                                String profile_image_url = data1.getString("profile_image_url");
                            //    appPreferences.putString("profile_image_url",profile_image_url);
                                Common.showToast(context, "Profile Image Uploaded Successfully!");
                            }

                            if (cover_upload_message.equalsIgnoreCase("Image upload Successful.")) {

                                String cover_image_url   = data1.getString("cover_image_url");
                            //    appPreferences.putString("cover_image_url",cover_image_url);
                                Common.showToast(context, "Cover Image Uploaded Successfully!");
                            }


                        }
                    }

                    if(account_status==0){
                        Intent intent = new Intent(Profile.this,Login.class);
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

            }

        }

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

    private void requestPermission(){

        if(permissionChecker==1)
        {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                permissionChecker=0;
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);

            } else {
                permissionChecker=0;
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
            }
        }else {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.CAMERA)) {

                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, PERMISSION_REQUEST_CODE);

            } else {

                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, PERMISSION_REQUEST_CODE);
            }
        }
    }

    private boolean checkPermission3(){
        int result = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED){

            return true;

        } else {

            return false;

        }
    }
/*=========================================================================================================================*/
    private boolean checkPermission2(){
        int result = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA);
        if (result == PackageManager.PERMISSION_GRANTED){

            return true;

        } else {

            return false;

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
            intent.putExtra("crop", true);
            intent.setDataAndType(mImageCaptureUri,"image/*");

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


                alert = builder.create();
                alert.show();
            }
        }
    }

    private void doCrop12(final String mImageCaptureUri) {
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
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            // indicate output X and Y
            cropIntent.putExtra("outputX", 280);
            cropIntent.putExtra("outputY", 280);

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

    private void doCrop11(final String mImageCaptureUri) {
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

    private void doCrop1(final Uri mImageCaptureUri) {
        final ArrayList<CropOption> cropOptions = new ArrayList<CropOption>();

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setType("image/*");

        List<ResolveInfo> list = getPackageManager().queryIntentActivities(intent, 0);

        int size = list.size();

        if (size == 0) {
            Toast.makeText(this, "Can not find image crop app", Toast.LENGTH_SHORT).show();

            return;
        } else {


            intent.putExtra("outputX", 200);
            intent.putExtra("outputY", 200);
            intent.putExtra("aspectX", 2);
            intent.putExtra("aspectY", 2);
            intent.putExtra("scale", true);
            //intent.putExtra("extra_size_limit", 512);
            intent.putExtra("return-data", true);
            intent.putExtra("crop", true);
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
    public static String getStringImage(Bitmap bmp) {
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


    private void loadViewForCode() {
        PullToZoomScrollViewEx scrollView = (PullToZoomScrollViewEx) findViewById(R.id.scroll_view);
        View headView = LayoutInflater.from(this).inflate(R.layout.profile_head_view, null, false);
        View zoomView = LayoutInflater.from(this).inflate(R.layout.profile_zoom_view, null, false);
        View contentView = LayoutInflater.from(this).inflate(R.layout.profile_content_view, null, false);
        scrollView.setHeaderView(headView);
        scrollView.setZoomView(zoomView);
        scrollView.setScrollContentView(contentView);
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


        if( profileAsyncTask != null)
        {
            profileAsyncTask.cancel( true);
            LoggerGeneral.info("async canecelled");
        }
        if(mDrawerLayout.isDrawerOpen(GravityCompat.END))
        {
            mDrawerLayout.closeDrawers();
            return;
        }
        else {

            if(Build.VERSION.SDK_INT >23)
            {
                //super.onBackPressed();
                AlertDialog.Builder ab = new AlertDialog.Builder(Profile.this,R.style.MyAlertDialogStyle);
                ab.setMessage("Are you sure to exit?");
                ab.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                        Intent intentf = new Intent("finish_activity2");
                        context.sendBroadcast(intentf);
                        //if you want to kill app . from other then your main avtivity.(Launcher)
                        android.os.Process.killProcess(android.os.Process.myPid());
                        System.exit(1);
                    /*Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    startActivity(intent);*/


                        //if you want to finish just current activity

                        //Profile.this.finish();
                    }
                });
                ab.setNegativeButton("no", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                ab.show();
            }
            else {

                //super.onBackPressed();
                AlertDialog.Builder ab = new AlertDialog.Builder(Profile.this);
                ab.setMessage("Are you sure to exit?");
                ab.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                        Intent intentf = new Intent("finish_activity2");
                        context.sendBroadcast(intentf);
                        //if you want to kill app . from other then your main avtivity.(Launcher)
                        android.os.Process.killProcess(android.os.Process.myPid());
                        System.exit(1);
                    /*Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    startActivity(intent);*/


                        //if you want to finish just current activity

                        //Profile.this.finish();
                    }
                });
                ab.setNegativeButton("no", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                ab.show();
            }
        }
    }
    private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

        static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

        Bitmap strbitmap,strbitmapcover,bitmap;

        Context context;
        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            if (loadedImage != null) {
                ImageView imageView = (ImageView) view;
                boolean firstDisplay = !displayedImages.contains(imageUri);
                if (firstDisplay) {
                    FadeInBitmapDisplayer.animate(imageView, 500);
                    displayedImages.add(imageUri);
                }




                if (rphoto.getDrawable() instanceof BitmapDrawable) {
                    strbitmap = ((BitmapDrawable) rphoto.getDrawable()).getBitmap();
                } else {
                            Drawable d = rphoto.getDrawable();
                            strbitmap = Bitmap.createBitmap(d.getIntrinsicWidth(), d.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
                            Canvas canvas = new Canvas(bitmap);
                            d.draw(canvas);
                        }
                String storeImage =  getStringImage(strbitmap);


                appPreferences.putString("storeimageres", storeImage);



                if (coverImage.getDrawable() instanceof BitmapDrawable) {
                    strbitmapcover = ((BitmapDrawable) coverImage.getDrawable()).getBitmap();
                } else {
                            Drawable d = coverImage.getDrawable();
                            strbitmapcover = Bitmap.createBitmap(d.getIntrinsicWidth(), d.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
                            Canvas canvas = new Canvas(bitmap);
                            d.draw(canvas);
                        }

                        /*==================================================================*/
                String storeImagecover =  getStringImage(strbitmapcover);
/*==================================================================*/

                appPreferences.putString("storeimagecoverres", storeImagecover);


                LoggerGeneral.info("show1111111--" + appPreferences.getString("storeimageres", "") + "---" + appPreferences.getString("storeimagecoverres", ""));

            }
        }
    }

    class  getNotifications extends AsyncTask<String, String, JSONObject> {
        String type;
        ProgressDialog mDialog;
        @Override
        protected JSONObject doInBackground(String... params) {
            // TODO Auto-generated method stub

            String url= Constants.Base+Constants.getNotification;

            JSONObject object = new JSONObject();
            try {

                //  object.put("society_id",SocietyId);
                //  object.put("user_id",appPreferences.getString("user_id",""));

                // object.put("society_id","19");
                object.put("user_id",appPreferences.getString("user_id",""));
                LoggerGeneral.info("JsonObjectPrintNotification" + object.toString());

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


                    appPreferences.putString("eventResponse",results.toString());
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

                            int length=jsonArray.length();

                            appPreferences.putInt("NotificationCount",length);

                            Constants.notififcationcount=appPreferences.getInt("NotificationCount", 0);

                            Constants.notififcationcount=Constants.notififcationcount-appPreferences.getInt("NotificationOld", 0);

                            if(Constants.notififcationcount<0)
                            {
                                appPreferences.putInt("NotificationOld",0);
                                Constants.notififcationcount=appPreferences.getInt("NotificationCount", 0);

                                Constants.notififcationcount=Constants.notififcationcount-appPreferences.getInt("NotificationOld", 0);
                            }

                            if(Constants.notififcationcount>0) {
                                notificationcount.setVisibility(View.VISIBLE);
                                notificationtext.setText(String.valueOf(Constants.notififcationcount));
                            }
                            else {
                                notificationcount.setVisibility(View.GONE);
                            }

                        }

                        if(status_code==1){


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
                        startActivity(intent);
                        ((Activity)context).finish();
                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mDialog.dismiss();


            }
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

            mDialog = new ProgressDialog(Profile.this,ProgressDialog.THEME_HOLO_DARK);
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
                        Intent register   = new Intent(Profile.this,Login.class);
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



}


