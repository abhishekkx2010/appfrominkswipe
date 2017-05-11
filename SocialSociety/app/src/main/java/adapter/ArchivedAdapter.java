package adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.inkswipe.SocialSociety.EventDetails;
import com.inkswipe.SocialSociety.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.util.List;

import model.Archivedlistmodel;
import util.AppPreferences;

/**
 * Created by rohit.gaikwad on 9/20/2016.
 */
public class ArchivedAdapter   extends RecyclerView.Adapter<ArchivedAdapter.ViewHolder>

    {
        public final int TYPE_MOVIE = 0;
        public final int TYPE_LOAD = 1;
        public final int TYPE_ADD = 2;
        private DisplayImageOptions options;
        Context context;
        List<Archivedlistmodel> archivedlist;
        String month;
        int monthCheck=1;
        AppPreferences appPreferences;
        String SocietyId;
        String event;
        String dateOfevent;
        public ArchivedAdapter(Context context, List < Archivedlistmodel > eventlist,String month,String SocietyId,String event,String dateOfevent) {

        this.context = context;
        this.archivedlist = eventlist;
            this.month=month;
            this.SocietyId=SocietyId;
            this.event=event;
            this.dateOfevent=dateOfevent;
            appPreferences = AppPreferences.getAppPreferences(context);

    }

        @Override
        public ViewHolder onCreateViewHolder (ViewGroup parent,int viewType){
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.archivedlistrow, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);


        return viewHolder;
    }

        @Override
        public void onBindViewHolder (ViewHolder holder,int position){
        final Archivedlistmodel list = archivedlist.get(position);
        //   holder.imageView.setImageBitmap(list.getImage());

        //    holder.imageView.setImageResource(R.mipmap.ic_launcher);

        if (month.equals(list.getMonth()))
        {
            if(monthCheck==1) {
                monthCheck = 0;
                holder.eventofmonth.setText(list.getMonth());
            }
            else {
                holder.eventofmonth.setVisibility(View.GONE);
                holder.eventofmonthTitle.setVisibility(View.GONE);
            }
        }
            else {
            month=list.getMonth();
            holder.eventofmonth.setText(list.getMonth());
        }



            holder.textViewName.setText(list.getEtitle());
            holder.date.setText(list.getsDate());
            holder.createdby.setText(list.getEuser_name());
            holder.time.setText(list.getTtime());
            //holder.address.setText(list.getEstate()+" "+list.getEaddress()+" "+list.getElandmark()+"\n"+list.getEcity()+"-"+list.getEpostal_code());
            holder.address.setText(list.getEdescription());
            holder.archivedMain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent evetDetails=new Intent(context, EventDetails.class);

                        evetDetails.putExtra("archiveEvent","archiveEvent");
                        evetDetails.putExtra("creator","creator");
                        evetDetails.putExtra("event_id",list.getEid());
                        evetDetails.putExtra("SocietyId",list.getEsociety_id());
                        evetDetails.putExtra("dateofevent",dateOfevent);
                        evetDetails.putExtra("event",event);
                        evetDetails.putExtra("time",list.getTtime());
                        context.startActivity(evetDetails);
                    ((Activity)context).finish();

                }
            });

    }


        @Override
        public int getItemCount () {
        return archivedlist.size();

    }

        class ViewHolder extends RecyclerView.ViewHolder {

            public TextView textViewName, eventofmonth;
            public TextView date, createdby;
            public TextView time, address,eventofmonthTitle;

            public LinearLayout linearagendar;
            public RelativeLayout backimage;

            LinearLayout archivedMain;


            public Button cancel;

            public ViewHolder(View itemView) {
                super(itemView);

                eventofmonth = (TextView) itemView.findViewById(R.id.eventofmonth);
                textViewName = (TextView) itemView.findViewById(R.id.eventname);
                date = (TextView) itemView.findViewById(R.id.date);
                createdby = (TextView) itemView.findViewById(R.id.createdby);
                time = (TextView) itemView.findViewById(R.id.time);
                address = (TextView) itemView.findViewById(R.id.address);
                eventofmonthTitle= (TextView) itemView.findViewById(R.id.eventofmonthTitle);
                archivedMain= (LinearLayout) itemView.findViewById(R.id.archivedMain);


            }

        }


}
