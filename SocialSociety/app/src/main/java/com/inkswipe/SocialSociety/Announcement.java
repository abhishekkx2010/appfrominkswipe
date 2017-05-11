package com.inkswipe.SocialSociety;


import android.annotation.SuppressLint;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import adapter.AnnouncementAdapter;
import model.AnnouncementModel;
import util.AppPreferences;
import util.Common;
import util.Constants;
import util.LoggerGeneral;
import util.ServiceFacade;


/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class Announcement extends Fragment {





    RecyclerView recyclerView;
    List<AnnouncementModel> announcementModels;
    AnnouncementAdapter adapter;
    Context context;
    AppPreferences appPreferences;
    LinearLayout linearLayout;

    TextView announcement;
    String SocietyId;
    public  static  getMyAnnouncement getMyAnnouncement ;
    String annoucementId,society_idAnnouncement,announcement_title,description,announcement_image,created_by,created_on,status,user_name,days_pass,announcement_images_url;
    @SuppressLint("ValidFragment")
    public Announcement(String SocietyId) {

        this.SocietyId = SocietyId;


        LoggerGeneral.info("socId---"+SocietyId);

        // Required empty public constructor
    }

    public Announcement( ) {


        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_announcement, container, false);
        appPreferences = AppPreferences.getAppPreferences(getActivity());
        context=getActivity();
        recyclerView = (RecyclerView)rootView.findViewById(R.id.recycler_view);

        announcement = (TextView)rootView.findViewById(R.id.announcement);
        linearLayout= (LinearLayout) rootView.findViewById(R.id.create_announcement);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoggerGeneral.info("socId---"+SocietyId);
                Intent intent=new Intent(getActivity(),CreateAnnouncement.class);
                intent.putExtra("SocietyId",SocietyId);
                Common.internet_check=0;
                startActivity(intent);
                getActivity().finish();
            }
        });
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        announcementModels= new ArrayList<AnnouncementModel>();
       /* for(int i=0;i<=5;i++)
        {
            AnnouncementModel announcementModel=new AnnouncementModel();
            announcementModel.setTitle("Krishna Janmashtami");
            announcementModel.setCreator("created by Ramesh Kumar");
            announcementModel.setTime("2 days ago");
            announcementModel.setDescription("We request all society member to join them in the will be .....");

            announcementModels.add(announcementModel);
        }*/


        if(Common.isOnline(context)){
            getMyAnnouncement = new getMyAnnouncement();
            getMyAnnouncement.execute();
            //    new getMyAnnouncement().execute();
        }
        else {
            Common.showToast(context,"No internet connection!");
        }





        return rootView;
    }



    class  getMyAnnouncement extends AsyncTask<String, String, JSONObject> {
        String type;
        ProgressDialog mDialog;
        @Override
        protected JSONObject doInBackground(String... params) {
            // TODO Auto-generated method stub

            String url= Constants.Base+Constants.GetAnnouncement;

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
        protected void onCancelled(){

            super.onCancelled();
            mDialog.dismiss();
            LoggerGeneral.info("onCancelled");

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
                            announcementModels= new ArrayList<AnnouncementModel>();
                            for (int i = data.length() - 1; 0 <= i; i--) {

                                AnnouncementModel announcementModel=new AnnouncementModel();

                                JSONObject jsonObject1 = data.getJSONObject(i);
                                annoucementId=jsonObject1.getString("id");

                                society_idAnnouncement=jsonObject1.getString("society_id");
                                announcement_title=jsonObject1.getString("announcement_title");
                                description=jsonObject1.getString("description");
                                announcement_image=jsonObject1.getString("announcement_image");
                                created_by=jsonObject1.getString("created_by");
                                created_on=jsonObject1.getString("created_on");
                                status=jsonObject1.getString("status");
                                user_name=jsonObject1.getString("user_name");
                                days_pass=jsonObject1.getString("days_pass");
                                announcement_images_url=jsonObject1.getString("announcement_images_url");


                                announcementModel.setAnnoucementId(annoucementId);
                                announcementModel.setSociety_idAnnouncement(society_idAnnouncement);
                                announcementModel.setAnnouncement_title(announcement_title);
                                announcementModel.setDescription(description);
                                announcementModel.setAnnouncement_image(announcement_image);
                                announcementModel.setCreated_by(created_by);
                                announcementModel.setCreated_on(created_on);
                                announcementModel.setStatus(status);
                                announcementModel.setUser_name(user_name);
                                announcementModel.setDays_pass(days_pass);
                                announcementModel.setAnnouncement_images_url(announcement_images_url);

                                announcementModels.add(announcementModel);

                                LoggerGeneral.info("s_societies000---" + announcementModels.size());

                            }


                            LoggerGeneral.info("s_societies---" + announcementModels);
                            adapter=new AnnouncementAdapter(context,announcementModels);
                            recyclerView.setAdapter(adapter);

                            adapter.notifyDataSetChanged();


                        }

                        if (status_code == 1) {


                            announcement.setVisibility(View.VISIBLE);



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
                        Common.internet_check=0;
                        startActivity(intent);
                        getActivity().finish();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

        }

    }

    public void call()
    {
        if(Common.isOnline(context)) {

            new getMyAnnouncement().execute();
        } else {
            Common.showToast(context,"No internet connection");
        }
    }

}
