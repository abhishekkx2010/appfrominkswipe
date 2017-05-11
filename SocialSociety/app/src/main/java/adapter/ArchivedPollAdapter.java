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
import com.inkswipe.SocialSociety.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import model.ArchivedPolllistmodel;
import util.LoggerGeneral;

/**
 * Created by Ajinkya.Deshpande on 9/28/2016.
 */
public class ArchivedPollAdapter  extends RecyclerView.Adapter<ArchivedPollAdapter.ViewHolder>

{
    public final int TYPE_MOVIE = 0;
    public final int TYPE_LOAD = 1;
    public final int TYPE_ADD = 2;
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    Context context;
    List<ArchivedPolllistmodel> archivedpollist;
    private DisplayImageOptions options,options1;
    public ArchivedPollAdapter(Context context, List < ArchivedPolllistmodel > archivedpollist) {

        this.context = context;
        this.archivedpollist = archivedpollist;

    }

    @Override
    public ViewHolder onCreateViewHolder (ViewGroup parent,int viewType){
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.archivedpol_listrow, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);


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
    public void onBindViewHolder (ViewHolder holder,int position){
        final ArchivedPolllistmodel list = archivedpollist.get(position);
        //   holder.imageView.setImageBitmap(list.getImage());

        //    holder.imageView.setImageResource(R.mipmap.ic_launcher);

        holder.name.setText(list.getUser_name());
        holder.subject.setText(list.getPoll_question());
        holder.date.setText(list.getCreated_on());
        holder.enddate.setText(list.getPoll_end_date());
        holder.title.setText(list.getPoll_title());
        ImageLoader.getInstance().displayImage(list.getUser_profile_image(), holder.imageView, options1, animateFirstListener);

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (list.getShare_result().equalsIgnoreCase("0")) {


                    LoggerGeneral.info("insharezero"+list.getShare_result());
                    Intent intent = new Intent(context, ArchivedClosedPolls.class);
                    intent.putExtra("view", "view");
                    intent.putExtra("goback","arch");
                    intent.putExtra("shareresult", "zero");
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("pollId", list.getId());
                    intent.putExtra("SocietyId", list.getSociety_id());
                    context.startActivity(intent);
                    ((Activity) context).finish();
                }
                else {
                    LoggerGeneral.info("inshareone"+list.getShare_result());

                    Intent intent = new Intent(context, ArchivedClosedPolls.class);
                    intent.putExtra("view", "view");
                    intent.putExtra("goback","arch");
                    intent.putExtra("shareresult", "one");
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("pollId", list.getId());
                    intent.putExtra("SocietyId", list.getSociety_id());
                    context.startActivity(intent);
                    ((Activity) context).finish();
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
    public int getItemCount () {
        return archivedpollist.size();

    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView name, subject,title;
        public TextView date, enddate;

        public LinearLayout linearagendar;
        public ImageView backimage,imageView;


        public Button cancel;
        public LinearLayout linearLayout;

        public ViewHolder(View itemView) {
            super(itemView);


            name  = (TextView)itemView.findViewById(R.id.name);
            date = (TextView) itemView.findViewById(R.id.date);
            enddate = (TextView) itemView.findViewById(R.id.enddate);
            subject = (TextView) itemView.findViewById(R.id.subject);

            title = (TextView)itemView.findViewById(R.id.title);
            linearLayout = (LinearLayout)itemView.findViewById(R.id.linear);
            imageView = (ImageView)itemView.findViewById(R.id.image);

        }

    }


}
