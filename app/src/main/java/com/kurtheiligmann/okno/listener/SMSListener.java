package com.kurtheiligmann.okno.listener;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import com.kurtheiligmann.okno.data.DataManager;
import com.kurtheiligmann.okno.media.MediaManager;

/**
 * Created by kurtheiligmann on 2/9/15.
 */
public class SMSListener extends BroadcastReceiver {
    public static Context getContext() {
        return context;
    }

    public static void setContext(Context context) {
        SMSListener.context = context;
    }

    private static Context context;
    private static final String SMS_RECEIVED_ACTION = "android.provider.Telephony.SMS_RECEIVED";
    private static final String PDUS_BUNDLE_NAME = "pdus";

    @Override
    public void onReceive(Context context, Intent intent) {
        DataManager dataManager = new DataManager(context);
        boolean listenerEnabled = dataManager.getEnabled();
        dataManager.close();
        if (listenerEnabled && intent.getAction().equals(SMS_RECEIVED_ACTION)) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                Object[] pdus = (Object[]) bundle.get(PDUS_BUNDLE_NAME);
                SmsMessage[] messages = new SmsMessage[pdus.length];
                MediaManager mediaManager = new MediaManager(context);
                for (int i = 0; i < messages.length; i++) {
                    messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);

                    String msgBody = messages[i].getMessageBody();
                    mediaManager.playToneForMessageBody(msgBody);
                    Log.i("SMSListener", "message: " + msgBody);
                }
            }
        }
    }
}
