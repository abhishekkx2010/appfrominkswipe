package adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.inkswipe.SocialSociety.ArchivedClosedPolls;
import com.inkswipe.SocialSociety.ArchivedPolls;
import com.inkswipe.SocialSociety.PollDetails;
import com.inkswipe.SocialSociety.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import model.OpinionPoll_listmodel;
import util.AppPreferences;
import util.Common;
import util.LoggerGeneral;

/**
 * Created by Ajinkya on 9/22/2016.
 */
public class OpinionpolllistAdapter extends  RecyclerView.Adapter<OpinionpolllistAdapter.ViewHolder> {

    Context context;
    AppPreferences appPreferences;
    List<OpinionPoll_listmodel> opinionpoll_list;
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    private DisplayImageOptions options,options1;
    String SocietyId;
    public OpinionpolllistAdapter(Context context,List<OpinionPoll_listmodel> opinionpoll_list,String SocietyId){
        this.context = context;
        this.opinionpoll_list = opinionpoll_list;
        this.SocietyId=SocietyId;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.opinionpollrow, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);

        appPreferences = AppPreferences.getAppPreferences(context);
        //   new getSessionStatus().execute();


        return viewHolder;
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

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final OpinionPoll_listmodel list = opinionpoll_list.get(position);


        holder.name.setText(list.getUser_name());
        holder.subject.setText(list.getPoll_question());
        holder.enddate.setText(list.getPoll_end_date());
        holder.createdBy.setText(list.getCreated_by());
        holder.title.setText(list.getPoll_title());
        holder.createdon.setText(list.getCreated_on());
        ImageLoader.getInstance().displayImage(list.getUser_profile_image(), holder.imageView, options1, animateFirstListener);

        holder.archived.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent archive = new Intent(context, ArchivedPolls.class);
                archive.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(archive);
                ((Activity) context).finish();
            }
        });


        holder.viewr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Common.isOnline(context)) {

                    if (list.getShare_result().equalsIgnoreCase("0")) {
                        Intent view = new Intent(context, PollDetails.class);
               /*   view.putExtra("creator","creator");

                     view.putExtra("view", "view"); */

                        if (appPreferences.getString("user_id", "").equals(list.getCreated_by()) || appPreferences.getString("user_id", "") == list.getCreated_by()) {
                            view.putExtra("creator", "creator");


                        } else {
                            view.putExtra("view", "view");
                        }

                        view.putExtra("poll_id", list.getId());
                        view.putExtra("SocietyId", SocietyId);
                        view.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(view);
                        ((Activity) context).finish();

                    }

                    else {


                        Intent intent = new Intent(context, ArchivedClosedPolls.class);

                        if (appPreferences.getString("user_id", "").equals(list.getCreated_by()) || appPreferences.getString("user_id", "") == list.getCreated_by()) {
                            intent.putExtra("creator", "creator");


                        } else {
                            intent.putExtra("view", "view");
                        }

                        LoggerGeneral.info("inone--"+list.getId());
                        intent.putExtra("shareresult", "one");
                        intent.putExtra("goback","dash");
                        intent.putExtra("poll_id", list.getId());
                        intent.putExtra("pollId",list.getId());
                        intent.putExtra("SocietyId", SocietyId);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                        ((Activity) context).finish();
                    }
                    } else {
                        Common.showToast(context, "No internet connection");
                    }
                }



        });

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.dpdialog);
                dialog.setCanceledOnTouchOutside(true);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


                ImageView dpImage = (ImageView) dialog.findViewById(R.id.dpimage);

                ImageLoader.getInstance().displayImage(list.getUser_profile_image(), dpImage, options, animateFirstListener);


                TextView profilenm = (TextView)dialog.findViewById(R.id.profilemnm);
                profilenm.setText(list.getUser_name());
                ImageView profileim = (ImageView)dialog.findViewById(R.id.profileim);

                profileim.setVisibility(View.GONE);

                dialog.show();
            }
        });
    }





    @Override
    public int getItemCount() {

        //   return items.size();
        return opinionpoll_list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView name,createdon;
        public TextView date,month;
        public TextView subject,title;

        public  TextView createdBy,enddate;
        public  LinearLayout archived,viewr;




        public Button cancel;

        public ViewHolder(View itemView) {
            super(itemView);

            name = (TextView)itemView.findViewById(R.id.mname);
            subject = (TextView)itemView.findViewById(R.id.subject);
            createdBy = (TextView)itemView.findViewById(R.id.createdby);
            enddate = (TextView)itemView.findViewById(R.id.endate);
            archived = (LinearLayout)itemView.findViewById(R.id.newarchived);
            viewr = (LinearLayout)itemView.findViewById(R.id.viewr);
            title = (TextView)itemView.findViewById(R.id.title);
            imageView = (ImageView)itemView.findViewById(R.id.mimage);
            createdon = (TextView)itemView.findViewById(R.id.createdon);

        }

    }

}
