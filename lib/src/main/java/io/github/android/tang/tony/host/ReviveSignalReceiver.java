package io.github.android.tang.tony.host;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import javax.inject.Inject;

import hugo.weaving.DebugLog;
import timber.log.Timber;

@DebugLog
public class ReviveSignalReceiver extends BroadcastReceiver {

    @Inject
    Creator creator;
    @Inject
    SharedPreferenceHelper preferenceHelper;

    @Override
    public void onReceive(Context context, Intent intent) {
        Host.get().hostComponent().inject(this);
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            onDeviceRebooted();
        } else {
            throw new RuntimeException("Unsupported action : " + intent.getAction());
        }
    }

    private void onDeviceRebooted() {
        if (selfReviveEnabled() && wasLiving()) {
            creator.revive();
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

}