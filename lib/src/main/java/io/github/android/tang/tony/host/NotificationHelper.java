package io.github.android.tang.tony.host;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import javax.inject.Inject;
import javax.inject.Named;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import hugo.weaving.DebugLog;
import io.github.android.tang.tony.host.config.NotificationConfig;
import io.github.android.tang.tony.host.config.NotificationConfig.ActionConfig;
import timber.log.Timber;

@DebugLog
class NotificationHelper {

    private final Context context;
    private final NotificationManager notificationManager;
    private final NotificationConfig notificationConfig;
    private final String applicationId;

    @Inject
    public NotificationHelper(Context context,
                              @Named("application_id") String applicationId,
                              NotificationManager notificationManager,
                              NotificationConfig notificationConfig) {
        this.context = context;
        this.applicationId = applicationId;
        this.notificationConfig = notificationConfig;
        this.notificationManager = notificationManager;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationConfig.Channel channel = notificationConfig.channel();
            createNotificationChannel(channel);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotificationChannel(NotificationConfig.Channel config) {
        getNotificationManager().createNotificationChannel(channel(config));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private NotificationChannel channel(NotificationConfig.Channel config) {
        return new NotificationChannel(config.channelId(), config.channelName(), config.importance());
    }

    private NotificationCompat.Builder buildOngoingNotification() {

        String title = notificationConfig.ui().title();
        String body = notificationConfig.ui().body();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, notificationConfig.channel().channelId())
                .setContentTitle(title)
                .setContentText(body)
                .setContentIntent(launchPendingIntent())
                .setSmallIcon(notificationConfig.ui().smallIcon())
                .setAutoCancel(true);

        ActionConfig stop = notificationConfig.ui().stop();
        builder.addAction(action(stop, Action.STOP));

        ActionConfig pause = notificationConfig.ui().pause();
        if (pause != null) {
            builder.addAction(action(pause, Action.PAUSE));
        }
        return builder;
    }

    private NotificationCompat.Builder buildOnPausedNotification(ActionConfig resume) {

        String title = notificationConfig.ui().title();
        String body = resume.body();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, notificationConfig.channel().channelId())
                .setContentTitle(title)
                .setContentText(body)
                .setContentIntent(launchPendingIntent())
                .setSmallIcon(notificationConfig.ui().smallIcon());

        builder.addAction(action(notificationConfig.ui().stop(), Action.STOP));
        builder.addAction(action(resume, Action.RESUME));
        return builder;
    }

    private NotificationCompat.Action action(ActionConfig config, @ActionType int action) {
        PendingIntent intent = actionIntent(action);
        return new NotificationCompat.Action(config.drawableId(), config.actionTitle(), intent);
    }

    private PendingIntent actionIntent(@ActionType int action) {
        Intent intent = ServiceAbortionActionBroadcastReceiver.constructIntent(applicationId, action);
        return PendingIntent.getBroadcast(context, action, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private PendingIntent launchPendingIntent() {
        Intent intent = notificationConfig.launchIntent();
        return PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private NotificationManager getNotificationManager() {
        return notificationManager;
    }

    public void bindAsForegroundService(Service service) {
        Notification notification = buildOngoingNotification().build();
        service.startForeground(notificationConfig.channel().channelId().hashCode(), notification);
    }


    public void onHostToSleep() {
        ActionConfig resume = notificationConfig.ui().resume();
        if (resume != null) {
            Notification notification = buildOnPausedNotification(resume).build();
            notificationManager.notify(notificationConfig.channel().channelId().hashCode(), notification);
        } else {
            Timber.e("Inconsistent notification status");
        }
    }

    public void onHostToDestructed() {
        notificationManager.cancel(notificationConfig.channel().channelId().hashCode());
    }

}