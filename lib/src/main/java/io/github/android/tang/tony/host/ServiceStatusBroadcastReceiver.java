package io.github.android.tang.tony.host;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import hugo.weaving.DebugLog;
import timber.log.Timber;

@DebugLog
public class ServiceStatusBroadcastReceiver extends BroadcastReceiver {

    private static final String EXTRA_STATUS = "extra_status";
    private final Callback callback;

    public ServiceStatusBroadcastReceiver(Callback callback) {
        this.callback = callback;
    }

    static void broadcast(Context context, boolean started) {
        Intent intent = new Intent(BuildConfig.ACTION_STOP_FOREGROUND_SERVICE).putExtra(EXTRA_STATUS, started);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    private void onStatusUpdated(boolean started) {
        log(started);
        callback.onUpdate(started);
    }

    private void log(boolean started) {
        Timber.d("ForegroundService started:%s", started);
    }

    public boolean started(Intent intent) {
        return intent.getBooleanExtra(EXTRA_STATUS, false);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        onStatusUpdated(started(intent));
    }

    public interface Callback {
        void onUpdate(boolean alive);
    }
}
