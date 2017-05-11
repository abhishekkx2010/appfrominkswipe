package com.inkswipe.SocialSociety;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
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
import adapter.PropertyAdapter;
import model.DrawerList;
import model.SocietyModel;
import util.AppPreferences;
import util.Common;
import util.Constants;
import util.ExceptionHandler;
import util.LoggerGeneral;
import util.ServiceFacade;

public class SearchSociety extends AppCompatActivity {

    LinearLayout home;
    Fragment fragment = null;
    TextView appname;
    LinearLayout searchsoc;

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;

    RelativeLayout notification;

    AppPreferences appPreferences;
    Context context;
    MyEditTextView socname,city;
    String societyname,cityname;
    Dialog dialog;

    public  List<SocietyModel> s_societies;
    SocietyModel societyModel;

            /* "address": "vikhroli west",
            "landmark": "near railway station",
            "state": "maharashtra",
            "city": "mumbai",
            "pincode": "400079",
            "post": "1",
            "post_name": "member",
            "society_image": "761715074a6867853d9b13b7902ae249.jpg",
            "user_id": null,
            "status": "-1",
            "date_created": "2016-10-04 13:46:31",
            "date_modified": "2016-10-05 11:47:55",
            "created_by": "Ajinkya",
            "society_image_url": "http://projects.inkswipe.in/social-society/uat/upload/images/society_images/761715074a6867853d9b13b7902ae249.jpg"
*/

    String id,name,address,landmark,s_state,s_city,pincode,post,post_name,society_image,user_id,status,date_created,date_modified,created_by,society_image_url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        setContentView(R.layout.activity_search_society);
        context = SearchSociety.this;
        appPreferences  =AppPreferences.getAppPreferences(context);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView title=((TextView) toolbar.findViewById(R.id.toolbar_title));
        title.setText("Search Society");

        toolbar.setTitleTextColor(android.graphics.Color.WHITE);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

     //   toolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.backarrow));


        toolbar.getNavigationIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //What to do on back clicked
                Intent intent = new Intent(SearchSociety.this, Profile.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
                LoggerGeneral.info("Backgo"+intent);
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
                Intent notification=new Intent(SearchSociety.this,NotificationApp.class);
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
                Intent myProperty=new Intent(SearchSociety.this,MyProperty.class);
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


        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // make ToastCustom when click
                /*ToastCustom.makeText(getApplicationContext(), "Position " + position, ToastCustom.LENGTH_LONG).show();
                */

                switch (position) {

                    case 1:
                        LoggerGeneral.info("1");
                        Intent profile=new Intent(SearchSociety.this,Profile.class);
                        profile.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(profile);
                        finish();
                        mDrawerLayout.closeDrawers();
                        break;
                    case 2:
                        LoggerGeneral.info("2");
                        Intent addSociety=new Intent(SearchSociety.this,AddSociety.class);
                        startActivity(addSociety);
                        finish();
                        break;
                    case 3:
                        LoggerGeneral.info("3");
                        if(appPreferences.getInt("property_count",0)>0) {
                        Intent myProperty=new Intent(SearchSociety.this,MyProperty.class);
                        PropertyAdapter.intentCheck=0;
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
                        Intent events=new Intent(SearchSociety.this,MyProperty.class);
                        Common.showToast(context, "Please select property first");
                        PropertyAdapter.intentCheck=4;
                        events.putExtra("event",3);
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
                        Intent createEvents=new Intent(SearchSociety.this,MyProperty.class);
                        Common.showToast(context, "Please select property first");
                        PropertyAdapter.intentCheck=5;
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
                        Intent archivedEvents=new Intent(SearchSociety.this,MyProperty.class);
                        Common.showToast(context, "Please select property first");
                        PropertyAdapter.intentCheck=6;
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
                        Intent announcement=new Intent(SearchSociety.this,MyProperty.class);
                        Common.showToast(context, "Please select property first");
                        PropertyAdapter.intentCheck=7;
                        announcement.putExtra("announcement",4);
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
                        Intent createAnnouncement=new Intent(SearchSociety.this,MyProperty.class);
                        Common.showToast(context, "Please select property first");
                        PropertyAdapter.intentCheck=8;
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
                        Intent polls=new Intent(SearchSociety.this,MyProperty.class);
                        Common.showToast(context, "Please select property first");
                        PropertyAdapter.intentCheck=9;
                        polls.putExtra("polls",2);
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
                        Intent createEventPoll=new Intent(SearchSociety.this,MyProperty.class);
                        Common.showToast(context, "Please select property first");
                        PropertyAdapter.intentCheck=10;
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
                        Intent archivedEventPoll=new Intent(SearchSociety.this,MyProperty.class);
                        Common.showToast(context, "Please select property first");
                        PropertyAdapter.intentCheck=11;
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
                        Intent members=new Intent(SearchSociety.this,MyProperty.class);
                        Common.showToast(context, "Please select property first");
                        PropertyAdapter.intentCheck=12;
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
                        Intent offers=new Intent(SearchSociety.this,MyProperty.class);
                        Common.showToast(context, "Please select property first");
                        PropertyAdapter.intentCheck=13;
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
                        Intent complaint=new Intent(SearchSociety.this,MyProperty.class);
                        Common.showToast(context, "Please select property first");
                        PropertyAdapter.intentCheck=14;
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
                        Intent notification=new Intent(SearchSociety.this,NotificationApp.class);
                        //    notification.putExtra("SocietyId",Soci)
                        int test1=appPreferences.getInt("NotificationOld",0);
                        test1=test1+Constants.notififcationcount;
                        appPreferences.putInt("NotificationOld",test1);
                        Constants.notififcationcount=0;
                        startActivity(notification);
                        finish();
                        break;
                    case 16:

                        /*Intent intent = new Intent(SearchSociety.this,Login.class);
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

        socname = (MyEditTextView)findViewById(R.id.socname);
        city = (MyEditTextView)findViewById(R.id.city);




        s_societies = new ArrayList<SocietyModel>();
        searchsoc  = (LinearLayout)findViewById(R.id.searchsoc);
        searchsoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                societyname = socname.getText().toString().trim();
                cityname  = city.getText().toString().trim();


                if(societyname!=null && societyname.length()>0 ) {

                    if(city!=null && city.length()>0) {
                        if(!cityname.matches("[0]+")){
                            if(cityname.length()==6) {
                                if (Common.isOnline(context)) {
                                    new searchSociety().execute();
                                } else {
                                    Common.showToast(context, "No internet connection!");
                                }
                            }
                            else {
                                Common.showToast(context,"Invalid pincode");
                            }
                        }
                    else {
                            Common.showToast(context,"Invalid pincode");}
                    }
                    else {
                        Common.showToast(context,"Enter pincode");
                    }
                }
                else {
                    Common.showToast(context,"Enter society name");
                }

            /*  Intent searchsoc = new Intent(SearchSociety.this, MySociety.class);
                startActivity(searchsoc);
                finish();*/
            }
        });

    }
/*=====================================================================================================*/
    @Override
    public void onBackPressed() {
        if(mDrawerLayout.isDrawerOpen(GravityCompat.END))
        {
            mDrawerLayout.closeDrawers();
            return;
        }
        else {

            super.onBackPressed();
            Intent back = new Intent(SearchSociety.this, Profile.class);
            back.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(back);
            finish();
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

            mDialog = new ProgressDialog(SearchSociety.this,ProgressDialog.THEME_HOLO_DARK);
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
                        Intent register   = new Intent(SearchSociety.this,Login.class);
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


    class  searchSociety extends AsyncTask<String, String, JSONObject> {
        String type;
        ProgressDialog mDialog;
        @Override
        protected JSONObject doInBackground(String... params) {
            // TODO Auto-generated method stub

            String url= Constants.Base+Constants.SearchSociety;

            JSONObject object = new JSONObject();
            try {

                object.put("pincode",cityname);
                object.put("society_name",societyname);
                object.put("user_id",appPreferences.getString("user_id",""));


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
        /*    transparent.setVisibility(View.VISIBLE);
            rotateLoading.setVisibility(View.VISIBLE);
            rotateLoading.start();
*/
            mDialog = new ProgressDialog(SearchSociety.this,ProgressDialog.THEME_HOLO_DARK);
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

                        JSONArray jsonArray = results.getJSONArray("data");


                        LoggerGeneral.info("showjarray---" + jsonArray);

                        for (int i = 0; i <= jsonArray.length() - 1; i++) {

                            LoggerGeneral.info("showjarray2---" + jsonArray);

                            societyModel = new SocietyModel();

                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);


                            id = jsonObject1.getString("id");
                            name = jsonObject1.getString("name");
                            address = jsonObject1.getString("address");
                            landmark = jsonObject1.getString("landmark");
                            s_state = jsonObject1.getString("state");
                            s_city = jsonObject1.getString("city");
                            pincode = jsonObject1.getString("pincode");
                            post = jsonObject1.getString("post");
                            post_name = jsonObject1.getString("post_name");
                            society_image = jsonObject1.getString("society_image");
                            user_id = jsonObject1.getString("user_id");
                            status = jsonObject1.getString("status");
                            date_created = jsonObject1.getString("date_created");
                            date_modified = jsonObject1.getString("date_modified");
                            created_by = jsonObject1.getString("created_by");
                            society_image_url = jsonObject1.getString("society_image_url");


                            societyModel.setIdd(i);
                            societyModel.setId(id);
                            societyModel.setName(name);
                            societyModel.setAddress(address);
                            societyModel.setLandmark(landmark);
                            societyModel.setState(s_state);
                            societyModel.setCity(s_city);
                            societyModel.setPincode(pincode);
                            societyModel.setPost(post);
                            societyModel.setPost_name(post_name);
                            societyModel.setSociety_image(society_image);
                            societyModel.setUser_id(user_id);
                            societyModel.setStatus(status);
                            societyModel.setDate_created(date_created);
                            societyModel.setDate_modified(date_modified);
                            societyModel.setCreated_by(created_by);
                            societyModel.setSociety_image_url(society_image_url);


                            s_societies.add(societyModel);


                            LoggerGeneral.info("s_societies000---" + s_societies);

                        }


                        LoggerGeneral.info("s_societies---" + s_societies);
                        Intent intent = new Intent(SearchSociety.this, MySociety.class);

                        intent.putExtra("societylist", (new DataWrapper((ArrayList<SocietyModel>) s_societies)));
                        startActivity(intent);
                        finish();


                    }

                    if (status_code == 1) {
                        dialog = new Dialog(SearchSociety.this);
                        dialog.setContentView(R.layout.nosocpopup);
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

                        LinearLayout verify = (LinearLayout) dialog.findViewById(R.id.verifyb);
                        verify.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(SearchSociety.this, AddSociety.class);
                                intent.putExtra("fromsearch","fromsearch");
                                startActivity(intent);
                                finish();
                            }
                        });

                        dialog.show();


                    }
                }

                    if(account_status==0){
                        Intent intent = new Intent(SearchSociety.this,Login.class);
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
}
