package io.github.android.tang.tony.host;

import android.content.Context;
import android.content.Intent;
import javax.inject.Inject;

@HostScope
class Agent {

  private final Mother mother;
  private final Context context;
  private final HostStatusCache hostStatusCache;
  private final NotificationHelper notificationHelper;

  @Inject
  public Agent(
      Mother mother,
      Context context,
      HostStatusCache hostStatusCache,
      NotificationHelper notificationHelper) {
    this.mother = mother;
    this.context = context;
    this.hostStatusCache = hostStatusCache;
    this.notificationHelper = notificationHelper;
  }

  public void mutate(@ActionType int action) {
    switch (action) {
      case Action.DEACTIVATE:
        deactivate();
        break;
      case Action.ACTIVATE:
        activate();
        break;
      case Action.DESTRUCT:
        destruct();
        break;
    }
  }

  private void destruct() {
    hostStatusCache.update(HostStatusCache.InternalStatus.NONE);
    context.stopService(instance());
    notify(Status.NONE);
  }

  private void activate() {
    hostStatusCache.update(HostStatusCache.InternalStatus.ACTIVATED);
    mother.deliver();
    notify(Status.ACTIVATED);
  }

  private void deactivate() {
    hostStatusCache.update(HostStatusCache.InternalStatus.DEACTIVATED);
    context.stopService(instance());
    notify(Status.DEACTIVATED);
  }

  private void notify(@HostStatus int status) {
    switch (status) {
      case Status.ACTIVATED:
        break;
      case Status.DEACTIVATED:
        notificationHelper.showSleepStatus();
        break;
      case Status.NONE:
        notificationHelper.cancel();
        break;
    }
    HostStatusBroadcastReceiver.broadcast(context, status);
  }

  private HostStatusCache.InternalStatus status() {
    return hostStatusCache.status();
  }

  private Intent instance() {
    return HostService.constructHostIntent(context);
  }

  public void revive() {
    switch (status()) {
      case NONE:
        notify(Status.NONE);
        break;
      case DEACTIVATED:
        notify(Status.DEACTIVATED);
        break;
      case ACTIVATED:
        mother.deliver();
        break;
    }
  }
}
