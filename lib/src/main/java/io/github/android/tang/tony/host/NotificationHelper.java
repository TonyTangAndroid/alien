package io.github.android.tang.tony.host;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import hugo.weaving.DebugLog;
import io.github.android.tang.tony.host.config.NotificationConfig;
import io.github.android.tang.tony.host.config.NotificationConfig.ActionConfig;
import javax.inject.Inject;
import javax.inject.Named;
import timber.log.Timber;

@DebugLog
class NotificationHelper {

  private final Context context;
  private final NotificationManager notificationManager;
  private final NotificationConfig notificationConfig;
  private final String applicationId;

  @Inject
  public NotificationHelper(
      Context context,
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
    return new NotificationChannel(config.id(), config.name(), config.importance());
  }

  private NotificationCompat.Builder buildOngoingNotification() {

    String title = notificationConfig.ui().title();
    String body = notificationConfig.ui().body();
    NotificationCompat.Builder builder =
        new NotificationCompat.Builder(context, notificationConfig.channel().id())
            .setContentTitle(title)
            .setContentText(body)
            .setContentIntent(launchPendingIntent())
            .setSmallIcon(notificationConfig.ui().smallIcon())
            .setAutoCancel(true);

    ActionConfig stop = notificationConfig.ui().destruct();
    builder.addAction(action(stop, Action.DESTRUCT));

    ActionConfig pause = notificationConfig.ui().deactivate();
    if (pause != null) {
      builder.addAction(action(pause, Action.DEACTIVATE));
    }
    return builder;
  }

  private NotificationCompat.Builder buildOnPausedNotification(ActionConfig resume) {

    String title = notificationConfig.ui().title();
    String body = resume.body();
    NotificationCompat.Builder builder =
        new NotificationCompat.Builder(context, notificationConfig.channel().id())
            .setContentTitle(title)
            .setContentText(body)
            .setContentIntent(launchPendingIntent())
            .setSmallIcon(notificationConfig.ui().smallIcon());

    builder.addAction(action(notificationConfig.ui().destruct(), Action.DESTRUCT));
    builder.addAction(action(resume, Action.ACTIVATE));
    return builder;
  }

  private NotificationCompat.Action action(ActionConfig config, @ActionType int action) {
    PendingIntent intent = actionIntent(action);
    return new NotificationCompat.Action(config.drawableId(), config.title(), intent);
  }

  private PendingIntent actionIntent(@ActionType int action) {
    Intent intent = HostMutator.constructIntent(applicationId, action);
    return PendingIntent.getBroadcast(context, action, intent, PendingIntent.FLAG_UPDATE_CURRENT);
  }

  private PendingIntent launchPendingIntent() {
    Intent intent = notificationConfig.launchIntent();
    return PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
  }

  private NotificationManager getNotificationManager() {
    return notificationManager;
  }

  public void showOngoingStatus(Service service) {
    Notification notification = buildOngoingNotification().build();
    service.startForeground(notificationConfig.channel().id().hashCode(), notification);
  }

  public void showSleepStatus() {
    ActionConfig resume = notificationConfig.ui().activate();
    if (resume != null) {
      Notification notification = buildOnPausedNotification(resume).build();
      notificationManager.notify(notificationConfig.channel().id().hashCode(), notification);
    } else {
      Timber.e("Inconsistent notification status");
    }
  }

  public void cancel() {
    notificationManager.cancel(notificationConfig.channel().id().hashCode());
  }
}
