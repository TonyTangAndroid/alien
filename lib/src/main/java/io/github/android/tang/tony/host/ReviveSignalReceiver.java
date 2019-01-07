package io.github.android.tang.tony.host;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import hugo.weaving.DebugLog;

@DebugLog
public class ReviveSignalReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            Host.get().revive();
        } else {
            throw new RuntimeException("Unsupported action : " + intent.getAction());
        }
    }
}