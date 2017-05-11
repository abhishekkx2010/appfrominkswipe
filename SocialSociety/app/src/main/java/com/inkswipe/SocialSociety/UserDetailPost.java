package com.inkswipe.SocialSociety;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import adapter.CropOptionAdapter;
import adapter.CustomAdapter;
import adapter.PostAdapter;
import adapter.PropertyAdapter;
import model.CommentModel;
import model.CropOption;
import model.DrawerList;
import model.PostModel;
import util.AppPreferences;
import util.Common;
import util.Constants;
import util.LoggerGeneral;
import util.ServiceFacade;

public class UserDetailPost extends AppCompatActivity {



    RecyclerView recyclerView;
    List<PostModel> postModels;
    static PostAdapter adapter;

    AppPreferences appPreferences;

    LinearLayout bordercreatepost;


    String SocietyId;

    String id;
    String user_id;
    String society_id;
    String post_description;
    static int imageChecker=0;
    String post_image;
    String status;
    String created_on;
    String days_pass;
    String post_image_url;
    String created_by;
    String profile_image_url;
    private static final int PERMISSION_REQUEST_CODE = 1;
    public static int permissionChecker=0;
    Uri imageUri;
    static final int requestCodeForSdCard = 1, requestCodeForCamera = 2, requestCodeForCorp = 3;
    LinearLayout camera;
    int RESULT_OK =-1;
    String postimage;
    LinearLayout post;
    String u_postdesc;
    EditText postdesc;
    ImageView rphoto;
    TextView noofcomments;
    DisplayImageOptions options;
    LinearLayout postText;
    int commentlengh;
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

    ImageView postimageView;
    static GetPosts postAsyncTask;

    public static ProgressDialog mDialog123;

    Context context;


    String duser_id;

    TextView dashname;

    String createdby;
    String profilepic;



    ImageView rphotoo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail_post);

        context = UserDetailPost.this;


        appPreferences = AppPreferences.getAppPreferences(context);

        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setNestedScrollingEnabled(false);
        postModels= new ArrayList<PostModel>();

        bordercreatepost= (LinearLayout)findViewById(R.id.bordercreatepost);

        bordercreatepost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                postdesc.requestFocus();

                final InputMethodManager inputMethodManager =
                        (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);

                inputMethodManager.showSoftInput(postdesc, InputMethodManager.SHOW_FORCED);
            }
        });
        duser_id  = getIntent().getStringExtra("duser_id");
        SocietyId = getIntent().getStringExtra("SocietyId");



        profilepic = getIntent().getStringExtra("profilepic");
        createdby  = getIntent().getStringExtra("createdby");



        dashname =(TextView)findViewById(R.id.dashname);
        dashname.setText(createdby);




        postimageView= (ImageView)findViewById(R.id.postImage);

        rphotoo = (ImageView)findViewById(R.id.rphotoo);
        ImageLoader.getInstance().displayImage(profilepic, rphotoo, options, animateFirstListener);


        rphoto= (ImageView) findViewById(R.id.rphoto);
        ImageLoader.getInstance().displayImage(appPreferences.getString("profile_image_url",""), rphoto, options, animateFirstListener);
        post = (LinearLayout)findViewById(R.id.post);
        postText= (LinearLayout)findViewById(R.id.postText);

        postText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postdesc.requestFocus();
                InputMethodManager inputMethodManager =
                        (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.toggleSoftInputFromWindow(
                        postdesc.getApplicationWindowToken(),
                        InputMethodManager.SHOW_FORCED, 0);
            }
        });

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                u_postdesc =postdesc.getText().toString();



                if(u_postdesc!=null && u_postdesc.length()>0 ||postimage.length()>0 &&postimage!=null ) {
                    if (Common.isOnline(context)) {

                        new UserPost().execute();
                    } else {
                        Common.showToast(context, "No internet connection");
                    }
                }
                else {


                    Common.showToast(context,"Cannot submit blank post");
                }
            }
        });

        camera = (LinearLayout)findViewById(R.id.camera);

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageChecker = 2;

                if (Build.VERSION.SDK_INT >= 23) {
                    // Do some stuff


                    if (!checkPermission2()) {

                        requestPermission();

                    } else {
                        imageChecker = 2;
                        selectImage();
                    }
                } else {
                    imageChecker = 2;
                    selectImage();
                }


            }
        });



        postdesc = (EditText)findViewById(R.id.postdesc);
        postdesc.addTextChangedListener(new TextWatcher() {

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
                u_postdesc =postdesc.getText().toString();



                if (u_postdesc != null && u_postdesc.length() > 0 ) {


                    LoggerGeneral.info("show---" + u_postdesc);
                    post.setVisibility(View.VISIBLE);
                }

                //   if(postimage.length()==0 ||postimage==null||postimage.equals(null)) {
                if (u_postdesc.length() == 0) {
                    post.setVisibility(View.GONE);
                }

            /*    }
                else {
                    post.setVisibility(View.VISIBLE);
                }*/


            }

        });



        /*for(int i=0;i<=5;i++)
        {
            PostModel postModel=new PostModel();
            postModel.setUserName("John Doe");
            postModel.setTime("2 days ago");
            postModel.setDescription("We request all society member to join them in the will be .....");


            List<CommentModel> commentModels=new ArrayList<CommentModel>();
            for(int j=0;j<2;j++)
            {
                CommentModel commentModel=new CommentModel();
                commentModel=new CommentModel();
                commentModel.setUserName("John Doe");
                commentModel.setComment("We request all society member to join them in the will be .....");
                commentModels.add(commentModel);
            }
            postModel.setCommentModel(commentModels);

            postModels.add(postModel);
        }*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.my_statusbar_color));
        }





        if(Common.isOnline(context)) {


            postAsyncTask = new GetPosts();
            postAsyncTask.execute();
            //     new GetPosts().execute();
        }
        else {
            Common.showToast(context,"No internet connection");
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView title=((TextView) toolbar.findViewById(R.id.toolbar_title));
        title.setText("Detail Post");
        toolbar.setTitleTextColor(android.graphics.Color.WHITE);
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.fallb)
                .showImageForEmptyUri(R.mipmap.fallb)
                .showImageOnFail(R.mipmap.fallb)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .displayer(new SimpleBitmapDisplayer())
                .build();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.getNavigationIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
        //    toolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.backarrow));

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent DashBoard = new Intent(UserDetailPost.this, DashBoard.class);
                Common.internet_check = 0;
                //intentCheck=0;
                DashBoard.putExtra("SocietyId", SocietyId);
                startActivity(DashBoard);
                finish();


                //What to do on back clicked

            /*    if(fromNotification!=null)
                {
                    Intent notification=new Intent(DashBoard.this,NotificationApp.class);
                    notification.putExtra("Societyid",SocietyId1);
                    startActivity(notification);
                    finish();
                }
                else {
                    Intent intent = new Intent(DashBoard.this, MyProperty.class);
                    PropertyAdapter.intentCheck = 0;
                    Common.internet_check = 0;
                    startActivity(intent);
                    finish();
                }*/
            }
        });

     /*   notification= (RelativeLayout) findViewById(R.id.notification);

        RelativeLayout notificationcount= (RelativeLayout) findViewById(R.id.notificationcount);

        TextView notificationtext= (TextView) findViewById(R.id.notificationtext);

        if(Constants.notififcationcount>0) {
            notificationcount.setVisibility(View.VISIBLE);
            notificationtext.setText(String.valueOf(Constants.notififcationcount));
        }
        else {
            notificationcount.setVisibility(View.GONE);
        }

        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent notification = new Intent(DashBoard.this, NotificationApp.class);
                //    notification.putExtra("SocietyId",Soci)
                int test1 = appPreferences.getInt("NotificationOld", 0);
                test1 = test1 + Constants.notififcationcount;
                appPreferences.putInt("NotificationOld", test1);
                notification.putExtra("SocietyId", SocietyId);
                Constants.notififcationcount = 0;
                Common.internet_check=0;
                startActivity(notification);
                finish();
            }
        });

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        LayoutInflater inflater = getLayoutInflater();
        View listHeaderView = inflater.inflate(R.layout.header_list, null, false);

        LinearLayout navHome= (LinearLayout) listHeaderView.findViewById(R.id.Navhome);

        navHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myProperty=new Intent(DashBoard.this,MyProperty.class);
                PropertyAdapter.intentCheck=0;
                Common.internet_check=0;
                startActivity(myProperty);
                finish();
            }
        });

        home = (LinearLayout)findViewById(R.id.home);
        home.setOnClickListener(homeOnclickListener);

        mDrawerList.addHeaderView(listHeaderView);

        DrawerList list=new DrawerList();

        List<ItemObject> listViewItems = list.drawer();


        mDrawerList.setAdapter(new CustomAdapter(this, listViewItems));

*/


       /* if(commentlengh>0) {

            noofcomments.setText(commentlengh + " Comments");

        }
        if(commentlengh==0) {
            noofcomments.setText("0 Comments");
        }*/
    }


    private void selectImage() {


        View view = getLayoutInflater().inflate (R.layout.alertimage1, null);
        LinearLayout camera = (LinearLayout) view.findViewById(R.id.camera);
        LinearLayout gallery = (LinearLayout) view.findViewById(R.id.gallery);
        LinearLayout cancel = (LinearLayout) view.findViewById(R.id.cancel);

        final Dialog mBottomSheetDialog = new Dialog(context,
                R.style.MaterialDialogSheet);
        mBottomSheetDialog.setContentView(view);
        mBottomSheetDialog.setCancelable(true);
        mBottomSheetDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mBottomSheetDialog.getWindow().setGravity(Gravity.BOTTOM);
        mBottomSheetDialog.show();


        camera.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mBottomSheetDialog.cancel();
                if (Build.VERSION.SDK_INT >= 23) {
                    // Do some stuff


                    if (!checkPermission3()) {
                        permissionChecker = 1;
                        requestPermission();

                    } else {

                        File f = new File(Common.getChacheDir(context), "abc.jpg");
                        if (f.exists()) {
                            f.delete();
                        }

                        f = Common.createNewFileOrOverwrite(Common.getChacheDir(context), "abc.jpg");
                        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        imageUri = Uri.fromFile(f);
                        i.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                        startActivityForResult(i, requestCodeForCamera);
                    }
                } else {


                    File f = new File(Common.getChacheDir(context), "abc.jpg");
                    if (f.exists()) {
                        f.delete();
                    }

                    f = Common.createNewFileOrOverwrite(Common.getChacheDir(context), "abc.jpg");
                    Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    imageUri = Uri.fromFile(f);
                    i.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    startActivityForResult(i, requestCodeForCamera);
                }
            }
        });

        gallery.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mBottomSheetDialog.cancel();
                if (Build.VERSION.SDK_INT >= 23) {
                    // Do some stuff


                    if (!checkPermission3()) {
                        permissionChecker = 1;
                        requestPermission();

                    } else {

                        Intent gallery_Intent = new Intent(context.getApplicationContext(), GalleryUtil.class);
                        startActivityForResult(gallery_Intent, requestCodeForSdCard);
                    }
                } else {
                    Intent gallery_Intent = new Intent(context.getApplicationContext(), GalleryUtil.class);
                    startActivityForResult(gallery_Intent, requestCodeForSdCard);
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mBottomSheetDialog.cancel();
            }
        });

        mBottomSheetDialog.show();
    }



    private boolean checkPermission2(){
        int result = ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA);
        if (result == PackageManager.PERMISSION_GRANTED){

            return true;

        } else {

            return false;

        }
    }

    private void requestPermission(){

        if(permissionChecker==1)
        {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                permissionChecker=0;
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);

            } else {
                permissionChecker=0;
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
            }
        }else {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.CAMERA)) {

                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, PERMISSION_REQUEST_CODE);

            } else {

                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, PERMISSION_REQUEST_CODE);
            }
        }
    }

    private boolean checkPermission3(){
        int result = ContextCompat.checkSelfPermission(context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED){

            return true;

        } else {

            return false;

        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == requestCodeForSdCard && resultCode == RESULT_OK && data != null) {


            String picturePath = data.getStringExtra("picturePath");
            //perform Crop on the Image Selected from Gallery
            doCrop1(picturePath);



        } else if (requestCode == requestCodeForCamera && resultCode == RESULT_OK) {

            LoggerGeneral.info("comek here 1");
            /*File f = new File(Common.getChacheDir(context), "abc.jpg");
            imageUri = Uri.fromFile(f);
            Bitmap newBMP = null;
            newBMP = Common.decodeFile(f);
            Common.saveBitmapToFile(newBMP, f);*/
            doCrop(imageUri);


        } else if (requestCode == requestCodeForCorp && resultCode == RESULT_OK) {

            LoggerGeneral.info("requestcode for corp ");
            try {
                if (data != null) {
                    LoggerGeneral.info("data != null");
                    Bitmap newBMP = null;
                    Bitmap rotBMP = null;

                    Uri imageUri = data.getData();


                    if(imageUri!=null) {
                        newBMP = MediaStore.Images.Media.getBitmap(context.getContentResolver(), imageUri);

                    }
                    else {
                        newBMP = data.getExtras().getParcelable("data");
                    }
                    //newBMP = data.getExtras().getParcelable("data");

                    // File uri=new File(imageUri.getPath());
                    //imgPreview.setImageBitmap(newBMP);


                    File f = Common.createNewFileOrOverwrite(Common.getChacheDir(context), "abc.jpg");
                    Common.saveBitmapToFile(newBMP, f);
                    startUploadActivity(newBMP);


                } else {
                    LoggerGeneral.info("data == null");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


        } else {
            LoggerGeneral.info("failed");
        }

    }

    public void startUploadActivity(Bitmap newBMP) {
        Common.showToast(context, "Image selected");

        postimageView.setVisibility(View.VISIBLE);

        postimage=getStringImage(newBMP);

        postimageView.setImageBitmap(newBMP);

        LoggerGeneral.info("postimggg---" + postimage);

        post.setVisibility(View.VISIBLE);

        Bitmap resizedBMP = getResizedBitmap(newBMP, 500,500);


        ByteArrayOutputStream bs = new ByteArrayOutputStream();


    }
    public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height,
                matrix, false);

        return resizedBitmap;
    }

    private void doCrop1(final String mImageCaptureUri) {
        try {
            //Start Crop Activity

            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            // indicate image type and Uri
            LoggerGeneral.info("Hiiiiiiii" + mImageCaptureUri);
            File f = new File(mImageCaptureUri);
            Uri contentUri = Uri.fromFile(f);

            cropIntent.setDataAndType(contentUri, "image/*");


            // set crop properties
            cropIntent.putExtra("crop", "true");
            // indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 9);
            cropIntent.putExtra("aspectY", 5);
            // indicate output X and Y
            cropIntent.putExtra("outputX", 400);
            cropIntent.putExtra("outputY", 200);

            // retrieve data on return


            cropIntent.putExtra("return-data", true);
            // start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, requestCodeForCorp);
        }
        // respond to users whose devices do not support the crop action
        catch (ActivityNotFoundException anfe) {
            // display an error message
            String errorMessage = "your device doesn't support the crop action!";
            Toast toast = Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private void doCrop(final Uri mImageCaptureUri) {
        final ArrayList<CropOption> cropOptions = new ArrayList<CropOption>();

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setType("image/*");

        List<ResolveInfo> list = getPackageManager().queryIntentActivities(intent, 0);

        int size = list.size();

        if (size == 0) {
            Toast.makeText(context, "Can not find image crop app", Toast.LENGTH_SHORT).show();

            return;
        } else {


            intent.putExtra("outputX", 400);
            intent.putExtra("outputY", 200);
            intent.putExtra("aspectX", 9);
            intent.putExtra("aspectY", 5);
            intent.putExtra("scale", false);
            //intent.putExtra("extra_size_limit", 512);
            intent.putExtra("return-data", true);
            //intent.putExtra("crop", true);
            intent.setData(mImageCaptureUri);

            if (size == 1) {
                Intent i = new Intent(intent);
                ResolveInfo res = list.get(0);

                i.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));

                startActivityForResult(i, requestCodeForCorp);
            } else {
                for (ResolveInfo res : list) {
                    final CropOption co = new CropOption();

                    co.title = getPackageManager().getApplicationLabel(res.activityInfo.applicationInfo);
                    co.icon = getPackageManager().getApplicationIcon(res.activityInfo.applicationInfo);
                    co.appIntent = new Intent(intent);

                    LoggerGeneral.info(res.activityInfo.packageName + " " + res.activityInfo.name);
                    co.appIntent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));

                    cropOptions.add(co);
                }

                CropOptionAdapter adapter = new CropOptionAdapter(context, cropOptions);

                AlertDialog.Builder builder = new AlertDialog.Builder(context,AlertDialog.THEME_DEVICE_DEFAULT_DARK);
                builder.setTitle("Choose Crop App");
                builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        startActivityForResult(cropOptions.get(item).appIntent, requestCodeForCorp);
                    }
                });
                builder.setOnKeyListener(new DialogInterface.OnKeyListener() {
                    @Override
                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                        return false;
                    }
                });
                builder.setOnCancelListener(new DialogInterface.OnCancelListener() {

                    public void onCancel(DialogInterface dialog) {
                        if (mImageCaptureUri != null) {

                            getContentResolver().delete(mImageCaptureUri, null, null);

                        }
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
            }
        }
    }
    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        // String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        Log.e("string", "in Byte" + imageBytes);
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);

       /* final int lnth=bmp.getByteCount();
        ByteBuffer dst= ByteBuffer.allocate(lnth);
        bmp.copyPixelsToBuffer(dst);
        byte[] barray=dst.array();
        String encodedImage = Base64.encodeToString(barray, Base64.DEFAULT);*/
        return encodedImage;
    }

    public static void dataNotify()
    {
        adapter.notifyDataSetChanged();
    }


    class  UserPost extends AsyncTask<String, String, JSONObject> {
        String type;
        ProgressDialog mDialog;
        @Override
        protected JSONObject doInBackground(String... params) {
            // TODO Auto-generated method stub

            String url= Constants.Base+Constants.UserSocietyPost;

            JSONObject object = new JSONObject();
            try {

                //    object.put("society_id",SocietyId);
                object.put("user_id",appPreferences.getString("user_id",""));
                object.put("society_id",SocietyId);
                object.put("post_description",u_postdesc);
                object.put("post_image",postimage);
                object.put("image_extension","jpg");

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

                    int account_status = meta.getInt("account_status");

                    if(account_status==1){
                        if (status_code == 0) {


                            Intent dashBoard=new Intent(context,DashBoard.class);
                            dashBoard.putExtra("SocietyId",SocietyId);
                            Common.internet_check=0;
                            context.startActivity(dashBoard);

                            ((Activity) context).finish();

                        }
                    }

                    if(account_status == 0) {
                        Intent intent = new Intent(context,Login.class);
                        appPreferences.remove("user_id");
                        appPreferences.remove("user_name");
                        appPreferences.remove("email_id");
                        appPreferences.remove("storecoverimage");
                        appPreferences.remove("storeimage");
                        appPreferences.remove("profile_image_url");
                        Common.internet_check=0;
                        startActivity(intent);
                        ((Activity) context).finish();
                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mDialog.dismiss();


            }
        }

    }





    class  GetPosts extends AsyncTask<String, String, JSONObject> {
        String type;

        @Override
        protected JSONObject doInBackground(String... params) {
            // TODO Auto-generated method stub

            String url= Constants.Base+Constants.getDetailUserPost;

            JSONObject object = new JSONObject();
            try {

                //    object.put("society_id",SocietyId);
                object.put("society_id",SocietyId);
                object.put("user_id",duser_id);
                LoggerGeneral.info("Society post id22" + SocietyId);

                //      object.put("property_id",property_id);


                LoggerGeneral.info("JsonObjectPrintUserDetail" + object.toString());

            } catch (Exception ex) {

            }

            String str = '"' + appPreferences.getString("jwt", "") + '"';
            JSONObject jsonObject  = ServiceFacade.getResponsJsonParams(url, object);

            Log.d("hi", "getresponse" + jsonObject);

            Log.d("hi", "getresponse" + jsonObject);


            return jsonObject;
        }

        @Override
        protected void onCancelled(){

            super.onCancelled();
            //mDialog.dismiss();
            LoggerGeneral.info("onCancelled");

        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

            mDialog123 = new ProgressDialog(context,ProgressDialog.THEME_HOLO_DARK);
            mDialog123.setMessage("Processing...");
            mDialog123.show();
            mDialog123.setCancelable(false);
            mDialog123.setCanceledOnTouchOutside(false);
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

                    LoggerGeneral.info("");
                    if(account_status==1){
                        if (status_code == 0) {


                            JSONArray jsonArray = results.getJSONArray("data");


                            for(int i = jsonArray.length()-1;0<=i;i--) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                PostModel postModel  =new PostModel();
                                id                      = jsonObject.getString("id");
                                user_id                 = jsonObject.getString("user_id") ;
                                society_id              = jsonObject.getString("society_id");
                                post_description        = jsonObject.getString("post_description");
                                post_image              = jsonObject.getString("post_image");
                                status                  = jsonObject.getString("status");
                                created_on              = jsonObject.getString("created_on");
                                days_pass               = jsonObject.getString("days_pass");
                                post_image_url          = jsonObject.getString("post_image_url");
                                created_by              = jsonObject.getString("created_by");
                                profile_image_url       = jsonObject.getString("profile_image_url");
                                JSONArray commentsArray = jsonObject.getJSONArray("comments");


                                String myFormat1 = "yyyy-MM-dd hh:mm:ss";
                                String myFormat = "dd MMM, yyyy";
                                SimpleDateFormat sdf1 = new SimpleDateFormat(myFormat1, Locale.US);
                                Date myDate = null;
                                try {
                                    myDate = sdf1.parse(created_on);

                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);


                                created_on = sdf.format(myDate);

                                postModel.setId(id);
                                postModel.setUser_id(user_id);
                                postModel.setSociety_id(society_id);
                                postModel.setPost_description(post_description);
                                postModel.setPost_image(post_image);
                                postModel.setStatus(status);
                                postModel.setCreated_on(created_on);
                                postModel.setDays_pass(days_pass);
                                postModel.setPost_image_url(post_image_url);
                                postModel.setCreated_by(created_by);
                                postModel.setProfile_image_url(profile_image_url);

                                List<CommentModel> commentModels=new ArrayList<CommentModel>();
                                for(int j=0;j<commentsArray.length();j++)
                                {
                                    JSONObject commentsObject=commentsArray.getJSONObject(j);
                                    CommentModel commentModel=new CommentModel();

                                    int id=commentsObject.getInt("id");
                                    String comment=commentsObject.getString("comment");
                                    String userName=commentsObject.getString("name");
                                    String profileImage=commentsObject.getString("profile_image");
                                    String userId=commentsObject.getString("user_id");


                                    commentModel.setUserId(userId);
                                    commentModel.setComment(comment);
                                    commentModel.setId(id);
                                    commentModel.setUserName(userName);
                                    commentModel.setProfileImage(profileImage);
                                    commentModels.add(commentModel);

                                  /*  if(commentsArray.length()>0) {

                                        noofcomments.setText(commentsArray.length() + " Comments");

                                    }
                                    else{
                                        noofcomments.setText("0 Comments");
                                    }*/
                                }

                                LoggerGeneral.info("commentssize---"+commentsArray.length()+"---"+commentModels.size());

                                postModel.setCommentModel(commentModels);


                                commentlengh=commentModels.size();

                                postModels.add(postModel);
                            }


                            adapter=new PostAdapter(context,postModels,society_id,"noclick");
                            recyclerView.setAdapter(adapter);

                        //    mDialog123.dismiss();
                        }

                        if(status_code==1){
                            mDialog123.dismiss();
                        }

                    }

                    if(account_status == 0) {
                        Intent intent = new Intent(context,Login.class);
                        appPreferences.remove("user_id");
                        appPreferences.remove("user_name");
                        appPreferences.remove("email_id");
                        appPreferences.remove("storecoverimage");
                        appPreferences.remove("storeimage");
                        appPreferences.remove("profile_image_url");
                        Common.internet_check=0;
                        startActivity(intent);
                        ((Activity) context).finish();
                    }



                } catch (JSONException e) {
                    e.printStackTrace();
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


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent DashBoard = new Intent(UserDetailPost.this, DashBoard.class);
        Common.internet_check = 0;
        //intentCheck=0;
        DashBoard.putExtra("SocietyId", SocietyId);
        startActivity(DashBoard);
        finish();







    }
}
