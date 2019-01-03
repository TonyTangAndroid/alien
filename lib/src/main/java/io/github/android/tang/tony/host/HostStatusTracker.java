package io.github.android.tang.tony.host;

import android.content.Context;

import javax.inject.Inject;

@HostScope
public class HostStatusTracker implements ServiceStatusBroadcastReceiver.Callback {

    private boolean started;

    @Inject
    public HostStatusTracker(Context context) {
        Host.register(context, new ServiceStatusBroadcastReceiver(this));
    }

    public boolean alive() {
        return started;
    }


    @Override
    public void onUpdate(boolean alive) {
        this.started = alive;
    }
}
