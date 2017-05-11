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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import adapter.CustomAdapter;
import adapter.GroupAdapter;
import adapter.PropertyAdapter;
import model.DrawerList;
import model.GroupsModel;
import util.AppPreferences;
import util.Common;
import util.Constants;
import util.ExceptionHandler;
import util.LoggerGeneral;
import util.ServiceFacade;

public class Groups extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter1;
    Context context;
    GroupAdapter adapter;
    TextView nomember;
    static List<GroupsModel> groupsModelList;

    LinearLayout home;
    Fragment fragment = null;
    TextView appname;

    AppPreferences appPreferences;
    GroupsModel groupsModel;
    LinearLayout search,listOfMebers;
    TextView title;
    EditText searchSociety;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    public boolean chechSearchVisiblity=false;

    RelativeLayout notification;
    String SocietyId;

    String groupId,groupSocietyId,groupName,groupImage,groupStatus,groupCreatedBy,groupCreatedOn,groupImageUrl,groupUserName;
    public static Bundle temp_bundle;
    int savedInstanceCheck=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (getIntent().hasExtra("bundle") && savedInstanceState==null){
            LoggerGeneral.info("isItRight");
            savedInstanceCheck=1;
            savedInstanceState = getIntent().getExtras().getBundle("bundle");
        }
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        setContentView(R.layout.activity_groups);
        context =Groups.this;
        Common.internet_check=7;

        nomember = (TextView)findViewById(R.id.nogroup);
        appPreferences=AppPreferences.getAppPreferences(context);
        SocietyId=getIntent().getStringExtra("SocietyId");

        if (savedInstanceCheck==1)
        {
            SocietyId=savedInstanceState.getString("SocietyId");
        }
        LoggerGeneral.info("societyId is heree"+SocietyId);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        title=((TextView) toolbar.findViewById(R.id.toolbar_title));
        title.setText("Groups");
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

                        final List<GroupsModel> filteredList = new ArrayList<GroupsModel>();

                        for (int i = 0; i < groupsModelList.size(); i++) {

                            final String text = groupsModelList.get(i).getGroupName().toLowerCase();
                            if (text.contains(query)) {

                                filteredList.add(groupsModelList.get(i));
                            }
                        }

                        recyclerView.setLayoutManager(new LinearLayoutManager(context));
                        adapter = new GroupAdapter(context, filteredList,1,SocietyId);
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
                    adapter1 = new GroupAdapter(context, groupsModelList,1,SocietyId);

                    recyclerView.setAdapter(adapter1);

                    adapter1.notifyDataSetChanged();
                }
                else {

                    Intent back = new Intent(Groups.this, DashBoard.class);
                    back.putExtra("SocietyId",SocietyId);
                    Common.internet_check=0;
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
                Intent notification=new Intent(Groups.this,NotificationApp.class);
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
                Intent myProperty=new Intent(Groups.this,MyProperty.class);
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
                        Intent profile = new Intent(Groups.this, Profile.class);
                        profile.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        Common.internet_check=0;
                        startActivity(profile);
                        finish();
                        break;
                    case 2:
                        LoggerGeneral.info("2");
                        Intent addSociety = new Intent(Groups.this, AddSociety.class);
                        Common.internet_check=0;
                        startActivity(addSociety);
                        finish();
                        break;
                    case 3:
                        LoggerGeneral.info("3");
                        Intent myProperty = new Intent(Groups.this, MyProperty.class);
                        Common.internet_check=0;
                        PropertyAdapter.intentCheck = 0;
                        startActivity(myProperty);
                        finish();
                        break;
                    case 4:
                        LoggerGeneral.info("4");
                        Intent events = new Intent(Groups.this, DashBoard.class);
                        Common.internet_check=0;
                        events.putExtra("SocietyId",SocietyId);
                        events.putExtra("event", 3);
                        startActivity(events);
                        finish();
                        break;
                    case 5:
                        LoggerGeneral.info("5");
                        Intent createEvents = new Intent(Groups.this, CreateEvent.class);
                        Common.internet_check=0;
                        createEvents.putExtra("SocietyId",SocietyId);
                        startActivity(createEvents);
                        finish();
                        break;
                    case 6:
                        LoggerGeneral.info("6");
                        Intent archivedEvents = new Intent(Groups.this, Archivedevent.class);
                        Common.internet_check=0;
                        archivedEvents.putExtra("SocietyId",SocietyId);
                        startActivity(archivedEvents);
                        finish();
                        break;
                    case 7:
                        LoggerGeneral.info("7");
                        Intent announcement = new Intent(Groups.this, DashBoard.class);
                        Common.internet_check=0;
                        announcement.putExtra("SocietyId",SocietyId);
                        announcement.putExtra("announcement", 4);
                        startActivity(announcement);
                        finish();
                        break;
                    case 8:
                        LoggerGeneral.info("8");
                        Intent createAnnouncement = new Intent(Groups.this, CreateAnnouncement.class);
                        Common.internet_check=0;
                        createAnnouncement.putExtra("SocietyId",SocietyId);
                        startActivity(createAnnouncement);
                        finish();
                        break;
                    case 9:
                        LoggerGeneral.info("9");
                        Intent polls = new Intent(Groups.this, DashBoard.class);
                        Common.internet_check=0;
                        polls.putExtra("SocietyId",SocietyId);
                        polls.putExtra("polls", 2);
                        startActivity(polls);
                        finish();
                        break;
                    case 10:
                        LoggerGeneral.info("10");
                        Intent createEventPoll = new Intent(Groups.this, CreatePoll.class);
                        Common.internet_check=0;
                        createEventPoll.putExtra("SocietyId",SocietyId);
                        startActivity(createEventPoll);
                        finish();
                        break;
                    case 11:
                        LoggerGeneral.info("11");
                        Intent archivedEventPoll = new Intent(Groups.this, ArchivedPolls.class);
                        Common.internet_check=0;
                        archivedEventPoll.putExtra("SocietyId",SocietyId);
                        startActivity(archivedEventPoll);
                        finish();
                        break;
                    case 12:
                        LoggerGeneral.info("12");
                        Intent members = new Intent(Groups.this, Members.class);
                        Common.internet_check=0;
                        members.putExtra("SocietyId",SocietyId);
                        startActivity(members);
                        finish();
                        break;
                    case 13:
                        LoggerGeneral.info("13");
                        Intent offers = new Intent(Groups.this, Offer.class);
                        Common.internet_check=0;
                        offers.putExtra("SocietyId",SocietyId);
                        startActivity(offers);
                        finish();
                        break;
                    case 14:
                        LoggerGeneral.info("14");
                        Intent complaint = new Intent(Groups.this, Complaint.class);
                        Common.internet_check=0;
                        complaint.putExtra("SocietyId",SocietyId);
                        startActivity(complaint);
                        finish();
                        break;
                    case 15:
                        LoggerGeneral.info("15");
                        Intent notification=new Intent(Groups.this,NotificationApp.class);
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
                      /*  LoggerGeneral.info("16");
                        Intent intent = new Intent(Groups.this, Login.class);
                        Common.internet_check=0;
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
        groupsModelList = new ArrayList<GroupsModel>();



        /*groupsModel = new GroupsModel();
        for(int i = 0;i<=10;i++){

            groupsModel = new GroupsModel();
            groupsModel.setGroupName("Jhon Doe"+i);
            groupsModel.setDescription("Member");
            groupsModel.setId(i);
            groupsModelList.add(groupsModel);
//            societyModel.setSocimg(String.valueOf(R.mipmap.background_material));
            LoggerGeneral.info("showsoc" +groupsModel.getGroupName() + "---" + groupsModelList.get(i).getGroupName());


        }*/

        if(Common.isOnline(context)){

            new getMyGroup().execute();
        }
        else {
            Common.showToast(context,"No internet connection!");
        }



        /*adapter1 = new GroupAdapter(this, groupsModelList,1);

        recyclerView.setAdapter(adapter1);

        adapter1.notifyDataSetChanged();*/
        listOfMebers= (LinearLayout) findViewById(R.id.listOfMembers);
        listOfMebers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent members=new Intent(Groups.this,Members.class);
                members.putExtra("SocietyId",SocietyId);
                Common.internet_check=0;
                startActivity(members);
                finish();
            }
        });

        temp_bundle = new Bundle();
        onSaveInstanceState(temp_bundle);

    }

    public static void removeItem(int id)
    {
        LoggerGeneral.info("pass name"+id);
        for(int i=0;i<groupsModelList.size();i++)
        {
            GroupsModel societyModel=groupsModelList.get(i);
            int remove=societyModel.getIdd();

            if(id==remove)
            {
                LoggerGeneral.info("passed name"+remove);
                groupsModelList.remove(i);
                break;
            }
        }
        LoggerGeneral.info("hiii" + groupsModelList.size());
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
            adapter1 = new GroupAdapter(this, groupsModelList,1,SocietyId);

            recyclerView.setAdapter(adapter1);

            adapter1.notifyDataSetChanged();
            return;
        }
        else {

            super.onBackPressed();
            Intent back = new Intent(Groups.this, DashBoard.class);
            back.putExtra("SocietyId",SocietyId);
            Common.internet_check=0;
            startActivity(back);
            finish();
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

            mDialog = new ProgressDialog(Groups.this,ProgressDialog.THEME_HOLO_DARK);
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
                        Intent register   = new Intent(Groups.this,Login.class);
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

    class  getMyGroup extends AsyncTask<String, String, JSONObject> {
        String type;
        ProgressDialog mDialog;
        @Override
        protected JSONObject doInBackground(String... params) {
            // TODO Auto-generated method stub

            String url= Constants.Base+Constants.getGroups;

            JSONObject object = new JSONObject();
            try {

                // object.put("user_id",appPreferences.getString("user_id",""));
                // object.put("user_id",appPreferences.getString("user_id",""));

                object.put("society_id",SocietyId);

                object.put("user_id",appPreferences.getString("user_id",""));

                LoggerGeneral.info("JsonObjectPrint" + object.toString());

            } catch (Exception ex) {

            }

            //String str = '"' + appPreferences.getString("jwt", "") + '"';
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

                            JSONArray data = results.getJSONArray("data");


                            //JSONArray jsonArray = data.getJSONArray("data");

                            LoggerGeneral.info("showjarray---" + data);
                            groupsModelList = new ArrayList<GroupsModel>();
                            for (int i = 0; i <= data.length() - 1; i++) {

                                groupsModel = new GroupsModel();

                                JSONObject jsonObject1 = data.getJSONObject(i);

                                groupId=jsonObject1.getString("id");
                                groupSocietyId=jsonObject1.getString("society_id");
                                groupName=jsonObject1.getString("group_name");
                                groupImage=jsonObject1.getString("group_image");
                                groupStatus=jsonObject1.getString("status");
                                groupCreatedBy=jsonObject1.getString("created_by");
                                groupCreatedOn=jsonObject1.getString("created_on");
                                groupImageUrl=jsonObject1.getString("group_image_url");
                                groupUserName=jsonObject1.getString("user_name");

                                groupsModel.setIdd(i);
                                groupsModel.setGroupId(groupId);
                                groupsModel.setGroupSocietyId(groupSocietyId);
                                groupsModel.setGroupName(groupName);
                                groupsModel.setGroupImage(groupImage);
                                groupsModel.setGroupStatus(groupStatus);
                                groupsModel.setGroupCreatedBy(groupCreatedBy);
                                groupsModel.setGroupCreatedOn(groupCreatedOn);
                                groupsModel.setGroupImageUrl(groupImageUrl);
                                groupsModel.setGroupUserName(groupUserName);

                                groupsModelList.add(groupsModel);


                                LoggerGeneral.info("s_societies000---" + groupsModelList.size());

                            }


                            LoggerGeneral.info("s_societies---" + groupsModelList);

                            if(groupsModelList.size()>0) {
                                nomember.setVisibility(View.GONE);
                                adapter = new GroupAdapter(context, groupsModelList, 1, SocietyId);
                                recyclerView.setAdapter(adapter);

                                adapter.notifyDataSetChanged();

                            }
                            if(groupsModelList.size()==0) {

                                LoggerGeneral.info("eneteredgroup");

                                nomember.setVisibility(View.VISIBLE);
                            }

                        }

                        if (status_code == 1) {

                            nomember.setVisibility(View.VISIBLE);






                        }
                    }

                    if(account_status==0){
                        Intent intent = new Intent(context,Login.class);
                        Common.internet_check=0;
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
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current game state

        if(SocietyId!=null) {
            temp_bundle.putString("SocietyId", SocietyId);
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



}

