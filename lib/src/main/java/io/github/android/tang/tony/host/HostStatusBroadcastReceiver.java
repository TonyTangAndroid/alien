package io.github.android.tang.tony.host;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import hugo.weaving.DebugLog;
import timber.log.Timber;

@DebugLog
public class HostStatusBroadcastReceiver extends BroadcastReceiver {

    private static final String EXTRA_STATUS = "extra_status";
    private final HostMutationCallback callback;

    public HostStatusBroadcastReceiver(HostMutationCallback callback) {
        this.callback = callback;
    }

    static void broadcast(Context context, @HostStatus int status) {
        Intent intent = new Intent(BuildConfig.ACTION_MUTATE_HOST).putExtra(EXTRA_STATUS, status);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    private void onStatusUpdated(@HostStatus int status) {
        log(status);
        callback.onMutate(status);
    }

    private void log(@HostStatus int status) {
        Timber.d("Host status :%s", status);
    }

    @HostStatus
    public int status(Intent intent) {
        return intent.getIntExtra(EXTRA_STATUS, Status.NONE);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        onStatusUpdated(status(intent));
    }

}
