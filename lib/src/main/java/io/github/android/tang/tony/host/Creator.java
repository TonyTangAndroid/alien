package io.github.android.tang.tony.host;

import android.content.Context;
import android.content.Intent;
import android.os.Build;

import javax.inject.Inject;

import timber.log.Timber;

@HostScope
public class Creator {

    private final Context context;
    private final SharedPreferenceHelper sharedPreferenceHelper;

    @Inject
    public Creator(Context context, SharedPreferenceHelper sharedPreferenceHelper) {
        this.context = context;
        this.sharedPreferenceHelper = sharedPreferenceHelper;
        mutate(sharedPreferenceHelper.enabled());
    }

    public void onDeviceRebooted() {
        if (selfReviveEnabled() && wasLiving()) {
            revive();
        } else {
            Timber.d("Service has not been enabled");
        }
    }

    private boolean wasLiving() {
        return sharedPreferenceHelper.enabled();
    }

    private boolean selfReviveEnabled() {
        return true;
    }

    public void destroy() {
        Intent hostIntent = conceive();
        context.stopService(hostIntent);
    }

    public void deliver() {
        context.startService(conceive());
    }

    public void toggleStatus() {
        boolean previousStatus = sharedPreferenceHelper.enabled();
        boolean newStatus = !previousStatus;
        mutate(newStatus);
        sharedPreferenceHelper.update(newStatus);
    }

    public void mutate(boolean newStatus) {
        if (newStatus) {
            deliver();
        } else {
            destroy();
        }
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
