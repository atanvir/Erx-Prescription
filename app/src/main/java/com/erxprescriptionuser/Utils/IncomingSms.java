package com.erxprescriptionuser.Utils;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Button;

import com.chaos.view.PinView;

public class IncomingSms extends BroadcastReceiver {
    private PinView pinView;
    private Button btnSubmit;
    private String otp;
    private Activity activity;

    public IncomingSms(Activity activity,PinView pinView, Button btnSubmit)
    {
        this.pinView=pinView;
        this.btnSubmit=btnSubmit;
        this.activity=activity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("came","yes");
       SmsMessage smsMessage[]= Telephony.Sms.Intents.getMessagesFromIntent(intent);
       for(SmsMessage message:smsMessage)
       {
           Log.e("Message",message.getMessageBody());
           if(message.getMessageBody().contains("is your verification code."))
           {
              otp= message.getMessageBody().split("is your verification code.")[0];
              Log.e("otp",otp);
              pinView.setText(otp);
              btnSubmit.performClick();
           }
       }

    }
}
