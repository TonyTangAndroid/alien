package org.altbeacon.beacon.demo.monitor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import hugo.weaving.DebugLog;
import io.github.android.tang.tony.host.Alien;
import io.github.android.tang.tony.host.Host;

@DebugLog
public class RebootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            Host.get().activateAlien(beaconAlien(context));
        } else {
            throw new RuntimeException("Unsupported action : " + intent.getAction());
        }
    }

    private Alien beaconAlien(Context context) {
        return ((App) context.getApplicationContext()).beaconAlien();
    }
}