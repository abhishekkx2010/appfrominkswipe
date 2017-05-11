package com.inkswipe.SocialSociety;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import util.AppPreferences;
import util.Common;

public class TermsCond extends AppCompatActivity {
    ImageButton imageButton;
    AppPreferences appPreferences;
    Context context;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_cond);



        context  =TermsCond.this;
        appPreferences = AppPreferences.getAppPreferences(context);
        bitmap = BitmapFactory.decodeResource(TermsCond.this.getResources(),
                R.mipmap.fallb);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView title = ((TextView) toolbar.findViewById(R.id.toolbar_title));
        title.setText("Terms and Conditions");
        title.setTypeface(Common.font(context, "arial.ttf"), 1);
        toolbar.setTitleTextColor(android.graphics.Color.WHITE);



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.my_statusbar_color));
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.getNavigationIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
        getSupportActionBar().setDisplayShowTitleEnabled(false);



        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //What to do on back clicked
               /* Intent intent = new Intent(TermsCond.this, RegisterPreview.class);
                startActivity(intent);*/
                finish();
            }
        });



       /* imageButton = (ImageButton) findViewById(R.id.imageButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
            }
        });*/

    }
}
