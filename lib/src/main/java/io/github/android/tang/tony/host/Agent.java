package io.github.android.tang.tony.host;

import android.content.Context;
import android.content.Intent;

import javax.inject.Inject;

@HostScope
public class Agent {

    private final Mother mother;
    private final Context context;
    private final HostStatusCache hostStatusCache;
    private final NotificationHelper notificationHelper;

    @Inject
    public Agent(Mother mother,
                 Context context,
                 HostStatusCache hostStatusCache,
                 NotificationHelper notificationHelper) {
        this.mother = mother;
        this.context = context;
        this.hostStatusCache = hostStatusCache;
        this.notificationHelper = notificationHelper;
    }


    public void activate() {
        hostStatusCache.update(HostStatusCache.InternalStatus.ACTIVATED);
        mother.deliver();
        HostStatusBroadcastReceiver.broadcast(context, Status.ALIVE);
    }

    public void sleep() {
        hostStatusCache.update(HostStatusCache.InternalStatus.ON_CALL);
        context.stopService(instance());
        notificationHelper.showSleepStatus();
        HostStatusBroadcastReceiver.broadcast(context, Status.SLEEP);
    }

    public void destruct() {
        hostStatusCache.update(HostStatusCache.InternalStatus.NONE);
        context.stopService(instance());
        notificationHelper.cancel();
        HostStatusBroadcastReceiver.broadcast(context, Status.NONE);
    }

    public HostStatusCache.InternalStatus status() {
        return hostStatusCache.status();
    }

    private Intent instance() {
        return HostService.constructHostIntent(context);
    }


    public void revive() {
        mother.deliver();
    }
}
