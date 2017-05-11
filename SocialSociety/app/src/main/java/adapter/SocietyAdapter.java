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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.inkswipe.SocialSociety.AddProperty;
import com.inkswipe.SocialSociety.DataWrapper;
import com.inkswipe.SocialSociety.MySociety;
import com.inkswipe.SocialSociety.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import model.SocietyModel;
import util.AppPreferences;
import util.LoggerGeneral;

/**
 * Created by Ajinkya.Deshpande on 9/17/2016.
 */
public class SocietyAdapter extends  RecyclerView.Adapter<SocietyAdapter.ViewHolder> {


    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    AppPreferences appPreferences;
    String session_status,pending_sessions,date_of_session;
    int is_package_available;
    String latlog,longitudelog;
    Context context;
    List<SocietyModel>societies;
    DisplayImageOptions options;
    SocietyModel societyModel = null;
    String societyId;
    public SocietyAdapter(Context context,List<SocietyModel>societies) {

        this.context = context;
        this.societies=societies;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.society_row, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);

        appPreferences = AppPreferences.getAppPreferences(context);
        //   new getSessionStatus().execute();


        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.cover_image)
                .showImageForEmptyUri(R.mipmap.cover_image)
                .showImageOnFail(R.mipmap.cover_image)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .displayer(new SimpleBitmapDisplayer())
                .build();


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

         societyModel = societies.get(position);



        holder.txtTitle.setText(societyModel.getName() + ", " + societyModel.getCity());
        ImageLoader.getInstance().displayImage(societyModel.getSociety_image_url(), holder.backimg, options, animateFirstListener);
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeAt(position, societyModel);
            }
        });

        holder.backimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SocietyModel societyModel1 = societies.get(position);

                societyId = societyModel1.getId();
                Intent intent = new Intent(context, AddProperty.class);

                LoggerGeneral.info("socId----" + societyId);
                intent.putExtra("societyId", societyId);
                intent.putExtra("societylist", (new DataWrapper((ArrayList<SocietyModel>) MySociety.societies)));
                context.startActivity(intent);
                ((Activity)context).finish();
            }
        });

      //  holder.citytxt.setText(","+societyModel.getCity());
    }


    public  void removeAt(int position,SocietyModel societyModel) {
        int idd=societyModel.getIdd();
        societies.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, societies.size());
        MySociety.removeItem(idd);
        LoggerGeneral.info("Hiiiii"+position+"===="+societies.size());
    }


    @Override
    public int getItemCount() {

        //   return items.size();
        return societies.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imgclear,remove;;
        private DisplayImageOptions options;
        public TextView txtTitle,source,citytxt;
        public ImageView backimg;
        RelativeLayout delete;
        private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

        public ViewHolder(View itemView) {
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


            txtTitle = (TextView) itemView.findViewById(R.id.txt);

            backimg =   (ImageView)itemView.findViewById(R.id.backimg);

            delete = (RelativeLayout) itemView.findViewById(R.id.delete);
            delete.setVisibility(View.GONE);

            citytxt = (TextView)itemView.findViewById(R.id.citytxt);


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