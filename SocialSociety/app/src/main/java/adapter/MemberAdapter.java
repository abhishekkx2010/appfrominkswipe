package adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

import model.MembersModel;
import util.LoggerGeneral;

/**
 * Created by preet on 9/28/2016.
 */
public class MemberAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    static Context context;
    List<MembersModel> membersModelList;
    static int check;
    public MemberAdapter(Context context, List<MembersModel> membersModelList,int check) {
        this.context = context;
        this.membersModelList = membersModelList;
        this.check=check;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return new MemberHolder(inflater.inflate(R.layout.member_row,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {


        if(membersModelList.size()>0) {

            ((MemberHolder) holder).bindData(membersModelList.get(position));

        }
        else
        {
            ((MemberHolder) holder).shownomember();


        }
    }

    @Override
    public int getItemCount() {
        LoggerGeneral.info("Mene"+membersModelList.size());
        return membersModelList.size();


    }

    static class MemberHolder extends RecyclerView.ViewHolder{
        private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
        private DisplayImageOptions options;
        TextView membername,memberPost,description,nomember;
        ImageView userImage;

        public MemberHolder(View itemView) {
            super(itemView);

            options = new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.mipmap.fallb)
                    .showImageForEmptyUri(R.mipmap.fallb)
                    .showImageOnFail(R.mipmap.fallb)
                    .cacheInMemory(false)
                    .cacheOnDisk(false)
                    .considerExifParams(true)
                    .displayer(new SimpleBitmapDisplayer())
                    .build();

            ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context).build();
            ImageLoader.getInstance().init(config);

            userImage= (ImageView) itemView.findViewById(R.id.memberImage);
            membername= (TextView) itemView.findViewById(R.id.memberName);
            memberPost= (TextView) itemView.findViewById(R.id.memberPost);


            nomember = (TextView)itemView.findViewById(R.id.nomember);

        }

        void bindData(final MembersModel membersModel) {


            if (check == 1) {


                ImageLoader.getInstance().displayImage(membersModel.getProfile_image_url(), userImage, options, animateFirstListener);

                membername.setText(membersModel.getMemberName());
                LoggerGeneral.info("House Arraya===="+membersModel.getProperty_name_array());
                memberPost.setText(membersModel.getProperty_type_array()+" - "+membersModel.getProperty_name_array()+"/"+membersModel.getHouse_no_array());
              //  memberPost.setText(membersModel.getProperty_type_array());

                userImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final Dialog dialog = new Dialog(context);
                        dialog.setContentView(R.layout.dpdialog);
                        dialog.setCanceledOnTouchOutside(true);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


                        ImageView dpImage = (ImageView) dialog.findViewById(R.id.dpimage);

                        ImageLoader.getInstance().displayImage(membersModel.getProfile_image_url(), dpImage, options, animateFirstListener);


                        TextView profilenm = (TextView) dialog.findViewById(R.id.profilemnm);
                      //  TextView memberPost = (TextView) dialog.findViewById(R.id.memberPost);
                       // memberPost.setText(membersModel.getProperty_type_array());
                        profilenm.setText(membersModel.getMemberName());
                        ImageView profileim = (ImageView) dialog.findViewById(R.id.profileim);

                        profileim.setVisibility(View.GONE);
                      //  memberPost.setVisibility(View.GONE);
                        dialog.show();

                    }
                });
            }

            if (check == 0) {
                ImageLoader.getInstance().displayImage(membersModel.getProfileImageURL(), userImage, options, animateFirstListener);

                membername.setText(membersModel.getUserName());
                memberPost.setText(membersModel.getProperty_type_array()+" - "+membersModel.getProperty_name_array()+"/"+membersModel.getHouse_no_array());

                userImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Dialog dialog = new Dialog(context);
                        dialog.setContentView(R.layout.dpdialog);
                        dialog.setCanceledOnTouchOutside(true);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


                        ImageView dpImage = (ImageView) dialog.findViewById(R.id.dpimage);

                        ImageLoader.getInstance().displayImage(membersModel.getProfileImageURL(), dpImage, options, animateFirstListener);


                        TextView profilenm = (TextView) dialog.findViewById(R.id.profilemnm);
                        TextView memberPost = (TextView) dialog.findViewById(R.id.memberPost);
                        memberPost.setText((CharSequence) membersModel.getProperty_type_array());
                        profilenm.setText(membersModel.getMemberName());
                        ImageView profileim = (ImageView) dialog.findViewById(R.id.profileim);

                        profileim.setVisibility(View.GONE);
                        memberPost.setVisibility(View.GONE);
                        dialog.show();

                    }
                });
            }

        }

        void shownomember(){


            nomember.setVisibility(View.VISIBLE);


        }
    }

    public static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

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
