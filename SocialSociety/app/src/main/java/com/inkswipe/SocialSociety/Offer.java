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

import com.flurry.android.FlurryAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import adapter.CustomAdapter;
import adapter.OfferAdapter;
import adapter.PropertyAdapter;
import model.DrawerList;
import model.OfferModel;
import util.AppPreferences;
import util.Common;
import util.Constants;
import util.ExceptionHandler;
import util.LoggerGeneral;
import util.ServiceFacade;

public class Offer extends AppCompatActivity {
    RecyclerView recyclerView;
    List<OfferModel> offerModels;
    OfferAdapter adapter;
    Context context;
    AppPreferences appPreferences;

    LinearLayout search;
    EditText searchSociety;
    TextView title;

    LinearLayout home;
    Fragment fragment = null;
    TextView appname;
    List<String> listDataHeader;

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;

    TextView nooffers;
    int savedInstanceCheck=0;
    public static Bundle temp_bundle;
    RelativeLayout notification;
    public boolean chechSearchVisiblity=false;
    String SocietyId ;
    String offerId,offerTitle,offerDescription,offerCompanyName,offerCompanyAddress,offerOfferLogo,offerImage,offerShareWith,offerContactPersonName,offercontact_person_mobile,offercontact_person_email,offeramount,offercreator_notes,offerstatus,offercreated_by,offerstart_date,offerexpire_on,offercreated_on,offer_image_url,offer_logo_url,offerdays_pass,category,externalUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (getIntent().hasExtra("bundle") && savedInstanceState==null){
            LoggerGeneral.info("isItRight");
            savedInstanceCheck=1;
            savedInstanceState = getIntent().getExtras().getBundle("bundle");
        }
        super.onCreate(savedInstanceState);
        Common.internet_check=6;
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        setContentView(R.layout.activity_offer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context=Offer.this;
        SocietyId = getIntent().getStringExtra("SocietyId");
        if(savedInstanceCheck==1)
        {
            SocietyId=savedInstanceState.getString("SocietyId");
        }
        title=((TextView) toolbar.findViewById(R.id.toolbar_title));
        title.setText("Offers");
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
                    adapter=new OfferAdapter(context,offerModels);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                }
                else {

                    Intent back = new Intent(Offer.this, DashBoard.class);
                    back.putExtra("SocietyId",SocietyId);
                    Common.internet_check=0;
                    startActivity(back);
                    finish();
                }
            }
        });


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.my_statusbar_color));
        }

        appPreferences = AppPreferences.getAppPreferences(this);

        nooffers = (TextView)findViewById(R.id.nooffers);
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
                Intent notification=new Intent(Offer.this,NotificationApp.class);
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

                        final List<OfferModel> filteredList = new ArrayList<OfferModel>();

                        for (int i = 0; i < offerModels.size(); i++) {

                            final String text = offerModels.get(i).getOfferTitle().toLowerCase();
                            if (text.contains(query)) {

                                filteredList.add(offerModels.get(i));
                            }
                        }

                        recyclerView.setLayoutManager(new LinearLayoutManager(context));
                        adapter = new OfferAdapter(context, filteredList);
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
                Intent myProperty=new Intent(Offer.this,MyProperty.class);
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
                        Intent profile = new Intent(Offer.this, Profile.class);
                        profile.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        Common.internet_check=0;
                        startActivity(profile);
                        finish();
                        break;
                    case 2:
                        LoggerGeneral.info("2");
                        Intent addSociety = new Intent(Offer.this, AddSociety.class);
                        Common.internet_check=0;
                        startActivity(addSociety);
                        finish();
                        break;
                    case 3:
                        LoggerGeneral.info("3");
                        Intent myProperty = new Intent(Offer.this, MyProperty.class);
                        Common.internet_check=0;
                        PropertyAdapter.intentCheck = 0;
                        startActivity(myProperty);
                        finish();
                        break;
                    case 4:
                        LoggerGeneral.info("4");
                        Intent events = new Intent(Offer.this, DashBoard.class);
                        Common.internet_check=0;
                        events.putExtra("SocietyId", SocietyId);
                        events.putExtra("event", 3);
                        startActivity(events);
                        finish();
                        break;
                    case 5:
                        LoggerGeneral.info("5");
                        Intent createEvents = new Intent(Offer.this, CreateEvent.class);
                        Common.internet_check=0;
                        createEvents.putExtra("SocietyId", SocietyId);
                        startActivity(createEvents);
                        finish();
                        break;
                    case 6:
                        LoggerGeneral.info("6");
                        Intent archivedEvents = new Intent(Offer.this, Archivedevent.class);
                        Common.internet_check=0;
                        archivedEvents.putExtra("SocietyId", SocietyId);
                        startActivity(archivedEvents);
                        finish();
                        break;
                    case 7:
                        LoggerGeneral.info("7");
                        Intent announcement = new Intent(Offer.this, DashBoard.class);
                        Common.internet_check=0;
                        announcement.putExtra("SocietyId", SocietyId);
                        announcement.putExtra("announcement", 4);
                        startActivity(announcement);
                        finish();
                        break;
                    case 8:
                        LoggerGeneral.info("8");
                        Intent createAnnouncement = new Intent(Offer.this, CreateAnnouncement.class);
                        Common.internet_check=0;
                        createAnnouncement.putExtra("SocietyId", SocietyId);
                        startActivity(createAnnouncement);
                        finish();
                        break;
                    case 9:
                        LoggerGeneral.info("9");
                        Intent polls = new Intent(Offer.this, DashBoard.class);
                        Common.internet_check=0;
                        polls.putExtra("SocietyId", SocietyId);
                        polls.putExtra("polls", 2);
                        startActivity(polls);
                        finish();
                        break;
                    case 10:
                        LoggerGeneral.info("10");
                        Intent createEventPoll = new Intent(Offer.this, CreatePoll.class);
                        Common.internet_check=0;
                        createEventPoll.putExtra("SocietyId", SocietyId);
                        startActivity(createEventPoll);
                        finish();
                        break;
                    case 11:
                        LoggerGeneral.info("11");
                        Intent archivedEventPoll = new Intent(Offer.this, ArchivedPolls.class);
                        Common.internet_check=0;
                        archivedEventPoll.putExtra("SocietyId", SocietyId);
                        startActivity(archivedEventPoll);
                        finish();
                        break;
                    case 12:
                        LoggerGeneral.info("12");
                        Intent members = new Intent(Offer.this, Members.class);
                        Common.internet_check=0;
                        members.putExtra("SocietyId", SocietyId);
                        startActivity(members);
                        finish();
                        break;
                    case 13:
                        LoggerGeneral.info("13");
                        /*Intent offers=new Intent(Offer.this,Offer.class);
                        startActivity(offers);
                        finish();*/
                        mDrawerLayout.closeDrawers();
                        if(Common.isOnline(context)){

                            new getMyOffers().execute();
                        }
                        else {
                            Common.showToast(context,"No internet connection!");
                        }
                        break;
                    case 14:
                        LoggerGeneral.info("14");
                        Intent complaint = new Intent(Offer.this, Complaint.class);
                        Common.internet_check=0;
                        complaint.putExtra("SocietyId", SocietyId);
                        startActivity(complaint);
                        finish();
                        break;
                    case 15:
                        LoggerGeneral.info("15");
                        Intent notification=new Intent(Offer.this,NotificationApp.class);
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
                        LoggerGeneral.info("16");
                      /*  Intent intent = new Intent(Offer.this, Login.class);
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

        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        /*offerModels= new ArrayList<OfferModel>();
        for(int i=0;i<=5;i++)
        {
            OfferModel offerModel=new OfferModel();
            offerModel.setDescription("want to update your style ? check our all new collection today and e the first to grab the style you love,all great prices.Shop Now!");
            offerModel.setBrandName("Flipkart");
            offerModels.add(offerModel);
        }
        LoggerGeneral.info("List Size" + offerModels.size());
        adapter=new OfferAdapter(context,offerModels);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();*/

        if(Common.isOnline(context)){

            new getMyOffers().execute();
        }
        else {
            Common.showToast(context,"No internet connection!");
        }

        temp_bundle = new Bundle();
        onSaveInstanceState(temp_bundle);

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
            adapter=new OfferAdapter(context,offerModels);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            return;
        }
        else {

            super.onBackPressed();
            Intent back = new Intent(Offer.this, DashBoard.class);
            Common.internet_check=0;
            back.putExtra("SocietyId",SocietyId);
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

            mDialog = new ProgressDialog(Offer.this,ProgressDialog.THEME_HOLO_DARK);
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
                        Intent register   = new Intent(Offer.this,Login.class);
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

    class  getMyOffers extends AsyncTask<String, String, JSONObject> {
        String type;
        ProgressDialog mDialog;
        @Override
        protected JSONObject doInBackground(String... params) {
            // TODO Auto-generated method stub

              String url= Constants.Base+Constants.getMyOffers;


           // String url ="http://114.79.137.193:8080/social-society/poonam/get-offers";
            JSONObject object = new JSONObject();
            try {

                // object.put("user_id",appPreferences.getString("user_id",""));
                // object.put("user_id",appPreferences.getString("user_id",""));

                object.put("society_id",SocietyId);
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
                            offerModels=new ArrayList<OfferModel>();
                            for (int i = data.length() - 1; 0 <= i; i--) {

                                OfferModel offerModel=new OfferModel();

                                JSONObject jsonObject1 = data.getJSONObject(i);

                                offerId=jsonObject1.getString("id");
                                offerTitle=jsonObject1.getString("title");
                                offerDescription=jsonObject1.getString("description");
                                offerCompanyName=jsonObject1.getString("company_name");
                                offerCompanyAddress=jsonObject1.getString("company_address");
                                offerOfferLogo=jsonObject1.getString("offer_logo");
                                offerImage=jsonObject1.getString("offer_image");
                                offerShareWith=jsonObject1.getString("shared_with");
                                offerContactPersonName=jsonObject1.getString("contact_person_name");
                                offercontact_person_mobile=jsonObject1.getString("contact_person_mobile");
                                offercontact_person_email=jsonObject1.getString("contact_person_email");
                                offeramount=jsonObject1.getString("amount");
                                offercreator_notes=jsonObject1.getString("creator_notes");
                                offerstatus=jsonObject1.getString("status");
                                offercreated_by=jsonObject1.getString("created_by");
                                offerstart_date=jsonObject1.getString("start_date");
                                offerexpire_on=jsonObject1.getString("expire_on");
                                offercreated_on=jsonObject1.getString("created_on");
                                offer_image_url=jsonObject1.getString("offer_image_url");
                                offer_logo_url=jsonObject1.getString("offer_logo_url");
                                offerdays_pass=jsonObject1.getString("days_pass");
                                category      = jsonObject1.getString("category");
                                externalUrl   =jsonObject1.getString("url");

                                offerModel.setOfferId(offerId);
                                offerModel.setOfferTitle(offerTitle);
                                offerModel.setOfferDescription(offerDescription);
                                offerModel.setOfferCompanyName(offerCompanyName);
                                offerModel.setOfferCompanyAddress(offerCompanyAddress);
                                offerModel.setOfferOfferLogo(offerOfferLogo);
                                offerModel.setOfferImage(offerImage);
                                offerModel.setOfferShareWith(offerShareWith);
                                offerModel.setOfferContactPersonName(offerContactPersonName);
                                offerModel.setOffercontact_person_mobile(offercontact_person_mobile);
                                offerModel.setOffercontact_person_email(offercontact_person_email);
                                offerModel.setOfferamount(offeramount);
                                offerModel.setOffercreator_notes(offercreator_notes);
                                offerModel.setOfferstatus(offerstatus);
                                offerModel.setOffercreated_by(offercreated_by);
                                offerModel.setOffercreated_on(offercreated_on);
                                offerModel.setOfferstart_date(offerstart_date);
                                offerModel.setOfferexpire_on(offerexpire_on);
                                offerModel.setOffer_image_url(offer_image_url);
                                offerModel.setOffer_logo_url(offer_logo_url);
                                offerModel.setCategory(category);
                                offerModel.setExternalUrl(externalUrl);


                                offerModels.add(offerModel);


                                LoggerGeneral.info("s_societies000---" + offerModels.size());

                            }


                            LoggerGeneral.info("s_societies---" + offerModels);

                            adapter=new OfferAdapter(context,offerModels);
                            recyclerView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();


                        }

                        if (status_code == 1) {

                            nooffers.setVisibility(View.VISIBLE);

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
