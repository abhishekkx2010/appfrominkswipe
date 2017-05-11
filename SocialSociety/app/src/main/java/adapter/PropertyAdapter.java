package adapter;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.inkswipe.SocialSociety.AddSociety;
import com.inkswipe.SocialSociety.ArchivedPolls;
import com.inkswipe.SocialSociety.Archivedevent;
import com.inkswipe.SocialSociety.Complaint;
import com.inkswipe.SocialSociety.CreateAnnouncement;
import com.inkswipe.SocialSociety.CreateEvent;
import com.inkswipe.SocialSociety.CreatePoll;
import com.inkswipe.SocialSociety.DashBoard;
import com.inkswipe.SocialSociety.EditProperty;
import com.inkswipe.SocialSociety.Login;
import com.inkswipe.SocialSociety.Members;
import com.inkswipe.SocialSociety.MyProperty;
import com.inkswipe.SocialSociety.NotificationApp;
import com.inkswipe.SocialSociety.Offer;
import com.inkswipe.SocialSociety.Profile;
import com.inkswipe.SocialSociety.R;
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

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import model.PropertyModel;
import util.AppPreferences;
import util.Common;
import util.Constants;
import util.LoggerGeneral;
import util.ServiceFacade;

/**
 * Created by Ajinkya.Deshpande on 9/17/2016.
 */
public class PropertyAdapter extends  RecyclerView.Adapter<PropertyAdapter.ViewHolder> {

    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

    Context context;
    AppPreferences appPreferences;
    String session_status,pending_sessions,date_of_session;
    int is_package_available;
    String latlog,longitudelog;
    List<PropertyModel> britems;
    public static int intentCheck;
    Dialog dialog;
    DisplayImageOptions options;
    public PropertyAdapter(Context context,List<PropertyModel>britems) {

        this.context = context;
        this.britems=britems;

        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.cover_image)
                .showImageForEmptyUri(R.mipmap.cover_image)
                .showImageOnFail(R.mipmap.cover_image)
                .cacheInMemory(false)
                .cacheOnDisk(false)
                .considerExifParams(true)
                .displayer(new SimpleBitmapDisplayer())
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context).build();
        ImageLoader.getInstance().init(config);
        dialog = null;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.society_row, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);

        appPreferences = AppPreferences.getAppPreferences(context);
        //   new getSessionStatus().execute();


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        final PropertyModel list2 = britems.get(position);
        //   holder.imageView.setImageBitmap(list.getImage());

        //    holder.imageView.setImageResource(R.mipmap.ic_launcher);


        holder.imgedit.setVisibility(View.VISIBLE);

        holder.textViewName.setText(list2.getProperty_name() +", "+ list2.getProperty_type()+"-"+list2.getHouse_no()+", " + list2.getSocietyName() + ", " + list2.getCityName());

        holder.backimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (intentCheck) {
                    case 1:
                        LoggerGeneral.info("1");
                        Intent profile = new Intent(context, Profile.class);
                        profile.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        Common.internet_check = 0;
                        intentCheck=0;
                        context.startActivity(profile);
                        ((Activity) context).finish();
                        break;
                    case 2:
                        LoggerGeneral.info("2");
                        Intent addSociety = new Intent(context, AddSociety.class);
                        intentCheck=0;
                        Common.internet_check = 0;
                        context.startActivity(addSociety);
                        ((Activity) context).finish();
                        break;
                    case 3:
                        LoggerGeneral.info("3");
                        Intent myProperty = new Intent(context, MyProperty.class);
                        intentCheck=0;
                        Common.internet_check = 0;
                        context.startActivity(myProperty);
                        ((Activity) context).finish();
                        break;
                    case 4:
                        LoggerGeneral.info("4");
                        Intent events = new Intent(context, DashBoard.class);
                        Common.internet_check = 0;


                        intentCheck=0;
                        events.putExtra("event", 3);
                        events.putExtra("SocietyId", list2.getSocietyId());
                        LoggerGeneral.info("inproperty----"+list2.getSocietyName());

                        appPreferences.putString("societynamedash",list2.getSocietyName());
                        context.startActivity(events);
                        ((Activity) context).finish();
                        break;
                    case 5:
                        LoggerGeneral.info("5");
                        Intent createEvents = new Intent(context, CreateEvent.class);
                        Common.internet_check = 0;
                        intentCheck=0;
                        createEvents.putExtra("SocietyId", list2.getSocietyId());
                        context.startActivity(createEvents);
                        ((Activity) context).finish();
                        break;
                    case 6:
                        LoggerGeneral.info("6");
                        Intent archivedEvents = new Intent(context, Archivedevent.class);
                        Common.internet_check = 0;
                        intentCheck=0;
                        archivedEvents.putExtra("SocietyId", list2.getSocietyId());
                        context.startActivity(archivedEvents);
                        ((Activity) context).finish();
                        break;
                    case 7:
                        LoggerGeneral.info("7");
                        Intent announcement = new Intent(context, DashBoard.class);
                        Common.internet_check = 0;
                        intentCheck=0;
                        announcement.putExtra("SocietyId", list2.getSocietyId());
                        announcement.putExtra("announcement", 4);
                        context.startActivity(announcement);
                        ((Activity) context).finish();
                        break;
                    case 8:
                        LoggerGeneral.info("8");
                        Intent createAnnouncement = new Intent(context, CreateAnnouncement.class);
                        Common.internet_check = 0;
                        intentCheck=0;
                        createAnnouncement.putExtra("SocietyId", list2.getSocietyId());
                        context.startActivity(createAnnouncement);
                        ((Activity) context).finish();
                        break;
                    case 9:
                        LoggerGeneral.info("9");
                        Intent polls = new Intent(context, DashBoard.class);
                        Common.internet_check = 0;
                        intentCheck=0;
                        polls.putExtra("SocietyId", list2.getSocietyId());
                        polls.putExtra("polls", 2);
                        context.startActivity(polls);
                        ((Activity) context).finish();
                        break;
                    case 10:
                        LoggerGeneral.info("10");
                        Intent createEventPoll = new Intent(context, CreatePoll.class);
                        Common.internet_check = 0;
                        intentCheck=0;
                        createEventPoll.putExtra("SocietyId", list2.getSocietyId());
                        context.startActivity(createEventPoll);
                        ((Activity) context).finish();
                        break;
                    case 11:
                        LoggerGeneral.info("11");
                        Intent archivedEventPoll = new Intent(context, ArchivedPolls.class);
                        Common.internet_check = 0;
                        intentCheck=0;
                        archivedEventPoll.putExtra("SocietyId", list2.getSocietyId());
                        context.startActivity(archivedEventPoll);
                        ((Activity) context).finish();
                        break;
                    case 12:
                        LoggerGeneral.info("12");
                        Intent members = new Intent(context, Members.class);
                        Common.internet_check = 0;
                        intentCheck=0;
                        members.putExtra("SocietyId", list2.getSocietyId());
                        context.startActivity(members);
                        ((Activity) context).finish();
                        break;
                    case 13:
                        LoggerGeneral.info("13");
                        Intent offers = new Intent(context, Offer.class);
                        Common.internet_check = 0;
                        intentCheck=0;
                        offers.putExtra("SocietyId", list2.getSocietyId());
                        context.startActivity(offers);
                        ((Activity) context).finish();
                        break;
                    case 14:
                        LoggerGeneral.info("14");
                        Intent complaint = new Intent(context, Complaint.class);
                        Common.internet_check = 0;
                        intentCheck=0;
                        complaint.putExtra("SocietyId", list2.getSocietyId());
                        context.startActivity(complaint);
                        ((Activity) context).finish();
                        break;
                    case 15:
                        LoggerGeneral.info("15");
                        Intent notification = new Intent(context, NotificationApp.class);
                        Common.internet_check = 0;
                        intentCheck=0;
                        context.startActivity(notification);
                        ((Activity) context).finish();
                        break;
                    case 16:

                        intentCheck=0;
                        LoggerGeneral.info("16");
                        break;
                    default:
                        LoggerGeneral.info("15");
                        Intent DashBoard = new Intent(context, DashBoard.class);
                        Common.internet_check = 0;
                        intentCheck=0;
                        DashBoard.putExtra("SocietyId", list2.getSocietyId());
                        LoggerGeneral.info("inproperty----"+list2.getSocietyName());

                        appPreferences.putString("societynamedash",list2.getSocietyName());
                        context.startActivity(DashBoard);
                        ((Activity) context).finish();
                        break;


                }
            }
        });


        LoggerGeneral.info("showpropimg--" + list2.getProperty_image());
        if(holder.backimage!=null){
            holder.backimage.invalidate();
        }

        ImageLoader.getInstance().displayImage(list2.getProperty_image(), holder.backimage, options, animateFirstListener);
        holder.imgedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent edit = new Intent(context, EditProperty.class);
                Common.internet_check=0;
                context.startActivity(edit);
                ((Activity) context).finish();
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final String property_id = list2.getId();

                dialog = new Dialog(context);
                dialog.setContentView(R.layout.deletepropertypopup);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

                LinearLayout yes = (LinearLayout) dialog.findViewById(R.id.yes);

                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (Common.isOnline(context)) {

                            new deleteProp(property_id, position, list2).execute();
                        } else {
                            Common.showToast(context, "No internet connection");
                        }
                    }
                });

                LinearLayout no = (LinearLayout) dialog.findViewById(R.id.no);

                no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });


                dialog.show();


            }
        });




        holder.citytxt.setVisibility(View.GONE);
        LoggerGeneral.info("getname---" + list2.getName());



        holder.imgedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Common.isOnline(context)) {
                    Intent intent = new Intent(context, EditProperty.class);
                    intent.putExtra("property_id", list2.getId());
                    intent.putExtra("property_name", list2.getProperty_name());
                    intent.putExtra("property_type", list2.getProperty_type());
                    intent.putExtra("user_type", list2.getUser_type());
                    intent.putExtra("house_no", list2.getHouse_no());
                    intent.putExtra("is_available_for_rent", list2.getIs_available_for_rent());
                    intent.putExtra("rent_availability_date", list2.getRent_availability_date());
                    intent.putExtra("status", list2.getStatus());
                    intent.putExtra("created_on", list2.getCreated_on());
                    intent.putExtra("propertyimage_url", list2.getProperty_image());

                    Common.internet_check = 0;
                    context.startActivity(intent);
                    ((Activity) context).finish();
                }
                else
                {
                    Common.showToast(context,"No internet connection");
                }
            }
        });

      //  holder.societynametxt.setVisibility(View.VISIBLE);
      //  holder.societynametxt.setText(","+list2.getSocietyName());
      //  holder.societycitytxt.setVisibility(View.VISIBLE);
      //  holder.societycitytxt.setText(","+list2.getCityName());
    }

    class  deleteProp extends AsyncTask<String, String, JSONObject> {
        String type;
        ProgressDialog mDialog;
        int position;
        PropertyModel list2;
        String property_id;
        public deleteProp(String property_id,int position,PropertyModel list2){
            this.property_id =property_id;
            this.position=position;
            this.list2=list2;

        }


        @Override
        protected JSONObject doInBackground(String... params) {
            // TODO Auto-generated method stub

            String url= Constants.Base+Constants.DeleteProperty;

            JSONObject object = new JSONObject();
            try {

                object.put("user_id",appPreferences.getString("user_id",""));
                object.put("property_id",property_id);

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
                            dialog.dismiss();

                            removeAt(position, list2);

                            JSONArray jsonArray = results.getJSONArray("data");


                            LoggerGeneral.info("showjarray---" + jsonArray);


                            appPreferences.putInt("property_count", appPreferences.getInt("property_count", 0) - 1);

                            LoggerGeneral.info("count after add property22" + appPreferences.getInt("property_count", 0));



                            Common.showToast(context,"Property removed successfully!");

                            if(appPreferences.getInt("property_count", 0)==0)
                            {
                                Intent profile=new Intent(context,Profile.class);
                                profile.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                Common.internet_check=0;
                                context.startActivity(profile);
                                ((Activity)context).finish();
                            }

                        }

                        if (status_code == 1) {

                            dialog.dismiss();


                            Common.showToast(context, "Could not remove property!");

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
                        context.startActivity(intent);
                        ((Activity) context).finish();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

        }

    }

    public  void removeAt(int position, PropertyModel propertyModel) {
        int id=propertyModel.getIdd();
        britems.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, britems.size());
        MyProperty.removeItem(id);
        LoggerGeneral.info("Hiiiii"+position+"===="+britems.size());
    }


    @Override
    public int getItemCount() {

        //   return items.size();
        return britems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView,imgedit;
        public TextView textViewName,citytxt,societynametxt,societycitytxt;
        public TextView date,month;
        public TextView datecita;
        public ImageView cal;
        public LinearLayout linearagendar;
        public ImageView backimage;
        RelativeLayout delete;


        public Button cancel;



        public ViewHolder(View itemView) {
            super(itemView);
            backimage    = (ImageView) itemView.findViewById(R.id.backimg);
            textViewName = (TextView) itemView.findViewById(R.id.txt);
            imgedit = (ImageView)itemView.findViewById(R.id.editimage);
            delete = (RelativeLayout) itemView.findViewById(R.id.delete);
            citytxt = (TextView)itemView.findViewById(R.id.citytxt);
            societycitytxt = (TextView)itemView.findViewById(R.id.societycitytxt);
            societynametxt  = (TextView)itemView.findViewById(R.id.societynametxt);




        }




    }
    private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

        static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            if (loadedImage != null) {
                ImageView imageView = (ImageView) view;
                boolean firstDisplay = !displayedImages.contains(imageUri);
                if (firstDisplay) {
                    FadeInBitmapDisplayer.animate(imageView, 500);
                    displayedImages.add(imageUri);
                }
            }
        }
    }



}