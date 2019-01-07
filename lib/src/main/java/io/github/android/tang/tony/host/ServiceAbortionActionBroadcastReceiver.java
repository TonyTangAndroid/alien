package io.github.android.tang.tony.host;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import javax.inject.Inject;

import hugo.weaving.DebugLog;

@DebugLog
public class ServiceAbortionActionBroadcastReceiver extends BroadcastReceiver {

    private static final String EXTRA_ACTION_TYPE = "extra_action_type";
    @Inject
    Agent agent;
    @Inject
    HostStatusPersister sharedPreferenceHelper;

    public static Intent constructIntent(String applicationId, @ActionType int action) {
        Intent intent = new Intent(BuildConfig.ACTION_STOP_FOREGROUND_SERVICE);
        intent.putExtra(EXTRA_ACTION_TYPE, action);
        intent.setPackage(applicationId);//Must be set to support Android Oreo.
        return intent;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Host.get().hostComponent().inject(this);
        onActionTriggered(intent.getIntExtra(EXTRA_ACTION_TYPE, Action.STOP));
    }

    private void onActionTriggered(@ActionType int action) {
        switch (action) {
            case Action.PAUSE:
                agent.sleep();
                break;
            case Action.RESUME:
                agent.activate();
                break;
            case Action.STOP:
                agent.destruct();
                break;
        }
    }

}
