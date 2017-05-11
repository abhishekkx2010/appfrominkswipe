package com.inkswipe.SocialSociety;

import android.annotation.TargetApi;
import android.app.*;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;


/**
 * Created by nitin.c on 10/3/2016.
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    public static int id=0;
    Bitmap d;
    Map data;
    RemoteMessage remoteMessage;
    String notification,description,external_uri,SocietyId,eventId,eventType,imageurl;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //Displaying data in log
        //It is optional
        id++;
        data = remoteMessage.getData();
        this.remoteMessage=remoteMessage;
        Log.d(TAG, "From: " + remoteMessage.getFrom() + "======" + (String) data.get("message") + "=====" + remoteMessage.getData());
        //Log.d(TAG, "NotificationApp Message Body: " + remoteMessage.getNotification().getBody());
         imageurl= (String) data.get("image_url");
        d=getBitmapFromURL(imageurl);
         notification= (String) data.get("notification_title");
         description= (String) data.get("description");
         external_uri= (String) data.get("url");

         SocietyId= (String) data.get("society_id");
         eventId=(String) data.get("event_id");
         eventType=(String) data.get("event_type");

        if(imageurl!=null) {
            ArrayList<String> notificationData = new ArrayList<String>();
            notificationData.add(notification);
            notificationData.add(description);
            notificationData.add(external_uri);
            notificationData.add(imageurl);
            //Calling method to generate notification
            sendNotification(notificationData);
        }
        else
        {
            ArrayList<String> notificationData = new ArrayList<String>();
            notificationData.add(SocietyId);
            notificationData.add(eventId);
            notificationData.add(eventType);
            //Calling method to generate notification
            sendNotification(notificationData);
        }
    }

    //This method is only generating push notification
    //It is same as we did in earlier posts
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void sendNotification(ArrayList<String> notificationData) {
// Creates an explicit intent for an ResultActivity to receive.

        Intent resultIntent=new Intent(this,MainActivity.class);
        resultIntent.putExtra("SocietyId",SocietyId);
        resultIntent.putExtra("event_id", eventId);
        resultIntent.putExtra("event_type", eventType);
        resultIntent.putExtra("fromPushNotification", "fromPushNotification");

        // This ensures that the back button follows the recommended convention for the back key.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);

        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(MainActivity.class);

        // Adds the Intent that starts the Activity to the top of the stack.
        stackBuilder.addNextIntent(resultIntent);


        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(id, PendingIntent.FLAG_UPDATE_CURRENT);

        // Create remote view and set bigContentView.
        RemoteViews expandedView = new RemoteViews(this.getPackageName(), R.layout.customnotification);
        expandedView.setTextViewText(R.id.text_view, notificationData.get(0));
        expandedView.setTextViewText(R.id.text_description, notificationData.get(1));
        expandedView.setImageViewBitmap(R.id.notificationBackground, d);




        if(external_uri!=null && external_uri.length()>0)
        {
            Intent resultIntent2 = new Intent(Intent.ACTION_VIEW);
            resultIntent2.setData(Uri.parse(external_uri));
            PendingIntent pending = PendingIntent.getActivity(this, id, resultIntent2, PendingIntent.FLAG_UPDATE_CURRENT);
            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Notification notification = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.mipmap.launcher)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pending)
                    .setContentTitle("Social Society").build();


            notification.bigContentView = expandedView;




            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


            notificationManager.notify(id /* ID of notification */, notification);
        }
        else if(imageurl!=null || notification!=null || description!=null){
            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Notification notification = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.mipmap.launcher)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(resultPendingIntent)
                    .setContentTitle("Social Society").build();

            
            notification.bigContentView = expandedView;

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


            notificationManager.notify(id  /*ID of notification*/ , notification);

        }
        else if(SocietyId!=null)
        {
            data=remoteMessage.getData();
            String message= (String) data.get("message");
            message=message.replace("| ","");
            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.mipmap.launcher)
                    .setContentTitle("Social Society")
                    .setContentText(message)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(resultPendingIntent);

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(id /* ID of notification */, notificationBuilder.build());
        }


    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }
}

