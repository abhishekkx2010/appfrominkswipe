package com.inkswipe.SocialSociety;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

/**
 * Created by Ajinkya on 19-04-2016.
 */
public class SmsReceiver extends BroadcastReceiver {

    private static SmsListener mListener;

    public static String senderid,smessageBody;


    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle data  = intent.getExtras();

        Object[] pdus = (Object[]) data.get("pdus");

        for(int i=0;i<pdus.length;i++){
            SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdus[i]);

            String sender = smsMessage.getDisplayOriginatingAddress();


            senderid=sender;

            Log.d("hiisender","sendename"+sender);

            //You must check here if the sender is your provider and not another one with same text.

            String messageBody = smsMessage.getMessageBody();

            smessageBody=messageBody;

            Log.d("hiisender","sendermessage"+messageBody);

            //Pass on the text to our listener.
            if(mListener!=null) {
                mListener.messageReceived(messageBody);
            }
        }

    }

    public static void bindListener(SmsListener listener) {
        mListener = listener;
    }
}
