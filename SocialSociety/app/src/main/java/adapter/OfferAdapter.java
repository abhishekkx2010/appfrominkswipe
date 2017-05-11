package adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import model.OfferModel;

public class OfferAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    static Context context;
    List<OfferModel> offerModels;



    public OfferAdapter(Context context, List<OfferModel> offerModels) {
        this.context = context;
        this.offerModels = offerModels;




    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return new OfferHolder(inflater.inflate(R.layout.offer_row,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {


        ((OfferHolder)holder).bindData(offerModels.get(position));
    }

    @Override
    public int getItemCount() {
        return offerModels.size();
    }

    static class OfferHolder extends RecyclerView.ViewHolder{

        private DisplayImageOptions options;
        private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
        TextView brandName,description,category;
        ImageView offerLogo,offerImageView;
        LinearLayout share;
        public OfferHolder(View itemView) {
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

            brandName= (TextView) itemView.findViewById(R.id.brandName);
            description= (TextView) itemView.findViewById(R.id.description);
            offerLogo= (ImageView) itemView.findViewById(R.id.offerLogo);
            offerImageView= (ImageView) itemView.findViewById(R.id.offerImage);
            category= (TextView) itemView.findViewById(R.id.category);
            share= (LinearLayout) itemView.findViewById(R.id.share);


        }

        void bindData(final OfferModel OfferModel){
            brandName.setText(OfferModel.getOfferCompanyName());
            description.setText(OfferModel.getOfferDescription());
            category.setText(OfferModel.getCategory());

            ImageLoader.getInstance().displayImage(OfferModel.getOffer_logo_url(), offerLogo, options, animateFirstListener);
            ImageLoader.getInstance().displayImage(OfferModel.getOffer_image_url(), offerImageView, options, animateFirstListener);

            share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Ink News");
                    String body = "" + OfferModel.getOfferTitle();
                    shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, body);
                    //shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                    shareIntent.setType("text/plain");
                    context.startActivity(Intent.createChooser(shareIntent, "Share on"));

                }
            });

            if(OfferModel.getExternalUrl()!=null&&OfferModel.getExternalUrl().length()>0){

                offerImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(OfferModel.getExternalUrl()));
                        context.startActivity(browserIntent);
                    }
                });


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
