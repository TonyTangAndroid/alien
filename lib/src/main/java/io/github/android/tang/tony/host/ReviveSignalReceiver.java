package io.github.android.tang.tony.host;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import javax.inject.Inject;

import hugo.weaving.DebugLog;

@DebugLog
public class ReviveSignalReceiver extends BroadcastReceiver {

    @Inject
    Creator creator;

    @Override
    public void onReceive(Context context, Intent intent) {
        Host.get().hostComponent().inject(this);
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            creator.onDeviceRebooted();
        } else {
            throw new RuntimeException("Unsupported action : " + intent.getAction());
        }
    }
}