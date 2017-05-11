package com.inkswipe.SocialSociety;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.inkswipe.SocialSociety.pulltozoomview.PullToZoomScrollViewEx;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

import util.AppPreferences;
import util.Common;
import util.Constants;
import util.ExceptionHandler;
import util.LoggerGeneral;
import util.ServiceFacade;

public class RegisterPreview extends AppCompatActivity {
    Context context;

    PullToZoomScrollViewEx scrollView;
    LinearLayout prpublish;
    String mImage;
    Bitmap strbitmap;
    ImageView rphoto;
    Bitmap bitmap;

    String uname;
    String uemailid;
    String upassword;
    String uconpassword;
    String umobno;
    String gender;
    String uaddress;
    String ulandmark;
    String state;
    String city;
    String pincode;

    ImageView primage;
    AppPreferences appPreferences;
    String android_id;
    TextView tname,temail,tmobile,tgender,taddress,tlandmark,tstate,tcity,tpincode,ch2;
    String postbitmap;
    String bImage;
    CheckBox ch1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        setContentView(R.layout.activity_register_preview);

        context  =RegisterPreview.this;

        appPreferences = AppPreferences.getAppPreferences(context);
        bitmap = BitmapFactory.decodeResource(RegisterPreview.this.getResources(),
                R.mipmap.fallb);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        TextView title=((TextView) toolbar.findViewById(R.id.toolbar_title));
        title.setText("Preview");
        title.setTypeface(Common.font(context, "arial.ttf"), 1);
        toolbar.setTitleTextColor(android.graphics.Color.WHITE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.my_statusbar_color));
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

     //   toolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.backarrow));

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //What to do on back clicked

                if (rphoto.getDrawable() instanceof BitmapDrawable) {
                    strbitmap = ((BitmapDrawable) rphoto.getDrawable()).getBitmap();
                } else {
                    Drawable d = rphoto.getDrawable();
                    strbitmap = Bitmap.createBitmap(d.getIntrinsicWidth(), d.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
                    Canvas canvas = new Canvas(bitmap);
                    d.draw(canvas);
                }

                Intent intent = new Intent(RegisterPreview.this, Register.class);

                intent.putExtra("prbtimage", strbitmap);
                intent.putExtra("frompreview","frompreview");
                intent.putExtra("username",uname);
                intent.putExtra("email",uemailid);
                intent.putExtra("password","");
                intent.putExtra("conpassword","");
                intent.putExtra("mobile",umobno);
                intent.putExtra("gender",gender);
                intent.putExtra("address",uaddress);
                intent.putExtra("landmark",ulandmark);
                intent.putExtra("State",state);
                intent.putExtra("City",city);
                intent.putExtra("prpincode",pincode);
                startActivity(intent);
                finish();
            }
        });
        loadViewForCode();
        scrollView = (PullToZoomScrollViewEx)findViewById(R.id.scroll_View1);
        DisplayMetrics localDisplayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(localDisplayMetrics);
        int mScreenHeight = localDisplayMetrics.heightPixels;
        int mScreenWidth = localDisplayMetrics.widthPixels;
        LinearLayout.LayoutParams localObject = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (9.0F * (mScreenWidth / 16.0F)));
        scrollView.setHeaderLayoutParams(localObject);

        prpublish = (LinearLayout)scrollView.getRootView().findViewById(R.id.prpublish);

        prpublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               /* Intent profile = new Intent(RegisterPreview.this,Profile.class);
                startActivity(profile);
                finish();*/
                if(Common.isOnline(context)) {

                    if (rphoto.getDrawable() instanceof BitmapDrawable) {
                        strbitmap = ((BitmapDrawable) rphoto.getDrawable()).getBitmap();
                    } else {
                        Drawable d = rphoto.getDrawable();
                        strbitmap = Bitmap.createBitmap(d.getIntrinsicWidth(), d.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
                        Canvas canvas = new Canvas(bitmap);
                        d.draw(canvas);
                    }


                    postbitmap = getStringImage(strbitmap);

                    if(Common.isOnline(context)) {

                        if(ch1.isChecked()) {
                            new getRegistered().execute();
                        }else
                        {
                            Common.showToast(context, "Please accept terms and conditions");
                        }}
                    else
                    {
                        Common.showToast(context,"No internet connection");
                    }

                }
                else {
                    Common.showToast(context,"No internet connection");
                }

            }
        });

        mImage = getIntent().getStringExtra("primage");


        rphoto = (ImageView)findViewById(R.id.rphoto);


        android_id = Settings.Secure.getString(this.getContentResolver(),Settings.Secure.ANDROID_ID);

        Bitmap rbitmap = (Bitmap) getIntent().getParcelableExtra("btimage");

        LoggerGeneral.info("rbitmap----" + rbitmap);

        rphoto.setImageBitmap(rbitmap);


        tname = (TextView)findViewById(R.id.rpname);

        temail = (TextView)findViewById(R.id.email);

        tmobile = (TextView)findViewById(R.id.mobile);

        tgender = (TextView)findViewById(R.id.gender);

        taddress = (TextView)findViewById(R.id.address);

        tlandmark = (TextView)findViewById(R.id.landmark);

        tstate = (TextView)findViewById(R.id.state);

        tcity = (TextView)findViewById(R.id.city);

        tpincode = (TextView)findViewById(R.id.pincode);

        ch1 = (CheckBox) findViewById(R.id.ch1);
        ch1.setChecked(true);

        ch2 = (TextView) findViewById(R.id.ch2);
        ch2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(RegisterPreview.this,TermsCond.class);
                startActivity(i);
                //finish();
            }
        });



         uname        =  getIntent().getStringExtra("username");

         uemailid     =  getIntent().getStringExtra("email");
         upassword    =  getIntent().getStringExtra("password");
         uconpassword =  getIntent().getStringExtra("conpassword");
         umobno       =  getIntent().getStringExtra("mobile");
         gender       =  getIntent().getStringExtra("gender");
         uaddress     =  getIntent().getStringExtra("address");
         ulandmark    =  getIntent().getStringExtra("landmark");
         state        =  getIntent().getStringExtra("State");
         city         =  getIntent().getStringExtra("City");
         pincode      =  getIntent().getStringExtra("pincode");


        if(uname!=null){


        tname.setText(uname);


        }
        if(uemailid!=null){

            temail.setText(uemailid);
        }
        if(umobno!=null){

            tmobile.setText(umobno);
        }

        if(gender!=null){

            tgender.setText(gender);
        }

        if(uaddress!=null){

            taddress.setText(uaddress);
        }

        if(ulandmark!=null){

            tlandmark.setText(ulandmark);
        }

        if(state!=null){

            tstate.setText(state);
        }

        if(city!=null){

            tcity.setText(city);
        }

        if(pincode!=null){

            tpincode.setText(pincode);
        }


    }
    private void loadViewForCode() {
        PullToZoomScrollViewEx scrollView = (PullToZoomScrollViewEx) findViewById(R.id.scroll_View1);
        View headView = LayoutInflater.from(this).inflate(R.layout.register_preview_header, null, false);
        View zoomView = LayoutInflater.from(this).inflate(R.layout.register_preview_zoom, null, false);
        View contentView = LayoutInflater.from(this).inflate(R.layout.register_preview_content, null, false);
        scrollView.setHeaderView(headView);
        scrollView.setZoomView(zoomView);
        scrollView.setScrollContentView(contentView);
    }


    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        // String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        Log.e("string", "in Byte" + imageBytes);
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);

        return encodedImage;
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (rphoto.getDrawable() instanceof BitmapDrawable) {
            strbitmap = ((BitmapDrawable) rphoto.getDrawable()).getBitmap();
        } else {
            Drawable d = rphoto.getDrawable();
            strbitmap = Bitmap.createBitmap(d.getIntrinsicWidth(), d.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            d.draw(canvas);
        }

        Intent intent = new Intent(RegisterPreview.this, Register.class);


        intent.putExtra("prbtimage", strbitmap);
        intent.putExtra("frompreview","frompreview");
        intent.putExtra("username",uname);
        intent.putExtra("email",uemailid);
        intent.putExtra("password","");
        intent.putExtra("conpassword","");
        intent.putExtra("mobile",umobno);
        intent.putExtra("gender",gender);
        intent.putExtra("address",uaddress);
        intent.putExtra("landmark",ulandmark);
        intent.putExtra("State",state);
        intent.putExtra("City",city);
        intent.putExtra("pincode", pincode);





        startActivity(intent);
        finish();
    }



    class  getRegistered extends AsyncTask<String, String, JSONObject> {
        String type;
        ProgressDialog mDialog;
        @Override
        protected JSONObject doInBackground(String... params) {
            // TODO Auto-generated method stub

            String url= Constants.Base+Constants.Register;

            JSONObject object = new JSONObject();
            try {

                object.put("name",uname);
                object.put("email",uemailid);
                object.put("mobile",umobno);
                object.put("password",upassword);
                object.put("gender",gender);
                object.put("password",upassword);
                object.put("address",uaddress);
                object.put("state",state);
                object.put("city",city);
                object.put("pincode",pincode);
                object.put("landmark",ulandmark);
                object.put("device_id",android_id);
                object.put("device_type","1");
                object.put("device_token",appPreferences.getString("Token",""));
                object.put("profile_image",postbitmap);
                object.put("image_extension","jpeg");

                LoggerGeneral.info("JsonObjectPrint" + object.toString());

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
        /*    transparent.setVisibility(View.VISIBLE);
            rotateLoading.setVisibility(View.VISIBLE);
            rotateLoading.start();
*/
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
            mDialog.dismiss();

            if(results!=null){

                try {
                    JSONObject jsonObject =results.getJSONObject("meta");


                    int  status_code  = jsonObject.getInt("status_code");


                    if(status_code==0){


                        JSONObject data =results.getJSONObject("data");

                        String userid    =data.getString("user_id");
                        String user_name =data.getString("user_name");
                        String email_id  =data.getString("email_id");


                        //appPreferences.putString("user_id",userid);
                        //appPreferences.putString("user_name",user_name);


                        Intent login = new Intent(RegisterPreview.this,Login.class);
                        startActivity(login);
                        finish();

                        Common.showToast(context,"Successfully registered");

                    }
                    if (status_code==1)
                    {
                        Common.showToast(context,"Email id already exist");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

        }

    }
}
