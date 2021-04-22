package com.erxprescriptionuser.Firebase;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;

import androidx.annotation.NonNull;

import com.erxprescriptionuser.Activity.MainActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessageService extends FirebaseMessagingService {
    String TAG=MyFirebaseMessageService.class.getSimpleName();
    public static INotificationCount listner;

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        Log.e(TAG,"data:"+remoteMessage.getData());
        Log.e(TAG,"notification:"+remoteMessage.getNotification());
        if(remoteMessage.getData().size()>0) {
            if(listner!=null) {listner.onMessageReceived(remoteMessage.getData().get("type"));}
            handleMessage(remoteMessage.getData().get("title"), remoteMessage.getData().get("body"));
        }
        else if(remoteMessage.getNotification()!=null) {
            handleMessage(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
        }
    }

    private void handleMessage(String title, String body) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra("cameFrom", MyFirebaseMessageService.class.getSimpleName());
        showNotificationMessage(getApplicationContext(), title, body, intent, 5);
    }

    private void showNotificationMessage(Context context, String title, String message, Intent intent, int id) {
        NotificationHelper notificationHelper = new NotificationHelper(context);
        notificationHelper.createNotification(title, message, intent, id);
    }



    public static void setListner(INotificationCount listen)
    {
        listner=listen;
    }

    public interface INotificationCount
    {
        void onMessageReceived(String type);
    }



}
