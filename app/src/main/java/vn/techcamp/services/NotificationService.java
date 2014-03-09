package vn.techcamp.services;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;

import vn.techcamp.activities.MainActivity;
import vn.techcamp.android.R;
import vn.techcamp.receivers.GCMNotificationReceiver;
import vn.techcamp.utils.Logging;
import vn.techcamp.utils.PreferenceUtils;

/**
 * @author Jupiter (vu.cao.duy@gmail.com) on 3/9/14.
 */
public class NotificationService extends IntentService {
    public static final String ACTION_REGISTER_GCM = "vn.techcamp.android.ACTION_REGISTER_GCM";
    public static final String ACTION_RECEIVE_NOTIFICATION = "com.google.android.c2dm.intent.RECEIVE";
    private static final String SENDER_ID = "401779820821";
    private static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationMgr;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public NotificationService() {
        super("NotificationService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mNotificationMgr = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String action = intent.getAction();
        if (ACTION_REGISTER_GCM.equals(action)) {
            doRegisterGCM();
            stopSelf();
        } else if (ACTION_RECEIVE_NOTIFICATION.equals(action)) {
            handleGcmIntent(intent);
        }
    }

    private void doRegisterGCM() {
        try {
            GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
            String regid = gcm.register(SENDER_ID);
            // Persist the regID - no need to register again.
            HttpService.registerDeviceToken(getApplicationContext(), regid);
            PreferenceUtils.storeGcmRegId(getApplicationContext(), regid);
        } catch (IOException ex) {
            Logging.error(ex.getMessage(), ex);
        }
    }

    private void handleGcmIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty()) {  // has effect of unparcelling Bundle
            Logging.debug("Received " + extras);
            if (GoogleCloudMessaging.
                    MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                sendNotification("Hello");
//                sendNotification("Received: " + extras.toString());
            }
        }
        GCMNotificationReceiver.completeWakefulIntent(intent);
        stopSelf();
    }

    private void sendNotification(String msg) {
        mNotificationMgr = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), 0);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.techcamp_logo)
                        .setContentTitle("GCM Notification")
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(msg))
                        .setContentText(msg);

        mBuilder.setContentIntent(contentIntent);
        mNotificationMgr.notify(NOTIFICATION_ID, mBuilder.build());
    }
}
