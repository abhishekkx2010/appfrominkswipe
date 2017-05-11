package com.inkswipe.SocialSociety;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Build;

import java.util.List;

import util.Common;

/**
 * Created by nitin.c on 11/7/2016.
 */
public class CheckConnectivity extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent arg1) {
        //Common.showToast(context, String.valueOf(isAppIsInBackground(context)));
        boolean isConnected = arg1.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
        if (isConnected) {
            //Common.showToast(context, "Internet Connection Lost");


        } else {
            //Common.showToast(context,"Internet connected");

            if (!isAppIsInBackground(context)) {

                switch (Common.internet_check) {
                    case 1:
                        Intent intent = new Intent(context, Profile.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.putExtra("bundle", Profile.temp_bundle);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                        Intent intentf = new Intent("finish_activity");
                        context.sendBroadcast(intentf);
                        break;
                    case 2:
                        Intent intent2 = new Intent(context, MyProperty.class);
                        intent2.putExtra("bundle", MyProperty.temp_bundle);
                        intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent2);
                        Intent intentf2 = new Intent("finish_activity");
                        context.sendBroadcast(intentf2);
                        break;
                    case 3:
                        Intent intent3 = new Intent(context, ArchivedClosedPolls.class);
                        intent3.putExtra("bundle", ArchivedClosedPolls.temp_bundle);
                        intent3.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent3);
                        Intent intentf3 = new Intent("finish_activity");
                        context.sendBroadcast(intentf3);
                        break;
                    case 4:
                        Intent intent4 = new Intent(context, Archivedevent.class);
                        intent4.putExtra("bundle", Archivedevent.temp_bundle);
                        intent4.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent4);
                        Intent intentf4 = new Intent("finish_activity");
                        context.sendBroadcast(intentf4);
                        break;
                    case 5:
                        Intent intent5 = new Intent(context, ArchivedPolls.class);
                        intent5.putExtra("bundle", ArchivedPolls.temp_bundle);
                        intent5.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent5);
                        Intent intentf5 = new Intent("finish_activity");
                        context.sendBroadcast(intentf5);
                        break;
                    case 6:
                        Intent intent6 = new Intent(context, Offer.class);
                        intent6.putExtra("bundle", Offer.temp_bundle);
                        intent6.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent6);
                        Intent intentf6 = new Intent("finish_activity");
                        context.sendBroadcast(intentf6);
                        break;
                    case 7:
                        Intent intent7 = new Intent(context, Groups.class);
                        intent7.putExtra("bundle", Groups.temp_bundle);
                        intent7.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent7);
                        Intent intentf7 = new Intent("finish_activity");
                        context.sendBroadcast(intentf7);
                        break;
                    case 8:
                        Intent intent8 = new Intent(context, Members.class);
                        intent8.putExtra("bundle", Members.temp_bundle);
                        intent8.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent8);
                        Intent intentf8 = new Intent("finish_activity");
                        context.sendBroadcast(intentf8);
                        break;
                    case 9:
                        Intent intent9 = new Intent(context, EventDetails.class);
                        intent9.putExtra("bundle", EventDetails.temp_bundle);
                        intent9.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent9);
                        Intent intentf9 = new Intent("finish_activity");
                        context.sendBroadcast(intentf9);
                        break;
                    case 10:
                        Intent intent10 = new Intent(context, EventDetails.class);
                        intent10.putExtra("bundle", EventDetails.temp_bundle);
                        intent10.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent10);
                        Intent intentf10 = new Intent("finish_activity");
                        context.sendBroadcast(intentf10);
                        break;
                    case 11:
                        Intent intent11 = new Intent(context, DashBoard.class);
                        intent11.putExtra("bundle", DashBoard.temp_bundle);
                        intent11.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent11);
                        Intent intentf11 = new Intent("finish_activity");
                        context.sendBroadcast(intentf11);
                        break;
                    case 12:
                        Intent intent12 = new Intent(context, DashBoard.class);
                        intent12.putExtra("bundle", DashBoard.temp_bundle);
                        intent12.putExtra("polls", 2);
                        intent12.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent12);
                        Intent intentf12 = new Intent("finish_activity");
                        context.sendBroadcast(intentf12);
                        break;
                    case 13:
                        Intent intent13 = new Intent(context, DashBoard.class);
                        intent13.putExtra("bundle", DashBoard.temp_bundle);
                        intent13.putExtra("event", 3);
                        intent13.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent13);
                        Intent intentf13 = new Intent("finish_activity");
                        context.sendBroadcast(intentf13);
                        break;
                    case 14:
                        Intent intent14 = new Intent(context, DashBoard.class);
                        intent14.putExtra("bundle", DashBoard.temp_bundle);
                        intent14.putExtra("announcement", 4);
                        intent14.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent14);
                        Intent intentf14 = new Intent("finish_activity");
                        context.sendBroadcast(intentf14);
                        break;
                    case 15:
                        Intent intent15 = new Intent(context, EventsList.class);
                        intent15.putExtra("bundle", EventsList.temp_bundle);
                        intent15.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent15);
                        Intent intentf15 = new Intent("finish_activity");
                        context.sendBroadcast(intentf15);
                    case 16:
                        Intent intent16 = new Intent(context, EventDetails.class);
                        intent16.putExtra("bundle", EventDetails.temp_bundle);
                        intent16.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent16);
                        Intent intentf16 = new Intent("finish_activity");
                        context.sendBroadcast(intentf16);
                        break;
                    /*case 17:
                        Intent intent17 = new Intent(context, PollDetails.class);
                        intent17.putExtra("bundle", EventDetails.temp_bundle);
                        intent17.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent17);
                        Intent intentf17 = new Intent("finish_activity");
                        context.sendBroadcast(intentf17);
                        break;
                    case 18:
                        Intent intent18 = new Intent(context, PollDetails.class);
                        intent18.putExtra("bundle", EventDetails.temp_bundle);
                        intent18.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent18);
                        Intent intentf18 = new Intent("finish_activity");
                        context.sendBroadcast(intentf18);
                        break;*/
                }


            }
        }
    }

    private boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }

        return isInBackground;
    }

}