package io.github.android.tang.tony.host;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import edu.cs4730.foregroundservicedemo.R;
import hugo.weaving.DebugLog;

@DebugLog
class NotificationHelper {

    private final Context context;
    private final NotificationManager notificationManager;
    public static String CHANNEL_ID = "foreground_service";

    public NotificationHelper(Context context) {
        this.context = context;
        this.notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(CHANNEL_ID, context.getString(R.string.channel_name));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotificationChannel(String channelId, String channelName) {
        NotificationChannel notificationChannel =
                new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_MIN);
        getNotificationManager().createNotificationChannel(notificationChannel);
    }

    private NotificationCompat.Builder buildOngoingNotification(String title, String body) {
        return new NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(body)
                .setContentIntent(constructMainActivityPendingIntent())
                .setSmallIcon(getSmallIcon())
                .addAction(constructStopAction())
                .setAutoCancel(true);
    }

    private NotificationCompat.Action constructStopAction() {
        return new NotificationCompat.Action(getSmallIcon(), context.getString(R.string.stop),
                constructStopPendingIntent());
    }

    private PendingIntent constructStopPendingIntent() {
        Intent intent = ServiceAbortionActionBroadcastReceiver.constructIntent();
        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private PendingIntent constructMainActivityPendingIntent() {
        Intent intent = mainActivityIntent();
        return PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private Intent mainActivityIntent() {
        PackageManager pm = context.getPackageManager();
        return pm.getLaunchIntentForPackage(context.getPackageName());
    }

    private int getSmallIcon() {
        return android.R.drawable.sym_def_app_icon;
    }

    private NotificationManager getNotificationManager() {
        return notificationManager;
    }


    public void bindAsForegroundService(Service service) {
        String title = context.getString(R.string.app_name);
        String content = context.getString(R.string.foreground_service_is_running);
        Notification notification = buildOngoingNotification(title, content).build();
        service.startForeground(CHANNEL_ID.hashCode(), notification);
    }
}