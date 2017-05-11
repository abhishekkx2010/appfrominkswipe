package adapter;


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
import android.widget.TextView;

import com.inkswipe.SocialSociety.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import model.AnnouncementModel;
import util.LoggerGeneral;

public class AnnouncementAdapter extends  RecyclerView.Adapter<AnnouncementAdapter.ViewHolder> {

    static Context context;
    List<AnnouncementModel> newsModels;
    DisplayImageOptions options;
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    public AnnouncementAdapter(Context context, List<AnnouncementModel> announcementModels){
        this.context = context;
        this.newsModels = announcementModels;

        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.cover_image)
                .showImageForEmptyUri(R.mipmap.cover_image)
                .showImageOnFail(R.mipmap.cover_image)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .displayer(new SimpleBitmapDisplayer())
                .build();
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.announcement_row, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);

        //appPreferences = AppPreferences.getAppPreferences(context);
        //   new getSessionStatus().execute();


        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final AnnouncementModel announcementModel=newsModels.get(position);


        holder.title.setText(announcementModel.getAnnouncement_title());
        holder.creator.setText(announcementModel.getUser_name());
        holder.time.setText(announcementModel.getDays_pass());
        holder.description.setText(announcementModel.getDescription());

        LoggerGeneral.info("Lenght of image" + announcementModel.getAnnouncement_images_url().length());

        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Social Society");
                String body = "" + announcementModel.getAnnouncement_title()+"\n-Posted by Social Society";
                shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, body);
                //shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                shareIntent.setType("text/plain");
                context.startActivity(Intent.createChooser(shareIntent, "Share on"));

            }
        });

        if (newsModels.get(position).getAnnouncement_images_url().length() > 0) {

            holder.imageView.setVisibility(View.VISIBLE);
            ImageLoader.getInstance().displayImage(announcementModel.getAnnouncement_images_url(), holder.imageView, options, animateFirstListener);

        }
        else {
            holder.imageView.setVisibility(View.GONE);
        }

    }





    @Override
    public int getItemCount() {

        //   return items.size();
        return newsModels.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title,creator,time,description;
        ImageView imageView;
        LinearLayout share;



        public Button cancel;

        public ViewHolder(View itemView) {
            super(itemView);

            ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context).build();
            ImageLoader.getInstance().init(config);
            title= (TextView) itemView.findViewById(R.id.title);
            creator= (TextView) itemView.findViewById(R.id.creator);
            time= (TextView) itemView.findViewById(R.id.time);
            description= (TextView) itemView.findViewById(R.id.description);
            imageView= (ImageView) itemView.findViewById(R.id.image);
            share= (LinearLayout) itemView.findViewById(R.id.share);

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


/*
public class AnnouncementAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    static Context context;
    List<AnnouncementModel> newsModels;
    DisplayImageOptions options;
    public AnnouncementAdapter(Context context, List<AnnouncementModel> announcementModels) {
        this.context = context;
        this.newsModels = announcementModels;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
            return new AnnouncementHolder(inflater.inflate(R.layout.announcement_row,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {


            ((AnnouncementHolder)holder).bindData(newsModels.get(position));
    }

    @Override
    public int getItemCount() {
        return newsModels.size();
    }

    static class AnnouncementHolder extends RecyclerView.ViewHolder{
        private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
        private DisplayImageOptions options;
        TextView title,creator,time,description;
        ImageView imageView;
        public AnnouncementHolder(View itemView) {
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

            ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context).build();
            ImageLoader.getInstance().init(config);
            title= (TextView) itemView.findViewById(R.id.title);
            creator= (TextView) itemView.findViewById(R.id.creator);
            time= (TextView) itemView.findViewById(R.id.time);
            description= (TextView) itemView.findViewById(R.id.description);
            imageView= (ImageView) itemView.findViewById(R.id.image);



        }

        void bindData(AnnouncementModel announcementModel) {
            title.setText(announcementModel.getAnnouncement_title());
            creator.setText("Created by," + announcementModel.getUser_name());
            time.setText(announcementModel.getDays_pass());
            description.setText(announcementModel.getDescription());

            LoggerGeneral.info("Lenght of image" + announcementModel.getAnnouncement_images_url().length());

            if (announcementModel.getAnnouncement_images_url().length() > 0) {

                ImageLoader.getInstance().displayImage(announcementModel.getAnnouncement_images_url(), imageView, options, animateFirstListener);

            }
            else {
                imageView.setVisibility(View.GONE);
            }
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
*/
