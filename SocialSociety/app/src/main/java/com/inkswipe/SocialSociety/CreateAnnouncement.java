package com.inkswipe.SocialSociety;

import android.app.AlertDialog;
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
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
import java.util.ArrayList;
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

public class CreateAnnouncement extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    LinearLayout home;
    Fragment fragment = null;
    TextView appname;
    private static final int PERMISSION_REQUEST_CODE = 1;
    public static int permissionChecker=0;
    Uri imageUri;
    static final int requestCodeForSdCard = 1, requestCodeForCamera = 2, requestCodeForCorp = 3;
    RelativeLayout notification;
            LinearLayout loginRegister;

    static int imageChecker=0;

    EditText edtitle,description;
    String c_title,c_description;
    Context context;

    TextView  uploadPhoto;
    String announceimage;

    String SocietyId;
    LinearLayout borderannouncementcreate;

    AppPreferences appPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        setContentView(R.layout.activity_create_announcement);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        context = CreateAnnouncement.this;
        appPreferences = AppPreferences.getAppPreferences(context);
        TextView title=((TextView) toolbar.findViewById(R.id.toolbar_title));
        title.setText("Create Announcement");
        toolbar.setTitleTextColor(android.graphics.Color.WHITE);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

     //   toolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.backarrow));

        SocietyId = getIntent().getStringExtra("SocietyId");

        LoggerGeneral.info("Societyid---"+SocietyId);

        toolbar.getNavigationIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //What to do on back clicked
                Intent intent = new Intent(CreateAnnouncement.this, DashBoard.class);
                intent.putExtra("SocietyId",SocietyId);
                intent.putExtra("announcement",4);
                startActivity(intent);
                finish();
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
                Intent notification=new Intent(CreateAnnouncement.this,NotificationApp.class);
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
                Intent myProperty=new Intent(CreateAnnouncement.this,MyProperty.class);
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
                        Intent profile = new Intent(CreateAnnouncement.this, Profile.class);
                        profile.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(profile);
                        finish();
                        break;
                    case 2:
                        LoggerGeneral.info("2");
                        Intent addSociety = new Intent(CreateAnnouncement.this, AddSociety.class);
                        startActivity(addSociety);
                        finish();
                        break;
                    case 3:
                        LoggerGeneral.info("3");
                        Intent myProperty = new Intent(CreateAnnouncement.this, MyProperty.class);
                        PropertyAdapter.intentCheck = 0;
                        startActivity(myProperty);
                        finish();
                        break;
                    case 4:
                        LoggerGeneral.info("4");
                        Intent events = new Intent(CreateAnnouncement.this, DashBoard.class);
                        events.putExtra("event", 3);
                        events.putExtra("SocietyId", SocietyId);
                        startActivity(events);
                        finish();
                        break;
                    case 5:
                        LoggerGeneral.info("5");
                        Intent createEvents = new Intent(CreateAnnouncement.this, CreateEvent.class);
                        createEvents.putExtra("SocietyId", SocietyId);
                        startActivity(createEvents);
                        finish();
                        break;
                    case 6:
                        LoggerGeneral.info("6");
                        Intent archivedEvents = new Intent(CreateAnnouncement.this, Archivedevent.class);
                        archivedEvents.putExtra("SocietyId", SocietyId);
                        startActivity(archivedEvents);
                        finish();
                        break;
                    case 7:
                        LoggerGeneral.info("7");
                        Intent announcement = new Intent(CreateAnnouncement.this, DashBoard.class);
                        announcement.putExtra("SocietyId", SocietyId);
                        announcement.putExtra("announcement", 4);
                        startActivity(announcement);
                        finish();
                        break;
                    case 8:
                        LoggerGeneral.info("8");
                       /* Intent createAnnouncement=new Intent(Profile.this,CreateAnnouncement.class);
                        startActivity(createAnnouncement);
                        finish();*/
                        mDrawerLayout.closeDrawers();
                        break;
                    case 9:
                        LoggerGeneral.info("9");
                        Intent polls = new Intent(CreateAnnouncement.this, DashBoard.class);
                        polls.putExtra("SocietyId", SocietyId);
                        polls.putExtra("polls", 2);
                        startActivity(polls);
                        finish();
                        break;
                    case 10:
                        LoggerGeneral.info("10");
                        Intent createEventPoll = new Intent(CreateAnnouncement.this, CreatePoll.class);
                        createEventPoll.putExtra("SocietyId", SocietyId);
                        startActivity(createEventPoll);
                        finish();
                        break;
                    case 11:
                        LoggerGeneral.info("11");
                        Intent archivedEventPoll = new Intent(CreateAnnouncement.this, ArchivedPolls.class);
                        archivedEventPoll.putExtra("SocietyId", SocietyId);
                        startActivity(archivedEventPoll);
                        finish();
                        break;
                    case 12:
                        LoggerGeneral.info("12");
                        Intent members = new Intent(CreateAnnouncement.this, Members.class);
                        members.putExtra("SocietyId", SocietyId);
                        startActivity(members);
                        finish();
                        break;
                    case 13:
                        LoggerGeneral.info("13");
                        Intent offers = new Intent(CreateAnnouncement.this, Offer.class);
                        offers.putExtra("SocietyId", SocietyId);
                        startActivity(offers);
                        finish();
                        break;
                    case 14:
                        LoggerGeneral.info("14");
                        Intent complaint = new Intent(CreateAnnouncement.this, Complaint.class);
                        complaint.putExtra("SocietyId", SocietyId);
                        startActivity(complaint);
                        finish();
                        break;
                    case 15:
                        LoggerGeneral.info("15");
                        Intent notification=new Intent(CreateAnnouncement.this,NotificationApp.class);
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
                        /*LoggerGeneral.info("16");
                        Intent intent = new Intent(CreateAnnouncement.this, Login.class);
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



        edtitle = (EditText)findViewById(R.id.title);

        description = (EditText)findViewById(R.id.description);

        borderannouncementcreate= (LinearLayout) findViewById(R.id.borderannouncementcreate);

        borderannouncementcreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                description.requestFocus();

                final InputMethodManager inputMethodManager =
                        (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);

                inputMethodManager.showSoftInput(description, InputMethodManager.SHOW_FORCED);
            }
        });

        loginRegister  =(LinearLayout)findViewById(R.id.loginRegister);
        loginRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                c_title = edtitle.getText().toString().trim();

                c_description = description.getText().toString().trim();


                if(c_title!=null && c_title.length()>0){

                    if(c_description!=null && c_description.length()>0){


                      //  if(announceimage!=null){

                            if(Common.isOnline(context)) {

                                new createAnnouncment().execute();
                            }
                            else {
                                Common.showToast(context,"No internet connection");
                            }
                    //    }
                    /*    else {
                            Common.showToast(context,"Select Image");

                        }*/

                    }
                    else {
                        Common.showToast(context,"Enter announcement description");
                    }



                }
                else {
                    Common.showToast(context,"Enter announcement title");
                }




            }
        });

        uploadPhoto = (TextView)findViewById(R.id.uploadPhoto);

        uploadPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            //    In

                imageChecker = 2;

                if (Build.VERSION.SDK_INT >= 23) {
                    // Do some stuff


                    if (!checkPermission2()) {

                        requestPermission();

                    } else {
                        imageChecker = 2;
                        selectImage();
                    }
                } else {
                    imageChecker = 2;
                    selectImage();
                }


            }
        });

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

            mDialog = new ProgressDialog(CreateAnnouncement.this,ProgressDialog.THEME_HOLO_DARK);
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
                        Intent register   = new Intent(CreateAnnouncement.this,Login.class);
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


    class  createAnnouncment extends AsyncTask<String, String, JSONObject> {
        String type;
        ProgressDialog mDialog;
        @Override
        protected JSONObject doInBackground(String... params) {
            // TODO Auto-generated method stub

            String url= Constants.Base+Constants.RegisterAnnouncement;

            JSONObject object = new JSONObject();
            try {

                object.put("society_id",SocietyId);
                object.put("announcement_title",c_title);
                object.put("description",c_description);
                object.put("user_id",appPreferences.getString("user_id",""));
                object.put("announcement_image",announceimage);
                object.put("image_extension","jpg");


          //      object.put("property_id",property_id);


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

            mDialog = new ProgressDialog(CreateAnnouncement.this,ProgressDialog.THEME_HOLO_DARK);
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

                            Intent announcement = new Intent(CreateAnnouncement.this, DashBoard.class);
                            announcement.putExtra("announcement", 4);
                            announcement.putExtra("SocietyId",SocietyId);
                            startActivity(announcement);
                            finish();
                         /* JSONObject data = results.getJSONObject("data");

                            String property_id = data.getString("property_id");

                            appPreferences.putString("property_id",property_id);*/

                        }

                    }

                    if(account_status == 0) {
                        Intent intent = new Intent(CreateAnnouncement.this,Login.class);
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
    public void onBackPressed() {
        if(mDrawerLayout.isDrawerOpen(GravityCompat.END))
        {
            mDrawerLayout.closeDrawers();
            return;
        }
        else {
            super.onBackPressed();
            Intent back = new Intent(CreateAnnouncement.this, DashBoard.class);
            back.putExtra("SocietyId",SocietyId);
            back.putExtra("announcement",4);
            startActivity(back);
            finish();
        }
    }
    private void selectImage() {


        View view = getLayoutInflater().inflate(R.layout.alertimage1, null);
        LinearLayout camera = (LinearLayout) view.findViewById(R.id.camera);
        LinearLayout gallery = (LinearLayout) view.findViewById(R.id.gallery);
        LinearLayout cancel = (LinearLayout) view.findViewById(R.id.cancel);

        final Dialog mBottomSheetDialog = new Dialog(CreateAnnouncement.this,
                R.style.MaterialDialogSheet);
        mBottomSheetDialog.setContentView(view);
        mBottomSheetDialog.setCancelable(true);
        mBottomSheetDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mBottomSheetDialog.getWindow().setGravity(Gravity.BOTTOM);
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



    private boolean checkPermission2(){
        int result = ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA);
        if (result == PackageManager.PERMISSION_GRANTED){

            return true;

        } else {

            return false;

        }
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
        int result = ContextCompat.checkSelfPermission(context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED){

            return true;

        } else {

            return false;

        }
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

        announceimage=getStringImage(newBMP);

        Bitmap resizedBMP = getResizedBitmap(newBMP, 500,500);


        ByteArrayOutputStream bs = new ByteArrayOutputStream();


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

}
