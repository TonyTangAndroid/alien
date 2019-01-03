package io.github.android.tang.tony.host;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import hugo.weaving.DebugLog;
import timber.log.Timber;

@DebugLog
public class DeviceRebootedBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            onDeviceRebooted(context);
        } else {
            throw new RuntimeException("Unsupported action : " + intent.getAction());
        }
    }

    private void onDeviceRebooted(Context context) {
        if (new SharedPreferenceHelper(context).enabled()) {
            startService(context);
        } else {
            Timber.d("Service has not been enabled");
        }
    }

    private void startService(Context context) {
        Intent serviceToBeStarted = DemoService.constructDemoService(context);
        if (supportToStartForegroundService()) {
            startServiceDirectly(context, serviceToBeStarted);
        } else {
            startServiceThroughJobScheduler(context, serviceToBeStarted);
        }
    }

    private void startServiceThroughJobScheduler(Context context, Intent serviceToBeStarted) {
        DeviceRebootedJobSchedulerService.enqueueWork(context, serviceToBeStarted);
    }

    private void startServiceDirectly(Context context, Intent serviceToBeStarted) {
        context.startService(serviceToBeStarted);
    }

    private boolean supportToStartForegroundService() {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.O;
    }
}