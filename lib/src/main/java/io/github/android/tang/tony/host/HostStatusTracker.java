package io.github.android.tang.tony.host;

import javax.inject.Inject;

@HostScope
public class HostStatusTracker implements ServiceStatusBroadcastReceiver.Callback {

    private boolean started;

    @Inject
    public HostStatusTracker() {
        Host.get().register(this);
    }

    public boolean alive() {
        return started;
    }


    @Override
    public void onUpdate(boolean alive) {
        this.started = alive;
    }
}
