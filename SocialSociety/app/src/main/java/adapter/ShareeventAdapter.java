package adapter;


import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.inkswipe.SocialSociety.R;
import com.inkswipe.SocialSociety.ShareEvent;
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

import model.MembersModel;
import util.LoggerGeneral;

/**
 * Created by Ajinkya.Deshpande on 9/17/2016.
 */
public class ShareeventAdapter extends  RecyclerView.Adapter<ShareeventAdapter.ViewHolder> {

    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

    private DisplayImageOptions options;
    Context context;
    List<MembersModel> sharevent;
    public static int check=0;

    public ShareeventAdapter(Context context,List<MembersModel>sharevent) {

        this.context = context;
        this.sharevent=sharevent;

        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.fallb)
                .showImageForEmptyUri(R.mipmap.fallb)
                .showImageOnFail(R.mipmap.fallb)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .displayer(new SimpleBitmapDisplayer())
                .build();
        ShareEvent.shareList=new ArrayList<String>();

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.shareevent, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);



        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder,  final int position) {

       MembersModel shareeventModel = sharevent.get(position);

        holder.memberPost.setText(shareeventModel.getProperty_type_array()+" - "+shareeventModel.getProperty_name_array()+"/"+shareeventModel.getHouse_no_array());
        holder.txtname.setText(shareeventModel.getMemberName());

        holder.image.setBackgroundResource(R.mipmap.fallb);

        ImageLoader.getInstance().displayImage(shareeventModel.getProfile_image_url(), holder.image, options, animateFirstListener);

        if(shareeventModel.isImageChanged())
        {

            Drawable res = context.getResources().getDrawable(R.drawable.uusertick);
            holder.tick.setImageDrawable(res);


        }
        else
        {
            holder.tick.setImageDrawable(null);
        }

        holder.userSelectLayout.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {

                ShareEvent.groupMemberCheck=1;

                LoggerGeneral.info("Position of log1"+position);
                MembersModel shareeventModel = sharevent.get(position);



                if (sharevent.get(position).isImageChanged())
                {
                    holder.tick.setImageDrawable(null);
                    LoggerGeneral.info("Position of log2" + position);
                    sharevent.get(position).setIsImageChanged(false);
                    check--;
                    for(int i=0;i<=ShareEvent.shareList.size()-1;i++)
                    {

                        if(ShareEvent.shareList.get(i)==sharevent.get(position).getMemberId())
                        {
                            ShareEvent.shareList.remove(i);
                            LoggerGeneral.info("shareEventMembverDisize" + (ShareEvent.shareList.size()));

                            synchronized (ShareEvent.shareList)

                            {

                                ShareEvent.shareList.notify();

                            }
                        }
                    }
                    if(check==0)
                    {

                    final int sdk = android.os.Build.VERSION.SDK_INT;
                    if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                        ShareEvent.prPublish.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.fourborderlightgray));
                    } else {
                        ShareEvent.prPublish.setBackground(context.getResources().getDrawable(R.drawable.fourborderlightgray));
                    }
                    }
                }
                else
                {
                    check++;
                    sharevent.get(position).setIsImageChanged(true);

                    LoggerGeneral.info("Position of log3" + position);
                    Drawable res = context.getResources().getDrawable(R.drawable.usertick);
                    holder.tick.setImageDrawable(res);
                    ShareEvent.shareList.add(sharevent.get(position).getMemberId());

                    LoggerGeneral.info("shareEventMembverDisize11" + (ShareEvent.shareList.size()));

                    final int sdk = android.os.Build.VERSION.SDK_INT;
                    if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                        ShareEvent.prPublish.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.fourborder));
                    } else {
                        ShareEvent.prPublish.setBackground(context.getResources().getDrawable(R.drawable.fourborder));
                    }
                }
            }
        });
    }





    @Override
    public int getItemCount() {

        //   return items.size();
        return sharevent.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView image,tick;

        public TextView txtname,memberPost;
        LinearLayout userSelectLayout;



        public ViewHolder(View itemView) {
            super(itemView);


            memberPost= (TextView) itemView.findViewById(R.id.memberPost);
            txtname = (TextView) itemView.findViewById(R.id.mname);

            image =   (ImageView)itemView.findViewById(R.id.mimage);

            userSelectLayout= (LinearLayout) itemView.findViewById(R.id.userSelectLayout);

            tick= (ImageView) itemView.findViewById(R.id.tick);




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


/*if (holder.tick.isShown()) {
        if(check==1)
        {
        check--;
final int sdk = android.os.Build.VERSION.SDK_INT;
        if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
        ShareEvent.prPublish.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.fourborderlightgray));
        } else {
        ShareEvent.prPublish.setBackground(context.getResources().getDrawable(R.drawable.fourborderlightgray));
        }
        }
        else
        {
        check--;
        LoggerGeneral.info("Check in Adapter" + check);
        }
        holder.tick.setVisibility(View.GONE);
        holder.tick.setImageDrawable(null);
        }
        else {

        check++;
        LoggerGeneral.info("Check in Adapter" + check);
final int sdk = android.os.Build.VERSION.SDK_INT;
        if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
        ShareEvent.prPublish.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.fourborder));
        } else {
        ShareEvent.prPublish.setBackground(context.getResources().getDrawable(R.drawable.fourborder));
        }
        holder.tick.setVisibility(View.VISIBLE);
        Drawable res = context.getResources().getDrawable(R.drawable.tickcircle);
        holder.tick.setImageDrawable(res);

        }*/


/*
import android.content.Context;
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

import model.ShareeventModel;
import util.LoggerGeneral;

*/
/**
 * Created by Ajinkya on 9/21/2016.
 *//*

public class ShareeventAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private DisplayImageOptions options;
    Context context;
    List<ShareeventModel> sharevent;


    public ShareeventAdapter(Context context, List<ShareeventModel> sharevent) {
        this.context = context;
        this.sharevent = sharevent;

        LoggerGeneral.info("inadapter");


    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);

        return new ShareeventHolder(inflater.inflate(R.layout.shareevent,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ShareeventHolder)holder).bindData(sharevent.get(position));
    }

    static class ShareeventHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public ImageView image,tick;
        private DisplayImageOptions options;
        public TextView txtname;
        LinearLayout userSelectLayout;

        private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
        public ShareeventHolder(View itemView) {
            super(itemView);

            options = new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.mipmap.fallb)
                    .showImageForEmptyUri(R.mipmap.fallb)
                    .showImageOnFail(R.mipmap.fallb)
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .considerExifParams(true)
                    .displayer(new SimpleBitmapDisplayer())
                    .build();


            txtname = (TextView) itemView.findViewById(R.id.mname);

            image =   (ImageView)itemView.findViewById(R.id.mimage);

            userSelectLayout= (LinearLayout) itemView.findViewById(R.id.userSelectLayout);

            tick= (ImageView) itemView.findViewById(R.id.tick);
        }

        void bindData(ShareeventModel movieModel){

            txtname.setText(movieModel.getName());

            image.setBackgroundResource(R.mipmap.fallb);

        }

        @Override
        public void onClick(View view) {

        }
    }

    @Override
    public int getItemCount() {
        return sharevent.size();
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
