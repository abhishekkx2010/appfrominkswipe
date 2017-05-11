package com.inkswipe.SocialSociety;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.flurry.android.FlurryAgent;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import adapter.CustomAdapter;
import adapter.PropertyAdapter;
import model.DrawerList;
import model.PropertyModel;
import util.AppPreferences;
import util.Common;
import util.Constants;
import util.ExceptionHandler;
import util.LoggerGeneral;
import util.ServiceFacade;

public class MyProperty extends AppCompatActivity {


    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter1;
    Context context;
    PropertyAdapter adapter;
    static List<PropertyModel> properties;

    LinearLayout home;
    Fragment fragment = null;
    TextView appname;
    ExpandableListView expListView;


    public static Bundle temp_bundle;

    LinearLayout prpublish;
    LinearLayout search;
    TextView title;
    EditText searchSociety;

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    public boolean chechSearchVisiblity=false;

    RelativeLayout notification;

    AppPreferences appPreferences;
    PropertyModel propertyModel;


    String id;
    String SocietyId;
    String userId;
    String property_name;
    String property_type;
    String user_type;
    String house_no;
    String is_available_for_rent;

    String rent_availability_date;

    String property_image;
    String status;
    String createdOn;
    List<PropertyModel>p_properties;
    String society_name,society_city;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        setContentView(R.layout.activity_my_property);

        context =MyProperty.this;
        Common.internet_check=2;
        appPreferences=AppPreferences.getAppPreferences(context);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        title=((TextView) toolbar.findViewById(R.id.toolbar_title));
        title.setText("My Property");
        toolbar.setTitleTextColor(android.graphics.Color.WHITE);

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
        ImageLoader.getInstance().init(config);

        p_properties = new ArrayList<PropertyModel>();


        search= (LinearLayout) toolbar.findViewById(R.id.searchSociety);
        searchSociety= (EditText) toolbar.findViewById(R.id.search);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title.setVisibility(View.GONE);
                searchSociety.setVisibility(View.VISIBLE);
                notification.setVisibility(View.GONE);
                search.setVisibility(View.GONE);
                home.setVisibility(View.GONE);
                chechSearchVisiblity = true;
                searchSociety.addTextChangedListener(new TextWatcher() {

                    public void afterTextChanged(Editable s) {
                    }

                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    public void onTextChanged(CharSequence query, int start, int before, int count) {

                        query = query.toString().toLowerCase();

                        final List<PropertyModel> filteredList = new ArrayList<PropertyModel>();

                        for (int i = 0; i < properties.size(); i++) {

                            final String text = properties.get(i).getProperty_name().toLowerCase();
                            if (text.contains(query)) {

                                filteredList.add(properties.get(i));
                            }
                        }

                        recyclerView.setLayoutManager(new LinearLayoutManager(context));
                        adapter = new PropertyAdapter(context, filteredList);
                        recyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();  // data set changed
                    }
                });

            }
        });


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.my_statusbar_color));
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

    //    toolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.backarrow));


        toolbar.getNavigationIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //What to do on back clicked
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    if(mDrawerLayout.isDrawerOpen(GravityCompat.END))
                    {
                        mDrawerLayout.closeDrawers();
                    }
                    else if(chechSearchVisiblity==true)
                    {

                        searchSociety.setVisibility(View.GONE);
                        searchSociety.setText("");
                        title.setVisibility(View.VISIBLE);
                        home.setVisibility(View.VISIBLE);
                        notification.setVisibility(View.VISIBLE);
                        search.setVisibility(View.VISIBLE);
                        chechSearchVisiblity=false;
                        adapter1 = new PropertyAdapter(context, properties);

                        recyclerView.setAdapter(adapter1);

                        adapter1.notifyDataSetChanged();


                    }
                    else {

                        Intent back = new Intent(MyProperty.this, Profile.class);
                        back.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(back);
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
                Intent notification=new Intent(MyProperty.this,NotificationApp.class);
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
                Intent myProperty=new Intent(MyProperty.this,MyProperty.class);
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
                        Intent profile = new Intent(MyProperty.this, Profile.class);
                        profile.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(profile);
                        Common.internet_check=0;
                        finish();
                        mDrawerLayout.closeDrawers();
                        break;
                    case 2:
                        LoggerGeneral.info("2");
                        Intent addSociety = new Intent(MyProperty.this, AddSociety.class);
                        Common.internet_check=0;
                        startActivity(addSociety);
                        finish();
                        break;
                    case 3:
                        LoggerGeneral.info("3");
                        PropertyAdapter.intentCheck = 0;
                        Intent myProperty=new Intent(MyProperty.this,MyProperty.class);
                        startActivity(myProperty);
                        finish();

                        break;
                    case 4:
                        LoggerGeneral.info("4");
                        /*Intent events=new Intent(MyProperty.this,MyProperty.class);
                        events.putExtra("event",3);
                        startActivity(events);
                        finish();*/
                        mDrawerLayout.closeDrawers();
                        Common.showToast(context, "Please select property first");
                        PropertyAdapter.intentCheck = 4;
                        break;
                    case 5:
                        LoggerGeneral.info("5");
                        /*Intent createEvents=new Intent(MyProperty.this,MyProperty.class);
                        startActivity(createEvents);
                        finish();*/
                        mDrawerLayout.closeDrawers();
                        Common.showToast(context, "Please select property first");
                        PropertyAdapter.intentCheck = 5;
                        break;
                    case 6:
                        LoggerGeneral.info("6");
                        /*Intent archivedEvents=new Intent(MyProperty.this,MyProperty.class);
                        startActivity(archivedEvents);
                        finish();*/
                        mDrawerLayout.closeDrawers();
                        Common.showToast(context, "Please select property first");
                        PropertyAdapter.intentCheck = 6;
                        break;
                    case 7:
                        LoggerGeneral.info("7");
                        /*Intent announcement=new Intent(MyProperty.this,MyProperty.class);
                        announcement.putExtra("announcement",4);
                        startActivity(announcement);
                        finish();*/
                        mDrawerLayout.closeDrawers();
                        Common.showToast(context, "Please select property first");
                        PropertyAdapter.intentCheck = 7;
                        break;
                    case 8:
                        LoggerGeneral.info("8");
                        /*Intent createAnnouncement=new Intent(MyProperty.this,MyProperty.class);
                        startActivity(createAnnouncement);
                        finish();*/
                        mDrawerLayout.closeDrawers();
                        Common.showToast(context, "Please select property first");
                        PropertyAdapter.intentCheck = 8;
                        break;
                    case 9:
                        LoggerGeneral.info("9");
                        /*Intent polls=new Intent(MyProperty.this,MyProperty.class);
                        polls.putExtra("polls", 2);
                        startActivity(polls);
                        finish();*/
                        mDrawerLayout.closeDrawers();
                        Common.showToast(context, "Please select property first");
                        PropertyAdapter.intentCheck = 9;
                        break;
                    case 10:
                        LoggerGeneral.info("10");
                       /* Intent createEventPoll=new Intent(MyProperty.this,MyProperty.class);
                        startActivity(createEventPoll);
                        finish();*/
                        mDrawerLayout.closeDrawers();
                        Common.showToast(context, "Please select property first");
                        PropertyAdapter.intentCheck = 10;
                        break;
                    case 11:
                        LoggerGeneral.info("11");
                        /*Intent archivedEventPoll=new Intent(MyProperty.this,MyProperty.class);
                        startActivity(archivedEventPoll);
                        finish();*/
                        mDrawerLayout.closeDrawers();
                        Common.showToast(context, "Please select property first");
                        PropertyAdapter.intentCheck = 11;
                        break;
                    case 12:
                        LoggerGeneral.info("12");
                        /*Intent members=new Intent(MyProperty.this,MyProperty.class);
                        startActivity(members);
                        finish();*/
                        mDrawerLayout.closeDrawers();
                        Common.showToast(context, "Please select property first");
                        PropertyAdapter.intentCheck = 12;
                        break;
                    case 13:
                        LoggerGeneral.info("13");
                        /*Intent offers=new Intent(MyProperty.this,MyProperty.class);
                        startActivity(offers);
                        finish();*/
                        mDrawerLayout.closeDrawers();
                        Common.showToast(context, "Please select property first");
                        PropertyAdapter.intentCheck = 13;
                        break;
                    case 14:
                        LoggerGeneral.info("14");
                        /*Intent complaint=new Intent(MyProperty.this,MyProperty.class);
                        startActivity(complaint);
                        finish();*/
                        mDrawerLayout.closeDrawers();
                        Common.showToast(context, "Please select property first");
                        PropertyAdapter.intentCheck = 14;
                        break;
                    case 15:
                        LoggerGeneral.info("15");
                        Intent notification=new Intent(MyProperty.this,NotificationApp.class);
                        //    notification.putExtra("SocietyId",Soci)
                        int test1=appPreferences.getInt("NotificationOld",0);
                        test1=test1+Constants.notififcationcount;
                        appPreferences.putInt("NotificationOld",test1);
                        notification.putExtra("SocietyId", SocietyId);
                        Constants.notififcationcount=0;
                        Common.internet_check=0;
                        startActivity(notification);
                        finish();
                        break;
                    case 16:

                     /*   Intent intent = new Intent(MyProperty.this, Login.class);
                        appPreferences.remove("user_id");
                        appPreferences.remove("user_name");
                        appPreferences.remove("email_id");
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


        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.addItemDecoration(new VerticalLineDecorator(2));
        properties = new ArrayList<PropertyModel>();


        prpublish = (LinearLayout)findViewById(R.id.prpublish);
        prpublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addsociety = new Intent(MyProperty.this, SearchSociety.class);
                Common.internet_check=0;
                startActivity(addsociety);
                finish();
            }
        });

/*

        for(int i = 0;i<=20;i++){
            propertyModel = new PropertyModel();
            propertyModel.setName("Rajeshwar nagar");
            propertyModel.setId(i);
            properties.add(propertyModel);
//            societyModel.setSocimg(String.valueOf(R.mipmap.background_material));
            LoggerGeneral.info("showsoc" + propertyModel.getName() + "---" + properties.get(i).getName());


        }
*/






        if(Common.isOnline(context)){
            properties = new ArrayList<PropertyModel>();
            new getMyprop().execute();

        }
        else {
                Common.showToast(context,"No internet connection");
        }

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

            mDialog = new ProgressDialog(MyProperty.this,ProgressDialog.THEME_HOLO_DARK);
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
                        Intent register   = new Intent(MyProperty.this,Login.class);
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
    class  getMyprop extends AsyncTask<String, String, JSONObject> {
        String type;
        ProgressDialog mDialog;
        @Override
        protected JSONObject doInBackground(String... params) {
            // TODO Auto-generated method stub

            String url= Constants.Base+Constants.GetProperty;

            JSONObject object = new JSONObject();
            try {

               // object.put("user_id",appPreferences.getString("user_id",""));
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
            mDialog = new ProgressDialog(MyProperty.this,ProgressDialog.THEME_HOLO_DARK);
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

                            JSONObject data = results.getJSONObject("data");


                            JSONArray jsonArray = data.getJSONArray("data");

                            LoggerGeneral.info("showjarray---" + jsonArray);
                            properties = new ArrayList<PropertyModel>();
                            if(properties!=null){
                                properties.clear();
                            }
                            for (int i = jsonArray.length() - 1; i >=0; i--) {

                                LoggerGeneral.info("showjarray2---" + jsonArray);

                                propertyModel = new PropertyModel();

                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);


                                id = jsonObject1.getString("id");
                                SocietyId = jsonObject1.getString("society_id");
                                userId = jsonObject1.getString("user_id");
                                property_name = jsonObject1.getString("property_name");
                                property_type = jsonObject1.getString("property_type");
                                user_type = jsonObject1.getString("user_type");
                                house_no = jsonObject1.getString("house_no");
                                is_available_for_rent = jsonObject1.getString("is_available_for_rent");
                                rent_availability_date = jsonObject1.getString("rent_availability_date");
                                property_image = jsonObject1.getString("property_image_url");
                                status = jsonObject1.getString("status");
                                createdOn  =jsonObject1.getString("created_on");
                                society_city = jsonObject1.getString("society_city");
                                society_name = jsonObject1.getString("society_name");



                                propertyModel.setIdd(i);
                                propertyModel.setId(id);
                                propertyModel.setSocietyId(SocietyId);
                                propertyModel.setProperty_name(property_name);
                                propertyModel.setProperty_type(property_type);
                                propertyModel.setUser_type(user_type);
                                propertyModel.setHouse_no(house_no);
                                propertyModel.setIs_available_for_rent(is_available_for_rent);
                                propertyModel.setRent_availability_date(rent_availability_date);
                                propertyModel.setProperty_type(property_type);
                                propertyModel.setProperty_image(property_image);
                                propertyModel.setStatus(status);
                                propertyModel.setCreated_on(createdOn);
                                propertyModel.setSocietyId(SocietyId);
                                propertyModel.setSocietyName(society_name);
                                propertyModel.setCityName(society_city);


                                p_properties.add(propertyModel);


                                properties.add(propertyModel);


                                LoggerGeneral.info("s_societies000---" + p_properties);

                            }


                            LoggerGeneral.info("s_societies---" + p_properties);
                            adapter1 = new PropertyAdapter(context, properties);

                            recyclerView.setAdapter(adapter1);

                            adapter1.notifyDataSetChanged();


                        }

                        if (status_code == 1) {






                        }
                    }

                    if(account_status==0){
                        Intent intent = new Intent(MyProperty.this,Login.class);
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
    public static void removeItem(int id)
    {
        LoggerGeneral.info("pass name" + id);
        for(int i=0;i<properties.size();i++)
        {
            PropertyModel propertyModel=properties.get(i);
            int remove=propertyModel.getIdd();
            LoggerGeneral.info("passed name"+remove);
            if(id==remove)
            {
                properties.remove(i);
                break;
            }
        }
        LoggerGeneral.info("hiii" + properties.size());
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
        else if(chechSearchVisiblity==true)
        {

            searchSociety.setVisibility(View.GONE);
            searchSociety.setText("");
            title.setVisibility(View.VISIBLE);
            home.setVisibility(View.VISIBLE);
            notification.setVisibility(View.VISIBLE);
            search.setVisibility(View.VISIBLE);
            chechSearchVisiblity=false;
            adapter1 = new PropertyAdapter(this, properties);

            recyclerView.setAdapter(adapter1);

            adapter1.notifyDataSetChanged();

            return;
        }
        else {

            super.onBackPressed();
            Intent back = new Intent(MyProperty.this, Profile.class);
            back.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            Common.internet_check=0;
            startActivity(back);
            finish();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current game state

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
