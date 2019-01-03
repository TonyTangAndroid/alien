package io.github.android.tang.tony.host;

import android.content.Context;

import hugo.weaving.DebugLog;

@DebugLog
public class DemoServiceStatusTracker implements ServiceStatusBroadcastReceiver.Callback {

    private boolean started;

    public DemoServiceStatusTracker(Context context) {
        ServiceStatusBroadcastReceiver.register(context, new ServiceStatusBroadcastReceiver(this));
    }

    public boolean started() {
        return started;
    }


    @Override
    public void onUpdate(boolean started) {
        this.started = started;
    }
}
