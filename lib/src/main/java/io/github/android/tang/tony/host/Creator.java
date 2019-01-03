package io.github.android.tang.tony.host;

import android.content.Context;
import android.content.Intent;
import android.os.Build;

import javax.inject.Inject;

@HostScope
public class Creator {

    private final Context context;

    @Inject
    public Creator(Context context) {
        this.context = context;

    }

    public void destroy() {
        Intent hostIntent = conceive();
        context.stopService(hostIntent);
    }

    public void deliver() {
        context.startService(conceive());
    }

    public void revive() {
        if (selfReviveSupported()) {
            selfRevive();
        } else {
            reviveThroughHandOfGod();
        }
    }

    private void reviveThroughHandOfGod() {
        ParturitionService.kickOff(context, conceive());
    }

    private Intent conceive() {
        return HostService.constructHostIntent(context);
    }

    private void selfRevive() {
        context.startService(conceive());
    }

    private boolean selfReviveSupported() {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.O;
    }
}
