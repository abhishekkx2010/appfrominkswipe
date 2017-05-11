package com.inkswipe.SocialSociety;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.flurry.android.FlurryAgent;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import adapter.CustomAdapter;
import adapter.PropertyAdapter;
import adapter.SocietyAdapter;
import model.DrawerList;
import model.SocietyModel;
import util.AppPreferences;
import util.Common;
import util.Constants;
import util.ExceptionHandler;
import util.LoggerGeneral;
import util.ServiceFacade;

public class MySociety extends AppCompatActivity {


    //UA-75773087-3 UA-75773087-3

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter1;
    Context context;
    SocietyAdapter adapter;
 //   public static List<SocietyModel> societies;
    EditText searchSociety;
    int textlength=0;
    private String TitleName[] = {"IT park","Rajesh Nagar"," Sai", "Varsha nagar","Someshwar","Guru krupa","Patrakar","MBP","Satkar","Vishwadeep","Shivprabhat","Raje","Antila","Anandgad","Jannat","Maharashtra Nagar","Kokan","Satya Nagar","Pratiskha Nagar","Amrut Nagar"};

    SocietyModel societyModel;
    LinearLayout prpublish;
    LinearLayout search;
    TextView title;
    LinearLayout home;
    RelativeLayout notification;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;

    public boolean chechSearchVisiblity=false;
    private ArrayList<String> array_sort;
    AppPreferences appPreferences;
    public static ArrayList<SocietyModel> societies;
    DataWrapper dw;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        setContentView(R.layout.activity_my_society);
        context =MySociety.this;
        appPreferences =AppPreferences.getAppPreferences(context);
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
        ImageLoader.getInstance().init(config);

        dw = (DataWrapper)getIntent().getSerializableExtra("societylist");
        if(societies!=null){
            societies.clear();
        }

        societies =dw.getSocieties();

        LoggerGeneral.info("showintentList------"+societies+"----"+societies.size());
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        title=((TextView) toolbar.findViewById(R.id.toolbar_title));
        title.setText("My Society");
        toolbar.setTitleTextColor(android.graphics.Color.WHITE);

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

                        final List<SocietyModel> filteredList = new ArrayList<SocietyModel>();

                        for (int i = 0; i < societies.size(); i++) {

                            final String text = societies.get(i).getName().toLowerCase();
                            if (text.contains(query)) {

                                filteredList.add(societies.get(i));
                            }
                        }

                        recyclerView.setLayoutManager(new LinearLayoutManager(context));
                        adapter = new SocietyAdapter(context, filteredList);
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
                    adapter =new SocietyAdapter(context,societies);
                  //  adapter = new SocietyAdapter(context, societies);

                    recyclerView.setAdapter(adapter);

                    adapter.notifyDataSetChanged();


                }
                else {


                    Intent back = new Intent(MySociety.this, Profile.class);
                    back.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
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
                Intent notification=new Intent(MySociety.this,NotificationApp.class);
                //    notification.putExtra("SocietyId",Soci)
                int test1=appPreferences.getInt("NotificationOld",0);
                test1=test1+Constants.notififcationcount;
                appPreferences.putInt("NotificationOld", test1);
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
                Intent myProperty=new Intent(MySociety.this,MyProperty.class);
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
                        Intent profile=new Intent(MySociety.this,Profile.class);
                        profile.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(profile);
                        finish();
                        mDrawerLayout.closeDrawers();
                        break;
                    case 2:
                        LoggerGeneral.info("2");
                        Intent addSociety=new Intent(MySociety.this,AddSociety.class);
                        startActivity(addSociety);
                        finish();
                        break;
                    case 3:
                        LoggerGeneral.info("3");
                        if(appPreferences.getInt("property_count",0)>0) {
                        Intent myProperty=new Intent(MySociety.this,MyProperty.class);
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
                        Intent events=new Intent(MySociety.this,MyProperty.class);
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
                        Intent createEvents=new Intent(MySociety.this,MyProperty.class);
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
                        Intent archivedEvents=new Intent(MySociety.this,MyProperty.class);
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
                        Intent announcement=new Intent(MySociety.this,MyProperty.class);
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
                        Intent createAnnouncement=new Intent(MySociety.this,MyProperty.class);
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
                        Intent polls=new Intent(MySociety.this,MyProperty.class);
                        Common.showToast(context, "Please select property first");
                        PropertyAdapter.intentCheck=9;
                        polls.putExtra("polls", 2);
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
                        Intent createEventPoll=new Intent(MySociety.this,MyProperty.class);
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
                        Intent archivedEventPoll=new Intent(MySociety.this,MyProperty.class);
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
                        Intent members=new Intent(MySociety.this,MyProperty.class);
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
                        Intent offers=new Intent(MySociety.this,MyProperty.class);
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
                        Intent complaint=new Intent(MySociety.this,MyProperty.class);
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
                        Intent notification=new Intent(MySociety.this,NotificationApp.class);
                        //    notification.putExtra("SocietyId",Soci)
                        int test1=appPreferences.getInt("NotificationOld",0);
                        test1=test1+Constants.notififcationcount;
                        appPreferences.putInt("NotificationOld",test1);
                        Constants.notififcationcount=0;
                        startActivity(notification);
                        finish();
                        break;
                    case 16:

                       /* Intent intent = new Intent(MySociety.this,Login.class);
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


        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.addItemDecoration(new VerticalLineDecorator(2));



        prpublish = (LinearLayout)findViewById(R.id.prpublish);
        prpublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addsociety  = new Intent(MySociety.this,AddSociety.class);
                startActivity(addsociety);
                finish();
            }
        });


       /* for(int i = 0;i<=19;i++){
            societyModel = new SocietyModel();
            societyModel.setName(TitleName[i]);
            societyModel.setIdd(i);
            societies.add(societyModel);
//            societyModel.setSocimg(String.valueOf(R.mipmap.background_material));
            LoggerGeneral.info("showsoc"+societyModel.getName()+"---"+societies.get(i).getName());


        }
*/




      //  adapter = new SocietyAdapter(this, societies);

        adapter  =new SocietyAdapter(this,societies);

        recyclerView.setAdapter(adapter);

        adapter.notifyDataSetChanged();




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

            mDialog = new ProgressDialog(MySociety.this,ProgressDialog.THEME_HOLO_DARK);
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
                        Intent register   = new Intent(MySociety.this,Login.class);
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

    public static void removeItem ( int id)
                    {
                        LoggerGeneral.info("pass name"+id);
        for(int i=0;i<societies.size();i++)
        {
            SocietyModel societyModel=societies.get(i);
            int remove=societyModel.getIdd();

            if(id==remove)
            {
                LoggerGeneral.info("passed name"+remove);
                societies.remove(i);
                break;
            }
        }
                        LoggerGeneral.info("hiii" + societies.size());
    }

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
            adapter = new SocietyAdapter(this, societies);

            recyclerView.setAdapter(adapter);

            adapter.notifyDataSetChanged();

            return;
        }
        else {

            super.onBackPressed();
            Intent back = new Intent(MySociety.this, SearchSociety.class);
            startActivity(back);
            finish();
        }
    }
}