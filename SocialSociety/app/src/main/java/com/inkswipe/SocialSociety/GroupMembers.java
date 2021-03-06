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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import adapter.CustomAdapter;
import adapter.MemberAdapter;
import adapter.MemberAdapter1;
import adapter.PropertyAdapter;
import model.DrawerList;
import model.MembersModel;
import util.AppPreferences;
import util.Common;
import util.Constants;
import util.ExceptionHandler;
import util.LoggerGeneral;
import util.ServiceFacade;

public class GroupMembers extends AppCompatActivity {
    String SocietyId,GroupId;
    Context context;
    AppPreferences appPreferences;
    TextView title;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    public boolean chechSearchVisiblity=false;
    EditText searchSociety;
    LinearLayout home;
    RelativeLayout notification;
    LinearLayout search;
    RecyclerView recyclerView;
    MemberAdapter1 adapter;
    List<MembersModel> membersModelList;
    MembersModel membersModel;
    String id,groupId,appUserId,userName,profileImageURL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        setContentView(R.layout.activity_group_members);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        SocietyId=getIntent().getStringExtra("SocietyId");
        GroupId=getIntent().getStringExtra("GroupId");

        context=GroupMembers.this;
        appPreferences= AppPreferences.getAppPreferences(context);
        title=((TextView) toolbar.findViewById(R.id.toolbar_title));
        title.setText("Group Members");
        toolbar.setTitleTextColor(android.graphics.Color.WHITE);

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
                    adapter = new MemberAdapter1(context, membersModelList,0);

                    recyclerView.setAdapter(adapter);

                    adapter.notifyDataSetChanged();
                }
                else {

                        Intent back = new Intent(GroupMembers.this, Groups.class);
                        back.putExtra("SocietyId",SocietyId);
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
                Intent notification=new Intent(GroupMembers.this,NotificationApp.class);
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

                        final List<MembersModel> filteredList = new ArrayList<MembersModel>();

                        for (int i = 0; i < membersModelList.size(); i++) {

                            final String text = membersModelList.get(i).getMemberName().toLowerCase();
                            if (text.contains(query)) {

                                filteredList.add(membersModelList.get(i));
                            }
                        }

                        recyclerView.setLayoutManager(new LinearLayoutManager(context));
                        adapter = new MemberAdapter1(context, filteredList,0);
                        recyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();  // data set changed
                    }
                });

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
                Intent myProperty=new Intent(GroupMembers.this,MyProperty.class);
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
                        Intent profile = new Intent(GroupMembers.this, Profile.class);
                        profile.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(profile);
                        finish();
                        break;
                    case 2:
                        LoggerGeneral.info("2");
                        Intent addSociety = new Intent(GroupMembers.this, AddSociety.class);
                        startActivity(addSociety);
                        finish();
                        break;
                    case 3:
                        LoggerGeneral.info("3");
                        Intent myProperty = new Intent(GroupMembers.this, MyProperty.class);
                        PropertyAdapter.intentCheck=0;
                        startActivity(myProperty);
                        finish();
                        break;
                    case 4:
                        LoggerGeneral.info("4");
                        Intent events = new Intent(GroupMembers.this, DashBoard.class);
                        events.putExtra("SocietyId",SocietyId);
                        events.putExtra("event", 3);
                        startActivity(events);
                        finish();
                        break;
                    case 5:
                        LoggerGeneral.info("5");
                        Intent createEvents = new Intent(GroupMembers.this, CreateEvent.class);
                        createEvents.putExtra("SocietyId",SocietyId);
                        startActivity(createEvents);
                        finish();
                        break;
                    case 6:
                        LoggerGeneral.info("6");
                        Intent archivedEvents = new Intent(GroupMembers.this, Archivedevent.class);
                        archivedEvents.putExtra("SocietyId",SocietyId);
                        startActivity(archivedEvents);
                        finish();
                        break;
                    case 7:
                        LoggerGeneral.info("7");
                        Intent announcement = new Intent(GroupMembers.this, DashBoard.class);
                        announcement.putExtra("SocietyId",SocietyId);
                        announcement.putExtra("announcement", 4);
                        startActivity(announcement);
                        finish();
                        break;
                    case 8:
                        LoggerGeneral.info("8");
                        Intent createAnnouncement = new Intent(GroupMembers.this, CreateAnnouncement.class);
                        createAnnouncement.putExtra("SocietyId",SocietyId);
                        startActivity(createAnnouncement);
                        finish();
                        break;
                    case 9:
                        LoggerGeneral.info("9");
                        Intent polls = new Intent(GroupMembers.this, DashBoard.class);
                        polls.putExtra("SocietyId",SocietyId);
                        polls.putExtra("polls", 2);
                        startActivity(polls);
                        finish();
                        break;
                    case 10:
                        LoggerGeneral.info("10");
                        Intent createEventPoll = new Intent(GroupMembers.this, CreatePoll.class);
                        createEventPoll.putExtra("SocietyId",SocietyId);
                        startActivity(createEventPoll);
                        finish();
                        break;
                    case 11:
                        LoggerGeneral.info("11");
                        Intent archivedEventPoll = new Intent(GroupMembers.this, ArchivedPolls.class);
                        archivedEventPoll.putExtra("SocietyId",SocietyId);
                        startActivity(archivedEventPoll);
                        finish();
                        break;
                    case 12:
                        LoggerGeneral.info("12");

                        mDrawerLayout.closeDrawers();
                        break;
                    case 13:
                        LoggerGeneral.info("13");
                        Intent offers = new Intent(GroupMembers.this, Offer.class);
                        offers.putExtra("SocietyId",SocietyId);
                        startActivity(offers);
                        finish();
                        break;
                    case 14:
                        LoggerGeneral.info("14");
                        Intent complaint = new Intent(GroupMembers.this, Complaint.class);
                        complaint.putExtra("SocietyId",SocietyId);
                        startActivity(complaint);
                        finish();
                        break;
                    case 15:
                        LoggerGeneral.info("15");
                        Intent notification=new Intent(GroupMembers.this,NotificationApp.class);
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
                        LoggerGeneral.info("16");
                      /*  Intent intent = new Intent(GroupMembers.this,Login.class);
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

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.addItemDecoration(new VerticalLineDecorator(2));
        membersModelList = new ArrayList<MembersModel>();

        if(Common.isOnline(context)){

            new getMyMembers().execute();
        }
        else {
            Common.showToast(context,"No internet connection!");
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

            mDialog = new ProgressDialog(GroupMembers.this,ProgressDialog.THEME_HOLO_DARK);
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
                        Intent register   = new Intent(GroupMembers.this,Login.class);
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

    class  getMyMembers extends AsyncTask<String, String, JSONObject> {
        String type;
        ProgressDialog mDialog;
        @Override
        protected JSONObject doInBackground(String... params) {
            // TODO Auto-generated method stub

            String url= Constants.Base+Constants.getGroupsMember;

            JSONObject object = new JSONObject();
            try {

                // object.put("user_id",appPreferences.getString("user_id",""));
                // object.put("user_id",appPreferences.getString("user_id",""));

                object.put("group_id",GroupId);
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
                            membersModelList = new ArrayList<MembersModel>();
                            for (int i = 0; i <= data.length() - 1; i++) {

                                membersModel = new MembersModel();

                                JSONObject jsonObject1 = data.getJSONObject(i);

                                id=jsonObject1.getString("id");
                                userName=jsonObject1.getString("user_name");
                              //  appUserId=jsonObject1.getString("app_user_id");
                                profileImageURL=jsonObject1.getString("profile_image_url");
                               // groupId=jsonObject1.getString("group_id");

                                membersModel.setId(id);
                                membersModel.setUserName(userName);
                              //  membersModel.setAppUserId(appUserId);
                                membersModel.setProfileImageURL(profileImageURL);
                              //  membersModel.setGroupId(groupId);


                                membersModelList.add(membersModel);


                                LoggerGeneral.info("s_societies000---" + membersModelList.size());

                            }


                            LoggerGeneral.info("s_societies---" + membersModelList);
                            adapter=new MemberAdapter1(context,membersModelList,0);
                            recyclerView.setAdapter(adapter);

                            adapter.notifyDataSetChanged();


                        }

                        if (status_code == 1) {






                        }
                    }

                    if(account_status==0){
                        Intent intent = new Intent(context,Login.class);
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
    public void onBackPressed() {

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
            adapter = new MemberAdapter1(context, membersModelList,0);

            recyclerView.setAdapter(adapter);

            adapter.notifyDataSetChanged();
        }
        else {

            Intent back = new Intent(GroupMembers.this, Groups.class);
            back.putExtra("SocietyId",SocietyId);
            startActivity(back);
            finish();

        }

    }

}
