package adapter;


import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.inkswipe.SocialSociety.GroupMembers;
import com.inkswipe.SocialSociety.Groups;
import com.inkswipe.SocialSociety.Login;
import com.inkswipe.SocialSociety.R;
import com.inkswipe.SocialSociety.ShareEvent;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import model.GroupsModel;
import util.AppPreferences;
import util.Common;
import util.Constants;
import util.LoggerGeneral;
import util.ServiceFacade;

/**
 * Created by Ajinkya.Deshpande on 9/17/2016.
 */
public class GroupAdapter extends  RecyclerView.Adapter<GroupAdapter.ViewHolder> {


    Context context;
    AppPreferences appPreferences;
    String session_status, pending_sessions, date_of_session;
    int is_package_available;
    String latlog, longitudelog;
    List<GroupsModel> britems;
    int check1;
    public static int check = 0;
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    private DisplayImageOptions options;
    String SocietyId;

    public GroupAdapter(Context context, List<GroupsModel> britems, int check1, String SocietyId) {

        this.context = context;
        this.britems = britems;
        this.check1 = check1;
        this.SocietyId = SocietyId;

        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.grfallb)
                .showImageForEmptyUri(R.mipmap.grfallb)
                .showImageOnFail(R.mipmap.grfallb)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .displayer(new SimpleBitmapDisplayer())
                .build();
        ShareEvent.shareList = new ArrayList<String>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.group_row, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);

        appPreferences = AppPreferences.getAppPreferences(context);
        //   new getSessionStatus().execute();


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final GroupsModel groupsModel = britems.get(position);

        if (britems.size() > 0){

            if (check1 == 0) {
                holder.delete.setVisibility(View.GONE);
            }

        if (groupsModel.isImageChanged()) {

            Drawable res = context.getResources().getDrawable(R.drawable.usertick);
            holder.tick.setImageDrawable(res);


        } else {
            holder.tick.setImageDrawable(null);
        }

        holder.groupName.setText(groupsModel.getGroupName());
        //holder.description.setText(groupsModel.getDescription());
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Common.isOnline(context)) {

                    new deleteGroup(britems.get(position).getGroupId()).execute();
                } else {
                    Common.showToast(context, "No internet connection!");
                }
                removeAt(position, groupsModel);
            }
        });
        if (check1 == 0) {
            if (groupsModel.isImageChanged()) {

                Drawable res = context.getResources().getDrawable(R.drawable.usertick);
                holder.tick.setImageDrawable(res);


            } else {
                holder.tick.setImageDrawable(null);
            }
        }
        if (check1 == 0) {
            ShareEvent.groupMemberCheck = 0;
            holder.userSelectLayout.setOnClickListener(new View.OnClickListener() {
                @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void onClick(View v) {

                    LoggerGeneral.info("Position of log1" + position);
                    GroupsModel groupModel = britems.get(position);


                    if (britems.get(position).isImageChanged()) {
                        holder.tick.setImageDrawable(null);
                        LoggerGeneral.info("Position of log2" + position);
                        britems.get(position).setIsImageChanged(false);
                        check--;
                        for (int i = 0; i <= ShareEvent.shareList.size() - 1; i++) {

                            if (ShareEvent.shareList.get(i) == britems.get(position).getGroupId()) {
                                ShareEvent.shareList.remove(i);
                                LoggerGeneral.info("shareEventMembverDisize" + (ShareEvent.shareList.size()));

                                synchronized (ShareEvent.shareList)

                                {

                                    ShareEvent.shareList.notify();

                                }
                            }
                        }
                        if (check == 0) {

                            final int sdk = android.os.Build.VERSION.SDK_INT;
                            if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                                ShareEvent.prPublish.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.fourborderlightgray));
                            } else {
                                ShareEvent.prPublish.setBackground(context.getResources().getDrawable(R.drawable.fourborderlightgray));
                            }
                        }
                    } else {
                        check++;
                        britems.get(position).setIsImageChanged(true);

                        LoggerGeneral.info("Position of log3" + position);
                        Drawable res = context.getResources().getDrawable(R.drawable.usertick);
                        holder.tick.setImageDrawable(res);
                        ShareEvent.shareList.add(britems.get(position).getGroupId());

                        LoggerGeneral.info("shareEventMembverDisize11" + (ShareEvent.shareList.size()));

                        final int sdk = android.os.Build.VERSION.SDK_INT;
                        if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                            ShareEvent.prPublish.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.fourborder));
                        } else {
                            ShareEvent.prPublish.setBackground(context.getResources().getDrawable(R.drawable.fourborder));
                        }
                    }
                }
            });
        }

        if (check1 == 1) {

            holder.userSelectLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent groupMembers = new Intent(context, GroupMembers.class);
                    groupMembers.putExtra("GroupId", britems.get(position).getGroupId());
                    groupMembers.putExtra("SocietyId", SocietyId);
                    context.startActivity(groupMembers);
                    ((Activity) context).finish();
                }
            });


        }

        ImageLoader.getInstance().displayImage(groupsModel.getGroupImageUrl(), holder.groupImage, options, animateFirstListener);


    }

        if(britems.size()==0){

            LoggerGeneral.info("check---"+britems.size());

            holder.nogroup.setVisibility(View.VISIBLE);
        }


    }


    public  void removeAt(int position, GroupsModel groupsModel) {
        int id=groupsModel.getIdd();
        britems.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, britems.size());
        Groups.removeItem(id);
        LoggerGeneral.info("Hiiiii"+position+"===="+britems.size());
    }


    @Override
    public int getItemCount() {

        //   return items.size();
        return britems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {


        TextView groupName,description,nogroup;
        public RelativeLayout delete;
        ImageView groupImage,tick;
        RelativeLayout userSelectLayout;

        public Button cancel;

        public ViewHolder(View itemView) {
            super(itemView);


            groupImage= (ImageView) itemView.findViewById(R.id.groupImage);
            groupName= (TextView) itemView.findViewById(R.id.groupName);
            description= (TextView) itemView.findViewById(R.id.groupDescription);
            delete= (RelativeLayout) itemView.findViewById(R.id.delete);
            userSelectLayout= (RelativeLayout) itemView.findViewById(R.id.userSelectLayout);
            tick= (ImageView) itemView.findViewById(R.id.tick);

            nogroup = (TextView)itemView.findViewById(R.id.nogroup);

        }




    }
    private class AnimateFirstDisplayListener extends SimpleImageLoadingListener {


        final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

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

    class  deleteGroup extends AsyncTask<String, String, JSONObject> {
        String type;
        ProgressDialog mDialog;

        String group_id;
        public deleteGroup(String group_id){
            this.group_id =group_id;
        }


        @Override
        protected JSONObject doInBackground(String... params) {
            // TODO Auto-generated method stub

            String url= Constants.Base+Constants.deleteGroup;

            JSONObject object = new JSONObject();
            try {

                object.put("group_id",group_id);
                object.put("user_id",appPreferences.getString("user_id",""));

                LoggerGeneral.info("JsonObjectPrintdeletegroup" + object.toString());

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

                            JSONArray jsonArray = results.getJSONArray("data");


                            LoggerGeneral.info("showjarray---" + jsonArray);


                            Common.showToast(context,"Group removed successfully!");

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
                        context.startActivity(intent);
                        ((Activity) context).finish();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

        }

    }



}