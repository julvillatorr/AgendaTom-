package com.tesis.agendatome.notification;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.tesis.agendatome.MainActivity;
import com.tesis.agendatome.R;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class GcmIntentService extends IntentService {
    public static final int NOTIFICATION_ID = 1;
    private static final String TAG = "GcmIntentService";
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;
 
    public GcmIntentService() {
        super("GcmIntentService");
    }
 
    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        String messageType = gcm.getMessageType(intent);

 
        if (!extras.isEmpty()) {
        	if (GoogleCloudMessaging.
                    MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                sendNotification("Send error: " + extras.toString());
            } else if (GoogleCloudMessaging.
                    MESSAGE_TYPE_DELETED.equals(messageType)) {
                sendNotification("Deleted messages on server: " +
                        extras.toString());
            } else if (GoogleCloudMessaging.
                    MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                for (int i=0; i<5; i++) {
                    Log.i(TAG, "Working... " + (i+1)
                            + "/5 @ " + SystemClock.elapsedRealtime());
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                    }
                }
                Log.i(TAG, "Completed work @ " + SystemClock.elapsedRealtime());
                sendNotification(extras.getString("Notice"));
                Log.i(TAG, "Received: " + extras.toString());
            }
        }
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }
 
   private void sendNotification(String msg) {
        mNotificationManager = (NotificationManager)this.getSystemService(Context.NOTIFICATION_SERVICE);
        String title = "Nuevo evento en Tomé!";
        String subText = "Revisa el listado aquí.";
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("tab", "1");
        
        PendingIntent contentIntent = PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_ONE_SHOT);
 
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
	        .setSmallIcon(R.drawable.icon_launcher_black)
	        .setAutoCancel(true)
	        .setWhen(System.currentTimeMillis())
	        .setContentTitle(title)
	        .setContentText(subText)
	        .setStyle(new NotificationCompat.BigTextStyle().bigText(subText))
	        .setVibrate(new long[] {250,250,250,250,250})
	        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
	        .setLights(Color.CYAN, 3000, 3000)
	        .setContentIntent(contentIntent);
        
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }
}