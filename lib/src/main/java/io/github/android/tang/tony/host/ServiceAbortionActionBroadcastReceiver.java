package io.github.android.tang.tony.host;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import hugo.weaving.DebugLog;

@DebugLog
public class ServiceAbortionActionBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        new SharedPreferenceHelper(context).update(false);
        context.stopService(HostService.constructDemoService(context));
    }

    public static Intent
    constructIntent(String applicationId) {
        Intent intent = new Intent(BuildConfig.ACTION_STOP_FOREGROUND_SERVICE);
        intent.setPackage(applicationId);//Must be set to support Android Oreo.
        return intent;
    }

}
