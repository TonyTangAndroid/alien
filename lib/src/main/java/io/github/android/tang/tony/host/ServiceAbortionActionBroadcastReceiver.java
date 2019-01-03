package io.github.android.tang.tony.host;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import javax.inject.Inject;

import hugo.weaving.DebugLog;

@DebugLog
public class ServiceAbortionActionBroadcastReceiver extends BroadcastReceiver {

    @Inject
    Creator creator;
    @Inject
    SharedPreferenceHelper sharedPreferenceHelper;

    @Override
    public void onReceive(Context context, Intent intent) {
        Host.get().hostComponent().inject(this);
        creator.destruct();
    }

    public static Intent constructIntent(String applicationId) {
        Intent intent = new Intent(BuildConfig.ACTION_STOP_FOREGROUND_SERVICE);
        intent.setPackage(applicationId);//Must be set to support Android Oreo.
        return intent;
    }

}
