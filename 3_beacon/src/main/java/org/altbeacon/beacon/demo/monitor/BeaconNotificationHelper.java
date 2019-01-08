package org.altbeacon.beacon.demo.monitor;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import hugo.weaving.DebugLog;
import io.github.android.tang.tony.host.config.NotificationConfigBuilder;

@DebugLog
class BeaconNotificationHelper {

    private static final String BEACON_MESSAGE_CHANNEL_ID = "BEACON_MESSAGE_CHANNEL_ID";
    private final Context context;
    private final NotificationManager notificationManager;

    public BeaconNotificationHelper(Context context) {
        this.context = context;
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotificationChannel() {
        getNotificationManager().createNotificationChannel(channel());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private NotificationChannel channel() {
        return new NotificationChannel(BEACON_MESSAGE_CHANNEL_ID, "Beacon Event",
                NotificationManager.IMPORTANCE_DEFAULT);
    }


    private NotificationCompat.Builder buildBeaconNotification(String beaconUuid) {
        return new NotificationCompat.Builder(context, BEACON_MESSAGE_CHANNEL_ID)
                .setContentTitle(context.getString(R.string.beacon_detected))
                .setContentText(beaconUuid)
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentIntent(launchPendingIntent());
    }


    private PendingIntent launchPendingIntent() {
        Intent intent = NotificationConfigBuilder.defaultLaunchIntent(context);
        return PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private NotificationManager getNotificationManager() {
        return notificationManager;
    }


    public void showBeaconNotification(String uuid) {
        Notification notification = buildBeaconNotification(uuid).build();
        notificationManager.notify(BEACON_MESSAGE_CHANNEL_ID.hashCode(), notification);
    }

    public void cancel() {
        notificationManager.cancel(BEACON_MESSAGE_CHANNEL_ID.hashCode());
    }

}