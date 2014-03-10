package vn.techcamp.services;

import android.app.IntentService;
import android.app.Notification;
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
        } else if (ACTION_RECEIVE_NOTIFICATION.equals(action)) {
            handleGcmIntent(intent);
        }
    }

    private void doRegisterGCM() {
        int retryCounts = 1;
        long retryTimes = 10000;
        do {
            try {
                GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
                String regid = gcm.register(SENDER_ID);
                // Persist the regID - no need to register again.
                Logging.debug("Register Id " + regid);
                HttpService.registerDeviceToken(getApplicationContext(), regid);
                PreferenceUtils.storeGcmRegId(getApplicationContext(), regid);
                break;
            } catch (IOException ex) {
                Logging.error(ex.getMessage(), ex);
                retryCounts++;
                try {
                    Thread.sleep(retryTimes);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                retryTimes *= 2;
            }
        } while (retryCounts > 3);
        stopSelf();
    }

    private void handleGcmIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty()) {  // has effect of unparcelling Bundle
            Logging.debug("Received " + extras);
            if (GoogleCloudMessaging.
                    MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                String content = extras.getString("default");
                sendNotification(content);
            }
        }
        GCMNotificationReceiver.completeWakefulIntent(intent);
        stopSelf();
    }

    private void sendNotification(String msg) {
        mNotificationMgr = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent showAnnouncementPageIntent = new Intent(this, MainActivity.class);
        showAnnouncementPageIntent.putExtra(MainActivity.EXTRA_TAB_INDEX, MainActivity.ANNOUNCEMENT_TAB);
        showAnnouncementPageIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, showAnnouncementPageIntent
                , PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.techcamp_logo)
                        .setContentTitle(getString(R.string.app_name))
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(msg))
                        .setContentText(msg)
                        .setAutoCancel(true)
                        .setDefaults(Notification.DEFAULT_ALL);

        mBuilder.setContentIntent(contentIntent);
        mNotificationMgr.notify(NOTIFICATION_ID, mBuilder.build());
    }
}
