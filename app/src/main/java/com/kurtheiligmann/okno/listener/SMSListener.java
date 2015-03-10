package com.kurtheiligmann.okno.listener;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;

import com.kurtheiligmann.okno.data.DataManager;
import com.kurtheiligmann.okno.media.MediaManager;

public class SMSListener extends BroadcastReceiver {
    private static final String PDUS_BUNDLE_NAME = "pdus";
    private static Context context;

    public static Context getContext() {
        return context;
    }

    public static void setContext(Context context) {
        SMSListener.context = context;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        DataManager dataManager = new DataManager(context);
        boolean listenerEnabled = dataManager.getEnabled();
        dataManager.close();
        if (listenerEnabled && intent.getAction().equals(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                Object[] pdus = (Object[]) bundle.get(PDUS_BUNDLE_NAME);
                SmsMessage[] messages = new SmsMessage[pdus.length];
                MediaManager mediaManager = new MediaManager(context);
                for (int i = 0; i < messages.length; i++) {
                    messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);

                    String msgBody = messages[i].getMessageBody();
                    mediaManager.playToneForMessageBody(msgBody.toLowerCase());
                    Log.i("SMSListener", "message: " + msgBody);
                }
            }
        }
    }
}
