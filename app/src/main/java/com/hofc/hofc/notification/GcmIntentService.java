package com.hofc.hofc.notification;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.hofc.hofc.Accueil;
import com.hofc.hofc.ActusDetail;
import com.hofc.hofc.ActusImageGrid;
import com.hofc.hofc.R;

/**
 * Created by Fixe on 29/11/2014.
 * Used to receive notification from GCM
 */
public class GcmIntentService extends IntentService {

    private static int NOTIFICATION_ID = 1;


    private static final String MESSAGE_TITLE = "title";
    private static final String MESSAGE_CONTENT = "message";

    public GcmIntentService() {
        super("Test");
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public GcmIntentService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        String messageType = gcm.getMessageType(intent);
        if(!extras.isEmpty()) {
            if(GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                sendNotification(extras);
                NOTIFICATION_ID++;
            }
        }
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    private void sendNotification(Bundle extras) {
        if(extras != null && !extras.isEmpty()) {
            String title = extras.getString(MESSAGE_TITLE);
            String message = extras.getString(MESSAGE_CONTENT);
            NotificationManager mNotificationManager = (NotificationManager)
                    this.getSystemService(Context.NOTIFICATION_SERVICE);
            PendingIntent contentIntent;
            Intent intent;
            if("Calendrier".equalsIgnoreCase(extras.getString("TYPE"))) {
                intent = new Intent(this, Accueil.class);
                intent.setAction("OPEN_CALENDRIER");
            } else if("Actu".equalsIgnoreCase(extras.getString("TYPE"))) {
                String url = extras.getString("URL");
                if(url != null && url.contains("en-images")) {
                    intent = new Intent(this, ActusImageGrid.class);
                } else {
                    intent = new Intent(this, ActusDetail.class);
                }
                intent.putExtra("URL", extras.getString("URL"));
            } else {
                intent = new Intent(this, Accueil.class);
            }
            contentIntent = PendingIntent.getActivity(this, 0, intent, 0);
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(this)
                            .setSmallIcon(R.drawable.ic_launcher)
                            .setContentTitle(title)
                            .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText(message))
                            .setContentText(message)
                            .setAutoCancel(true);

            mBuilder.setContentIntent(contentIntent);
            mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
        }
    }
}
