package adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

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

import model.CommentModel;

/**
 * Created by Nitin on 9/26/2016.
 */
public class CommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    List<CommentModel> commentModels;

    public CommentAdapter(Context context, List<CommentModel> commentModels)
    {
        this.context=context;
        this.commentModels=commentModels;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return new CommentHolder(inflater.inflate(R.layout.comment_row,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {


        ((CommentHolder)holder).bindData(commentModels.get(position));
    }

    @Override
    public int getItemCount() {
        return commentModels.size();
    }

    static class CommentHolder extends RecyclerView.ViewHolder{
        private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
        private DisplayImageOptions options;
        TextView userName,time,description;
        ImageView userImage;
        ListView comments;
        public CommentHolder(View itemView) {
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
            userName= (TextView) itemView.findViewById(R.id.usernameComment);
            userImage= (ImageView) itemView.findViewById(R.id.userImage);
            description= (TextView) itemView.findViewById(R.id.comment);



        }

        void bindData(CommentModel commentModel){
         //   userName.setText(commentModel.getUserName());


            Spanned text = Html.fromHtml("<b><font color='#6eb743'>" + commentModel.getUserName() + "</font></b>" +" "+commentModel.getComment());
            userName.setText(text);
         //   description.setText(commentModel.getComment());
            ImageLoader.getInstance().displayImage(commentModel.getProfileImage(), userImage, options, animateFirstListener);

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