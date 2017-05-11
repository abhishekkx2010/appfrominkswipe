package adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.inkswipe.SocialSociety.EventDetails;
import com.inkswipe.SocialSociety.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import model.EventListmodel;
import util.AppPreferences;
import util.Common;

/**
 * Created by Ajinkya on 9/19/2016.
 */
public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.ViewHolder> {
    public final int TYPE_MOVIE = 0;
    public final int TYPE_LOAD = 1;
    public final int TYPE_ADD = 2;
    private DisplayImageOptions options;
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    Context context;
    List<EventListmodel> eventlist;
    AppPreferences appPreferences;
    String event;
    String dateofevent;
    public EventListAdapter(Context context,List<EventListmodel>eventlist,String event,String dateofevent) {

        this.context = context;
        this.eventlist=eventlist;
        this.event=event;
        this.dateofevent=dateofevent;

        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.cover_image)
                .showImageForEmptyUri(R.mipmap.cover_image)
                .showImageOnFail(R.mipmap.cover_image)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .displayer(new SimpleBitmapDisplayer())
                .build();

        appPreferences = AppPreferences.getAppPreferences(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.eventlistrow, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);



        return  viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final EventListmodel list = eventlist.get(position);
        //   holder.imageView.setImageBitmap(list.getImage());

        //    holder.imageView.setImageResource(R.mipmap.ic_launcher);




        holder.textViewName.setText(list.getEtitle());
        holder.date.setText(list.getsDate());
        holder.createdby.setText(list.getEuser_name());
        holder.description.setText(list.getEdescription());
        holder.daysago.setText(list.getDays_pass());

        if(list.getEevent_image_url().length()>0) {
            holder.imaget.setVisibility(View.VISIBLE);
            ImageLoader.getInstance().displayImage(list.getEevent_image_url(), holder.imaget, options, animateFirstListener);
        }
        else {
            holder.imaget.setVisibility(View.GONE);
        }

        holder.time.setText(list.getAt());

        holder.eventListMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Common.internet_check=0;
                Intent evetDetails=new Intent(context, EventDetails.class);
                if(list.getEcreated_by()==appPreferences.getString("user_id","")||list.getEcreated_by().equals(appPreferences.getString("user_id","")))
                {
                    evetDetails.putExtra("creator","creator");
                    evetDetails.putExtra("event_id",list.getEid());
                    evetDetails.putExtra("SocietyId",list.getEsociety_id());
                    evetDetails.putExtra("dateofevent",dateofevent);
                    evetDetails.putExtra("event",event);
                    evetDetails.putExtra("time",list.getAt());
                }
                else {
                    evetDetails.putExtra("eventList", "eventList");
                    evetDetails.putExtra("event_id",list.getEid());
                    evetDetails.putExtra("SocietyId",list.getEsociety_id());
                    evetDetails.putExtra("dateofevent",dateofevent);
                    evetDetails.putExtra("event",event);
                    evetDetails.putExtra("time",list.getAt());

                }context.startActivity(evetDetails);
                ((Activity)context).finish();
            }
        });


    }



    @Override
    public int getItemCount() {
        return eventlist.size();

    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textViewName;
        public TextView date,createdby;
        public TextView description;
        public TextView daysago,time;
        ImageView imaget;
        public LinearLayout linearagendar;
        public RelativeLayout backimage;

        LinearLayout eventListMain;


        public Button cancel;

        public ViewHolder(View itemView) {
            super(itemView);

            textViewName = (TextView) itemView.findViewById(R.id.eventname);
            date = (TextView)itemView.findViewById(R.id.date);
            createdby = (TextView)itemView.findViewById(R.id.createdby);
            description  = (TextView)itemView.findViewById(R.id.description);
            eventListMain= (LinearLayout) itemView.findViewById(R.id.eventListMain);
            daysago = (TextView)itemView.findViewById(R.id.daysago);
            imaget= (ImageView) itemView.findViewById(R.id.imaget);
            time = (TextView) itemView.findViewById(R.id.time);


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
