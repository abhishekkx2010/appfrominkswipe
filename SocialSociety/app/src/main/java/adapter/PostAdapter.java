package adapter;


import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.inkswipe.SocialSociety.DashBoard;
import com.inkswipe.SocialSociety.Login;
import com.inkswipe.SocialSociety.Post;
import com.inkswipe.SocialSociety.R;
import com.inkswipe.SocialSociety.UserDetailPost;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import model.CommentModel;
import model.PostModel;
import util.AppPreferences;
import util.Common;
import util.Constants;
import util.LoggerGeneral;
import util.ServiceFacade;

public class PostAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    static Context context;
    static List<PostModel> postModels;
    public AppPreferences appPreferences;
    static DashBoard dashBoard=new DashBoard();
    public static boolean clear;
    public static String societyId;
    static String u_posttext;
    static int dailogCancel=0;
    static String click;
    public PostAdapter(Context context, List<PostModel> postModels,String societyId,String click) {
        this.context = context;
        this.postModels = postModels;
        this.societyId=societyId;
        this.click = click;
        clear = false;
        dailogCancel=0;
        appPreferences = AppPreferences.getAppPreferences(context);

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return new PostHolder(inflater.inflate(R.layout.post_row,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        appPreferences = AppPreferences.getAppPreferences(context);

        ((PostHolder)holder).bindData(postModels.get(position));
    }

    @Override
    public int getItemCount() {
        return postModels.size();
    }

    static class PostHolder extends RecyclerView.ViewHolder{
        private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
        private DisplayImageOptions options,options1;
        TextView userName,time,description,noofcomments;
        ImageView userImage,image;
        RecyclerView comments;
        LinearLayout commentExtract,postcomment;
        EditText posttext;
        public String uposttext;
        public String upostId;
        public static boolean clear;
        LinearLayout share;
        ImageView postimage;

        public PostHolder(View itemView) {
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
            options1 = new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.mipmap.fallb)
                    .showImageForEmptyUri(R.mipmap.fallb)
                    .showImageOnFail(R.mipmap.fallb)
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .considerExifParams(true)
                    .displayer(new SimpleBitmapDisplayer())
                    .build();
            userName= (TextView) itemView.findViewById(R.id.userName);
            userImage= (ImageView) itemView.findViewById(R.id.userImage);
            image = (ImageView)itemView.findViewById(R.id.image);

            time= (TextView) itemView.findViewById(R.id.time);
            description= (TextView) itemView.findViewById(R.id.description);
            comments= (RecyclerView) itemView.findViewById(R.id.comments);
            commentExtract= (LinearLayout) itemView.findViewById(R.id.commentExtract);
            noofcomments = (TextView)itemView.findViewById(R.id.noofcomments);
            share= (LinearLayout) itemView.findViewById(R.id.share);
            postimage= (ImageView) itemView.findViewById(R.id.postimage);

            ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context).build();
            ImageLoader.getInstance().init(config);



        }

        void bindData(final PostModel postModel){
            dailogCancel++;
            AppPreferences appPreferences = AppPreferences.getAppPreferences(context);
            userName.setText(postModel.getCreated_by());

            userName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if(click.equals("click")) {


                        Intent userdetails = new Intent(context, UserDetailPost.class);

                        userdetails.putExtra("duser_id", postModel.getUser_id());
                        userdetails.putExtra("createdby", postModel.getCreated_by());
                        userdetails.putExtra("profilepic", postModel.getProfile_image_url());
                        userdetails.putExtra("SocietyId", societyId);

                        context.startActivity(userdetails);
                        ((Activity) context).finish();

                    }}
            });
            time.setText(postModel.getCreated_on());
            description.setText(postModel.getPost_description());
            ImageLoader.getInstance().displayImage(postModel.getProfile_image_url(), userImage, options1, animateFirstListener);

            share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Social Society");
                    String body = "" + postModel.getPost_description()+"\n-Posted by Social Society";
                    shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, body);
                    //shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                    shareIntent.setType("text/plain");
                    context.startActivity(Intent.createChooser(shareIntent, "Share on"));

                }
            });

            if(Common.isOnline(context)) {
                ImageLoader.getInstance().displayImage(appPreferences.getString("profile_image_url", ""), postimage, options1, animateFirstListener);
            }
            else
            {
                if(appPreferences.getString("profile_image_url", "")!=null ||!appPreferences.getString("profile_image_url","").equals("profile_image_url")||appPreferences.getString("profile_image_url", "")!="" ){

                    postimage.setImageBitmap(StringToBitMap(appPreferences.getString("storeimage","")));
                }
            }

            if(postModel.getPost_image_url().length()>0) {
                image.setVisibility(View.VISIBLE);
                ImageLoader.getInstance().displayImage(postModel.getPost_image_url(), image, options, animateFirstListener);
            }
            else {
                image.setVisibility(View.GONE);
            }
            commentExtract.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   /* if(comments.isShown())
                    {
                        comments.setVisibility(View.INVISIBLE);
                        Post.dataNotify();
                    }
                    else {*/

                    comments.setVisibility(View.VISIBLE);
                    //}
                }
            });

            List<CommentModel> commentModels= new ArrayList<CommentModel>();;
            CommentAdapter adapter;

            comments.setHasFixedSize(true);
            comments.setLayoutManager(new LinearLayoutManager(context));
            comments.setNestedScrollingEnabled(false);
            commentModels= postModel.getCommentModel();
            adapter=new CommentAdapter(context,commentModels);
            comments.setAdapter(adapter);
            if(dailogCancel==postModels.size())
            {
                Post.mDialog12.dismiss();
                if(UserDetailPost.mDialog123!=null) {
                    UserDetailPost.mDialog123.dismiss();
                }
            }

            InputFilter filter = new InputFilter() {
                @Override
                public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                    for (int i = start; i < end; i++) {
                        int type = Character.getType(source.charAt(i));
                        //System.out.println("Type : " + type);
                        if (type == Character.SURROGATE || type == Character.OTHER_SYMBOL) {
                            return "";
                        }
                    }
                    return null;
                }
            };


            posttext = (EditText)itemView.findViewById(R.id.posttext);
            posttext.setFilters(new InputFilter[]{filter});
            posttext.addTextChangedListener(new TextWatcher() {

                @Override
                public void afterTextChanged(Editable s) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                    //    doSomething();
                    uposttext = posttext.getText().toString().trim();

                    if (uposttext != null && uposttext.length() > 0) {

                        postcomment.setVisibility(View.VISIBLE);
                    }

                    if (uposttext.length() == 0) {
                        postcomment.setVisibility(View.GONE);
                    }


                }

            });

            if(commentModels.size()>0) {

                noofcomments.setText(commentModels.size() + " Comments");

            }
            else if(commentModels.size()==0){
                noofcomments.setText("Comments");
            }

         /*   posttext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    uposttext = posttext.getText().toString().trim();

                    if(uposttext!=null && uposttext.length()>0){

                        postcomment.setVisibility(View.VISIBLE);
                    }

                    if(uposttext.length()>0) {
                        postcomment.setVisibility(View.GONE);
                    }

                }
            });
*/



            postcomment = (LinearLayout) itemView.findViewById(R.id.postcomment);

            postcomment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {




                    uposttext = posttext.getText().toString();


                    upostId = postModel.getId();

                    if (uposttext != null && uposttext.length() > 0) {

                        if (Common.isOnline(context)) {

                            new PostComment(uposttext,upostId).execute();

                        } else {

                            Common.showToast(context, "No internet connection");
                        }

                    }
                    else {
                        Common.showToast(context,"Enter comment ");
                    }



                    if(clear==true){
                        posttext.setText("");
                        clear=false;
                    }
                }
            });

            userImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Dialog dialog = new Dialog(context);
                    dialog.setContentView(R.layout.dpdialog);
                    dialog.setCanceledOnTouchOutside(true);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


                    ImageView dpImage = (ImageView) dialog.findViewById(R.id.dpimage);

                    ImageLoader.getInstance().displayImage(postModel.getProfile_image_url(), dpImage, options, animateFirstListener);


                    TextView profilenm = (TextView) dialog.findViewById(R.id.profilemnm);
                    profilenm.setText(postModel.getCreated_by());
                    ImageView profileim = (ImageView) dialog.findViewById(R.id.profileim);

                    profileim.setVisibility(View.GONE);

                    dialog.show();
                }
            });

        }

        public Bitmap StringToBitMap(String encodedString){
            try {
                byte [] encodeByte= Base64.decode(encodedString, Base64.DEFAULT);
                Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                return bitmap;
            } catch(Exception e) {
                e.getMessage();
                return null;
            }
        }

        public  class  PostComment extends AsyncTask<String, String, JSONObject> {
            String type;
            ProgressDialog mDialog;
            String uposttext;
            String upostId;

            public AppPreferences appPreferences;
            public PostComment(String uposttext,String upostId ){
                this.uposttext = uposttext;
                this.upostId   = upostId;

                appPreferences = AppPreferences.getAppPreferences(context);
            }
            @Override
            protected JSONObject doInBackground(String... params) {
                // TODO Auto-generated method stub

                String url= Constants.Base+Constants.PostuserComment;

                JSONObject object = new JSONObject();
                try {

                    //    object.put("society_id",SocietyId);
                    object.put("user_id", appPreferences.getString("user_id",""));
                    object.put("post_id",upostId);
                    object.put("comment",uposttext);

                    //    object.put("property_id",property_id);


                    LoggerGeneral.info("JsonObjectPrintEditProperty" + object.toString());

                } catch (Exception ex) {

                }

                String str = '"' + appPreferences.getString("jwt", "") + '"';
                JSONObject jsonObject  = ServiceFacade.getResponsJsonParams(url, object);

                Log.d("hi", "getresponse" + jsonObject);

                Log.d("hi", "getresponse" + jsonObject);


                return jsonObject;
            }
            @Override
            protected void onPreExecute() {
                // TODO Auto-generated method stub
                super.onPreExecute();

                mDialog = new ProgressDialog(context,ProgressDialog.THEME_HOLO_DARK);
                mDialog.setMessage("Processing...");
                mDialog.show();
                mDialog.setCancelable(false);
                mDialog.setCanceledOnTouchOutside(false);
            }

            @Override
            protected void onPostExecute(JSONObject results) {
                // TODO Auto-generated method stub
                super.onPostExecute(results);
                Log.d("hi", "getresultsstate" + results);
                //mDialog.dismiss();

                if(results!=null){

                    try {

                        JSONObject meta = results.getJSONObject("meta");

                        int status_code = meta.getInt("status_code");

                        int account_status = 1;
                        if(meta.has("account_status")){
                            if(!meta.isNull("account_status")){
                                account_status = meta.getInt("account_status");
                            }
                        }

                        if(account_status==1){
                            if (status_code == 0) {



                                posttext.setText("");

                                Intent dashBoard=new Intent(context,DashBoard.class);
                                dashBoard.putExtra("SocietyId", societyId);
                                context.startActivity(dashBoard);
                                ((Activity)context).finish();

                                Common.showToast(context,"Comment posted successfully");


                            }
                        }

                        if (account_status == 0) {
                            Intent intent = new Intent(context, Login.class);
                            appPreferences.remove("user_id");
                            appPreferences.remove("user_name");
                            appPreferences.remove("email_id");
                            appPreferences.remove("storecoverimage");
                            appPreferences.remove("storeimage");
                            appPreferences.remove("profile_image_url");
                            context.startActivity(intent);
                            ((Activity) context).finish();
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    mDialog.dismiss();


                }
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



  /* public static class  PostComment extends AsyncTask<String, String, JSONObject> {
        String type;
        ProgressDialog mDialog;
        String uposttext;
        String upostId;

        public AppPreferences appPreferences;
        public PostComment(String uposttext,String upostId ){
            this.uposttext = uposttext;
            this.upostId   = upostId;

            appPreferences = AppPreferences.getAppPreferences(context);
        }
        @Override
        protected JSONObject doInBackground(String... params) {
            // TODO Auto-generated method stub

            String url= Constants.Base+Constants.PostuserComment;

            JSONObject object = new JSONObject();
            try {

                //    object.put("society_id",SocietyId);
                object.put("user_id",appPreferences.getString("user_id",""));
                object.put("post_id",upostId);
                object.put("comment",uposttext);

                //    object.put("property_id",property_id);


                LoggerGeneral.info("JsonObjectPrintEditProperty" + object.toString());

            } catch (Exception ex) {

            }

            String str = '"' + appPreferences.getString("jwt", "") + '"';
            JSONObject jsonObject  = ServiceFacade.getResponsJsonParams(url, object);

            Log.d("hi", "getresponse" + jsonObject);

            Log.d("hi", "getresponse" + jsonObject);


            return jsonObject;
        }
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

            mDialog = new ProgressDialog(context,R.style.MyAlertDialogStyle);
            mDialog.setMessage("Processing...");
            mDialog.show();
            mDialog.setCancelable(false);
            mDialog.setCanceledOnTouchOutside(false);
        }

        @Override
        protected void onPostExecute(JSONObject results) {
            // TODO Auto-generated method stub
            super.onPostExecute(results);
            Log.d("hi", "getresultsstate" + results);
            //mDialog.dismiss();

            if(results!=null){

                try {

                    JSONObject meta = results.getJSONObject("meta");

                    int status_code = meta.getInt("status_code");

                    int account_status = meta.getInt("account_status");

                    if(account_status==1){
                        if (status_code == 0) {


                            clear=true;


                            Common.showToast(context,"Comment posted successfully");


                        }
                    }

                    if(account_status == 0) {
                        Intent intent = new Intent(context,Login.class);
                        appPreferences.remove("user_id");
                        appPreferences.remove("user_name");
                        appPreferences.remove("email_id");
                        context.startActivity(intent);
                        ((Activity) context).finish();
                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mDialog.dismiss();


            }
        }

    }
*/
}
