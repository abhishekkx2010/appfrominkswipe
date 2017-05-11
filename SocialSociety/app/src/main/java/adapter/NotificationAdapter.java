package adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.inkswipe.SocialSociety.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import model.NotificationModel;
import util.Common;
import util.LoggerGeneral;

/**
 * Created by Ajinkya on 9/17/2016.
 */
public class NotificationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    static Context context;
    List<NotificationModel> notificationModels;
    static String SocietyId;
    public NotificationAdapter(Context context, List<NotificationModel> notificationModels,String SocietyId) {
        this.context = context;
        this.notificationModels = notificationModels;
        this.SocietyId=SocietyId;

        LoggerGeneral.info("notifysize"+notificationModels.size());



    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return new NotificationHolder(inflater.inflate(R.layout.notification_row,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {


        ((NotificationHolder)holder).bindData(notificationModels.get(position));
    }

    @Override
    public int getItemCount() {
        return notificationModels.size();
    }

    static class NotificationHolder extends RecyclerView.ViewHolder{
        private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
        private DisplayImageOptions options;
        TextView user,time,date,description,societyName;
        LinearLayout notificationmain;
        public NotificationHolder(View itemView) {
            super(itemView);

            options = new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.mipmap.cover_image)
                    .showImageForEmptyUri(R.mipmap.cover_image)
                    .showImageOnFail(R.mipmap.cover_image)
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .considerExifParams(true)
                    .displayer(new SimpleBitmapDisplayer())
                    .build();
            user= (TextView) itemView.findViewById(R.id.userName);
            time= (TextView) itemView.findViewById(R.id.time);
            date= (TextView) itemView.findViewById(R.id.date);
            description= (TextView) itemView.findViewById(R.id.description);
            notificationmain= (LinearLayout) itemView.findViewById(R.id.notificationmain);
            societyName = (TextView)itemView.findViewById(R.id.societyName);


        }

        void bindData(final NotificationModel notificationModel){
            user.setText(notificationModel.getUser_name());
            time.setText(notificationModel.getNtime());
            date.setText(notificationModel.getNdate());
            societyName.setText(notificationModel.getSocietyName());
        //    description.setText(" "+notificationModel.getDescription());
            description.setText(notificationModel.getNotification_message());

            notificationmain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(Common.isOnline(context)) {

                        if (notificationModel.getEvent_type().equals("1")) {
                            Intent DashBoard = new Intent(context, com.inkswipe.SocialSociety.DashBoard.class);
                            DashBoard.putExtra("fromNotification", "fromNotification");
                            DashBoard.putExtra("SocietyId", notificationModel.getSsociety_d());
                            DashBoard.putExtra("SocietyIdnotification", SocietyId);
                            context.startActivity(DashBoard);
                            ((Activity) context).finish();
                        } else if (notificationModel.getEvent_type().equals("2")) {
                            Intent DashBoard = new Intent(context, com.inkswipe.SocialSociety.PollDetails.class);
                            DashBoard.putExtra("fromNotification", "fromNotification");
                            DashBoard.putExtra("poll_id", notificationModel.getEvent_id());
                            DashBoard.putExtra("SocietyId", notificationModel.getSsociety_d());
                            DashBoard.putExtra("SocietyIdnotification", SocietyId);
                            context.startActivity(DashBoard);
                            ((Activity) context).finish();
                        } else if (notificationModel.getEvent_type().equals("3")) {


                            Intent DashBoard = new Intent(context, com.inkswipe.SocialSociety.EventDetails.class);
                            DashBoard.putExtra("fromNotification", "fromNotification");
                            DashBoard.putExtra("event_id", notificationModel.getEvent_id());
                            DashBoard.putExtra("SocietyId", notificationModel.getSsociety_d());
                            DashBoard.putExtra("SocietyIdnotification", SocietyId);
                            context.startActivity(DashBoard);
                            ((Activity) context).finish();
                        } else if (notificationModel.getEvent_type().equals("4")) {
                            Intent DashBoard = new Intent(context, com.inkswipe.SocialSociety.DashBoard.class);
                            DashBoard.putExtra("fromNotification", "fromNotification");
                            DashBoard.putExtra("announcement", 4);
                            DashBoard.putExtra("SocietyId", notificationModel.getSsociety_d());
                            DashBoard.putExtra("SocietyIdnotification", SocietyId);
                            context.startActivity(DashBoard);
                            ((Activity) context).finish();
                        }
                    }
                    else
                    {
                        Common.showToast(context,"No internet connection");
                    }
                }
            });
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
