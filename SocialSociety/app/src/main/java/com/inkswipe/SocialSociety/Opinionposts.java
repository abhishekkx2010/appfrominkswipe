package com.inkswipe.SocialSociety;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import adapter.OpinionpolllistAdapter;
import model.OpinionPoll_listmodel;
import util.AppPreferences;
import util.Common;
import util.Constants;
import util.LoggerGeneral;
import util.ServiceFacade;


/**
 * A simple {@link Fragment} subclass.
 */
public class Opinionposts extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    Context context;
    String SocietyId;
    List<OpinionPoll_listmodel> opinionpoll_list;

    OpinionPoll_listmodel opinionPoll_listmodel;


    LinearLayout archivedpoll,createpoll;


    AppPreferences appPreferences;
    public static  UserPost userPost;
    TextView pollss;

    @SuppressLint("ValidFragment")
    public Opinionposts(String societyId) {
        // Required empty public constructor

        this.SocietyId = societyId;
        appPreferences = AppPreferences.getAppPreferences(context);


        LoggerGeneral.info("Society post id22---" + SocietyId);
    }

    public Opinionposts( ) {


        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment



        View view = inflater.inflate(R.layout.fragment_opinionposts, container, false);

         context = getActivity();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context).build();
        ImageLoader.getInstance().init(config);

        pollss =(TextView)view.findViewById(R.id.pollss);
        recyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);

        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(context);

        recyclerView.setLayoutManager(layoutManager);

        archivedpoll = (LinearLayout) view.findViewById(R.id.archivedpoll);
        archivedpoll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,ArchivedPolls.class);
                intent.putExtra("SocietyId",SocietyId);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Common.internet_check=0;
                context.startActivity(intent);
                getActivity().finish();

            }
        });

        createpoll = (LinearLayout)view.findViewById(R.id.createpoll);
        createpoll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,CreatePoll.class);
                intent.putExtra("SocietyId",SocietyId);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Common.internet_check=0;
                context.startActivity(intent);
                getActivity().finish();

            }
        });
        opinionpoll_list = new ArrayList<OpinionPoll_listmodel>();

    /*    for(int i = 0;i<=20;i++){

            opinionPoll_listmodel.setUname("Rakesh Mourya");
            opinionPoll_listmodel.setCreatedby("Rakesh Mourya");
            opinionPoll_listmodel.setEnddate("01 Nov,2016");
            opinionPoll_listmodel.setSubject("Should we change the name and colour of our society? Please give answer!");
            opinionpoll_list.add(opinionPoll_listmodel);
//            societyModel.setSocimg(String.valueOf(R.mipmap.background_material));



        }*/


        //new getBookedPackages().execute();


        if(Common.isOnline(context)){

            userPost = new UserPost();
            userPost.execute();
        //    new UserPost().execute();
        }


/*==========================================================================================================================*/
      /*  Intent intent = getIntent();
        String json_object = intent.getStringExtra("json_objcet");
        try
        {




            JSONObject  result =  new JSONObject(json_object);
            JSONArray routearray = result.getJSONArray("Route");
            for (int i = 0; i < routearray.length(); i++) {

                String companyid = routearray.getJSONObject(i).getString("CompanyId");
                String CompanyName = routearray.getJSONObject(i).getString("CompanyName");
                String deptime = routearray.getJSONObject(i).getString("DepTime");
                String routeScheduleId = routearray.getJSONObject(i).getString("RouteScheduleId");
                String arrtime = routearray.getJSONObject(i).getString("ArrTime");
                String fare = routearray.getJSONObject(i).getString("Fare");
                String hasac = routearray.getJSONObject(i).getString("HasAC");
                String hasnac = routearray.getJSONObject(i).getString("HasNAC");
                String hasseater = routearray.getJSONObject(i).getString("HasSeater");
                String hassleeper = routearray.getJSONObject(i).getString("HasSleeper");
                String isvolvo = routearray.getJSONObject(i).getString("IsVolvo");
                String buslabel = routearray.getJSONObject(i).getString("BusLabel");
                String avaliableseats = routearray.getJSONObject(i).getString("AvailableSeats");
                String bustypename = routearray.getJSONObject(i).getString("BusTypeName");

                BusData bs = new BusData();
                bs.setCompanyname(CompanyName);
                bs.setCompanyid(companyid);
                bs.setFare(fare);
                bs.setBuslabel(buslabel);
                bs.setBustypename(bustypename);
                bs.setAvaliableseats(avaliableseats);

                bdata.add(bs);
            }



            BusDataAdapter  adapter = new BusDataAdapter(this, bdata);
            fromto.setAdapter(adapter);
        }




        catch (Exception e)
        {
            e.printStackTrace();
        }

*/
/*==================================================================================================*/







        return view;


        }


    class  UserPost extends AsyncTask<String, String, JSONObject> {
        String type;
        ProgressDialog mDialog;
        @Override
        protected JSONObject doInBackground(String... params) {
            // TODO Auto-generated method stub

            String url= Constants.Base+Constants.GetUserPolls;

            JSONObject object = new JSONObject();
            try {

              //  object.put("society_id",SocietyId);
              //  object.put("user_id",appPreferences.getString("user_id",""));
               object.put("user_id",appPreferences.getString("user_id",""));
               object.put("society_id",SocietyId);
             //   object.put("society_id",SocietyId);
                //    object.put("property_id",property_id);


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


            mDialog = new ProgressDialog(context,ProgressDialog.THEME_HOLO_DARK);
            mDialog.setMessage("Processing...");
            mDialog.show();
            mDialog.setCancelable(false);
            mDialog.setCanceledOnTouchOutside(false);
        }
        @Override
        protected void onCancelled(){

            super.onCancelled();
            mDialog.dismiss();
            LoggerGeneral.info("onCancelled");

        }


        @Override
        protected void onPostExecute(JSONObject results) {
            // TODO Auto-generated method stub
            super.onPostExecute(results);
            Log.d("hi", "getresultsstate" + results);
            //mDialog.dismiss();

            if (results != null) {

                try {

                    JSONObject meta = results.getJSONObject("meta");

                    int status_code = meta.getInt("status_code");
                  //  int pollstatus = meta.getInt("pollstatus");

                    int account_status = 1;
                    if (meta.has("account_status")) {
                        if (!meta.isNull("account_status")) {
                            account_status = meta.getInt("account_status");
                        }
                    }

                    if (account_status == 1) {
                        if (status_code == 0) {


                            JSONArray jsonArray = results.getJSONArray("data");

                            for (int i = jsonArray.length() - 1; 0 <= i; i--) {
                                opinionPoll_listmodel = new OpinionPoll_listmodel();
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                String id = jsonObject.getString("id");
                                String society_id = jsonObject.getString("society_id");
                                String poll_title = jsonObject.getString("poll_title");
                                String poll_question = jsonObject.getString("poll_question");
                                String poll_end_date = jsonObject.getString("poll_end_date");
                                String poll_image = jsonObject.getString("poll_image");
                                String poll_option_1 = jsonObject.getString("poll_option_1");
                                String poll_option_2 = jsonObject.getString("poll_option_2");
                                String poll_option_3 = jsonObject.getString("poll_option_3");
                                String poll_option_4 = jsonObject.getString("poll_option_4");
                                String poll_option_5 = jsonObject.getString("poll_option_5");
                                String poll_option_6 = jsonObject.getString("poll_option_6");
                                String user_name = jsonObject.getString("user_name");
                                String user_profile_image = jsonObject.getString("user_profile_image");
                                String shared_with = jsonObject.getString("shared_with");
                                String created_by = jsonObject.getString("created_by");
                                String created_on = jsonObject.getString("created_on");
                                String status = jsonObject.getString("status");
                                String poll_image_url = jsonObject.getString("poll_image_url");
                                String share_result = jsonObject.getString("share_result");

                                String myFormat1 = "yyyy-MM-dd hh:mm:ss";
                                String myFormat = "dd MMM, yyyy";
                                SimpleDateFormat sdf1 = new SimpleDateFormat(myFormat1, Locale.US);
                                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                                Date myDate = null;
                                try {
                                    myDate = sdf1.parse(poll_end_date);

                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                poll_end_date = sdf.format(myDate);

                                String myFormat11 = "yyyy-MM-dd hh:mm:ss";
                                String myFormat12 = "dd MMM, yyyy";
                                SimpleDateFormat sdf11 = new SimpleDateFormat(myFormat11, Locale.US);
                                SimpleDateFormat sdf12 = new SimpleDateFormat(myFormat12, Locale.US);

                                Date myDate1 = null;
                                try {
                                    myDate1 = sdf11.parse(created_on);

                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                created_on = sdf12.format(myDate1);

                                opinionPoll_listmodel.setId(id);
                                opinionPoll_listmodel.setSociety_id(society_id);
                                opinionPoll_listmodel.setPoll_title(poll_title);
                                opinionPoll_listmodel.setPoll_question(poll_question);
                                opinionPoll_listmodel.setPoll_end_date(poll_end_date);
                                opinionPoll_listmodel.setPoll_image(poll_image);
                                opinionPoll_listmodel.setPoll_option_1(poll_option_1);
                                opinionPoll_listmodel.setPoll_option_2(poll_option_2);
                                opinionPoll_listmodel.setPoll_option_3(poll_option_3);
                                opinionPoll_listmodel.setPoll_option_4(poll_option_4);
                                opinionPoll_listmodel.setPoll_option_5(poll_option_5);
                                opinionPoll_listmodel.setPoll_option_6(poll_option_6);
                                opinionPoll_listmodel.setUser_name(user_name);
                                opinionPoll_listmodel.setUser_profile_image(user_profile_image);
                                opinionPoll_listmodel.setShared_with(shared_with);
                                opinionPoll_listmodel.setCreated_by(created_by);
                                opinionPoll_listmodel.setCreated_on(created_on);
                                opinionPoll_listmodel.setStatus(status);
                                opinionPoll_listmodel.setPoll_image_url(poll_image_url);
                                opinionPoll_listmodel.setShare_result(share_result);

                                opinionpoll_list.add(opinionPoll_listmodel);


                            }

                            adapter = new OpinionpolllistAdapter(context, opinionpoll_list, SocietyId);

                            recyclerView.setAdapter(adapter);

                            adapter.notifyDataSetChanged();
                        }

                        if (status_code == 1) {
                            pollss.setVisibility(View.VISIBLE);
                        }

                }

                if (account_status == 0) {
                    Intent intent = new Intent(context, Login.class);
                    appPreferences.remove("user_id");
                    appPreferences.remove("user_name");
                    appPreferences.remove("email_id");
                    appPreferences.remove("storecoverimage");
                    appPreferences.remove("storeimage");
                    appPreferences.remove("profile_image_url");
                    Common.internet_check = 0;
                    startActivity(intent);
                    ((Activity) context).finish();
                }


            } catch(JSONException e){
                e.printStackTrace();
            }
            mDialog.dismiss();


        }
        }

    }




}
