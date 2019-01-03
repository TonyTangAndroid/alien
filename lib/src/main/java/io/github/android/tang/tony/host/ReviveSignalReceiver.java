package io.github.android.tang.tony.host;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import javax.inject.Inject;

import hugo.weaving.DebugLog;
import timber.log.Timber;

@DebugLog
public class ReviveSignalReceiver extends BroadcastReceiver {

    @Inject
    SharedPreferenceHelper preferenceHelper;

    @Override
    public void onReceive(Context context, Intent intent) {
        Host.get().hostComponent().inject(this);
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            onDeviceRebooted(context);
        } else {
            throw new RuntimeException("Unsupported action : " + intent.getAction());
        }
    }

    private void onDeviceRebooted(Context context) {
        if (selfReviveEnabled() && wasLiving()) {
            revive(context);
        } else {
            Timber.d("Service has not been enabled");
        }
    }

    private boolean wasLiving() {
        return preferenceHelper.enabled();
    }

    private boolean selfReviveEnabled() {
        return true;
    }

    private void revive(Context context) {
        Intent hostToBeBorn = HostService.constructHostIntent(context);
        if (selfReviveSupported()) {
            selfRevive(context, hostToBeBorn);
        } else {
            reviveThroughHandOfGod(context, hostToBeBorn);
        }
    }

    private void reviveThroughHandOfGod(Context context, Intent hostToBeBorn) {
        ParturitionService.kickOff(context, hostToBeBorn);
    }

    private void selfRevive(Context context, Intent serviceToBeStarted) {
        context.startService(serviceToBeStarted);
    }

    private boolean selfReviveSupported() {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.O;
    }
}